package com.citi.mc.app;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.citi.mc.R;
import com.citi.mc.badge.BadgeButton1;
import com.citi.mc.db.ChatMessage;
import com.citi.mc.db.ConsultMsg;
import com.citi.mc.db.Lvmsg;
import com.citi.mc.db.Monitor;
import com.citi.mc.db.UnitInfo;
import com.citi.mc.utils.Constant;
import com.citi.mc.utils.MobileChatClient;
import com.citi.mc.utils.MobileManagerClient;
import com.citi.mc.utils.SongAndVibrator;
import com.citi.mc.utils.PullToRefreshListView;
import com.citi.mc.utils.Timeparser;
import com.citi.mc.utils.UpdateManager;
import com.citi.mc.receiver.MessageReceiver;
import com.citi.mc.service.MessageService;
import com.citi.mc.service.OnGetMessageListener;
import com.citi.mc.service.OnServiceUIChangeListener;
import com.citi.mc.service.PollingUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.image.SmartImageView;
import com.citi.mc.utils.Constant;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;



/**
 * @作者 lianma
 * @email lianlupeng@gmail.com
 * @创建日期 2013-11-4
 * @版本 V 1.1 主要的activity version:1.1 changelog 1.重复加入消息的问题 version:1.2
 *     1.当activity启动的时候增加MessageService.iscancel = false; version1.3
 *     1.增加安卓客户端中需要需要增加一个请求，解决导致协助对话看不到顾客的 url:/userver/notifyMbonline
 */
@SuppressWarnings("deprecation")
@SuppressLint({ "NewApi", "WorldReadableFiles" })
public class MainTabActivity extends FragmentActivity {

	public static LinearLayout layout = null;
	public static BadgeButton1 rb1;		// chat tab
	public static BadgeButton1 rb2;		// leave message tab
	public static BadgeButton1 rb3;		// unit info tab
	public static BadgeButton1 rb4;		// app settings tab

	private Long time1 = null;
	//是否在其他地点有登录
	public static Boolean multipleLoginLock= false;
	private PullToRefreshListView listView;
	public Activity activity;
	private Boolean processFlag = true;
	private Long mExitTime = 0L;
	private Date curDate;
	private Long shijian;
	private int dingwei = 0;
	private String msgdate;
	public Timeparser timeparser;
	private static Context context;
	private SongAndVibrator songAndVibrator = null;
	// unitinfo
	public static UnitInfo unitinfo;
	public static Bitmap unitlogoBitmap = null;
	public static SmartImageView unitlogWebImage = null;
	// 设置标识哪个Fragment打开的标志位
	public static Boolean flag = false;
	// 获取消息
	private String msgId = null;
	public static String msgids = null;
	private String type;
	// 最新消息的mobilecookie
	public static String mobileCookie;
	public static String cookie;
	public static String userName;
	public static int chunzhi = 0;
	// 设置popwindow
	private PopupWindow window = null;
	public LinkedList<ChatMessage> msgs = null;
	public static LinkedList<ChatMessage> chatLinkedList = null;
	public static Monitor monitor = null;
	// 判断是否列表元素超过十个的标志为
	public static Boolean isDelete = false;

	// 判断是否有新消息
	public static Boolean isHaveNewMsg = false;
	// 判断状态栏是否有保持
	public static Boolean isHaveSave = true;
	// 判断是否停在chat界面
	public static boolean isChat = false;
	// 判断是否是pause不可见状态
	public static boolean isPause = false;
	// 判断activity是否被destroy
	public static boolean isDestroy = false;
	// 判断是否在客户列表页面
	public static boolean isConsultList = false;
	public static boolean isyemian = false;
	// 底部按钮效果的重构
	public static SetFragement setFragment;
	public static Fragment leaveFragment;
	public static Fragment infoFragment;
	public static SetDuiFragment kehuFragment;
	public static ConsultFragment consultFragment = null;
	public static ChatFragment chatFragment = null;
	public static Fragment[] fragmentList=new Fragment[4];
	public static int activeFragmentindex=0;
	public static Boolean isChatFragment=false;
//	private Intent intentt = null;
	private Boolean booleanb = true;
	public static long testtime=0;
	// private static Fragment chatFragment;
	MessageReceiver mMessageReceiver;
	private boolean serviceBound = false;  
	private  MessageService localService;
	private ServiceConnection serviceConnection = new ServiceConnection() {  
		  
        @Override  
        public void onServiceConnected(ComponentName className,  
                IBinder service) {  
            // 已经绑定了LocalService，强转IBinder对象，调用方法得到LocalService对象  
            MessageService.MsgBinder binder = ( MessageService.MsgBinder) service;  
            
            localService = binder.getService();  
            System.out.println("test416 onServiceConnected onCreate");
            MessageService.onGetMessageListener = new Listener();
            serviceBound = true;  
        }  
  
        @Override  
        public void onServiceDisconnected(ComponentName arg0) {  
        	serviceBound = false;  
        	System.out.println("test416 onServiceDisconnected onCreate");
        }  
    };  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		System.out.println("test422 MainTabActivity onCreate");

		super.onCreate(savedInstanceState);
		// 初始化notification
		initNotification();
//		intentt = new Intent(MainTabActivity.this, MessageService.class);
		if (unitinfo == null) {
			unitinfo = new UnitInfo();
		}
		// 得到在线用户信息
		monitor = Monitor.getMonitorpreLogin();
		setContentView(R.layout.tab);
		// 初始化network
		msgs = new LinkedList<ChatMessage>();
	
		// JPush 初始化
		JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MainTabActivity.this);
//      
//        builder.notificationDefaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;  // 设置为闪灯
//      
//        JPushInterface.setPushNotificationBuilder(1, builder);
        
        JPushInterface.setLatestNotifactionNumber(getApplicationContext(), 1);
       
        //registerMessageReceiver();
		
		rb1 = (BadgeButton1) findViewById(R.id.radio1);
		rb2 = (BadgeButton1) findViewById(R.id.radio2);
		rb3 = (BadgeButton1) findViewById(R.id.radio3);
		rb4 = (BadgeButton1) findViewById(R.id.radio4);

		// 存储留言的数据结构
		layout = (LinearLayout) findViewById(R.id.tabgroup);

		// 获得声音和震动对象
		if (songAndVibrator == null) {
			songAndVibrator = new SongAndVibrator(MainTabActivity.this);
		}

		// 四个BadageButton的点击事件
		/*
		 * 四个fragment的切换
		 */
		rb1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (processFlag) {
					show(0);
//					switchFragment(activeFragmentindex, 0);
					//Log.i("sa", "radio1点击了啊。。。。。。。。。。。。");
				}
			}
		});
		rb2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listLeaveMessage != null && listLeaveMessage.size() >= 0) {
				show(1);
//					switchFragment(activeFragmentindex, 1);
					//Log.i("sa", "radio2点击了啊。。。。。。。。。。。。");
				}
			}
		});
		rb3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				show(2);
//				switchFragment(activeFragmentindex, 2);
				//Log.i("sa", "radio3点击了啊。。。。。。。。。。。。");
			}
		});
		rb4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				show(3);
//				switchFragment(activeFragmentindex, 3);
				//Log.i("sa", "radio4点击了啊。。。。。。。。。。。。");
			}
		});

		rb1.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					rb1.setBadgeBackgroundResource(R.drawable.chat1);
					//rb2.setBadgeBackgroundResource(R.drawable.leavemsg);
					//rb3.setBadgeBackgroundResource(R.drawable.unitinfo);
					//rb4.setBadgeBackgroundResource(R.drawable.settings);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					rb1.setBadgeBackgroundResource(R.drawable.chat1);
				}
				return false;
			}
		});
		rb2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//rb1.setBadgeBackgroundResource(R.drawable.chat);
					rb2.setBadgeBackgroundResource(R.drawable.leavemsg1);
					//rb3.setBadgeBackgroundResource(R.drawable.unitinfo);
					//rb4.setBadgeBackgroundResource(R.drawable.settings);

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					rb2.setBadgeBackgroundResource(R.drawable.leavemsg1);
				}
				return false;
			}
		});
		rb3.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//rb1.setBadgeBackgroundResource(R.drawable.chat);
					//rb2.setBadgeBackgroundResource(R.drawable.leavemsg);
					rb3.setBadgeBackgroundResource(R.drawable.unitinfo1);
					//rb4.setBadgeBackgroundResource(R.drawable.settings);

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					rb3.setBadgeBackgroundResource(R.drawable.unitinfo1);
				}

				return false;
			}
		});

		rb4.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//rb1.setBadgeBackgroundResource(R.drawable.chat);
					//rb2.setBadgeBackgroundResource(R.drawable.leavemsg);
					//rb3.setBadgeBackgroundResource(R.drawable.unitinfo);
					rb4.setBadgeBackgroundResource(R.drawable.settings1);

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					rb4.setBadgeBackgroundResource(R.drawable.settings1);
				}
				return false;
			}
		});
		listLeaveMessage = new ArrayList<Lvmsg>();
		try {
			// 告知服务器，客服上线
			onLine();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try {
			leaveMobileChat();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		show(0);
		// 首先显示聊天界面
		initFirstFragment();
		context = getApplicationContext();
		 
        //绑定service
        
		if(!serviceBound)
		{
			Intent poll_service_intent = new Intent(context, MessageService.class);
			poll_service_intent.setAction(MessageService.ACTION);
		    bindService(poll_service_intent, serviceConnection, Context.BIND_AUTO_CREATE);  
		}
		isyemian = false;
		
		
		//提示升级

		// TODO Auto-generated method stub
		 try {
			UpdateManager.getUpdateManager().checkAppUpdate(this, false);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e("update", e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e("update", e.getMessage());
			// TODO Auto-generated catch block
			
			
		}
	
		
		
		
		
		
		
		
		
		
	}

//	public void registerMessageReceiver() {
//		mMessageReceiver = new MessageReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//		filter.addAction(MESSAGE_RECEIVED_ACTION);
//		registerReceiver(mMessageReceiver, filter);
//	}
//    
//	public void unregisterMessageReceiver() {
//		unregisterReceiver(mMessageReceiver);
//	}
	
	@Override public void onNewIntent(Intent intent){
//	    Log.e(,"onNewIntent");
		
	    super.onNewIntent(intent);
	    System.out.println("test422 MessageReceiver intent");
	}
	// 与服务器发生对话，一次验证过程
	public boolean mobileChat() throws JSONException,
			UnsupportedEncodingException {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		HashMap<String, String> param = new HashMap<String, String>();
		paramMap.put("mc_type", "android");

		RequestParams params = new RequestParams(paramMap);

		MobileChatClient.post("uchat", params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject timeline) {
				System.out.println(timeline);

				try {
					String flag = (String) timeline.getString("unitid");
					String flg = (String) timeline.getString("usid");
					System.out.println(flag);
					System.out.println(flg);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

		return true;
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		rb1.setBadgeBackgroundResource(R.drawable.chat);
		rb2.setBadgeBackgroundResource(R.drawable.leavemsg);
		rb3.setBadgeBackgroundResource(R.drawable.unitinfo);
		rb4.setBadgeBackgroundResource(R.drawable.settings);
		
		rb1.setBadgeimageBackgroundResource(R.drawable.maintab_toolbar_as);
		rb2.setBadgeimageBackgroundResource(R.drawable.maintab_toolbar_as);
		rb3.setBadgeimageBackgroundResource(R.drawable.maintab_toolbar_as);
		rb4.setBadgeimageBackgroundResource(R.drawable.maintab_toolbar_as);
	}
	/**
	 * 初始化界面*/
	private void initFirstFragment() {
	FragmentManager manager = getSupportFragmentManager();
	FragmentTransaction ft = manager.beginTransaction();
//	
	clearSelection();
	rb1.setBadgeBackgroundResource(R.drawable.chat1);
	rb1.setBadgeimageBackgroundResource(R.drawable.widget_bar_bg_p);
	int count = unreadMessage();
	if (count > 0) {
		showButtonWithText(R.id.radio1, count);
	}
	showButtonWithText(R.id.radio2, listLeaveMessage.size());
	//
	consultFragment = new ConsultFragment();
//	ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_in1);
	ft.replace(android.R.id.tabcontent, consultFragment,
			Constant.CONSULT_FRAGMENT_TAG);
//	ft.add(android.R.id.tabcontent, consultFragment);
//	fragmentList[0]=consultFragment;
	activeFragmentindex=0;
	ft.commit();
	}	
	public void show(int index) {
		//
	
		//
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		// 这里可能比较耗时
		
		
		switch (index) {
		// 聊天页，代码为1
		case 0: 
			if(activeFragmentindex!=0){
				clearSelection();
			rb1.setBadgeBackgroundResource(R.drawable.chat1);
			rb1.setBadgeimageBackgroundResource(R.drawable.widget_bar_bg_p);
			int count = unreadMessage();
			if (count > 0) {
				showButtonWithText(R.id.radio1, count);
			}
			showButtonWithText(R.id.radio2, listLeaveMessage.size());
			//
			consultFragment = new ConsultFragment();
//			ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_in1);
			ft.replace(android.R.id.tabcontent, consultFragment,
					Constant.CONSULT_FRAGMENT_TAG);
//			ft.add(android.R.id.tabcontent, consultFragment);
//			fragmentList[0]=consultFragment;
			activeFragmentindex=0;
			// }
//			System.out.println("show1()程序运行时间： "+(System.currentTimeMillis()-testtime)+"ms");	
			}
			break;
		// 留言页，代码为2
		case 1:
			if(activeFragmentindex!=1){
				clearSelection();
			ChatFragment.bool3 = false;
			rb2.setBadgeBackgroundResource(R.drawable.leavemsg1);
			rb2.setBadgeimageBackgroundResource(R.drawable.widget_bar_bg_p);
			// 留言页面
			// if(leaveFragment==null)
//			{
				leaveFragment = new LeaveMsgFragment();
//				ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_in1);
				ft.replace(android.R.id.tabcontent, leaveFragment);
//			}
			// else
			// {
			// ft.show(leaveFragment);
			// }
			//
//			switchFragment(activeFragmentindex, 1);
			activeFragmentindex=1;
			}
			break;
		// 企业信息页，代码为3
		case 2:
	
			if(activeFragmentindex!=2)
			{
				clearSelection();
			rb3.setBadgeBackgroundResource(R.drawable.unitinfo1);
			rb3.setBadgeimageBackgroundResource(R.drawable.widget_bar_bg_p);
			ChatFragment.bool3 = false;
			// 企业资料界面
//			 if(infoFragment==null)
//			{
				infoFragment = new EnterpriseDataFragment();
//				ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_in1);
				ft.replace(android.R.id.tabcontent, infoFragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//			}
//			 else
//			 {
//			 ft.show(infoFragment);
//			 }
			 activeFragmentindex=2;
			}
			break;
		// 设置页，代码为4
		case 3:
			if(activeFragmentindex!=3)
			{
				clearSelection();
			rb4.setBadgeBackgroundResource(R.drawable.settings1);
			rb4.setBadgeimageBackgroundResource(R.drawable.widget_bar_bg_p);
			ChatFragment.bool3 = false;
//			 if(setFragment==null)
//			{
				// 设置界面
				setFragment = new SetFragement();
//				ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_in1);
				// 跳转页面
				ft.replace(android.R.id.tabcontent, setFragment);
				// 备注
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//			}
//			 else
//			 ft.show(setFragment);
			 activeFragmentindex=3;
			}
			break;
		case 5:
			// 消息从notification传递来的
			// TextView txt = (TextView)txt.getText().toString()
			// get cookie and name from intent
			Bundle bundle = new Bundle();
			System.out.println(new SPhelper(this).getCookie()
					+ "==============>>>my_cookie");
			// bundle.putString("mobilecookie", new SPhelper(this).getCookie());
			bundle.putString("mobilecookie", new SPhelper(this).getCookie());
			bundle.putString("userName", userName);

			// bundle.putString("msgid",list.get(arg2).);
			chatFragment = (ChatFragment) getSupportFragmentManager()
					.findFragmentByTag(Constant.CHAT_FRAGMENT_TAG);
			if (chatFragment == null) {
				chatFragment = new ChatFragment();
				// 传递数据到chatFragment
				chatFragment.setArguments(bundle);
			}
			MainTabActivity.layout.setVisibility(View.GONE);

			ft.replace(android.R.id.tabcontent, chatFragment,
					Constant.CHAT_FRAGMENT_TAG);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(null);
			//ft.commit();

			break;

		}

		ft.commit();
	}

	/**
	 * @return
	 */
	private int unreadMessage() {
		// TODO Auto-generated method stub
		int unreadCount = 0;
		for (ConsultMsg msg : listConsultmsgs) {
			unreadCount += msg.Unreadcnt;
		}
		return unreadCount;
	}

	// 通知客服上线
	public boolean onLine() throws JSONException, UnsupportedEncodingException {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitid", monitor.unitid);// LoginActivity.inloginMonitor.unitid
		paramMap.put("usid", monitor.userid);
		paramMap.put("uscookie", monitor.uscookie);
		paramMap.put("deviceType", "android");
		
		RequestParams params = new RequestParams(paramMap);

		MobileChatClient.get("userver/online", params,
			new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject timeline) {
					try {
						// XXX: 检查下这里返回值true是字符串还是boolean类型
						if (timeline.getString("success").equals("true")) {
							Message message = new Message();
							message.what = 1;
							// handler.sendMessage(message);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			
			
				@Override
				public void onFailure(Throwable arg0) {
					
				}
				

			});

		return true;
	}

	// 处理刷新和doing，和poll消息
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// 消息类型为0，显示顾客列表tab
			if (msg.what == 1) {
				show(0);
			}
			// 处理消息poll循环的事件
			else if (msg.what == 2) {

			}
			// 客服在其它地方登录，收到的是被下线的通知
			else if (msg.what == 3) {
//				System.out.println("客服在其他地方登陆啦");
				//客服在其他地方登陆 点击“确定” 退出程序 ；点击“重新登录”重新登录
				
				Intent poll_service_intent = new Intent(context, MessageService.class);
				poll_service_intent.setAction(MessageService.ACTION);
				stopService(poll_service_intent);
				unbindService(serviceConnection);
				MessageService.isCancel = true;
				System.out.println("test423 request to stopService MessageService.isCancel"+MessageService.isCancel);
				MainTabActivity.multipleLoginLock=true;
				serviceBound = false; 
				
//				
				
				new AlertDialog.Builder(MainTabActivity.this).setCancelable(false)
						.setTitle("下线通知")
						.setIcon(R.drawable.g_ic_failed)
						.setMessage("您的帐号已在其他地方登陆,请重新登录")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								try {
									exitlogi();
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}
//								new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										// TODO Auto-generated method
//										// stub
//										Intent it = new Intent(
//												MainTabActivity.this,
//												LoginActivity.class);
//										startActivity(it);
//										MainTabActivity.this.finish();
//
//									}
//								}
								)
						.setNegativeButton("重新登录",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method
								// stub
								Intent it = new Intent(
										MainTabActivity.this,
										LoginActivity.class);
								startActivity(it);
								MainTabActivity.this.finish();
								MainTabActivity.multipleLoginLock=false;
							}
						}
//								new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int whichButton) {
//										try {
//											exitlogi();
//										} catch (UnsupportedEncodingException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										} catch (JSONException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}
//
//									}
//								}
								).create().show();
				//PollingUtils.stopPollingService(MainTabActivity.this,
				//		MessageService.class, MessageService.ACTION);
				// 这句话应该放在另一个函数里面，来取消后台请求的线程
//				MessageService.isCancel = true;
			}
			// 刷新顾客列表事件
			else if (msg.what == 4) {
				freshCustomerList();
			} 
			else if (msg.what == 1989) {
				// triggerNotification((ChatMessage) msg.obj);
				updateConsultFragmentMessage(null, 8);
			
			}
			// only for test
			else if (msg.what == 1991) {
				
				unitlogWebImage = new SmartImageView(MainTabActivity.this);
				unitlogWebImage.setImageUrl("http://www.mobilechat.im"
						+ "/uploads/unitlogos/13759670643342623.jpg");
				unitlogoBitmap = unitlogWebImage.getDrawingCache();
			}
		}

	};
	
	// 注销 没用了
//	public void zhuxiao(){
//		stopService(intentt);
//	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && flag == true) {
			
			if(!isyemian){
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// ===========================================================================消息获取的分发模块
	@Override
	protected void onResume() {
		super.onResume();
		
//		System.out.println("test416 MainTabActivity onResume,jpush receive");
		
		
		isPause = false;
		isDestroy = false;
		MessageService.isCancel = false;
		MessageService.isActivityPause = false;
		
		// 启动 poll service 服务
//		Intent poll_service_intent = new Intent(context, MessageService.class);
//		poll_service_intent.setAction(MessageService.ACTION);
//		if(!serviceBound)
//		{
//		 bindService(poll_service_intent, serviceConnection, Context.BIND_AUTO_CREATE);  
//		} m
//		startService(poll_service_intent);
		
//		PollingUtils.startPollingService(MainTabActivity.this,
//					getRandomTime(), MessageService.class,
//					MessageService.ACTION);

		//有关系吗?这是干嘛？》
//		Intent intent = getIntent();
		if (!UnitInfo.getAllUnitinfo().isEmpty()) {
			if (UnitInfo.getUnitinfofromId(monitor.unitid) != null) {
				unitinfo = UnitInfo.getUnitinfofromId(monitor.unitid);
			}
			getUnitInfo();
		} else {
			getUnitInfo();
		}
		
		
		
		
		
		//百度统计
				StatService.onResume(this);

	}

	// 设置点亮屏幕
	private void Light() {

		// 保持屏幕常亮
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock mWakeLock = pm.newWakeLock(
				PowerManager.FULL_WAKE_LOCK
						| PowerManager.ACQUIRE_CAUSES_WAKEUP, "LOCK_TAG");
		mWakeLock.acquire();

	}

	// 退出程序
	public boolean exitlogi() throws JSONException,
			UnsupportedEncodingException {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitid", monitor.unitid);
		paramMap.put("usid", monitor.userid);
		paramMap.put("uscookie", monitor.uscookie);
		RequestParams params = new RequestParams(paramMap);

		MobileChatClient.get("userver/offline", params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject timeline) {
						try {
							String flag = (String) timeline
									.getString("success");
							if (flag.equals("true")) {
								
								LoginActivity.isexitCount = 1;
								Intent intent = new Intent();
								Activity getedactivity = MainTabActivity.this;
								if (getedactivity != null) {
									intent.setClass(getedactivity,
											LoginActivity.class);
//									startActivity(intent);
									getedactivity.finish();
								}
							} else {
								LoginActivity.isexitCount = 1;
								Intent intent = new Intent();
								Activity getedactivity = MainTabActivity.this;
								if (getedactivity != null) {
									intent.setClass(getedactivity,
											LoginActivity.class);
//									startActivity(intent);
									getedactivity.finish();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally
						{
								System.exit(0);
								
						}

					}

				});
		return true;
	}

	// 按下home键进行的处理   
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//解绑定
				if(serviceBound)
				{
					Intent poll_service_intent = new Intent(context, MessageService.class);
					poll_service_intent.setAction(MessageService.ACTION);
					stopService(poll_service_intent);
					
					unbindService(serviceConnection);
					serviceBound = false;  
					
				}
	
		RequestParams requestParams = null;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		String mDeviceIdStr = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		Log.i("uuid", mDeviceIdStr);
		
		//调用JPush API设置Tag
		
		Set<String> tagSet = new LinkedHashSet<String>();
		tagSet.add(mDeviceIdStr);
		JPushInterface.setAliasAndTags(getApplicationContext(), null, tagSet, null);
		
		// 设置向上请求的参数
		paramMap.put("usid", MessageService.userid);	// XXX: 找个更好的地方取这个值 
		paramMap.put("deviceToken", mDeviceIdStr);
		paramMap.put("unReadMsgNum", "0");
		paramMap.put("deviceType", "android");
		
		requestParams = new RequestParams(paramMap);
		if (!isFinishing()) {
//			System.out.println("test423 keeponline send");
			if(!multipleLoginLock)
			{
			MobileChatClient.get(getApplicationContext(), "userver/keeponline",
					requestParams, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject jsonobj) {
							super.onSuccess(jsonobj);

//							Log.i("userver/keeponline", "test423 keeponline return"+jsonobj.toString());
						}
					});
			}
		}
		
		
		isPause = true;
		// 设置这个之后，才能真正把异步的那个循环请求取消掉
		MessageService.isCancel = true;
		System.out.println("test423 MessageService.isCancel"+MessageService.isCancel);

		Log.i("service", "test423 onStop"+serviceBound);

		MessageService.isActivityPause = true;
		cookie = mobileCookie;
		SPhelper cookiePhelper = new SPhelper(this);
		cookiePhelper.setCookie(mobileCookie);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
//		 启动 poll service 服务
//		Intent poll_service_intent = new Intent(context, MessageService.class);
//		poll_service_intent.setAction(MessageService.ACTION);
//		if(serviceBound)
//		{
//			startService(poll_service_intent);
//		}
		if(!serviceBound)
		{
			Intent poll_service_intent = new Intent(context, MessageService.class);
			poll_service_intent.setAction(MessageService.ACTION);
		    bindService(poll_service_intent, serviceConnection, Context.BIND_AUTO_CREATE);  
		    serviceBound = true;  
		}
		System.out.println("test423 MainTabActivity onRestart MessageService"+serviceBound);
	}

	// 双击返回键之后应该做的处理
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("test415 MainTabActivity onDestroy");
		super.onDestroy();

		if(serviceBound)
		{
			unbindService(serviceConnection);
//			Intent poll_service_intent = new Intent(context, MessageService.class);
//			poll_service_intent.setAction(MessageService.ACTION);
//			stopService(poll_service_intent);
		}
		// 撤消消息接收器。
		//unregisterMessageReceiver();
		
		// flagPollThreadState = false;
		// 如果应用程序退出，清空notification
		if (notifyMgr != null) {
			notifyMgr.cancelAll();
		}
		isDestroy = true;

		if (monitor != null) {
			monitor = Monitor.getMonitorpreLogin();
		}
	}

	// fragment的tag设置
	private String consultFragmentTag;
	private String chatFragmentTag;

	public String getConsultFragmentTag() {
		return consultFragmentTag;
	}

	public void setConsultFragmentTag(String consultFragmentTag) {
		this.consultFragmentTag = consultFragmentTag;
	}

	public String getChatFragmentTag() {
		return chatFragmentTag;
	}

	public void setChatFragmentTag(String chatFragmentTag) {
		this.chatFragmentTag = chatFragmentTag;
	}

	// 当获取的消息类型是consultFragmentTag相关的时候做的处理
	private boolean updateConsultFragmentMessage(ConsultMsg msg, int type) {

		boolean flag = false;
		flag = consultFragment.updateConsultList(msg, type);
		return flag;
	}

	// 这个函数里面判定是进行会话界面还是聊天界面进行处理。
	private byte[] lock = new byte[0];

	// 判断给定的消息是否在list里面
	public static boolean isInConsultList(ConsultMsg msg) {
		boolean flag = false;
		for (int i = 0; i < listConsultmsgs.size(); i++) {
			if (listConsultmsgs.get(i).mobilecookie.equals(msg.mobilecookie)
					&& listConsultmsgs.get(i).monitorId.equals(msg.monitorId)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	
	private ConsultMsg consult;

	// 标志当前聊天的用户cookie
	private String mobilecookiefromconsult;

	private String getMobilecookiefromconsult() {
		return mobilecookiefromconsult;
	}

	public void setMobilecookiefromconsult(String mobilecookiefromconsult) {
		this.mobilecookiefromconsult = mobilecookiefromconsult;
	}

	// 取消notification
	public static void clearNotification(int notificationId) {
		notifyMgr.cancel(notificationId);
	}

	public static void clearNotificationAll() {
		clearNotification(NOTIFY_ME_ID);
		clearNotification(NOTIFY_ALL_ID);
		// 删除MessageService的notification id
		clearNotification(Constant.MESSAGE_SERVICE_NOTIFICATION_ID);
		//System.out.println("cancel=================>>>");
	}

	// 初始化notification
	private void initNotification() {
		notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	// chatfragment是由谁创建的
	public static String toconsultlist = null;
	private static final int NOTIFY_ME_ID = 1989;
	private static final int NOTIFY_ALL_ID = 2013;
	private int count = 0;
	private static NotificationManager notifyMgr = null;
	// 定义一个HashMap<String,String>用于保存cookies=======>lastMessage
	public static HashMap<String, Object> mapNotification = new HashMap<String, Object>();

	//onpause会周期执行 是什么原因？
	@Override
	protected void onPause() {
		// TODO ;Auto-generated method stub
		super.onPause();
//		if(isFinishing())
//		{
//		Log.i("service", "testwhyonPauseready  isFinishing to stop service @MainTabActivity onPause.");
//		}
//		else
//		{
//			Log.i("service", "testwhyonPauseready");
//		}                       
		
		// 停止 poll service 服务
		Log.i("service", "test423 onPause ");
		// 向服务器发送一个keeponline请求
//		RequestParams requestParams = null;
//		HashMap<String, String> paramMap = new HashMap<String, String>();
//		String mDeviceIdStr = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
//		Log.i("uuid", mDeviceIdStr);
//		
//		//调用JPush API设置Tag
//		Set<String> tagSet = new LinkedHashSet<String>();
//		tagSet.add(mDeviceIdStr);
//		JPushInterface.setAliasAndTags(getApplicationContext(), null, tagSet, null);
//		// 设置向上请求的参数
//		paramMap.put("usid", MessageService.userid);	// XXX: 找个更好的地方取这个值 
//		paramMap.put("deviceToken", mDeviceIdStr);
//		paramMap.put("unReadMsgNum", "0");
//		paramMap.put("deviceType", "android");
//		
//		requestParams = new RequestParams(paramMap);
//		if (!isFinishing()) {
//			System.out.println("test423 keeponline send");
//			MobileChatClient.get(getApplicationContext(), "userver/keeponline",
//					requestParams, new JsonHttpResponseHandler() {
//						@Override
//						public void onSuccess(JSONObject jsonobj) {
//							super.onSuccess(jsonobj);
//
//							Log.i("userver/keeponline", "test423"+jsonobj.toString());
//						}
//					});
//		}
//		
//		
//		isPause = true;
//		// 设置这个之后，才能真正把异步的那个循环请求取消掉
//		MessageService.isCancel = true;
//		System.out.println("test423 MessageService.isCancel"+MessageService.isCancel);
		
		
		//百度统计
				StatService.onPause(this);
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("toconsultlist", toconsultlist);
	}

	// 获取企业资料
	public void getUnitInfo() {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitid", monitor.unitid);
		RequestParams params = new RequestParams(paramMap);
		MobileManagerClient.get("unit/info", params, monitor.uscookie,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						super.onFailure(arg0);
					}

					@Override
					protected Object parseResponse(String arg0)
							throws JSONException {
						// TODO Auto-generated method stub

						JSONObject jsonObject = new JSONObject(arg0);
						unitinfo.unitid = monitor.unitid;
						unitinfo.unitname = jsonObject.getString("name");
						try {
							if (jsonObject.getString("url").equals("")) {
								unitinfo.uniturl = "";
							} else {
								unitinfo.uniturl = jsonObject.getString("url");
							}
						} catch (JSONException e) {
							// TODO: handle exception
							unitinfo.uniturl = "未指定";
						}
						try {
							if (jsonObject.getString("tel").equals("")) {
								unitinfo.unittel = "未指定";
							} else {
								unitinfo.unittel = jsonObject.getString("tel");
							}
						} catch (JSONException e) {
							// TODO: handle exception
							unitinfo.unittel = "未指定";
						}
						try {
							if (jsonObject.getString("leavemsg_email").equals(
									"")) {
								unitinfo.unitemail = "未指定";
							} else {
								unitinfo.unitemail = jsonObject
										.getString("leavemsg_email");
							}
						} catch (JSONException e) {
							// TODO: handle exception
							unitinfo.unitemail = "未指定";
						}
						try {
							if (jsonObject.getString("location").equals("")) {
								unitinfo.address = "未指定";
							} else {
								unitinfo.address = jsonObject
										.getString("location");
							}
						} catch (JSONException e) {
							// TODO: handle exception
							unitinfo.address = "未指定";
						}
						try {
							if (jsonObject.getString("contact").equals("")) {
								unitinfo.unitcontact = "未指定";
							} else {
								unitinfo.unitcontact = jsonObject
										.getString("contact");
							}
						} catch (JSONException e) {
							// TODO: handle exception
							unitinfo.unitcontact = "未指定";
						}
						try {
							if (jsonObject.getString("logo").equals("")) {
								//这里要改
								unitinfo.unitlogo = "http://www.mobilechat.im/image/def_user_icon.jpg";
							} else {
								unitinfo.unitlogo = MobileManagerClient.GetBaseFileURl()
										+ jsonObject.getString("logo");
							}
						} catch (JSONException e) {
							// TODO: handle exception
							System.out.println("异常啦");
							unitinfo.unitlogo = "http://www.mobilechat.im/uploads/unitlogos/13759670643342623.jpg";
						}
						unitinfo.save();

						Message msg = new Message();
						msg.what = 1991;
						handler.sendMessage(msg);
						return super.parseResponse(arg0);

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
					}

					@Override
					public void onFailure(Throwable arg0, JSONObject arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
					}

				});

	}

	// 异步的获取留言消息
	public void leaveMobileChat() throws JSONException,
			UnsupportedEncodingException {
		HashMap<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("unitid", monitor.unitid);		// test "50e848d1447705745b000002"
		paramMap.put("usid", monitor.userid);		//  test "516620a2f1f9106e0500030e"
		paramMap.put("uscookie", monitor.uscookie);
		RequestParams params = new RequestParams(paramMap);
		MobileManagerClient.get("userver/getUnreadLeavemsgs", params,
				monitor.uscookie, new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject arg0) {
						// TODO Auto-generated method stub

						super.onSuccess(arg0);
					}

					@Override
					protected Object parseResponse(String arg0)
							throws JSONException {
						// TODO Auto-generated method stub

						if (arg0.equals("null")) {
						}

						return super.parseResponse(arg0);
					}

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(JSONArray arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						listLeaveMessage.clear();
						for (int i = 0; i < arg0.length(); i++) {
							try {
								JSONObject jsonObject = arg0.getJSONObject(i);
								Lvmsg leaveMessage = new Lvmsg();
								leaveMessage
										.setBadyImage(R.drawable.butn_close);
								Timeparser timeparser = new Timeparser();
								long a = new Long(jsonObject
										.getString("createdTime"));
								leaveMessage.setcreatedTime(paserTime(a));

								leaveMessage.setMessage(jsonObject
										.getString("content"));
								leaveMessage.setcontact(jsonObject
										.getString("contact"));
								leaveMessage.setmId(jsonObject.getString("_id"));
								listLeaveMessage.add(leaveMessage);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						showButtonWithText(R.id.radio2, listLeaveMessage.size());
					}

					@Override
					public void onFailure(Throwable arg0, JSONObject arg1) {
						// TODO Auto-generated method stub

						super.onFailure(arg0, arg1);
						
					}

					@Override
					protected void handleFailureMessage(Throwable arg0,
							String arg1) {
						// TODO Auto-generated method stub
						super.handleFailureMessage(arg0, arg1);
						
					}

				});

	}

	// 秒转成日期
	public String paserTime(long time) {
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String times = format.format(new Date(time));
		Log.i("日期格式---->", times);
		return times;
	}
	//只显示红点不显示留言条数
	private void showButtonWithText(int id,int text) {
		Activity activity = MainTabActivity.this;
		BadgeButton1 rb2 = (BadgeButton1) findViewById(id);
		if (activity != null) {
			{
				if (text > 0) {
//					rb2.setBadgeVisible();
					rb2.setBadgeDrawableRes(R.drawable.g_unread_messages_bg);
					rb2.showBadge();
				} 
				else {
					rb2.hideBadge();
				}

			}
		}

	}
	// 显示留言条数
	private void showButtonWithTextNumber(int id, int text) {
		Activity activity = MainTabActivity.this;
		BadgeButton1 rb2 = (BadgeButton1) findViewById(id);
		if (activity != null) {
			{
				if (text < 10 && text > 0) {
					rb2.setBadgeVisible();
					rb2.setBadgeDrawableRes(R.drawable.g_unread_messages_bg);
					rb2.setBadgeText(text + "");
					rb2.showBadge();
				} else if (text >= 10) {
					rb2.setBadgeVisible();
					rb2.setBadgeDrawableRes(R.drawable.g_unread_messages_bg);
					rb2.setBadgeText("..");
					rb2.showBadge();
				} else {
					rb2.hideBadge();
				}

			}
		}

	}

	// 判断当前时间和最后一次会话
	public Boolean getTime(Long time2) {

		if (time2 != null
				&& (((System.currentTimeMillis() - time2) / 1000) >= Constant.OFFLINE_TIME_COUNT)) {
			return true;
		} else {
			return false;
		}
	}

	// 时时更新客户列表的状态
	// XXX: 这里会不会造成一些刷新上的问题，比如烦人的跳动什么的
	private void freshCustomerList() {
		if (isfinished) {
			isfinished = false;
			int isRefreshFlag = 0;
			if (listConsultmsgs != null && listConsultmsgs.size() != 0) {
				for (int i = 0; i < listConsultmsgs.size(); i++) {
					ConsultMsg msgTmp = listConsultmsgs.get(i);
						
						if (msgTmp.isOnline && msgTmp.time != null
								&& getTime(msgTmp.time)) {
							isRefreshFlag++;
							//删除CopyOnWriteArrayList中的元素
							listConsultmsgs.remove(msgTmp);
							msgTmp.isOnline = false;
							msgTmp.time = System.currentTimeMillis();
							//给 CopyOnWriteArrayList 中添加元素
							listConsultmsgs.add(i, msgTmp);
						}
				}
				if (isRefreshFlag >= 1)
					updateConsultFragmentMessage(null, 8);
				else {
					isfinished = true;
				}
			} else if (listConsultmsgs.size() == 0) {
				isfinished = true;
			} else {
				isfinished = true;
			}
		}

	}

	public static CopyOnWriteArrayList<ConsultMsg> listConsultmsgs = new CopyOnWriteArrayList<ConsultMsg>();
	public static volatile boolean isfinished = true;
	public static volatile boolean isExisted = false;

	// 回调接口

	// service相关
	class Listener implements OnGetMessageListener {
		private static final long serialVersionUID = -8807053908851047217L;

		@Override
		public void onRefreshState() {
			//更新列表状态
			freshCustomerList();
		}

		//在聊天界面
		@Override
		public void onMessageArriveUpdateChatFragment(ChatMessage chatMessageGet) {
			if (getMobilecookiefromconsult() != null
					&& chatMessageGet.toId.equals(monitor.userid)
					&& chatMessageGet.fromId
							.equals(getMobilecookiefromconsult())) {
				// 来消息
				chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentByTag(Constant.CHAT_FRAGMENT_TAG);
				if (chatFragment == null) {
					chatFragment = new ChatFragment();
				}
				if(!MainTabActivity.isPause)
				{
						chatFragment.updateChatList(chatMessageGet);
				}
//				Log.d("message", "test417 Messagechat onMessageArriveUpdateChatFragment2"+MainTabActivity.isPause);
				flag = true;
			}
			/*
			 * 1.ChatFragment正在运行 
			 * 2.当前来的消息是登录客服的消息 
			 * 3.当前来的消息和当前客服正在聊天的客户不是同一个人
			 */
			else {
				Message message = Message.obtain();
				message.what = 1989;
				message.obj = chatMessageGet;
				handler.sendMessage(message);
			}
			//MessageService.isCancel = false;
		}

		//记录多少条未读消息
		@Override
		public void onMessageArriveUpdateKeHuOnline() {
			System.out.println("test========  onMessageArriveUpdateKeHuOnline");
			updateConsultFragmentMessage(null, 8);
			
			
		}

		//在列表页面
		@Override
		public void onMessageArriveUpdateConsultFragment() {
			Light();
			if(!MainTabActivity.isPause)
			{
//			updateConsultFragmentMessage(null, 8);
				Log.d("message", "test423 MessageConsult onMessageArriveUpdateConsultFragment2");
					consultFragment.updateConsultList(null, 8);
			}
		}

		//用户离线
		@Override
		public void onMessageArriveUpdateUserLoginState() {
			
			if(booleanb){
				handler.sendEmptyMessage(3);
				booleanb = false;
			}
			
			//MessageService.isCancel = false;
		}

		@Override
		public void onMessageArriveOnActivityPause(ChatMessage chatMessageGet) {
			//			 triggerNotification(chatMessageGet);
			//MessageService.isCancel = false;
			
		}
	}

	
	public static ArrayList<Lvmsg> listLeaveMessage;

	//
	private void setMainTableWhenSwitchFragment(int fragmentFromIndex, int fragmentToIndex) {
		clearSelection();
		switch (fragmentToIndex) {
		case 0:
			rb1.setBadgeBackgroundResource(R.drawable.chat1);
			rb1.setBadgeimageBackgroundResource(R.drawable.widget_bar_bg_p);
			int count = unreadMessage();
			if (count > 0) {
				showButtonWithText(R.id.radio1, count);
			}
			showButtonWithText(R.id.radio2, listLeaveMessage.size());
			break;

		case 1:
			ChatFragment.bool3 = false;
			rb2.setBadgeBackgroundResource(R.drawable.leavemsg1);
			rb2.setBadgeimageBackgroundResource(R.drawable.widget_bar_bg_p);
			break;
		case 2:
			rb3.setBadgeBackgroundResource(R.drawable.unitinfo1);
			rb3.setBadgeimageBackgroundResource(R.drawable.widget_bar_bg_p);
			ChatFragment.bool3 = false;
			break;
		case 3:
			rb4.setBadgeBackgroundResource(R.drawable.settings1);
			rb4.setBadgeimageBackgroundResource(R.drawable.widget_bar_bg_p);
			ChatFragment.bool3 = false;
			break;
		}

	}

	private Fragment fragmentFactoryByIndex(int fragmentToIndex, boolean exist) {

		switch (fragmentToIndex) {
		// 聊天页，代码为1
		case 0:
			if (exist)
				return consultFragment;
			else
				return fragmentList[0] = consultFragment = new ConsultFragment();
		case 1:
			if (exist) {
				return leaveFragment;
			} else {
				return fragmentList[1] = leaveFragment = new LeaveMsgFragment();
			}
		case 2:
			if (exist) {
				return infoFragment;
			} else {
				infoFragment = new EnterpriseDataFragment();
				fragmentList[2] = infoFragment;
				return infoFragment;

			}
		case 3:
			if (exist) {
				return setFragment;
			} else {
				return fragmentList[3] = setFragment = new SetFragement();
			}
		default:
			return null;
		}
		
	}
	private void  shengyin(final String type) {
		final SongAndVibrator songAndVibrator = new SongAndVibrator(
				getApplicationContext());
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				songAndVibrator.isSong(getApplicationContext(), type);
				songAndVibrator.isVibrate(getApplicationContext(),
						Constant.VIBRATOR_FROM_BACK);
			}

		}, 5);
	}
}
