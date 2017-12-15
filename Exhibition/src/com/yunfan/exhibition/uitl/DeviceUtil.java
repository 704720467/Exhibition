package com.yunfan.exhibition.uitl;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author zp
 *
 */
public class DeviceUtil {

	/**
	 * dp转像素
	 *
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, int dp) {
		return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
	}
	/**
	 * 获取屏幕宽单位是像素
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenWidthSize(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高单位是像素
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenHeightSize(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
}
