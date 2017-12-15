package com.yunfan.exhibition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO start your own service
		Toast.makeText(context, "接收到开机启动广播", Toast.LENGTH_LONG).show();
		 //启动应用，参数为需要自动启动的应用的包名   
		Intent intent1 = context.getPackageManager().getLaunchIntentForPackage("com.yunfan.exhibition");
		context.startActivity(intent1);
	}

}
