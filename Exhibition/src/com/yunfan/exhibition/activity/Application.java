package com.yunfan.exhibition.activity;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

import com.yunfan.exhibition.uitl.Constent;

public class Application extends android.app.Application {

	public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
	private SerialPort mSerialPort = null;
	private static Application app;
	public String TAG = "Application";

	public synchronized static Application getAppContext() {
		return app;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
	}

	public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null)
			mSerialPort = new SerialPort(new File(Constent.PATH), Constent.BAUDRATE, 0);
		return mSerialPort;
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
}
