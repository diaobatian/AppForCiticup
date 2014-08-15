package com.citi.mc.service;


/** 
 * @作者 lianma 
 * @email lianlupeng@gmail.com
 * @创建日期 2013-11-4 
 * @版本 V 1.0 
 * 获取消息的service，作为前端的service
 * version1.2
 * data：2013-11-23
 * setrvice完全作为路由层，作为消息的起始站
 */

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.RemoteViews;

import com.citi.mc.db.ChatMessage;
import com.citi.mc.db.ConsultMsg;
import com.citi.mc.db.Monitor;
import com.citi.mc.db.UserInfo;
import com.citi.mc.app.AppStartActivity;
import com.citi.mc.app.ChatFragment;
import com.citi.mc.app.LoginActivity;
import com.citi.mc.app.MainTabActivity;
import com.citi.mc.R;
import com.citi.mc.app.SPhelper;
import com.citi.mc.utils.Constant;
import com.citi.mc.utils.LogWriter;
import com.citi.mc.utils.MobileChatClient;
import com.citi.mc.utils.SongAndVibrator;
import com.citi.mc.utils.Timeparser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MessageService extends Service {

	public static final String ACTION = "com.citi.mc.service.PollingService";
//	 private final IBinder mBinder = new LocalBinder();  
	private AlarmManager mAlarmManager = null;
	private PendingIntent mPendingIntent = null;
	private Notification mNotification;
	private NotificationManager mManager;
	// poll消息是否执行的标志位
	public static volatile boolean isCancel = false;
	// poll消息是否执行的标志位
	public static volatile boolean isNotifiManager = false;
	// 客服的状态信息
	public static String unitid;
	public static String userid;
	public static String uscookie;
	private Boolean boolean1 = false;

	private String userCookie;
	private String userCity;
	private String userProvince;
	private String userIsp;
	// 获得五位数的随机数
	private long t;
	// 定义各个页面的状态
	public static boolean isConsultFragment = true;
//	public static boolean isChatFragment = false;
	public static String currentMobileCookie = null;
	public static boolean isActivityPause = false;
	// 收到消息的ids
	private String messageIdString;
	
	
//	private OnServiceUIChangeListener ServiceUIChangeListener;
//	//回调接口
//	public void SetServiceUIChangeListener(OnServiceUIChangeListener onServiceUIChangeListener) {  
//    this.ServiceUIChangeListener = onServiceUIChangeListener;  
//}  
	//private PowerManager pm;
	//private WakeLock wakeLock = null;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("MessageService", "MessageService onCreate() executed");

		
		initNotifiManager();
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("MessageService", "test416 MessageServiceonDestroy() executed");

		// 销毁时重新启动Service
//		PollingUtils.startPollingService(MessageService.this,
//				getRandomTime(), MessageService.class,
//				MessageService.ACTION);
		super.onDestroy(); 
		isCancel = true;
		
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("MessageService", "test423 MessageServiceonStartCommand() executed");
		
		
		Monitor monitor = Monitor.getMonitorpreLogin();
		if (monitor != null) {
			unitid = monitor.unitid;
			userid = monitor.userid;
			uscookie = monitor.uscookie;
		}
		//其他地点美誉登录才能发poll
		if (!MainTabActivity.multipleLoginLock) {
			if (MobileChatClient.polling) {
				isCancel = false;
			} else {
				isCancel = false;
				poll();
			}
		}
		
//		if(!pm.isScreenOn()){
//			acquireWakeLock();
//		}else{
//			releaseWakeLock();
//		}
		
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d("MessageService", "test423 MessageServic onStart() executed");
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d("MessageService", "test423 MessageService onBind() executed polling" +MobileChatClient.polling);
		Monitor monitor = Monitor.getMonitorpreLogin();
		if (monitor != null) {
			unitid = monitor.unitid;
			userid = monitor.userid;
			uscookie = monitor.uscookie;
		}
		//service与mainActivity绑定后没必要判断是否在poll 
//		if (MobileChatClient.polling) {
//			isCancel = false;
//		}
//		else 
		Log.d("MessageService", "test423 MessageService onBind() executed MainTabActivity.multipleLoginLock" +MainTabActivity.multipleLoginLock);
		if (!MainTabActivity.multipleLoginLock) {
//			if (!MobileChatClient.polling) {
				isCancel = false;
				poll();
//			}
		}
//		}
		return new MsgBinder();
	}

	public class MsgBinder extends Binder {
		/**
		 * 获取当前Service的实例
		 * 
		 * @return
		 */
		public MessageService getService() {
			return MessageService.this;
		}
	}
	/**
	 * 更新进度的回调接口 所有消息的操作，都是通过回掉完成的 such as updateChatFragment
	 * updateConsultFragment
	 */
	public static OnGetMessageListener onGetMessageListener;
	private String mobileCookie;
	private ConsultMsg consult;

	/**
	 * 获取当前Service的实例
	 * 
	 * @return
	 */
	public MessageService getService() {
		return MessageService.this;
	}

	/*
	 * 辅助方法： isExistThenUpdate 保持最多30条会话
	 */
	private void isExistThenUpdate(ConsultMsg consultmsg, String lastMsg)   {
		boolean isExist = false;
//		Date oldestTime=Calendar.getInstance().getTime();
		//要删除的位置
		int index=0;
		long tempTime=0;
		long oldestTime=System.currentTimeMillis();
//		Date tmptime;
//		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < MainTabActivity.listConsultmsgs.size(); i++) {
			// System.out.println("mobile cookies======" + msgTmp.mobilecookie);
			if (MainTabActivity.listConsultmsgs.get(i).mobilecookie
					.equals(mobileCookie)
					&& MainTabActivity.listConsultmsgs.get(i).monitorId
							.equals(userid)) {
				MainTabActivity.listConsultmsgs
						.remove(MainTabActivity.listConsultmsgs.get(i));
				if (!MainTabActivity.isInConsultList(consultmsg)) {
					MainTabActivity.listConsultmsgs.add(0, consultmsg);
				}
				isExist = true;
				// break;
			}
			
			tempTime=MainTabActivity.listConsultmsgs.get(i).time;
				if ((oldestTime>tempTime)&&(MainTabActivity.listConsultmsgs
						.get(i).isOnline==false)) {
					oldestTime = tempTime;
					index = i;
				}
		}
		// 如果是新用户
		if (!isExist) {
			if (!MainTabActivity.isInConsultList(consultmsg)) {
				MainTabActivity.listConsultmsgs.add(0, consultmsg);
			}
		}
		if(Constant.MAX_CONSULTMSGCOUNT < MainTabActivity.listConsultmsgs.size())
		{
			
			ConsultMsg.RemoveMobileCookie(MainTabActivity.listConsultmsgs.get(index).mobilecookie,MainTabActivity.listConsultmsgs.get(index).monitorId);
			MainTabActivity.listConsultmsgs.remove(index);
			//通知改变UI 不用
//			ServiceUIChangeListener.ChangeUI(1);
			
			
			
		}
	}

	// 处理新上线用户
	private void dealWithKehuOnline(String mobileCookie, JSONObject jsonObject) {
		ConsultMsg consultmsg = new ConsultMsg();
		consultmsg.mobilecookie = mobileCookie;
		consultmsg.isFirstVisit = ConsultMsg.getConsultMsgFromMobileCookie(mobileCookie, userid);
		if (ConsultMsg.getConsultMsgFromMobileCookie(consultmsg.mobilecookie,
				userid)) {
			try {
				consultmsg.image = R.drawable.m;
				consultmsg.isOnline = true;
				consultmsg.Unreadcnt = 0;
				consultmsg.monitorId = userid;
				consultmsg.os = jsonObject.getString("os");
				consultmsg.Username = jsonObject.getString("username");
				consultmsg.tel = jsonObject.getString("tel");
				consultmsg.email = jsonObject.getString("email");
				consultmsg.IM = jsonObject.getString("IM");
				consultmsg.Address = jsonObject.getString("address");
				consultmsg.remark = jsonObject.getString("remark");
				consultmsg.domain = jsonObject.getString("domain");
				consultmsg.keyword = jsonObject.getString("keyword");
				consultmsg.Appoinment = jsonObject.getBoolean("appoinment");
				consultmsg.latestmsg = jsonObject.getString("latest");
				consultmsg.userver = jsonObject.getString("userver");

				consultmsg.createdtime = Timeparser.getTime(jsonObject
						.getString("createdTime"));
				consultmsg.time = jsonObject.getLong("createdTime");
				consultmsg.LDTime = Timeparser.getTime(jsonObject
						.getString("LDTime"));
				consultmsg.province = jsonObject.getString("province");
				consultmsg.city = jsonObject.getString("city");
				consultmsg.isp = jsonObject.getString("isp");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// 1.获取传递多来的(ConsultMsg) msg.obj
			// 2.清除MainTabActivity.listConsultmsgs存在的Consultmsg
			consultmsg.time = System.currentTimeMillis();
			isExistThenUpdate(consultmsg, consultmsg.latestmsg);
			ConsultMsg.saveConsultmsg(consultmsg, userid);
		}
		// 用户已经存在
		else {
			System.out.println("用户已存在");
		}
		if (MainTabActivity.monitor != null) {
			System.out.println("onMessageArriveUpdateKeHuOnline");
			// 记录多少条未读消息
			onGetMessageListener.onMessageArriveUpdateKeHuOnline();
		} else {
			//isCancel = false;
		}
	}

	public void retry() {

		if (!isCancel) {
			
			repoll();
		} else {
			if (MobileChatClient.polling) {
				LogWriter.write("MessageService", "retry() MobileChatClient.polling" + MobileChatClient.polling);
				MobileChatClient.cancelTask(this);
				MobileChatClient.polling = false;
			}
		}
	}
	
	public void repoll() { 
		// 随机数生成器
		Random rd=new Random();
		// 获得50~250ms的一个随机数
		int delaytime = (int)(rd.nextDouble() * 200 + 50);
		
		System.out.println("test423 MessageService repoll");
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				// 重新发poll
				
				poll();
			}
		}, delaytime);
		
	}
	
	
	/**
	 * 模拟下载任务，每秒钟更新一次
	 */
	public void poll() {
		
		System.out.println("test423 prestart to poll" + " ,iscancel " + isCancel);
		LogWriter.write("MessageService", "prestart to poll" + " ,iscancel " + isCancel);
		if (!isCancel) {
			// 定期的刷新列表
			if (onGetMessageListener != null)
				onGetMessageListener.onRefreshState();
			
			MobileChatClient.polling = true;
			System.out.println("test423 start to poll" + " ,iscancel " + isCancel);
			MobileChatClient.get(getApplicationContext(), "userver/poll",
					getRequestParams(), new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject jsonobj) {
//							if(MainTabActivity.isPause)
//								return;
							
							super.onSuccess(jsonobj);
							String type = null;
							try {
								boolean flag = jsonobj.getBoolean("success");
								
								// 成功获取消息
								if (flag) {
									JSONArray msgPollArray = jsonobj
											.getJSONArray("msgs");
									
									messageIdString = "";
									int lastMsg = 0;
									ChatMessage chatMessageGet = null;
									
									for (int i = 0; i < msgPollArray.length(); i++) {
										// 获取相关的基本信息
										JSONObject msgPollObject = msgPollArray
												.getJSONObject(i);
										String msgId = "";
										if (msgPollObject.has("_id")) {
											msgId = msgPollObject
													.getString("_id");
											if( msgId != null ){
												messageIdString = String.valueOf(msgId)
														+ "," + messageIdString;
											}
										}
											
										type = msgPollObject.getString("type");
										mobileCookie = msgPollObject
												.getString("fromId");
										// 路由机制
										
										// 普通消息
										if (type.equals("mb_normal")
												&& msgPollObject.getString(
														"toId").equals(userid)) {
											lastMsg++;
											// 创建一条新来的消息
											chatMessageGet = new ChatMessage(
													R.drawable.badge1,
													"true",
													msgPollObject
															.getString("content"),
													msgPollObject
															.getString("createdTime"),
													msgId,
													msgPollObject
															.getString("fromId"),
													userid,
													msgPollObject
															.getString("fromName"));
											
											consult = ConsultMsg
													.getConsutFromUserName(
															mobileCookie,
															userid);
											// 客户没有直接登陆,直接发起聊天会话,设置此顾客的状态为firstvisit
											if (consult == null) {
												// 构造获取参数
												HashMap<String, String> paramMap = new HashMap<String, String>();
												paramMap.put("unitid", unitid);
												paramMap.put("usid", userid);
												paramMap.put("uscookie",
														uscookie);
												paramMap.put("mobile",
														mobileCookie);
												RequestParams params = new RequestParams(
														paramMap);

												sendEmptyMessageToServer(
														unitid, userid,
														uscookie, mobileCookie);
												// 构造url，异步获取
												System.out.println("test423 userver/mobileInfo send  ");
												MobileChatClient
														.get("userver/mobileInfo",
																params,
																new JsonHttpResponseHandler() {

																	@Override
																	public void onFinish() {
																		super.onFinish();
																		//isCancel = false;
																	}

																	@Override
																	public void onFailure(
																			Throwable arg0,
																			JSONObject arg1) {
																		super.onFailure(
																				arg0,
																				arg1);
																		System.out
																				.println("fail");
																		//isCancel = false;
																	}

																	@Override
																	public void onSuccess(
																			JSONObject json) {
																		super.onSuccess(json);
																		System.out
																				.println("success");
																		try {
																			if (json.getBoolean("success")) {

																				// 处理新上线用户
																				dealWithKehuOnline(
																						mobileCookie,
																						json.getJSONObject("mobile"));
																			} else {
																				//isCancel = false;
																			}
																		} catch (JSONException e) {
																			// TODO
																			e.printStackTrace();
																			//isCancel = false;
																		}
																	}
																});
											}
											// 客户信息已经保存在手机端
											else {
												Log.i("as",
														"msgPollObject.getString(content)=====>"
																+ msgPollObject
																		.getString("content"));
												consult.latestmsg = msgPollObject
														.getString("content");
												consult.latestTime = Timeparser.getTime(msgPollObject
														.getString("createdTime"));
												consult.time = Long
														.parseLong(msgPollObject
																.getString("createdTime"));
												consult.Username = msgPollObject
														.getString("fromName");
												consult.mobilecookie = mobileCookie;
												consult.isOnline = true;
												if(!MainTabActivity.isPause)
												{
												consult.Unreadcnt += 1;
												}
												consult.time = System
														.currentTimeMillis();
												// 保存聊天消息
												ChatMessage.saveLvmsg(
														chatMessageGet,
														mobileCookie, userid);
												sendEmptyMessageToServer(
														unitid, userid,
														uscookie, mobileCookie);
												// 如果app activity没有调用ondestroy方法
												// if(AppInfo.getById(Constant.APPINFO_DEFAULT_ID)!=null&&AppInfo.getById(Constant.APPINFO_DEFAULT_ID).isDeade==0)
												if (MainTabActivity.monitor != null) {
													isExistThenUpdate(
															consult,
															msgPollObject
																	.getString("content"));
													new SPhelper(
															getApplicationContext())
															.setCookie(mobileCookie);
													// 如果正在聊天页面
													
													if (MainTabActivity.isChatFragment) {
//														Log.d("message", "test417 Messagechat onMessageArriveUpdateChatFragment");
														onGetMessageListener.onMessageArriveUpdateChatFragment(chatMessageGet);
													}
													// 如果在客户列表页面
													else if (isConsultFragment) {
														Log.d("message", "test423 MessageConsult onMessageArriveUpdateConsultFragment 1");
														onGetMessageListener
																.onMessageArriveUpdateConsultFragment();
													} else {
														onGetMessageListener
																.onMessageArriveUpdateConsultFragment();
													}
													if (isActivityPause) {
														//应该可以直接调用这个接口吧   无效这时候service已经停掉了
														onGetMessageListener
																.onMessageArriveOnActivityPause(chatMessageGet);

													}
													// 根绝测试添加其他的处理状态
												} else {
													System.out
															.println("MainTabActivity is null");
												}
												// 保存consult item的信息
												consult.save();
											}
										}
										// 路由的第二个mobile_online
										// 处理新上线的用户
										else if (type.equals("mobile_online")) {

											JSONObject jsonObject = msgPollObject
													.getJSONObject("mobile");
											String mobileCookies = jsonObject
													.getString("cookie");
											
											dealWithKehuOnline(mobileCookies,
													jsonObject);
											if (ChatMessage
													.getchatfrommid(mobileCookies)) {
												welcomeMessage(chatMessageGet,
														msgPollObject);

											}

											boolean1 = true;
											// 处理新获得的客户信息
										} else if (type.equals("mb_ipinfo")) {
											UserInfo userInfo = new UserInfo();
											JSONObject jsonObject = msgPollObject
													.getJSONObject("ipinfo");
											userInfo.cookie = mobileCookie;
											userInfo.city = jsonObject
													.getString("city");
											userInfo.province = jsonObject
													.getString("province");
											userInfo.isp = jsonObject
													.getString("isp");
											userInfo.save();
										}
										// 未实现的路由机制
										else {
											//isCancel = false;
										}
									}
									// 当activity调用destroy方法时进行的操作
									// if(AppInfo.getById(Constant.APPINFO_DEFAULT_ID)!=null&&AppInfo.getById(Constant.APPINFO_DEFAULT_ID).isDeade==1)
//									Log.d("sound", MainTabActivity.isPause+" test418 sound type "+ type +" lastMsg" + lastMsg);
									if(!MainTabActivity.isPause){
										if (lastMsg >= 1) {
											if (MainTabActivity.isChatFragment
													&& ChatFragment.isChatFragmentCookie
															.equals(mobileCookie))
												type = "mb_system";
							
//											Log.d("sound", "test418 sound type "+ type +" lastMsg" + lastMsg);
											shengyin(type);
										}
								}
							
								} 
								//用户在其他地方登陆
								else if (flag == false) {
									onGetMessageListener
											.onMessageArriveUpdateUserLoginState();
								}
								
								//retry();

							} catch (Exception e) {
								//retry();
								System.out.println("pollhandle+ pollsuccess but handle Exception"+e.getMessage());
//								Log.d("pollhandle", "pollsuccess but handle Exception " + e.getMessage());
								e.printStackTrace();
								//isCancel = false;
							}

						}
						//超时的时候
						@Override
						public void onFailure(Throwable arg0, JSONObject arg1) {
							super.onFailure(arg0, arg1); 
							
//							Log.d("pollhandle", "onFailure " + arg0.getMessage());
							System.out.println("pollhandle + onFailure"+arg1);
							retry();
							
							
							// 当没有poll到消息的时候，iscancel得状态是没办法改变的
							//isCancel = false;
						}
						//超时重发
						@Override
						protected void handleFailureMessage(Throwable arg0,
								String arg1) {
							super.handleFailureMessage(arg0, arg1);
//							Log.d("pollhandle", "handleFailureMessage " + arg0.getMessage());
							System.out.println("pollhandle + handleFailureMessage"+arg1);
							retry();
							
							
							// showNotification(Constant.MESSAGE_NOT_POLLED,0);
							// 当没有poll到消息的时候，iscancel得状态是没办法改变的
							//isCancel = false;
						}

						
						@Override
						protected void sendFailureMessage(Throwable arg0,
								String arg1) {
							super.sendFailureMessage(arg0, arg1);
							System.out.println("pollhandle + sendFailureMessage"+arg1);
							retry();
							
							//isCancel = false;
						}

						//
						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							super.onFinish();

								System.out.println("pollhandle test423 onFinish");
//								if()
							retry();
							
							//isCancel = false;
						}
					});
		}
	}

	public void cancelGetMessage() {
		isCancel = true;
	}

	public void startGetMessage() {
		isCancel = false;
	}

	/**
	 * 
	 * @param chatMessageGet
	 * @param msgPollObject
	 * @类名 MessageService.java
	 * @包名 com.citi.mobilechat.service
	 * @作者 ChunTian
	 * @时间 2013年12月27日 下午12:39:47
	 * @Email ChunTian1314@vip.qq.com
	 * @版本 V1.0
	 * @功能 创建新用户欢迎消息
	 */
	private void welcomeMessage(ChatMessage chatMessageGet,
			JSONObject msgPollObject) {

		try {
			t = System.currentTimeMillis();
			chatMessageGet = new ChatMessage(
					R.drawable.badge1,
					"false",
					"产品免费试用中，欢迎注册 http://www.mobilechat.im 产品咨询热线028-66566666 QQ:33012263",
					System.currentTimeMillis() + "", t
							+ Long.parseLong(String.valueOf(1)) + "",
					msgPollObject.getString("fromId"), userid, msgPollObject
							.getString("fromName"));
			// 保存聊天消息
			ChatMessage.saveLvmsg(chatMessageGet, mobileCookie, userid);
			chatMessageGet = new ChatMessage(R.drawable.badge1, "false",
					"4个客服，现在推广价1200元/年", System.currentTimeMillis() + "", t
							+ Long.parseLong(String.valueOf(2)) + "",
					msgPollObject.getString("fromId"), userid,
					msgPollObject.getString("fromName"));
			// 保存聊天消息
			ChatMessage.saveLvmsg(chatMessageGet, mobileCookie, userid);
			chatMessageGet = new ChatMessage(R.drawable.badge1, "false",
					"欢迎咨询 ^_^", System.currentTimeMillis() + "", t
							+ Long.parseLong(String.valueOf(3)) + "",
					msgPollObject.getString("fromId"), userid,
					msgPollObject.getString("fromName"));
			// 保存聊天消息
			ChatMessage.saveLvmsg(chatMessageGet, mobileCookie, userid);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 构造RequestParams
	private RequestParams getRequestParams() {
		RequestParams requestParams = null;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		// sPhelper = new SPhelper(getActivity());
		paramMap.put("unitid", unitid);// LoginActivity.inloginMonitor.unitid
		paramMap.put("usid", userid);
		paramMap.put("uscookie", uscookie);
		paramMap.put("deviceType", "android");
		System.out.println("poll uscookie" + uscookie);
		//这一次求情 还要传上上一次已经获得的消息的Ids
		if (messageIdString != null && messageIdString.length() >= 1)
			paramMap.put("msgids",
					messageIdString.substring(0, messageIdString.length() - 1)
							.trim());
		requestParams = new RequestParams(paramMap);
		return requestParams;
	}

	// 消息的id.0

	// 初始化通知栏配置
	private void initNotifiManager() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//pm = (PowerManager)getSystemService(POWER_SERVICE);
	}

	// 弹出Notification
//	private void showNotification(ChatMessage messageInMap) {
//		System.out.println("showNotification");
//		Notification mNotification = new Notification(R.drawable.ic_launcher,
//				"有新消息", System.currentTimeMillis());
//		;
//		Intent intent = new Intent(this, MainTabActivity.class);
//		// toconsultlist="toconsultlist";
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		mNotification.contentView = new RemoteViews(getPackageName(),
//				R.layout.notification_item_layout);
//		PendingIntent pendingIntent = PendingIntent.getActivity(
//				getApplicationContext(), 0, intent, 0);
//		mNotification.contentView.setTextViewText(R.id.notification_content,
//				messageInMap.content);
//		mNotification.contentView.setTextViewText(R.id.notification_name,
//				messageInMap.fromName + "发来新消息");
//		mNotification.number = 1;
//		mNotification.contentView.setImageViewResource(R.id.notification_image,
//				R.drawable.ic_launcher);
//		// mNotification.contentView.setTextViewText(R.id.notifcation_time,"2013.11.15");
//		mNotification.defaults |= Notification.DEFAULT_LIGHTS;
//		mNotification.ledARGB = 0xff00ff00;
//		mNotification.ledOnMS = 300;
//		mNotification.ledOffMS = 1000;
//		mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
//		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//		mNotification.contentIntent = pendingIntent;
//		mManager.notify(Constant.MESSAGE_SERVICE_NOTIFICATION_ID, mNotification);
//	}

	

	// 显示声音
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

	// 发送一个空的请求，消除客户端登陆，给服务器带来的bug
	public static void sendEmptyMessageToServer(String unitid, String usid,
			String uscookie, String usercookie) {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitid", unitid);
		paramMap.put("usid", usid);
		paramMap.put("uscookie", uscookie);
		paramMap.put("mobile", usercookie);
		RequestParams params = new RequestParams(paramMap);
		// 构造url，异步获取
		MobileChatClient.get("userver/notifyMbonline", params,
				new JsonHttpResponseHandler() {
				});
	}

//	private void acquireWakeLock() {
//		if (null == wakeLock) {
//			
//			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
//					| PowerManager.ON_AFTER_RELEASE, "PostLocationService");
//			if (null != wakeLock) {
//				wakeLock.acquire();
//			}
//		}
//	}
//
//	// 释放设备电源锁
//	private void releaseWakeLock() {
//		if (null != wakeLock) {
//			wakeLock.release();
//			wakeLock = null;
//		}
//	}
	
	// 获取1-10的随机数
	private int getRandomTime() {
		Random rd=new Random();//随机数生成器
		int j=(int)(rd.nextDouble() * 10);
		return j;
	}


}
