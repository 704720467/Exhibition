package com.yunfan.exhibition.model;

/**
 * ���ؽ��յ��ĵ�վ��Ϣ���
 * 
 * @author zhang
 *
 */
public class StationResult {


	private EnumStationEvent type = EnumStationEvent.NON;// ���յ����¼�����
	private int arrivalStation = -1;// ��ǰվ���߽�Ҫ��վ
	private int startStation;// ʼ��վ
	private int endStation;// ĩվ
	private String describe;// ����

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
