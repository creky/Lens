/**
 * @Project: Lens
 * @Title: Constant.java
 * @Package: com.hh.lens
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-4 下午4:07:09
 * @Copyright: 2013 HH Inc. All right reserved
 * @Version:
 */
package com.hh.lens;

import android.os.Environment;

import java.io.File;

/**
 * @Classname: Constant
 * @Description: 
 * @Author: hh
 * @Date: 2013-2-4 下午4:07:09
 * @Version:
 */
public class Constant
{
    public static final String ENCODING="utf-8";
    public static final int PAGE_SIZE = 20;//默认分页大小
    public static final String BASE_PATH = "data/com.hh.lens/";
    public static final String BASE_URL="http://detail.zol.com.cn/";
    public static final String SD_BASE_PATH=Environment.getExternalStorageDirectory() + File.separator + Constant.BASE_PATH;
    public static final String PIC_FOLDER=  SD_BASE_PATH+ ".pic"+File.separator;
    public static final String COMM_PREFIX="Lens";

    public static final int IMG_THUMB_WIDTH=300;
    public static final int IMG_THUMB_HEIGHT=300;

    public static final String USER_AGENT_NAME="User-Agent";
    public static final String USER_AGENT="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36";

}
