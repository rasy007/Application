package com.example.musiclist;

import com.cn.sava.MusicNum;
import com.cn.ui.ScreenInfo;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Sleep extends Activity {
	private Button sleepqueding, sleepquxiao;
	private TextView sleeptext;
	SeekBar sleepseekbar;
	private Close close;
	RelativeLayout sleeptextrela;
	ScreenInfo s;
    int w;
    float pre=0;
    TranslateAnimation animation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleep);
		sleepqueding = (Button) this.findViewById(R.id.sleepqueding);
		sleepquxiao = (Button) this.findViewById(R.id.sleepquxiao);
		sleeptext = (TextView) this.findViewById(R.id.sleeptext);
		sleepseekbar = (SeekBar) this.findViewById(R.id.sleepseekbar);
		sleeptextrela = (RelativeLayout) this.findViewById(R.id.sleeptextrela);
        s=new ScreenInfo(Sleep.this);
        w=s.getWidth()*13/20;
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);

		sleepquxiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		sleepseekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				 animation=new TranslateAnimation(pre, arg1 * w / 95, 0, 0);
				 animation.setFillAfter(true);
				 animation.setDuration(200);
				 sleeptextrela.startAnimation(animation);

				 sleeptext.setText(String.valueOf(arg1 + 5));
				pre=arg1 * w / 95;
			}
		});
		sleepqueding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (MusicService.player == null) {
					Toast.makeText(getApplicationContext(), "音乐还未开启！",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(Sleep.this,
							com.cn.sava.SleepService.class);
					intent.putExtra("sleeptime",
							(sleepseekbar.getProgress() + 5) * 60);
					MusicNum.putisok(true);
					startService(intent);
					if (sleepseekbar.getProgress() >= 55) {
						Toast.makeText(
								getApplicationContext(),
								"1小时" + (sleepseekbar.getProgress() - 55)
										+ "分钟后自动关闭音乐", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(getApplicationContext(),
								sleepseekbar.getProgress() + 5 + "分钟后自动关闭音乐",
								Toast.LENGTH_SHORT).show();
					}
					finish();

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
