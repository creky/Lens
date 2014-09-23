/**
 * @Project: Test
 * @Title: Lens.java
 * @Package: com.vo
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午12:47:02
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.vo;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;


/**
 * @Classname: Lens
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午12:47:02
 * @Version:
 */
@Table(name="product")
public class Product
{
    @Id
	@Column(column = "proId")
	private Integer proId;

	@Column(column = "name")
	private String name;

	@Column(column = "price")
	private Integer price;

	@Column(column = "summaryUrl")
	private String summaryUrl;

	@Column(column = "priceUrl")
	private String priceUrl;

	@Column(column = "paramUrl")
	private String paramUrl;

	@Column(column = "picUrl")
	private String picUrl;

	@Column(column = "articleUrl")
	private String articleUrl;

	@Column(column = "bbsUrl")
	private String bbsUrl;

	@Column(column = "reviewUrl")
	private String reviewUrl;

	@Column(column = "saleUrl")
	private String saleUrl;

	@Column(column = "fittingUrl")
	private String fittingUrl;

	@Column(column = "onlineUrl")
	private String onlineUrl;

	@Column(column = "mainPic")
	private String mainPic;

	@Column(column = "publishTime")
	private Date publishTime;

	@Column(column = "updateTime")
	private Date updateTime;
	
	

	public Integer getProId()
	{
		return proId;
	}
	public void setProId(Integer proId)
	{
		this.proId = proId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getSummaryUrl()
	{
		return summaryUrl;
	}
	public void setSummaryUrl(String summaryUrl)
	{
		this.summaryUrl = summaryUrl;
	}
	public String getPriceUrl()
	{
		return priceUrl;
	}
	public void setPriceUrl(String priceUrl)
	{
		this.priceUrl = priceUrl;
	}
	public String getParamUrl()
	{
		return paramUrl;
	}
	public void setParamUrl(String paramUrl)
	{
		this.paramUrl = paramUrl;
	}
	public String getPicUrl()
	{
		return picUrl;
	}
	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}
	public String getArticleUrl()
	{
		return articleUrl;
	}
	public void setArticleUrl(String articleUrl)
	{
		this.articleUrl = articleUrl;
	}
	public String getBbsUrl()
	{
		return bbsUrl;
	}
	public void setBbsUrl(String bbsUrl)
	{
		this.bbsUrl = bbsUrl;
	}
	public String getReviewUrl()
	{
		return reviewUrl;
	}
	public void setReviewUrl(String reviewUrl)
	{
		this.reviewUrl = reviewUrl;
	}
	public String getSaleUrl()
	{
		return saleUrl;
	}
	public void setSaleUrl(String saleUrl)
	{
		this.saleUrl = saleUrl;
	}
	public String getFittingUrl()
	{
		return fittingUrl;
	}
	public void setFittingUrl(String fittingUrl)
	{
		this.fittingUrl = fittingUrl;
	}
	public String getOnlineUrl()
	{
		return onlineUrl;
	}
	public void setOnlineUrl(String onlineUrl)
	{
		this.onlineUrl = onlineUrl;
	}
	public String getMainPic()
	{
		return mainPic;
	}
	public void setMainPic(String mainPic)
	{
		this.mainPic = mainPic;
	}
	public Date getPublishTime()
	{
		return publishTime;
	}
	public void setPublishTime(Date publishTime)
	{
		this.publishTime = publishTime;
	}
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	public Integer getPrice()
	{
		return price;
	}
	public void setPrice(Integer price)
	{
		this.price = price;
	}
	/* (non-Javadoc)
	 * @Title: toString
	 * @Description: 
	 * @return
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Product [proId=").append(proId).append(", name=").append(name).append(", price=").append(price)
				.append(", summaryUrl=").append(summaryUrl).append(", priceUrl=").append(priceUrl)
				.append(", paramUrl=").append(paramUrl).append(", picUrl=").append(picUrl).append(", articleUrl=")
				.append(articleUrl).append(", bbsUrl=").append(bbsUrl).append(", reviewUrl=").append(reviewUrl)
				.append(", saleUrl=").append(saleUrl).append(", fittingUrl=").append(fittingUrl).append(", onlineUrl=")
				.append(onlineUrl).append(", mainPic=").append(mainPic).append(", publishTime=").append(publishTime)
				.append(", updateTime=").append(updateTime).append("]");
		return builder.toString();
	}
	
	
}
