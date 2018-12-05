package edu.wpi.cs3733.heze.lambda.api;

public class SetAvailabilityForDayResponse {
	public int httpCode; 
	public String dateID; 
	public boolean isAvailable;
	
	public SetAvailabilityForDayResponse(int code) {
		this.httpCode = code;
	}
	
	public SetAvailabilityForDayResponse(String dateID, boolean isAvailable, int code) {
		this.dateID = dateID;
		this.isAvailable = isAvailable; 
		this.httpCode = code;
	}	
	
	public SetAvailabilityForDayResponse(String dateID, boolean isAvailable) {
		this.httpCode = 200;
		this.dateID = dateID;
		this.isAvailable = isAvailable;
	}
	
	public String toString() {
		return "Response to setting availibility for day => (" + this.dateID + "," + this.isAvailable + ")";
	}
}
