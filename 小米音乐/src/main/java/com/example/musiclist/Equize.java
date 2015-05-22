package com.example.musiclist;

import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.sava.EquzeHelper;
import com.cn.sava.MusicNum;
import com.cn.ui.MySeekBar;
import com.cn.ui.ScreenInfo;
import com.cn.ui.MySeekBar.OnSeekBarChangeListener;

public class Equize extends Activity {
	private Close close;
	private int maxVolume, currentVolume;// 最大音量
	private AudioManager audioManager;// 音量管理者
	short minEQLevel = -1000, maxEQLevel = 1000, band;
	static String zidai[] = { "普通", "古典", "舞曲", "民谣", "重金属", "嘻哈", "爵士", "流行",
			"摇滚" };
	private Audiachange receiver322;
	MySeekBar bar2, bar3, bar4, bar5, bar6;
	MySeekBar bar1;
	ImageView equalizer_surface_bg;
	LinearLayout dongtailine;
	boolean choose = false;
	private Button equzesave, equzechoosebut;
	private static final String TAG = "AudioFxActivity";
	static PreferenceService service;
	int prog[][] = { { 0, 0, 0, 0, 0 }, { 416, 200, -166, 332, 332 },
			{ 500, 0, 166, 332, 83 }, { 200, 0, 0, 166, -83 },
			{ 332, 83, 747, 200, 0 }, { 416, 200, 0, 83, 200 },
			{ 332, 166, -166, 166, 416 }, { -83, 166, 416, 83, -166 },
			{ 416, 250, -83, 250, 416 } };
	int b0, b1, b2, b3, b4;
	int b[] = { b0, b1, b2, b3, b4 };
	int w, h;
	DrawView view;
	ImageView inggebgadjust;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mainequze);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		ImageButton equze_back = (ImageButton) this
				.findViewById(R.id.musiclist_back);
		ImageButton musiclist_play = (ImageButton) this
				.findViewById(R.id.musiclist_play);
		TextView titlename = (TextView) this.findViewById(R.id.titlename);
		titlename.setText("均衡器");
		musiclist_play.setVisibility(View.GONE);
		equze_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		equalizer_surface_bg = (ImageView) this
				.findViewById(R.id.equalizer_surface_bg);
		inggebgadjust = (ImageView) this.findViewById(R.id.inggebgadjust);
		dongtailine = (LinearLayout) this.findViewById(R.id.dongtailine);
		equzesave = (Button) this.findViewById(R.id.equzesave);
		equzechoosebut = (Button) this.findViewById(R.id.equzechoose);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获得最大音量
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 获得当前音量
		service = new PreferenceService(this);
		ScreenInfo s = new ScreenInfo(Equize.this);
		LinearLayout.LayoutParams paramsss = (LinearLayout.LayoutParams) inggebgadjust
				.getLayoutParams();

		w = s.getWidth();
		h = s.getHeight() / 93 * 9;
		view = new DrawView(this);
		view.setMinimumHeight(300);
		view.setMinimumWidth(300);
		view.invalidate();
		dongtailine.addView(view);
		paramsss.width = w;
		paramsss.height = w * 403 / 360;
		inggebgadjust.setLayoutParams(paramsss);

		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		equzesave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		receiver322 = new Audiachange();
		IntentFilter filter322 = new IntentFilter("cn.com.karl.progress");
		this.registerReceiver(receiver322, filter322);

		bar1 = (MySeekBar) findViewById(R.id.barvolum);
		bar2 = (MySeekBar) findViewById(R.id.bar2);
		bar3 = (MySeekBar) findViewById(R.id.bar3);
		bar4 = (MySeekBar) findViewById(R.id.bar4);
		bar5 = (MySeekBar) findViewById(R.id.bar5);
		bar6 = (MySeekBar) findViewById(R.id.bar6);
		Map<String, String> params00 = service.getPreferences00();
		Map<String, String> params11 = service.getPreferences01();
		Map<String, String> params22 = service.getPreferences02();
		Map<String, String> params33 = service.getPreferences03();
		Map<String, String> params44 = service.getPreferences04();

		int params[] = { Integer.valueOf(params00.get("progress0")),
				Integer.valueOf(params11.get("progress1")),
				Integer.valueOf(params22.get("progress2")),
				Integer.valueOf(params33.get("progress3")),
				Integer.valueOf(params44.get("progress4")) };
		final MySeekBar bar[] = { bar2, bar3, bar4, bar5, bar6 };

		for (int i = 0; i <= 8; i++) {
			final int n = i;
			if (params[0] == (prog[n][0] + maxEQLevel)
					&& params[1] == (prog[n][1] + maxEQLevel)
					&& params[2] == (prog[n][2] + maxEQLevel)
					&& params[3] == (prog[n][3] + maxEQLevel)
					&& params[4] == (prog[n][4] + maxEQLevel)) {
				equzechoosebut.setText(zidai[n]);
				break;
			} else {
				equzechoosebut.setText("手动");
			}
		}
		bar1.setMax(maxVolume);
		bar1.setProgress(currentVolume);
		bar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(MySeekBar VerticalSeekBar) {
			}

			@Override
			public void onStartTrackingTouch(MySeekBar VerticalSeekBar) {
			}

			@Override
			public void onProgressChanged(MySeekBar VerticalSeekBar,
					int progress, boolean fromUser) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						progress, AudioManager.FLAG_ALLOW_RINGER_MODES);
			}
		});

		for (int i = 0; i < 5; i++) {
			final int n = i;
			bar[i].setMax(2000);
			bar[i].setProgress(params[n]);
			b[n] = params[n] * h / 2000;
			view.invalidate();
			bar[n].setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(MySeekBar VerticalSeekBar) {
				}

				@Override
				public void onStartTrackingTouch(MySeekBar VerticalSeekBar) {
				}

				@Override
				public void onProgressChanged(MySeekBar VerticalSeekBar,
						int val, boolean fromUser) {
					EquzeHelper.setEe(n, val);
					if (MusicService.player != null) {
						Intent intent = new Intent(Equize.this,
								MusicService.class);
						intent.putExtra("progress", val);
						intent.putExtra("number", n);
						MusicNum.putplay(14);
						MusicNum.putisok(true);
						startService(intent);
					}

					if (n == 0) {
						b[0] = val * h / 2000;
					}
					if (n == 1) {
						b[1] = val * h / 2000;
					}
					if (n == 2) {
						b[2] = val * h / 2000;
					}
					if (n == 3) {
						b[3] = val * h / 2000;
					}
					if (n == 4) {
						b[4] = val * h / 2000;
					}
					if (!choose) {
						equzechoosebut.setText("手动");
					}
					view.invalidate();
				}
			});
		}
		equzechoosebut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				choose = true;
				new AlertDialog.Builder(Equize.this)
						.setItems(zidai, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								equzechoosebut.setText(zidai[arg1]);
								for (int i = 0; i <= arg1; i++) {
									final int n = i;
									for (int j = 0; j <= 4; j++) {
										final int m = j;
										bar[m].setProgress(prog[n][m]
												+ maxEQLevel);
										try {
											MusicService.bar[4 - m]
													.setProgress(prog[n][m]
															+ maxEQLevel);

										} catch (Exception e) {
										}
									}
								}
							}
						}).create().show();
			}
		});
	}

	public class DrawView extends View {
		public DrawView(Context context) {
			super(context);
		}

		
		@SuppressLint("DrawAllocation")
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			// 创建画笔
			Paint p = new Paint();
			p.setShadowLayer(12, 12, 12, Color.parseColor("#4b4fff"));
			p.setColor(Color.parseColor("#4b4fff"));
			p.setAntiAlias(true);
			p.setTypeface(Typeface.DEFAULT_BOLD);
			canvas.drawLine(14, h - b[0], w / 4, h - b[1], p);// 斜线
			canvas.drawLine(w / 4, h - b[1], w / 2, h - b[2], p);// 斜线
			canvas.drawLine(w / 2, h - b[2], w * 3 / 4, h - b[3], p);// 斜线
			canvas.drawLine(w * 3 / 4, h - b[3], w - 14, h - b[4], p);// 斜线
		}
	}

	@Override
	protected void onDestroy() {
		service.band0(TAG, bar2.getProgress());
		service.band1(TAG, bar3.getProgress());
		service.band2(TAG, bar4.getProgress());
		service.band3(TAG, bar5.getProgress());
		service.band4(TAG, bar6.getProgress());
		this.unregisterReceiver(receiver322);
		this.unregisterReceiver(close);
		super.onDestroy();
	}

	public class Audiachange extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			currentVolume = audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);// 获得当前音量
			bar1.setProgress(currentVolume);
		}
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}
