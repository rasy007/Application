package com.example.widget;

import com.cn.receiver.WidgetFrontService;
import com.cn.receiver.WidgetNextService;
import com.cn.receiver.WidgetPlayService;
import com.example.musiclist.MusicActivity;
import com.example.musiclist.MusicService;
import com.example.musiclist.R;
import com.example.musiclist.VolumService;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class appWidgetProvider extends AppWidgetProvider {
	@Override
	public void onReceive(Context context, Intent intent) {

		// -----------------设置按下开始按钮发出的广播Action-----------------------
		Intent startIntent = new Intent(context, WidgetPlayService.class);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startIntent.putExtra("play", 5);

		PendingIntent startPendingIntent = PendingIntent.getService(context, 0,
				startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// -----------------设置按下上一首按钮发出的广播Action-----------------------
		Intent frontIntent = new Intent(context, WidgetFrontService.class);
		frontIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		frontIntent.putExtra("play", 5);
		PendingIntent frontPendingIntent = PendingIntent.getService(context, 0,
				frontIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// -----------------设置按下下一首按钮发出的广播Action-----------------------
		Intent nextIntent = new Intent(context, WidgetNextService.class);
		nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		nextIntent.putExtra("play", 5);
		PendingIntent nextPendingIntent = PendingIntent.getService(context, 0,
				nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 喜欢按钮
		Intent loveIntent = new Intent(context, MusicService.class);
		loveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Intent stopIntent = new Intent(context, MusicActivity.class);
		stopIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		stopIntent.putExtra("ooook", true);

		PendingIntent stopPendingIntent = PendingIntent.getActivity(context, 0,
				stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Intent stopIntent2 = new Intent("ggsdgvrghr");
		PendingIntent stopPendingIntent2 = PendingIntent.getBroadcast(context,
				0, stopIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

		// 得到RemoteViews对象
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_main);
		remoteViews.setOnClickPendingIntent(R.id.widget_frontBtn,
				frontPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widget_startBtn2,
				startPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widget_nextBtn,
				nextPendingIntent);
		remoteViews
				.setOnClickPendingIntent(R.id.widget_logo, stopPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widget_xiangxi,
				stopPendingIntent2);
		// 更新widget

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName componentName = new ComponentName(context,
				appWidgetProvider.class);
		appWidgetManager.updateAppWidget(componentName, remoteViews);
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		// -----------------设置按下开始按钮发出的广播Action-----------------------
		Intent startIntent = new Intent(context, WidgetPlayService.class);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startIntent.putExtra("play", 5);

		PendingIntent startPendingIntent = PendingIntent.getService(context, 0,
				startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// -----------------设置按下上一首按钮发出的广播Action-----------------------
		Intent frontIntent = new Intent(context, WidgetFrontService.class);
		frontIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		frontIntent.putExtra("play", 5);
		PendingIntent frontPendingIntent = PendingIntent.getService(context, 0,
				frontIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// -----------------设置按下下一首按钮发出的广播Action-----------------------
		Intent nextIntent = new Intent(context, WidgetNextService.class);
		nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		nextIntent.putExtra("play", 5);
		PendingIntent nextPendingIntent = PendingIntent.getService(context, 0,
				nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 喜欢按钮
		Intent loveIntent = new Intent(context, MusicService.class);
		loveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Intent stopIntent = new Intent(context, MusicActivity.class);
		stopIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		stopIntent.putExtra("ooook", true);

		PendingIntent stopPendingIntent = PendingIntent.getActivity(context, 0,
				stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Intent stopIntent2 = new Intent("ggsdgvrghr");
		PendingIntent stopPendingIntent2 = PendingIntent.getBroadcast(context,
				0, stopIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

		// 得到RemoteViews对象
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_main);
		remoteViews.setOnClickPendingIntent(R.id.widget_frontBtn,
				frontPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widget_startBtn2,
				startPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widget_nextBtn,
				nextPendingIntent);
		remoteViews
				.setOnClickPendingIntent(R.id.widget_logo, stopPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widget_xiangxi,
				stopPendingIntent2);
		// 更新widget
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	// ================添加第一个widge到桌面时调用，第二个以上不再调用，只调用一次====================
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Intent nextIntent = new Intent(context, VolumService.class);
		nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(nextIntent);
	}

	// ================删除widget时会调用==========================================================
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		super.onDeleted(context, appWidgetIds);
	}

	// ================当删除最后一个widget时才会调用===============================================
	@Override
	public void onDisabled(Context context) {

		super.onDisabled(context);
	}
}