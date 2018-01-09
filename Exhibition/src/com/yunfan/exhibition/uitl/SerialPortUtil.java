package com.yunfan.exhibition.uitl;

import com.yunfan.exhibition.model.EnumStationEvent;
import com.yunfan.exhibition.model.StationResult;

public class SerialPortUtil {
	private static final int SET_START_AND_END_STATION = 1;// ����ʼĩվ
	private static final int SET_PRE_ARRIVAL = 2;// ����ǰ����վ
	private static final int SET_ARRIVAL = 3;// ���õ�վ

	private static final String DATA_START_WITH = "55AA02";

	// public static void main(String[] args) {
	// String s = "55AA029F2000BD";
	// analyticData(s);
	// }

	/**
	 * �����Ӵ��ڽ��յ�������
	 * 
	 * @param strSerialData
	 *            ��������16���Ƶ��ַ���
	 */
	public static StationResult analyticData(String strSerialData) {
		StationResult stationResult = new StationResult();
		if (strSerialData.length() < 7 * 2 || strSerialData.length() > 20 * 2) {
			System.out.println("���ݲ�ȱstrSerialData��" + strSerialData);
			stationResult.setDescribe("���ݲ�ȱstrSerialData��" + strSerialData);
			return stationResult;
		}
		strSerialData = strSerialData.substring(strSerialData.lastIndexOf("55"));
		if (strSerialData == null || strSerialData.length() < 7 * 2 || strSerialData.length() > 20 * 2) {
			System.out.println("���ݲ�ȱstrSerialData��" + strSerialData);
			stationResult.setDescribe("���ݲ�ȱstrSerialData��" + strSerialData);
			return stationResult;
		}

		if (!strSerialData.startsWith(DATA_START_WITH)) {
			System.out.println("���ݸ�ʽ������Ҫ�������55AA02��ʼ!");
			stationResult.setDescribe("���ݸ�ʽ������Ҫ�������55AA02��ʼ!");
			return stationResult;
		}
		int[] dataArray = new int[7];
		boolean success = splittingDataIntoDecimal(dataArray, strSerialData);
		if (!success)
			return null;
		if ((dataArray[2] ^ dataArray[3] ^ dataArray[4] ^ dataArray[5]) != dataArray[6]) {
			System.out.println("����У�鱨����2��3��4��5Ϊ����򲻵������һλ���ݣ���" + strSerialData);
			stationResult.setDescribe("����У�鱨����2��3��4��5Ϊ����򲻵������һλ���ݣ���" + strSerialData);
			return stationResult;
		}

		String back = null;
		if (dataArray[3] == dataArray[4]) {
			back = "��������ʼĩվ��" + dataArray[3] + "=" + dataArray[4] + "\nʼ��վ���Ϊ��" + dataArray[4] + ";�յ�վ���Ϊ��" + dataArray[5];
			stationResult.setType(EnumStationEvent.BeginningAndEnd);
			System.out.println(back);
		} else if ((dataArray[3] >= Integer.parseInt("80", 16)) && (dataArray[3] <= Integer.parseInt("A0", 16))) {
			back = "��������  Ԥ��վ  ��Ϣ��ǰ��  Ԥ��վ  ���Ϊ" + (dataArray[3] - Integer.parseInt("80", 16)) + "\nʼ��վ���Ϊ��" + dataArray[4] + ";�յ�վ���Ϊ��" + dataArray[5];
			stationResult.setType(EnumStationEvent.PreArrival);
			System.out.println(back);
		} else if ((dataArray[3] >= 0) && (dataArray[3] <= Integer.parseInt("20", 16))) {
			back = "��������  ���ε�վ  ��Ϣ��ǰ��  ��վ  ���Ϊ" + dataArray[3] + "\nʼ��վ���Ϊ��" + dataArray[4] + ";�յ�վ���Ϊ��" + dataArray[5];
			stationResult.setType(EnumStationEvent.Arrival);
			System.out.println(back);
		} else {
			System.out.println("��������δƥ�䵽��Ӧ��ѡ�");
			back = "��������δƥ�䵽��Ӧ��ѡ�";
		}
		stationResult.setStartStation(dataArray[4]);
		stationResult.setEndStation(dataArray[5]);
		stationResult.setArrivalStation((dataArray[3] - (stationResult.getType() == EnumStationEvent.Arrival ? 0 : Integer.parseInt("80", 16))));
		stationResult.setDescribe(back);
		return stationResult;

	}

	/**
	 * �������Ϊʮ�������ݣ������ݷ�Ϊ7��
	 * 
	 * @return
	 */
	private static boolean splittingDataIntoDecimal(int[] dataArray, String strSerialData) {
		boolean back = false;
		try {
			for (int i = 0; i < dataArray.length; i++) {
				String str = strSerialData.substring(i * 2, (i + 1) * 2);
				System.out.print("��ȡ��" + i + "�����ݣ�" + str);
				dataArray[i] = Integer.parseInt(str, 16);
				System.out.println("��strת��Ϊʮ���ƣ�" + dataArray[i]);
			}
			back = true;
		} catch (Exception e) {
			back = false;
			System.out.println("�������Ϊʮ�������ݣ������ݷ�Ϊ7��splittingDataIntoDecimal����" + e.getMessage());
		} finally {
			return back;
		}
	}

	/**
	 * ˢ�½�����ͼ��ʾ
	 */
	private static void refreshTheView() {

	}

}
