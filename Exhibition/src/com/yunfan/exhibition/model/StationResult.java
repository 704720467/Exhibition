package com.yunfan.exhibition.model;

/**
 * 返回接收到的到站信息结果
 * 
 * @author zhang
 *
 */
public class StationResult {


	private EnumStationEvent type = EnumStationEvent.NON;// 接收到的事件类型
	private int arrivalStation = -1;// 当前站或者将要到站
	private int startStation;// 始发站
	private int endStation;// 末站
	private String describe;// 描述

	public EnumStationEvent getType() {
		return type;
	}

	public void setType(EnumStationEvent type) {
		this.type = type;
	}

	public int getArrivalStation() {
		return arrivalStation;
	}

	public void setArrivalStation(int arrivalStation) {
		this.arrivalStation = arrivalStation;
	}

	public int getStartStation() {
		return startStation;
	}

	public void setStartStation(int startStation) {
		this.startStation = startStation;
	}

	public int getEndStation() {
		return endStation;
	}

	public void setEndStation(int endStation) {
		this.endStation = endStation;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

}
