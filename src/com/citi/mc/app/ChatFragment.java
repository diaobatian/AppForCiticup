package com.citi.mc.app;

/**
 *聊天界面
 */
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.util.Log;
import com.baidu.mobstat.StatService;
import com.citi.mc.R;
import com.citi.mc.adapter.ChatMsgAdapter;
import com.citi.mc.adapter.ExpressionAdapter;
import com.citi.mc.adapter.MyPagerAdapter;
import com.citi.mc.db.ChatMessage;
import com.citi.mc.db.ConsultMsg;
import com.citi.mc.db.GetChatExpression;
import com.citi.mc.db.Monitor;
import com.citi.mc.db.UserInfo;
import com.citi.mc.utils.Constant;
import com.citi.mc.utils.MobileChatClient;
import com.citi.mc.utils.ChatSmileyParser;
import com.citi.mc.utils.IsNetWorkAvailable;
import com.citi.mc.utils.PullToRefreshListView;
import com.citi.mc.utils.PullToRefreshListView.OnRefreshListener;
import com.citi.mc.utils.Timeparser;
import com.citi.mc.service.MessageService;
import com.citi.mc.service.PollingUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 消息的fragment
 * refactor 
 * author:niangma
 * time :2013.11.6
 * 
 * version:1.1
 * changelog:
 * 
 1.完成ui loading出现在加载数据的时候，实现思路：不应该在onCreate方法里面执行太多的代码
 2.redirecButton：以前的功能是转接客服，现在是客服资料，改成kefuInfoButton
 3.消息获取机制
 3.1.先从数据库获取消息展示100条，每个用户暂定50条数据，考虑到用户的稳定性,这个数量是可以控制的
 3.2.异步的加载数据进行更新，确保显示的是最新的数据
 4.消息的验证
 version1.2:
 1.增加异步线程取消的操作
 version1.3
 机制存在bug
 1.和该客服聊过天，但是是在web端进行聊天的，数据库的消息数目不够50，或者根本没有
 2.转接来的客户的消息查看无法查看
 * */
@SuppressLint({ "NewApi", "ShowToast", "ResourceAsColor" })
public class ChatFragment extends Fragment implements OnGestureListener,
		OnTouchListener {
	private ImageView imageView;
	private SPhelper sPhelper = null;
	private List<GridView> array;
	private ViewPager viewPager;
	private MyPagerAdapter myPagerAdapter;
	private PullToRefreshListView chaListView;
	private ChatMsgAdapter chatAdapter;
	private Monitor tmpMonitor;
	private EditText chatEditText;
	private Button sendButton;
	private GridView gridView;
	private Date curDate = null;
	private Handler redirecHandler;
	private ChatSmileyParser smileyParser;
	private JSONArray jsonArray;
	private InsertAsync insertAsync;
	/**消息列表*/
	public List<ChatMessage> list = null;
	/**显示在界面的消息*/
	public List<ChatMessage> msgs = null;
	private Dialog mDialog;
	private Button backBtn;
	public Context context;
	public Activity activity;
	public View view;
	private String msgId = null;
	/**表示是否在聊天页面，false表示在，true表示不在*/
	private Boolean boolean1 = false;
	private HashMap<String, Object> userinfoHashMap = null;
	private Button kefuInfoButton;
	private List<HashMap<String, Object>> mData = null;
	public static Boolean bool3 = false;
	public JSONArray monitorUserJsonArray = null;
	public JSONArray monitorJsonArray = null;
	private GestureDetector mGestureDetector;
	View.OnTouchListener gestureListener;
	private TextView userNameTextView = null;
	// 标志当前聊天的用户cookie
	private String mobilecookiefromconsult;
	// 标志当前用户是否是第一次访问
	private boolean isFirstVisit = false;
	// 设置软件盘自动隐藏
	private InputMethodManager mInputMethodManager;
	// 底部导航栏父容器
	private LinearLayout tabLayout = null;
	/**设置消息记录的在客户的所有消息中的位置,从零开始安时间先后向前推*/
	private int position = 0;
	private String lastMsgId = "";
	private int start = 0;
	// 定义一个加载历史消息记录的标志位
	public static boolean isLoad = false;
	// 定义一个从数据库读取数据的标志位
	
	private boolean isFromDb = false;
	/**
	 会话初始时插入的消息数*/
	public static final  int MessageCount=4;
	// 定义一个返回事件的标志位
	private Boolean isFromOnLine = false;
	public static Boolean booleanBack = false;
	private ChatMessage chatMessageSend = null;
	public static Boolean boolean2 = false;
	// 设置发送消息的临时缓存
	private String unSendMsg = null;
	public static boolean isReadyCancel = false;
	private int length = 0;
	/**判断是否已经获取了最大的messageid的值 */ 
	private volatile boolean isMaxMessageIdGot = false;
	public static String isChatFragmentCookie = null;
	private View viewv;
	private ChatMessage showchatMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.mobilechat_chat, container, false);
		MainTabActivity.isChatFragment = true;
		Log.d("message", "test417 Messagechat onCreateView"+MainTabActivity.isChatFragment);
		isMaxMessageIdGot = false;
		MainTabActivity.isyemian = true;
		initView();
		// 初始化从activity跳转过来的数据
		initDataFromActivity();
		// 初始化fragment用到的数据
		initFragmentData();
		boolean1 = false;
		
		// 第一次在手机上的会话从网络上加载
		if(isFirstVisit){
			getHistoryListFromNet();
		}
		else{
			getHistoryListFromDB();
		}

		kefuInfoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean1 = true;
				kefuInfo(); 
			}
		});

		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 隐藏软键盘
				mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
						0);
				boolean1 = true;
				//backToActivity();
				Message mess = new Message();
				mess.what = 7;
				messageHandler.sendMessage(mess);
			}
		});
		chaListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// 隐藏软键盘
				mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
						0);
				return mGestureDetector.onTouchEvent(event);// mGestureDetector.onTouchEvent(event);
			}
		});
		// 下拉刷新加载历史消息记录，没有存数据库了
		/**
		 刷新不再请求网络；一致从数据库读本地数据*/
		chaListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				{
					getMessageFromDB();
					// 从数据库加载，可能不是最新的，要自动更新
//					if (isFromDb) {
//
//						System.out.println("onrefresh fromdb");
//						if (getMessageFromDB())
//							isFromDb = false;
//					}
					// 当数据库的数据已经被加载，开始进行网络的请求
//					else {
//						System.out.println("onrefresh fromnet");
//						isLoad = true;
						// try {
						//
//						if (!isMaxMessageIdGot) {
//							Message message = Message.obtain();
//							message.what = 10;
//							messageHandler.sendMessage(message);
//						} else {
//							System.out.print("ready to get netdata");
//							getHistoryListFromNet();
//						}
//					} 

				}
				chaListView.postDelayed(new Runnable() {

					@Override
					public void run() {
						chaListView.onRefreshComplete();
					}
				}, 1000);
			}
		});

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendMessageToServer(v);
			}
		});

		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPagerVisible(v);
			}
		});
		if (MainTabActivity.layout.getVisibility() == View.VISIBLE) {
			MainTabActivity.layout.setVisibility(View.GONE);
		}
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isReadyCancel = false; 
		// 说明当前ChatFragment是正在运行的
		//下面的菜单
		if (MainTabActivity.layout.getVisibility() == View.VISIBLE) {
			MainTabActivity.layout.setVisibility(View.GONE);
		}
		MainTabActivity.isChat = true;
		if (list.size() == 0) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					getHistoryListFromDB();

				}
			}, 150);
		}
		
		
		
		//百度统计
		StatService.onResume(this);
		
	}

	// oncreateView方法的分割
	// 客服信息按钮
	private void kefuInfo() {
		userNameTextView.setText("顾客资料");
		Bundle bundle = new Bundle();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_in1);
		bundle.putString("mobilecookie", mobilecookiefromconsult);
		SetDuiFragment newSetDuiFragment = new SetDuiFragment();
		newSetDuiFragment.setArguments(bundle);
		MainTabActivity.layout.setVisibility(View.GONE);
		ft.replace(android.R.id.tabcontent, newSetDuiFragment,
				Constant.CONSULT_FRAGMENT_TAG);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}

	// 发送消息
	private void sendMessageToServer(View v) {
		if (!("").equals(chatEditText.getText().toString().trim())
				&& (chatEditText.getText().toString() != null)) {
			
				curDate = new Date(System.currentTimeMillis());

				if (IsNetWorkAvailable.isAvailable(getActivity())) {
					// 2 false自己 3.发送的消息4.时间5.发个哪个用户
					chatMessageSend = new ChatMessage(R.drawable.badge1,
							"false", chatEditText.getText().toString(),
							System.currentTimeMillis() + "", msgId,
							MainTabActivity.monitor.userid,
							mobilecookiefromconsult,MainTabActivity.monitor.username);
					chatMessageSend.setUniquelocal(getUniqueuelocal());
					chatMessageSend.isSended = Constant.MESSAGE_IS_SENDING;
					// 更新消息
					chatAdapter.add(chatMessageSend);
					// 向上移动一格
					chaListView.setSelection(chaListView.getAdapter()
							.getCount());
					unSendMsg = chatEditText.getText().toString();
					
					chatEditText.setText("");
					
					showchatMessage = chatMessageSend;
					
					//发送数据
					try {
						sendMsg(showchatMessage);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// 隐藏软键盘
					mInputMethodManager.hideSoftInputFromWindow(
							v.getWindowToken(), 0);
					viewPager.setVisibility(View.GONE);
				} else {
					IsNetWorkAvailable.setNetWork(getActivity(), getActivity());
				}
		} else {
			
			Toast.makeText(getActivity(), "输入消息不能为空", 2000).show();
		}
	}

	// imageView
	private void viewPagerVisible(View v) {
		try {
			viewPager.setVisibility(viewPager.getVisibility() == View.GONE ? View.VISIBLE: View.GONE);
			// 隐藏软键盘
			mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
		} catch (Exception e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		}
	}

	// 显示表情的GridView
	private void initView() {
		backBtn = (Button) view.findViewById(R.id.chat_back);
		// 用于显示聊天用户的名称
		userNameTextView = (TextView) view.findViewById(R.id.chat_head_txt);
		// 对应底部左边的icon
		imageView = (ImageView) view.findViewById(R.id.chat_expression);
		viewPager = (ViewPager) view.findViewById(R.id.view_pager);

		chaListView = (PullToRefreshListView) view
				.findViewById(android.R.id.list);
		chaListView.setVisibility(View.VISIBLE);
		chatEditText = (EditText) view.findViewById(R.id.chat_edit);
		// 绑定EditText的监听器
		chatEditText.addTextChangedListener(watcher);

		sendButton = (Button) view.findViewById(R.id.editSend);
		sendButton.setEnabled(false);

		kefuInfoButton = (Button) view.findViewById(R.id.kefuinfo_button);
		array = new ArrayList<GridView>();

		gridView = new GridView(getActivity());
		GetChatExpression getExperssion = new GetChatExpression();

		try {
			ExpressionAdapter experssionAdapter = new ExpressionAdapter(
					getActivity(), getExperssion.getExpressions(), 0);
			gridView.setAdapter(experssionAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ChatSmileyParser.init(getActivity());
		smileyParser = ChatSmileyParser.getInstance();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 将图像添加到EditText里面
				chatEditText.append(smileyParser.smileyTexts[arg2]);
				// 当聊天信息满屏时 信息上一一格
				chaListView.setSelection(chaListView.getAdapter().getCount());
			}

		});
		gridView.setNumColumns(7);
		// 表情面板底色
		gridView.setBackgroundColor(Color.rgb(255, 255, 255));// rgb(213, 213,
																// 213)
		gridView.setHorizontalSpacing(9);
		gridView.setVerticalSpacing(3);
		gridView.setColumnWidth(30);
		gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridView.setGravity(Gravity.CENTER);
		// android.2.3不支持 gridView.setLeft(5);
		// gridView.setLeft(5);
		array.add(gridView);
	}

	// 初始化从activity跳转过来的数据
	private void initDataFromActivity() {

		context = getActivity();
		tmpMonitor = MainTabActivity.monitor;
		// imageViews = new ImageView[6];
		mobilecookiefromconsult = getArguments().getString("mobilecookie");
		isChatFragmentCookie = mobilecookiefromconsult;
	    isFirstVisit = getArguments().getBoolean("isFirstVisit");
		UserInfo userInfo = UserInfo.getNameId(mobilecookiefromconsult);
		//  已读消息清空
		ConsultMsg msConsultmsg = ConsultMsg.getConsutFromUserName(
				mobilecookiefromconsult, MainTabActivity.monitor.userid);
		if (mobilecookiefromconsult != null) {
			if (msConsultmsg != null) {
				msConsultmsg.Unreadcnt = 0;
				msConsultmsg.save();
			}
			// 更新fragment的信息
			for (int i = 0; i < MainTabActivity.listConsultmsgs.size(); i++) {
				ConsultMsg msg = MainTabActivity.listConsultmsgs.get(i);
				if (msg.mobilecookie.equals(mobilecookiefromconsult)) {
					msg.Unreadcnt = 0;
					if (msg.isOnline) {
						if(msg.province.equals("")){
							if(userInfo != null){
								userNameTextView.setText(msg.Username == null ? "顾客" : userInfo.province + userInfo.city + userInfo.isp +msg.Username.replace("顾客"," ").trim() + "(在线)");
							}
						}else{
							userNameTextView.setText(msg.Username == null ? "顾客" : msg.province + msg.city + msg.isp +msg.Username.replace("顾客"," ").trim() + "(在线)");
						}
						
					} else {
						if(msg.province.equals("")){
							if(userInfo != null){
								userNameTextView.setText(msg.Username == null ? "顾客" : userInfo.province + userInfo.city + userInfo.isp +msg.Username.replace("顾客"," ").trim() + "(离线)");
							}
						}else{
							userNameTextView.setText(msg.Username == null ? "顾客" : msg.province + msg.city + msg.isp +msg.Username.replace("顾客"," ").trim() + "(离线)");
						}
					}
				}
			}
		}

		// 初始化底部导航栏父容器
		tabLayout = (LinearLayout) MainTabActivity.layout;

		// 调用发送函数
		setMobilecookiefromconsult(mobilecookiefromconsult);
		// 键盘控制初始化
		mInputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		// 接口回调=============================todo
		MainTabActivity.mapNotification.clear();
		MainTabActivity.clearNotificationAll();
		MainTabActivity.isHaveSave = false;
		if (MainTabActivity.toconsultlist != null) {
			MainTabActivity.toconsultlist = null;
		}
		// 
		MainTabActivity.flag = false;
	}

	// 初始化fragment用到的数据
	private void initFragmentData() {
		list = new Vector<ChatMessage>();
		msgs = new LinkedList<ChatMessage>();

		if (mGestureDetector == null) {
			mGestureDetector = new GestureDetector((OnGestureListener) this);
			mGestureDetector.setIsLongpressEnabled(true);
		}
		redirecHandler = new Handler();
		myPagerAdapter = new MyPagerAdapter(getActivity(), array);
		viewPager.setAdapter(myPagerAdapter);
	}

	// 监听EditText输入框数据到变化
	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			try {
				// 向服务器发送一个正在输入的函数
				inputting();
				sendButton.setEnabled(true);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	public Handler pollHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				
				break;
			case 2:
				
				Intent poll_service_intent = new Intent(context, MessageService.class);
				poll_service_intent.setAction(MessageService.ACTION);
				getActivity().stopService(poll_service_intent);
				MessageService.isCancel = true;
				MainTabActivity.multipleLoginLock=true;
				
				new AlertDialog.Builder(context).setCancelable(false)
						.setTitle("下线通知")
						.setIcon(R.drawable.g_ic_failed)
						.setMessage("您的帐号已在其他地方登陆,请重新登录")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method
										// stub
										Intent it = new Intent();
										it.setClass(context,
												LoginActivity.class);
										startActivity(it);
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
								})
						.setNegativeButton("重新登录",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										Intent it = new Intent(
												getActivity(),
												LoginActivity.class);
										startActivity(it);
										getActivity().finish();
										MainTabActivity.multipleLoginLock=false;

									}
								}).create().show();
				
				// 记得这里要注销服务
				// XXX: 停止 poll service 服务，同时设置服务停止的标识
				
				
				break;
			default:
				break;
			}
		}

	};
//	private void zhuxiao(){
//		//PollingUtils.stopPollingService(getActivity(),
//		//		MessageService.class, MessageService.ACTION);
//	}
	// 返回登录界面
	public boolean exitlogi() throws JSONException,
			UnsupportedEncodingException {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitid", tmpMonitor.unitid);
		paramMap.put("usid", tmpMonitor.userid);
		paramMap.put("uscookie", tmpMonitor.uscookie);
		RequestParams params = new RequestParams(paramMap);

		MobileChatClient.get("userver/offline", params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject timeline) {
						try {
							String flag = (String) timeline
									.getString("success");
							Activity getedactivity =getActivity();
							if (flag.equals("true")) {
								
								LoginActivity.isexitCount = 1;
								Intent intent = new Intent();
								
								if (getedactivity != null) {
									intent.setClass(getedactivity,
											LoginActivity.class);
//									startActivity(intent);
									getedactivity.finish();
								}
							} else {
								LoginActivity.isexitCount = 1;
								Intent intent = new Intent();
							
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

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		
		ConsultMsg consult = ConsultMsg.getConsutFromUserName(
				mobilecookiefromconsult, MainTabActivity.monitor.userid);
		consult.Unreadcnt = 0;
		consult.save();
		for (ConsultMsg msg1 : MainTabActivity.listConsultmsgs) {
			if (msg1.mobilecookie.equals(mobilecookiefromconsult)) {
				msg1.Unreadcnt = 0;
			}
		}
		MainTabActivity.isChat = false;
		// 接口回调=============================todo
		MainTabActivity.mapNotification.clear();
		MainTabActivity.clearNotificationAll();
		MainTabActivity.isHaveSave = false;
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//System.out.println("onDestroy===========================");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		//System.out.println("onDetach=====================");
		super.onDetach();
		// 标志当前ChatFragment不在运行状态
		MainTabActivity.isChatFragment = false;
	}

	/** 
	 * 获取position的最大值，便于查询
	 */
	public void getHttpMsgsPositionMax() throws JSONException,
			UnsupportedEncodingException {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		sPhelper = new SPhelper(getActivity());
		paramMap.put("unitid", tmpMonitor.unitid);
		paramMap.put("usid", tmpMonitor.userid);
		paramMap.put("uscookie", tmpMonitor.uscookie);
		paramMap.put("mobile", mobilecookiefromconsult);
       
		paramMap.put("length", "1");
		RequestParams params = new RequestParams(paramMap);
		if (!isReadyCancel) {
			MobileChatClient.get(getActivity(), "userver/getMsgs", params,
					new JsonHttpResponseHandler() {
						@Override
						protected void sendFailureMessage(Throwable arg0,
								byte[] arg1) {
							// TODO Auto-generated method stub
							super.sendFailureMessage(arg0, arg1);
						}

						@Override
						public void onSuccess(JSONObject jsonObject) {
							try {
								
								// 获取最后一条消息的位置 
								position = jsonObject.getInt("position");
								// 获取最后一条消息的id
								lastMsgId = jsonObject.getString("_id");

								System.out.println("position " + position
										+ ",lastMsgId" + lastMsgId);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							// 机制
							/*
							 * 1.最后一条信息的消息id相等，则按照思路加载数据，否则，直接从网络获取
							 */
							{
								if (ChatMessage.getAllChatByCurrentCookie(
										tmpMonitor.uscookie, tmpMonitor.userid)
										.size() > 0
										&& position > 0
										&& lastMsgId != null
										&& lastMsgId.equals(ChatMessage
												.getLastMessageId(
														tmpMonitor.uscookie,
														tmpMonitor.userid))) {
									System.out.println("get data from db");
									isFromDb = true;
									messageHandler.sendEmptyMessage(6);
								} else {
									System.out.println("get data from net");
									isFromDb = false;
									messageHandler.sendEmptyMessage(5);
								}
							}
							{
							}
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
							return;
						}

						@Override
						protected void sendFailureMessage(Throwable arg0,
								String arg1) {
							// TODO Auto-generated method stub
							super.sendFailureMessage(arg0, arg1);
							return;
						}

					});
		}
	}

	/**
	 * 
	 * @方法名  inputting 
	 * @包名  com.example.mobiletest 
	 * @作者  ChunTiaan 
	 * @时间  2014年1月15日 下午4:36:51
	 * @Email ChunTian1314@vip.qq.com
	 * @版本  V1.0
	 * @功能: TODO(向服务器发个正在输入函数) 
	 * @param @throws JSONException
	 * @param @throws UnsupportedEncodingException    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void inputting() throws JSONException, UnsupportedEncodingException {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		sPhelper = new SPhelper(getActivity());
		paramMap.put("unitid", tmpMonitor.unitid);
		paramMap.put("usid", tmpMonitor.userid);
		paramMap.put("uscookie", tmpMonitor.uscookie);
		paramMap.put("mobile", mobilecookiefromconsult);
		RequestParams params = new RequestParams(paramMap);
		MobileChatClient.get(getActivity(), "userver/inputting", params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject arg0) {
						// TODO Auto-generated method stub
						try {
							if ((arg0.getString("success")).equals("true")) {

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(arg0);
					}
				});

	}

	private String getUniqueuelocal() {
		return "888888" + System.currentTimeMillis()
				+ Math.round(Math.random() * 89999 + 10000);
	}

	// 发送消息
	public void sendMsg(final ChatMessage chatMessage) throws JSONException,
			UnsupportedEncodingException {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		sPhelper = new SPhelper(getActivity());
		paramMap.put("unitid", tmpMonitor.unitid);
		paramMap.put("usid", tmpMonitor.userid);
		paramMap.put("uscookie", tmpMonitor.uscookie);

		if (chatMessage != null) {
			paramMap.put("content", chatMessage.content);
			unSendMsg = chatMessage.content;
			mobilecookiefromconsult = chatMessage.toId;
			paramMap.put("mobile", mobilecookiefromconsult);
			chatAdapter.freshAdded(chatMessage, Constant.MESSAGE_IS_SENDING);
			saveMessages(chatMessage);
		}
		RequestParams params = new RequestParams(paramMap);
		MobileChatClient.post1(getActivity(), "userver/sendMsg", params,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject arg0) {
						// TODO Auto-generated method stub
						try {
							if ((arg0.getString("success")).equals("true")) {
								chatMessage.mId = arg0.getString("msgid");
								chatMessage.isSended = Constant.MESSAGE_IS_SENDED;
								chatAdapter.freshAdded(chatMessage,
										Constant.MESSAGE_IS_SENDED);
								saveMessages(chatMessage);
							} else if (arg0.getInt("code") == 104) {
								Message mess = new Message();
								mess.what = 2;
								pollHandler.sendMessage(mess);
							} else {
								if (chatMessage != null) {
									chatMessage.isSended = Constant.MESSAGE_IS_FAILED;
									chatAdapter.freshAdded(chatMessage,
											Constant.MESSAGE_IS_FAILED);
									saveMessages(chatMessageSend);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (chatMessage != null) {
								chatMessage.isSended = Constant.MESSAGE_IS_FAILED;
								chatAdapter.freshAdded(chatMessage,
										Constant.MESSAGE_IS_FAILED);
								saveMessages(chatMessageSend);
							}
						}
					}
				});

	}

	// 不论消息是否发送成功，都会保存消息，只是多了一个状态
	private void saveMessages(ChatMessage chatMessage) {
		/*
		 * 保存消息,当数据量大于50的时候要进行相应的处理 机制删除就是一个队列的操作,check before save
		 */
		// 保存消息，肯定是唯一的
		/*
		 * 1.发送的消息唯一的标志，本地的msgid 2.发送消息的状态更新 3.从服务器端获取消息，进行数据库中消息状态的更新
		 */
		// 1.发送的消息唯一的标志，本地的msgid
		ChatMessage.saveLvmsgLocal(chatMessage, mobilecookiefromconsult,
				tmpMonitor.userid);

		ConsultMsg consult = ConsultMsg.getConsutFromUserName(
				mobilecookiefromconsult, MainTabActivity.monitor.userid);
		consult.Unreadcnt = 0;
		consult.save();
//		isLoad = false;
		ConsultMsg consultmsgTmp = ConsultMsg.getConsutFromUserName(
				mobilecookiefromconsult, MainTabActivity.monitor.userid);
		consultmsgTmp.latestmsg = unSendMsg;// chatEditText.getText().toString();
		consultmsgTmp.save();

		chaListView.setSelection(chaListView.getAdapter().getCount());
		// 置灰
		sendButton.setEnabled(false);
	}

	// 异步插入数据库
	public class InsertAsync extends
			AsyncTask<List<ChatMessage>, Integer, String> {

		@Override
		protected String doInBackground(List<ChatMessage>... params) {
			for (ChatMessage test : params[0]) {
				ChatMessage.saveLvmsg(test, tmpMonitor.uscookie,
						tmpMonitor.userid);
			}
			return null;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	// 通过Activity动态更新消息列表
	// 定义public方法，activity调用
	public boolean updateChatList(ChatMessage msg) {
		boolean flag = false;
		
		if (msg != null) {
			Message message = Message.obtain();
			message.obj = msg;
			message.what = 1;
			messageHandler.sendMessage(message);
			flag = true;
		}
		return flag;
	}

	// 定义并实现新消息到达时ui的更新
	private Handler messageHandler = new Handler() {
//		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				List<ChatMessage> ChatMessagelist= (List<ChatMessage>) msg.obj;
				chatAdapter.addMany(ChatMessagelist);
				// 获得前聊天的用户cookie的所有数据
				ConsultMsg consultmsgTemp = ConsultMsg
						.getConsutFromUserName(mobilecookiefromconsult,
								MainTabActivity.monitor.userid);
				// 获得聊天信息的最后一个字段
				consultmsgTemp.latestmsg = (ChatMessagelist.get(ChatMessagelist.size()-1)).content;
				// 更新数据库数据
				consultmsgTemp.save();
				// 当聊天信息满屏时 信息上一一格
				chaListView.setSelection(chaListView.getAdapter().getCount());
				
				break ;
			case 1:
			// 来到消息之后做的处理
			{
				
				// 在消息界面添加消息
				chatAdapter.add((ChatMessage) msg.obj);
				// 获得前聊天的用户cookie的所有数据
				ConsultMsg consultmsgTmp = ConsultMsg
						.getConsutFromUserName(mobilecookiefromconsult,
								MainTabActivity.monitor.userid);
				// 获得聊天信息的最后一个字段
				consultmsgTmp.latestmsg = ((ChatMessage) msg.obj).content;
				// 更新数据库数据
				consultmsgTmp.save();
				// 当聊天信息满屏时 信息上一一格
				chaListView.setSelection(chaListView.getAdapter().getCount());
				Log.d("message", "test417 Messagechat messageHandler"+ChatFragment.this.isVisible());
			}
				break;
			case 2:
				Toast.makeText(getActivity(), "数据加载完毕!", Toast.LENGTH_SHORT)
						.show();
				break;
				//
			case 3:
				chatAdapter = new ChatMsgAdapter(getActivity(),
				R.layout.listitem_chat_left, getFragmentManager());
		chaListView.setAdapter(chatAdapter);
		Vector<ChatMessage>testlistChatMessages=(Vector<ChatMessage>)msg.obj;
		chatAdapter.addMany(testlistChatMessages);
//		chaListView.setSelection(chaListView.getAdapter().getCount());
		chaListView.setLongClickable(true);

		break;
				//
			case 4:
				chatEditText.setText("");
				break;
				//从网络请求返回的最近记录的ID大于本地存储的最大ID，说明服务器上有新纪录。--以前的实现
				//不再从网络获取数据；只有在第一次打开会话而且没有记录的时候才从网络获取数据--彭程140313
			case 5: {
//				isMaxMessageIdGot = true;
//				getHistoryListFromNet();
			}
				break;
			case 6: {
//				isMaxMessageIdGot = true;
//				isFromDb = !getHistoryListFromDB();
			}
				break;
			case 7:
				
				boolean2 = true;
				MainTabActivity.isChatFragment = false;
				MobileChatClient.cancelTask(getActivity());
				
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.setCustomAnimations(R.anim.push_left_out, R.anim.push_left_zhong);
				ConsultFragment newconsultFragment = new ConsultFragment();
				ft.replace(android.R.id.tabcontent, newconsultFragment,
						Constant.CONSULT_FRAGMENT_TAG);
				ft.addToBackStack(null);
				ft.commit();
				
				break;
			case 8:
				break;
			case 10:

//				try {
//					getHttpMsgsPositionMax();
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				break;
			default:
				break;
			}
		}
	};

	// 发送数据到activity
	private void setMobilecookiefromconsult(String cookie) {
		((MainTabActivity) getActivity()).setMobilecookiefromconsult(cookie);
	}

	// 获取客户资料
	public void getuserinfo() throws JSONException,
			UnsupportedEncodingException {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		sPhelper = new SPhelper(getActivity());
		paramMap.put("unitid", tmpMonitor.unitid);
		paramMap.put("usid", tmpMonitor.userid);
		paramMap.put("uscookie", tmpMonitor.uscookie);
		paramMap.put("mobile", mobilecookiefromconsult);
		RequestParams params = new RequestParams(paramMap);
		MobileChatClient.get(getActivity(), "userver/getMobiledata", params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						super.onFailure(arg0);
						Message msg = new Message();
						msg.what = 0;
						redirecHandler.sendMessage(msg);
					}

					@Override
					protected Object parseResponse(String arg0)
							throws JSONException {
						// TODO Auto-generated method stub
						JSONObject jsonObject = new JSONObject(arg0);
						String flag = (String) jsonObject.getString("success");
						if (flag.equals("true")) {
							JSONObject dataJsonObject = jsonObject
									.getJSONObject("data");
							
							userinfoHashMap = new HashMap<String, Object>();
							if (!dataJsonObject.getString("username")
									.equals("")) {
								userinfoHashMap.put("username",
										dataJsonObject.getString("username"));
							} else {
								userinfoHashMap.put("username", "未指定");
							}
							// "appoinment":"false",
							if (!dataJsonObject.getString("appoinment").equals(
									"")) {
								userinfoHashMap.put("appoinment",
										dataJsonObject.getString("appoinment"));
							} else {
								userinfoHashMap.put("username", "false");
							}
							// userinfoHashMap.put("address",
							// dataJsonObject.getString("address"));
							if (!dataJsonObject.getString("tel").equals("")) {
								userinfoHashMap.put("tel",
										dataJsonObject.getString("tel"));
							} else {
								userinfoHashMap.put("tel", "未指定");
							}
							if (!dataJsonObject.getString("IM").equals("")) {
								userinfoHashMap.put("QQ",
										dataJsonObject.getString("IM"));
							} else {
								userinfoHashMap.put("QQ", "未指定");
							}
							if (dataJsonObject.getString("appoinment").equals(
									"true")) {
								userinfoHashMap.put("appoinment", true);
							} else {
								userinfoHashMap.put("appoinment", false);
							}
							if (dataJsonObject.getString("blacklist").equals(
									"true")) {
								userinfoHashMap.put("blacklist", true);
							} else {
								userinfoHashMap.put("blacklist", false);
							}
							
							Message msg = new Message();
							msg.what = 1;
							redirecHandler.sendMessage(msg);
						} else {
							
						}
						return super.parseResponse(arg0);

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						try {
							System.out.println(arg0.getString("sucess")
									+ "...............mobiledata put sucess ");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable arg0, JSONObject arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
						System.out.println("jsonobject failure aaa");
					}

				});

	}

	// 从数据库里面读取聊天记录(读取聊天记录的方式是先读数据库，读完了直接从网络读取所有记录覆盖本地)
	// 从数据库获取数据
	/**
	 * 机制修改，返回表示DB是否为空
	 */
	private boolean getHistoryListFromDB() {
		boolean isEmpty = false;
		list.clear();
		msgs.clear();
		List<ChatMessage> tmpList = ChatMessage.getAllChatByCurrentCookie(
				mobilecookiefromconsult);
		for (int i = 0; i < tmpList.size(); i++) {
			ChatMessage msg = tmpList.get(i);
			list.add(msg);
		}
		//取最近的20条记录
		if (list.size() >= Constant.STEP_LOAD_FROM_DB) {
			msgs.addAll(list.subList(list.size() - Constant.STEP_LOAD_FROM_DB, list.size()));
		} 
		else
		{
			msgs.addAll(list);
		}
		//是否完全加载聊天记录
//		isFinished = (list.size() == msgs.size() ? true : false);
		//数据库里面是否
		isEmpty= (list.size()==MessageCount? true:false);
		
		chatAdapter = new ChatMsgAdapter(getActivity(),
				R.layout.listitem_chat_left, getFragmentManager());
		chaListView.setAdapter(chatAdapter);
		chatAdapter.addMany(msgs);
		chaListView.setSelection(chaListView.getAdapter().getCount());
		chaListView.setLongClickable(true);
		if (mDialog != null)
			mDialog.dismiss();
		return isEmpty;
	}

	/**根据传递的msg进行数据的查询  返回是否读完的标志*/ 
	private boolean getMessageFromDB() {
		boolean ifFinished = false;
		//
		int step = ((list.size() - msgs.size()) > Constant.STEP_LOAD_FROM_DB) ? Constant.STEP_LOAD_FROM_DB
				: (list.size() - msgs.size());
		//
		List<ChatMessage> tmp = list.subList(list.size() - msgs.size() - step,
				list.size() - msgs.size());
		chatAdapter.addMany(tmp);
		msgs.addAll(tmp);

		// 当msgs 的长度和list的长度一致时说明，数据取完，开始下滑从网络获取数据
		ifFinished = (list.size() == msgs.size() ? true : false);
		return ifFinished;
	}

	/*
	 * 一旦需要从网络获取数据那么直接获取服务器上所有的聊天记录并更新本地
	 */
	private boolean getHistoryListFromNet() {
		
		boolean flag = false;

//		position = position - list.size();
//	   if (position >= Constant.STEP_LOAD_FROM_NETWORK - 1) {
//			start = position - Constant.STEP_LOAD_FROM_NETWORK + 1;
//			length = Constant.STEP_LOAD_FROM_NETWORK;
//			position = ((start - 1) < 0 ? 0 : start - 1);
//		} else {
//			start = 0;
//			length = position - start;
//			flag = true;
//			position = 0;
//		}
	
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitid", tmpMonitor.unitid);
		paramMap.put("usid", tmpMonitor.userid);
		paramMap.put("uscookie", tmpMonitor.uscookie);
		paramMap.put("mobile", mobilecookiefromconsult);
        //取所有，length曲最大值
		paramMap.put("length", String.valueOf(Integer.MAX_VALUE));
		
		RequestParams params = new RequestParams(paramMap);
		MobileChatClient.get(getActivity(), "userver/getMsgs", params,
				new JsonHttpResponseHandler() {
					@Override
					protected void sendFailureMessage(Throwable arg0,
							byte[] arg1) {
						// TODO Auto-generated method stub
						super.sendFailureMessage(arg0, arg1);
					}

					@Override
					public void onSuccess(JSONObject jsonObject) {
						try {
							ArrayList<ChatMessage> tmpList=new ArrayList<ChatMessage>();
							JSONArray jArray = jsonObject.getJSONArray("msgs");
							
							//判断消息显示在左边还是右边；客户的消息在左边；客服的消息在右边；
							String messageIsLeftString="";
							for(int i=0;i<jArray.length();i++)
							{
								//为什么会产生这么多null?
								if(jArray.getJSONObject(i)!=null)
								{
									//
									messageIsLeftString=jArray.getJSONObject(i).getString("type").equals("mb_normal")?"true":"false";
									
									//
								tmpList.add(new ChatMessage(0,messageIsLeftString,jArray.getJSONObject(i).getString("content"),
										jArray.getJSONObject(i).getString("createdTime"),jArray.getJSONObject(i).getString("_id"),
										jArray.getJSONObject(i).getString("fromId"),jArray.getJSONObject(i).getString("fromName"),
										jArray.getJSONObject(i).getString("type"),jArray.getJSONObject(i).getString("toId")));
								}
							}
							list.clear();
							//填充欢迎消息中缺失的FormID 和toid,服务器传来的欢迎消息Fromid是企业ID，toid为空;
							for (int i = 0; i < tmpList.size(); i++) {
								ChatMessage msg = tmpList.get(i);
							    if(msg.type.equals("unit_welcome")){
							    	msg.fromId=tmpList.get(tmpList.size()-1).fromId;
							    	msg.toId=tmpList.get(tmpList.size()-1).toId;
									}
								list.add(msg); 
							}
							
							msgs.clear();
							try{
//								Log.i("ready to deleted ");
							ChatMessage.ClearByCookie(mobilecookiefromconsult, tmpMonitor.userid);
							Log.i("deleted successfully");
							
							}
							catch(Exception e)
							{
//								int hh=0;
							}
							try {
								ChatMessage.saveChatMessage(list);
								System.out.print("saved successfully");
							} catch (Exception e) {
								// TODO: handle exception
//								int hh=1;
							}
							
							//取最近的20条记录
							if (list.size() >= Constant.Single_STEP_LOAD) {
								msgs.addAll(list.subList(list.size() - Constant.Single_STEP_LOAD, list.size()));
							} 
							else
							{
								msgs.addAll(list);
							}
							Message msg=Message.obtain();
							msg.obj=list;
							msg.what=3;
							// 获取最后一条消息的位置
//							position = jsonObject.getInt("position");
//							// 获取最后一条消息的id
//							lastMsgId = jsonObject.getString("_id");
							isFirstVisit=false;
							messageHandler.sendMessage(msg);
							Log.i("messaged successfully");
							ConsultMsg.setFirstVisitFalseByMobilecookie(mobilecookiefromconsult, tmpMonitor.userid);
//							chatAdapter = new ChatMsgAdapter(getActivity(),
//									R.layout.listitem_chat_left, getFragmentManager());
//							chaListView.setAdapter(chatAdapter);
//							chatAdapter.addMany(msgs);
//							chaListView.setSelection(chaListView.getAdapter().getCount());
//							chaListView.setLongClickable(true);


//
//							System.out.println("position " + position
//									+ ",lastMsgId" + lastMsgId);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
		}
		);
		//
		return flag;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MainTabActivity.isChat = true;
	}

//	// 跳转到以前的activity
//	private void backToActivity() {
//		ThreadGroup.uncaughtException
//	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	private byte[] lock = new byte[0];

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("sa", "chatFrament===>boolean1" + boolean1);
		if (!boolean1) {
//			MainTabActivity.isChatFragment = false;
			MobileChatClient.cancelTask(getActivity());
			((MainTabActivity) getActivity()).show(0);
			//不显示
			tabLayout.setVisibility(View.GONE);
		}
		
		//百度统计
		StatService.onPause(this);
	}
	
	
	
	
	
	
}
