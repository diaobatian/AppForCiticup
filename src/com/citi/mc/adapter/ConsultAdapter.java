package com.citi.mc.adapter;

/**
 * 
 * 会话界面适配器
 */
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.citi.mc.badge.BadgeButton;
import com.citi.mc.bean.LeaveMessage;
import com.citi.mc.db.ChatMessage;
import com.citi.mc.db.ConsultMsg;
import com.citi.mc.db.UserInfo;
import com.citi.mc.app.ChatFragment;
import com.citi.mc.app.ConsultFragment;
import com.citi.mc.app.MainTabActivity;
import com.citi.mc.R;
import com.citi.mc.utils.Constant;
import com.citi.mc.utils.Timeparser;
import com.loopj.android.image.SmartImageView;

import android.R.integer;
import android.R.raw;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class ConsultAdapter extends BaseAdapter {

	private List<ConsultMsg> list;
	private Context context;
	private LayoutInflater layoutInflater;
	public static Boolean isyonghu;
	private Button curDel_btn;
	private float x, ux;
	private FragmentManager fragmentManager;
	public int count = 5;
	private HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
	private ConsultMsg consultmsg = null;
	public static Boolean bool = false;
	
	public ConsultAdapter(Context context, List<ConsultMsg> list,
			FragmentManager fragmentManager, Boolean bool) {
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		this.fragmentManager = fragmentManager;
		this.bool = bool;
	}

	public ConsultAdapter(Context context, int textViewResourceId) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		list = new LinkedList<ConsultMsg>();
	}

	public void addMessage(ConsultMsg msg) {
		// list.add(0, msg);
		list.add(msg);
		notifyDataSetChanged();
	}

	public void freshData(List<ConsultMsg> msgList) {
		this.list = msgList;
		notifyDataSetChanged();
	}

	public void addMessageFirst(ConsultMsg msg) {

		list.add(0, msg);
		notifyDataSetChanged();
	}

	public void addMessages(List<ConsultMsg> msgList) {
		// list.addAll(0,msgList);
		list.addAll(msgList);
		notifyDataSetChanged();
	}

	public void removeMessage(int position) {
		list.remove(position);
		notifyDataSetChanged();
	}

	public void clear() {
		list.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("size about list" + list.size());
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		// TODO Auto-generated method stub

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		final int pos = position;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.listitem_consult,
					null);
			viewHolder.badyImage = (BadgeButton) convertView
					.findViewById(R.id.consult_img);
			viewHolder.txtview = (TextView) convertView
					.findViewById(R.id.consult_item_name);
			viewHolder.details = (TextView) convertView
					.findViewById(R.id.consult_item_detail);
			viewHolder.timeTextView = (TextView) convertView
					.findViewById(R.id.time_txt);
			viewHolder.lixianTextView = (TextView) convertView
					.findViewById(R.id.lixian_txt);
			viewHolder.linear3 = (LinearLayout) convertView
					.findViewById(R.id.linear5);
			viewHolder.linear = (LinearLayout) convertView
					.findViewById(R.id.linear08);
			viewHolder.linear2 = (RelativeLayout) convertView
					.findViewById(R.id.linear09);
			viewHolder.btnDel = (ImageButton) convertView.findViewById(R.id.del);
			viewHolder.btnDel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// ConsultMsg consultmsg
					// =MainTabActivity.listConsultmsgs.get(pos);
					// consultmsg.isDelete = false;
					// consultmsg.save();
					ConsultMsg.RemoveMobileCookie(list.get(pos).mobilecookie,
							list.get(pos).monitorId);
					MainTabActivity.listConsultmsgs.remove(pos);
					notifyDataSetChanged();
				}
			});
			
			if(position % 2 == 0){
				viewHolder.linear3.setBackgroundResource(R.drawable.list_view);
			}else{
				viewHolder.linear3.setBackgroundResource(R.drawable.list_view2);
			}
			
			final Bundle bundle = new Bundle();
			final LinearLayout linear4 = viewHolder.linear3;
			viewHolder.badyImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						linear4.setBackgroundResource(R.color.list_item_bq);
						System.out.println("====================>");
						FragmentTransaction ft = fragmentManager
								.beginTransaction();
						bundle.putString("mobilecookie",
								list.get(pos).mobilecookie);
						bundle.putString("userName", list.get(pos).Username);
						bundle.putBoolean("isOnline", list.get(pos).isOnline);
						System.out.println(list.get(pos).city
								+ "city================================"
								+ list.get(pos).latestmsg);
						// bundle.putString("msgid",list.get(arg2).);
						ChatFragment newchatFragment = new ChatFragment();
						v.setBackgroundResource(R.color.light_gray);
						MainTabActivity.layout.setVisibility(View.GONE);
						newchatFragment.setArguments(bundle);
						ft.replace(android.R.id.tabcontent, newchatFragment,
								Constant.CHAT_FRAGMENT_TAG);

						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
						ft.addToBackStack(null);
						ft.commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 头像变灰
		consultmsg = list.get(pos);
		// 如果isDelete的状态为true，则显示删除按钮
		if (bool) {
			viewHolder.linear3.setBackgroundResource(R.drawable.list_view1);
			viewHolder.linear2.setVisibility(View.VISIBLE);
			viewHolder.linear.setVisibility(View.GONE);
		} else {
			if(position % 2 == 0){
				viewHolder.linear3.setBackgroundResource(R.drawable.list_view);
			}else{
				viewHolder.linear3.setBackgroundResource(R.drawable.list_view2);
			}
			viewHolder.linear2.setVisibility(View.GONE);
			viewHolder.linear.setVisibility(View.VISIBLE);
		}
		int id;
		if (consultmsg.isOnline) {
			String[] strs = consultmsg.os.toLowerCase().split("[ ]");
			if (consultmsg.os.toLowerCase().contains("android")) {
				id = R.drawable.android60y;
			} else if (consultmsg.os.toLowerCase().contains("ios")) {
				id = R.drawable.iphone60y;
			} else if (consultmsg.os.toLowerCase().contains("weixin")) {
				id = R.drawable.weixin;
			} else if (consultmsg.os.toLowerCase().contains("sweibo")) {
				id = R.drawable.weibo;
			} else if (strs[0].equalsIgnoreCase("windows")
					&& strs[1].equalsIgnoreCase("phone")) {
				id = R.drawable.windowsphone;
			} else {
				id = R.drawable.def_user_icon60y;
			}
			isyonghu = true;
			viewHolder.lixianTextView.setText("");
		} else {
			String[] strs = consultmsg.os.toLowerCase().split("[ ]");
//			if (consultmsg.os.toLowerCase().contains("android")) {
//				id = R.drawable.android60y;
//			} else if (consultmsg.os.toLowerCase().contains("ios")) {
//				id = R.drawable.iphone60y;
//			} else if (consultmsg.os.toLowerCase().contains("weixin")) {
//				id = R.drawable.weixin;
//			} else if (consultmsg.os.toLowerCase().contains("sweibo")) {
//				id = R.drawable.weibo;
//			} else if (strs[0].equalsIgnoreCase("windows")
//					&& strs[1].equalsIgnoreCase("phone")) {
//				id = R.drawable.windowsphone;
//			} else {
//				id = R.drawable.def_user_icon60y;
//			}
			if (consultmsg.os.toLowerCase().contains("android")) {
				id = R.drawable.android60y_dark;
			} else if (consultmsg.os.toLowerCase().contains("ios")) {
				id = R.drawable.iphone60y_dark;
			} else if (consultmsg.os.toLowerCase().contains("weixin")) {
				id = R.drawable.weixin_an;
			} else if (consultmsg.os.toLowerCase().contains("sweibo")) {
				id = R.drawable.weibo_an;
			} else if (strs[0].equalsIgnoreCase("windows")
					&& strs[1].equalsIgnoreCase("phone")) {
				id = R.drawable.windowsphone_an;
			} else {
				id = R.drawable.def_user_icon60y_dark;
			}
			isyonghu = false;
			viewHolder.lixianTextView.setText("离线");
		}

		if(consultmsg.province.equals("")){
			UserInfo userInfo = UserInfo.getNameId(consultmsg.mobilecookie);
			if(userInfo != null){
				viewHolder.txtview.setText(userInfo.province + userInfo.city + userInfo.isp + consultmsg.Username.replace("顾客"," ").trim());
			}
			else {
				viewHolder.txtview.setText(consultmsg.Username);
			}
		}else{
			viewHolder.txtview.setText(consultmsg.province + consultmsg.city + consultmsg.isp + consultmsg.Username.replace("顾客"," ").trim());
			
		}
		
		viewHolder.details.setText(String.valueOf(consultmsg.latestmsg));

		if (ChatMessage.getTimeCookieId(consultmsg.mobilecookie) != null
				&& !ChatMessage.getTimeCookieId(consultmsg.mobilecookie)
						.equals("")) {
			viewHolder.timeTextView.setText(Timeparser.getTime(String.valueOf(ChatMessage.getTimeCookieId(String.valueOf(consultmsg.mobilecookie)))));
		} else {

//			if (viewHolder.timeTextView.getText().toString().equals("")) {
//				viewHolder.timeTextView.setText("刚刚");
//			}

		}

		if (consultmsg.Unreadcnt != 0) {
			Log.i("sa", "ConsultAdapter==============================>value---");
			viewHolder.badyImage.setBadgeText(String
					.valueOf(consultmsg.Unreadcnt));
			viewHolder.badyImage.showBadge();
		}
		if (consultmsg.Unreadcnt == 0) {
			System.out.println("unreadcnt=======================0>>");
			viewHolder.badyImage.hideBadge();
		}
		viewHolder.badyImage.setImageRes(id);
		return convertView;
	}

	class ViewHolder {
		// ImageView badyImage;
		TextView txtview;
		TextView details;
		BadgeButton badyImage;
		// ImageView directorImageView;
		TextView timeTextView;
		TextView lixianTextView;
		ImageButton btnDel;
		LinearLayout linear, linear3;
		RelativeLayout linear2;

	}

	private Integer getResources(ConsultMsg consultmsg) {
		int id;
		if (consultmsg.os == "android") {
			id = R.drawable.android;
		} else if (consultmsg.os == "ios") {
			id = R.drawable.ios;
		} else {
			id = R.drawable.onlinecomputer;
		}
		return id;

	}

}
