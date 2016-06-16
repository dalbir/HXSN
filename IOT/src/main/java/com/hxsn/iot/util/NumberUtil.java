package com.hxsn.iot.util;

/**
 * 数字转换工具
 * @author liuzhiyuan
 *
 */
public class NumberUtil {

	/**
	 * 字符串转整形
	 * @param num 要转换的字符串
	 * @param defNum 转换失败默认值
	 * @return
	 */
	public static int parseInt(String num, int defNum){
		int result = defNum;
		try{
			defNum = Integer.parseInt(num);
		}catch(Exception e){
		}
		return defNum;
	}
	
	/**
	 * 字符串转浮点型
	 * @param num 要转换的字符串
	 * @param defNum 转换失败默认值 
	 * @return
	 */
	public static float parseFloat(String num, float defNum){
		float result = defNum;
		try{
			defNum = Float.parseFloat(num);
		}catch(Exception ex){
		}
		return result;
	}
}
