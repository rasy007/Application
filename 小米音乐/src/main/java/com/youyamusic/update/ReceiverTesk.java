package com.youyamusic.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverTesk extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		YouYaDownLoad mDownloadTask = new YouYaDownLoad(context,
				"http://www.dtdatong.com/temp/youyaupdate.apk");
		mDownloadTask.execute();
	}
}