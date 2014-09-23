/**
 * @Project: Test
 * @Title: Attrbute.java
 * @Package: com.vo
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午2:16:11
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.vo;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @Classname: Attribute
 * @Description: 
 * @Author: hh
 * @Date: 2013-1-27 下午2:16:11
 * @Version:
 */
@Table(name="attribute")
public class Attribute
{
    @Id
	@Column(column="attrId")
	private Integer attrId;
	
	@Column(column="proId")
	private Integer proId;
	
	@Column(column="attrCata")
	private String attrCata;
	
	@Column(column="attrName")
	private String attrName;
	
	@Column(column="attrValue")
	private String attrValue;
	
	
	
	
	public Integer getAttrId()
	{
		return attrId;
	}
	public void setAttrId(Integer attrId)
	{
		this.attrId = attrId;
	}
	public Integer getProId()
	{
		return proId;
	}
	public void setProId(Integer proId)
	{
		this.proId = proId;
	}
	public String getAttrCata()
	{
		return attrCata;
	}
	public void setAttrCata(String attrCata)
	{
		this.attrCata = attrCata;
	}
	public String getAttrName()
	{
		return attrName;
	}
	public void setAttrName(String attrName)
	{
		this.attrName = attrName;
	}
	public String getAttrValue()
	{
		return attrValue;
	}
	public void setAttrValue(String attrValue)
	{
		this.attrValue = attrValue;
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
		builder.append("Attribute [attrId=").append(attrId).append(", proId=").append(proId).append(", attrCata=")
				.append(attrCata).append(", attrName=").append(attrName).append(", attrValue=").append(attrValue)
				.append("]");
		return builder.toString();
	}
}
