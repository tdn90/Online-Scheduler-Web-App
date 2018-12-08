package edu.wpi.cs3733.heze.lambda.api;

public class SysAdminDeleteScheduleResponse {
	public int httpCode;
	
	public SysAdminDeleteScheduleResponse(int httpCode) {
		this.httpCode = httpCode;
	}
	
	public String toString() {
		return "Response to delete list of schedules => (code:" + this.httpCode + ")";
	}
}
