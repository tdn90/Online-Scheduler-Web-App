package edu.wpi.cs3733.heze.lambda.api;

public class GetOpenSlotsRequest {
	
	public String scheduleID;
	public int month;
	public int year;
	public int day_of_week; //Monday = 2
	public int day_of_month;
	public long startTime;
	
	public GetOpenSlotsRequest(String scheduleID, int month, int year, int day_of_week, int day_of_month, long startTime) {
		this.scheduleID = scheduleID;
		this.month = month;
		this.year = year;
		this.day_of_week = day_of_week;
		this.day_of_month = day_of_month;
		this.startTime = startTime;
	}
	
	public String toString() {
		return "Filter by: " + this.scheduleID + ", " + this.month + ", " + this.year + ", " + this.day_of_week + ", " + this.day_of_month;
	}
}
