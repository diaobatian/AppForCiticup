package com.citi.mc.db;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.citi.mc.utils.Constant;

/*
 * 消息的model
 * refactor 
 * author:lianma 
 * time :2013.11.5
 * 
 * version:1.1
 * changelog:增加注释
 * 
 * version:1.2
 * 增加的功能:
 * 1.消息状态的标志:isSended
 * 2.增加uniquelocal标志本地消息
 * */
@Table(name = "chatMessage")
public class ChatMessage extends Model implements Serializable {
	private static final long serialVersionUID = -8060503040772021877L;

	@Column(name = "uniquelocal")
	public String uniquelocal;
	@Column(name = "issended")
	public int isSended;
	@Column(name = "mId")
	public String mId;
	@Column(name = "type")
	public String type;
	@Column(name = "content")
	public String content;
	@Column(name = "fromId")
	public String fromId;
	@Column(name = "fromName")
	public String fromName;
	@Column(name = "toId")
	public String toId;
	@Column(name = "toName")
	public String toName;
	@Column(name = "createTime")
	public String createTime;
	@Column(name = "status")
	public String status;
	@Column(name = "readedTime")
	public String readedTime;
	@Column(name = "left")
	public String left;
	@Column(name = "image")
	public int image;
	@Column(name = "time")
	public long time;

	public ChatMessage(int image, String left, String content, String mId,
			String type, String fromId, String fromName, String toId,
			String toName, String createTime, String status, String readTime) {
		this.left = left;
		this.content = content;
		this.mId = mId;
		this.type = type;
		this.fromId = fromId;
		this.fromName = fromName;
		this.toId = toId;
		this.toName = toName;
		this.createTime = createTime;
		this.readedTime = readTime;
		this.image = image;
	}

	public ChatMessage(int image, String left, String content,
			String createdtime, String id, String fromId, String toId,
			String fromName) {
		this.left = left;
		this.content = content;
		this.image = image;
		this.createTime = createdtime;
		this.mId = id;
		this.fromId = fromId;
		this.fromName = fromName;
		this.toId = toId;
	}

	public ChatMessage(int image, String left, String content,
			String createdtime, String id, String fromId, String fromName,
			String type, String toId) {
		this.left = left;
		this.content = content;
		this.image = image;
		this.createTime = createdtime;
		this.mId = id;
		this.fromId = fromId;
		this.fromName = fromName;
		this.type = type;
		this.toId = toId;
	}

	public ChatMessage(int image, String left, String content,
			String createdtime, String id, String fromId, String toId) {
		this.left = left;
		this.content = content;
		this.image = image;
		this.createTime = createdtime;
		this.mId = id;
		this.fromId = fromId;
		this.toId = toId;

	}

	public ChatMessage(int image, String left, String content,
			String createdtime) {
		this.left = left;
		this.content = content;
		this.image = image;
		this.createTime = createdtime;

	}

	public void setUniquelocal(String id) {
		this.uniquelocal = id;
	}

	public ChatMessage() {
		// TODO Auto-generated constructor stub
	}

	// 当前客服和客户聊天记录的最前面一条信息
	public static void Clear(String cookie, String userId) {
		System.out.println("Clear 当前客服和客户聊天记录的最前面一条信息");
		List<ChatMessage> msgList = getAllChatByCurrentCookie(cookie, userId);
		if (!msgList.isEmpty()) {
			System.out.println("msgList.get(0).getId()"
					+ msgList.get(0).getId());
			new Delete().from(ChatMessage.class)
					.where("Id = ?", msgList.get(0).getId()).execute();
		}
	}
    /**
     * 清除该用户与对应cookie下的所有对话记录
		*/
	public static void ClearByCookie(String cookie, String userId) {
		new Delete().from(ChatMessage.class)
					.where("fromId = ? AND toId = ? OR fromId = ? AND toId = ?",cookie, userId, userId, cookie).execute();
		
	}
	// 查询所有消息
	public static List<ChatMessage> getAllChat() {
		return new Select().from(ChatMessage.class).execute();
	}

	// 查询所有当前客服和用户的所有消息
	/**
	 根据对话双方查询所有对话
	 @param  String 客户端的cookie
	 @param  String 客服的ID
	  */
	public static List<ChatMessage> getAllChatByCurrentCookie(String cookie,
			String userId) {
		return new Select()
				.from(ChatMessage.class)
				.where("fromId = ? AND toId = ? OR fromId = ? AND toId = ?",
						cookie, userId, userId, cookie).orderBy("Id ASC")
				.execute();
	}
	/**
	 根据对话双方查询所有对话
	 @param  String 客户端的cookie
	 @param  String 客服的ID
	  */
	public static List<ChatMessage> getAllChatByCurrentCookie(String cookie) {
			return new Select()
					.from(ChatMessage.class)
					.where("fromId = ? OR toId = ?",
							cookie, cookie).orderBy("Id ASC")
					.execute();
		}
	// 根据status查询所有消息
	public static List<ChatMessage> getunprocessedmsg() {

		return new Select().from(ChatMessage.class).where("status=?", 0)
				.execute();
	}

	// 根据消息的ID进行删除
	public static void deleteChat(String Id) {
		new Delete().from(ChatMessage.class).where("mId = ?", Id);
	}

	// 根据消息的ID查询返回消息是否存在
	/*
	 * return ：boolean true:不存在 false:存在
	 */
	public static boolean getchatfrommid(String fromId) {
		boolean flag = true;

		int size = new Select().from(ChatMessage.class).where("fromId=?", fromId)
				.execute().size();
		flag = (size > 0 ? false : true);
		return flag;
	}
	
	// 根据消息的ID查询返回消息是否存在
	/*
	 * return ：boolean true:不存在 false:存在
	 */
	public static boolean getchatmsgfrommid(String id) {
		boolean flag = true;

		int size = new Select().from(ChatMessage.class).where("mId=?", id)
				.execute().size();
		flag = (size > 0 ? false : true);
		return flag;
	}

	// 根据消息的uniqueid查询返回消息是否存在
	/*
	 * return ：boolean true:不存在 false:存在
	 */
	public static boolean getChatmsgByUniqueId(String id) {
		boolean flag = true;

		int size = new Select().from(ChatMessage.class)
				.where("uniquelocal=?", id).execute().size();
		flag = (size > 0 ? false : true);
		return flag;
	}

	// 传递ChatMessage对象保存信息
	public static void saveLvmsg(ChatMessage msg, String cookie, String userId) {
		if (getchatmsgfrommid(msg.mId)) {
			if (getAllChatByCurrentCookie(cookie, userId).size() > Constant.COUNT_PERSISTENT_MAX) {
				Clear(cookie, userId);
				// ifNotExistSaveElseUpdate(msg);
				msg.save();
			} else {
				// ifNotExistSaveElseUpdate(msg);
				msg.save();
			}
		}
	}

	public static void saveLvmsgLocal(ChatMessage msg, String cookie,
			String userId) {
		if (getAllChatByCurrentCookie(cookie, userId).size() > Constant.COUNT_PERSISTENT_MAX) {
			Clear(cookie, userId);
			ifNotExistSaveElseUpdateLocal(msg);
		} else {
			ifNotExistSaveElseUpdateLocal(msg);
		}
	}
	public static void saveSingleMessage(ChatMessage message)
	{
		message.save();
	}
    public static void saveChatMessage (List<ChatMessage> chatMessageLists) {
	ActiveAndroid.beginTransaction();
	try{
		for(ChatMessage message : chatMessageLists)
		{
			ChatMessage.saveSingleMessage(message);
		}  
		ActiveAndroid.setTransactionSuccessful();
	}
	catch (Exception e) {
		// TODO: handle exception
	}
	finally{
		ActiveAndroid.endTransaction();
	}
	
}
	// 删除数据库中的第一个元素
	public static void deleteFirstRowInDb(String cookie, String userId) {
		Clear(cookie, userId);
	}

	// 先检查消息是否存在，存在的话则更新
	public static void ifNotExistSaveElseUpdateLocal(ChatMessage chatMessage) {
		// 不存在
		if (getChatmsgByUniqueId(chatMessage.uniquelocal)) {
			chatMessage.save();
		}
		// 存在则更新
		else {
			ChatMessage chatMessageTmp = getChatMessageById(chatMessage.uniquelocal);
			chatMessageTmp.isSended = chatMessage.isSended;
			chatMessageTmp.save();
		}
	}

	// 根据mid获取chatmessage
	public static ChatMessage getChatMessageById(String id) {
		return new Select().from(ChatMessage.class)
				.where("uniquelocal = ?", id).executeSingle();
	}

	// 获取最后一条消息的messageid
	public static String getLastMessageId(String cookie, String userId) {
		String msgid = "";
		List<ChatMessage> msgList = getAllChatByCurrentCookie(cookie, userId);
		if (!msgList.isEmpty()) {
			msgid = ((ChatMessage) new Select().from(ChatMessage.class)
					.where("Id = ?", msgList.get(msgList.size() - 1).getId())
					.executeSingle()).mId;
		}
		return msgid;
	}

	// 查询cookie所有聊天时间
	public static List<ChatMessage> getAllCookie(String cookie) {
		return new Select().from(ChatMessage.class)
				.where("fromId = ? ", cookie).orderBy("Id ASC").execute();
	}

	// 获取最后一条消息的时间
	public static String getTimeCookieId(String cookie) {
		String time = "";
		List<ChatMessage> msgList = getAllCookie(cookie);
		if (!msgList.isEmpty()) {
			time = ((ChatMessage) new Select().from(ChatMessage.class)
					.where("Id = ?", msgList.get(msgList.size() - 1).getId())
					.executeSingle()).createTime;
		}
		return time;
	}

}
