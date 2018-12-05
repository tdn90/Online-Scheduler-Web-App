package edu.wpi.cs3733.heze.lambda.api;

public class SetAvailabilityForTimeRequest {
	public long startTime; // milliseconds since midnight
	public String scheduleID;
	public boolean isAvailable;
	
	public SetAvailabilityForTimeRequest(long startTime, String scheduleID, boolean isAvailable) {
		super();
		this.startTime = startTime;
		this.scheduleID = scheduleID;
		this.isAvailable = isAvailable;
	}
	
	public String toString() {
		return "ToggleTimeSlotsAtTime(startTime=" + this.startTime + ", availability=" + this.isAvailable + ")";
	}
}
