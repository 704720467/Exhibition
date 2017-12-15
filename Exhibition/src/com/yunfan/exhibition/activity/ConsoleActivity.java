package com.yunfan.exhibition.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yunfan.exhibition.R;
import com.yunfan.exhibition.uitl.CHexConverUtil;

public class ConsoleActivity extends SerialPortActivity {

    EditText mReception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.console);
        mReception = (EditText) findViewById(R.id.EditTextReception);

		findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {


			}
		});
    }

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			public void run() {
				// 异或： int f = a ^ b ^ c ^ d;
				if (mReception != null) {

					// mReception.append(CHexConverUtil.hexStr2Str(new String(
					// buffer, 0, size)));
					// mReception.append(new String(buff));
					String m = "数组长度=" + size + "/n";
					String m1 = "直接转为字符串=" + new String(buffer, 0, size) + "/n";
					String m2 = "方法一：hexStr2Str=" + CHexConverUtil.hexStr2Str(new String(buffer, 0, size)) + "/n";
					String m3 = "方法二：bytesToHexString=" + CHexConverUtil.bytesToHexString(buffer, size) + "/n";
					byte[] hex = "0123456789ABCDEF".getBytes();
					byte[] buff = new byte[2 * size];
					for (int i = 0; i < size; i++) {
						buff[2 * i] = hex[(buffer[i] >> 4) & 0x0f];
						buff[2 * i + 1] = hex[buffer[i] & 0x0f];
					}
					String m4 = "方法三：bytesToHexString=" + new String(buff);
					tipAlert(m + m1 + m2 + m3 + m4);
					mReception.append(m + m1 + m2 + m3 + m4);
				}
			}



			private void tipAlert(String message) {
				final AlertDialog.Builder b = new AlertDialog.Builder(
						ConsoleActivity.this);
				b.setTitle("提示：");
				b.setMessage(message);
				b.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				b.show();
			}
		});
	}
}
