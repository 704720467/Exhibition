package com.yunfan.exhibition.model;

public class StationModel {
	private int stationNumber;// ÐòºÅ
	private String stationName;// Õ¾Ãû
	private String remarks;// ±¸×¢
	private EnumStationType arrivalState = EnumStationType.NoArrival;

	public int getStationNumber() {
		return stationNumber;
	}

	public void setStationNumber(int stationNumber) {
		this.stationNumber = stationNumber;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public EnumStationType getArrivalState() {
		return arrivalState;
	}

	public void setArrivalState(EnumStationType arrivalState) {
		this.arrivalState = arrivalState;
	}

	@Override
	public String toString() {
		return "StationModel [stationNumber=" + stationNumber + ", stationName=" + stationName + ", remarks=" + remarks + "]";
	}

}
