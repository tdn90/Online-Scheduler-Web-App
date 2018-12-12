package edu.wpi.cs3733.heze.lambda.api;

import edu.wpi.cs3733.heze.entity.Schedule;

public class GetScheduleResponse {
	public int httpcode;
	public Schedule data;
	public GetScheduleResponse(int hc) //Error code only constructor
	{
		this.httpcode = hc;
		data = null;
	}
	public GetScheduleResponse(int hc, Schedule s)
	{
		this.httpcode = hc;
		this.data = s;
	}
}
