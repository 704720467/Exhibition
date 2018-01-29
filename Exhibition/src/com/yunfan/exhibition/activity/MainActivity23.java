package com.yunfan.exhibition.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yunfan.exhibition.R;
import com.yunfan.exhibition.TestDemo;

public class MainActivity23 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main23);
		findViewById(R.id.ceshi).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String s = "55AA029F2000BD";
				String s1 = "C8C810021255AA029F2000BD";
				String s2 = "55AA029F2000BDC8C8100212";
				String s3 = "7E030506EF00999999";
				TestDemo.analyticData(s3);
			}
		});
	}
}
