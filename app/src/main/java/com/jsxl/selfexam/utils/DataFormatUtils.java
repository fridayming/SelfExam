package com.jsxl.selfexam.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormatUtils {
     
	@SuppressLint("SimpleDateFormat") 
	public static String dataToTime(long data){
		Date date = new Date(data);
        SimpleDateFormat format  =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s = format.format(date);
		return s;
	}
	public static String dataToTime2(long data){
		Date date = new Date(data);
        SimpleDateFormat format  =  new SimpleDateFormat("yyyy-MM-dd");
        String s = format.format(date);
		return s;
	}
	
	
}
