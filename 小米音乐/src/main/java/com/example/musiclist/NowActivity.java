package com.example.musiclist;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import com.cn.sava.MusicNum;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NowActivity extends Activity {
	private ListView nowplaylistview;
	static int[] musicnum = new int[2000];
	private Close close;
	static PreferenceService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.nowplay);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		ImageButton equze_back = (ImageButton) this
				.findViewById(R.id.musiclist_back);
		ImageButton musiclist_play = (ImageButton) this
				.findViewById(R.id.musiclist_play);
		TextView titlename = (TextView) this.findViewById(R.id.titlename);
		titlename.setText("最近播放");
		musiclist_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(NowActivity.this,
						MusicActivity.class);
				startActivity(intent);
			}
		});
		equze_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		nowplaylistview = (ListView) this.findViewById(R.id.musiclistevery);
		service = new PreferenceService(this);
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		MusicAdapter adapter = new MusicAdapter(this,
				MusicListr.getMusicData(this));
		nowplaylistview.setAdapter(adapter);

		nowplaylistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent1 = new Intent(NowActivity.this,
						MusicService.class);
				MusicNum.putplay(8);
				MusicNum.putisok(true);
				intent1.putExtra("_id", musicnum[arg2]);
				startService(intent1);
				Intent intent = new Intent(NowActivity.this,
						MusicActivity.class);
				startActivity(intent);
			}
		});
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();

		}
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(close);
		super.onDestroy();
	}

	public static class MusicListr {
		public static List<Music> getMusicData(Context context) {
			List<Music> musicList = new ArrayList<Music>();
			ContentResolver cr = context.getContentResolver();
			if (cr != null) {
				// 获取所有歌曲
				Cursor cursor = cr.query(
						MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
						null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
				if (null == cursor) {
					return null;
				}
				if (cursor.moveToFirst()) {
					int id = -1, i = 0;
					do {
						Music m = new Music();
						String title = cursor.getString(cursor
								.getColumnIndex(MediaStore.Audio.Media.TITLE));
						String singer = cursor.getString(cursor
								.getColumnIndex(MediaStore.Audio.Media.ARTIST));
						String album = cursor.getString(cursor
								.getColumnIndex(MediaStore.Audio.Media.ALBUM));
						long size = cursor.getLong(cursor
								.getColumnIndex(MediaStore.Audio.Media.SIZE));
						long time = cursor
								.getLong(cursor
										.getColumnIndex(MediaStore.Audio.Media.DURATION));
						String url = cursor.getString(cursor
								.getColumnIndex(MediaStore.Audio.Media.DATA));
						String name = cursor
								.getString(cursor
										.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
						String sbr = name.substring(name.length() - 3,
								name.length());
						if (sbr.equals("mp3")
								&& (time >= 1000 && time <= 900000)) {
							m.setTitle(title);
							m.setSinger(singer);
							m.setAlbum(album);
							m.setSize(size);
							m.setTime(time);
							m.setUrl(url);
							id = id + 1;
							m.setName(name);

							for (int j = 0; j < 299; j++) {
								if (id == MusicService.history[j]
										&& MusicService.history[j] > 0) {
									musicList.add(m);
									musicnum[i] = id;
									i++;
								}
							}
						}
					} while (cursor.moveToNext());
				}
				if (cursor != null) {
					cursor.close();
				}
			}
			return musicList;
		}
	}
}
