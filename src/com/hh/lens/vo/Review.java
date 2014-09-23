/**
 * @Project: Test
 * @Title: Review.java
 * @Package: com.vo
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午2:21:31
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.vo;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;


/**
 * @Classname: Review
 * @Description:
 * @Author: hh
 * @Date: 2013-1-27 下午2:21:31
 * @Version:
 */
@Table(name="review")
public class Review
{
    @Id
	@Column(column = "reviewId")
	private Integer reviewId;

	@Column(column = "proId")
	private Integer proId;

	@Column(column = "score")
	private Float score;

	@Column(column = "title")
	private String title;

	@Column(column = "goodPoint")
	private String goodPoint;

	@Column(column = "badPoint")
	private String badPoint;

	@Column(column = "reviewCont")
	private String reviewCont;

	@Column(column = "author")
	private String author;

	@Column(column = "agree")
	private Integer agree;

	@Column(column = "oppose")
	private Integer oppose;

	@Column(column = "publishTime")
	private Date publishTime;

	
	
	
	public Integer getReviewId()
	{
		return reviewId;
	}

	public void setReviewId(Integer reviewId)
	{
		this.reviewId = reviewId;
	}

	public Integer getProId()
	{
		return proId;
	}

	public void setProId(Integer proId)
	{
		this.proId = proId;
	}

	public Float getScore()
	{
		return score;
	}

	public void setScore(Float score)
	{
		this.score = score;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getGoodPoint()
	{
		return goodPoint;
	}

	public void setGoodPoint(String goodPoint)
	{
		this.goodPoint = goodPoint;
	}

	public String getBadPoint()
	{
		return badPoint;
	}

	public void setBadPoint(String badPoint)
	{
		this.badPoint = badPoint;
	}

	public String getReviewCont()
	{
		return reviewCont;
	}

	public void setReviewCont(String reviewCont)
	{
		this.reviewCont = reviewCont;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public Integer getAgree()
	{
		return agree;
	}

	public void setAgree(Integer agree)
	{
		this.agree = agree;
	}

	public Integer getOppose()
	{
		return oppose;
	}

	public void setOppose(Integer oppose)
	{
		this.oppose = oppose;
	}

	public Date getPublishTime()
	{
		return publishTime;
	}

	public void setPublishTime(Date publishTime)
	{
		this.publishTime = publishTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @Title: toString
	 * 
	 * @Description:
	 * 
	 * @return
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Review [reviewId=").append(reviewId).append(", proId=").append(proId).append(", score=")
				.append(score).append(", title=").append(title).append(", goodPoint=").append(goodPoint)
				.append(", badPoint=").append(badPoint).append(", reviewCont=").append(reviewCont).append(", author=")
				.append(author).append(", agree=").append(agree).append(", oppose=").append(oppose)
				.append(", publishTime=").append(publishTime).append("]");
		return builder.toString();
	}
}
