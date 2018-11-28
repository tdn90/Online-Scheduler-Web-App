package edu.wpi.cs3733.heze.lambda.api;

public class CreateScheduleResponse {
	public int httpCode;
	public String scheduleID;
	public String secretKey;
	
	public CreateScheduleResponse (String scheduleID, String scheduleKey, int code) {
		this.secretKey = scheduleKey;
		this.scheduleID = scheduleID;
		this.httpCode = code;
	}
	
	// 200 means success
	public CreateScheduleResponse (String scheduleID, String scheduleKey) {
		this.secretKey = scheduleKey;
		this.scheduleID = scheduleID;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response to Create Schedule => (" + secretKey + ", " + scheduleID + ")";
	}
}