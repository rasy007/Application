package com.sanrenx.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;


/**
 * 时间轴
 * @author 三人行技术开发团队
 *
 */
public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private List<OneStatusEntity> oneList;
	private ExpandableListView expandlistView;
	private StatusExpandAdapter statusAdapter;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = this;
		expandlistView = (ExpandableListView) findViewById(R.id.expandlist);
		putInitData();
		statusAdapter = new StatusExpandAdapter(context, oneList);
		expandlistView.setAdapter(statusAdapter);
		expandlistView.setGroupIndicator(null);
		// 去掉默认带的箭头
		// 遍历所有group,将所有项设置成默认展开
		int groupCount = expandlistView.getCount();
		for (int i = 0; i < groupCount; i++) {
			expandlistView.expandGroup(i);
		}
		expandlistView.setOnGroupClickListener(new OnGroupClickListener() {
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
	}

	private void putInitData() {
		String[] strArray = new String[] { "贷款", "更名", "交接" };
		String[] str1 = new String[] { "经理送件", "银行送件审核", "银行评估", "买卖双方签约" };
		String[] str2 = new String[] { "更名", "划首付", "买方取产权证", "物业维修基金更名",
				"土地证更名" };
		String[] str3 = new String[] { "买方到银行抵押手续", "买方取他向权利证", "银行给卖方划尾款",
				"全部办结" };

		String[] timeStr1 = new String[] { "2013-11-02 13:16:22",
				"2013-11-02 13:16:22", "2013-11-02 13:16:22",
				"2013-11-02 13:16:22" };
		String[] timeStr2 = new String[] { "2013-11-02 13:16:22",
				"2013-11-02 13:16:22", "", "", "" };
		String[] timeStr3 = new String[] { "", "", "", "" };

		oneList = new ArrayList<OneStatusEntity>();
		for (int i = 0; i < strArray.length; i++) {
			OneStatusEntity one = new OneStatusEntity();
			one.setStatusName(strArray[i]);
			List<TwoStatusEntity> twoList = new ArrayList<TwoStatusEntity>();
			String[] order = str1;
			String[] time = timeStr1;
			switch (i) {
			case 0:
				order = str1;
				time = timeStr1;
				Log.i(TAG, "str1");
				break;
			case 1:
				order = str2;
				time = timeStr2;
				Log.i(TAG, "str2");
				break;
			case 2:
				order = str3;
				time = timeStr3;
				Log.i(TAG, "str3");
				break;
			}

			for (int j = 0; j < order.length; j++) {
				TwoStatusEntity two = new TwoStatusEntity();
				two.setStatusName(order[j]);
				if (time[j].equals("")) {
					two.setCompleteTime("暂无");
					two.setIsfinished(false);
				} else {
					two.setCompleteTime(time[j] + " 完成");
					two.setIsfinished(true);
				}

				twoList.add(two);
			}
			one.setTwoList(twoList);
			oneList.add(one);
		}
		Log.i(TAG, "二级状态：" + oneList.get(0).getTwoList().get(0).getStatusName());

	}

}