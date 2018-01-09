package com.yunfan.exhibition.activity;

import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import android.app.Activity;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.yunfan.exhibition.R;
import com.yunfan.exhibition.model.EnumStationEvent;
import com.yunfan.exhibition.model.EnumStationType;
import com.yunfan.exhibition.model.StationModel;
import com.yunfan.exhibition.uitl.DeviceUtil;
import com.yunfan.exhibition.uitl.MyTextViewUtil;
import com.yunfan.exhibition.view.ArrivalTipLayout;
import com.yunfan.exhibition.view.StationView;

public class MainActivity22 extends Activity {
	private RelativeLayout mContentLayout;
	private LinearLayout mStationContentLayout;
	private FrameLayout mVideoLayour;
	private VideoView videoView;

	private ArrivalTipLayout mArrivalLayout;
	private TextView mTvLastStation;
	private TextView mTvNowStation;
	private TextView mTvNextStation;

	public ArrayList<StationModel> stationList;
	// public HashMap<String, StationModel> stationListMap;
	public int rectangleWidth = 0;// �����ο���
	public int middlePointWidth = 0;// �м��Ŀ���
	private int screenWidth = 0;
	public int rightAdWidth = 0;// ��������
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
		new ExcelDataLoader().execute("lightRailStationName.xls");
	}

	/**
	 * ��ȡ excel �����е�����,���������߳��е���
	 * 
	 * @param xlsName
	 *            excel ���������
	 * @param index
	 *            �ڼ��ű����е�����
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

	// ���첽������ ����
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

			screenWidth = DeviceUtil.getScreenWidthSize(com.yunfan.exhibition.activity.Application.getAppContext()) * 2;
			rightAdWidth = screenWidth / 3;
			middlePointWidth = DeviceUtil.dp2px(MainActivity22.this, 6);
			float allRectangleWidth = (screenWidth - rightAdWidth) - stationList.size() * middlePointWidth;
			rectangleWidth = Math.round(allRectangleWidth / stationList.size() / 2);
			mContentLayout.getLayoutParams().width = screenWidth - rightAdWidth;
			int ss = (rectangleWidth * 2 + middlePointWidth) * stationList.size();
			Log.e(TAG, "===================>" + mContentLayout.getLayoutParams().width + ";" + rightAdWidth + ";stationList.size()=" + stationList.size() + ";;ss=" + ss);
			ViewGroup.LayoutParams lp = mVideoLayour.getLayoutParams();
			lp.width = rightAdWidth;
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
			stationView = new StationView(this, stationList.get(i), rectangleWidth, middlePointWidth);
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
			boolean isArrival = (positiveSequence && stationModel.getStationNumber() < mainPosition)// ���������£����С��Ԥ��վ�����Ѿ���վ
					|| (!positiveSequence && stationModel.getStationNumber() > mainPosition);// ���������£���Ŵ���Ԥ��վ�����Ѿ���վ
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

	@Override
	protected void onResume() {
		super.onResume();
		if (videoView == null)
			return;
		String uriStr = "android.resource://" + getPackageName() + "/" + R.raw.zhui_guang_zhe;
		Uri uri = Uri.parse(uriStr);
		// ������Ƶ������
		// videoView.setMediaController(new MediaController(this));
		// ������ɻص�
		videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
		// ������Ƶ·��
		videoView.setVideoURI(uri);
		// ��ʼ������Ƶ
		videoView.start();
	}

	class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			Toast.makeText(MainActivity22.this, "���������", Toast.LENGTH_SHORT).show();
			videoView.start();
		}
	}
}