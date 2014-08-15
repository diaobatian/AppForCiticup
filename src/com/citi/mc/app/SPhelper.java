package com.citi.mc.app;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings;

/**
 * 保存到本地储蓄类
 * @author ChunTian
 *
 */
public class SPhelper {
	SharedPreferences sharedPreferences;
	Editor editor;

	public SPhelper(Context c) {
		sharedPreferences = c.getSharedPreferences("shared",
				Activity.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}
	public void setPassword(String s) {
		editor.putString("password", s);
		editor.commit();
	}
	public String getPassword() {
		String s = sharedPreferences.getString("password",
				null);
		return s;
	}
	public void setWelcome_msg(String s) {
		editor.putString("welcom_msg", s);
		editor.commit();
	}
	public String getWelcome_msg() {
		String s = sharedPreferences.getString("welcom_msg",
				null);
		return s;
	}
	public void setUsername(String s) {
		editor.putString("usname", s);
		editor.commit();
	}
	public String getUsername() {
		String s = sharedPreferences.getString("usname",
				null);
		return s;
	}
	public void setUsid(String s) {
		editor.putString("usid", s);
		editor.commit();
	}
	public String getUsid() {
		String s = sharedPreferences.getString("usid",
				null);
		return s;
	}
	public void setUnitid(String s) {
		editor.putString("unitid", s);
		editor.commit();
	}
	public String getUnitid() {
		String s = sharedPreferences.getString("unitid",
				null);
		return s;
	}
	public void setUscookie(String s) {
		editor.putString("uscookie", s);
		editor.commit();
	}
	public String getUscookie() {
		String s = sharedPreferences.getString("uscookie",
				null);
		return s;
	}
	public void setEmail(String s) {
		editor.putString("email", s);
		editor.commit();
	}
	public String getEmail() {
		String s = sharedPreferences.getString("email",
				null);
		return s;
	}
	public void setLoginSig(String s) {
		editor.putString("islogin", s);
		editor.commit();
	}
	public String getLoginSig() {
		String s = sharedPreferences.getString("islogin",
				null);
		return s;
	}
	public void setToggleState(String s)
	{
		editor.putString("toggleOpen", s);
		editor.commit();
	}
	
	public String getToggleState()
	{
		String string = sharedPreferences.getString("toggleOpen", null);
		
		return string;
	}
	
	public void setSongState(String s)
	{
		editor.putString("songOpen", s);
		editor.commit();
	}
	
	public String getSongState()
	{
		String string = sharedPreferences.getString("songOpen", null);
		
		return string;
	}
	public void setInvisiableState(String TrueOrFalse)
	{
		editor.putString("invisiable", TrueOrFalse);
		editor.commit();
	}
	public String getInvisiableState()
	{
		return sharedPreferences.getString("invisiable", null);
	}
	public void setNodisturbState(String TrueOrFalse)
	{
		editor.putString("nodisturb", TrueOrFalse).commit();
	
	}
	public String getNodisturbState()
	{
		return sharedPreferences.getString("nodisturb", null);
	}
	public void setAutoSavePassword(String s)
	{
		editor.putString("autoSavePassword", s).commit();
	
	}
	
	public String getAutoSavePassword()
	{
		String string = sharedPreferences.getString("autoSavePassword", null);
		
		return string;
	}
	public void setCookie(String s) {
		editor.putString("mobileCookie", s);
		editor.commit();
	}
	public String getCookie() {
		String s = sharedPreferences.getString("mobileCookie",
				null);
		return s;
	}
}
