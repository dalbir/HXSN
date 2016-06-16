package com.hxsn.farmage.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/**

*/
public class WiFiStateUtils {
	
	
	public static boolean isWiFiActive(Context inContext) {   
	        Context context = inContext.getApplicationContext();   
	        ConnectivityManager connectivity = (ConnectivityManager) context   
	                .getSystemService(Context.CONNECTIVITY_SERVICE);   
	        if (connectivity != null) {   
	        	NetworkInfo[] info = connectivity.getAllNetworkInfo();   
	            if (info != null) {   
	            	for (int i = 0; i < info.length; i++) {   
	            		if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {  
	            			return true;            }   
	                }   
	            }   
	        }   
	        return false;  
	        }  

	
	
	 public static boolean detect(Context context) {  
		      ConnectivityManager manager = (ConnectivityManager) context.getSystemService(  
		                    Context.CONNECTIVITY_SERVICE);  
		       if (manager == null) {  
		           return false;  
	       }  
	      NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
		      if (networkinfo == null || !networkinfo.isAvailable()) {  
		           return false;  
		       }  
		   return true;  
   }  

	
	
}
