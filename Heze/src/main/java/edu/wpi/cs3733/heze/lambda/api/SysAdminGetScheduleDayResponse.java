package edu.wpi.cs3733.heze.lambda.api;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs3733.heze.entity.Schedule;

public class SysAdminGetScheduleDayResponse {
	public List<Schedule> schedule_lst;
	public int httpCode;
	
	public SysAdminGetScheduleDayResponse(ArrayList<Schedule> schedule_lst, int httpCode) {
		this.schedule_lst = schedule_lst;
		this.httpCode = httpCode;
	}
	
	public String toString() {
		return "SysAdminGetScheduleByDay => (httpCode=" + httpCode + ")";
	}
}
