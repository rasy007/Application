package com.youyamusic.update;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.example.musiclist.R;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class SearchUpdate extends AsyncTask<String, Integer, String> {
	public Context context;
	boolean istoast = false;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	public SearchUpdate(Context context3, boolean toa) {
		this.context = context3;
		this.istoast = toa;
	}

	@Override
	protected String doInBackground(String... params) {
		String version = "";
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(HttpUtils.getXML(params[0]));
			NodeList nodeList = document.getElementsByTagName("version");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element2 = (Element) nodeList.item(i);
				NodeList childNodes = element2.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
						if ("id".equals(childNodes.item(j).getNodeName())) {
							version = childNodes.item(j).getFirstChild()
									.getNodeValue();
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return version;
	}

	@SuppressWarnings({ "deprecation" })
	@SuppressLint("ShowToast")
	@Override
	protected void onPostExecute(String result) {
		Packagemanage packagemanage = new Packagemanage(context);
		int version = 0;
		try {
			version = Integer.valueOf(result);
		} catch (Exception e) {

		}
		if (version > packagemanage.getVersion()) {
			String title = "检测到有更新版本";
			String content = "点击下载更新";
			int icon = R.drawable.ic_launcher;
			Notification notification = new Notification(icon, title,
					System.currentTimeMillis());
			Intent intent = new Intent("com.youyamusic.update");
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					10, intent, 0);
			notification.setLatestEventInfo(context, title, content,
					pendingIntent);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			NotificationManager manager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(100, notification);
		} else {
			if (istoast) {
				Toast.makeText(context, "暂无更新", 1).show();
			}
		}
		super.onPostExecute(result);
	}
}