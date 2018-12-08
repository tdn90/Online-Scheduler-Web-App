package edu.wpi.cs3733.heze.lambda.api;

public class SysAdminDeleteScheduleRequest {
	public int days;
	
	public SysAdminDeleteScheduleRequest(int days){
		this.days = days;
	}
	
	public String toString() {
		return "Request to delete Schdeule older than: " + days + " days";
	}
}
