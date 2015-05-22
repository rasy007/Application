package com.cn.sava;

public class Indexviewpager {
	private static int index=0;
	private static int mainlistposition=0;
	private static int albumlistposition=0;
	private static int artistlistposition=0;
	private static int musicitem=1;
	private static int current=5;
	public static void putcurrent(int streamVolume){
		current=streamVolume;
	}
	public static int getputcurrent(){
		return current;
	}
	
public static void putint(int t){
	index=t;
}
public static int getputint(){
	return index;
}

public static void putmusic(int t){
	musicitem=t;
}
public static int getputmusic(){
	return musicitem;
}

public static void putmainlistposition(int t){
	mainlistposition=t;
}
public static int getmainlistposition(){
	return mainlistposition;
}

public static void putalbumlistposition(int selectedItemPosition){
	albumlistposition=selectedItemPosition;
}
public static int getalbumlistposition(){
	return albumlistposition;
}
public static void putartistlistposition(int selectedItemPosition) {
	artistlistposition=selectedItemPosition;
}
public static int getartistlistposition(){
	return artistlistposition;
}



}
