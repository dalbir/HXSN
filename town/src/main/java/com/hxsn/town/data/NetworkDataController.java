package com.hxsn.town.data;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.w3c.dom.NodeList;

import android.util.Log;

public class NetworkDataController {
	private InputStream is;

//	public NodeList getXmlData(String uri) {
//		
//		try {
//			Thread thread = new Thread(new GetDataRunnable(uri));
//			thread.start();
//			thread.join();
//			InputStream is = getInputStream();
//	        XMLParse xml = XMLParse.getInstance();
//			NodeList nodelist;
//			nodelist = xml.parserToList(xml.parser(is));
//			return nodelist;
//		} catch (Exception e) {
//			//需要做处理
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setInputStream(InputStream is) {
//		this.is = is;
//	}
//	
//	public InputStream getInputStream() {
//		return is;
//	}
//	
//	class GetDataRunnable implements Runnable {
//		String uri;
//		public GetDataRunnable(String uri) {
//			this.uri = uri;
//		}
//		@Override
//		public void run() {
//			URL url;
//			try {
//				url = new URL(uri);
//				HttpURLConnection conn =  (HttpURLConnection) url.openConnection();     
//		        conn.setConnectTimeout(5000);    
//		        conn.setRequestMethod("POST");
//		        InputStream is =conn.getInputStream(); 
//		        Log.i("","___________________is="+is);
//		        setInputStream(is);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	public NodeList getXmlData(String uri) {
		
		NLCallable mCallable = new NLCallable(uri);
		FutureTask<NodeList> task = new FutureTask<NodeList>(mCallable);
        new Thread(task).start();
        NodeList nodelist = null;
		try {
			nodelist = task.get();
		} catch (Exception e) {
			Log.i("","服务器异常，e="+e);
			e.printStackTrace();
		}
		return nodelist;
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
//    		if(isTest){
//    			url = new URL(getLocalUri()+this.url);
//    		} else {
    			url = new URL(this.url);
//    		}
	    	HttpURLConnection conn =  (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(5000); 
	        conn.setRequestMethod("POST");
	        InputStream is =conn.getInputStream(); 
	        
	        XMLParse xml = XMLParse.getInstance();
			nodelist = xml.parserToList(xml.parser(is));
	    	
	        return nodelist;
	    }
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
}
