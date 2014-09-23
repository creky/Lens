/**
 * @Project: Test
 * @Title: Comment.java
 * @Package: com.vo
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午2:23:56
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.vo;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @Classname: Comment
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午2:23:56
 * @Version:
 */
@Table(name="comment")
public class Comment
{
    @Id
	@Column(column="commentId")
	private Integer commentId;
	
	@Column(column="proId")
	private Integer proId;
	
	@Column(column="score")
	private Float score;
	
	@Column(column="commentCata")
	private String commentCata;
	
	@Column(column="commentCont")
	private String commentCont;
	
	@Column(column="agree")
	private Integer agree;
	
	@Column(column="oppose")
	private Integer oppose;
	
	
	
	public Integer getCommentId()
	{
		return commentId;
	}
	public void setCommentId(Integer commentId)
	{
		this.commentId = commentId;
	}
	public Integer getProId()
	{
		return proId;
	}
	public void setProId(Integer proId)
	{
		this.proId = proId;
	}
	public String getCommentCata()
	{
		return commentCata;
	}
	public void setCommentCata(String commentCata)
	{
		this.commentCata = commentCata;
	}
	public String getCommentCont()
	{
		return commentCont;
	}
	public void setCommentCont(String commentCont)
	{
		this.commentCont = commentCont;
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
	public Float getScore()
	{
		return score;
	}
	public void setScore(Float score)
	{
		this.score = score;
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
		builder.append("Comment [commentId=").append(commentId).append(", proId=").append(proId).append(", score=")
				.append(score).append(", commentCata=").append(commentCata).append(", commentCont=")
				.append(commentCont).append(", agree=").append(agree).append(", oppose=").append(oppose).append("]");
		return builder.toString();
	}
}
