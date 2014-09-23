package com.hh.lens.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import com.hh.lens.AppContext;
import com.hh.lens.Constant;
import com.hh.lens.R;
import com.hh.lens.adapter.ImageListAdapter;
import com.hh.lens.util.StringUtils;
import com.hh.lens.vo.Product;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

public class MainActivity extends Activity
{
	private List<Product> dataList;

	private ListView listView;

	private View footerView;

	private EditText searchEditor;

	private Button searchButton;

	private ImageListAdapter adapter;

	private int currPage = 1;

	private static final int MODE_NORMAL = 1;// 正常模式

	private static final int MODE_SEARCH = 2;// 搜索模式

	private int mode = MODE_NORMAL;

	private static final int MSG_UPDATEUI = 1;

	private static final int MSG_DELETE_FOOTER = 2;

	private static final int MSG_CLEAR_DATA_LIST = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);
		this.init();
	}

	/**
	 * 
	 * @Methodname: init
	 * @Discription:
	 * @Author HH
	 * @Time 2013-2-4 上午12:19:31
	 */
	private void init()
	{
		// AppContext appContext = (AppContext)getApplication();
		searchEditor = (EditText) findViewById(R.id.search_editer);
		searchButton = (Button) findViewById(R.id.search_btn);
		listView = (ListView) findViewById(R.id.main_list);

		footerView = LayoutInflater.from(this).inflate(R.layout.footview, null, false);
		listView.addFooterView(footerView);

        try {
            dataList = AppContext.db.findAll(Selector.from(Product.class).orderBy("updateTime").limit(Constant.PAGE_SIZE));
        } catch (DbException e) {
            e.printStackTrace();
        }
        adapter = new ImageListAdapter(this, dataList);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new ListOnScrollListener());
		listView.setOnItemClickListener(new ListItemClickListener());

		searchButton.setOnClickListener(new SearchButtonClickListener());
		searchEditor.setOnKeyListener(new KeyListener());
	}

	private class KeyListener implements OnKeyListener
	{
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event)
		{
			if (keyCode == KeyEvent.KEYCODE_ENTER)
			{
				searchButton.performClick();
			}
			return false;
		}
	}

	private class ListItemClickListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			if (view == footerView)
			{
				loadMore();
			}
			else
			{
				Product product = dataList.get(position);
				Intent intent = new Intent();
				intent.putExtra("proId", product.getProId());
				intent.putExtra("proName", product.getName());
				intent.setClass(MainActivity.this, DetailActivity.class);
				startActivity(intent);
			}
		}
	}

	private class SearchButtonClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{

			currPage = 1;
			Message msg = new Message();
			msg.what = MSG_UPDATEUI;

			Selector selector=Selector.from(Product.class).where("1","=","0");
			String searchText = searchEditor.getText().toString().trim();
			if (StringUtils.isNotEmpty(searchText))
			{
				mode = MODE_SEARCH;
				String[] searchKeyWords = searchText.split(" +");
				for (int i = 0; i < searchKeyWords.length; i++)
				{
                    selector.and("name", "like", "%" + searchKeyWords[i] + "%");
				}
			}
			else
			{
				mode = MODE_NORMAL;
			}

            List<Product> proList = null;
            try {
                proList = AppContext.db.findAll(selector.orderBy("updateTime").limit(Constant.PAGE_SIZE));
            } catch (DbException e) {
                e.printStackTrace();
            }
            msg.what = MSG_CLEAR_DATA_LIST;
			msg.obj = proList;
			handler.sendMessage(msg);
			hideSoftInput(MainActivity.this, MainActivity.this);
			// dataList.addAll(proList);
			// msg.what = MSG_UPDATEUI;
			// handler.sendMessage(msg);
		}

	}

	private class ListOnScrollListener implements OnScrollListener
	{
		private boolean loadingMore = false;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
			int lastInScreen = firstVisibleItem + visibleItemCount;

			if (lastInScreen == totalItemCount && !loadingMore)
			{
				new Thread()
				{
					public void run()
					{
						loadingMore = true;

						loadMore();
						// adapter.notifyDataSetChanged();
						// handler.post(updateListUI);
						loadingMore = false;
					}
				}.start();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * 加载更多内容
	 * 
	 * @Methodname: loadMore
	 * @Discription:
	 * @Author HH
	 * @Time 2013-2-5 上午1:12:29
	 */
	private void loadMore()
	{
        Selector selector=Selector.from(Product.class).where("1","=","0");

        //String sql="select proId,name,mainPic,price from product where 1=0 ";

		// 产品搜索逻辑
		String searchText = searchEditor.getText().toString().trim();
		if (mode == MODE_SEARCH && StringUtils.isNotEmpty(searchText))
		{
            selector.and("name", "like", "%" + searchText + "%");
		}
		else
		{
			mode = MODE_NORMAL;
		}

        selector.orderBy("updateTime").limit(Constant.PAGE_SIZE).offset(currPage * Constant.PAGE_SIZE);
        List<Product> proList = null;
        try {
            proList = AppContext.db.findAll(selector);
        } catch (DbException e) {
            Log.e("Lens", "DB Error!", e);
            dataList=null;
        }
        if (proList == null || proList.isEmpty())
		{
			Message msg = new Message();
			msg.what = MSG_DELETE_FOOTER;
			handler.sendMessage(msg);
		}

		dataList.addAll(proList);

		Message msg = new Message();
		msg.what = MSG_UPDATEUI;
		handler.sendMessage(msg);
		// handler.post(updateListUI);
		currPage++;
	}

	private Handler handler = new Handler()
	{
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case MSG_UPDATEUI:
					adapter.notifyDataSetChanged();
					break;
				case MSG_DELETE_FOOTER:
					listView.removeFooterView(footerView);
					break;
				case MSG_CLEAR_DATA_LIST:
					dataList.clear();
					if (msg.obj != null)
					{
						dataList.addAll((List<Product>) msg.obj);
					}
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 隐藏软键盘
	 * 
	 * @param c
	 * @param a
	 */
	public static void hideSoftInput(final Context c, final Activity a)
	{

		InputMethodManager inputManager = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
