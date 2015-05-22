package com.cn.sava;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import android.util.Log;

public class DebugMessage {

	public static void put(String address, String content, String lrcname) {
		try {
			FileOutputStream outStream = new FileOutputStream(address + lrcname
					+ ".lrc", true);
			OutputStreamWriter writer = new OutputStreamWriter(outStream,
					"utf-8");
			writer.write(content);
			writer.write("/n");
			writer.flush();
			writer.close();// 记得关闭
			outStream.close();
		} catch (Exception e) {
			Log.e("m", "file write error");
		}
	}
}
