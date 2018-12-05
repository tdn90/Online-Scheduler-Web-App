package edu.wpi.cs3733.heze.lambda.api;

public class ToggleTimeSlotResponse {
	public int httpCode; 
	public boolean isTimeSlotAvailable; 
	
	public ToggleTimeSlotResponse(int httpCode) {
		this.httpCode = httpCode;
	}
	
	public ToggleTimeSlotResponse(int httpCode, boolean isAvailable) {
		this.httpCode = httpCode;
		this.isTimeSlotAvailable = isAvailable;
	}
	
	public String toString() {
		return "Response to Toggle Time Slot => (code:" + this.httpCode + "\nAvailability:" + this.isTimeSlotAvailable + ")";
	}
}
