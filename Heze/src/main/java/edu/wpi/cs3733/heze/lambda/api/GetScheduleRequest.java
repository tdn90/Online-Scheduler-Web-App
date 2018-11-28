package edu.wpi.cs3733.heze.lambda.api;

public class GetScheduleRequest {
	public String secretkey;
	
	public GetScheduleRequest(String k)
	{
		this.secretkey = k;
	}
	
	public String toString() {
		return "GetSchedule(" + secretkey + ")";
	}
}
