package com.citi.mc.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.activeandroid.util.Log;

import android.net.ParseException;
import android.widget.Toast;

/**
 * 时间判断器
 * 
 * @author ChunTian version1.1:
 * 
 *         changelog:
 * 
 *         1.符合微信的时间要求格式
 */
public class Timeparser {
	public Timeparser() {
		// TODO Auto-generated constructor stub

	}

	/** 准备第一个模板，从字符串中提取出日期数字 */
	private static String pat1 = "yyyy-MM-dd HH:mm:ss";
	/** 准备第二个模板，将提取后的日期数字变为指定的格式 */
	private static String pat2 = "yyyy年MM月dd日 HH:mm:ss";
	/** 实例化模板对象 */
	private static SimpleDateFormat sdf1 = new SimpleDateFormat(pat1);
	private static SimpleDateFormat sdf2 = new SimpleDateFormat(pat2);

//	public static void main(String[] args) {
//		Date dates = Dates("1384431262712");
//		String string = sdf1.format(dates);
//		System.out.println(string);
//		System.out.println(getTime("2013-11-10 08:46:20"));
//	}

	public static Long farmatTime(String string) {
		Date date = null;
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = Date(sf.parse(string));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}

	public static Date Date(Date date) {
		Date datetimeDate;
		datetimeDate = new Date(date.getTime());
		return datetimeDate;
	}

	public static Date Dates(String time) {
		Date datetimeDate;
		Long dates = Long.parseLong(time);
		datetimeDate = new Date(dates);
		return datetimeDate;
	}

	public static String getTime(String commitDate) {
		Date dates = Dates(commitDate);
		commitDate = sdf1.format(dates);
		// TODO Auto-generated method stub
		// 在主页面中设置当天时间
		Date nowTime = new Date();
		String currDate = sdf1.format(nowTime);
		Date date = null;
		try {
			// 将给定的字符串中的日期提取出来
			date = sdf1.parse(commitDate);
			Log.i("as", "datedatedatedate====>" + date+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		int dayOfWeek = 0;
		try {
			dayOfWeek = dayForWeek(currDate);
			System.out.println(dayOfWeek);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int nowDate = Integer.valueOf(currDate.substring(8, 10));
		int commit = Integer.valueOf(commitDate.substring(8, 10));
		String monthDay = sdf2.format(date).substring(5, 12);
		String yearMonthDay = sdf2.format(date).substring(0, 12);
		String year = sdf1.format(date).substring(0, 10);
		int month = Integer.valueOf(monthDay.substring(0, 2));
		int day = Integer.valueOf(monthDay.substring(3, 5));
		if (month < 10 && day < 10) {
			monthDay = monthDay.substring(1, 3) + monthDay.substring(4);
		} else if (month < 10) {
			monthDay = monthDay.substring(1);
		} else if (day < 10) {
			monthDay = monthDay.substring(0, 3) + monthDay.substring(4);
		}
		int yearMonth = Integer.valueOf(yearMonthDay.substring(5, 7));
		int yearDay = Integer.valueOf(yearMonthDay.substring(8, 10));
		if (yearMonth < 10 && yearDay < 10) {
			yearMonthDay = yearMonthDay.substring(0, 5)
					+ yearMonthDay.substring(6, 8) + yearMonthDay.substring(9);
		} else if (yearMonth < 10) {
			yearMonthDay = yearMonthDay.substring(0, 5)
					+ yearMonthDay.substring(6);
		} else if (yearDay < 10) {
			yearMonthDay = yearMonthDay.substring(0, 8)
					+ yearMonthDay.substring(9);
		}
		String des = null;
		String hourMin = commitDate.substring(11, 16);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 当前时间
		String datee = sdf.format(new Date());
		String[] xianzai = datee.split("[ ]");
		String[] yearr = xianzai[0].split("[-]");
		String[] strr = year.split("[-]");// 设定时间的模板
		Log.i("as", "xianzai[0]=======>" + xianzai[0] + "year===>" + year);
		if (yearr[2].trim().equals(strr[2].trim()) && yearr[1].trim().equals(strr[1].trim()) && yearr[0].trim().equals(strr[0].trim())) {
			
			des = hourMin;
			
		}
		// 两天
		else if ((Integer.valueOf(yearr[2]) - Integer.valueOf(strr[2])) == 1  && yearr[1].trim().equals(strr[1].trim()) && yearr[0].trim().equals(strr[0].trim())) {
		
				des = "昨天";

		}
		// 三天
		else if ((Integer.valueOf(yearr[2]) - Integer.valueOf(strr[2])) == 2  && yearr[1].trim().equals(strr[1].trim()) && yearr[0].trim().equals(strr[0].trim())) {
		
				des = "前天";
		}
		else 
		{
			
			des = year;
			
		}
		return des;
	}

	public static Date Date() {
		Date datetimeDate;
		Long dates = 1361514787384L;
		datetimeDate = new Date(dates);
		return datetimeDate;
	}

	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	private static HashMap<Integer, String> hash = new HashMap<Integer, String>();

	static {
		hash.put(1, "星期一");
		hash.put(2, "星期二");
		hash.put(3, "星期三");
		hash.put(4, "星期四");
		hash.put(5, "星期五");
		hash.put(6, "星期六");
		hash.put(7, "星期日");
	}

	private static String getReallyDay(int index, int type) {
		String infoString = null;
		switch (type) {
		// 昨天
		case 1:
			if (index >= 2) {
				infoString = hash.get(index - 1);
			}
			break;
		// 前天
		case 2:
			if (index >= 3) {
				infoString = hash.get(index - 2);
			}
			break;
		// 昨天
		case 3:
			if (index >= 4) {
				infoString = hash.get(index - 3);
			}
			break;// 昨天
		case 4:
			if (index >= 5) {
				infoString = hash.get(index - 4);
			}
			break;// 昨天
		case 5:
			if (index >= 6) {
				infoString = hash.get(index - 5);
			}
			break;// 昨天
		case 6:
			if (index <= 7 && index >= 6) {
				infoString = hash.get(index - 6);
			}
			break;
		}
		return infoString;
	}

}
