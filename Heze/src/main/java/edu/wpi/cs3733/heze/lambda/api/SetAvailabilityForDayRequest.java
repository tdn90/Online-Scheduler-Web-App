package edu.wpi.cs3733.heze.lambda.api;

public class SetAvailabilityForDayRequest { 
	public String dateID; 
	public boolean isAvailable;
	
	public SetAvailabilityForDayRequest(String dateID, boolean isAvailable) {
		this.dateID = dateID;
		this.isAvailable = isAvailable;
	}
	
	public String toString() {
		return "Set Availability For Day(" + dateID + "," + this.isAvailable + ")"; 
	}
}
