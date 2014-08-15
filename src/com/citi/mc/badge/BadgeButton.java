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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
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

public class BadgeButton extends FrameLayout {

	private TextView badgeText;
	private ImageView imageView;

	private RelativeLayout badgeView;
	private Bitmap bitmap;

	private ImageView btnClickThrough;

	@SuppressWarnings("deprecation")
	public BadgeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.drawable.badge_button, this);

		// initialize layout components
		badgeText = (TextView) findViewById(R.id.badgebtn_txt);
		badgeView = (RelativeLayout) findViewById(R.id.badgebtn_rl);
		btnClickThrough = (ImageView) findViewById(R.id.badgeBtn);
		imageView = (ImageView) findViewById(R.id.badgebtn_img);

	}

	private float extractFloat(String attributeValue) {
		attributeValue = attributeValue.replaceAll("[a-zA-Z]", "");
		return Float.parseFloat(attributeValue);
	}

	@Override
	public void setOnClickListener(OnClickListener listener) {
		btnClickThrough.setOnClickListener(listener);
	}

	public void setOnTouchListener(OnTouchListener listener) {
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

	public void setBadgeDrawable(Drawable badgeDrawable) {
		((ImageView) findViewById(R.id.badgebtn_img))
				.setImageDrawable(badgeDrawable);
	}

	public void setBadgeDrawableRes(int res) {
		((ImageView) findViewById(R.id.badgebtn_img)).setImageResource(res);
	}

	public void setImageRes(int id){
		bitmap = BitmapFactory.decodeResource(getResources(),id);
		Bitmap output = toRoundCorner(bitmap);
		btnClickThrough.setImageBitmap(output);
		
		if(bitmap!=null&&!bitmap.isRecycled()){
			bitmap.recycle();
			bitmap=null;
		}
		System.gc();
		
		//btnClickThrough.setImageResource(id);
		
	}
	
	public String getBadgeText() {
		return badgeText.getText().toString();
	}

	public void hideBadge() {
		badgeView.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @方法名  BadgeButton 
	 * @包名  com.example.badge 
	 * @作者  ChunTian 
	 * @时间  2013年12月31日 下午4:44:28
	 * @Email ChunTian1314@vip.qq.com
	 * @版本  V1.0
	 * @功能: TODO(处理图像 将图像设置为圆形) 
	 * @param @param bitmap
	 * @param @return    设定文件 
	 * @return Bitmap    返回类型 
	 * @throws
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public void showBadge() {
		badgeView.setVisibility(View.VISIBLE);
	}

	private class TestListen implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			System.out.println("ok");
		}

	}
}
