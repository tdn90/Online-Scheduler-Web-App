package edu.wpi.cs3733.heze.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs3733.heze.util.Utilities;

public class Schedule {
	String scheduleID;
	List<ScheduleDate> days; 
	String schedule_secretKey; 
	String name; 
	int startTime; 
	int endTime; 
	int meetingDuration; 
	
	public static Schedule createSchedule (String name, String startDay, String endDay, 
			int meetingDuration, int startHour, int endHour) {
		String id = Utilities.generateKey(10);
		String secretKey = Utilities.generateKey(30);
		Schedule schedule = new Schedule(id, secretKey, name, startHour, endHour, meetingDuration);
		DateTimeFormatter DATEFORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    DateTimeFormatter DATEFORMATTER = new DateTimeFormatterBuilder().append(DATEFORMATTER1)
		    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
		    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		    .toFormatter();

		/* do a for loop to create all days between startDay and EndDay (inclusive)
		 * and add to schedule
		 */
		LocalDateTime start = LocalDateTime.parse(startDay, DATEFORMATTER);
		LocalDateTime end = LocalDateTime.parse(endDay, DATEFORMATTER);
		end.plusDays(1);
		
		for (LocalDateTime currentDate = start; currentDate.isBefore(end); currentDate = currentDate.plusDays(1)) {
			if (currentDate.getDayOfWeek() != java.time.DayOfWeek.SUNDAY && currentDate.getDayOfWeek() != java.time.DayOfWeek.SATURDAY) {
				schedule.addDays(ScheduleDate.makeDay(currentDate, startHour, endHour, meetingDuration));
			}
		}
		return schedule;
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
		Collections.sort(this.days);
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
