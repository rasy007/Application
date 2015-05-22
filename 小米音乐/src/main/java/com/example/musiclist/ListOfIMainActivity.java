package com.example.musiclist;

import java.util.List;

import com.cn.sava.Indexviewpager;
import com.cn.sava.MusicNum;
import com.cn.ui.ScreenInfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;

public class ListOfIMainActivity extends Activity {
	private ListView listView;
	private Close close;
	private ImageButton musiclist_play, musiclist_back;
	int lastX = 0;
	int lastY = 0;
	ScreenInfo s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.iactivity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		listView = (ListView) this.findViewById(R.id.musiclistevery);
		listView.setBackgroundResource(R.drawable.music_land_bg);
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		s = new ScreenInfo(ListOfIMainActivity.this);
		List<Music> listMusic = MusicList.getMusicData(getApplicationContext());
		musiclist_play = (ImageButton) this.findViewById(R.id.musiclist_play);
		musiclist_back = (ImageButton) this.findViewById(R.id.musiclist_back);
		musiclist_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ListOfIMainActivity.this,
						MusicActivity.class);
				startActivity(intent);
			}
		});
		musiclist_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		MusicAdapter adapter = new MusicAdapter(ListOfIMainActivity.this,
				listMusic);
		listView.setAdapter(adapter);
		MusicList.getMusicData(this);
		listView.setSelection(Indexviewpager.getmainlistposition());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent1 = new Intent(ListOfIMainActivity.this,
						MusicService.class);
				MusicNum.putplay(8);
				MusicNum.putisok(true);
				intent1.putExtra("_id", arg2);
				startService(intent1);

				Intent intent = new Intent(ListOfIMainActivity.this,
						MusicActivity.class);
				startActivity(intent);
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				Indexviewpager.putmainlistposition(arg1);
			}
		});
	}

	@Override
	protected void onDestroy() {
		listView.setAdapter(null);
		this.unregisterReceiver(close);
		super.onDestroy();
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}
