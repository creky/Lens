/**
 * @Project: Lens
 * @Title: PicZoomActivity.java
 * @Package: com.hh.lens.ui
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-6 上午1:11:51
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import com.hh.lens.Constant;
import com.hh.lens.R;
import com.hh.lens.listener.ImageTouchScaleListener;
import com.hh.lens.util.StringUtils;

/**
 * @Classname: PicZoomActivity
 * @Description:
 * @Author: hh
 * @Date: 2013-2-6 上午1:11:51
 * @Version:
 */
public class ImageZoomActivity extends Activity
{
	private ImageView imageView;
	
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageview);
		init();
	}

	/**
	 * 
	 * @Methodname: init
	 * @Discription:
	 * @Author HH
	 * @Time 2013-2-6 上午1:20:58
	 */
	private void init()
	{
		String imgPath = getIntent().getStringExtra("picPath");
		if (StringUtils.isNotEmpty(imgPath))
		{
			imageView = (ImageView) findViewById(R.id.imageZoom);

			String picPath = Constant.PIC_FOLDER + imgPath;
			Bitmap img = BitmapFactory.decodeFile(picPath);
			if (img == null)
			{
				new AlertDialog.Builder(this).setTitle("提示消息").setMessage("打开图片失败，请检查是否已经已经将图片放到SD卡中！")
						.setPositiveButton("确定", new OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								finish();
							}
						}).show();
			}
			else
			{
				imageView.setImageBitmap(img);
				//imageView.setOnTouchListener(new ImageTouchListener());
				
				DisplayMetrics dm = new DisplayMetrics();
		        getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取屏幕分辨率
		        ImageTouchScaleListener listener=new ImageTouchScaleListener(imageView, dm, img.getWidth(), img.getHeight());
		        imageView.setOnTouchListener(listener);
			}
		}
	}
}
