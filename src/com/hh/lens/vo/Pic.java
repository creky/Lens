/**
 * @Project: Test
 * @Title: Pic.java
 * @Package: com.vo
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午2:18:14
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.vo;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;


/**
 * @Classname: Pic
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午2:18:14
 * @Version:
 */
@Table(name="pic")
public class Pic
{
    @Id
	@Column(column="picId")
	private Integer picId;

	@Column(column="proId")
	private Integer proId;

	@Column(column="picCata")
	private String picCata;

	@Column(column="picName")
	private String picName;

	@Column(column="picUrl")
	private String picUrl;

	@Column(column="picPath")
	private String picPath;
	
	
	public Integer getPicId()
	{
		return picId;
	}
	public void setPicId(Integer picId)
	{
		this.picId = picId;
	}
	public Integer getProId()
	{
		return proId;
	}
	public void setProId(Integer proId)
	{
		this.proId = proId;
	}
	public String getPicCata()
	{
		return picCata;
	}
	public void setPicCata(String picCata)
	{
		this.picCata = picCata;
	}
	public String getPicName()
	{
		return picName;
	}
	public void setPicName(String picName)
	{
		this.picName = picName;
	}
	public String getPicUrl()
	{
		return picUrl;
	}
	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}
	public String getPicPath()
	{
		return picPath;
	}
	public void setPicPath(String picPath)
	{
		this.picPath = picPath;
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
		builder.append("Pic [picId=").append(picId).append(", proId=").append(proId).append(", picCata=")
				.append(picCata).append(", picName=").append(picName).append(", picUrl=").append(picUrl)
				.append(", picPath=").append(picPath).append("]");
		return builder.toString();
	}
}
