package team.co.kr.testproject.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarUtil {

    @SuppressLint("SimpleDateFormat")
	public static String getToday() {
    	return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }
    
    @SuppressLint("SimpleDateFormat")
	public static String getToday(String strFormat) {
    	return new SimpleDateFormat(strFormat).format(new Date());
    }
    
    @SuppressLint("SimpleDateFormat")
	public static String getTomorrow() {
    	Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 1);
        return new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
    }
    
    @SuppressLint("SimpleDateFormat")
	public static String getTomorrow(String strFormat) {
    	Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 1);
    	return new SimpleDateFormat(strFormat).format(cal.getTime());
    }
    
    @SuppressLint("SimpleDateFormat")
	public static String getYesterday() {
    	Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, -1);
        return new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
    }
    
    @SuppressLint("SimpleDateFormat")
	public static String getYesterday(String strFormat) {
    	Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, -1);
    	return new SimpleDateFormat(strFormat).format(cal.getTime());
    }
    
    @SuppressLint("SimpleDateFormat")
	public static String getDate(long timeMillis) {
    	return new SimpleDateFormat("yyyy/MM/dd").format(new Date(timeMillis));
    }
    
    public static String getDate(long timeMillis, String strFormat) {
    	return new SimpleDateFormat(strFormat).format(new Date(timeMillis));
    }
    
    @SuppressLint("SimpleDateFormat")
	public static String getMinutesSeconds(int seconds) {
    	return new SimpleDateFormat("mm:ss").format(new Date(seconds * 1000));
    }

    public static long getNextClockLeftTimeMillis() {
    	LogUtil.log("********** CURRENT TIME : [" + getDate(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss") + "]");
    	
    	Calendar next = Calendar.getInstance();
    	next.add(Calendar.HOUR_OF_DAY, 1);

    	int hour = next.get(Calendar.HOUR_OF_DAY);
    	
    	next.set(Calendar.HOUR_OF_DAY, hour);
    	next.set(Calendar.MINUTE, 0);
    	next.set(Calendar.SECOND, 0);
    	
    	long nextTimeMillis = next.getTimeInMillis();
    	LogUtil.log("********** NEXT TIME : [" + getDate(nextTimeMillis, "yyyy/MM/dd HH:mm:ss") + "]");
    	return nextTimeMillis - System.currentTimeMillis();
    }
}
