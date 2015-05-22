package com.example.musiclist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.cn.sava.MusicNum;
import com.cn.ui.ImageBg;
import com.example.love.SlipButton;
import com.youyamusic.update.SearchUpdate;

@SuppressLint("NewApi")
public class Setting extends Activity {
	public static final String PREFS_NAME = "prefsname";
	String KEY[] = { "remember1", "remember2", "remember3", "remember4",
			"remember5", "remember6", "remember7", "remember8", "remember9",
			"remember10", "remember11", "remember12" };
	private SharedPreferences mSettings = null;
	private SeekBar shuaidong_seekBar;
	SlipButton check[];
	private Close close;
	private LinearLayout shuaige_see, linearsetting13, linearsetting12,
			linearsetting11, linearsetting10, linearsetting09, linearsetting08,
			linearsetting07, linearsetting06, linearsetting05, linearsetting04,
			linearsetting03, linearsetting02, linearsetting01;
	static PreferenceService service;
	private File picFile;
	private Uri photoUri;
	private final int activity_result_camara_with_data = 1006;
	private final int activity_result_cropimage_with_data = 1007;

	private SlipButton checkbutton01, checkbutton02, checkbutton03,
			checkbutton04, checkbutton05, checkbutton06, checkbutton07,
			checkbutton08, checkbutton09, checkbutton10, checkbutton11,
			checkbutton12;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.seeting);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		ImageButton equze_back = (ImageButton) this
				.findViewById(R.id.musiclist_back);
		ImageButton musiclist_play = (ImageButton) this
				.findViewById(R.id.musiclist_play);
		TextView titlename = (TextView) this.findViewById(R.id.titlename);
		titlename.setText("设置");
		musiclist_play.setVisibility(View.GONE);
		equze_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		service = new PreferenceService(this);
		mSettings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		shuaige_see = (LinearLayout) this.findViewById(R.id.shuaige_see);

		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		findview();

		LinearLayout linall[] = { linearsetting01, linearsetting02,
				linearsetting03, linearsetting04, linearsetting05,
				linearsetting06, linearsetting07, linearsetting08,
				linearsetting09, linearsetting10, linearsetting11,
				linearsetting12, linearsetting13 };
		shuaidong_seekBar = (SeekBar) this.findViewById(R.id.shuaidong_seekBar);
		shuaidong_seekBar.setMax(100);
		Map<String, String> params2 = service.getPreferences2();
		shuaidong_seekBar
				.setProgress(Integer.valueOf(params2.get("seekvalue")));

		shuaidong_seekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						String mingzi = "huangkaixiang";
						service.save2(mingzi, progress);
					}
				});
		final SlipButton check[] = { checkbutton01, checkbutton02,
				checkbutton03, checkbutton04, checkbutton05, checkbutton06,
				checkbutton07, checkbutton08, checkbutton09, checkbutton10,
				checkbutton11, checkbutton12 };

		// 初始时候验证是否打勾
		for (int i = 0; i <= 11; i++) {
			check[i].setChecked(mSettings.getBoolean(KEY[i], false));
			final int n = i;
			check[n].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (!check[n].isChecked()) {
						check[n].setChecked(true);
						Editor editor = mSettings.edit();// 获取编辑器
						editor.putBoolean(KEY[n], true);
						editor.commit();

						Intent intent = new Intent("cn.change.seekbar");
						intent.putExtra("change", n);
						intent.putExtra("isbtn", 1);
						sendBroadcast(intent);
						MusicNum.putusbtn(n, true);
						if (n == 5) {
							oksee();
						}
					} else {
						check[n].setChecked(false);
						Editor editor = mSettings.edit();// 获取编辑器
						editor.putBoolean(KEY[n], false);
						editor.commit();
						Intent intent = new Intent("cn.change.seekbar");
						intent.putExtra("change", n);
						intent.putExtra("isbtn", 2);
						sendBroadcast(intent);
						MusicNum.putusbtn(n, false);
						if (n == 5) {
							nosee();
						}
					}
				}
			});
		}
		for (int j = 0; j <= 11; j++) {
			final int n = j;
			linall[n].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (!check[n].isChecked()) {
						check[n].setChecked(true);
						Editor editor = mSettings.edit();// 获取编辑器
						editor.putBoolean(KEY[n], true);
						editor.commit();

						Intent intent = new Intent("cn.change.seekbar");
						intent.putExtra("change", n);
						intent.putExtra("isbtn", 1);
						sendBroadcast(intent);
						MusicNum.putusbtn(n, true);
						if (n == 5) {
							oksee();
						}
					} else {
						check[n].setChecked(false);
						Editor editor = mSettings.edit();// 获取编辑器
						editor.putBoolean(KEY[n], false);
						editor.commit();
						Intent intent = new Intent("cn.change.seekbar");
						intent.putExtra("change", n);
						intent.putExtra("isbtn", 2);
						sendBroadcast(intent);
						MusicNum.putusbtn(n, false);
						if (n == 5) {
							nosee();
						}
					}
				}
			});
		}

		linearsetting13.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 检查更新
				String path = "http://www.dtdatong.com/temp/youya.xml";
				SearchUpdate getJsonLrc = new SearchUpdate(Setting.this, true);
				getJsonLrc.execute(path);
			}
		});
	}

	private void findview() {
		checkbutton01 = (SlipButton) this.findViewById(R.id.checkbutton01);
		checkbutton02 = (SlipButton) this.findViewById(R.id.checkbutton02);
		checkbutton03 = (SlipButton) this.findViewById(R.id.checkbutton03);
		checkbutton04 = (SlipButton) this.findViewById(R.id.checkbutton04);
		checkbutton05 = (SlipButton) this.findViewById(R.id.checkbutton05);
		checkbutton06 = (SlipButton) this.findViewById(R.id.checkbutton06);
		checkbutton07 = (SlipButton) this.findViewById(R.id.checkbutton07);
		checkbutton08 = (SlipButton) this.findViewById(R.id.checkbutton08);
		checkbutton09 = (SlipButton) this.findViewById(R.id.checkbutton09);
		checkbutton10 = (SlipButton) this.findViewById(R.id.checkbutton10);
		checkbutton11 = (SlipButton) this.findViewById(R.id.checkbutton11);
		checkbutton12 = (SlipButton) this.findViewById(R.id.checkbutton12);

		linearsetting01 = (LinearLayout) this
				.findViewById(R.id.linearsetting01);
		linearsetting02 = (LinearLayout) this
				.findViewById(R.id.linearsetting02);
		linearsetting03 = (LinearLayout) this
				.findViewById(R.id.linearsetting03);
		linearsetting04 = (LinearLayout) this
				.findViewById(R.id.linearsetting04);
		linearsetting05 = (LinearLayout) this
				.findViewById(R.id.linearsetting05);
		linearsetting06 = (LinearLayout) this
				.findViewById(R.id.linearsetting06);
		linearsetting07 = (LinearLayout) this
				.findViewById(R.id.linearsetting07);
		linearsetting08 = (LinearLayout) this
				.findViewById(R.id.linearsetting08);
		linearsetting09 = (LinearLayout) this
				.findViewById(R.id.linearsetting09);
		linearsetting10 = (LinearLayout) this
				.findViewById(R.id.linearsetting10);
		linearsetting11 = (LinearLayout) this
				.findViewById(R.id.linearsetting11);
		linearsetting12 = (LinearLayout) this
				.findViewById(R.id.linearsetting12);
		linearsetting13 = (LinearLayout) this
				.findViewById(R.id.linearsetting13);
	}

	@Override
	protected void onResume() {
		if (checkbutton06.isChecked()) {
			oksee();
		} else {
			nosee();
		}
		super.onResume();
	}

	public void linearsettui(View v) {
		doPickPhotoAction();
	}

	private void doPickPhotoAction() {
		final Context dialogContext = new ContextThemeWrapper(this,
				android.R.style.Theme_Light);
		String cancel = "取消";
		String[] choices;
		choices = new String[3];
		choices[0] = "默认背景"; // 拍照
		choices[1] = "拍照"; // 拍照
		choices[2] = "从相册中选择"; // 从相册中选择
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle("选择图片");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					@SuppressLint("ShowToast")
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:
							ImageBg.setback(null);
							service.background("", 0);
							Intent intent = new Intent(
									"com.cn.musicserviceplayer");
							sendBroadcast(intent);
							break;
						case 1:
							String status = Environment
									.getExternalStorageState();
							if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
								doTakePhoto();// 用户点击了从照相机获取
							} else {
								Toast.makeText(dialogContext, "SD卡不存在", 1)
										.show();
							}
							break;
						case 2:
							doCropPhoto();// 从相册中去获取
							break;
						}
					}
				});
		builder.setNegativeButton(cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/*
	 * 拍照获取图片
	 */
	protected void doTakePhoto() {
		try {
			File uploadFileDir = new File(Environment
					.getExternalStorageDirectory().toString(),
					"/youyamusic/backbg");
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (!uploadFileDir.exists()) {
				uploadFileDir.mkdirs();
			}
			picFile = new File(uploadFileDir, "background.jpeg");
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			photoUri = Uri.fromFile(picFile);
			Log.i("photoUri", String.valueOf(photoUri));
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(cameraIntent,
					activity_result_camara_with_data);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doCropPhoto() {
		try {
			File pictureFileDir = new File(Environment
					.getExternalStorageDirectory().toString(),
					"/youyamusic/backbg");
			if (!pictureFileDir.exists()) {
				pictureFileDir.mkdirs();
			}
			picFile = new File(pictureFileDir, "background.jpeg");
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			photoUri = Uri.fromFile(picFile);
			final Intent intent = getCropImageIntent();
			startActivityForResult(intent, activity_result_cropimage_with_data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 调用图片剪辑程序 (相册)
	 */
	public Intent getCropImageIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 576);
		intent.putExtra("aspectY", 709);
		intent.putExtra("outputX", 1152);
		intent.putExtra("outputY", 1418);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		return intent;
	}

	/**
	 * 调用图片剪辑程序 (拍照)
	 */
	private void cropImageUriByTakePhoto() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 576);
		intent.putExtra("aspectY", 709);
		intent.putExtra("outputX", 1152);
		intent.putExtra("outputY", 1418);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, activity_result_cropimage_with_data);
	}

	@SuppressWarnings("unused")
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		case activity_result_camara_with_data: // 拍照
			try {
				cropImageUriByTakePhoto();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case activity_result_cropimage_with_data: // 相册
			try {
				if (data != null && resultCode != 0) {
					service.background(photoUri.toString(), 0);
					Uri uri = Uri.parse(photoUri.toString());
					ContentResolver contentResolver = this.getContentResolver();
					Bitmap bitmap2 = BitmapFactory.decodeStream(contentResolver
							.openInputStream(uri));
					ImageBg.setback(bitmap2);
					MusicNum.putusbtn(18, true);
					MusicNum.putusbtn(20, true);
					Intent intent = new Intent("com.cn.musicserviceplayer");
					sendBroadcast(intent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void nosee() {
		linearsetting07.setAlpha(0.3f);
		shuaige_see.setVisibility(View.GONE);
	}

	private void oksee() {
		shuaige_see.setVisibility(View.VISIBLE);
		linearsetting07.setAlpha(1.0f);
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(close);
		super.onDestroy();
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}
