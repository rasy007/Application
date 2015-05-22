package com.example.musiclist;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.cn.sava.Indexviewpager;
import com.cn.sava.MusicNum;
import com.cn.ui.ImageBg;
import com.cn.ui.WidgetBitmap;
import com.example.love.ToTime;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "DefaultLocale", "NewApi", "ShowToast" })
public class MusicActivity extends ActivityGroup {
	String KEY[] = { "remember4", "remember5", "remember9", "remember10",
			"remember12", "remember13", "nowplay", "prefsname" };
	private SharedPreferences mSettings = null;
	private TextView textName, textSinger, allmusic, currentmusic, textEndTime;
	static TextView textStartTime;
	private ImageButton imageBtnRewind;
	public static ImageButton music_equze;
	private ImageButton imageBtnForward;
	public static ImageButton imageBtnRandom;
	private ImageButton play_back;
	public ImageButton imageBtnPlay;
	ToTime totime;
	String time, musicna;
	static SeekBar seekBar1;
	private List<Music> lists;
	SharedPreferences.Editor saveput;
	SharedPreferences saveget;
	boolean exist;
	private MusicPlay6er receiver9;
	private Close close;
	int progress;
	static ImageView imagebg, imagebg2, backalpha;
	ImageView page_icon;
	public ImageView love;
	static PreferenceService service;
	private int page[] = { R.drawable.page_icon_left, R.drawable.page_icon_mid,
			R.drawable.page_icon_right };
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	Animation chuxian, yincang;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.music);
		lists = MusicList.getMusicData(this);
		service = new PreferenceService(this);
		mSettings = getSharedPreferences(KEY[7], Context.MODE_PRIVATE);
		saveget = getSharedPreferences("shoucang", 0);
		saveput = getSharedPreferences("shoucang", 0).edit();
		totime = new ToTime();
		// 找到控件
		findviews();
		// 注册广播3个
		register();
		// 按钮点击监听
		buttonclick();
		InItView();
		viewPager.setAdapter(new myPagerView());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				setSelector(arg0);
			}

			@Override
			public void onPageScrolled(int paramInt, float paramFloat, int arg2) {
				Indexviewpager.putmusic(paramInt);

				if (paramInt == 0 && arg2 != 0) {
					if (paramFloat < 0.25) {
						paramFloat = 0.25f;
					}
					try {
						backalpha.setAlpha(1 - paramFloat);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (paramInt == 1 && arg2 != 0) {
					if (paramFloat < 0) {
						paramFloat = 0f;
					}
					if (paramFloat > 0.75) {
						paramFloat = 0.75f;
					}
					try {
						backalpha.setAlpha(paramFloat);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				// 结束拖动
				if (lists.size() > 0) {
					if (MusicService.player != null) {
						MusicService.player.seekTo(seekBar.getProgress()
								* MusicService.player.getDuration() / 1000);
					} else {
						Music m = lists.get(MusicService._id);
						service.save3(
								textStartTime.getText().toString(),
								Integer.valueOf((int) (seekBar1.getProgress()
										* m.getTime() / 1000)));
					}
					seekBar1.setProgress(seekBar.getProgress());
				} else {
					Toast.makeText(getApplicationContext(), "列表为空，定位失败！", 1)
							.show();
				}
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// 开始拖动
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// 正在拖动
				if (lists.size() > 0) {
					Music m = lists.get(MusicService._id);
					seekBar1.setProgress(seekBar.getProgress());
					textStartTime.setText(totime.toTime((int) (progress
							* m.getTime() / 1000)));
				}
			}
		});
	}

	Bitmap bitmap;

	public void setSelector(int id) {
		for (int i = 0; i < page.length; i++) {
			if (id == i) {
				// 设置底部图片
				page_icon.setImageResource(page[i]);
				viewPager.setCurrentItem(i);
			}
		}
	}

	void InItView() {
		pageViews = new ArrayList<View>();

		View view01 = getLocalActivityManager().startActivity("activity01",
				new Intent(this, com.example.musiclist.MainActivity.class))
				.getDecorView();

		View view02 = getLocalActivityManager().startActivity("activity02",
				new Intent(this, com.cn.lyric.NoActivity.class)).getDecorView();

		View view03 = getLocalActivityManager().startActivity("activity03",
				new Intent(this, com.cn.lyric.LyricActivity.class))
				.getDecorView();
		pageViews.add(view01);
		pageViews.add(view02);
		pageViews.add(view03);
	}

	private void buttonclick() {
		imageBtnRewind.setOnClickListener(new MyListener());
		imageBtnPlay.setOnClickListener(new MyListener());
		imageBtnForward.setOnClickListener(new MyListener());
		imageBtnRandom.setOnClickListener(new MyListener());
		play_back.setOnClickListener(new MyListener());
		music_equze.setOnClickListener(new MyListener());
	}

	private void register() {
		receiver9 = new MusicPlay6er();
		IntentFilter filter9 = new IntentFilter("com.cn.musicserviceplayer");
		this.registerReceiver(receiver9, filter9);

		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);

	}

	private void findviews() {
		viewPager = (ViewPager) findViewById(R.id.pager);
		chuxian = AnimationUtils.loadAnimation(this, R.anim.alpha_z);
		yincang = AnimationUtils.loadAnimation(this, R.anim.alpha_x);
		textName = (TextView) this.findViewById(R.id.music_nameq);
		textSinger = (TextView) this.findViewById(R.id.music_singerq);
		textStartTime = (TextView) this.findViewById(R.id.music_start_time);
		textEndTime = (TextView) this.findViewById(R.id.music_end_time);
		currentmusic = (TextView) this.findViewById(R.id.currentmusic);
		allmusic = (TextView) this.findViewById(R.id.allmusic);
		seekBar1 = (SeekBar) this.findViewById(R.id.music_seekBar);
		imageBtnRewind = (ImageButton) this.findViewById(R.id.music_rewind);
		imageBtnPlay = (ImageButton) this.findViewById(R.id.music_play);
		music_equze = (ImageButton) this.findViewById(R.id.music_equze);
		play_back = (ImageButton) this.findViewById(R.id.play_back);
		imageBtnForward = (ImageButton) this.findViewById(R.id.music_foward);
		imageBtnRandom = (ImageButton) this.findViewById(R.id.music_random);
		love = (ImageView) this.findViewById(R.id.love);
		imagebg = (ImageView) this.findViewById(R.id.imagebg);
		imagebg2 = (ImageView) this.findViewById(R.id.imagebg2);
		backalpha = (ImageView) this.findViewById(R.id.backalpha);
		page_icon = (ImageView) this.findViewById(R.id.pageicon);
		allmusic.setText(String.valueOf(lists.size()));

	}

	@Override
	protected void onStart() {
		// 得到上次保存的歌曲
		Map<String, String> params = service.getPreferences();
		Map<String, String> params3 = service.getPreferences3();
		if (MusicService.player == null) {
			int ida = Integer.valueOf(params.get("currentId"));
			progress = Integer.valueOf(params3.get("progress"));
			String time = params3.get("time");
			try {
				Music m = lists.get(ida);

				seekBar1.setProgress((int) (progress * 1000 / m.getTime()));
				textStartTime.setText(time);

				if (lists.size() > 0) {
					currentmusic.setText(String.valueOf(ida + 1));
				} else {
					currentmusic.setText("0");
				}
				textName.setText(m.getTitle());
				if (m.getSinger().equals("未知艺术家")) {
					textSinger.setText("music");
				} else {
					textSinger.setText(m.getSinger());
				}
				textEndTime.setText(totime.toTime((int) m.getTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (MusicService.nowplay) {
			Music m = lists.get(MusicService._id);
			textEndTime.setText(totime.toTime((int) m.getTime()));
			textName.setText(m.getTitle());
			if (m.getSinger().equals("未知艺术家")) {
				textSinger.setText("music");
			} else {
				textSinger.setText(m.getSinger());
			}
			if (lists.size() > 0)
				currentmusic.setText(String.valueOf(MusicService._id + 1));
			else
				currentmusic.setText("0");

			imageBtnPlay.setBackgroundResource(R.drawable.pause1);
		} else {
			Music m = lists.get(MusicService._id);
			seekBar1.setProgress((int) (MusicService.player
					.getCurrentPosition() * 1000 / MusicService.player
					.getDuration()));
			textStartTime.setText(totime.toTime(MusicService.player
					.getCurrentPosition()));
			if (lists.size() > 0)
				currentmusic.setText(String.valueOf(MusicService._id));
			else
				currentmusic.setText("0");
			textName.setText(m.getTitle());
			if (m.getSinger().equals("未知艺术家")) {
				textSinger.setText("music");
			} else {
				textSinger.setText(m.getSinger());
			}
			textEndTime.setText(totime.toTime((int) MusicService.player
					.getDuration()));
		}

		if (WidgetBitmap.getisopen() == 0) {
			SharedPreferences localSharedPreferences = getSharedPreferences(
					"music", 0);

			int a = Integer.valueOf(localSharedPreferences.getInt("currentId",
					0));
			String albumArt = getAlbumArt(a);

			if (albumArt == null || albumArt.equals("")) {
				ImageBg.setBitmap(null);
				ImageBg.bitmap = null;
			} else {
				Bitmap bm = BitmapFactory.decodeFile(albumArt);
				ImageBg.setBitmap(bm);
			}

			Uri uri = Uri.parse(localSharedPreferences.getString("background",
					""));
			ContentResolver contentResolver = this.getContentResolver();
			Bitmap bitmap2 = null;
			if (String.valueOf(uri).length() > 3) {
				try {
					bitmap2 = BitmapFactory.decodeStream(contentResolver
							.openInputStream(uri));
					ImageBg.setback(bitmap2);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				}
			}
			WidgetBitmap.setisopen(1);
		}

		super.onStart();
	}

	private String getAlbumArt(int album_id) {
		String mUriAlbums = "content://media/external/audio/albums";
		String[] projection = new String[] { "album_art" };
		Cursor cur = this.getContentResolver().query(
				Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
				projection, null, null, null);
		String album_art = null;
		if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
			cur.moveToNext();
			album_art = cur.getString(0);
		}
		cur.close();
		cur = null;
		return album_art;
	}

	Runnable runnable2 = new Runnable() {
		@Override
		public void run() {
			handler2.postDelayed(runnable2, 100);
			if (MusicService.player != null) {
				seekBar1.setProgress((int) (MusicService.player
						.getCurrentPosition() * 1000 / MusicService.player
						.getDuration()));
				seekBar1.invalidate();
				textStartTime.setText(totime.toTime((int) MusicService.player
						.getCurrentPosition()));
			}
		}
	};

	@Override
	protected void onResume() {
		if (MusicNum.getbtn(8)) {
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		if (MusicNum.getbtn(20)) {
			MusicNum.putusbtn(20, false);
		}

		setSelector(Indexviewpager.getputmusic());
		if (Indexviewpager.getputmusic() == 0
				|| Indexviewpager.getputmusic() == 2) {
			backalpha.setAlpha(0.75f);
		}

		exist = getexist(String.valueOf(MusicService._id));
		if (exist) {
			love.setImageResource(R.drawable.menu_add_to_favorite_n);
		} else {
			love.setImageResource(R.drawable.menu_add_to_favorite_d);
		}
		love.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (lists.size() > MusicService._id) {
					if (!exist) {
						addtosave(String.valueOf(MusicService._id));
						love.setImageResource(R.drawable.menu_add_to_favorite_n); // 变白
						Toast.makeText(getApplicationContext(), "已添加到收藏列表",
								Toast.LENGTH_SHORT).show();
						exist = true;
					} else if (exist) {
						try {
							delfromsave(String.valueOf(MusicService._id));
							love.setImageResource(R.drawable.menu_add_to_favorite_d); // 变暗
							Toast.makeText(getApplicationContext(), "已从收藏列表移除",
									Toast.LENGTH_SHORT).show();
							exist = false;
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "抱歉，删除失败！",
									Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Toast.makeText(getApplicationContext(), "抱歉，操作失败！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		if (MusicNum.getbtn(11) && !MusicNum.getbtn(12)) {
			imageBtnRandom.setBackgroundResource(R.drawable.play_loop_specok);
		} else if (MusicNum.getbtn(12) && !MusicNum.getbtn(11)) {
			imageBtnRandom.setBackgroundResource(R.drawable.play_random_selok);
		} else if (!MusicNum.getbtn(12) && !MusicNum.getbtn(11)) {
			imageBtnRandom.setBackgroundResource(R.drawable.play_loop_selok);
		}
		handler2.postDelayed(runnable2, 100);
		setbackground();
		super.onResume();
	}

	private void setbackground() {
		if (ImageBg.getBitmap() != null) {
			imagebg.startAnimation(chuxian);
			imagebg.setImageBitmap(ImageBg.getBitmap());
		} else {
			if (ImageBg.getback() != null) {
				imagebg.startAnimation(chuxian);
				imagebg.setImageBitmap(ImageBg.getback());
			} else {
				imagebg.startAnimation(chuxian);
				imagebg.setImageBitmap(null);
				imagebg.setImageResource(R.drawable.video_stub_large);
			}
		}
		m.postDelayed(r, 500);
	}

	Handler m = new Handler();
	Runnable r = new Runnable() {
		@Override
		public void run() {
			if (ImageBg.getBitmap() != null) {
				imagebg2.setImageBitmap(ImageBg.getBitmap());
			} else {
				if (ImageBg.getback() != null) {
					imagebg2.setImageBitmap(ImageBg.getback());
				} else {
					imagebg2.setImageBitmap(null);
					imagebg2.setImageResource(R.drawable.video_stub_large);
				}
			}
		}
	};

	Handler handler2 = new Handler();

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(close);
		this.unregisterReceiver(receiver9);
		super.onDestroy();
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}

	// prepare完成后
	public class MusicPlay6er extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Music m = lists.get(MusicService._id);
			textEndTime.setText(totime.toTime((int) m.getTime()));
			textName.setText(m.getTitle());
			if (m.getSinger().equals("未知艺术家")) {
				textSinger.setText("music");
			} else {
				textSinger.setText(m.getSinger());
			}
			if (lists.size() > 0)
				currentmusic.setText(String.valueOf(MusicService._id + 1));
			else
				currentmusic.setText("0");
			imageBtnPlay.setBackgroundResource(R.drawable.pause1);
			exist = getexist(String.valueOf(MusicService._id));
			if (exist) {
				love.setImageResource(R.drawable.menu_add_to_favorite_n);
			} else {
				love.setImageResource(R.drawable.menu_add_to_favorite_d);
			}
			setbackground();
		}
	}

	// 各种按键监听器（不修改）
	private class MyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == imageBtnRewind) { // 上一首
				MusicNum.putint(4);
				Intent intent = new Intent(MusicActivity.this,
						MusicService.class);
				MusicNum.putplay(7);
				MusicNum.putisok(true);
				startService(intent);
			} else if (v == imageBtnPlay) {
				// 正在播放
				if (MusicService.player != null && MusicService.nowplay) {
					imageBtnPlay.setBackgroundResource(R.drawable.play1);
				} else {
					if (lists.size() > 0)
						imageBtnPlay.setBackgroundResource(R.drawable.pause1);
				}
				Intent intent = new Intent(MusicActivity.this,
						MusicService.class);
				MusicNum.putplay(3);
				MusicNum.putisok(true);
				startService(intent);

			} else if (v == imageBtnForward) {
				MusicNum.putint(3);
				Intent intent = new Intent(MusicActivity.this,
						MusicService.class);
				MusicNum.putplay(5);
				MusicNum.putisok(true);
				startService(intent);
				// 下一首
			} else if (v == imageBtnRandom) {
				// 随机播放
				if (MusicNum.getbtn(12) == true && MusicNum.getbtn(11) == false) { // 点了随机变循环
					imageBtnRandom
							.setBackgroundResource(R.drawable.play_loop_selok); // xunhuan
					MusicNum.putusbtn(11, false);
					MusicNum.putusbtn(12, false);
					saveRemember13(false);
					saveRemember12(false);
				} else if (MusicNum.getbtn(11) == true
						&& MusicNum.getbtn(12) == false) { // 点了单曲变随机
					imageBtnRandom
							.setBackgroundResource(R.drawable.play_random_selok);// suiji
					MusicNum.putusbtn(11, false);
					MusicNum.putusbtn(12, true);

					saveRemember13(true);
					saveRemember12(false);
				} else if (!MusicNum.getbtn(11) && !MusicNum.getbtn(12)) { // 点了循环变单曲
					imageBtnRandom
							.setBackgroundResource(R.drawable.play_loop_specok);// danqu
					MusicNum.putusbtn(11, true);
					MusicNum.putusbtn(12, false);
					saveRemember13(false);
					saveRemember12(true);
				}

			} else if (v == music_equze) {
				Intent intent = new Intent(MusicActivity.this, Equize.class);
				startActivity(intent);
			} else if (v == play_back) {
				finish();
			}
		}
	}

	// 记住随机循环（不修改）
	private void saveRemember12(boolean remember12) {
		Editor editor = mSettings.edit();// 获取编辑器
		editor.putBoolean(KEY[4], remember12);
		editor.commit();
	} // 获取保存的用户名

	// 记住循环和随机是否开启
	private void saveRemember13(boolean remember13) {
		Editor editor = mSettings.edit();// 获取编辑器
		editor.putBoolean(KEY[5], remember13);
		editor.commit();
	} // 获取保存的用户名

	// 单曲循环和随机播放图标显示和隐藏（不修改）
	class myPagerView extends PagerAdapter {
		// 显示数目
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add(0,1,0,"退出"); //添加选项
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("ShowToast")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.exit) {
			Intent intent = new Intent("com.sleep.close");
			sendBroadcast(intent);
			finish();
		}
		if (item.getItemId() == R.id.about) {
			Intent intent1 = new Intent(MusicActivity.this, About.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.deletecurrent) {
			if (MusicService.nowplay) {
				File f = new File(lists.get(MusicService._id).getUrl());

				if (f.exists()) {
					try {
						f.delete();
						if (MusicService.his > 0) {
							MusicService.his -= 1;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					MusicNum.putint(3);
					Intent intent = new Intent(MusicActivity.this,
							MusicService.class);
					MusicNum.putplay(5);
					MusicNum.putisok(true);
					startService(intent);
					Toast.makeText(getApplicationContext(), "删除成功！", 1).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "抱歉,当前没有歌曲在播放！", 1)
						.show();
			}
		}
		if (item.getItemId() == R.id.entersleep) {
			Intent intent1 = new Intent(MusicActivity.this, Sleep.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.menusearch) {
			Intent intent1 = new Intent(MusicActivity.this, Search.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.setting) {
			Intent intent1 = new Intent(MusicActivity.this, Setting.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.settingring) {
			if (MusicService.player == null) {
				Toast.makeText(getApplicationContext(), "当前音乐为空！", 1).show();
			} else {
				try {
					Music m = lists.get(MusicService._id);
					String path = m.getUrl();
					setMyRingtone(path);
					Toast.makeText(getApplicationContext(), "设置铃声成功！", 1)
							.show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "设置铃声失败！", 1)
							.show();
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public void setMyRingtone(String path) {
		File file = new File(path);
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(file
				.getAbsolutePath());
		Uri newUri = this.getContentResolver().insert(uri, values);
		RingtoneManager.setActualDefaultRingtoneUri(this,
				RingtoneManager.TYPE_RINGTONE, newUri);
	}

	public boolean getexist(String id) {
		String all = saveget.getString("shoucang", "");
		String cuid = id;
		if (cuid.length() == 1) {
			cuid = cuid + "###";
		}
		if (cuid.length() == 2) {
			cuid = cuid + "##";
		}
		if (cuid.length() == 3) {
			cuid = cuid + "#";
		}
		if (all.contains(cuid)) {
			return true;
		} else {
			return false;
		}
	}

	public void addtosave(String id) {
		String all = saveget.getString("shoucang", "");
		String cuid = id;
		if (cuid.length() == 1) {
			cuid = cuid + "###";
		}
		if (cuid.length() == 2) {
			cuid = cuid + "##";
		}
		if (cuid.length() == 3) {
			cuid = cuid + "#";
		}
		all = all + cuid;
		saveput.putString("shoucang", all);
		saveput.commit();
	}

	public void delfromsave(String id) {
		String all = saveget.getString("shoucang", "");
		Log.i("shoucang", saveget.getString("shoucang", ""));
		String cuid = id;
		if (cuid.length() == 1) {
			cuid = cuid + "###";
		}
		if (cuid.length() == 2) {
			cuid = cuid + "##";
		}
		if (cuid.length() == 3) {
			cuid = cuid + "#";
		}
		all = all.replace(cuid, "");
		saveput.putString("shoucang", all);
		saveput.commit();
	}
}
