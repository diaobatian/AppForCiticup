
package com.citi.mc.app;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ActiveAndroid.initialize(this);
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
		System.out.println("application onTerminate");
		ActiveAndroid.dispose();
	}
}
