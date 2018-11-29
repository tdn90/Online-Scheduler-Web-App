package edu.wpi.cs3733.heze.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Schedule {
	String scheduleID;
	List<ScheduleDate> days; 
	String schedule_secretKey; 
	String name; 
	int startTime; 
	int endTime; 
	int meetingDuration; 
	
	public static Schedule createSchedule (String name, long startTime, long endTime, 
			int meetingDuration, int startHour, int endHour) {
		return null;
	}
	
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

	public String getScheduleID() {
		return scheduleID;
	}

	public void setScheduleID(String scheduleID) {
		this.scheduleID = scheduleID;
	}

	public List<ScheduleDate> getDays() {
		return days;
	}

	public void setDays(List<ScheduleDate> days) {
		this.days = days;
	}

	public String getSchedule_secretKey() {
		return schedule_secretKey;
	}

	public void setSchedule_secretKey(String schedule_secretKey) {
		this.schedule_secretKey = schedule_secretKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getMeetingDuration() {
		return meetingDuration;
	}

	public void setMeetingDuration(int meetingDuration) {
		this.meetingDuration = meetingDuration;
	}
	public JSONObject toJSON() throws ParseException {
		GsonBuilder gsonBuilder = new GsonBuilder();  
		gsonBuilder.serializeNulls();  
		Gson gson = gsonBuilder.create();
		
		JSONParser parser = new JSONParser();
		return (JSONObject)parser.parse(gson.toJson(this));
		
	}
	
	
}
