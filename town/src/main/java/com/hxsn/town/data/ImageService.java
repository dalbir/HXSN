package com.hxsn.town.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.w3c.dom.NodeList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 从网络下载图片工具类
 * 提供两个对外方法：1、getImage返回给用户byte数组 2、getBitmap返回给用户Bitmap位图，可根据setImageBitmap设置
 * @author yanghuan
 *
 */

public class ImageService {

	public static Bitmap getBitmap(String path) throws IOException {
		byte[] data = getImage(path);  
	    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  //生成位图  
	    return bitmap;
	}
	
	public static byte[] getImage(String path) throws IOException {
		
		NLCallable mCallable = new NLCallable(path);
		FutureTask<byte[]> task = new FutureTask<byte[]>(mCallable);
        new Thread(task).start();
        byte[] bytes = null;
		try {
			bytes = task.get();
		} catch (Exception e) {
			Log.i("","服务器异常，e="+e);
			e.printStackTrace();
		}
		return bytes;
	}
	private static class NLCallable implements Callable<byte[]>{
		private String url;
		public NLCallable(String url){
			this.url = url;
		}
		
	    @Override
	    public byte[] call() throws Exception {
	    	URL url = new URL(this.url);  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        conn.setRequestMethod("GET");   //设置请求方法为GET  
	        conn.setReadTimeout(5*1000);    //设置请求过时时间为5秒  
	        InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据  
	        byte[] data = readInputStream(inputStream);     //获得图片的二进制数据  
	    	
	        return data;
	    }
	}
	
	/* 
     * 从数据流中获得数据 
     */  
    private static byte[] readInputStream(InputStream inputStream) throws IOException {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return bos.toByteArray();  
          
    } 
}
