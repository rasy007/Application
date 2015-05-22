package com.example.musiclist;

import java.util.ArrayList;
import java.util.List;

import com.cn.sava.Indexviewpager;
import com.cn.sava.MusicNum;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AlbumsActivity extends Activity {
	private ListView albumListView;
	static int[] musicnum = new int[2000];
	private Close close;
	static PreferenceService service;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.albums);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		ImageButton equze_back = (ImageButton) this
				.findViewById(R.id.musiclist_back);
		ImageButton musiclist_play = (ImageButton) this
				.findViewById(R.id.musiclist_play);
		TextView titlename = (TextView) this.findViewById(R.id.titlename);
		titlename.setText("专辑");
		musiclist_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AlbumsActivity.this,
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
		service = new PreferenceService(this);
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		albumListView.setSelection(Indexviewpager.getalbumlistposition());
		AlbumsAdapter adapter = new AlbumsAdapter(this,
				MusicListr.getMusicData(this));
		albumListView.setAdapter(adapter);

		albumListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent1 = new Intent(AlbumsActivity.this,
						MusicService.class);
				MusicNum.putplay(8);
				MusicNum.putisok(true);
				intent1.putExtra("_id", arg2);
				startService(intent1);
				Intent intent = new Intent(AlbumsActivity.this,
						MusicActivity.class);
				startActivity(intent);
			}
		});
		albumListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				Indexviewpager.putalbumlistposition(arg1);
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
							if ("<unknown>".equals(singer)
									|| "未知".equals(singer)
									|| singer.contains("qq")
									|| singer.contains("《")
									|| singer.contains("》")
									|| singer.contains("（")) {
							} else {
								musicList.add(m);
								musicnum[i] = id;
								i++;
							}
						}
					} while (cursor.moveToNext());
					service.savealbum("singer", Integer.valueOf(i));
				}
				if (cursor != null) {
					cursor.close();
				}
			}
			return musicList;
		}
	}
}
