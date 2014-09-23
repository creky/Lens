package com.hh.lens.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.hh.lens.Constant;
import com.hh.lens.util.ClientUtil;
import com.hh.lens.util.ImageUtils;
import com.hh.lens.vo.Pic;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by hh on 2014/9/21 0021.
 */
public class HttpService {
    private Queue<Pic> downloadQueue = new ArrayBlockingQueue<Pic>(100);
    private Runnable downRunnable = null;

    private static HttpService ins;

    private HttpService() {
    }

    public static HttpService getInstance() {
        if (ins == null) {
            ins = new HttpService();
        }
        return ins;
    }

    public void downloadPic(Context context, View view, final Pic pic) {
        if (!downloadQueue.contains(pic)) {
            downloadQueue.offer(pic);
        }

        if (downRunnable == null) {
            downRunnable = new Download4Queue(context, view);
            new Thread(downRunnable).start();
        }
    }

    private class Download4Queue implements Runnable {
        private Context context;
        private View view;

        public Download4Queue(Context context, View view) {
            this.context = context;
            this.view = view;
        }

        @Override
        public void run() {
            Pic pic;
            while ((pic = downloadQueue.poll()) != null) {
                downloadPic4Queue(pic);
            }
            downRunnable = null;
        }

        public void downloadPic4Queue(final Pic pic) {
            String targetPath = Constant.PIC_FOLDER + pic.getProId();
            File file = new File(targetPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            String path = null;
            try {
                path = ClientUtil.getAndSaveFile(pic.getPicUrl(), null, Constant.PIC_FOLDER, String.valueOf(pic.getProId()), null);
                Log.i(Constant.COMM_PREFIX, "Download file successful,file name=" + path);
                Bitmap img = ImageUtils.loadImgThumbnail(pic.getPicPath(), Constant.IMG_THUMB_WIDTH, Constant.IMG_THUMB_HEIGHT, context, true);
                //view.setImageBitmap(img);
                if (view instanceof ImageView) {
                    ImageView imgView = (ImageView) view;
                    imgView.setImageBitmap(img);
                    imgView.postInvalidate();
                }
            } catch (Exception e) {
                Log.e(Constant.COMM_PREFIX, "Download file failed !", e);
            }
        }
    }
}
