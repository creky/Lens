package com.hh.lens.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.hh.lens.AppContext;
import com.hh.lens.R;
import com.hh.lens.adapter.ReviewListAdapter;
import com.hh.lens.util.StringUtils;
import com.hh.lens.view.AutoMarqueeTextView;
import com.hh.lens.vo.Comment;
import com.hh.lens.vo.Review;
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
public class ReviewActivity extends Activity
{

	private ListView listView;

	private List<Review> dataList;

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
		setContentView(R.layout.review_list_view);
		init();

		// List<Comment> commList = new
		// Select().from(Comment.class).where("commentCata =?", "缺点").execute();
		// for (Comment comm : commList)
		// {
		// Comment good = new Select("commentCont").from(Comment.class)
		// .where("commentCata =? and proId=?", new Object[] { "优点",
		// comm.getProId() }).executeSingle();
		// if (StringUtils.isNotEmpty(good.getCommentCont()))
		// {
		// new Update(Comment.class)
		// .set("commentCont=?",
		// comm.getCommentCont().replace(good.getCommentCont(), ""))
		// .where("commentId = ? ", comm.getCommentId()).execute();
		// }
		// }
		// System.out.println("+++++++完成+++++++");
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
		listView = (ListView) findViewById(R.id.reviewList);
		AutoMarqueeTextView textView = (AutoMarqueeTextView) findViewById(R.id.proName);
		textView.setText(proName);

		// 添加编辑评语
        List<Comment> commList = null;
        try {
            commList = AppContext.db.findAll(Selector.from(Comment.class).where("proId", "=", proId));
        } catch (DbException e) {
            Log.e("Lens","DB error!",e);
        }
        Comment goodComment = null;
		Comment badComment = null;
        if(commList!=null) {
            for (Comment comment : commList) {
                if ("优点".equals(comment.getCommentCata())) {
                    goodComment = comment;
                } else if ("缺点".equals(comment.getCommentCata())) {
                    badComment = comment;
                }
            }
        }
		View commentView = getLayoutInflater().inflate(R.layout.comment_item_view, null);
		TextView goodView = (TextView) commentView.findViewById(R.id.goodPoint);
		if (goodComment != null && StringUtils.isNotEmpty(goodComment.getCommentCont()))
		{
			// goodView.loadDataWithBaseURL(null, goodComment.getCommentCont(),
			// "text/html", AppContext.ENCODING, null);
			goodView.setText(html2FormatedText(goodComment.getCommentCont()));
		}
		else
		{
			commentView.findViewById(R.id.goodPointLine).setVisibility(View.GONE);
		}

		TextView badView = (TextView) commentView.findViewById(R.id.badPoint);
		if (badComment != null && StringUtils.isNotEmpty(badComment.getCommentCont()))
		{
			// badView.loadDataWithBaseURL(null, badComment.getCommentCont(),
			// "text/html", AppContext.ENCODING, null);
			badView.setText(html2FormatedText(badComment.getCommentCont()));
		}
		else
		{
			commentView.findViewById(R.id.badPointLine).setVisibility(View.GONE);
		}
		
		TextView scoreView=(TextView) commentView.findViewById(R.id.commentScore);
		if(goodComment!=null&&goodComment.getScore()!=0f){
			scoreView.setText(""+goodComment.getScore());
		}
		

		if (goodComment != null || badComment != null)
		{
			listView.addHeaderView(commentView);
		}

        try {
            dataList = AppContext.db.findAll(Selector.from(Review.class).where("proId", "=", proId).orderBy("reviewId"));
        } catch (DbException e) {
            Log.e("Lens","DB Error!",e);
            dataList=null;
        }
        listView.setAdapter(new ReviewListAdapter(this, dataList));
	}

	private static String html2FormatedText(String cont)
	{
		return cont.replaceAll("</em>", ". ").replaceAll("<br/>", "\n").replaceAll("</?.+?>", "");
	}

}
