package edu.wpi.cs3733.heze.entity;

public class ScheduleTime {
	long hour;
	long minute;
	
	public ScheduleTime(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}
	
	void getHourMinuteFromMilli(long milliseconds) {
		long minutes = milliseconds / 60000;
		this.hour = minutes / 60; 
		this.minute = minutes % 60;
	}
	
	long convertToMilli() {
		long totalMinutes = this.hour * 60 + this.minute;
		return totalMinutes * 60000;
	}
}
