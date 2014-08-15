package com.citi.mc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import android.R.integer;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Vibrator;

import com.citi.mc.app.MainTabActivity;
import com.citi.mc.R;
import com.citi.mc.app.SPhelper;

/**
 * 
 * 声音还是震动
 * @author ChunTian
 *
 */
public class SongAndVibrator 
{
	private  SPhelper sPhelper=null;
	private Context context;
	public SongAndVibrator(Context context)
	{
		this.context = context;
		
	}
	
	
	public void  isVibrate(Context context,int type)
	{
		SPhelper sPhelper = new SPhelper(context);
//		if(sPhelper.getToggleState().equals("true")||type==Constant.VIBRATOR_FROM_BACK)
		if(sPhelper.getToggleState().equals("true"))
		{
			//Vibrator实现手机震动效果
			Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(200);
			System.out.println(sPhelper.getToggleState()+"============>>>>>>>>zhendong");
		}
		
	}
	public void isSong(Context context,String type)
	 {
		
			Uri ringToneUri =null;
			 if(type.equals("mb_normal"))
			 {
				 System.out.println("isnew===============");
				 ringToneUri = Uri.parse("android.resource://com.citi.mc/"+R.raw.msg);
			 }
			 else if(type.equals("mobile_online"))
			 {
				 System.out.println("isonline=======================");
				 ringToneUri = Uri.parse("android.resource://com.citi.mc/"+R.raw.online);
			}
			 if(type.equals("mb_system"))
			 {
				 System.out.println("isnew===============");
				 ringToneUri = Uri.parse("android.resource://com.citi.mc/"+R.raw.system);
			 }
			 try
			 {
				 sPhelper= new SPhelper(context);
				 MediaPlayer mediaPlayer = new MediaPlayer();
				 mediaPlayer.setDataSource(context,ringToneUri);
				 mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				 mediaPlayer.setOnPreparedListener(new MediaLIstener());
				 mediaPlayer.prepareAsync();
				
			 }
			 catch(Exception e)
			 { 
				 e.printStackTrace(); 
			 }
		 
	 }

private class MediaLIstener implements OnPreparedListener
{
	
	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		System.out.println(sPhelper.getSongState()+"songstate==============");
		 mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
             public void onCompletion(MediaPlayer mp) {
                 //cleanupPlayer(mp);
            	 System.out.println("=================:::::complete");
             }
         });
         mediaPlayer.setOnErrorListener(new OnErrorListener() {
             public boolean onError(MediaPlayer mp, int what, int extra) {
               //  cleanupPlayer(mp);
            	 System.out.println("=================:::::error");
                 return true;
             }
         });
         if(sPhelper.getSongState()!=null)
         {
        	 if(sPhelper.getSongState().equals("true"))
    		 {
    			 AudioManager audiomanager = (AudioManager)context.getSystemService(Service.AUDIO_SERVICE);
    			    audiomanager.setMode(AudioManager.MODE_NORMAL);                                    
    			    audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,                            
    			            audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);            
    			    audiomanager.adjustVolume(AudioManager.ADJUST_RAISE, 0);            
    			 mediaPlayer.start();
    			 System.out.println(sPhelper.getSongState()+"=====================>>>>>shengyin");
    			 if(!mediaPlayer.isPlaying())
    			 {
    				 mediaPlayer.release();
    			 }
    		 }
    		 if(sPhelper.getSongState().equals("false"))
    		 {
    			 mediaPlayer.release();
    		 }
         }
	}

}
	
	 
	 
}
