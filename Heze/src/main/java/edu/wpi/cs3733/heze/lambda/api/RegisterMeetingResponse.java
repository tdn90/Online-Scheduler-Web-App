package edu.wpi.cs3733.heze.lambda.api;

import edu.wpi.cs3733.heze.entity.Meeting;

public class RegisterMeetingResponse {
	public int httpCode;
	public Meeting m;
	
	public RegisterMeetingResponse (int code) {
		this.httpCode = code;
	}
	
	public RegisterMeetingResponse (int code, Meeting m) {
		this.httpCode = code;
		this.m = m;
	}
	
	public String toString() {
		return "Response to Create Schedule => (code:" + this.httpCode + "mtg:" + this.m + ")";
	}
}
