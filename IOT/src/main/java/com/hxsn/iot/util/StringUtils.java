package com.hxsn.iot.util;

public class StringUtils {

	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str){
		if(str==null){
			return true;
		}
		if("".equals(str)){
			return true;
		}
		return false;
	}
}
