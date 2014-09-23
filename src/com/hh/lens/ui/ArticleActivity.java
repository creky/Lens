package com.hh.lens.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.hh.lens.AppContext;
import com.hh.lens.R;
import com.hh.lens.adapter.ArticleListAdapter;
import com.hh.lens.util.StringUtils;
import com.hh.lens.view.AutoMarqueeTextView;
import com.hh.lens.vo.Article;
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
public class ArticleActivity extends Activity
{

	private ListView listView;

	private List<Article> dataList;

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
		setContentView(R.layout.article_list_view);
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
		proId = getIntent().getIntExtra("proId", 0);
		proName = getIntent().getStringExtra("proName");
		listView = (ListView) findViewById(R.id.articleList);
		AutoMarqueeTextView textView = (AutoMarqueeTextView) findViewById(R.id.proName);
		textView.setText(proName);

        try {
            dataList = AppContext.db.findAll(Selector.from(Article.class).where("proId","=", proId).orderBy("articleId"));
        } catch (DbException e) {
            Log.e("Lens", "DB Error!", e);
            dataList=null;
        }
        listView.setAdapter(new ArticleListAdapter(this, dataList));
		listView.setOnItemClickListener(new ItemClickListener());
	}

	private class ItemClickListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Article article = dataList.get(position);
			if (StringUtils.isNotEmpty(article.getDetailUrl()))
			{
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getDetailUrl()));
				startActivity(intent);
			}
		}
	}

}
