package com.citi.mc.utils;

/** 
 * @作者 newbie 
 * @email lianlupeng@gmail.com
 * @创建日期 2013-8-1 
 * @版本 V 1.0 
 */
import android.content.Context;

public class UnitTransformUtil {

/** * 根据手机的分辨率从 dp 的单位 转成为 px(像素) */

public static int dip2px(Context context, float dpValue) {

final float scale = context.getResources().getDisplayMetrics().density;

return (int) (dpValue * scale + 0.5f);

}



/** * 根据手机的分辨率从 px(像素) 的单位 转成为 dp */

public static int px2dip(Context context, float pxValue) {

final float scale = context.getResources().getDisplayMetrics().density;

return (int) (pxValue / scale + 0.5f);

}

}