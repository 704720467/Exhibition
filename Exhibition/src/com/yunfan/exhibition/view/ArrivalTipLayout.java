/**
 * 
 */
package com.yunfan.exhibition.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunfan.exhibition.R;
import com.yunfan.exhibition.model.StationModel;

/**
 * @author zp
 *
 */
public class ArrivalTipLayout extends LinearLayout {

	private LayoutInflater mInflater;
	private View mContentLayour;
	private View mLastStationLayout;
	private View mNextStationLayout;
	private TextView mTvLastStation;
	private TextView mTvNowStation;
	private TextView mTvNextStation;

	public ArrivalTipLayout(Context context) {
		super(context);
		initView();
	}

	public ArrivalTipLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ArrivalTipLayout(Context context, StationModel stationModel, int rectangleWidth, int topMiddleWidth) {
		super(context);
		initView();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		mInflater = (LayoutInflater) LayoutInflater.from(getContext());
		mContentLayour = (LinearLayout) mInflater.inflate(R.layout.arrival_tips_layout, null);
		mLastStationLayout = mContentLayour.findViewById(R.id.last_station_layout);
		mNextStationLayout = mContentLayour.findViewById(R.id.next_station_layout);
		mTvLastStation = (TextView) mContentLayour.findViewById(R.id.last_station);
		mTvNowStation = (TextView) mContentLayour.findViewById(R.id.now_station);
		mTvNextStation = (TextView) mContentLayour.findViewById(R.id.next_station);
		addView(mContentLayour);
	}

	/**
	 * 设置到站信息
	 * 
	 * @param lastStationName
	 *            上一站站名
	 * @param nowStationName
	 *            当前站站名
	 * @param nextStationName
	 *            下一站站名
	 */
	public void setStationInfor(String lastStationName, String nowStationName, String nextStationName) {
		if (mLastStationLayout != null)
			mLastStationLayout.setVisibility(TextUtils.isEmpty(lastStationName) ? INVISIBLE : VISIBLE);
		if (mTvLastStation != null && !TextUtils.isEmpty(lastStationName))
			mTvLastStation.setText(lastStationName);

		if (mTvNowStation != null && !TextUtils.isEmpty(nowStationName))
			mTvNowStation.setText(nowStationName);

		if (mNextStationLayout != null)
			mNextStationLayout.setVisibility(TextUtils.isEmpty(nextStationName) ? INVISIBLE : VISIBLE);
		if (mTvNextStation != null && !TextUtils.isEmpty(nextStationName))
			mTvNextStation.setText(nextStationName);

	}

}
