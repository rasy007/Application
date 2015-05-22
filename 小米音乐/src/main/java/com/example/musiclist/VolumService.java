package com.example.musiclist;

import java.io.FileNotFoundException;
import com.cn.sava.MusicNum;
import com.cn.ui.ImageBg;
import com.cn.ui.RoundCorner;
import com.cn.ui.WidgetBitmap;
import com.cn.ui.Xuanzhuan;
import com.example.love.ToTime;
import com.example.musiclist.MusicActivity.MusicPlay6er;
import com.example.widget.appWidgetProvider;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

@SuppressWarnings("unused")
@SuppressLint("DefaultLocale")
public class VolumService extends Service {
	SharedPreferences localSharedPreferences;
	public static final String REMEMBER_USERID_KEY12 = "remember12";
	public static final String REMEMBER_USERID_KEY13 = "remember13";
	public static final String PREFS_NAME = "prefsname";
	Bitmap bitmap = null;
	private MusicPlay6er receiver9;
	int i = 0;
	Play6erisplay6ing receiverr2s;
	Notification notification;
	static NotificationManager notificationManager;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {

		receiver9 = new MusicPlay6er();
		IntentFilter filter9 = new IntentFilter("com.cn.musicserviceplayer");
		this.registerReceiver(receiver9, filter9);

		receiverr2s = new Play6erisplay6ing();
		IntentFilter filter2s = new IntentFilter("cn.com.karl.progress");
		this.registerReceiver(receiverr2s, filter2s);

		getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		localSharedPreferences = getSharedPreferences("music", 0);
		super.onCreate();
	}

	private class Play6erisplay6ing extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// if (MusicService.player != null) {
			// long current = MusicService.player.getCurrentPosition();
			// long total = MusicService.player.getDuration();
			// int degree = (int) (current * 360 / total);
			// RemoteViews remoteViews = new RemoteViews(
			// "com.example.musiclist", R.layout.widget_main);
			// Drawable drawable2 = getResources().getDrawable(
			// R.drawable.icon_panel_progress_thumb2);
			// BitmapDrawable bitmapDrawable2 = (BitmapDrawable) drawable2;
			// Bitmap bitmap2 = bitmapDrawable2.getBitmap();
			// Drawable drawable22 = getResources().getDrawable(
			// R.drawable.icon_panel_progress_barleft);
			// BitmapDrawable bitmapDrawable22 = (BitmapDrawable) drawable22;
			// Bitmap bitmap22 = bitmapDrawable22.getBitmap();
			// if (degree >= 360) {
			// degree = 360;
			// }
			// try {
			// Xuanzhuan.rotate(bitmap2, degree);
			// Xuanzhuan.rotate2(bitmap22, degree);
			// remoteViews.setImageViewBitmap(R.id.thumb2,
			// WidgetBitmap.getBitmap());
			// remoteViews.setImageViewBitmap(
			// R.id.icon_panel_progress_barleft,
			// WidgetBitmap.getBitmap2());
			// } catch (Exception e) {
			// e.printStackTrace();
			// } catch (OutOfMemoryError e) {
			// e.printStackTrace();
			// }
			// if (degree >= 180) {
			// remoteViews.setViewVisibility(R.id.leftyuanquan,
			// View.VISIBLE); // 右半绿圆出现
			// remoteViews.setViewVisibility(R.id.icon_panel_progress_bg2,
			// View.INVISIBLE);
			// }
			// if (degree < 180) {
			// remoteViews.setViewVisibility(R.id.leftyuanquan,
			// View.INVISIBLE); // 右半绿圆消失
			// remoteViews.setViewVisibility(R.id.icon_panel_progress_bg2,
			// View.VISIBLE);
			// }
			// AppWidgetManager appWidgetManager = AppWidgetManager
			// .getInstance(getBaseContext());
			// ComponentName componentName = new ComponentName(
			// getBaseContext(), appWidgetProvider.class);
			// appWidgetManager.updateAppWidget(componentName, remoteViews);
			// if (MusicNum.getbtn(7)) {
			// current = MusicService.player.getCurrentPosition();
			// if (current > (long) (10000)) {
			// Intent intent1 = new Intent(VolumService.this,
			// MusicService.class);
			// MusicNum.putplay(5);
			// MusicNum.putisok(true);
			// startService(intent1);
			// }
			// }
			// }
			i++;
			if (MusicService.player != null) {
				if (!MusicService.player.isPlaying() && MusicService.nowplay
						&& MusicNum.getser() == false) {
					if (i >= 10) {
						if (MusicService.isbusy) {
							Log.i("dasds", "拨号中");
						} else {
							Intent intent1 = new Intent(VolumService.this,
									MusicService.class);
							MusicNum.putplay(5);
							MusicNum.putisok(true);
							startService(intent1);
						}
					}
				} else {
					i = 0;
				}
			}
		}
	}

	public class MusicPlay6er extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			RemoteViews remoteViesws = new RemoteViews("com.example.musiclist",
					R.layout.widget_main);
			if (ImageBg.getBitmap() != null) {
				remoteViesws.setImageViewBitmap(R.id.widget_logo, RoundCorner
						.toRoundCorner(ImageBg.getBitmap(), ImageBg.getBitmap()
								.getHeight() / 35));
			} else {
				if (ImageBg.getback() != null) {
					remoteViesws.setImageViewBitmap(R.id.widget_logo,
							RoundCorner.toRoundCorner(ImageBg.getback(),
									ImageBg.getback().getHeight() / 34));

				} else {
					remoteViesws.setImageViewBitmap(R.id.widget_logo, null);
				}
			}

			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(getBaseContext());
			ComponentName componentName = new ComponentName(getBaseContext(),
					appWidgetProvider.class);
			appWidgetManager.updateAppWidget(componentName, remoteViesws);
		}
	}

	public void onDestroy() {
		super.onDestroy();
	}
}
