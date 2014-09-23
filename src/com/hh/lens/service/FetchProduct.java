/**
 * @Project: Test
 * @Title: FetchProduct.java
 * @Package: com.zol
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午12:05:57
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.service;

import android.util.Log;
import com.hh.lens.AppContext;
import com.hh.lens.Constant;
import com.hh.lens.util.ClientUtil;
import com.hh.lens.util.StringUtils;
import com.hh.lens.vo.*;
import com.lidroid.xutils.DbUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname: FetchProduct
 * @Description:
 * @Author: hh
 * @Date: 2013-1-27 下午12:05:57
 * @Version:
 */
public class FetchProduct
{
    private final static String LATEST_LENS_URL="http://detail.zol.com.cn/lens/new.html";
    private static DbUtils db=AppContext.db;

    public static void fetchLatestLens(){
        try {
            fetchLensList(LATEST_LENS_URL);
        } catch (Exception e) {
            Log.e(Constant.COMM_PREFIX,"Fetch lens error !",e);
        }
    }

	public static void fetchLensList(String lastUrl) throws Exception
	{
		Log.d("正在获取地址：{}", lastUrl);
		String html = ClientUtil.get(getFullUrl(lastUrl));
		Document doc=Jsoup.parse(html);
		Elements eles = doc.select(".list-item");
		for (Element item : eles)
		{
           Elements priceType=item.select(".price-type");
            if(priceType!=null&&priceType.text().contains("概念产品")){
                continue;
            }
            Elements es=item.select(".pro-intro h3 a");
            if(es==null||es.isEmpty()){
                continue;
            }
			String pageUrl = es.get(0).attr("href");
			analyzePage(pageUrl);
			Thread.sleep(2000);
		}
		
		String nextUrl=doc.select(".pagebar a.next").attr("href");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lastUrl", nextUrl);
		//sqlSession.update("updateLastUrl", map);
		Thread.sleep(3000);
		
		if(StringUtils.isNotEmpty(nextUrl)){
            fetchLensList(nextUrl);
		}
	}

	public static void fetchPageList() throws Exception
	{
		int pageCount = 26;
		int startPage = 0;
		Integer page = 0;//sqlSession.selectOne("getRecordById");
		if (page > 0)
		{
			startPage = page;
		}

		for (int i = startPage; i < pageCount; i++)
		{
			String html = ClientUtil.get(Constant.BASE_URL + "lens/" + i + ".html");
			Document doc = Jsoup.parse(html);
			Elements eles = doc.select(".list-item .pro-intro h3 a");
			for (Element e : eles)
			{
				String pageUrl = e.attr("href");
				analyzePage(pageUrl);
				Thread.sleep(2000);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", i);
//			sqlSession.update("updateRecord", map);
			Thread.sleep(5000);
			// return;
		}
	}

	/**
	 * 分析产品列表
	 * 
	 * @Methodname: analyzePage
	 * @Discription:
	 * @param pageUrl
	 * @throws Exception
	 * @Author HH
	 * @Time 2013-1-27 下午12:35:57
	 */
	private static void analyzePage(String pageUrl) throws Exception
	{
		Log.d(Constant.COMM_PREFIX,"分析单个产品");
		Product product = new Product();
		try
		{
			product.setSummaryUrl(pageUrl);
			product.setUpdateTime(new Date());
			Pattern pattern = Pattern.compile("index(\\d{1,10})\\.shtml");
			Matcher matcher = pattern.matcher(pageUrl);
			if (matcher.find())
			{
				product.setProId(Integer.parseInt(matcher.group(1)));
			}

            if(product.getProId()!=null) {
                product = db.findById(Product.class, product.getProId());
                if (product != null) {
                    Log.i(Constant.COMM_PREFIX, "ID为 " + product.getProId() + " 的已经有记录，跳过！");
                    return;
                }
            }

			String html = ClientUtil.get(getFullUrl(pageUrl));
			Document doc = Jsoup.parse(html);
			Elements eles = doc.select("#tagNav .nav li");
			if (eles.size() > 0)
			{
				product.setPriceUrl(eles.select("a:contains(报价)").attr("href"));
				product.setParamUrl(eles.select("a:contains(参数)").attr("href"));
				product.setPicUrl(eles.select("a:contains(图片)").attr("href"));
				product.setArticleUrl(eles.select("a:contains(评测)").attr("href"));
				product.setBbsUrl(eles.select("a:contains(论坛)").attr("href"));
				product.setReviewUrl(eles.select("a:contains(点评)").attr("href"));
				product.setSaleUrl(eles.select("a:contains(促销)").attr("href"));
				product.setFittingUrl(eles.select("a:contains(配件)").attr("href"));
				product.setOnlineUrl(eles.select("a:contains(购买)").attr("href"));
				product.setName(doc.select(".ptitle h1").text());
				String imgUrl = doc.select("#zoom1 img").attr("src");
				if (imgUrl.indexOf("/no_pic/") == -1)
				{
					String imgPath = ClientUtil.getAndSaveFile(getFullUrl(imgUrl), null, Constant.PIC_FOLDER, product.getProId()
							.toString(), null);
					product.setMainPic(imgPath);
				}

				Attribute attr = new Attribute();
				attr.setAttrCata("综合");
				attr.setProId(product.getProId());
				// 参考价格
				//attr.setAttrId(UniqueIdUtil.genId(Attribute.class));
				attr.setAttrName(doc.select(".main .product-price-info strong").get(0).ownText().replace("：", ""));
				String pStr = doc.select(".main .price-type").text();
				attr.setAttrValue(pStr);
                db.save(attr);

				Integer price = 0;
				if (pStr.contains("万"))
				{
					pStr = pStr.replace("￥", "").replace("万", "");
					price = Float.valueOf((Float.parseFloat(pStr) * 10000)).intValue();
				}
				else
				{
					pStr = pStr.replace("￥", "").replace("万", "");
					try
					{
						price=Integer.parseInt(pStr);
					}
					catch (Exception e)
					{
						price=0;
					}
				}
				product.setPrice(price);
                db.save(product);

				// 商家报价
//				attr.setAttrId(UniqueIdUtil.genId(Attribute.class));
				attr.setAttrName(doc.select(".main .product-merchant-price strong").text().replace("：", ""));
				attr.setAttrValue(doc.select(".main .product-merchant-price em a:contains(￥)").text());
                db.save(attr);

                Elements b2cPrice=doc.select(".main .product-b2c-price li");
                if(b2cPrice!=null&&!b2cPrice.isEmpty()){
                    for(Element e:b2cPrice){
                        String name=e.select("strong a").get(0).ownText();
                        String val=e.select(">span").text();
                        attr.setAttrName(name);
                        attr.setAttrValue(val);
                        db.save(attr);
                    }
                }

				Thread.sleep(1000);
				analyzeParam(product);
				Thread.sleep(1000);
				analyzePic(product);
				Thread.sleep(1000);
				analyzeArticle(product);
				Thread.sleep(1000);
				analyzeReview(product);
			}
		}
		catch (Exception e)
		{
			Log.e(Constant.COMM_PREFIX,"分析该产品失败："+ product, e);
		}
	}

	/**
	 * 获取点评
	 * 
	 * @Methodname: analyzeReview
	 * @Discription:
	 * @param product
	 * @throws Exception
	 * @Author HH
	 * @Time 2013-1-27 下午7:48:04
	 */
	private static void analyzeReview(Product product) throws Exception
	{
		try
		{
			String html = ClientUtil.get(getFullUrl(product.getReviewUrl()));
			Document doc = Jsoup.parse(html);
			Elements eles = doc.select(".comments-list>li.list-item");
			for (Element ele : eles)
			{
				if (ele.hasClass("hide")||ele.attr("id").equals("selfRew")||ele.attr("style").contains("display: none")) continue;
				if(ele.text().contains("以下点评为同系列产品点评"))continue;
				
				Review review = new Review();
//				review.setReviewId(UniqueIdUtil.genId(Review.class));
				review.setProId(product.getProId());
				String dtStr = ele.select(".comments-date").text().trim();
				
				Date dt;
				if (StringUtils.isNotEmpty(dtStr))
				{
					dt = new SimpleDateFormat("yyyy-MM-dd").parse(dtStr);
				}
				else
				{
					dt = new Date();
				}
				review.setPublishTime(dt);
				
				review.setTitle(ele.select(".comments-content h3").text());
                Elements goodEles=ele.select(".comment-item:has(.good) p>span");
                if(goodEles!=null&&!goodEles.isEmpty()){
                    String cmt=goodEles.get(0).text();
                    if(goodEles.size()>1){
                        cmt+=goodEles.get(1).attr("data-text");
                    }
                    review.setGoodPoint(cmt);
                }
                Elements badEles=ele.select(".comment-item:has(.bad) p>span");
                if(badEles!=null&&!badEles.isEmpty()){
                    String cmt=badEles.get(0).text();
                    if(badEles.size()>1){
                        cmt+=badEles.get(1).attr("data-text");
                    }
                    review.setBadPoint(cmt);
                }
                Elements sumEles=ele.select(".comment-item:has(.summary) p>span");
                if(sumEles!=null&&!sumEles.isEmpty()){
                    String cmt=sumEles.get(0).text();
                    if(sumEles.size()>1){
                        cmt+=sumEles.get(1).attr("data-text");
                    }
                    review.setReviewCont(cmt);
                }
				review.setAuthor(ele.select(".comments-user .comments-user-name").text());
				
				String score=ele.select(".single-score .score strong").text();
                review.setScore(Float.parseFloat(score));

				String agree=ele.select(".J_usefulNum").text();
				review.setAgree(Integer.parseInt(StringUtils.isEmpty(agree)?"0":agree));
//				String oppose=ele.select(".feed_box .isok span[id^=no_]").text();
//				review.setOppose(Integer.parseInt(StringUtils.isEmpty(oppose)?"0":oppose));
				
				if (!(StringUtils.isEmpty(review.getGoodPoint()) && StringUtils.isEmpty(review.getBadPoint()) && StringUtils
						.isEmpty(review.getReviewCont())))
				{
					db.save(review);
				}
			}

			if (product.getReviewUrl().endsWith("/review.shtml"))
			{
				analyzeComment(product, doc);
			}
			//sqlSession.commit();

			// 递归处理下一页
//			String nextPageUrl = doc.select(".pagebar a:contains(下一页)").attr("href");
//			if (StringUtils.isNotEmpty(nextPageUrl))
//			{
//				product.setReviewUrl(nextPageUrl);
//				analyzeReview(product);
//			}

		}
		catch (Exception e)
		{
			Log.e(Constant.COMM_PREFIX, "分析点评页面出错,产品信息为：" + product, e);
			throw e;
		}
	}

	/**
	 * 分析平均评论
	 * 
	 * @Methodname: analyzeComment
	 * @Discription:
	 * @param product
	 * @param doc
	 * @throws Exception
	 * @Author HH
	 * @Time 2013-1-27 下午11:21:48
	 */
	private static void analyzeComment(Product product, Document doc) throws Exception
	{
		try
		{
			if (doc.select(".nocomment").text().length() > 0) return;
			Comment comment = new Comment();
			comment.setProId(product.getProId());
			comment.setScore(Float.parseFloat(doc.select("#totalPoint .score strong").text()));
			Elements goods = doc.select(".good-words li");
			comment.setCommentCata("优点");

			StringBuffer sb = new StringBuffer();
			for (Element e : goods)
			{
				sb.append(e.text()).append("<br/>");
			}
			String cont = null;
			if (sb.length() >= 5)
			{
				cont = sb.delete(sb.length() - 5, sb.length()).toString();
			}
			comment.setCommentCont(cont);

//			String ok_good = doc.select("#ok_good").text();
//			comment.setAgree(Integer.parseInt(StringUtils.isEmpty(ok_good) ? "0" : ok_good));
//			String no_good = doc.select("#no_good").text();
//			comment.setOppose(Integer.parseInt(StringUtils.isEmpty(no_good) ? "0" : no_good));
//			comment.setCommentId(UniqueIdUtil.genId(Comment.class));
			db.save(comment);

			Elements bads = doc.select(".bad-words li");
			comment.setCommentCata("缺点");
			for (Element e : bads)
			{
				sb.append(e.text()).append("<br/>");
			}

			cont = null;
			if (sb.length() >= 5)
			{
				cont = sb.delete(sb.length() - 5, sb.length()).toString();
			}
			comment.setCommentCont(cont);

//			String ok_bad = doc.select("#ok_bad").text();
//			comment.setAgree(Integer.parseInt(StringUtils.isEmpty(ok_bad) ? "0" : ok_bad));
//			String no_bad = doc.select("#no_bad").text();
//			comment.setOppose(Integer.parseInt(StringUtils.isEmpty(no_bad) ? "0" : no_bad));
//			comment.setCommentId(UniqueIdUtil.genId(Comment.class));
			db.save(comment);
			//sqlSession.commit();
		}
		catch (Exception e)
		{
			Log.e(Constant.COMM_PREFIX, "分析综合评论页面出错,产品信息为：" + product, e);
			throw e;
		}
	}

	/**
	 * 获取评测文章
	 * 
	 * @Methodname: analyzeArticle
	 * @Discription:
	 * @param product
	 * @throws Exception
	 * @Author HH
	 * @Time 2013-1-27 下午7:48:01
	 */
	private static void analyzeArticle(Product product) throws Exception
	{
		try
		{
			String html = ClientUtil.get(getFullUrl(product.getArticleUrl()));
			Document doc = Jsoup.parse(html);
			Elements eles = doc.select(".article_list li");

			Article article = new Article();
			article.setProId(product.getProId());

			for (Element ele : eles)
			{
//				article.setArticleId(UniqueIdUtil.genId(Article.class));
				article.setTitle(ele.select(".title h4").text());
				article.setDetailUrl(ele.select(".title h4 a").attr("href"));
				String dt = ele.select(".title p .date").text();
				article.setPublishTime((new SimpleDateFormat("yyyy-MM-dd").parse(dt)));
				article.setAuthor(ele.select(".title p").get(0).ownText().replace("作者：", "").trim());
				article.setSummaryCont(ele.select(".summary").get(0).ownText().trim());
//				String imgUrl = ele.select(".pic img").attr("src");
//				String path = ClientUtil.getAndSaveFile(getFullUrl(imgUrl), null, Constant.PIC_FOLDER, product.getProId()
//						.toString(), null);
//				article.setMainPic(path);
				if (StringUtils.isNotEmpty(article.getSummaryCont()) && StringUtils.isNotEmpty(article.getTitle()))
				{
					db.save(article);
				}
				//sqlSession.commit();
			}
		}
		catch (Exception e)
		{
			Log.e(Constant.COMM_PREFIX,"分析评测页面出错,产品信息为："+ product, e);
			throw e;
		}
	}

	/**
	 * 获取图片
	 * 
	 * @Methodname: analyzePic
	 * @Discription:
	 * @param product
	 * @throws Exception
	 * @Author HH
	 * @Time 2013-1-27 下午7:47:59
	 */
	private static void analyzePic(Product product) throws Exception
	{
		try
		{
			String html = ClientUtil.get(getFullUrl(product.getPicUrl()));
			Document doc = Jsoup.parse(html);

			Pic pic = new Pic();
			pic.setProId(product.getProId());

			Elements eles = doc.select("#picList .pic_group");
			Elements picList = doc.select("#picList .piclist");
			for (int i = 0; i < eles.size(); i++)
			{
				pic.setPicCata(eles.get(i).select(".pic_type").text());
				Elements pics = picList.get(i).select("li");
				for (Element p : pics)
				{
//					pic.setPicId(UniqueIdUtil.genId(Pic.class));

					String picUrl=p.select("a img").attr(".src");
                    pic.setPicUrl(picUrl.replaceAll("_\\d{3}x\\d{3}",""));
                    pic.setPicName(p.select("a span").text());
                    if(StringUtils.isEmpty(picUrl)||StringUtils.isEmpty(pic.getPicName())) {
                        String bigPicHtml = ClientUtil.get(getFullUrl(p.select("a").attr("href")));
                        Document dc = Jsoup.parse(bigPicHtml);
                        pic.setPicUrl(dc.select("#bigImage .state img").attr("src"));
                        pic.setPicName(dc.select("#bigImage .state img").attr("alt"));
                    }

					String path = ClientUtil.getAndSaveFile(getFullUrl(pic.getPicUrl()), null, Constant.PIC_FOLDER, product
							.getProId().toString(), null);
					pic.setPicPath(path);
					if (StringUtils.isNotEmpty(pic.getPicPath()) && StringUtils.isNotEmpty(pic.getPicUrl())
							&& StringUtils.isNotEmpty(pic.getPicName()))
					{
						db.save(pic);
					}
				}
			}
			//sqlSession.commit();
		}
		catch (Exception e)
		{
			Log.e(Constant.COMM_PREFIX, "分析图片页面出错,产品信息为：" + product, e);
			throw e;
		}

	}

	/**
	 * 分析参数
	 * 
	 * @Methodname: analyzeParam
	 * @Discription:
	 * @param product
	 * @throws Exception
	 * @Author HH
	 * @Time 2013-1-27 下午7:47:55
	 */
	private static void analyzeParam(Product product) throws Exception
	{
		try
		{
			String html = ClientUtil.get(getFullUrl(product.getParamUrl()));
			Document doc = Jsoup.parse(html);
			Elements eles = doc.select("#newTb tr");

			Attribute at = new Attribute();
			at.setProId(product.getProId());

			for (Element ele : eles)
			{
				at.setAttrCata(ele.select("th").text());
				Elements attrs = ele.select(".param_content li");
				for (Element attr : attrs)
				{
//					at.setAttrId(UniqueIdUtil.genId(Attribute.class));
					at.setAttrName(attr.select("span[id^=newPmName_]").text());
					at.setAttrValue(attr.select("span[id^=newPmVal_]").text());
					if (StringUtils.isNotEmpty(at.getAttrName()) && StringUtils.isNotEmpty(at.getAttrValue()))
					{
						db.save(at);
					}
				}
			}
			//sqlSession.commit();
		}
		catch (Exception e)
		{
			Log.e(Constant.COMM_PREFIX, "分析参数页面出错,产品信息为：" + product, e);
			throw e;
		}
	}

	/**
	 * 返回完整路径
	 * 
	 * @Methodname: getFullUrl
	 * @Discription:
	 * @param url
	 * @return
	 * @Author HH
	 * @Time 2013-1-27 下午7:00:02
	 */
	private static String getFullUrl(String url)
	{
		return url.startsWith("http://") ? url : Constant.BASE_URL + url;
	}

}
