package com.citi.mc.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import com.citi.mc.db.ChatMessage;
import com.citi.mc.db.ConsultMsg;
import com.citi.mc.app.ChatFragment;
import com.citi.mc.app.MainTabActivity;
import com.citi.mc.R;
import com.citi.mc.utils.Constant;
import com.citi.mc.utils.ChatSmileyParser;
import com.citi.mc.utils.Timeparser;
import com.loopj.android.image.SmartImageView;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * 消息的adapter
 * refactor 
 * author:lianma 
 * time :2013.11.5
 * 
 * version:1.1
 * changelog:
 * 
 1.预留微信的图标
 2.功能完善
 3.添加布局实现，发送消息之后的验证
 * */
@SuppressLint("ResourceAsColor")
@SuppressWarnings("deprecation")
public class ChatMsgAdapter extends ArrayAdapter<ChatMessage> {

	private Context context;
	private LayoutInflater layoutInflater;
	private ChatMessage chat;
	private List<ChatMessage> chatContents = null;
	private ChatSmileyParser smileyParser;
	private FragmentManager fragmentManager;
	
	//状态的保存
	private List<Integer> stateMessageList;
	/*
	 * 构造函数
	 */
	public ChatMsgAdapter(Context context, int textViewResourceId,
			List<ChatMessage> listchatmsg) {
		super(context, textViewResourceId);
		this.context = context;
		this.chatContents = listchatmsg;
		layoutInflater = LayoutInflater.from(context);
		//初始化,数据刚开始全部设置成发送成功
		stateMessageList = new ArrayList<Integer>();
		for (int i = 0; i < chatContents.size(); i++) {
			stateMessageList.add(Constant.MESSAGE_IS_SENDED);
		}
	}

	public ChatMsgAdapter(Context context, int textViewResourceId,FragmentManager fragmentManager) {
		super(context, textViewResourceId);
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		chatContents = new LinkedList<ChatMessage>();
		this.fragmentManager = fragmentManager;
		//初始化,数据刚开始全部设置成发送成功
		stateMessageList = new LinkedList<Integer>();
	}

	// 动态的添加消息
	@Override
	public void add(ChatMessage object) {
		// TODO Auto-generated method stub
		if(object.left.equals("true"))
		{
			stateMessageList.add(Constant.MESSAGE_IS_SENDED);
		}
		else 
		{
			stateMessageList.add(Constant.MESSAGE_IS_SENDING);
		}
		chatContents.add(object);
		
		notifyDataSetChanged();
	}
	//刷新消息
	public void freshAdded(ChatMessage object,int type) {
		// TODO Auto-generated method stub
		synchronized (this) {
			if (object!=null&&object.uniquelocal!=null) {
				for(int i=0;i<chatContents.size();i++)
				{
					if(chatContents.get(i).uniquelocal!=null)
					{
						if(chatContents.get(i).uniquelocal.equals(object.uniquelocal))
						{
							chatContents.get(i).isSended = type;
							stateMessageList.set(i, type);
						}
					}
				}
				notifyDataSetChanged();
			}
		}
	}
	// 添加多条消息数据
	public void addMany(List<ChatMessage> list) {
		chatContents.addAll(0, list);
		for (int i = 0; i < list.size(); i++) {
			stateMessageList.add(0, list.get(i).isSended);
		}
		notifyDataSetChanged();
	}

	@Override
	public ChatMessage getItem(int position) {
		// TODO Auto-generated method stub
		return chatContents.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chatContents.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getPosition(ChatMessage item) {
		// TODO Auto-generated method stub
		return super.getPosition(item);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TextView chat_textView_time = null;
		TextView chatContent=null;
		
		//新增的功能
		ProgressBar progressbar;
		ImageView messageSendImageView;
		
		chat = chatContents.get(position);
		final int pos = position;
		smileyParser = ChatSmileyParser.getInstance();
		String str = chat.content;

		/*
		 * 消息是不是客服本身决定其位置 左边:客户 右边：客服(使用者)
		 */
		
		if (chat.left.equals("true")) {
			convertView = layoutInflater.inflate(R.layout.listitem_chat_left, null);
			chat_textView_time=(TextView)convertView.findViewById(R.id.timeofchat_text);
			chatContent=(TextView)convertView.findViewById(R.id.content);
			progressbar=(ProgressBar)convertView.findViewById(R.id.progressbar);
			messageSendImageView=(ImageView)convertView.findViewById(R.id.messageimage);
			//客户不用注备名字
			chat_textView_time.setText(Timeparser.getTime(chat.createTime));
//			chatContent.setTextColor(Color.rgb(0, 0, 0));
			progressbar.setVisibility(View.GONE);
			messageSendImageView.setVisibility(View.GONE);
		} 
		else if (chat.left.equals("false")) 
		{
			convertView = layoutInflater.inflate(R.layout.listitem_chat_right, null);
			chat_textView_time=(TextView)convertView.findViewById(R.id.timeofchat_right);
			chatContent=(TextView)convertView.findViewById(R.id.content);
			progressbar=(ProgressBar)convertView.findViewById(R.id.progressbar);
			messageSendImageView=(ImageView)convertView.findViewById(R.id.messageimage);
			//客服要填上名字
			chat_textView_time.setText(chat.fromName+" "+Timeparser.getTime(chat.createTime));
//			chatContent.setBackgroundResource(R.drawable.bg_pic_message_right);
//			chatContent.setTextColor(Color.rgb(255, 255, 255));
			if(stateMessageList.get(pos)!=null)
			{
				showByMessageState(progressbar,messageSendImageView,stateMessageList.get(pos),pos);
			}
		}
		else {
			throw new NullPointerException();
		}

//		chat_textView_time.setText(Timeparser.getTime(chat.createTime));
		try {
			chatContent.setText(smileyParser.addSmileySpans(str));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		// 点击消息之后，要进行的操作
		chatContent.setFocusable(true);
		chatContent
				.setOnLongClickListener(new View.OnLongClickListener() {

					@Override
					public boolean onLongClick(View view) {
						new AlertDialog.Builder(context).setPositiveButton(
								"复制", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										ClipboardManager copy = (ClipboardManager) context
												.getSystemService(Context.CLIPBOARD_SERVICE);
										copy.setText(chatContents.get(position).content
												.toString());
									}
								}).show();
						return false;
					}
				});
		return convertView;
	}
	
	private void showByMessageState(ProgressBar progressbar,ImageView messageSendImageView,int state,int pos)
	{
		if(state==Constant.MESSAGE_IS_SENDING)
		{
			progressbar.setVisibility(View.VISIBLE);
			messageSendImageView.setVisibility(View.GONE);
		}
		else if(state==Constant.MESSAGE_IS_FAILED)
		{
			final ChatMessage message = chat;
			messageSendImageView.setVisibility(View.VISIBLE);
			progressbar.setVisibility(View.GONE);
			messageSendImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ChatFragment chatFragment = (ChatFragment) fragmentManager.findFragmentByTag(Constant.CHAT_FRAGMENT_TAG);
					if(chatFragment!=null)
					{
						try {
							System.out.println("重新发送");
							message.isSended = Constant.MESSAGE_IS_SENDING;
							chatFragment.sendMsg(message);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
		else if(state==Constant.MESSAGE_IS_SENDED)
		{
			progressbar.setVisibility(View.GONE);
			messageSendImageView.setVisibility(View.GONE);
		}
	}
	static class ViewHolder {
		
	}

	// 根据传递消息的类型，获取相应的显示图片

	private int getResources(ChatMessage chatMessage) {

		//代表当前客服，对应的客户的信息
		ConsultMsg consultmsg = ConsultMsg.getConsutFromUserName(chatMessage.fromId,MainTabActivity.monitor.userid);
		int id;
		if (consultmsg.os.equalsIgnoreCase(Constant.ANDROID_ICON)) {
			id = R.drawable.android;
		} else if (consultmsg.os.equalsIgnoreCase(Constant.IOS_ICON)) {
			id = R.drawable.ios;
		} else if (consultmsg.os.equalsIgnoreCase(Constant.WEIXIN_ICON)) {
			id = R.drawable.weixin;
		} else {
			id = R.drawable.onlinecomputer;
		}
		return id;
	}

}
