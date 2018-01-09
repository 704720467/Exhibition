package com.yunfan.exhibition.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunfan.exhibition.R;
import com.yunfan.exhibition.model.EnumStationType;
import com.yunfan.exhibition.model.StationModel;

/**
 * 
 * 站点的布局
 * 
 * @author zp
 * 
 */
public class StationView extends LinearLayout {
	private Context context;
	private LayoutInflater mInflater;

	private View mContentLayour;
	private View mTopLeft;
	private View mTopMiddle;
	private View mTopRight;

	private TextView mTvStationInfo;
	private TextView mTvStationRemarks;
	private int rectangleWidth = 0;
	private int topMiddleWidth = 0;
	private StationModel stationModel;

	public StationView(Context context) {
		super(context);
	}

	public StationView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StationView(Context context, StationModel stationModel, int rectangleWidth, int topMiddleWidth) {
		super(context);
		this.context = context;
		this.stationModel = stationModel;
		this.rectangleWidth = rectangleWidth;
		this.topMiddleWidth = topMiddleWidth;
		initView();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		mInflater = (LayoutInflater) LayoutInflater.from(context);

		mContentLayour = (LinearLayout) mInflater.inflate(R.layout.item_station_layout, null);

		mTopLeft = mContentLayour.findViewById(R.id.top_left);
		mTopMiddle = mContentLayour.findViewById(R.id.top_middle);
		mTopRight = mContentLayour.findViewById(R.id.top_right);
		mTvStationInfo = (TextView) mContentLayour.findViewById(R.id.station_information);
		mTvStationRemarks = (TextView) mContentLayour.findViewById(R.id.station_remarks);

		mTopLeft.getLayoutParams().width = rectangleWidth;
		mTopMiddle.getLayoutParams().width = topMiddleWidth;
		mTopMiddle.getLayoutParams().height = topMiddleWidth;
		mTopMiddle.setBackgroundDrawable(getResources().getDrawable((stationModel.getArrivalState() == EnumStationType.Arrival) ? (R.drawable.bt_red_tv) : (R.drawable.bt_tv)));
		mTopRight.getLayoutParams().width = rectangleWidth;

		Log.e("", "===================>||||" + mTopLeft.getLayoutParams().width + ";" + mTopRight.getLayoutParams().width);

		mTvStationInfo.setTextColor(Color.parseColor((stationModel.getArrivalState() == EnumStationType.Arrival) ? "#ff0000" : "#000000"));
		mTvStationRemarks.setTextColor(Color.parseColor((stationModel.getArrivalState() == EnumStationType.Arrival) ? "#ff0000" : "#000000"));
		ViewGroup.LayoutParams lpLayoutParams = new ViewGroup.LayoutParams(22, ViewGroup.LayoutParams.MATCH_PARENT);
		mContentLayour.setLayoutParams(lpLayoutParams);

		mTvStationInfo.setText(stationModel.getStationName());
		if (!TextUtils.isEmpty(stationModel.getRemarks())) {
			mTvStationRemarks.setText(stationModel.getRemarks());
			mTvStationRemarks.setVisibility(View.VISIBLE);
		}

		addView(mContentLayour);
	}

	/**
	 * 呼吸效果
	 * 
	 * @param v
	 */
	public void startBreath() {
		if (mTopMiddle == null)
			return;
		ScaleAnimation scaleAnim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnim.setRepeatCount(Animation.INFINITE);
		scaleAnim.setRepeatMode(Animation.REVERSE);
		scaleAnim.setDuration(600);
		scaleAnim.setFillBefore(true);
		mTopMiddle.startAnimation(scaleAnim);
	}

	/**
	 * 呼吸效果
	 * 
	 * @param v
	 */
	public void endBreath() {
		if (mTopMiddle != null)
			mTopMiddle.clearAnimation();
	}

	public void setmTvStationInfoColor() {
		if (mTvStationInfo != null)
			mTvStationInfo.setTextColor(Color.parseColor("#ee5514"));
	}

}
