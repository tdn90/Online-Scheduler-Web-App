package edu.wpi.cs3733.heze.lambda.api;

public class DeleteScheduleResponse {
	public int httpCode;
	
	public DeleteScheduleResponse( int httpCode ) {
		this.httpCode = httpCode;
	}
	public String toString() {
		return "DeleteSchedule => (httpCode=" + httpCode + ")";
	}

}
