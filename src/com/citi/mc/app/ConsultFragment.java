package com.citi.mc.app;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.citi.mc.R;
import com.citi.mc.adapter.ConsultAdapter;
import com.citi.mc.badge.BadgeButton1;
import com.citi.mc.db.ConsultMsg;
import com.citi.mc.utils.Constant;
import com.citi.mc.utils.MobileChatClient;
import com.citi.mc.utils.IsNetWorkAvailable;
import com.citi.mc.utils.PullToRefreshListView;
import com.citi.mc.utils.Timeparser;
import com.citi.mc.service.MessageService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 客户列表的Fragment
 * refactor 
 * author:lianma 
 * time :2013.11.7
 * 
 * version:1.1
 * changelog:增加注释
 * 机制是正确的
 * 1.初始化的重构问题
 * 2.列表显示的规则的改变
 * 3.类似LeavMsgFragment的处理机制
 * */
@SuppressLint({ "NewApi", "ValidFragment", "ShowToast" })
public class ConsultFragment extends ListFragment implements OnScrollListener,
		OnItemLongClickListener, OnGestureListener {
	public ConsultAdapter consultAdapter = null;
	public static ListView consultListView;
	public static LinkedList<ConsultMsg> msgs = null;
	private int begin = 6;
	private int tmp = 6;
	private int visibleItemCount;
	private Button footerButton;
	private PullToRefreshListView listView;
	private int lastItem;
	private Button footBtnButton;
	private LinearLayout layout;
	private RelativeLayout consult_res;
	private LinearLayout headerProgressBarLayout;
	private int scrollState;
	private Activity activity;
	// private UserInsertAsync userInsertAsync = null;
	private int msgId = 0;
	private Context content;
	// 获取手机屏幕分辨率的类
	private DisplayMetrics dm;
	private String type;
	private static int j = 0;
	private String mobileCookie;
	private int firstItem;
	private SPhelper sPhelper = null;
	private View view = null;
	private Timeparser timeparser = null;
	//boolean1:表示列表处于待选择状态（长按列表项后出现）
	private Boolean fBoolean = false, bool = false, boolean1 = false;
	private ProgressBar progressBar = null;
	private int verticalMinDistance = 20;
	private int minVelocity = 0;
	private GestureDetector mGestureDetector;
	private Boolean onOrOff = false;
	// 记录未读消息
	public static int Unread;
	// 获得屏幕宽度
	public static int screenWidth = 0;
	// Vibrator实现手机震动效果
	private Vibrator vibrator;
	private ConsultMsg consultmsgTmp = null;
	// 存放离线消息
	private LinkedList<ConsultMsg> listOffline;
	// 监听数据变化模块
	// 数据库表
	private ConsultMsg tmpConsultmsg = null;
	// 标记listView是否按下
	private static Boolean Mark = false;
	// 网络状态的存在
	public static TextView networkTextView, consult_txthead;
	
	private ProgressBar progressBar1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.consult, container, false);
		String myTag = getTag();
		MainTabActivity.flag = true;
		MainTabActivity.isHaveNewMsg = false;
		MainTabActivity.layout.setVisibility(View.VISIBLE);
		MessageService.isConsultFragment = true;
		consult_txthead = (TextView)view.findViewById(R.id.consult_txthead);
		((MainTabActivity) getActivity()).setConsultFragmentTag(myTag);
		MainTabActivity.isyemian = false;
		consult_res = (RelativeLayout)view.findViewById(R.id.consult_res);
		consult_res.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConsultAdapter.bool = false;
				consultAdapter.notifyDataSetChanged();
				boolean1 = false;
			}
		});
		
		//这种方式会导致UI更加卡顿速度很慢
//		Message mess = new Message();
//		mess.what =10;
//		messageHandler.sendMessage(mess);
		//
		
		// 存放从网络获取的所有信息
		networkTextView = (TextView) view.findViewById(R.id.networkstate);
		networkTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(
						android.provider.Settings.ACTION_SETTINGS));
			}
		});
		msgs = new LinkedList<ConsultMsg>();

		// 实例化Unread数据
		Unread = (MainTabActivity.listConsultmsgs == null) ? 0
				: MainTabActivity.listConsultmsgs.size();

		// 时间判断器
		timeparser = new Timeparser();
		// progressBar = (ProgressBar)view.findViewById(R.id.get_more_info);
		headerProgressBarLayout = (LinearLayout) view
				.findViewById(R.id.get_more_info);
		// footerProgressBarLayout.setVisibility(View.GONE);
		if (!IsNetWorkAvailable.isAvailable(activity)) {
			headerProgressBarLayout.setVisibility(View.GONE);
		}
		try {
			addView(view);
		} catch (Exception e) {
			e.printStackTrace();
		}
		consult_txthead = (TextView) view.findViewById(R.id.consult_txthead);
		// 对话数字的展示
		System.out.println("count message  " + unreadMessage());
		showButtonWithText(R.id.radio1, unreadMessage());
		Message message = new Message();
		message.what = 9;
		messageHandler.sendMessage(message);
		
		//
		if(MainTabActivity.listConsultmsgs.size() == consultListView.getCount()){
		consultListView.setVisibility(View.VISIBLE);
		progressBar1.setVisibility(View.GONE);
	}else{  
		consultListView.setVisibility(View.GONE); 
		progressBar1.setVisibility(View.VISIBLE);
	}

//		System.out.println("show(1)到CreateView程序运行时间： "+(System.currentTimeMillis()-MainTabActivity.testtime)+"ms");
		//
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = getActivity();
		content = getActivity();
		vibrator = (Vibrator) content
				.getSystemService(Service.VIBRATOR_SERVICE);
//		System.out.println("show(1)onCreate()程序运行时间： "+(System.currentTimeMillis()-MainTabActivity.testtime)+"ms");

	}


	private void addView(View view) throws Exception {

		consultListView = (ListView) view.findViewById(android.R.id.list);
		progressBar1 = (ProgressBar)view.findViewById(R.id.progressBar1);
		
		// 根据list是否为空
		if (MainTabActivity.listConsultmsgs == null
				|| MainTabActivity.listConsultmsgs.size() == 0) {
			{
				// 从数据库中取到数据
				getHistoryListFromDB();
			}
		} else if (MainTabActivity.listConsultmsgs.size() > 0) {
			
			//?
			
		}
		// 获取在线用户
		initConsultAdapter();
		consultListView.setOnItemLongClickListener(this);
		mGestureDetector = new GestureDetector((OnGestureListener) this);

		headerProgressBarLayout.setVisibility(View.GONE);
//		System.out.println("onCreate()程序运行时间： "+(System.currentTimeMillis()-MainTabActivity.testtime)+"ms");
	}


	// 会话界面满屏时 移动获取下面数据
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		this.scrollState = scrollState;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MainTabActivity.isConsultList = false;
//		System.out.println("onDestroyView()程序运行时间");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		// 告诉Activity当前fragment不在运行
		MessageService.isConsultFragment = false;
	}

	@Override
	public void onPause() {
		super.onPause();

		// 百度
		StatService.onPause(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		// 告诉Activity当前fragment正在运行
		MainTabActivity.isConsultList = true;
		initConsultAdapter();
		if (IsNetWorkAvailable.isAvailable(activity)) {
			if (networkTextView != null) {
				networkTextView.setVisibility(View.GONE);
			}
		}
		try {
			getUserName();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("1onResume()程序运行时间： "+(System.currentTimeMillis()-MainTabActivity.testtime)+"ms");
		//
		StatService.onResume(this);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
//		System.out.println("onDestroyView()程序运行时间");
	}

	private Handler messageHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			boolean flag = (msg.obj != null) ? true : false;
			if (msg.what == 1 && flag) {
			}

			if (msg.what == 2 && flag) {

			}
			if (msg.what == 3 && flag) {
			}
			if (msg.what == 4) {
			}

			if (msg.what == 5 && flag) {

			}
			// 处理新上线用户的消息，并根据设置进行在线提示音的展示
			else if (msg.what == 6 && flag) {
				dealOnlineNotification(msg);
			// 请求超时的处理
			} else if (msg.what == 7 && flag) {
				headerProgressBarLayout.setVisibility(View.GONE);
				Activity activity = getActivity();
				if (activity != null) {
					Toast.makeText(activity, "请求超时，请重新加载", Toast.LENGTH_LONG)
							.show();
				}
			}
			// 计时器发来的信号，定时下线
			
			//新上线客户
			else if (msg.what == 8) {
				for (int i = 0; i < MainTabActivity.listConsultmsgs.size(); i++) {
//					System.out.println(MainTabActivity.listConsultmsgs.get(i));
				}
				MainTabActivity.isfinished = true;
				consultAdapter.freshData(MainTabActivity.listConsultmsgs);
				consultListView.setAdapter(consultAdapter);
				consultAdapter.notifyDataSetChanged();
				int count = unreadMessage();
				if (count > 0) {
					showButtonWithText(R.id.radio1, count);
				}
				Log.d("message", "test423 MessageConsult onMessageArriveUpdateConsultFragment3 "+count);
				//MessageService.isCancel = false;
			} else if (msg.what == 9) {
				try {
					getUserName();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}else if (msg.what == 10) {
				
			
				if(MainTabActivity.listConsultmsgs.size() == consultListView.getCount()){
					
					consultListView.setVisibility(View.VISIBLE);
					progressBar1.setVisibility(View.GONE);
					
				}else{
					
					consultListView.setVisibility(View.GONE); 
					progressBar1.setVisibility(View.VISIBLE);
					
				}
//				System.out.println("msg10()程序运行时间： "+(System.currentTimeMillis()-MainTabActivity.testtime)+"ms");
				
			} 
		}
	};

	/**
	 * @return
	 */
	private int unreadMessage() {
		// TODO Auto-generated method stub
		int unreadCount = 0;
		for (ConsultMsg msg : MainTabActivity.listConsultmsgs) {
			unreadCount += msg.Unreadcnt;
		}
		return unreadCount;
	}

	// 处理客户上线online
	private void dealOnlineNotification(Message msg) {
		consultAdapter.notifyDataSetChanged();

	}

	// when the network is ok
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (IsNetWorkAvailable.isAvailable(activity)) {
			System.out.println("onstart()================");
			if (networkTextView != null) {
				networkTextView.setVisibility(View.GONE);
				MainTabActivity.rb1.performClick();

			}
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1 == null || e2 == null) {
			return false;
		}
		// TODO Auto-generated method stub
		if (e1.getY() - e2.getY() > verticalMinDistance
				&& Math.abs(velocityY) > minVelocity) {

			onOrOff = true;
			return true;

		} else if (e2.getY() - e1.getY() > verticalMinDistance
				&& Math.abs(velocityY) > minVelocity) {

			// 切换Activity
			onOrOff = false;
			return true;

		}
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

	// 当fragment绑定到activity时做的操作，数据的获取与展示
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	// 定义public方法，activity调用
	public boolean updateConsultList(ConsultMsg msg, int type) {

		boolean flag = false;
		Message message = Message.obtain();
		message.obj = msg;
		message.what = type;
		messageHandler.sendMessage(message);
		flag = true;
		return flag;

	}

	// 从数据库获取数据
	private void getHistoryListFromDB() {

		MainTabActivity.listConsultmsgs.clear();
		ArrayList<ConsultMsg> listRevert = (ArrayList<ConsultMsg>) ConsultMsg
				.getAllbyMonitor(MainTabActivity.monitor.userid);
		listOffline = new LinkedList<ConsultMsg>();
		// 在线和离线分开存储
		for (ConsultMsg msg : listRevert) {
			// 在线
			if (msg.isOnline == true) {
				if (!MainTabActivity.isInConsultList(msg))
					MainTabActivity.listConsultmsgs.add(msg);
			} else {
				// 离线
				listOffline.add(msg);
			}
		}
		
		// updateListOffline();
		MainTabActivity.listConsultmsgs.addAll(listOffline);
	}

	// 初始化consultadapter
	private void initConsultAdapter() {
		System.out.println("initConsultAdapter");
		FragmentManager fragmentManager = getFragmentManager();
		consultAdapter = new ConsultAdapter(activity,
				MainTabActivity.listConsultmsgs, fragmentManager, false);
		consultListView.setAdapter(consultAdapter);
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		// 判断是否出现删除按钮
		
		if (!boolean1) {
			// 震动时长
			vibrator.vibrate(100);
			ConsultAdapter.bool = true;
			consultAdapter.notifyDataSetChanged();
			boolean1 = true;
		}
		return true;
	}

	@Override 
	public void onListItemClick(ListView l, View v, int position, long id) {
		// ConsultMsg consultmsg =
		// MainTabActivity.listConsultmsgs.get(position);
		// 这是什么奇怪的标志位？？？
		if (boolean1) {

			ConsultAdapter.bool = false;
			consultAdapter.notifyDataSetChanged();
			boolean1 = false;

		
		}
		// 从顾客列表页面跳转到聊天对话页
		else {
			MessageService.isConsultFragment = false;
			final Bundle bundle = new Bundle();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_in1);
			bundle.putString("mobilecookie",MainTabActivity.listConsultmsgs.get(position).mobilecookie);
			bundle.putBoolean("isFirstVisit",ConsultMsg.getConsutFromUserName(MainTabActivity.listConsultmsgs.get(position).mobilecookie, MainTabActivity.monitor.userid).isFirstVisit );
			ChatFragment newchatFragment = new ChatFragment();
			newchatFragment.setArguments(bundle);
			ft.replace(android.R.id.tabcontent, newchatFragment,Constant.CHAT_FRAGMENT_TAG);
			ft.addToBackStack(null);
			ft.commit();
		}
	}

	private void showButtonWithText(int id, int text) {
		{
			if (getActivity() != null) {
				BadgeButton1 rb1 = (BadgeButton1) getActivity().findViewById(
						R.id.radio1);
				if (rb1 != null) {
					if (text > 0) {
//						rb1.setBadgeVisible();
						rb1.setBadgeDrawableRes(R.drawable.g_unread_messages_bg);
						rb1.showBadge();
					}
					 else {
						rb1.hideBadge();
					}
				}
			}

		}

	}
	// 显示消息
	private void showButtonWithTextNumber(int id, int text) {
		{
			if (getActivity() != null) {
				BadgeButton1 rb1 = (BadgeButton1) getActivity().findViewById(
						R.id.radio1);
				if (rb1 != null) {
					if (text < 10 && text > 0) {
						rb1.setBadgeVisible();
						rb1.setBadgeDrawableRes(R.drawable.g_unread_messages_bg);
						rb1.setBadgeText(text + "");
						rb1.showBadge();
					} else if (text >= 10) {
						rb1.setBadgeVisible();
						rb1.setBadgeDrawableRes(R.drawable.g_unread_messages_bg);
						rb1.setBadgeText("..");
						rb1.showBadge();
					} else {
						rb1.hideBadge();
					}
				}
			}

		}

	}

	// 获取客服名称
	public void getUserName() throws JSONException,
			UnsupportedEncodingException {
		HashMap<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("unitid", MainTabActivity.monitor.unitid);
		paramMap.put("usid", MainTabActivity.monitor.userid);
		paramMap.put("uscookie", MainTabActivity.monitor.uscookie);
		RequestParams requestParams = new RequestParams(paramMap);
		MobileChatClient.get("userver/getMyinfo", requestParams,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject timeline) {
						System.out.println("consult fragment getname"
								+ timeline);
						try {
							String flag = (String) timeline
									.getString("username");

							
							// String a = arg0+"";
							consult_txthead.setText(flag + "的会话");

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

}
