package com.cn.lyric;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.cn.sava.MusicNum;
import com.example.musiclist.Music;
import com.example.musiclist.MusicList;
import com.example.musiclist.MusicService;
import com.example.musiclist.R;

public class LyricActivity extends Activity {
	private LyricView lrc_view;
	private ListView erllist;
	private MusicPlay6er receiver9;
	private List<Music> lists;
	private String mp3Path;
	public static boolean searching = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyricview);
		lrc_view = (LyricView) findViewById(R.id.LyricShow);
		erllist = (ListView) this.findViewById(R.id.erllist);
		erllist.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, getData()));
		lists = MusicList.getMusicData(getApplicationContext());
		receiver9 = new MusicPlay6er();
		IntentFilter filter9 = new IntentFilter("com.cn.musicserviceplayer");
		this.registerReceiver(receiver9, filter9);
		mp3Path = Environment.getExternalStorageDirectory().toString()
				+ "/youyamusic/lrc/";
		SerchLrc();
		lrc_view.SetTextSize();
		new Thread(new runable()).start();
	}

	private List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("");
		data.add("");
		data.add("");
		data.add("");
		return data;
	}

	public void SerchLrc() {
		String lrc = mp3Path;
		lrc = lrc
				+ lists.get(MusicService._id).getName().replace(".mp3", ".lrc");
		// LyricView.read(lrc);
		IndexLrc.read(lrc);
		lrc_view.SetTextSize();
		lrc_view.setOffsetY(350);
	}

	@Override
	protected void onStart() {
		lrc_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						LyricActivity.this);
				LayoutInflater factory = LayoutInflater
						.from(LyricActivity.this);
				final View textEntryView = factory.inflate(R.layout.lrcsearch,
						null);
				builder.setIcon(R.drawable.icon_scan_all);
				builder.setTitle("歌词查找");
				final EditText songnamesearch = (EditText) textEntryView
						.findViewById(R.id.songnamesearch);
				final EditText singernamesearch = (EditText) textEntryView
						.findViewById(R.id.singernamesearch);

				if (MusicService.player != null) {
					songnamesearch.setText(lists.get(MusicService._id)
							.getTitle());
				}
				builder.setView(textEntryView);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if (MusicNum.getbtn(1)) {
									if (!songnamesearch.getText().toString()
											.equals("")
											&& songnamesearch.getText()
													.toString() != null) {
										Intent intent = new Intent(
												"com.musiclist.searchlrc");
										intent.putExtra("songname",
												songnamesearch.getText()
														.toString());

										intent.putExtra("singername",
												singernamesearch.getText()
														.toString());
										sendBroadcast(intent);
									}
								} else {
									Toast.makeText(getApplicationContext(),
											"设置中未允许下载歌词", 1).show();
								}
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});
				builder.create().show();
			}
		});

		super.onStart();
	}

	public class MusicPlay6er extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			SerchLrc();
			lrc_view.SetTextSize();
		}
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(receiver9);
		super.onDestroy();
	}

	class runable implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(100);
					if (MusicService.player.isPlaying()) {
						lrc_view.setOffsetY(lrc_view.getOffsetY()
								- lrc_view.SpeedLrc() + 8);
						lrc_view.SelectIndex(MusicService.player
								.getCurrentPosition());
						mHandler.post(mUpdateResults);
					}
				} catch (Exception e) {
				}
			}
		}
	}

	Handler mHandler = new Handler();
	Runnable mUpdateResults = new Runnable() {
		public void run() {
			lrc_view.invalidate(); // 更新视图
		}
	};
}
