package edu.wpi.cs3733.heze.lambda.api;

public class ToggleTimeSlotRequest {
	public String timeSlotID; 
	
	public ToggleTimeSlotRequest(String timeSlotID) {
		this.timeSlotID = timeSlotID;
	}
	
	public String toString() {
		return "ToggleTimeSlot(TimeSlotID=" + this.timeSlotID + ")";
	}
}
