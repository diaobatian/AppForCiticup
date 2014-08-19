package com.citi.mc.app;
import com.baidu.mobstat.StatService;
import com.citi.mc.*;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;
import cn.jpush.android.api.JPushInterface;


public class AppStartActivity extends Activity
{


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		setContentView(R.layout.start);
		//实现home键；返回后直接进mianactivity的问题
		if (!isTaskRoot()) { 
		    // Android launched another instance of the root activity into an existing task 
		    //  so just quietly finish and go away, dropping the user back into the activity 
		    //  at the top of the stack (ie: the last state of this task) 
		    finish(); 
		    return; 
		} 
        
		// 启动画面出现片刻后，切换到登录视图
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{ 
				Intent intent = new Intent();
//				Log.i("Task:"+getTaskId(), "startActivity" +   " test416Task.");
				intent.setClass(AppStartActivity.this, LoginActivity.class);
				startActivity(intent);
				AppStartActivity.this.finish();
			}
		}, 1200);
		
		
		
		//发布时去除
		//百度统计 延迟20秒发送log;
		StatService.setSessionTimeOut(600);
		StatService.setDebugOn(true);
		StatService.setLogSenderDelayed(20);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onResume()
	{
		super.onResume();
		//百度统计
		StatService.onResume(this);
		//
	}
	@Override
	public void onPause()
	{
		super.onPause();
		StatService.onPause(this);
	}

}
