package com.citi.mc.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.MemoryFile;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.baidu.mobstat.StatService;
import com.citi.mc.R;
import com.citi.mc.db.UnitInfo;
import com.citi.mc.utils.MobileManagerClient;
import com.citi.mc.utils.StringQuanpinTranform;
import com.citi.mc.utils.TempFileManager;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.image.SmartImageView;

@SuppressLint("NewApi")
public class EnterpriseDataFragment extends Fragment {
	private ImageView iv1 = null;
	private SmartImageView imageView = null;
	private Button backBtn = null;
	private Button alterBtn = null;
	private int index;
	private TextView nameTextView = null;
	private TextView contacTextView = null;
	private TextView telTextView = null;
	private TextView urlTextView = null;
	private TextView address = null;
	private TextView leavemsg_emailTextView = null;
	private HashMap<String, Object> unitinfoHashMap = new HashMap<String, Object>();
	// private Handler getUnitinfoHandler=null;
	private UnitInfo unitinfo = null;
	// Vibrator实现手机震动效果
	private Vibrator vibrator;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.enterprise_info, container,false);
		vibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
		//设置MainTabActivity的flag为true
		MainTabActivity.flag = true;
		unitinfo = MainTabActivity.unitinfo;
		if (unitinfo != null) {
			initUI(view);
			// 初始化数据
		}
		//网络获取数据
		else
		{
			
		}
				return view;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		//百度统计
		StatService.onPause(this);
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		//百度统计
				StatService.onResume(this);
	}
	
	
	
	//加载网络图片
	private class DownloadPicThread implements Runnable {
		/**
		 * 0外存不可用
		 * 1外存可用*/
		private int downLoadmode;
		public DownloadPicThread(int choosenDownLoadmode)
		{
			downLoadmode=choosenDownLoadmode;
		}
		@Override
		public void run() {
			{
				
				// 如果外存可用把处理的图片放在外存		
				MobileManagerClient.getInternetImage(unitinfo.unitlogo, "Absolute", new BinaryHttpResponseHandler()
				{
					 @Override
				     public void onSuccess(byte[] imageData) {

								Bitmap enterpriseIconBitMap=BitmapFactory.decodeByteArray(imageData, 0, imageData.length); 
								Bitmap  enterpriseIconBitMapRound =toRoundBitmap(enterpriseIconBitMap);
								switch (downLoadmode) {
								case 0:
									String saveDic = Environment
											.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/mobilechat/cache/";
									String logoFileName = unitinfo.unitlogo
											.substring(unitinfo.unitlogo
													.lastIndexOf("/") + 1);
									File dicFile = new File(saveDic);
									if (!dicFile.exists()) {
										dicFile.mkdirs();
									}
									File logoFile = new File(TempFileManager
											.filePath("unitlogo", logoFileName));
									try {
										// if(!logoFile.exists())
										// {
										// logoFile.createNewFile();
										// }
										FileOutputStream fops = new FileOutputStream(
												logoFile);
										// fops.write(imageData);
										enterpriseIconBitMapRound.compress(
												Bitmap.CompressFormat.PNG, 100,
												fops);
										fops.flush();
										fops.close();

									} catch (FileNotFoundException e) {

										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;
								case 1:
									
									
									break;
								default:
									break;
							
								}
										Message msg = Message.obtain();
										// 本地没有缓存图片；需要通过缓存获取
										msg.what = 0;
										msg.obj=enterpriseIconBitMapRound;
										getUnitinfoHandler.sendMessage(msg);
//									Bitmap newbitmap=toRoundBitmap(bitmap);
//								 newbitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
//					Canvas canvas = new Canvas(newbitmap);
//					Paint paint = new Paint(); 
//					paint.setAntiAlias(true);
//					
//					paint.setStyle(Style.STROKE);
//					paint.setColor(Color.WHITE);
//					paint.setStrokeWidth(4);
//					canvas.drawBitmap(newbitmap, 0, 0, paint);
//					canvas.save(Canvas.ALL_SAVE_FLAG);
//					canvas.restore();
					
//					imageView.setImageBitmap(newbitmap);
					
					
					
//					Drawable drawable=getResources().getDrawable(R.drawable.logo_ring);
//					try{
//					    	int getIntrinsicWidth=imageView.getDrawable().getIntrinsicWidth();
//					    	int getIntrinsicHeight=imageView.getDrawable().getIntrinsicHeight();
//					    BitmapDrawable bd = (BitmapDrawable)drawable;
//					    Bitmap bm =bd.getBitmap();
//					    Bitmap newScaledBitmap=Bitmap.createScaledBitmap(bm, getIntrinsicWidth,getIntrinsicHeight , true);
//					    imageView.setBackground(new BitmapDrawable(newScaledBitmap ));}
//					    catch(Exception e)
//					    {
//					    	e.printStackTrace();
//					    	System.out.println("error");
//					    }
				         // Successfully got a response
				     }

				     @Override
				     public void onFailure(Throwable e, byte[] imageData) {
				    	 System.out.println("onFailure");
				         // Response failed :(
				     }
				});
		
			// Drawable drawable=new
			}
		}
			
	}

	private class Dianjishijian implements OnLongClickListener {

		// TODO Auto-generated method stub

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.contact:
				// 震动时长
				vibrator.vibrate(200);
				alertDialog(String.valueOf(unitinfo.unitcontact));
				break;
			case R.id.tel:
				// 震动时长
				vibrator.vibrate(200);
				alertDialog(String.valueOf(unitinfo.unittel));
				break;
			case R.id.leavemsg_email:
				// 震动时长
				vibrator.vibrate(200);
				alertDialog(String.valueOf(unitinfo.unitemail));
				break;
			case R.id.url:
				// 震动时长
				vibrator.vibrate(200);
				alertDialog(String.valueOf(unitinfo.uniturl));
				break;
			case R.id.address:
				// 震动时长
				vibrator.vibrate(200);
				alertDialog(String.valueOf(unitinfo.address));
				break;
			default:
				break;

			}
			return true;
		}
	}

	private void alertDialog(final String text) {

		ClipboardManager copy = (ClipboardManager) getActivity()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		copy.setText(null);
		copy.setText(text);
		Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
	}

	// 获取企业资料
	public void getUnitInfo() {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitid", MainTabActivity.monitor.unitid);
		// paramMap.put("usid", tmpMonitor.userid);
		// paramMap.put("uscookie", tmpMonitor.uscookie);
		// paramMap.put("mobile",mobilecookiefromconsult);

		RequestParams params = new RequestParams(paramMap);
		MobileManagerClient.get("unit/info", params,
				MainTabActivity.monitor.uscookie,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						super.onFailure(arg0);
						System.out.println("into onfailure................");
					}

					@Override
					protected Object parseResponse(String arg0)
							throws JSONException {
						// TODO Auto-generated method stub

						JSONObject jsonObject = new JSONObject(arg0);
						Log.i("sa", "jsonObject======================>"
								+ jsonObject);
						System.out
								.println(jsonObject
										+ "into parseResponse arg0 sucess...............");
						// 向unitiinfohashmap中插入企业资料数据
						// unitinfoHashMap=new HashMap<String, Object>();
						// unitinfoHashMap.put("name",
						// jsonObject.getString("name"));
						unitinfo.unitid = LoginActivity.inloginMonitor.unitid;
						unitinfo.unitname = jsonObject.getString("name");
						try {
							// unitinfoHashMap.put("url",
							// jsonObject.getString("url"));
							if (jsonObject.getString("url").equals("")) {
								unitinfo.uniturl = "";
							}
							unitinfo.uniturl = jsonObject.getString("url");
						} catch (JSONException e) {
							// TODO: handle exception
							System.out.println("异常啦");
							unitinfo.uniturl = "未指定";
						}
						try {
							// unitinfoHashMap.put("tel",
							// jsonObject.getString("url"));
							if (jsonObject.getString("tel").equals("")) {
								unitinfo.unittel = "未指定";
							}
							unitinfo.unittel = jsonObject.getString("tel");
						} catch (JSONException e) {
							// TODO: handle exception
							System.out.println("异常啦");
							unitinfo.unittel = "未指定";
						}
						try {
							// unitinfoHashMap.put("tel",
							// jsonObject.getString("url"));
							if (jsonObject.getString("leavemsg_email").equals(
									"")) {
								unitinfo.unitemail = "未指定";
							}
							unitinfo.unitemail = jsonObject
									.getString("leavemsg_email");
						} catch (JSONException e) {
							// TODO: handle exception
							System.out.println("异常啦");
							unitinfo.unitemail = "未指定";
						}
						/*
						 * try { //unitinfoHashMap.put("tel",
						 * jsonObject.getString("url")); if
						 * (jsonObject.getString("contact").equals("")) {
						 * unitinfo.unitcontact=""; }
						 * unitinfo.unitcontact=jsonObject.getString("contact");
						 * } catch (JSONException e) { // TODO: handle exception
						 * System.out.println("异常啦");
						 * unitinfo.unitcontact="未指定"; }
						 */

						try {
							// unitinfoHashMap.put("tel",
							// jsonObject.getString("url"));
							if (jsonObject.getString("contact").equals("")) {
								unitinfo.unitcontact = "未指定";
							}
							unitinfo.unitcontact = jsonObject
									.getString("contact");
						} catch (JSONException e) {
							// TODO: handle exception
							System.out.println("异常啦");
							unitinfo.unitcontact = "未指定";
						}
						try {
							// unitinfoHashMap.put("tel",
							// jsonObject.getString("url"));
							JSONObject upbtnJsonObject = jsonObject
									.getJSONObject("popUpBtn");
							System.out.println(upbtnJsonObject
									+ "upbtnjson............");

							JSONObject selfbtnJsonObject = upbtnJsonObject
									.getJSONObject("selfBtn");
							if (selfbtnJsonObject.getString("path").equals("")) {
								unitinfo.unitlogo = "/image/selfdefbg.png";
							}
							unitinfo.unitlogo = selfbtnJsonObject
									.getString("path");
						} catch (JSONException e) {
							// TODO: handle exception
							System.out.println("异常啦");
							unitinfo.unitlogo = "/image/selfdefbg.png";
						}
						try {

							if (jsonObject.getString("location").equals("")) {
								unitinfo.address = "未指定";
							}
							unitinfo.address = jsonObject.getString("location");
						} catch (JSONException e) {
							// TODO: handle exception
							System.out.println("异常啦");
							unitinfo.address = "未指定";
						}
						unitinfo.save();
						Message msg = new Message();
						msg.what = 1;
						getUnitinfoHandler.sendMessage(msg);
						return super.parseResponse(arg0);

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						System.out.println(arg0
								+ "...............mobiledata put sucess ");
					}

					@Override
					public void onFailure(Throwable arg0, JSONObject arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
						System.out.println("jsonobject failure aaa");
					}

				});

	}

	private final Handler getUnitinfoHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// 初始话文本信息
			if (msg.what == 0) {
				Bitmap bitmap=(Bitmap) msg.obj;
				imageView.setImageBitmap(bitmap);
			}
			// 初始化图片
			else if (msg.what == 1) {

			}

		}

	};

	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}

	public static String StringFilter(String str) throws PatternSyntaxException {
		str = str.replaceAll(".", "[").replaceAll("】", "]")
				.replaceAll("！", "!");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	private void initUI(View view)
	{
		nameTextView=(TextView)view.findViewById(R.id.name);
		contacTextView=(TextView)view.findViewById(R.id.contact);
		telTextView=(TextView)view.findViewById(R.id.tel);
		imageView= (SmartImageView)view.findViewById(R.id.enterprise_sign);
		urlTextView=(TextView)view.findViewById(R.id.websiteurl);
		leavemsg_emailTextView=(TextView)view.findViewById(R.id.email);
//		address=(TextView)view.findViewById(R.id.address);
		contacTextView.setOnLongClickListener(new Dianjishijian());
		telTextView.setOnLongClickListener(new Dianjishijian());
		urlTextView.setOnLongClickListener(new Dianjishijian());
		leavemsg_emailTextView.setOnLongClickListener(new Dianjishijian());
		//
		nameTextView.setText(String.valueOf(unitinfo.unitname));
		contacTextView.setText(String.valueOf(unitinfo.unitcontact));
		String str = String.valueOf(unitinfo.uniturl);
		String[] strs = str.split("//");
		urlTextView.setText(StringQuanpinTranform
				.ToDBC("http://www.mobileChat.im/"));
		Log.i("as", "unitinfo.uniturl==========>" + unitinfo.uniturl);
		leavemsg_emailTextView.setText(String
				.valueOf(unitinfo.unitemail));
		telTextView.setText(String.valueOf(unitinfo.unittel));

//存在SD卡 先检查SD卡是否存在 存在就去看SD里面有没有历史记录；
//		if (Environment.MEDIA_MOUNTED.equals(Environment
//				.getExternalStorageState()))
//		{
//			
//			if (TempFileManager.fileExist(TempFileManager.UNITLOGO,
//					 unitinfo.unitlogo.substring(unitinfo.unitlogo
//								.lastIndexOf("/") + 1))) {
//				
//				String absolutePath = Environment.getExternalStorageDirectory()
//						.getAbsolutePath()
//						+ "/mobilechat/cache/"
//						+ unitinfo.unitlogo.substring(unitinfo.unitlogo
//								.lastIndexOf("/") + 1);
//				
//				Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
//				
////				Bitmap newbitmap = toRoundBitmap(bitmap);
//				
//				imageView.setImageBitmap(bitmap);
//			}
//			else
//			{
//				//外存可用 图片下载下来后要保存在本地
//				new Thread(new DownloadPicThread(0)).start();
//			}
//		}
//		else
//		{
			//如果外存不可用 要在内存中处理；且不能保存
			new Thread(new DownloadPicThread(1)).start();
//		}
		
		
	}
	//画背景企业图标的背景圆环
	public static void DrawIconCircle()
	{
		
	}
	 public Bitmap toRoundBitmap(Bitmap bitmap) {
         int width = bitmap.getWidth();
         int height = bitmap.getHeight();
         float roundPx;
         float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
         if (width <= height) {
                 roundPx = (float) (width / 2.00000);
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
                 roundPx = (float) (height / 2.00000);
                 float clip = (float) ((width - height) / 2.0000);
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
//         Matrix matrix = new Matrix(); 
//         matrix.postScale(1.5f,1.5f); //长和宽放大缩小的比例
//       Bitmap output=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
         final Rect src = new Rect((int) left, (int) top, (int) right,(int) bottom);
         final Rect dst = new Rect((int) dst_left, (int) dst_top,
 (int) dst_right, (int) dst_bottom);
         final RectF rectF = new RectF(dst);
         
         final Paint paint = new Paint();
         paint.setAntiAlias(true);
         final int color = 0xff424242;
         paint.setColor(color);
         
         Canvas canvas = new Canvas(output);
         canvas.drawARGB(0, 0, 0, 0);
         
//         Path path = new Path();  
//         path.addCircle(  
//             ((float)dst_right) / 2,  
//             ((float)dst_bottom) / 2,  
//             (Math.min(((float)dst_right), ((float)dst_bottom)) / 2),  
//             Path.Direction.CCW);  
//         canvas.clipPath(path);  
         
         canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//         canvas.drawCircle(roundPx, roundPx, roundPx, paint);  
         paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
         canvas.drawBitmap(bitmap, src, dst, paint);
         return output;
 }

}