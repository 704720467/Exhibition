package com.yunfan.exhibition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO start your own service
		Toast.makeText(context, "���յ����������㲥", Toast.LENGTH_LONG).show();
		 //����Ӧ�ã�����Ϊ��Ҫ�Զ�������Ӧ�õİ���   
		Intent intent1 = context.getPackageManager().getLaunchIntentForPackage("com.yunfan.exhibition");
		context.startActivity(intent1);
	}

}
