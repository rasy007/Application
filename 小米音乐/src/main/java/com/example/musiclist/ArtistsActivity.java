package com.example.musiclist;

import java.util.ArrayList;
import java.util.List;
import com.cn.sava.Indexviewpager;
import com.cn.sava.MusicNum;
import com.cn.ui.MenuTouch;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ArtistsActivity extends Activity {
	private ListView artistListView;
	static int[] musicnum = new int[2000];
	static PreferenceService service;
	private Close close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.artist);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.title_bar);
		// ImageButton equze_back = (ImageButton) this
		// .findViewById(R.id.musiclist_back);
		// ImageButton musiclist_play = (ImageButton) this
		// .findViewById(R.id.musiclist_play);
		// TextView titlename = (TextView) this.findViewById(R.id.titlename);
		// titlename.setText("歌手");
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		/*
		 * musiclist_play.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { Intent intent = new
		 * Intent(ArtistsActivity.this, MusicActivity.class);
		 * startActivity(intent); } }); equze_back.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { finish(); } });
		 */
		service = new PreferenceService(this);

		artistListView = (ListView) this.findViewById(R.id.musiclistevery);

		ArtistsAdapter adapter = new ArtistsAdapter(this,
				MusicList9.getMusicData(this));
		artistListView.setAdapter(adapter);
		artistListView.setOnTouchListener(new MenuTouch());
		artistListView.setSelection(Indexviewpager.getartistlistposition());
		artistListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent1 = new Intent(ArtistsActivity.this,
						MusicService.class);
				MusicNum.putplay(8);
				MusicNum.putisok(true);
				intent1.putExtra("_id", musicnum[arg2]);
				startService(intent1);
				Intent intent = new Intent(ArtistsActivity.this,
						MusicActivity.class);
				startActivity(intent);
			}
		});
		artistListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				Indexviewpager.putartistlistposition(arg1);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public static class MusicList9 {

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
							m.setName(name);
							id++;
							if ("<unknown>".equals(singer)
									|| "未知".equals(singer) || "".equals(singer)
									|| singer.contains("QQ")
									|| singer.contains("qq")
									|| singer.contains("www.")
									|| singer.contains("〖")
									|| singer.contains("（")
									|| singer.contains("http://")
									|| singer.contains("《")) {

							} else {
								musicList.add(m);
								musicnum[i] = id;
								i++;
							}

						}
					} while (cursor.moveToNext());
					service.savesinger("singer", Integer.valueOf(i));
				}

				if (cursor != null) {
					cursor.close();
				}

			}
			return musicList;
		}

	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}
