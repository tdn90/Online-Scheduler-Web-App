package edu.wpi.cs3733.heze.lambda.api;

public class SysAdminGetScheduleRequest {
	public int hours;
	
	public SysAdminGetScheduleRequest(int hours) {
		this.hours = hours;
	}
	
	public String toString() {
		return "Request to retrieve schedules older than: " + hours + " hours";
	}
}
