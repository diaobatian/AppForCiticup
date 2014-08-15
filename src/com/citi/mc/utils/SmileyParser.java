package com.citi.mc.utils;


import java.io.InputStream;
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

public class SmileyParser {

	private static SmileyParser instance;
	Handler handler;
	
	
	public static SmileyParser getInstance()

	{

		return instance;

	}

	public static void init(Context context)

	{

		instance = new SmileyParser(context);

	}

	private final Context context;

	public final String[] smileyTexts;

	private final Pattern pattern;

	private final HashMap<String, Integer> smileyMap;

	private SmileyParser(Context context)

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

	Smileys.getSmileyResource(Smileys.WEIXIAO), // 0

			Smileys.getSmileyResource(Smileys.PIEZUI), // 1

			Smileys.getSmileyResource(Smileys.SE), // 2

			Smileys.getSmileyResource(Smileys.FADAI), // 3

			Smileys.getSmileyResource(Smileys.DEYI), // 4

			Smileys.getSmileyResource(Smileys.LIULIE), // 5

			Smileys.getSmileyResource(Smileys.HAIXIU), // 6

			Smileys.getSmileyResource(Smileys.BIZUI), // 7

			Smileys.getSmileyResource(Smileys.SHUI), // 8

			Smileys.getSmileyResource(Smileys.DAKU), // 9

			Smileys.getSmileyResource(Smileys.GANGA), // 10

			Smileys.getSmileyResource(Smileys.FANU), // 11

			Smileys.getSmileyResource(Smileys.TIAOPI), // 12

			Smileys.getSmileyResource(Smileys.CIYA), // 13

			Smileys.getSmileyResource(Smileys.JINGYA), // 14

			Smileys.getSmileyResource(Smileys.NANGUO), // 15

			Smileys.getSmileyResource(Smileys.KU), // 16

			Smileys.getSmileyResource(Smileys.LENGHAN), // 17

			Smileys.getSmileyResource(Smileys.ZHUAKUANG), // 18

			Smileys.getSmileyResource(Smileys.TU), // 19
			Smileys.getSmileyResource(Smileys.TOUXIAO), // 0

			Smileys.getSmileyResource(Smileys.KEAI), // 1

			Smileys.getSmileyResource(Smileys.BAIYAN), // 2

			Smileys.getSmileyResource(Smileys.AOMAN), // 3

			Smileys.getSmileyResource(Smileys.JIE), // 4

			Smileys.getSmileyResource(Smileys.KUN), // 5

			Smileys.getSmileyResource(Smileys.JINGKONG), // 6

			Smileys.getSmileyResource(Smileys.LIUHAN), // 7

			Smileys.getSmileyResource(Smileys.HANXIAO), // 8

			Smileys.getSmileyResource(Smileys.DABING), // 9

			Smileys.getSmileyResource(Smileys.FENDOU), // 10

			Smileys.getSmileyResource(Smileys.ZHOUMA), // 11

			Smileys.getSmileyResource(Smileys.YIWEN), // 12

			Smileys.getSmileyResource(Smileys.XU), // 13

			Smileys.getSmileyResource(Smileys.YUN), // 14

			Smileys.getSmileyResource(Smileys.ZHEMO), // 15

			Smileys.getSmileyResource(Smileys.SHUAI), // 16

			Smileys.getSmileyResource(Smileys.KULOU), // 17

			Smileys.getSmileyResource(Smileys.QIAODA), // 18

			Smileys.getSmileyResource(Smileys.ZAIJIAN), // 19
			Smileys.getSmileyResource(Smileys.CAHAN), // 0

			Smileys.getSmileyResource(Smileys.KOUBI), // 1

			Smileys.getSmileyResource(Smileys.GUZHANG), // 2

			Smileys.getSmileyResource(Smileys.QIUDALE), // 3

			Smileys.getSmileyResource(Smileys.HUAIXIAO), // 4

			Smileys.getSmileyResource(Smileys.ZUOHENGHENG), // 5

			Smileys.getSmileyResource(Smileys.YOUHENGHENG), // 6

			Smileys.getSmileyResource(Smileys.HAQIAN), // 7

			Smileys.getSmileyResource(Smileys.BISHI), // 8

			Smileys.getSmileyResource(Smileys.WEIQU), // 9

			Smileys.getSmileyResource(Smileys.KUAIKULE), // 10

			Smileys.getSmileyResource(Smileys.YINXIAN), // 11

			Smileys.getSmileyResource(Smileys.QINQIN), // 12

			Smileys.getSmileyResource(Smileys.XIA), // 13

			Smileys.getSmileyResource(Smileys.KELIAN), // 14

			Smileys.getSmileyResource(Smileys.CAIDAO), // 15

			Smileys.getSmileyResource(Smileys.XIGUA), // 16

			Smileys.getSmileyResource(Smileys.PIJIU), // 17

			Smileys.getSmileyResource(Smileys.LANQIU), // 18

			Smileys.getSmileyResource(Smileys.PINGPANG), // 19
			Smileys.getSmileyResource(Smileys.KAFEI), // 0

			Smileys.getSmileyResource(Smileys.FAN), // 1

			Smileys.getSmileyResource(Smileys.ZHUTOU), // 2

			Smileys.getSmileyResource(Smileys.MEIGUI), // 3

			Smileys.getSmileyResource(Smileys.DIAOXIE), // 4

			Smileys.getSmileyResource(Smileys.SHIAI), // 5

			Smileys.getSmileyResource(Smileys.AIXIN), // 6

			Smileys.getSmileyResource(Smileys.XINSUI), // 7

			Smileys.getSmileyResource(Smileys.DANGAO), // 8

			Smileys.getSmileyResource(Smileys.SHANDIAN), // 9

			Smileys.getSmileyResource(Smileys.ZHADAN), // 10

			Smileys.getSmileyResource(Smileys.DAO), // 11

			Smileys.getSmileyResource(Smileys.ZUQIU), // 12

			Smileys.getSmileyResource(Smileys.PIAOCHONG), // 13

			Smileys.getSmileyResource(Smileys.BIANBIAN), // 14

			Smileys.getSmileyResource(Smileys.YUELIANG), // 15

			Smileys.getSmileyResource(Smileys.TAIYANG), // 16

			Smileys.getSmileyResource(Smileys.LIWU), // 17

			Smileys.getSmileyResource(Smileys.YONGBAO), // 18

			Smileys.getSmileyResource(Smileys.QIANG), // 19
			Smileys.getSmileyResource(Smileys.RUO), // 0

			Smileys.getSmileyResource(Smileys.WOSHOU), // 1

			Smileys.getSmileyResource(Smileys.SHENGLI), // 2

			Smileys.getSmileyResource(Smileys.BAOQUAN), // 3

			Smileys.getSmileyResource(Smileys.GOUYIN), // 4

			Smileys.getSmileyResource(Smileys.QUANTOU), // 5

			Smileys.getSmileyResource(Smileys.CHAJIN), // 6

			Smileys.getSmileyResource(Smileys.AINI), // 7

			Smileys.getSmileyResource(Smileys.NO), // 8

			Smileys.getSmileyResource(Smileys.OK), // 9

			Smileys.getSmileyResource(Smileys.AIQING), // 10

			Smileys.getSmileyResource(Smileys.FEIWEN), // 11

			Smileys.getSmileyResource(Smileys.TIAOTIAO), // 12

			Smileys.getSmileyResource(Smileys.FADOU), // 13

			Smileys.getSmileyResource(Smileys.OUHUO), // 14

			Smileys.getSmileyResource(Smileys.ZHUANQUAN), // 15

			Smileys.getSmileyResource(Smileys.KETOU), // 16

			Smileys.getSmileyResource(Smileys.HUITOU), // 17

			Smileys.getSmileyResource(Smileys.TIAOSHENG), // 18

			Smileys.getSmileyResource(Smileys.HUISHOU), // 19
			Smileys.getSmileyResource(Smileys.JIDONG), // 15

			Smileys.getSmileyResource(Smileys.JIEWU), // 16

			Smileys.getSmileyResource(Smileys.QIANWEN), // 17

			Smileys.getSmileyResource(Smileys.ZUOTAIJI), // 18

			Smileys.getSmileyResource(Smileys.YOUTAIJI), // 19

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
