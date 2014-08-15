package com.citi.mc.app;


import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.citi.mc.R;
import com.citi.mc.db.Monitor;
import com.citi.mc.utils.MobileChatClient;
import com.citi.mc.utils.SongAndVibrator;
import com.citi.mc.utils.Timeparser;
import com.citi.mc.utils.UpdateManager;
import com.citi.mc.service.MessageService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint({ "NewApi", "WorldReadableFiles" })
public class SetFragement extends Fragment
{
	//private Monitor monitor;
	private Date curDate;
	public Timeparser timeparser;
	private Monitor tmpMonitor;
	EditText testText=null;
	private Handler exitHandler;
	private TextView versionTextView = null;
	//清楚fragment的状态
	private void clearState() {
		MainTabActivity.listConsultmsgs.clear();
		MainTabActivity.consultFragment = null;
		MainTabActivity.chatFragment=null;
		MainTabActivity.kehuFragment = null;
		MainTabActivity.leaveFragment=null;
		MainTabActivity.infoFragment=null;
		MainTabActivity.setFragment =null;
	}
 	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 取得当前登录的客服对象
		tmpMonitor=MainTabActivity.monitor;//LoginActivity.inloginMonitor;
		//OnItemClickListener onClickListener = new OnItemClickListener();
		curDate=new Date(System.currentTimeMillis());
		Date currDate=new Date();
	}
	
 	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		try{
		View view = inflater.inflate(R.layout.mobile_set, container, false); 
		
		//TextView alterText = (TextView)view.findViewById(R.id.alter_password);
		
		
		//finsh_btn = (Button)view.findViewById(R.id.mobile_finsh);
		TextView feedbackText = (TextView)view.findViewById(R.id.feedback_set);
		TextView contactText = (TextView)view.findViewById(R.id.contanct_set);
		Button exitButton  = (Button)view.findViewById(R.id.mobile_exit);
		final ToggleButton tb1 = (ToggleButton)view.findViewById(R.id.vibrator_setbn);
		final ToggleButton tb2 = (ToggleButton)view.findViewById(R.id.sound_setbn);
		versionTextView = (TextView)view.findViewById(R.id.version_set);
		versionTextView.setText("点击更新，当前版本V"+getCurrentVersion());
		
		SPhelper sPhelper = new SPhelper(getActivity());
		//设置MainTabActivity的flag为true
		MainTabActivity.flag = true;
		
		if(sPhelper.getSongState().equals("true"))
        {
        	tb2.setBackgroundResource(R.drawable.slide_toggle_on);
        }
		else {
			tb2.setBackgroundResource(R.drawable.slide_toggle_off);
		}
		
		if(sPhelper.getToggleState().equals("true"))
		{
			tb1.setBackgroundResource(R.drawable.slide_toggle_on);
		}
		else {
			tb1.setBackgroundResource(R.drawable.slide_toggle_off);
		}
		
		
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//tmpMonitor=monitor.getMonitorinLogin();
			
					new AlertDialog.Builder(getActivity())
					.setTitle("退出登录")
					.setIcon(R.drawable.g_ic_failed)
					.setMessage("确定要退出当前账户")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method
									// stub
									try {
										//注销线程
										cancelService();
										LoginActivity.isexitCount=1;
										MessageService.isCancel = true;
										MessageService.isNotifiManager = true;
										MobileChatClient.cancelTask(getActivity().getApplicationContext());
										clearState();
				        				Intent intent = new Intent();
				        				Activity getedactivity=getActivity();
				        				if (getedactivity!=null) 
				        				{
				        					intent.setClass(getedactivity, LoginActivity.class);
				        					getActivity().finish();
				        					System.out.println("testwhyfinish");
				        					
//					                  	  	startActivity(intent);
										}
										exitloginMobileChat();
										
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									finally
									{
//										System.exit(0);
									}
									
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {}
							}).create().show();	
			}
		});
	
		exitHandler=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what==0) {
					LoginActivity.isexitCount=1;
					Intent intent = new Intent();
    				Activity getedactivity=getActivity();
    				if (getedactivity!=null) {
    					intent.setClass(getedactivity, LoginActivity.class);
                  	  	startActivity(intent);
                  	  	getedactivity.finish();
					}
					//Toast.makeText(getActivity(), "网络状况不佳，请重试", Toast.LENGTH_SHORT).show();
				}
				
			}
			
			
			
		};

		feedbackText.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				show(2);
			}
		});
		contactText.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				show(3);
			}
		});
		
//		tb1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(tb1.isChecked())
//				{
//					tb1.setBackgroundResource(R.drawable.slide_toggle_on);
//				}
//				else {
//					tb1.setBackgroundResource(R.drawable.slide_toggle_off);
//				}
//				
//			}
//		});
		
		
		// 振动
		tb1.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					tb1.setBackgroundResource(R.drawable.slide_toggle_on);
					SPhelper sp = new SPhelper(getActivity());
					sp.setToggleState("true");
					
				}
				else
				{	
					SPhelper sp = new SPhelper(getActivity());
					sp.setToggleState("false");
					tb1.setBackgroundResource(R.drawable.slide_toggle_off);
				}
			}
		});
		tb2.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			SongAndVibrator songTest = new SongAndVibrator(getActivity());
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					SPhelper sp = new SPhelper(getActivity());
					sp.setSongState("true");
					tb2.setBackgroundResource(R.drawable.slide_toggle_on);
					
					new Thread()
					{
						public void run()
						{
							songTest.isSong(getActivity(),"newmessage");
							
						}
					}.start();
				}
				else 
				{
					SPhelper sp = new SPhelper(getActivity());
					sp.setSongState("false");
					tb2.setBackgroundResource(R.drawable.slide_toggle_off);
				}
			}
		});
		
		
		versionTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 try {
					UpdateManager.getUpdateManager().checkAppUpdate(getActivity(), true);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
        return view;  
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	private void cancelService() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 获取当前客户端版本信息
	 */
	private String getCurrentVersion(){
		String curVersionName = "";
        try { 
        	PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        	curVersionName = info.versionName;
        } catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
        return curVersionName;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	private void show(int index)
	{
		FragmentManager manager = getFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_in1);
		Fragment fragment = (Fragment)getFragmentManager().findFragmentById(android.R.id.tabcontent);
		MainTabActivity.layout.setVisibility(View.GONE);
		switch (index)
		{
		case 0:  
            fragment = new SetFragement();  
            //ft.add(R.id.setfra, fragment);
            break;  
        case 1:  
            fragment = new AlFragment();  
            ft.replace(android.R.id.tabcontent, fragment);
            
            break;
        case 2:
        	
        	fragment = new FeedBackFragment();
        	ft.replace(android.R.id.tabcontent, fragment);
        	break;
        case 3:
//        	MainTabActivity.layout.setVisibility(View.GONE);
        	//联系我们页面
        	fragment = new ContactUsFragment();
        	ft.replace(android.R.id.tabcontent, fragment);
        	break;
		}
		
		// 这里是给android物理回退键设的效果
		//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);  
        ft.addToBackStack(null);  
        ft.commit();  

	}
	public boolean exitloginMobileChat() throws JSONException, UnsupportedEncodingException
	 {
		 //sPhelper = new SPhelper(this);
		
		 HashMap<String, String> paramMap = new HashMap<String, String>();
		 paramMap.put("unitid", tmpMonitor.unitid);
		 paramMap.put("usid", tmpMonitor.userid);
		 paramMap.put("uscookie", tmpMonitor.uscookie);

		 RequestParams params = new RequestParams(paramMap);
		 
		 MobileChatClient.get("userver/offline", params, new JsonHttpResponseHandler() {
	            @Override
	            public void onSuccess(JSONObject timeline) {
	            	System.out.println(timeline);
	            	System.out.println("testwhyin sucess .................");
	            	try {
	            		String flag = (String)timeline.getString("success");
	            		if(flag.equals("true"))
	            		{
	        				
	            		}
	            		else
	            		{
	            			Message msgMessage=new Message();
	            			msgMessage.what=0;
	            			exitHandler.sendMessage(msgMessage);
	            		}
	            	} catch (JSONException e) {
	            		// TODO Auto-generated catch block
	            		e.printStackTrace();
	            	}
	            	finally
	            	{
	            		//退出程序
	            		System.exit(0);
	            	}

	            }

	    });
	    return true;
	 }
	
}