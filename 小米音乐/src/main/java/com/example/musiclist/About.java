package com.example.musiclist;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class About extends Activity {
	private Button xianshishuru, tijiao;
	private EditText liuyan;
	private Close close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);

		xianshishuru = (Button) this.findViewById(R.id.xianshishuru);
		tijiao = (Button) this.findViewById(R.id.tijiao);
		liuyan = (EditText) this.findViewById(R.id.liuyan);
		xianshishuru.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				liuyan.setVisibility(View.VISIBLE);
				tijiao.setVisibility(View.VISIBLE);
				xianshishuru.setVisibility(View.GONE);
			}
		});
		tijiao.setOnClickListener(new OnClickListener() {
			@SuppressLint("UnlocalizedSms")
			@Override
			public void onClick(View v) {
				String content = liuyan.getText().toString();
				if (content.equals("")) {
					Toast.makeText(getApplicationContext(), "信息为空，发送失败",
							Toast.LENGTH_SHORT).show();
				} else {
					SmsManager manager = SmsManager.getDefault();
					ArrayList<String> texts = manager.divideMessage(content);
					for (String text : texts) {
						manager.sendTextMessage("18720094195", null,
								"【来自优雅音乐的建议】：" + text, null, null);
					}
					Toast.makeText(getApplicationContext(), "成功！感谢您的建议",
							Toast.LENGTH_SHORT).show();
					liuyan.setText("");
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
