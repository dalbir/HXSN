package com.hxsn.iot.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;import android.provider.Settings;
import android.util.Log;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.util.TLog;
import com.hxsn.iot.util.XMLParse;

import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/*
 *
 */
public class TestGetData {
	private final static String TAG = "TestGetData";
	
	public static final int CONNECTED = 11;
	
	private  Activity context;
	private Boolean isTest = DataController.isTest;
	
	public TestGetData(){
		context = Contents.getInstance().getActivity();
	}
	
	private Handler handler = new Handler(){
		
		@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
			MessDialog.show("服务器异常", context);
    	}
	};
	
	public NodeList getXmlData(String uri) {
		TLog.log("请求地址:" + uri);
		NLCallable mCallable = new NLCallable(uri);
		FutureTask<NodeList> task = new FutureTask<NodeList>(mCallable);
        new Thread(task).start();
        NodeList nodelist = null;
		try {
			nodelist = task.get();
		} catch (Exception e) {
			Log.e("","服务器异常，e="+e);
		}
		return nodelist;
	}
	
	public String getXmlString(String uri){
		XMLCallable callable = new XMLCallable(uri);
		FutureTask<String> task = new FutureTask<String>(callable);
		new Thread(task).start();
		StringBuffer sb = new StringBuffer();
		try{
			sb.append(task.get());
		}catch(Exception e){
			Log.e(TAG, e.getMessage());
		}
		return sb.toString();
	}
	
	private class NLCallable implements Callable<NodeList>{
		private String url;
		public NLCallable(String url){
			this.url = url;
		}
		
	    @Override
	    public NodeList call() throws Exception {
	    	NodeList nodelist = null;
	    	URL url;
    		if(isTest){
    			url = new URL(getServerUrl()+this.url);
    		} else {
    			url = new URL(getServerUrl()+"xservice/android?"+this.url);
    		}
    		Contents.getInstance().setServerUrl(getServerUrl());
//    		Log.i("","TestGetData:url="+NetworkUtil.getLocalUri()+this.url);
	    	HttpURLConnection conn =  (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(5000); 
	        conn.setRequestMethod("POST");
	        InputStream is =conn.getInputStream(); 
	        XMLParse xml = XMLParse.getInstance();
			nodelist = xml.parserToList(xml.parser(is));
	        return nodelist;
	    }
	}
	
	/**
	 * 读取xml内容
	 * @author liuzhiyuan
	 *
	 */
	private class XMLCallable implements Callable<String>{
		String uri;
		public XMLCallable(String uri){
			this.uri = uri;
		}
		@Override
		public String call() throws Exception {
			URL url = new URL(getServerUrl()+"xservice/android?"+this.uri);
			Contents.getInstance().setServerUrl(getServerUrl());
	    	HttpURLConnection conn =  (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(5000); 
	        conn.setRequestMethod("POST");
	        InputStream is =conn.getInputStream(); 
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuffer sb = new StringBuffer();
	        String tmp = "";
	        while((tmp=reader.readLine())!=null){
	        	sb.append(tmp);
	        }
			return sb.toString();
		}
		
	}
	
	private String getServerUrl(){
			SharedPreferences sharedata = context.getSharedPreferences("data", 0);
			String serverUrl = sharedata.getString("ServerUri", null);
			return serverUrl;
	}
	
	/**
     * 检测网络是否可用
     * @return
     */
    protected boolean checkNetwork(){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		//网络不可用
		if(null == networkInfo || !networkInfo.isAvailable()){
			new AlertDialog.Builder(context).setMessage(R.string.nonetwork)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
					
					@Override
					public void onClick(DialogInterface dialog, int which){
						Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
						context.startActivity(intent);
						context.finish();
					}
				}).show();
			return false;
		}
		return true;
	}
    //地址错误提示框
    private void showAddressErrorDialog(){
    	new AlertDialog.Builder(context).setMessage(R.string.address_error)
		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.dismiss();
			}
		}).show();
    }
	
	//获得本地数据
	public NodeList getXmlFile(String uri) {
		
		try {
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(uri);
	        XMLParse xml = XMLParse.getInstance();
			NodeList nodelist;
			nodelist = xml.parserToList(xml.parser(is));
			return nodelist;
		} catch (Exception e) {
			//需要做处理
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String Inputstr2Str_byteArr(InputStream in, String encode)  
   {  
       StringBuffer sb = new StringBuffer();  
       byte[] b = new byte[1024];  
       int len = 0;  
       try  
       {  
           if (encode == null || encode.equals(""))  
           {  
               // 默认以utf-8形式  
               encode = "utf-8";  
           }  
           while ((len = in.read(b)) != -1)  
           {  
               sb.append(new String(b, 0, len, encode));  
           }  
           return sb.toString();  
       }  
       catch (IOException e)  
       {  
           e.printStackTrace();  
       }  
       return "";  
         
   }  
     
   /** 
    * 利用ByteArrayOutputStream：Inputstream------------>String <功能详细描述> 
    *  
    * @param in 
    * @return 
    * @see [类、类#方法、类#成员] 
    */  
   public static String Inputstr2Str_ByteArrayOutputStream(InputStream in,String encode)  
   {  
       ByteArrayOutputStream out = new ByteArrayOutputStream();  
       byte[] b = new byte[1024];  
       int len = 0;  
       try  
       {  
           if (encode == null || encode.equals(""))  
           {  
               // 默认以utf-8形式  
               encode = "utf-8";  
           }  
           while ((len = in.read(b)) > 0)  
           {  
               out.write(b, 0, len);  
           }  
           return out.toString(encode);  
       }  
       catch (IOException e)  
       {  
           e.printStackTrace();  
       }  
       return "";  
   }
	
}
