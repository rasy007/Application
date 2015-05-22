package com.cn.ui;

import android.graphics.Bitmap;

public class WidgetBitmap {
	public static Bitmap bitmap;

	public static Bitmap getBitmap() {
		return bitmap;
	}

	public static void setBitmap(Bitmap bitmap) {
		WidgetBitmap.bitmap = bitmap;
	}

	public static Bitmap bitmap2;

	
	public static Bitmap getBitmap2() {
		return bitmap2;
	}

	public static void setBitmap2(Bitmap bitmap2) {
		WidgetBitmap.bitmap2 = bitmap2;
	}

	public static int isopen = 0;

	public static int getisopen() {
		return isopen;
	}

	public static void setisopen(int open) {
		WidgetBitmap.isopen = open;
	}

}
