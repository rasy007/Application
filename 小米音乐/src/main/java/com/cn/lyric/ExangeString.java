package com.cn.lyric;

public class ExangeString {
	public static String[] extra = { ":", ",", "!", "~", ".", "/", "?", "[",
			"]", "'", "@", "#", "$", "%", "&", "*", "(", ")", "-", "=", "+",
			"|", "。", "，", "、" };

	public static String exange(String name) {
		for (int i = 0; i < extra.length; i++) {
			name.replace(extra[i], "");
		}
		return name;
	}
}
