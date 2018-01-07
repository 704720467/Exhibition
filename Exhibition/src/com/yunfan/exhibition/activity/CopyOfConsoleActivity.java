package com.yunfan.exhibition.activity;

import java.util.ArrayList;

import com.yunfan.exhibition.R;
import com.yunfan.exhibition.model.StationModel;
import com.yunfan.exhibition.uitl.DeviceUtil;
import com.yunfan.exhibition.view.ArrivalTipLayout;
import com.yunfan.exhibition.view.StationView;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class CopyOfConsoleActivity extends Activity {
	private VideoView videoView;
	private String uriStr;
	private Uri uri;

	private RelativeLayout mContentLayout;
	private LinearLayout mStationContentLayout;
	private RelativeLayout mVideoLayour;

	private ArrivalTipLayout mArrivalLayout;
	private TextView mTvLastStation;
	private TextView mTvNowStation;
	private TextView mTvNextStation;

	public ArrayList<StationModel> stationList;
	// public HashMap<String, StationModel> stationListMap;
	public int rectangleWidth = 0;// 长方形宽度
	public int middlePointWidth = 0;// 中间点的宽度
	private int screenWidth = 0;
	public int rightAdWidth = 0;// 左侧广告宽度
	private String TAG = "MainActivity";
	private ArrayList<StationView> stationLayoutLiat;
	private TextView mGetBackData;
	private static int SHOW_ARRIVAL_TIP = 0;
	private static int HIDE_ARRIVAL_TIP = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		// 本地的视频 需要在手机SD卡根目录添加一个 fl1234.mp4 视频
		// String videoUrl1 =
		// Environment.getExternalStorageDirectory().getPath() + "/fl1234.mp4";
		// String videoUrl =
		// "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
		// 网络视频
		// String videoUrl2 = videoUrl;

		mContentLayout = (RelativeLayout) findViewById(R.id.content_layout);
		mStationContentLayout = (LinearLayout) findViewById(R.id.station_content);
		videoView = (VideoView) findViewById(R.id.video);
		mVideoLayour = (RelativeLayout) findViewById(R.id.video_layour);
		mArrivalLayout = (ArrivalTipLayout) findViewById(R.id.arrival_layout);
		mTvLastStation = (TextView) findViewById(R.id.last_station);
		mTvNowStation = (TextView) findViewById(R.id.now_station);
		mTvNextStation = (TextView) findViewById(R.id.next_station);
		mGetBackData = (TextView) findViewById(R.id.get_back_data);
		videoView = (VideoView) this.findViewById(R.id.videoView);

		// initData();
		uriStr = "android.resource://" + getPackageName() + "/" + R.raw.zhui_guang_zhe;
		uri = Uri.parse(uriStr);
	}

	private void initData() {
		screenWidth = DeviceUtil.getScreenWidthSize(this);
		middlePointWidth = DeviceUtil.dp2px(this, 6);
		int size = 20;
		// float s = (screenWidth - (float) screenWidth / 3) -
		// stationList.size() * middlePointWidth;
		float s = (screenWidth - (float) screenWidth / 3) - size * middlePointWidth;

		rectangleWidth = Math.round(s / size / 2);
		rightAdWidth = screenWidth - ((middlePointWidth + rectangleWidth) * size);
		mContentLayout.getLayoutParams().width = ((middlePointWidth + rectangleWidth * 2) * size);
		// videoView.getLayoutParams().width = rightAdWidth;
		// videoView.getLayoutParams().height = 1920;
		Log.e(TAG, "===================>" + screenWidth + ";" + mContentLayout.getLayoutParams().width + ";"
				+ rightAdWidth);
		ViewGroup.LayoutParams lp = mVideoLayour.getLayoutParams();
		lp.width = rightAdWidth;
		mVideoLayour.invalidate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (videoView == null)
			return;
		// 设置视频控制器
		// videoView.setMediaController(new MediaController(this));
		// 播放完成回调
		videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
		// 设置视频路径
		videoView.setVideoURI(uri);
		// 开始播放视频
		videoView.start();
	}

	class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			Toast.makeText(CopyOfConsoleActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
			videoView.start();
		}
	}
}
