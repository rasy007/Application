package com.youyamusic.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class Packagemanage {
	private Context context;

	public Packagemanage(Context context1) {
		this.context = context1;
	}

	public int getVersion() {
		int versionCode = 0;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionCode = info.versionCode; // 1
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
}
