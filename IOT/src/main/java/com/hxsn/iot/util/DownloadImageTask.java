package com.hxsn.iot.util;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.hxsn.iot.R;

import java.io.IOException;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Drawable>{
	private ImageView imageView;
	private Activity activity;
	public DownloadImageTask(Activity activity, ImageView imageView){
		this.imageView = imageView;
		this.activity = activity;
	}
	
	@Override
	protected Drawable doInBackground(String... params) {
		 return loadImageFromNetwork(params[0]);
	}
	
	protected void onPostExecute(Drawable result) {
		imageView.setImageDrawable(result);
    }
	
	private Drawable loadImageFromNetwork(String imageUrl){
	 Drawable drawable = null;
	 try { 
	  // 可以在这里通过文件名来判断，是否本地有此图片
	  drawable = Drawable.createFromStream(
	    new URL(imageUrl).openStream(), "image.jpg");
	 } catch (IOException e) {
	  Log.d("test", e.getMessage());
	 }
	 if (drawable == null) {
	  Log.d("test", "null drawable");
	  drawable = activity.getResources().getDrawable(R.drawable.controll_image);
	 } else {
	  Log.d("test", "not null drawable");
	 }
	 
	 return drawable ;
	}
}
