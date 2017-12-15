/**
 * 
 */
package com.yunfan.exhibition.model;

/**
 * 事件类型
 * 
 * @author zhang
 *
 */
public enum EnumStationEvent {
	NON, // 没有任何有效事件
	BeginningAndEnd, // 设置始末站事件
	PreArrival, // 设置预到站事件
	Arrival;// 设置到站事件
}
