package com.yunfan.exhibition;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import jxl.Sheet;
import jxl.Workbook;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.yunfan.exhibition.activity.SerialPortActivity;
import com.yunfan.exhibition.model.EnumStationEvent;
import com.yunfan.exhibition.model.EnumStationType;
import com.yunfan.exhibition.model.StationModel;
import com.yunfan.exhibition.model.StationResult;
import com.yunfan.exhibition.uitl.DeviceUtil;
import com.yunfan.exhibition.uitl.MyTextViewUtil;
import com.yunfan.exhibition.uitl.SerialPortUtil;
import com.yunfan.exhibition.view.ArrivalTipLayout;
import com.yunfan.exhibition.view.StationView;

public class MainActivity extends SerialPortActivity {
	private RelativeLayout mContentLayout;
	private LinearLayout mStationContentLayout;
	private FrameLayout mVideoLayour;
	private VideoView videoView;

	private ArrivalTipLayout mArrivalLayout;
	private TextView mTvLastStation;
	private TextView mTvNowStation;
	private TextView mTvNextStation;
	private TextView mTvDataTv;

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
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mArrivalLayout == null)
				return;
			if (mArrivalLayout.getVisibility() != View.VISIBLE) {
				handler.removeMessages(SHOW_ARRIVAL_TIP);
				handler.sendMessageDelayed(handler.obtainMessage(SHOW_ARRIVAL_TIP), 20 * 1000);
			}
			mArrivalLayout.setVisibility((mArrivalLayout.getVisibility() != View.VISIBLE) ? View.VISIBLE : View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		mContentLayout = (RelativeLayout) findViewById(R.id.content_layout);
		mStationContentLayout = (LinearLayout) findViewById(R.id.station_content);
		videoView = (VideoView) findViewById(R.id.video);
		mVideoLayour = (FrameLayout) findViewById(R.id.video_layour);
		mArrivalLayout = (ArrivalTipLayout) findViewById(R.id.arrival_layout);
		mTvLastStation = (TextView) findViewById(R.id.last_station);
		mTvNowStation = (TextView) findViewById(R.id.now_station);
		mTvNextStation = (TextView) findViewById(R.id.next_station);
		mGetBackData = (TextView) findViewById(R.id.get_back_data);
		// mTvDataTv = (TextView) findViewById(R.id.dataTv);
		new ExcelDataLoader().execute("lightRailStationName.xls");
	}

	/**
	 * 获取 excel 表格中的数据,不能在主线程中调用
	 * 
	 * @param xlsName
	 *            excel 表格的名称
	 * @param index
	 *            第几张表格中的数据
	 */
	private ArrayList<StationModel> getXlsData(String xlsName, int index) {
		ArrayList<StationModel> countryList = new ArrayList<StationModel>();
		AssetManager assetManager = getAssets();

		try {
			Workbook workbook = Workbook.getWorkbook(assetManager.open(xlsName));
			Sheet sheet = workbook.getSheet(index);

			int sheetNum = workbook.getNumberOfSheets();
			int sheetRows = sheet.getRows();
			int sheetColumns = sheet.getColumns();
			for (int i = 1; i < sheetRows; i++) {
				StationModel StationModel = new StationModel();
				StationModel.setStationNumber(Integer.parseInt(sheet.getCell(0, i).getContents()));
				StationModel.setStationName(sheet.getCell(1, i).getContents());
				StationModel.setRemarks(sheet.getCell(2, i).getContents());
				countryList.add(StationModel);
			}

			workbook.close();

		} catch (Exception e) {
			Log.e(TAG, "read error=" + e, e);
		}

		return countryList;
	}

	// 在异步方法中 调用
	public class ExcelDataLoader extends AsyncTask<String, Void, ArrayList<StationModel>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected ArrayList<StationModel> doInBackground(String... params) {
			return getXlsData(params[0], 0);
		}

		@Override
		protected void onPostExecute(ArrayList<StationModel> countryModels) {
			if (countryModels != null && countryModels.size() > 0) {
				if (stationList == null)
					stationList = new ArrayList<StationModel>();
				stationList.clear();
				for (int i = 0; i < countryModels.size(); i++) {
					Log.e(TAG, countryModels.get(i).toString() + "\n");
					stationList.add(countryModels.get(i));
				}
				initData();
			}
		}

		private void initData() {
			screenWidth = DeviceUtil.getScreenWidthSize(MainActivity.this) * 2;
			rightAdWidth = screenWidth / 3;
			middlePointWidth = DeviceUtil.dp2px(MainActivity.this, 6);
			float allRectangleWidth = (screenWidth - rightAdWidth) - stationList.size() * middlePointWidth;
			rectangleWidth = Math.round(allRectangleWidth / stationList.size() / 2);
			mContentLayout.getLayoutParams().width = (rectangleWidth * 2 + middlePointWidth) * stationList.size();
			rightAdWidth = screenWidth - mContentLayout.getLayoutParams().width;
			Log.e(TAG, "===================>MainActivity" + mContentLayout.getLayoutParams().width + ";" + rightAdWidth + ";stationList.size()=" + stationList.size());
			mVideoLayour.getLayoutParams().width = rightAdWidth;

			MyTextViewUtil.setStringForTextView((TextView) findViewById(R.id.start_station), stationList.get(0).getStationName());
			MyTextViewUtil.setStringForTextView((TextView) findViewById(R.id.end_station), stationList.get(stationList.size() - 1).getStationName());
			MyTextViewUtil.setStringForTextView((TextView) findViewById(R.id.next_station), stationList.get(1).getStationName());
			addChileView();
		}
	}

	private void addChileView() {
		if (stationLayoutLiat == null)
			stationLayoutLiat = new ArrayList<StationView>();
		stationLayoutLiat.clear();
		mStationContentLayout.removeAllViews();
		for (int i = 0; i < stationList.size(); i++) {
			StationView stationView;
			stationView = new StationView(this, stationList.get(i), (screenWidth / stationList.size()));
			if (stationList.get(i).getArrivalState() == EnumStationType.PreArrival)
				stationView.startBreath();
			mStationContentLayout.addView(stationView);
			stationLayoutLiat.add(stationView);
		}
	}

	private void initData(int mainPosition, EnumStationEvent event) {

		if (event == EnumStationEvent.BeginningAndEnd) {
			for (StationModel stationModel : stationList)
				stationModel.setArrivalState(EnumStationType.NoArrival);
			return;
		}

		boolean positiveSequence = (stationList.get(0).getStationNumber()) < (stationList.get(stationList.size() - 1).getStationNumber());
		for (StationModel stationModel : stationList) {
			boolean isArrival = (positiveSequence && stationModel.getStationNumber() < mainPosition)// 正序的情况下，序号小于预到站代表已经到站
					|| (!positiveSequence && stationModel.getStationNumber() > mainPosition);// 倒叙的情况下，序号大于预到站代表已经到站
			stationModel.setArrivalState(isArrival ? EnumStationType.Arrival : EnumStationType.NoArrival);
		}
		mainPosition = positiveSequence ? mainPosition : (stationList.size() - 1 - mainPosition);
		stationList.get(mainPosition).setArrivalState((event == EnumStationEvent.PreArrival ? EnumStationType.PreArrival : EnumStationType.Arrival));
		if (event == EnumStationEvent.Arrival) {
			handler.sendMessage(handler.obtainMessage());
			String lastStationName = (mainPosition == 0) ? null : stationList.get(mainPosition - 1).getStationName();
			String nowStationName = stationList.get(mainPosition).getStationName();
			String nextStationName = (mainPosition == stationList.size() - 1) ? null : stationList.get(mainPosition + 1).getStationName();
			mArrivalLayout.setStationInfor(lastStationName, nowStationName, nextStationName);
		}
		if (event == EnumStationEvent.PreArrival) {
			MyTextViewUtil.setStringForTextView(mTvNextStation, stationList.get(mainPosition).getStationName());
		}
	}

	StringBuffer stringBuffer = new StringBuffer("接收到的数据，还没有进行处理：\n");
	boolean showData = true;

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		byte[] hex = "0123456789ABCDEF".getBytes();
		byte[] buff = new byte[2 * size];
		for (int i = 0; i < size; i++) {
			buff[2 * i] = hex[(buffer[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[buffer[i] & 0x0f];
		}
		// try {
		// if (showData) {
		// stringBuffer.append("\n" + new String(buff));
		// mTvDataTv.setText(stringBuffer);
		// }
		// } catch (Exception e) {
		// stringBuffer.append("\n数据拼接报错");
		// mTvDataTv.setText(stringBuffer);
		// }
		// if (showData)
		// return;
		final StationResult stationResult = SerialPortUtil.analyticData(new String(buff));

		runOnUiThread(new Runnable() {
			public void run() {
				if (stationResult != null) {
					if (stationResult.getType() == EnumStationEvent.NON)
						return;

					int mainPosition = stationResult.getArrivalStation();
					if (stationList.get(0).getStationNumber() != stationResult.getStartStation())
						Collections.reverse(stationList);

					// if (stationResult.getType() ==
					// EnumStationEvent.BeginningAndEnd) {
					MyTextViewUtil.setStringForTextView((TextView) findViewById(R.id.start_station), stationList.get(0).getStationName());
					MyTextViewUtil.setStringForTextView((TextView) findViewById(R.id.end_station), stationList.get(stationList.size() - 1).getStationName());
					int nextStationPostion = ((stationList.size() - 1) == mainPosition) ? mainPosition : mainPosition + 1;
					MyTextViewUtil.setStringForTextView((TextView) findViewById(R.id.next_station), stationList.get(nextStationPostion).getStationName());
					// }
					if (stationResult.getType() == EnumStationEvent.PreArrival) {
						if (stationList.size() <= mainPosition)
							return;
					}
					if (stationResult.getType() == EnumStationEvent.Arrival) {
						if (stationList.size() <= mainPosition)
							return;
					}

					try {
						initData(mainPosition, stationResult.getType());
					} catch (Exception e) {
						toShowDialog("整理数据报错：" + e.getMessage());
					}

					try {
						addChileView();
					} catch (Exception e) {
						toShowDialog("绘制布局出错：" + e.getMessage());
					}

				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (videoView == null)
			return;
		playVideo();
	}

	public void playVideo() {
		File file = new File("storage/emulated/0/DCIM/ad.mp4");
		if (file.exists()) {
			videoView.setVideoPath(file.getAbsolutePath());
			videoView.requestFocus();
			try {
				videoView.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {
					Toast.makeText(MainActivity.this, "视频播放完毕", Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			Toast.makeText(MainActivity.this, "要播放的视屏文件不存在", Toast.LENGTH_SHORT).show();
		}

		// String uriStr = "android.resource://" + getPackageName() + "/" +
		// R.raw.zhui_guang_zhe;
		// Uri uri = Uri.parse(uriStr);
		// // 设置视频控制器
		// // videoView.setMediaController(new MediaController(this));
		// // 播放完成回调
		// videoView.setOnCompletionListener(new
		// MyPlayerOnCompletionListener());
		// // 设置视频路径
		// videoView.setVideoURI(uri);
		// // 开始播放视频
		// videoView.start();

	}

	class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			Toast.makeText(MainActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
			videoView.start();
		}
	}

	public void toShowDialog(String tip) {
		new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")// 设置对话框标题

				.setMessage(tip)// 设置显示的内容

				.setPositiveButton("确定", new DialogInterface.OnClickListener() {// 添加确定按钮

							@Override
							public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件
								// dismiss();
							}

						}).setNegativeButton("返回", new DialogInterface.OnClickListener() {// 添加返回按钮

							@Override
							public void onClick(DialogInterface dialog, int which) {// 响应事件

							}

						}).show();// 在按键响应事件中显示此对话框
	}
}
