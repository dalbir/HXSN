package com.hxsn.iot.util;

import android.os.Environment;

import com.hxsn.iot.AbsApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileUtil {
	public static File updateDir = new File(Environment.getExternalStorageDirectory()
			+ "/" + AbsApplication.downloadDir);
	public static File updateFile = null;
	
	public static void createFile(String name) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			//updateDir = new File(Environment.getExternalStorageDirectory()
			//		+ "/" + CTPFApplication.downloadDir);
			updateFile = new File(updateDir + "/" + name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void deleteFile(File mFile) {
		if (mFile.exists()) { 
			if (mFile.isFile()) { 
				mFile.delete();
			} else if (mFile.isDirectory()) { 
				File files[] = mFile.listFiles(); 
				for (int i = 0; i < files.length; i++) { 
					deleteFile(files[i]); 
			    }
			}
			mFile.delete();
		}
	}
	
	public static void copyHistoryFile(String sourFile, String desFile) {
		FileOutputStream fos;
		FileInputStream fis;
		try {
			fos = new FileOutputStream(desFile);
			fis = new FileInputStream(sourFile);
			byte[] buffer = new byte[1024];
	        int count;
	        while ((count = fis.read(buffer)) > 0) {
		        fos.write(buffer, 0, count);
			}
			fos.flush();
	        fos.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
