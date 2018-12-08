package edu.wpi.cs3733.heze.lambda.api;
import java.util.*;
import edu.wpi.cs3733.heze.entity.Schedule;

public class SysAdminGetScheduleResponse {
	public List<Schedule> schedule_lst;
	public int httpCode;
	
	public SysAdminGetScheduleResponse (ArrayList<Schedule> schedule_lst,int httpCode) {
		this.schedule_lst = schedule_lst;
		this.httpCode = httpCode;
	}
	
	public String toString() {
		return "SysAdminGetSchedule => (httpCode=" + httpCode + ")";
	}
}
