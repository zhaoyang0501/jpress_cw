package io.jpress.jp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String date(String format){
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(date);
		return dateString;
	}
	
}
