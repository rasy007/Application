package com.example.musiclist;

import java.util.ArrayList;
import java.util.List;

import com.cn.sava.MusicNum;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RecentlyActivity extends Activity {
	private ListView albumListView;
	static int[] musicnum = new int[2000];
	private Close close;
	SharedPreferences.Editor saveput;
	SharedPreferences saveget;
	int n = 0;
	static String later[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.recently);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		ImageButton equze_back = (ImageButton) this
				.findViewById(R.id.musiclist_back);
		ImageButton musiclist_play = (ImageButton) this
				.findViewById(R.id.musiclist_play);
		TextView titlename = (TextView) this.findViewById(R.id.titlename);
		titlename.setText("我的收藏");

		saveget = getSharedPreferences("shoucang", 0);
		saveput = getSharedPreferences("shoucang", 0).edit();
		String name = saveget.getString("shoucang", "");
		n = 0;
		later = new String[name.length() / 4];

		for (int i = 0; i < name.length(); i = i + 4) {
			later[n] = name.substring(i, i + 4);

			if (later[n].substring(3, 4).equals("#")) {
				later[n] = later[n].substring(0, 3);
				if (later[n].substring(2, 3).equals("#")) {
					later[n] = later[n].substring(0, 2);
					if (later[n].substring(1, 2).equals("#")) {
						later[n] = later[n].substring(0, 1);
					}
				}
			}
			n++;
		}

		musiclist_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RecentlyActivity.this,
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
		albumListView = (ListView) this.findViewById(R.id.musiclistevery);

		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);

		MusicAdapter adapter = new MusicAdapter(this,
				MusicListr.getMusicData(this));
		albumListView.setAdapter(adapter);
		albumListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(RecentlyActivity.this,
						MusicActivity.class);
				startActivity(intent);

				Intent intent1 = new Intent(RecentlyActivity.this,
						MusicService.class);
				MusicNum.putplay(8);
				MusicNum.putisok(true);
				intent1.putExtra("_id", musicnum[arg2]);
				startService(intent1);

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
						if (sbr.equals("mp3") && (time>=1000 && time <=900000)) {
							m.setTitle(title);
							m.setSinger(singer);
							m.setAlbum(album);
							m.setSize(size);
							m.setTime(time);
							m.setUrl(url);
							id++;
							m.setName(name);
							if (exist(id)) {
								musicList.add(m);
								musicnum[i] = id;
								i++;
							}
						}
					} while (cursor.moveToNext());
				}
				if (cursor != null) {
					cursor.close();
				}
				if (cr != null) {
					cr = null;
				}
			}
			return musicList;
		}
	}

	public static boolean exist(int id) {
		boolean a = false;
		for (int i = 0; i < later.length; i++) {
			if (String.valueOf(id).equals(later[i])) {
				a = true;
			}
		}
		return a;
	}
}
