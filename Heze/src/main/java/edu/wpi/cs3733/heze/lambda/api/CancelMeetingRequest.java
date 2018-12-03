package edu.wpi.cs3733.heze.lambda.api;

public class CancelMeetingRequest {
	public String secretKey;
	
	public CancelMeetingRequest( String secretKey ) {
		this.secretKey = secretKey;
	}
	public String toString() {
		return "CancelMeeting(SecretKey=" + secretKey + ")";
	}
}
