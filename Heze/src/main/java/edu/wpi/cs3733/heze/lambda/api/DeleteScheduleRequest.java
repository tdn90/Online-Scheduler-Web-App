package edu.wpi.cs3733.heze.lambda.api;

public class DeleteScheduleRequest {
	public String scheduleID;
	public String secretKey;
	
	public DeleteScheduleRequest( String scheduleID, String secretKey ) {
		this.scheduleID = scheduleID;
		this.secretKey = secretKey;
	}
	public String toString() {
		return "DeleteSchedule(ID=" + scheduleID + ", Key=" + secretKey + ")";
	}
}
