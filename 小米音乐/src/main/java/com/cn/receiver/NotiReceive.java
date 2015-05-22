package com.cn.receiver;

import com.cn.sava.MusicNum;
import com.example.musiclist.MusicService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotiReceive extends BroadcastReceiver {
	static boolean isnoty = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intent2 = new Intent(context, MusicService.class);
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MusicNum.putplay(3);
		MusicNum.putisok(true);
		context.startService(intent2);
		isnoty = true;
	}

	public static boolean getnoti() {
		return isnoty;
	}

	public static void putnoti(boolean b) {
		isnoty = b;
	}
}