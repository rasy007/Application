package com.cn.ui;

import com.example.musiclist.Index;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MenuTouch implements OnTouchListener {
	int startY = 0;
	int x = 0;
	int base = Index.base;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		int action = arg1.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			x = (int) arg1.getRawY();
			startY = (int) Index.bottomlistlin.getTranslationY();

		}
		if (action == MotionEvent.ACTION_MOVE) {
			int a = startY + ((int) arg1.getRawY() - x);
			if (a <= base && a >= 0) {
				Index.bottomlistlin.setTranslationY(a);
			}
		}
		if (action == MotionEvent.ACTION_UP) {
			if (Index.bottomlistlin.getTranslationY() > base / 2) {
				Index.bottomlistlin.setTranslationY(base);
			} else {
				Index.bottomlistlin.setTranslationY(0);
			}
		}
		return false;
	}

}
