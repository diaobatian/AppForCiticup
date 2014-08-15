package com.citi.mc.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class IsNetWorkAvailable 
{
	
	public static void setNetWork(Context context,final Activity activity)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("网络状态");
		builder.setMessage("当前无可用网络，请设置");
		builder.setPositiveButton("设置", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));				
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		builder.create();
		builder.show();
	}
	
	//判断是否连接
	public static boolean isAvailable(Context context)
	{
		//备注
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager == null)
		{
			return false;
		}
		else {
			//获得NetworkInfo对象    获取所有网络连接
			NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
			if(infos!=null)
			{
				for(int i=0;i<infos.length;i++)
				{
					//判断是否连接网络
					if(infos[i].getState()==NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
				}
			}
		}
		return false;
		
	}
}
