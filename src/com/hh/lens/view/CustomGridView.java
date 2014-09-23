/**
 * @Project: Lens
 * @Title: UnScrollGridView.java
 * @Package: com.hh.lens.view
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-5 下午2:40:42
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @Classname: UnScrollGridView
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-5 下午2:40:42
 * @Version:
 */
public class CustomGridView extends GridView
{

	/**
	 * @Title:
	 * @Description:
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomGridView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/**
	 * @Title:
	 * @Description:
	 * @param context
	 */
	public CustomGridView(Context context)
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
	public CustomGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/* (non-Javadoc)
	 * @Title: onMeasure
	 * @Description: 
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 *
	 * @see android.widget.GridView#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int expandSpec = MeasureSpec.makeMeasureSpec((Integer.MAX_VALUE >> 2)+200,  
                MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
