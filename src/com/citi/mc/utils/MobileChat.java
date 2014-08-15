package com.citi.mc.utils;

import org.apache.http.impl.cookie.BasicClientCookie;

import android.content.Context;

import com.citi.mc.utils.HttpUrls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class MobileChat {

	private static final String BASE_URL = "http://106.187.89.54:8000/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	client.addHeader("cookie", "uscookie=gtrhjriyo");
    	client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler,Context context) {
        
    	
    	BasicClientCookie newCookie = new BasicClientCookie("cookie", "awesome");
    	PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
    	
    	newCookie.setVersion(1);
    	newCookie.setDomain("http://106.187.89.54:8180");
    	newCookie.setPath("/");
    	myCookieStore.addCookie(newCookie);
    	
    	
    	client.setCookieStore(myCookieStore);
    	
    	
    	client.post(getAbsoluteUrl(url), params, responseHandler);
    }
   
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	client.addHeader("cookie", "uscookie=gtrhjriyo");
    	client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    
    private static String getAbsoluteUrl(String relativeUrl) {
        return  BASE_URL+ relativeUrl;
    }
	
	
  
}
