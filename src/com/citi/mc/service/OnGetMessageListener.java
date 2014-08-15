package com.citi.mc.service;


import java.io.Serializable;

import org.json.JSONObject;

import com.citi.mc.db.ChatMessage;

/** 
 * @作者 lianma
 * @email lianlupeng@gmail.com
 * @创建日期 2013-11-4 
 * @版本 V 1.0 
 * 自定义的回调接口，用于更新相应的消息
 * 当有消息来到的时候进行回调的函数
 * 消息的统一格式ChatMessage
 */
public interface OnGetMessageListener extends Serializable{

	//在聊天页面
	public abstract void onMessageArriveUpdateChatFragment(ChatMessage chatMessageGet);
	//记录多少条未读消息
	public abstract void onMessageArriveUpdateKeHuOnline();
	//在列表页面
	public abstract void onMessageArriveUpdateConsultFragment();
	//用户离线
	public abstract void onMessageArriveUpdateUserLoginState();
	public abstract void onMessageArriveOnActivityPause(ChatMessage chatMessageGet);
	//刷新顾客列表
	public abstract void onRefreshState();
}


