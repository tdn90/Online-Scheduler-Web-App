package edu.wpi.cs3733.heze.entity;

import java.util.ArrayList;
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
	
	public Schedule(String scheduleID, String schedule_secretKey, String name, int startTime,
			int endTime, int meetingDuration) {
		super();
		this.scheduleID = scheduleID;
		this.days = new ArrayList<ScheduleDate>();
		this.schedule_secretKey = schedule_secretKey;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.meetingDuration = meetingDuration;
	}
	
	public void addDays(ScheduleDate day) {
		this.days.add(day);
	}
	
	public Iterator<ScheduleDate> daysIt() {
		return days.iterator();
	}
}
