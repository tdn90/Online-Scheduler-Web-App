package edu.wpi.cs3733.heze.lambda.api;

public class ExtendScheduleRequest {

	
	public ExtendScheduleRequest(int numDays, String secretKey) {
		super();
		this.numDays = numDays;
		this.secretKey = secretKey;
	}
	public int numDays;
	public String secretKey;
	
	
}
