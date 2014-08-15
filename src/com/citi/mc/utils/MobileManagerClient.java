package com.citi.mc.utils;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.util.HashMap;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.citi.mc.bean.Update;
import com.citi.mc.app.LoginActivity;
import com.citi.mc.utils.HttpUrls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/** 
 * @作者 newbie 
 * @email lianlupeng@gmail.com
 * @创建日期 2013-6-26 
 * @版本 V 1.0
 * 工具类 
 */
public class MobileManagerClient {

	private static final String BASE_FILEURL = "http://www.mobilechat.im";
	private static final String BASE_URL = "http://www.mobilechat.im/";
//	private static final String BASE_FILEURL="http://test.mobilechat.im";
//	private static final String BASE_URL = "http://test.mobilechat.im/";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36";
    private static AsyncHttpClient client = new AsyncHttpClient();
     private static Update update=null;
    
    public static void get(String url, RequestParams params, String cookie,AsyncHttpResponseHandler responseHandler) {
    	//client.addHeader("cookie", "uscookie=gtrhjriyo");
    	System.out.println("mangerclient start=======================");
    	client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
    	client.addHeader("User-Agent", USER_AGENT);
    	client.addHeader("cookie", "uscookie="+cookie);
    	client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler,Context context) {
        
    	
    	BasicClientCookie newCookie = new BasicClientCookie("cookie", "awesome");
    	PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
    	
    	newCookie.setVersion(1);
    	newCookie.setDomain("http://www.mobilechat.im/");
    	newCookie.setPath("/");
    	myCookieStore.addCookie(newCookie);
    	
    	
    	client.setCookieStore(myCookieStore);
    	
    	
    	client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    //
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	//添加 http head
    	client.addHeader("cookie", "uscookie="+LoginActivity.randomcookie);
    	//
    	client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    
    private static String getAbsoluteUrl(String relativeUrl) {
        return  BASE_URL+ relativeUrl;
    }
    private static String getUpdateUrl(String url)
    {
//    	 return "http://mobilechat.im/" + url;
    	return BASE_URL+url;
    }
    public static void getUpdate(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
    	client.addHeader("User-Agent", USER_AGENT);
    	client.get(getUpdateUrl(url), params, responseHandler);
    }
	
    public static Update  getUpdate(final Handler handler) throws JSONException, UnsupportedEncodingException
	{
		HashMap<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("os","android");

		RequestParams params = new RequestParams(paramMap);

		MobileManagerClient.getUpdate("appversion", params, new JsonHttpResponseHandler()
		{

			
			@Override
			protected void handleSuccessJsonMessage(int arg0, Object arg1) {
				// TODO Auto-generated method stub
				super.handleSuccessJsonMessage(arg0, arg1);
				System.out.println("111111111111111111111111");
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				System.out.println("22222222222222222222");
			}

			@Override
			protected Object parseResponse(String arg0) throws JSONException {
				// TODO Auto-generated method stub
				return super.parseResponse(arg0);
			}

			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0);
//				System.out.println("44444444444444");
				 update = new Update();
				 try {
					 if(arg0.getString("path")!=null)
					 {
					update.setPath(arg0.getString("path"));
					 }
					 if(arg0.getString("version")!=null)
					 {
					update.setVersionCode(arg0.getString("version"));
					 }
//					System.out.println(arg0+"version=====================>>>");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 Message message=Message.obtain();
				 message.what = 1;
				 message.obj = update;
				 handler.sendMessage(message);
				 
			}
			
			
			
		});
			
		return update;	
			
			
				
	}
    /**
     * @param url 图片地址
     * @param urlMode路径模式
     * @param handler返回流处理*/
    //获取网络图片
    public static void getInternetImage(String url,String urlMode,BinaryHttpResponseHandler responseHandler)
    {
    	if(urlMode=="Absolute")
    	{
    		client.get(url, responseHandler);
    	}
    	else if(urlMode=="Relative")
    	{
    	client.get(getAbsoluteUrl(url), responseHandler);
    	}
    }
    public static String GetBaseFileURl()
    {
    	return BASE_FILEURL;
    }
    
}