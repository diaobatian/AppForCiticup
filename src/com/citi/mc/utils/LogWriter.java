package com.citi.mc.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

import android.os.Environment;
public class LogWriter {
/**写日志<br>

* 写logString字符串到./log目录下的文件中
* @param logString 日志字符串
* @author tower
*/
	

	
public static void write(String fileNameHead,String logString) {
	
	
	
	
	
	String storageState = Environment.getExternalStorageState();	
	String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobilechat/log/";
	if(storageState.equals(Environment.MEDIA_MOUNTED)){
try {
 
 Calendar cd = Calendar.getInstance();//日志文件时间
 int year=cd.get(Calendar.YEAR);
 String month=addZero(cd.get(Calendar.MONTH)+1);
 String day=addZero(cd.get(Calendar.DAY_OF_MONTH));
 String hour=addZero(cd.get(Calendar.HOUR_OF_DAY));
 String min=addZero(cd.get(Calendar.MINUTE));
 String sec=addZero(cd.get(Calendar.SECOND));
 String logFilePathName=".log";
 File fileParentDir=new File(savePath);//判断log目录是否存在
 if (!fileParentDir.exists()) {
  fileParentDir.mkdirs();
 }
 if (fileNameHead==null||fileNameHead.equals("")) {
  logFilePathName=savePath+year+month+day+".txt";//日志文件名
 }else {
  logFilePathName=savePath+fileNameHead+year+month+day+".txt";//日志文件名
 }

 PrintWriter printWriter=new PrintWriter(new FileOutputStream(logFilePathName, true));//紧接文件尾写入日志字符串
 String time="["+year+month+day+"-"+hour+":"+min+":"+sec+"] ";
 printWriter.println(time+logString);
 printWriter.flush();
 printWriter.close();

} catch (FileNotFoundException e) {
 // TODO Auto-generated catch block
 e.getMessage();
}
	}
}

/**整数i小于10则前面补0
* @param i
* @return
* @author tower
*/
public static String addZero(int i) {
if (i<10) {
 String tmpString="0"+i;
 return tmpString;
}
else {
 return String.valueOf(i);
}  

}
}