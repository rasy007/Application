package com.cn.lyric;

import java.util.ArrayList;
import java.util.List;

import com.example.musiclist.MusicActivity;
import com.example.musiclist.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NoActivity extends Activity {
	LinearLayout noshow;
	private Animation myAnimation, myAnimationbbb;
	boolean isview = false;
	ListView noshowlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noshow);
		noshow = (LinearLayout) this.findViewById(R.id.noshow);
		noshowlist = (ListView) this.findViewById(R.id.noshowlist);
		noshowlist.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, getData()));
		myAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_z);
		myAnimationbbb = AnimationUtils.loadAnimation(this, R.anim.alpha_x);

		noshow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showo();
			}
		});
		noshowlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showo();
			}
		});
	}

	protected void showo() {
		if (!isview) {
			MusicActivity.music_equze.startAnimation(myAnimation);
			MusicActivity.imageBtnRandom.startAnimation(myAnimation);
			MusicActivity.music_equze.setVisibility(View.VISIBLE);
			MusicActivity.imageBtnRandom.setVisibility(View.VISIBLE);
			isview = true;
		} else {
			MusicActivity.music_equze.startAnimation(myAnimationbbb);
			MusicActivity.music_equze.setVisibility(View.INVISIBLE);
			MusicActivity.imageBtnRandom.startAnimation(myAnimationbbb);
			MusicActivity.imageBtnRandom.setVisibility(View.INVISIBLE);
			isview = false;
		}
	}

	private List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("");
		return data;
	}
}
