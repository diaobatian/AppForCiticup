package com.citi.mc.db;


import java.text.SimpleDateFormat;

import android.os.Handler;
import android.util.Log;

/**
 * 
 * @author ChunTian
 *
 */
public class UserOffline {
	
	private Long t;
	
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {

			ConsultMsg consultmsg = new ConsultMsg();
			long totalSec;
			// 获得当前时间
			SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
			String danqiandate = sdf.format(new java.util.Date());
			Log.i("as", "当前时间=====" + danqiandate);
			String[] my = danqiandate.split(":");
			int min = Integer.parseInt(my[0]);
			int sec = Integer.parseInt(my[1]);
			totalSec = min * 60 + sec;
			Log.i("as", (totalSec - t) + "");
			if ((totalSec - t) >= 90) {
				consultmsg.isOnline = false;
				handler.removeCallbacks(runnable); // 停止Timer
				Log.i("as", "用户下线了");

			}

			// 方法安排一个Runnable对象到主线程队列中
			handler.postDelayed(runnable, 1000);

		}
	};
	
	public void judgmentoffline(String name,Long judgment){
		
		t=judgment;
		// 方法安排一个Runnable对象到主线程队列中
		handler.postDelayed(runnable, 1000);
		
	}
	
}
