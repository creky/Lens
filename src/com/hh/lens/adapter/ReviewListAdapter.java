package com.hh.lens.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hh.lens.R;
import com.hh.lens.vo.Review;

/**
 * @Classname: ImageListAdapter
 * @Description:
 * @Author: hh
 * @Date: 2013-2-4 下午3:00:51
 * @Version:
 */
public class ReviewListAdapter extends BaseAdapter
{
	private List<Review> list;

	private Context context;

	public ReviewListAdapter(Context c, List<Review> l)
	{
		context = c;
		list = l;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @Title: getView
	 * 
	 * @Description:
	 * 
	 * @param position
	 * 
	 * @param convertView
	 * 
	 * @param parent
	 * 
	 * @return
	 * 
	 * @see android.widget.SimpleAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.review_item_view, null);
		}
		TextView scoreView = (TextView) convertView.findViewById(R.id.review_score);
		scoreView.setText("评分: " + list.get(position).getScore());

		// TextView dateView = (TextView)
		// convertView.findViewById(R.id.comment_date);
		// dateView.setText(StringUtils.dateFormater.get().format(list.get(position).getPublishTime()));

		TextView goodView = (TextView) convertView.findViewById(R.id.good_point);
		goodView.setText(list.get(position).getGoodPoint());

		TextView badView = (TextView) convertView.findViewById(R.id.bad_point);
		badView.setText(list.get(position).getBadPoint());

		TextView contView = (TextView) convertView.findViewById(R.id.review_cont);
		contView.setText(list.get(position).getReviewCont());

		TextView agreeView = (TextView) convertView.findViewById(R.id.review_agree);
		Drawable drawableAgree=context.getResources().getDrawable(R.drawable.p_up);
		drawableAgree.setBounds(0, 0, 24, 24);
		agreeView.setCompoundDrawables(drawableAgree, null, null, null);
		agreeView.setText(list.get(position).getAgree().toString());

		TextView opposeView = (TextView) convertView.findViewById(R.id.review_oppose);
		Drawable drawableOppose=context.getResources().getDrawable(R.drawable.p_down);
		drawableOppose.setBounds(0, 0, 24, 24);
		opposeView.setCompoundDrawables(drawableOppose, null, null, null);
		opposeView.setText(list.get(position).getOppose().toString());
		

		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @Title: getCount
	 * 
	 * @Description:
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @Title: getItem
	 * 
	 * @Description:
	 * 
	 * @param position
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @Title: getItemId
	 * 
	 * @Description:
	 * 
	 * @param position
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		return list.get(position).getReviewId();
	}

}
