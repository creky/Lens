/**
 * @Project: Lens
 * @Title: MarQueeTextView.java
 * @Package: com.hh.lens.view
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-7 上午4:26:02
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @Classname: MarQueeTextView
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-7 上午4:26:02
 * @Version:
 */
public class AutoMarqueeTextView extends TextView
{

	/**
	 * @Title:
	 * @Description:
	 * @param context
	 */
	public AutoMarqueeTextView(Context context)
	{
		super(context);
	}
	
	
	/**
	 * @Title:
	 * @Description:
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AutoMarqueeTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}


	/**
	 * @Title:
	 * @Description:
	 * @param context
	 * @param attrs
	 */
	public AutoMarqueeTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}


	/* (non-Javadoc)
	 * @Title: isFocused
	 * @Description: 
	 * @return
	 *
	 * @see android.view.View#isFocused()
	 */
	@Override
	public boolean isFocused()
	{
		return true;
	}

}
