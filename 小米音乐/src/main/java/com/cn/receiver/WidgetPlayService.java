package com.cn.receiver;

import com.cn.sava.MusicNum;
import com.example.musiclist.MusicService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WidgetPlayService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		int a = 0;
		try {
			a = intent.getIntExtra("play", 0);
			Log.i("a", a + "-");
		} catch (NullPointerException e) {
			e.printStackTrace();
			a = 0;
		}
		if (a != 0) {
			MusicNum.putplay(3);
			MusicNum.putisok(true);
			Intent intent2 = new Intent(WidgetPlayService.this,
					MusicService.class);
			startService(intent2);
		}
		super.onStart(intent, startId);
	}

}