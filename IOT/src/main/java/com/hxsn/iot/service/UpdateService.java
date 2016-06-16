package com.hxsn.iot.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.hxsn.iot.R;
import com.hxsn.iot.activity.AiotActivity;
import com.hxsn.iot.util.FileUtil;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateService extends Service {
	private static final int TIMEOUT = 10 * 1000;
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;

	private String app_name;
	private String url;

	private NotificationManager notificationManager;
	private Notification notification;

	private Intent updateIntent;
	private PendingIntent pendingIntent;

	private int notification_id = 1;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		app_name = intent.getStringExtra("app_name");
		url = intent.getStringExtra("url");
		//FileUtil.createFile(app_name);
		FileUtil.createFile("sn-aiot-phone-1.0");
		createNotification();
		createThread();

		return super.onStartCommand(intent, flags, startId);

	}

	public void createThread() {
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_OK:
					
					Uri uri = Uri.fromFile(FileUtil.updateFile);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "application/vnd.android.package-archive");

					pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);

					notification = new Notification.Builder(UpdateService.this)
							.setContentTitle("New mail from " + app_name)
							.setContentText(getString(R.string.update_download_success))
							.setDeleteIntent(pendingIntent)
							.build();

					//notification.setLatestEventInfo(UpdateService.this, app_name, getString(R.string.update_download_success), pendingIntent);


					notificationManager.notify(notification_id, notification);

					stopService(updateIntent);
					break;
				case DOWN_ERROR:
					notification = new Notification.Builder(UpdateService.this)
							.setContentTitle("New mail from " + app_name)
							.setContentText(getString(R.string.update_download_fail))
							.setDeleteIntent(pendingIntent)
							.build();
					//notification.setLatestEventInfo(UpdateService.this, app_name, getString(R.string.update_download_fail), pendingIntent);
					notificationManager.notify(notification_id, notification);
					stopService(updateIntent);
					break;

				default:
					stopService(updateIntent);
					break;
				}

			}

		};

		final Message message = new Message();

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					long downloadSize = downloadUpdateFile(url,
							FileUtil.updateFile.toString());
					if (downloadSize > 0) {
						
						message.what = DOWN_OK;
						handler.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
					message.what = DOWN_ERROR;
					handler.sendMessage(message);
				}

			}
		}).start();
	}

	RemoteViews contentView;

	public void createNotification() {
		
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(android.R.drawable.stat_sys_download_done, getString(R.string.updating), System.currentTimeMillis());

		contentView = new RemoteViews(getPackageName(),
				R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle, getString(R.string.downloading));
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

		notification.contentView = contentView;

		updateIntent = new Intent(this, AiotActivity.class);
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

		notification.contentIntent = pendingIntent;

		notificationManager.notify(notification_id, notification);

	}
	
	public long downloadUpdateFile(String down_url, String file)
			throws Exception {
		int down_step = 5;
		int totalSize;
		int downloadCount = 0;
		int updateCount = 0;
		InputStream inputStream;
		OutputStream outputStream;

		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);
		byte buffer[] = new byte[1024];
		int readsize = 0;
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;
			if (updateCount == 0
					|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
				updateCount += down_step;
				contentView.setTextViewText(R.id.notificationPercent,
						updateCount + "%");
				contentView.setProgressBar(R.id.notificationProgress, 100,
						updateCount, false);
				notificationManager.notify(notification_id, notification);

			}

		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();
		outputStream.close();

		return downloadCount;

	}

}
