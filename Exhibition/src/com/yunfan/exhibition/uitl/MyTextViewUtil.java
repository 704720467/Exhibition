package com.yunfan.exhibition.uitl;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * ¥¶¿ÌTextView∞Ô÷˙¿‡
 * 
 * @author admin
 *
 */
public class MyTextViewUtil {
	
	public static void setStringForTextView(TextView textView, String text) {
		if (textView == null)
			return;
		textView.setText(TextUtils.isEmpty(text) ? "" : text);
	}

}
