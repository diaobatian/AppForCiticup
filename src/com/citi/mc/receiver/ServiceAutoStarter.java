package com.citi.mc.receiver;


import com.citi.mc.service.MessageService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/** 
 * @作者 newbie 
 * @email lianlupeng@gmail.com
 * @创建日期 2013-11-14 
 * @版本 V 1.0 
 */
public class ServiceAutoStarter extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent arg1) {
	  	Log.d("MessageService", "MessageService ServiceAutoStarter startService() executed");
//	  	完全不需要吧
//		context.startService(new Intent(context, MessageService.class));
	}

}
