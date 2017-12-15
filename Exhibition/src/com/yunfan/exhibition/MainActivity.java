package com.yunfan.exhibition;

import java.util.ArrayList;
import java.util.Collections;

import jxl.Sheet;
import jxl.Workbook;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.yunfan.exhibition.activity.SerialPortActivity;
import com.yunfan.exhibition.model.EnumStationEvent;
import com.yunfan.exhibition.model.EnumStationType;
import com.yunfan.exhibition.model.StationModel;
import com.yunfan.exhibition.model.StationResult;
import com.yunfan.exhibition.uitl.DeviceUtil;
import com.yunfan.exhibition.uitl.SerialPortUtil;
import com.yunfan.exhibition.view.ArrivalTipLayout;
import com.yunfan.exhibition.view.StationView;

public class MainActivity extends SerialPortActivity {
	private RelativeLayout mContentLayout;
	private LinearLayout mStationContentLayout;
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
		setContentView(R.layout.activity_main);
		mContentLayout = (RelativeLayout) findViewById(R.id.content_layout);
		mStationContentLayout = (LinearLayout) findViewById(R.id.station_content);
		videoView = (VideoView) findViewById(R.id.video);
		mArrivalLayout = (ArrivalTipLayout) findViewById(R.id.arrival_layout);
		mTvLastStation = (TextView) findViewById(R.id.last_station);
		mTvNowStation = (TextView) findViewById(R.id.now_station);
		mTvNextStation = (TextView) findViewById(R.id.next_station);
		mGetBackData = (TextView) findViewById(R.id.get_back_data);
		new ExcelDataLoader().execute("lightRailStationName2.xls");
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
			//
			// Log.d(TAG, "the num of sheets is " + sheetNum);
			// Log.d(TAG, "the name of sheet is  " + sheet.getName());
			// Log.d(TAG, "total rows is ��=" + sheetRows);
			// Log.d(TAG, "total cols is ��=" + sheetColumns);

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
			// progressDialog.setMessage("������,���Ժ�......");
			// progressDialog.setCanceledOnTouchOutside(false);
			// progressDialog.show();
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
				// if (stationListMap == null)
				// stationListMap = new HashMap<String, StationModel>();
				// stationListMap.clear();
				stationList.clear();
				for (int i = 0; i < countryModels.size(); i++) {
					Log.e(TAG, countryModels.get(i).toString() + "\n");
					stationList.add(countryModels.get(i));
					// stationListMap.put(countryModels.get(i).getStationNumber(),
					// countryModels.get(i));
				}
				initData();
			}
		}

		private void initData() {
			screenWidth = DeviceUtil.getScreenWidthSize(MainActivity.this);
			middlePointWidth = DeviceUtil.dp2px(MainActivity.this, 6);
			float s = (screenWidth - (float) screenWidth / 3) - stationList.size() * middlePointWidth;

			rectangleWidth = Math.round(s / stationList.size() / 2);
			rightAdWidth = screenWidth - ((middlePointWidth + rectangleWidth) * stationList.size());
			mContentLayout.getLayoutParams().width = ((middlePointWidth + rectangleWidth * 2) * stationList.size());
			videoView.getLayoutParams().width = rightAdWidth;
			// stationList.get(0).setArrivalState(1);
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

		boolean positiveSequence = (stationList.get(0).getStationNumber() < stationList.get(stationList.size() - 1).getStationNumber());
		stationList.get(mainPosition).setArrivalState(event == EnumStationEvent.PreArrival ? EnumStationType.PreArrival : EnumStationType.Arrival);
		for (StationModel stationModel : stationList) {
			boolean isArrival = (positiveSequence && stationModel.getStationNumber() < mainPosition)// ���������£����С��Ԥ��վ�����Ѿ���վ
					|| (!positiveSequence && stationModel.getStationNumber() > mainPosition);// ���������£���Ŵ���Ԥ��վ�����Ѿ���վ
			stationModel.setArrivalState(isArrival ? EnumStationType.Arrival : EnumStationType.NoArrival);
		}
	}

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		byte[] hex = "0123456789ABCDEF".getBytes();
		byte[] buff = new byte[2 * size];
		for (int i = 0; i < size; i++) {
			buff[2 * i] = hex[(buffer[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[buffer[i] & 0x0f];
		}
		final StationResult stationResult = SerialPortUtil.analyticData(new String(buff));
		runOnUiThread(new Runnable() {
			public void run() {
				if (stationResult != null) {


					if (stationResult.getType() == EnumStationEvent.NON)
						return;

					int mainPosition = stationResult.getArrivalStation();

					if (stationResult.getType() == EnumStationEvent.BeginningAndEnd) {
						if (stationList.get(0).getStationNumber() != stationResult.getStartStation())
							Collections.reverse(stationList);
					}
					if (stationResult.getType() == EnumStationEvent.PreArrival) {
						if (stationList.size() <= mainPosition)
							return;
					}
					if (stationResult.getType() == EnumStationEvent.Arrival) {
						if (stationList.size() <= mainPosition)
							return;
						handler.sendMessage(handler.obtainMessage());
						String lastStationName=(stationResult.getArrivalStation()==0)?null:stationList.get(stationResult.getArrivalStation()-1).getStationName();
						String nowStationName=stationList.get(stationResult.getArrivalStation()).getStationName();
						String nextStationName=(stationResult.getArrivalStation()==stationList.size()-1)?null:stationList.get(stationResult.getArrivalStation()+1).getStationName();
						mArrivalLayout.setStationInfor(lastStationName, nowStationName, nextStationName);
					}
					initData(mainPosition, stationResult.getType());
					addChileView();
				}
			}
		});
	}
}