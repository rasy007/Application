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
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class Search extends Activity {
	private static EditText edit_search;
	private ListView lv;
	private TextView resultsearch;
	static int[] musicnum = new int[3000];
	private Close close;
	String musicname;
	List<Music> list = new ArrayList<Music>();
	List<Music> newlist = new ArrayList<Music>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sousuo);
		edit_search = (EditText) this.findViewById(R.id.edit_search);
		edit_search.addTextChangedListener(new TextWatcher_Enum());
		resultsearch = (TextView) this.findViewById(R.id.resultsearch);
		lv = (ListView) this.findViewById(R.id.musiclistevery);
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		List<Music> listMusic = MusicList.getMusicData(getApplicationContext());
		MusicAdapter adapter = new MusicAdapter(Search.this, listMusic);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (edit_search.getText().toString().equals("")) {

					MusicNum.putplay(1);
					MusicNum.put_id(arg2);
					// / Intent intent = new
					// Intent(Search.this,MusicActivity.class);
					// startActivity(intent);
					finish();

					Intent intent1 = new Intent(Search.this, MusicService.class);
					// intent1.putExtra("play",8);
					MusicNum.putplay(8);
					MusicNum.putisok(true);
					intent1.putExtra("_id", arg2);
					startService(intent1);

				} else {
					MusicNum.putplay(1);
					MusicNum.put_id(musicnum[arg2]);
					// Intent intent = new
					// Intent(Search.this,MusicActivity.class);
					// startActivity(intent);
					finish();

					Intent intent1 = new Intent(Search.this, MusicService.class);
					// intent1.putExtra("play",8);
					MusicNum.putplay(8);
					MusicNum.putisok(true);
					intent1.putExtra("_id", musicnum[arg2]);
					startService(intent1);

				}
			}
		});
	}

	public static class MusicList2 {

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
					int id = -1;
					int i = 0;
					do {
						// Log.i("id", String.valueOf(id));
						Music m = new Music();
						String title = cursor.getString(cursor
								.getColumnIndex(MediaStore.Audio.Media.TITLE));

						String singer = cursor.getString(cursor
								.getColumnIndex(MediaStore.Audio.Media.ARTIST));
						if ("<unknown>".equals(singer)) {
							singer = "未知艺术家";
						}
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
							m.setName(name);
							id++;

							if (title
									.contains(edit_search.getText().toString())) {
								musicList.add(m);
								musicnum[i] = id;
								i++;
							}

						}
					} while (cursor.moveToNext());
					if (cursor != null) {
						cursor.close();
					}
				}
				if (cursor != null) {
					cursor.close();
				}
			}
			return musicList;
		}

	}

	class TextWatcher_Enum implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			if (edit_search.getText().toString().equals("")) {

				List<Music> listMusic = MusicList
						.getMusicData(getApplicationContext());
				MusicAdapter adapter = new MusicAdapter(Search.this, listMusic);
				lv.setAdapter(adapter);
				resultsearch.setText("");

			} else {
				List<Music> listMusic = MusicList2
						.getMusicData(getApplicationContext());
				MusicAdapter adapter2 = new MusicAdapter(Search.this, listMusic);
				lv.setAdapter(adapter2);
				resultsearch
						.setText(String.valueOf(listMusic.size()) + "条搜索结果");
			}
		}

		@Override
		public void afterTextChanged(Editable s) {

		}

	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}