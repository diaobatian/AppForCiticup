package com.citi.mc.receiver;

import com.citi.mc.app.MainTabActivity;

import cn.jpush.android.api.JPushInterface;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class MessageReceiver extends BroadcastReceiver {

	public static final String MESSAGE_RECEIVED_ACTION = "com.citi.mc.receiver.NOTIFICATION_RECEIVED";
	public static final String MESSAGE_OPENED_ACTION = "com.citi.mc.receiver.NOTIFICATION_OPENED";
	
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }
	
	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final String TAG = "MessageReceiver onReceive";
		
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		

        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            
        	//打开自定义的Activity
        	Intent i = new Intent(context, MainTabActivity.class);
        	i.putExtras(bundle);
        	System.out.println("onReceive + before set flag");
        	//这没问题吗？
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	try
        	{
        	context.startActivity(i);
        	}
        	catch(ActivityNotFoundException e)
        	{
        		System.out.println("onReceive +1");
        	}
        	catch(Exception e)
        	{
        		System.out.println("onReceive +2"+e.toString());
        	}
        
        
        } else {
        	Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
	}
	
	
	
}

