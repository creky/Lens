/**
 * @Project: Test
 * @Title: Article.java
 * @Package: com.vo
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午2:25:08
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.vo;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * @Classname: Article
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午2:25:08
 * @Version:
 */
@Table(name="article")
public class Article
{
    @Id
	@Column(column="articleId")
	private Integer articleId;
	
	@Column(column="proId")
	private Integer proId;
	
	@Column(column="title")
	private String title;
	
	@Column(column="author")
	private String author;
	
	@Column(column="summaryCont")
	private String summaryCont;
	
	@Column(column="detailCont")
	private String detailCont;
	
	@Column(column="detailUrl")
	private String detailUrl;
	
	@Column(column="mainPic")
	private String mainPic;
	
	@Column(column="publishTime")
	private Date publishTime;
	
	public Integer getArticleId()
	{
		return articleId;
	}
	public void setArticleId(Integer articleId)
	{
		this.articleId = articleId;
	}
	public Integer getProId()
	{
		return proId;
	}
	public void setProId(Integer proId)
	{
		this.proId = proId;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getAuthor()
	{
		return author;
	}
	public void setAuthor(String author)
	{
		this.author = author;
	}
	public String getSummaryCont()
	{
		return summaryCont;
	}
	public void setSummaryCont(String summaryCont)
	{
		this.summaryCont = summaryCont;
	}
	public String getDetailCont()
	{
		return detailCont;
	}
	public void setDetailCont(String detailCont)
	{
		this.detailCont = detailCont;
	}
	public String getDetailUrl()
	{
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl)
	{
		this.detailUrl = detailUrl;
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
		builder.append("Article [articleId=").append(articleId).append(", proId=").append(proId).append(", title=")
				.append(title).append(", author=").append(author).append(", summaryCont=").append(summaryCont)
				.append(", detailCont=").append(detailCont).append(", detailUrl=").append(detailUrl)
				.append(", mainPic=").append(mainPic).append(", publishTime=").append(publishTime).append("]");
		return builder.toString();
	}

}
