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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.hh.lens.Constant;
import com.hh.lens.service.HttpService;
import com.hh.lens.util.ImageUtils;
import com.hh.lens.vo.Pic;

import java.util.List;

/**
 * @Classname: ImageListAdapter
 * @Description:
 * @Author: hh
 * @Date: 2013-2-4 下午3:00:51
 * @Version:
 */
public class ImageAdapter extends BaseAdapter {
    private List<Pic> imgList;

    private Context imgContext;

    public ImageAdapter(Context c, List<Pic> list) {
        imgContext = c;
        imgList = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(imgContext);
        Bitmap img = null;
        Pic pic = imgList.get(position);
        String imgPath = pic.getPicPath();
        String picPath = Constant.PIC_FOLDER  + imgPath;
        img = ImageUtils.loadImgThumbnail(picPath, Constant.IMG_THUMB_WIDTH, Constant.IMG_THUMB_HEIGHT, imgContext, false);
        if (img == null) {
            HttpService.getInstance().downloadPic(imgContext, imageView, pic);
            img = ImageUtils.loadImgThumbnail(picPath, Constant.IMG_THUMB_WIDTH, Constant.IMG_THUMB_HEIGHT, imgContext, true);
        }
        imageView.setImageBitmap(img);
        return imageView;
    }


    /* (non-Javadoc)
     * @Title: getCount
     * @Description:
     * @return
     *
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return imgList.size();
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
    public Object getItem(int position) {
        return imgList.get(position);
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
    public long getItemId(int position) {
        return imgList.get(position).getPicId();
    }

}
