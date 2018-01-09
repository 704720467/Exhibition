package com.yunfan.exhibition.uitl;

import com.yunfan.exhibition.model.EnumStationEvent;
import com.yunfan.exhibition.model.StationResult;

public class SerialPortUtil {
	private static final int SET_START_AND_END_STATION = 1;// 设置始末站
	private static final int SET_PRE_ARRIVAL = 2;// 设置前方到站
	private static final int SET_ARRIVAL = 3;// 设置到站

	private static final String DATA_START_WITH = "55AA02";

	// public static void main(String[] args) {
	// String s = "55AA029F2000BD";
	// analyticData(s);
	// }

	/**
	 * 解析从串口接收到的数据
	 * 
	 * @param strSerialData
	 *            串口数据16进制的字符串
	 */
	public static StationResult analyticData(String strSerialData) {
		StationResult stationResult = new StationResult();
		if (strSerialData.length() < 7 * 2 || strSerialData.length() > 20 * 2) {
			System.out.println("数据残缺strSerialData：" + strSerialData);
			stationResult.setDescribe("数据残缺strSerialData：" + strSerialData);
			return stationResult;
		}
		strSerialData = strSerialData.substring(strSerialData.lastIndexOf("55"));
		if (strSerialData == null || strSerialData.length() < 7 * 2 || strSerialData.length() > 20 * 2) {
			System.out.println("数据残缺strSerialData：" + strSerialData);
			stationResult.setDescribe("数据残缺strSerialData：" + strSerialData);
			return stationResult;
		}

		if (!strSerialData.startsWith(DATA_START_WITH)) {
			System.out.println("数据格式不符合要求必须以55AA02开始!");
			stationResult.setDescribe("数据格式不符合要求必须以55AA02开始!");
			return stationResult;
		}
		int[] dataArray = new int[7];
		boolean success = splittingDataIntoDecimal(dataArray, strSerialData);
		if (!success)
			return null;
		if ((dataArray[2] ^ dataArray[3] ^ dataArray[4] ^ dataArray[5]) != dataArray[6]) {
			System.out.println("数据校验报错（第2、3、4、5为的异或不等于最后一位数据）！" + strSerialData);
			stationResult.setDescribe("数据校验报错（第2、3、4、5为的异或不等于最后一位数据）！" + strSerialData);
			return stationResult;
		}

		String back = null;
		if (dataArray[3] == dataArray[4]) {
			back = "本次设置始末站：" + dataArray[3] + "=" + dataArray[4] + "\n始发站序号为：" + dataArray[4] + ";终点站序号为：" + dataArray[5];
			stationResult.setType(EnumStationEvent.BeginningAndEnd);
			System.out.println(back);
		} else if ((dataArray[3] >= Integer.parseInt("80", 16)) && (dataArray[3] <= Integer.parseInt("A0", 16))) {
			back = "本次设置  预到站  信息：前方  预到站  序号为" + (dataArray[3] - Integer.parseInt("80", 16)) + "\n始发站序号为：" + dataArray[4] + ";终点站序号为：" + dataArray[5];
			stationResult.setType(EnumStationEvent.PreArrival);
			System.out.println(back);
		} else if ((dataArray[3] >= 0) && (dataArray[3] <= Integer.parseInt("20", 16))) {
			back = "本次设置  本次到站  信息：前方  到站  序号为" + dataArray[3] + "\n始发站序号为：" + dataArray[4] + ";终点站序号为：" + dataArray[5];
			stationResult.setType(EnumStationEvent.Arrival);
			System.out.println(back);
		} else {
			System.out.println("本次设置未匹配到对应的选项！");
			back = "本次设置未匹配到对应的选项！";
		}
		stationResult.setStartStation(dataArray[4]);
		stationResult.setEndStation(dataArray[5]);
		stationResult.setArrivalStation((dataArray[3] - (stationResult.getType() == EnumStationEvent.Arrival ? 0 : Integer.parseInt("80", 16))));
		stationResult.setDescribe(back);
		return stationResult;

	}

	/**
	 * 拆分数据为十进制数据，将数据分为7组
	 * 
	 * @return
	 */
	private static boolean splittingDataIntoDecimal(int[] dataArray, String strSerialData) {
		boolean back = false;
		try {
			for (int i = 0; i < dataArray.length; i++) {
				String str = strSerialData.substring(i * 2, (i + 1) * 2);
				System.out.print("提取第" + i + "组数据：" + str);
				dataArray[i] = Integer.parseInt(str, 16);
				System.out.println("将str转换为十进制：" + dataArray[i]);
			}
			back = true;
		} catch (Exception e) {
			back = false;
			System.out.println("拆分数据为十进制数据，将数据分为7组splittingDataIntoDecimal报错：" + e.getMessage());
		} finally {
			return back;
		}
	}

	/**
	 * 刷新界面视图显示
	 */
	private static void refreshTheView() {

	}

}
