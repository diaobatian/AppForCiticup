package com.citi.mc.badge;

/*
 * Copyright (C) 2011 Jo�o Xavier <jcxavier@jcxavier.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import com.citi.mc.R;

import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BadgeButton1 extends FrameLayout {
   
    private TextView badgeText;
    private ImageView imageView;
   
    private RelativeLayout badgeView;
    private FrameLayout frame;
    
    private ImageView btnClickThrough;
    
    @SuppressWarnings("deprecation")
	public BadgeButton1(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        inflater.inflate(R.drawable.badge_button_iosstyle, this);
        
        // initialize layout components   
//        badgeText = (TextView) findViewById(R.id.badgebtn_txt);
        badgeView = (RelativeLayout) findViewById(R.id.badgebtn_rl);
        btnClickThrough = (ImageView) findViewById(R.id.badgeBtn);
        imageView = (ImageView) findViewById(R.id.badgebtn_img);
        frame = (FrameLayout)findViewById(R.id.frame);
    }
    
   
    private float extractFloat(String attributeValue) {
        attributeValue = attributeValue.replaceAll("[a-zA-Z]", "");
        return Float.parseFloat(attributeValue);
    }

    
    @Override
    public void setOnClickListener(OnClickListener listener) {
        btnClickThrough.setOnClickListener(listener);
    }
    
    

    
   public void setOnTouchListener(OnTouchListener listener)
   {
	   btnClickThrough.setOnTouchListener(listener);
   }
   

    public void setBadgeText(String badgeTextString) {
        badgeText.setText(badgeTextString);
    }
    
   
    public void setBadge() {
    	badgeText.setVisibility(View.GONE);
    }
    public void setBadgeVisible() {
    	badgeText.setVisibility(View.VISIBLE);
    }
    
    public void setBadgeDrawableRes(int res)
    {
    	 ((ImageView) findViewById(R.id.badgebtn_img)).setImageResource(res);
    }
    
	/**
	 * @param id
	 */
    public void setBadgeBackgroundResource(int id)
    {
    	((ImageView)findViewById(R.id.badgeBtn)).setImageResource(id);
    }
    
    /**
	 * @param id
	 */
    public void setBadgeimageBackgroundResource(int id)
    {
    	((FrameLayout)findViewById(R.id.frame)).setBackgroundResource(id);
    }
    
    public String getBadgeText() {
        return badgeText.getText().toString();
    }
   
    public void hideBadge() {
        badgeView.setVisibility(View.GONE);
    }
    
    
 
    public void showBadge() {
        badgeView.setVisibility(View.VISIBLE);
    }
    private class TestListen implements View.OnClickListener
	{

	@Override
	public void onClick(View v) {
		System.out.println("ok");
	}
		
	}
}

