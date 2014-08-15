package com.citi.mc.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/** 
 * @作者 newbie 
 * @email lianlupeng@gmail.com
 * @创建日期 2013-11-13 
 * @版本 V 1.0 
 * 实现网络监听的工具类
 * version:1.0
 */
public class NetworkUtil {

	 	public static int TYPE_WIFI = 1;
	    public static int TYPE_MOBILE = 2;
	    public static int TYPE_NOT_CONNECTED = 0;
	     
	     
	    public static int getConnectivityStatus(Context context) {
	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	 
	        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	        if (null != activeNetwork) 
	        {
	            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
	                return TYPE_WIFI;
	             
	            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
	                return TYPE_MOBILE;
	        } 
	        return TYPE_NOT_CONNECTED;
	    }
	     
	    public static String getConnectivityStatusString(Context context) {
	        int conn = NetworkUtil.getConnectivityStatus(context);
	        String status = null;
	        if (conn == NetworkUtil.TYPE_WIFI) {
	            status = "Wifi enabled";
	        } else if (conn == NetworkUtil.TYPE_MOBILE) {
	            status = "Mobile data enabled";
	        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
	            status = "Not connected to Internet";
	        }
	        return status;
	    }
}
