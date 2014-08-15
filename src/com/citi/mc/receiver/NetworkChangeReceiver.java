package com.citi.mc.receiver;

/** 
 * @作者 newbie 
 * @email lianlupeng@gmail.com
 * @创建日期 2013-11-13 
 * @版本 V 1.0 
 * 网络状态的接收器继承BroadcastReceiver
 * version:1.0
 */
import com.citi.mc.app.ChatFragment;
import com.citi.mc.app.ConsultFragment;
import com.citi.mc.app.MainTabActivity;
import com.citi.mc.utils.NetworkUtil;
import com.citi.mc.service.MessageService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
 
public class NetworkChangeReceiver extends BroadcastReceiver {
 
    @Override
    public void onReceive(final Context context, final Intent intent) {
 
//        String status = NetworkUtil.getConnectivityStatusString(context);
// 
//        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        int networkState = NetworkUtil.getConnectivityStatus(context);
        //无网络连接时做的处理
        if(networkState==NetworkUtil.TYPE_NOT_CONNECTED)
        {
        	if(ConsultFragment.networkTextView!=null&&(ConsultFragment.networkTextView.getVisibility()==View.GONE))
        	{
        		ConsultFragment.networkTextView.setVisibility(View.VISIBLE);
        	}
//        	if(ChatFragment.networkTextView!=null&&(ChatFragment.networkTextView.getVisibility()==View.GONE))
//        	{
//        		ChatFragment.networkTextView.setVisibility(View.VISIBLE);
//        	}
        }
        //有网络链接的时候
        else
        {
        	if(ConsultFragment.networkTextView!=null&&(ConsultFragment.networkTextView.getVisibility()==View.VISIBLE))
        	{
        		ConsultFragment.networkTextView.setVisibility(View.GONE);
        	}
//        	if(ChatFragment.networkTextView!=null&&(ChatFragment.networkTextView.getVisibility()==View.VISIBLE))
//        	{
//        		ChatFragment.networkTextView.setVisibility(View.GONE);
//        	}
        	//只要有网络就启动service
        	Log.d("MessageService", "MessageService NetworkChangeReceiver startService() executed");
//        	context.startService(new Intent(context, MessageService.class));
        	
		}
    }
}