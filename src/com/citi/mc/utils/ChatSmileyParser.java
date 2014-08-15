package com.citi.mc.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.citi.mc.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatSmileyParser {

	private static ChatSmileyParser instance;
	Handler handler;
	public static ChatSmileyParser getInstance()

	{

		return instance;

	}

	public static void init(Context context)

	{

		instance = new ChatSmileyParser(context);

	}

	private final Context context;

	public final String[] smileyTexts;

	private final Pattern pattern;

	private final HashMap<String, Integer> smileyMap;

	private ChatSmileyParser(Context context)

	{

		this.context = context;

		this.smileyTexts = context.getResources().getStringArray(
				R.array.default_smiley_name);

		this.smileyMap = buildSmileyToRes();

		this.pattern = buildPattern();

	}

	/**
	 * 2013.7.13 zl 获取图片的资源
	 */
	public static final int[] DEFAULT_SMILEY_RES_IDS = {

		ChatSmileys.getSmileyResource(ChatSmileys.WEIXIAO), // 0

			ChatSmileys.getSmileyResource(ChatSmileys.SE), // 1

			ChatSmileys.getSmileyResource(ChatSmileys.KU), // 2

			ChatSmileys.getSmileyResource(ChatSmileys.LIULEI), // 3

			ChatSmileys.getSmileyResource(ChatSmileys.DAKU), // 4

			ChatSmileys.getSmileyResource(ChatSmileys.GANGA), // 5

			ChatSmileys.getSmileyResource(ChatSmileys.TIAOPI), // 6

			ChatSmileys.getSmileyResource(ChatSmileys.CIYA), // 7

			ChatSmileys.getSmileyResource(ChatSmileys.QIUDALE), // 8

			ChatSmileys.getSmileyResource(ChatSmileys.NANGUO), // 9

			ChatSmileys.getSmileyResource(ChatSmileys.LENGHAN), // 10
			ChatSmileys.getSmileyResource(ChatSmileys.TOUXIAO),

			ChatSmileys.getSmileyResource(ChatSmileys.KEAI), // 11

			ChatSmileys.getSmileyResource(ChatSmileys.BAIYAN), // 12

			ChatSmileys.getSmileyResource(ChatSmileys.LIUHAN), // 13

			ChatSmileys.getSmileyResource(ChatSmileys.HANXIAO), // 14

			ChatSmileys.getSmileyResource(ChatSmileys.DABING), // 15

			ChatSmileys.getSmileyResource(ChatSmileys.FENDOU), // 16

			ChatSmileys.getSmileyResource(ChatSmileys.ZHOUMA), // 17

			ChatSmileys.getSmileyResource(ChatSmileys.YIWEN), // 18

			ChatSmileys.getSmileyResource(ChatSmileys.YUN), // 19
			ChatSmileys.getSmileyResource(ChatSmileys.SHUAI), // 0

			ChatSmileys.getSmileyResource(ChatSmileys.ZAIJIAN), // 1

			ChatSmileys.getSmileyResource(ChatSmileys.KOUBI), // 2

			ChatSmileys.getSmileyResource(ChatSmileys.HUAIXIAO), // 3

			ChatSmileys.getSmileyResource(ChatSmileys.KUAIKULE), // 4

			ChatSmileys.getSmileyResource(ChatSmileys.KELIAN), // 5

			ChatSmileys.getSmileyResource(ChatSmileys.PIJIU), // 6

			ChatSmileys.getSmileyResource(ChatSmileys.KAFEI), // 7

			ChatSmileys.getSmileyResource(ChatSmileys.XINSUI), // 8

			ChatSmileys.getSmileyResource(ChatSmileys.QIANG), // 9

			ChatSmileys.getSmileyResource(ChatSmileys.RUO), // 10

			ChatSmileys.getSmileyResource(ChatSmileys.WOSHOU), // 11

			ChatSmileys.getSmileyResource(ChatSmileys.SHENGLI), // 12

			ChatSmileys.getSmileyResource(ChatSmileys.OK), // 13

			

	};

	private HashMap<String, Integer> buildSmileyToRes()

	{

		if (DEFAULT_SMILEY_RES_IDS.length != smileyTexts.length)

		{

			throw new IllegalStateException("Smiley resource ID/text mismatch");

		}

		HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>();

		for (int i = 0; i < smileyTexts.length; i++)

		{

			smileyToRes.put(smileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);

		}

		return smileyToRes;

	}

	private Pattern buildPattern()

	{

		StringBuilder patternString = new StringBuilder(smileyTexts.length * 3);

		patternString.append('(');

		for (String s : smileyTexts)

		{

			patternString.append(Pattern.quote(s));

			patternString.append('|');

		}

		patternString.replace(patternString.length() - 1,

		patternString.length(), ")");

		return Pattern.compile(patternString.toString());

	}
	public CharSequence addSmileySpans(CharSequence text)

	{

		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		Matcher matcher = pattern.matcher(text);

		while (matcher.find())

		{

			int resId = smileyMap.get(matcher.group());

			builder.setSpan(new ImageSpan(context, resId), matcher.start(),

					matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		}

		return builder;

	}
public void addSmileySpans(final CharSequence text,final TextView tv)

	{
		new Thread()
		{
			@Override
			public void run()
			{
				final String gifID = 0 + "";
				int isExist = gifExist(gifID);
				// Log.v("hwLog","gifID:"+gifID+"/nisExist="+isExist);
				if (isExist != -1)
				{
					Log.e("hwLog", "终止:" + isExist);
					// 已有动画，终止旧动画
					List<GifTextDrawable> mFaceList = gifList.get(isExist)
							.getGifTextDrawableList();

					for (int i = 0; i < mFaceList.size(); i++) {
						mFaceList.get(i).stop();
					}
					gifList.remove(isExist);
				}
				//===================================gouzao
				 SpannableStringBuilder  builder = new SpannableStringBuilder(text);
				System.out.println(builder+"=============builder");
				final List<GifTextDrawable> mFaceList = new ArrayList<GifTextDrawable>();
				
				Matcher matcher = pattern.matcher(text);

				while (matcher.find())

				{
					// 根据找到的资源id，进行gifView的替换
					int resId = smileyMap.get(matcher.group());
					System.out.println("resid===================>" + resId);
					GifTextDrawable mFace = new GifTextDrawable(tv);
					
		     		final GifOpenHelper gHelper = new GifOpenHelper();
		     		gHelper.read(context.getResources().openRawResource(resId));
		     		BitmapDrawable bd = new BitmapDrawable(gHelper.getImage());
		     		mFace.addFrame(bd, gHelper.getDelay(0));
		     		//以前i为1
		     		for (int i = 0; i < gHelper.getFrameCount(); i++) {
		     			mFace.addFrame(new BitmapDrawable(gHelper.nextBitmap()), gHelper
		     					.getDelay(i));
		     		}
		            float faceH = bd.getIntrinsicHeight();//getIntrinsicHeight();
		            float faceW = bd.getIntrinsicWidth();
		            
		            //视为为dp
		            faceH = faceH;
		            faceW = faceW;
		         
		            //再转回px
		            faceH = UnitTransformUtil.dip2px(context, faceH);
		            faceW = UnitTransformUtil.dip2px(context, faceW);
		            
		            
		            //表情高度固定为文字高度
		            float x=faceH/tv.getLineHeight();
		            faceW = faceW / x;
		            faceH = tv.getLineHeight();
		            
		     		mFace.setBounds(0, 0, (int)faceW,(int)faceH);
		     		mFace.setOneShot(false);
		     	
		             ImageSpan span = new ImageSpan(mFace, ImageSpan.ALIGN_BOTTOM);  //ALIGN_BOTTOM
		             //替换一个表情
		             builder.setSpan(span,matcher.start(),matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//span
		             
		             mFaceList.add(mFace);
					// 设置
//					builder.setSpan(new ImageSpan(context, resId), matcher.start(),
		//
//					matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					// =============================================
					// final InputStream is
					// =context.getResources().openRawResource(R.drawable.f000);
					// final GifFrame[] frames = CommonUtil.getGif(is);
					// System.out.println("delay:" + frames[1].delay + ",size:" +
					// frames.length);
					//
					// mGifTask = new PlayGifTask(iv, frames);
					// mGifTask.start();
				}
				
				gifList.add(new GifObj(0+"",mFaceList));
		     	System.out.println(builder+"==============budiler=============");
		     	final SpannableStringBuilder  ssForPost = builder;
		         //显示新的已经替换表情的text
		  
		       tv.post(new Runnable(){
		        
					public void run() {
						// TODO 自动生成的方法存根
						System.out.println("加载了啊==================");
						System.out.println(ssForPost+"==============bu===============");
						
						tv.setText(ssForPost);
						
						System.out.println(tv.getText()+"================tv");
						if(!mFaceList.isEmpty())
						{
							for(int i=0;i<mFaceList.size();i++)
							{
								
								mFaceList.get(i).start();
							}
						}
						
					}
		        	 
		         });
		        
			}
		}.start();
	}
	
	// change by sishao =======================================
	private int gifExist(String gifID) {
		if(gifList==null)
		{
			gifList = new ArrayList<GifObj>();
		}
		for (int i = 0; i < gifList.size(); i++) {
			if (gifList.get(i).getGifId().equals(gifID)) {
				return i;
			}
		}
		return -1;

	}

	private static List<GifObj> gifList = new ArrayList<GifObj>();
}
