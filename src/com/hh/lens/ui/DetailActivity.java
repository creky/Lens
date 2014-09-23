/**
 * @Project: Lens
 * @Title: DetailActivity.java
 * @Package: com.hh.lens.activity
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-5 上午10:16:10
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ListView;
import com.hh.lens.AppContext;
import com.hh.lens.R;
import com.hh.lens.adapter.AttributesAdapter;
import com.hh.lens.adapter.ImageAdapter;
import com.hh.lens.view.AutoMarqueeTextView;
import com.hh.lens.vo.Attribute;
import com.hh.lens.vo.Pic;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * @Classname: DetailActivity
 * @Description:
 * @Author: hh
 * @Date: 2013-2-5 上午10:16:10
 * @Version:
 */
public class DetailActivity extends Activity
{

	private Gallery gallery;

	private ListView detailListView;

	private List<Pic> dataList;

	private int proId;

	private String proName;

	/*
	 * (non-Javadoc)
	 * 
	 * @Title: onCreate
	 * 
	 * @Description:
	 * 
	 * @param savedInstanceState
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.detailview);
		init();
	}

	/**
	 * 
	 * @Methodname: init
	 * @Discription:
	 * @Author HH
	 * @Time 2013-2-5 上午10:18:33
	 */
	private void init()
	{
		detailListView = (ListView) findViewById(R.id.attrbox);
		View headerView = LayoutInflater.from(this).inflate(R.layout.detailheader, null);

		proId = getIntent().getIntExtra("proId", 0);
		proName = getIntent().getStringExtra("proName");
		AutoMarqueeTextView textView = (AutoMarqueeTextView) headerView.findViewById(R.id.proName);
		textView.setText(proName);

		// Product product=new
		// Select().from(Product.class).where("proId=?",proId).executeSingle();

        try {
            dataList = AppContext.db.findAll(Selector.from(Pic.class).where("proId","=", proId).orderBy("picId"));
        } catch (DbException e) {
            e.printStackTrace();
        }
        gallery = (Gallery) headerView.findViewById(R.id.picbox);

		if (dataList != null && !dataList.isEmpty())
		{
			gallery.setVisibility(View.VISIBLE);
			gallery.setAdapter(new ImageAdapter(this, dataList));
			gallery.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					Intent intent = new Intent();
					intent.putExtra("picPath", dataList.get(position).getPicPath());
					intent.setClass(DetailActivity.this, ImageZoomActivity.class);
					startActivity(intent);
				}
			});
		}
		else
		{
			gallery.setVisibility(View.GONE);
		}

		detailListView.addHeaderView(headerView);

		// 初始化参数列表
		initAttributes(proId);
		initOthers(proId);
	}

	/**
	 * 初始化评测、点评
	 * 
	 * @Methodname: intiOthers
	 * @Discription:
	 * @param proId
	 * @Author HH
	 * @Time 2013-2-7 下午8:36:28
	 */
	private void initOthers(int proId)
	{
		Button articleButton = (Button) findViewById(R.id.article);
		Button reviewButton = (Button) findViewById(R.id.review);

		articleButton.setOnClickListener(new ArticleClickListener());
		reviewButton.setOnClickListener(new ReviewClickListener());
	}

	/**
	 * 初始化参数列表
	 * 
	 * @param proId
	 * @Methodname: initAttributes
	 * @Discription:
	 * @Author HH
	 * @Time 2013-2-5 下午1:08:46
	 */
	private void initAttributes(int proId)
	{
        List<Attribute> attrs = null;
        try {
            attrs = AppContext.db.findAll(Selector.from(Attribute.class).where("proId", "=", proId).orderBy("attrId"));
        } catch (DbException e) {
            Log.e("Lens", "DB Error!", e);
        }
        if (attrs != null)
		{
			detailListView.setAdapter(new AttributesAdapter(this, attrs));
		}
	}

	private class ArticleClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.putExtra("proId", proId);
			intent.putExtra("proName", proName);
			intent.setClass(DetailActivity.this, ArticleActivity.class);
			startActivity(intent);
		}
	}

	private class ReviewClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.putExtra("proId", proId);
			intent.putExtra("proName", proName);
			intent.setClass(DetailActivity.this, ReviewActivity.class);
			startActivity(intent);
		}
	}
}
