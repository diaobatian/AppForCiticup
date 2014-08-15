
package com.citi.mc.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Message;

import com.citi.mc.bean.Update;
import com.citi.mc.app.ChatFragment;
import com.citi.mc.utils.HttpUrls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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
public class MobileChatClient {

//	private static final String BASE_URL = "http://test.mobilechat.im:8000/";
	private static final String BASE_URL = "http://chat.mobilechat.im:8000/";
	private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)";
    public static AsyncHttpClient client = new AsyncHttpClient();
    public static boolean polling = false;
    
    public static void get(Context context,String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	if (client == null) client = new AsyncHttpClient();
    	client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
    	client.addHeader("User-Agent", USER_AGENT);
    	client.get(context,getAbsoluteUrl(url), params, responseHandler);
    	//设置超时时间
    	client.setTimeout(Constant.POLL_TIMEOUT);
    }
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	if (client == null) client = new AsyncHttpClient();
    	
    	client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
    	client.addHeader("User-Agent", USER_AGENT);
    	client.get(getAbsoluteUrl(url), params, responseHandler);
    	//设置超时时间
    	client.setTimeout(Constant.POLL_TIMEOUT);
    	
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler,Context context) {
    	if (client == null) client = new AsyncHttpClient();
    	
    	BasicClientCookie newCookie = new BasicClientCookie("cookie", "awesome");
    	PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
    	newCookie.setVersion(1);
    	newCookie.setDomain("http://106.187.89.54:8180");
    	newCookie.setPath("/");
    	myCookieStore.addCookie(newCookie);
    	client.setCookieStore(myCookieStore);
    	client.post(context,getAbsoluteUrl(url), params, responseHandler);
    	client.setTimeout(Constant.POLL_TIMEOUT);
    	
    }
    
public static void post1(Context context,String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if (client == null) client = new AsyncHttpClient();
		
		client.post(context,getAbsoluteUrl(url), params, responseHandler);
    	client.setTimeout(Constant.POLL_TIMEOUT);
    	
    }
    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	if (client == null) client = new AsyncHttpClient();
    	
    	client.put(null,getAbsoluteUrl(url), params, responseHandler);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	if (client == null) client = new AsyncHttpClient();
    	
    	client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    
    private static String getAbsoluteUrl(String relativeUrl) {
        return  BASE_URL+ relativeUrl;
    }
	
    
  	//cancel task
  	public static void cancelTask(Context context)
  	{
  		if (client == null) client = new AsyncHttpClient();
  		
  		client.cancelRequests(context, true);
  		//ChatFragment.isReadyCancel = true;
  	}
  	
}
