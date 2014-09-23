package com.hh.lens.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hh.lens.R;
import com.hh.lens.vo.Article;

/**
 * @Classname: ImageListAdapter
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-4 下午3:00:51
 * @Version:
 */
public class ArticleListAdapter extends BaseAdapter
{
	private List<Article> list;
	
	private Context context;
	
	public ArticleListAdapter(Context c,List<Article> l){
		context=c;
		list=l;
	}

	
	/* (non-Javadoc)
	 * @Title: getView
	 * @Description: 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 *
	 * @see android.widget.SimpleAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView==null){
			LayoutInflater inflater=LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.article_item_view, null);
		}
		
		TextView view=(TextView)convertView.findViewById(R.id.articleItem);
		view.setText(list.get(position).getTitle());
		
		TextView summaryView=(TextView)convertView.findViewById(R.id.articleSummary);
		summaryView.setText("        "+list.get(position).getSummaryCont());
		
		return convertView;
	}




	/* (non-Javadoc)
	 * @Title: getCount
	 * @Description: 
	 * @return
	 *
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		return list.size();
	}




	/* (non-Javadoc)
	 * @Title: getItem
	 * @Description: 
	 * @param position
	 * @return
	 *
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}




	/* (non-Javadoc)
	 * @Title: getItemId
	 * @Description: 
	 * @param position
	 * @return
	 *
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		return list.get(position).getArticleId();
	}

}
