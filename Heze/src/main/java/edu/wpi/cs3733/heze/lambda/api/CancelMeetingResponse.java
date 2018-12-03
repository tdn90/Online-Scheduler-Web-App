package edu.wpi.cs3733.heze.lambda.api;

public class CancelMeetingResponse {
	public int httpCode;
	
	public CancelMeetingResponse( int httpCode ) {
		this.httpCode = httpCode;
	}
	public String toString() {
		return "CancelMeeting => (httpCode=" + httpCode + ")";
	}
}
