package com.cn.lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

@SuppressLint("UseValueOf")
public class IndexLrc {

	public static boolean haslrc = false;
	public static TreeMap<Integer, LyricObject> lrc_map;
	Context context;

	public IndexLrc(Context context) {
		this.context = context;
		lrc_map = new TreeMap<Integer, LyricObject>();
	}

	@SuppressLint("UseValueOf")
	public static boolean read(String file) {
		TreeMap<Integer, LyricObject> lrc_read = new TreeMap<Integer, LyricObject>();
		String data = "";
		try {
			File saveFile = new File(file);
			if (!saveFile.isFile()) {
				haslrc = false;
				return false;
			}
			haslrc = true;
			FileInputStream stream = new FileInputStream(saveFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					stream, "utf-8"));
			Pattern pattern = Pattern.compile("\\d{2}");
			while ((data = br.readLine()) != null) {
				data = data.replace("[", "");// 将前面的替换成后面的
				data = data.replace("]", "@");
				String splitdata[] = data.split("@");// 分隔
				if (data.endsWith("@")) {
					for (int k = 0; k < splitdata.length; k++) {
						String str = splitdata[k];
						str = str.replace(":", ".");
						str = str.replace(".", "@");
						String timedata[] = str.split("@");
						Matcher matcher = pattern.matcher(timedata[0]);
						if (timedata.length == 3 && matcher.matches()) {
							int m = Integer.parseInt(timedata[0]); // 分
							int s = Integer.parseInt(timedata[1]); // 秒
							int ms = Integer.parseInt(timedata[2]); // 毫秒
							int currTime = (m * 60 + s) * 1000 + ms * 10;
							LyricObject item1 = new LyricObject();
							item1.begintime = currTime;
							item1.lrc = "";
							lrc_read.put(currTime, item1);
						}
					}
				} else {
					String lrcContenet = splitdata[splitdata.length - 1];
					for (int j = 0; j < splitdata.length - 1; j++) {
						String tmpstr = splitdata[j];
						tmpstr = tmpstr.replace(":", ".");
						tmpstr = tmpstr.replace(".", "@");
						String timedata[] = tmpstr.split("@");
						Matcher matcher = pattern.matcher(timedata[0]);
						if (timedata.length == 3 && matcher.matches()) {
							int m = Integer.parseInt(timedata[0]); // 分
							int s = Integer.parseInt(timedata[1]); // 秒
							int ms = Integer.parseInt(timedata[2]); // 毫秒
							int currTime = (m * 60 + s) * 1000 + ms * 10;
							LyricObject item1 = new LyricObject();
							item1.begintime = currTime;
							item1.lrc = lrcContenet;
							Log.i("lrcContenet", lrcContenet);
							lrc_read.put(currTime, item1);// 将currTime当标签
						}
					}
				}
			}

			stream.close();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
		}
		/*
		 * 遍历hashmap 计算每句歌词所需要的时间
		 */
		try {
			lrc_map.clear();
		} catch (Exception e) {
		}

		data = "";
		Iterator<Integer> iterator = lrc_read.keySet().iterator();
		LyricObject oldval = null;
		int i = 0;
		while (iterator.hasNext()) {
			try {

				Object ob = iterator.next();
				LyricObject val = (LyricObject) lrc_read.get(ob);
				if (oldval == null)
					oldval = val;
				else {
					LyricObject item1 = new LyricObject();
					item1 = oldval;
					item1.timeline = val.begintime - oldval.begintime;
					lrc_map.put(new Integer(i), item1);
					i++;
					oldval = val;
				}
				if (!iterator.hasNext()) {
					lrc_map.put(new Integer(i), val);
				}
			} catch (Exception e) {
			}
		}
		return haslrc;
	}
}
