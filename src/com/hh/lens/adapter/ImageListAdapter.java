/**
 * @Project: Lens
 * @Title: ImageListAdapter.java
 * @Package: com.hh.lens.adapter
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-4 下午3:00:51
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hh.lens.AppContext;
import com.hh.lens.Constant;
import com.hh.lens.R;
import com.hh.lens.util.ImageUtils;
import com.hh.lens.vo.Pic;
import com.hh.lens.vo.Product;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Classname: ImageListAdapter
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-4 下午3:00:51
 * @Version:
 */
public class ImageListAdapter extends BaseAdapter
{
	private List<Product> dataList;
	
	private Context context;
	
	/**
	 * @Title:
	 * @Description:
	 * @param mainActivity
	 * @param dataList
	 */
	public ImageListAdapter(Context c, List<Product> list)
	{
		context=c;
		dataList=list;
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
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem, null);
		}

        Product product=dataList.get(position);

		TextView textView = (TextView) convertView.findViewById(R.id.itemTitle);
		textView.setText(product.getName());

		String picPath = Constant.PIC_FOLDER + product.getMainPic();

		Bitmap bitmap = ImageUtils.loadImgThumbnail(picPath, 100, 100, context, false);
        if(bitmap==null){
            Pic pic= null;
            try {
                pic = AppContext.db.findFirst(Selector.from(Pic.class).where("proId","=",product.getProId()).and("picCata","like","%整体%"));
            } catch (DbException e) {
                Log.e(Constant.COMM_PREFIX,"DB Error!",e);
            }
            if(pic!=null){
                bitmap=ImageUtils.loadImgThumbnail(Constant.PIC_FOLDER+pic.getPicPath(), 100, 100, context, false);
                if(bitmap==null){
                    bitmap=ImageUtils.loadImgThumbnail(Constant.PIC_FOLDER+product.getMainPic().replaceAll("\\.jpg$","(1).jpg"), 100, 100, context, false);
                }
                if(bitmap!=null){
                    try {
                        OutputStream out=new FileOutputStream(Constant.PIC_FOLDER+product.getMainPic());
                        bitmap.compress(Bitmap.CompressFormat.JPEG,80,out);
                        out.close();
                    } catch (IOException e) {
                        Log.e(Constant.COMM_PREFIX,"Save bitmap Error!",e);
                    }
                }else{
                    bitmap=ImageUtils.loadImgThumbnail(picPath, 100, 100, context, true);
                }
            }
        }
		Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);

		// 缩放成100dip高宽的图片
		int[] imgSize = ImageUtils.scaleImageSize(drawable.getMinimumWidth(), drawable.getMinimumHeight(), 100, 100);
		drawable.setBounds(0, 0, imgSize[0], imgSize[1]);
		textView.setCompoundDrawables(drawable, null, null, null);
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
		return dataList.size();
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
		return dataList.get(position);
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
		return dataList.get(position).getProId();
	}
}
