package com.example.musiclist;

import com.cn.sava.MusicNum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class BaiduOnline extends Activity {
	ImageButton baidu_back;
	WebView mWebView;
	private Close close;
	String urii = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.baiduonline);
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.baiduonlinetitle_bar);
		baidu_back = (ImageButton) this.findViewById(R.id.baidu_back);
		baidu_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	protected void onStart() {
		if (MusicNum.getbtn(2)) {
			urii = getIntent().getStringExtra("url");
			setControl();
			setWebStyle();
			if (MusicService.player != null)
				MusicService.player.pause();
			MusicNum.putser(true);
		} else {
			Toast.makeText(getApplicationContext(), "请到设置开启允许在线音乐",
					Toast.LENGTH_SHORT).show();
		}
		super.onStart();
	}

	private void setControl() {
		mWebView = (WebView) findViewById(R.id.webshow);
	}

	private void setWebStyle() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.requestFocus();
		mWebView.loadUrl(urii);
		mWebView.setWebViewClient(new MyWebViewClient());
	}

	class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url_) {
			view.loadUrl(url_);
			return true;
		}
	}

	@Override
	protected void onDestroy() {
		if (mWebView != null) {
			mWebView.clearHistory();
			mWebView.removeAllViewsInLayout();
			mWebView.clearDisappearingChildren();
			mWebView.clearFocus();
			mWebView.clearView();
			mWebView.destroy();
		}
		this.unregisterReceiver(close);
		if (MusicService.nowplay && MusicNum.getser()) {
			MusicService.player.start();
		}
		super.onDestroy();
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}
