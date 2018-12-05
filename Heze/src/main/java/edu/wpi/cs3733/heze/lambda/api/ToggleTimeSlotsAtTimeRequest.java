package edu.wpi.cs3733.heze.lambda.api;

import java.time.LocalDateTime;

public class ToggleTimeSlotsAtTimeRequest {
	public String secretKey;
	public LocalDateTime startTime;
	public boolean availability;
	
	public ToggleTimeSlotsAtTimeRequest(String secretKey, LocalDateTime startTime, boolean availability)
	{
		this.secretKey = secretKey;
		this.startTime = startTime;
		this.availability = availability;
	}
	
	public String toString() {
		return "ToggleTimeSlotsAtTime(Key=" + this.secretKey + ", startTime=" + this.startTime.toString() + ", availability=" + this.availability + ")";
	}
}
