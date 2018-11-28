package edu.wpi.cs3733.heze.entity;

import java.util.Iterator;
import java.util.List;

public class Schedule {
	String scheduleID;
	List<ScheduleDate> days; 
	String schedule_secretKey; 
	String name; 
	int startTime; 
	int endTime; 
	int meetingDuration; 
	
	public Schedule(String scheduleID, List<ScheduleDate> days, String schedule_secretKey, String name, int startTime,
			int endTime, int meetingDuration) {
		super();
		this.scheduleID = scheduleID;
		this.days = days;
		this.schedule_secretKey = schedule_secretKey;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.meetingDuration = meetingDuration;
	}
	
	public Iterator<ScheduleDate> daysIt() {
		return days.iterator();
	}
}
