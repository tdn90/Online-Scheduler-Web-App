package edu.wpi.cs3733.heze.lambda.api;

public class SetAvailabilityForTimeResponse {
	public int httpCode;
	
	public SetAvailabilityForTimeResponse( int httpCode ) {
		this.httpCode = httpCode;
	}
	public String toString() {
		return "ToggleTimeSlotsAtTime => (httpCode=" + httpCode + ")";
	}
}
