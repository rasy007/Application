package com.youyamusic.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.musiclist.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

public class YouYaDownLoad extends AsyncTask<Void, Integer, String> {

	private String mUrl;
	private Context mContext;
	private String filedir;
	private NotificationManager mNotiManager;
	private Notification mNoti;
	private NotificationCompat.Builder mBuilder;
	private static final int notificationId = (int) System.currentTimeMillis();

	public YouYaDownLoad(Context context, String url) {
		mUrl = url;
		mContext = context.getApplicationContext();
		filedir = Environment.getExternalStorageDirectory().toString()
				+ "/youyamusic/update";
		mNotiManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(mContext);
		mBuilder.setAutoCancel(false);
		mBuilder.setContentTitle("正在下载更新");
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setProgress(100, 0, false);
		mBuilder.setWhen(System.currentTimeMillis());
		mBuilder.setTicker("正在下载更新");
		mNoti = mBuilder.build();
		mNoti.flags = Notification.FLAG_SHOW_LIGHTS;
	}

	@Override
	protected void onPreExecute() {
		mNoti = mBuilder.build();
		mNoti.flags = Notification.FLAG_SHOW_LIGHTS;
		mNoti.defaults = Notification.DEFAULT_SOUND;
		mNoti.ledARGB = 0xff00ffff;
		mNoti.ledOffMS = 2000;
		mNoti.ledOnMS = 2000;
		mNotiManager.notify(notificationId, mNoti);
		super.onPreExecute();
	}

	@SuppressWarnings("resource")
	@Override
	protected String doInBackground(Void... params) {
		String filePath = null;
		InputStream is = null;
		FileOutputStream fos = null;
		HttpURLConnection conn = null;
		RandomAccessFile raf = null;
		try {
			long total = UpgradeUtil.getNetFileSize(mUrl);
			URL url = new URL(mUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(1000 * 10);
			conn.setRequestMethod("GET");
			File upgradeDir = new File(filedir);
			if (!upgradeDir.exists()) {
				upgradeDir.mkdir();
			}
			File upgrade = new File(upgradeDir, "YouYaMusic.apk");
			if (upgrade.exists()) {
				upgrade.delete();
			}
			conn.connect();
			is = conn.getInputStream();
			UpgradeUtil.deleteContentsOfDir(upgradeDir);
			fos = new FileOutputStream(upgrade);
			long size = 0;
			int count = 0;
			byte[] temp = new byte[1024 * 60];
			count = is.read(temp, 0, temp.length);
			int preciousProgress = 0;
			while (count > 0) {
				if (isCancelled()) {
					return null;
				}
				fos.write(temp, 0, count);
				size += count;
				int progress = (int) (size * 100 / total);
				if (progress - preciousProgress > 0) {
					publishProgress(progress);
					preciousProgress = progress;
				}
				count = is.read(temp, 0, temp.length);
			}
			fos.flush();
			filePath = upgrade.getAbsolutePath();

		} catch (MalformedURLException e) {
			cancel(true);
			return null;
		} catch (IOException e) {
			cancel(true);
			return null;
		} finally {
			UpgradeUtil.closeInputStream(is);
			UpgradeUtil.closeOutputStream(fos);
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
				}
			}
		}
		return filePath;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (values[0] < 100) {
			mBuilder.setContentText("已完成：" + " " + values[0] + "%");
			mNoti = mBuilder.setProgress(100, values[0], false).build();
			mNotiManager.notify(notificationId, mNoti);
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (TextUtils.isEmpty(result))
			return;
		super.onPostExecute(result);
		Intent promptInstall = new Intent(Intent.ACTION_VIEW).setDataAndType(
				Uri.fromFile(new File(result)),
				"application/vnd.android.package-archive");
		promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mBuilder.setTicker("下载完成");
		mBuilder.setContentTitle("下载完成");
		mBuilder.setContentIntent(PendingIntent.getActivity(mContext, 0,
				promptInstall, PendingIntent.FLAG_UPDATE_CURRENT));
		mBuilder.setContentText("已完成：" + " " + 100 + "%");
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mNoti = mBuilder.setProgress(100, 100, false).build();
		mNoti.flags = Notification.FLAG_SHOW_LIGHTS;
		mNoti.defaults = Notification.DEFAULT_SOUND;
		mNoti.ledARGB = 0xff00ffff;
		mNoti.ledOffMS = 2000;
		mNoti.ledOnMS = 2000;
		mNotiManager.notify(notificationId, mNoti);
		mContext.startActivity(promptInstall);
	}
}