package com.example.musiclist;

import java.util.ArrayList;
import java.util.HashMap;

import com.cn.ui.MenuTouch;
import com.example.musiclist.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class List extends Activity {
	private Close close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		ListView list = (ListView) findViewById(R.id.musiclistevery);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		String dianpuname[] = { "歌曲列表", "歌手列表", "专辑列表", "收藏列表", "最近播放", "音乐查找" };
		String miaoshu[] = { "列举出所有歌曲", "列举出所有歌手", "列举出所有专辑", "我收藏过的歌曲",
				"播放器最近播放过的音乐", "查找本地音乐" };
		for (int i = 0; i <= 5; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("em_name", dianpuname[i]);
			map.put("em_time", miaoshu[i]);
			listItem.add(map);
		}
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
				R.layout.list_items,// ListItem的XML实现
				new String[] { "em_name", "em_time" }, new int[] {
						R.id.em_name, R.id.em_time });
		list.setAdapter(listItemAdapter);
		list.setOnTouchListener(new MenuTouch());
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					Intent intent = new Intent(List.this,
							ListOfIMainActivity.class);
					startActivity(intent);
				}
				if (arg2 == 1) {
					Intent intent = new Intent(List.this,
							ListOfArtistsActivity.class);
					startActivity(intent);
				}
				if (arg2 == 2) {
					Intent intent = new Intent(List.this, AlbumsActivity.class);
					startActivity(intent);
				}
				if (arg2 == 3) {
					Intent intent = new Intent(List.this,
							RecentlyActivity.class);
					startActivity(intent);
				}
				if (arg2 == 4) {
					Intent intent = new Intent(List.this, NowActivity.class);
					startActivity(intent);
				}
				if (arg2 == 5) {
					Intent intent = new Intent(List.this, Search.class);
					startActivity(intent);
				}
			}
		});
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}
