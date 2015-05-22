package com.cn.lyric;

import java.util.TreeMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LyricView extends View {
	private float mX; // 屏幕X轴的中点，此值固定，保持歌词在X中间显示
	private float offsetY; // 歌词在Y轴上的偏移量，此值会根据歌词的滚动变小
	private float touchY; // 当触摸歌词View时，保存为当前触点的Y轴坐标
	private int lrcIndex = 0; // 保存歌词TreeMap的下标
	private int wordsize = 0;// 显示歌词文字的大小值
	private int diatance = 25;// 歌词每行的间隔
	Paint paint = new Paint();// 画笔，用于画不是高亮的歌词
	Paint paintHL = new Paint(); // 画笔，用于画高亮的歌词，即当前唱到这句歌词

	public LyricView(Context context) {
		super(context);
		init();
	}

	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (IndexLrc.haslrc) {
			paintHL.setTextSize(wordsize + 3);
			paint.setTextSize(wordsize);
			LyricObject temp = IndexLrc.lrc_map.get(lrcIndex);
			try {
				canvas.drawText(temp.lrc, mX, offsetY + (wordsize + diatance)
						* lrcIndex, paintHL);
			} catch (Exception e) {

			}
			// 画当前歌词之前的歌词
			for (int i = lrcIndex - 1; i >= 0; i--) {
				temp = IndexLrc.lrc_map.get(i);
				if (offsetY + (wordsize + diatance) * i < 0) {
					break;
				}
				try {
					canvas.drawText(temp.lrc, mX, offsetY
							+ (wordsize + diatance) * i, paint);
				} catch (Exception e) {

				}
			}
			// 画当前歌词之后的歌词
			for (int i = lrcIndex + 1; i < IndexLrc.lrc_map.size(); i++) {
				temp = IndexLrc.lrc_map.get(i);
				if (offsetY + (wordsize + diatance) * i > 1280) {
					break;
				}
				try {
					canvas.drawText(temp.lrc, mX, offsetY
							+ (wordsize + diatance) * i, paint);
				} catch (Exception e) {

				}
			}
		} else if (LyricStatue.searching) {
			paint.setTextSize(32);
			canvas.drawText("搜索歌词中...", mX, 400, paint);
		} else if (LyricStatue.downloading) {
			paint.setTextSize(32);
			canvas.drawText("正在下载歌词...", mX, 400, paint);
		} else {
			paint.setTextSize(32);
			canvas.drawText("手动查找歌词", mX, 400, paint);
		}
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float tt = event.getY();
		if (!IndexLrc.haslrc) {
			return super.onTouchEvent(event);
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			touchY = tt - touchY;
			offsetY = offsetY + touchY;
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		touchY = tt;
		return true;
	}

	public void init() {
		IndexLrc.lrc_map = new TreeMap<Integer, LyricObject>();
		offsetY = 440;
		paint = new Paint();
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setColor(Color.parseColor("#b7b7b7"));
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setAlpha(180);

		paintHL = new Paint();
		paintHL.setTextAlign(Paint.Align.CENTER);
		paintHL.setColor(Color.parseColor("#ffffff"));
		paintHL.setTypeface(Typeface.DEFAULT_BOLD);
		paintHL.setAntiAlias(true);
		paintHL.setAlpha(255);
	}

	/**
	 * 根据歌词里面最长的那句来确定歌词字体的大小
	 */
	public void SetTextSize() {
		if (!IndexLrc.haslrc) {
			return;
		}
		wordsize = 32;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mX = w * 0.5f;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * 歌词滚动的速度 返回歌词滚动的速度
	 */
	public Float SpeedLrc() {
		float speed = 0;
		if (offsetY + (wordsize + diatance) * lrcIndex > 220) {
			speed = ((offsetY + (wordsize + diatance) * lrcIndex - 220) / 20);
		} else if (offsetY + (wordsize + diatance) * lrcIndex < 220) {
			speed = (offsetY + (wordsize + diatance) * lrcIndex - 220) / 20;
		}
		return speed;
	}

	/**
	 * 按当前的歌曲的播放时间，从歌词里面获得那一句 当前歌曲的播放时间 返回当前歌词的索引值
	 */
	public int SelectIndex(int time) {
		if (!IndexLrc.haslrc) {
			return 0;
		}
		int index = 0;
		for (int i = 0; i < IndexLrc.lrc_map.size(); i++) {
			LyricObject temp = IndexLrc.lrc_map.get(i);
			if (temp.begintime < time) {
				++index;
			}
		}
		lrcIndex = index - 1;
		if (lrcIndex < 0) {
			lrcIndex = 0;
		}
		return lrcIndex;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	public int getSIZEWORD() {
		return wordsize;
	}

	public void setSIZEWORD(int sIZEWORD) {
		wordsize = sIZEWORD;
	}

}
