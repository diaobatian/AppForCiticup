/**
 * 
 */
package com.citi.mc.utils;

import java.io.File;

import android.os.Environment;

/**
 * @author 彭程
 * @category 用于外部文件结构的管理
 *
 */
	
public class TempFileManager {
	public static final String updateApk="UpdateApk";
	public static final String UNITLOGO="unitlogo";
	
	/**
	 * 
	 */
	public TempFileManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static Boolean fileExist(String fileType,String Filename)

	{
		File tagrtfile;
		if(fileType==UNITLOGO)
		{
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/mobilechat/cache/"
					+ Filename;
			tagrtfile=new File(path);
			return tagrtfile.exists();
		}
		return false;
		
	}
	public static String filePath(String fileType,String Filename)
	{
		if(fileType==UNITLOGO)
		{
		return Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/mobilechat/cache/"
				+ Filename;
		}
		else{
			return null;
		}
		
	}
}
