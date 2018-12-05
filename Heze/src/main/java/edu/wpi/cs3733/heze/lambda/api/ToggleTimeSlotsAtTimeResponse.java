package edu.wpi.cs3733.heze.lambda.api;

public class ToggleTimeSlotsAtTimeResponse {
	public int httpCode;
	
	public ToggleTimeSlotsAtTimeResponse( int httpCode ) {
		this.httpCode = httpCode;
	}
	public String toString() {
		return "ToggleTimeSlotsAtTime => (httpCode=" + httpCode + ")";
	}
}
