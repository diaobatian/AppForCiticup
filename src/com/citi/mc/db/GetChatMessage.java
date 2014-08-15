package com.citi.mc.db;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.text.InputFilter.LengthFilter;
import android.widget.ListView;

import com.citi.mc.bean.LeaveMessage;
import com.citi.mc.R;
import com.citi.mc.R.id;
import com.citi.mc.utils.MobileChatClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;




public class GetChatMessage
{
	//private List<ChatMessage>conversitionMsg = null;
	
	@SuppressWarnings("static-access")
	public  List<ChatMessage> getConversition(String fromId,String toId) throws Exception
	{
		ChatMessage chatMessage = new ChatMessage();
		 List<ChatMessage>conversitionMsg = null;
//		conversitionMsg = new ArrayList<ChatMessage>();
		conversitionMsg = new LinkedList<ChatMessage>();
		System.out.println(chatMessage.getAllChat().size()+"size=======================");
		for(int i=0;i<chatMessage.getAllChat().size();i++)
		{
			ChatMessage msg=chatMessage.getAllChat().get(i);
			System.out.println(msg+"getcgatmessgae==================msg==============");
			System.out.println(msg.content+"==============msgcontent=============");
			if((msg.fromId.equals(fromId)&&msg.toId.equals(toId))||((msg.fromId.equals(toId)&&msg.toId.equals(fromId))))
			{
				System.out.println("1udaoleaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

				conversitionMsg.add(msg);
				System.out.println(conversitionMsg.size()+"==========================>>>.....");
			}
			
		}
		
		System.out.println(conversitionMsg+"===============>>>conver");
			
		
		return conversitionMsg;
	}
/*	
	public static List<ChatMessage>  getMsgByEndSize(String fromId ,String toId) throws Exception
	{
		
		System.out.println("getMsgBySize>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		//System.out.println(dataBase.getAll());
		List<ChatMessage>mgs = null;
		
		mgs = new ArrayList<ChatMessage>();
		int flag = getConversition(fromId, toId).size();
		if(flag>=5)
		{
			mgs = getConversition(fromId, toId).subList(flag-5, flag);
		}
		else
		{
			mgs = getConversition(fromId, toId);
		}
		if(begin+5<=getConversition(fromId, toId).size())//chatMessage.getAllChat().size()
		{
			System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
			mgs = getConversition(fromId, toId).subList(begin, begin+5);
			
		}
		if(begin+5>getConversition(fromId, toId).size())
		{
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			mgs = getConversition(fromId, toId).subList(begin, getConversition(fromId, toId).size());
		}
		return mgs;
		
//		
	}
	
	
	
	public List<ChatMessage>  getMsgBySize(int begin,String fromId ,String toId) throws Exception
	{
		
		System.out.println("getMsgBySize>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		//System.out.println(dataBase.getAll());
		List<ChatMessage>mgs = null;
		
		mgs = new ArrayList<ChatMessage>();
		
		if(begin+5<=getConversition(fromId, toId).size())//chatMessage.getAllChat().size()
		{
			System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
			mgs = getConversition(fromId, toId).subList(begin, begin+5);
			
		}
		if(begin+5>getConversition(fromId, toId).size())
		{
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			mgs = getConversition(fromId, toId).subList(begin, getConversition(fromId, toId).size());
		}
		return mgs;
		
//		
	}
	
	public List<ChatMessage> getBySize(int begin) throws Exception
	{
		List<ChatMessage>mgs = null;
		
		mgs = new ArrayList<ChatMessage>();
		
		ChatMessage chatMessage = new ChatMessage();
		if(begin+5<=chatMessage.getAllChat().size())//chatMessage.getAllChat().size()
		{
			System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
			mgs = chatMessage.getAllChat().subList(begin, begin+5);
			
		}
		if(begin+5>chatMessage.getAllChat().size())//chatMessage.getAllChat().size()
		{
			System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
			mgs = chatMessage.getAllChat().subList(begin, chatMessage.getAllChat().size());
			
		}
		return mgs;
	}*/
	
}


      

