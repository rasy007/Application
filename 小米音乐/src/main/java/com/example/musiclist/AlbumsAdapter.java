package com.example.musiclist;

import java.util.List;

import com.example.love.ToTime;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlbumsAdapter extends BaseAdapter {
	ToTime time;
	private List<Music> listMusic;
	private Context context;

	public AlbumsAdapter(Context context, List<Music> listMusic) {
		this.context = context;
		this.listMusic = listMusic;
	}

	public void setListItem(List<Music> listMusic) {
		this.listMusic = listMusic;
	}

	@Override
	public int getCount() {
		return listMusic.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listMusic.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		time = new ToTime();

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.album_item, null);
		}
		Music m = listMusic.get(position);
		// 音乐名
		TextView textMusicName = (TextView) convertView
				.findViewById(R.id.album_item_name);
		textMusicName.setText(m.getSinger());
		// 歌手
		TextView textMusicSinger = (TextView) convertView
				.findViewById(R.id.album_item_singer);
		textMusicSinger.setText(m.getName().subSequence(0,
				m.getName().length() - 4));
		// 持续时间
		TextView textMusicTime = (TextView) convertView
				.findViewById(R.id.album_item_time);
		textMusicTime.setText(time.toTime((int) m.getTime()));

		return convertView;
	}
}
