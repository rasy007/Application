package com.cn.sava;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
public class SleepService extends Service{
	int time=0;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
    private int count = 0;   
    private boolean run = false;   
    private Handler handler = new Handler();   
    private Runnable task = new Runnable() {
        public void run() {
            if (run) {
                count++;
                handler.postDelayed(task, 1000); 
                Log.i("count", String.valueOf(count));
            }
            if(count>time){
            	Intent intent=new Intent("com.sleep.close");
    			sendBroadcast(intent);
            	run=false;
            }
        }
    };
	@Override
	public void onCreate() {
		super.onCreate();
	}
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		if(MusicNum.getisok()){
		time=0;
		time=intent.getIntExtra("sleeptime", 0);
		Log.i("count", String.valueOf(time));
		run = true;
		handler.postDelayed(task, 1000); 
		MusicNum.putisok(false);
		MusicNum.putplay(0);
		super.onStart(intent, startId);}
	}
}
