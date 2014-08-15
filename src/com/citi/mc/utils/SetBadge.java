package com.citi.mc.utils;

import android.view.View;

import com.citi.mc.badge.BadgeButton;

public class SetBadge 
{
	 public void showButtonWithText(View view,int id, String text) {
	     BadgeButton btn = (BadgeButton)view.findViewById(id);
	     btn.setBadgeText(text);
	     btn.showBadge();
	 }
}
