package com.example.musiclist;

import java.util.List;

import com.example.love.ToTime;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArtistsAdapter extends BaseAdapter {

	private List<Music> listMusic;
	private Context context;
	ToTime time;

	public ArtistsAdapter(Context context, List<Music> listMusic) {
		this.context = context;
		this.listMusic = listMusic;
	}

	public void setListItem(List<Music> listMusic) {
		this.listMusic = listMusic;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMusic.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listMusic.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		time = new ToTime();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.singer_item, null);
		}
		Music m = listMusic.get(position);
		// 音乐名
		TextView textMusicName = (TextView) convertView
				.findViewById(R.id.simger_item_name);
		textMusicName.setText(m.getSinger());
		// 歌手
		TextView textMusicSinger = (TextView) convertView
				.findViewById(R.id.simger_item_singer);
		textMusicSinger.setText(m.getAlbum());

		// 持续时间
		TextView textMusicTime = (TextView) convertView
				.findViewById(R.id.simger_item_time);
		textMusicTime.setText(time.toTime((int) m.getTime()));

		return convertView;
	}
}