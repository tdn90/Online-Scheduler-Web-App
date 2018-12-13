package edu.wpi.cs3733.heze.entity;

import java.time.LocalDateTime;

import edu.wpi.cs3733.heze.util.Utilities;

public class TimeSlot {
	String timeslotID;
	ScheduleTime startTime; 
	int meetingDuration; 
	LocalDateTime date; 
	Meeting meeting; 
	boolean organizerAvailable; 
	
	/*
	public static TimeSlot makeTimeSlot(ScheduleTime startTime, int meetingDuration) {
		String id = Utilities.generateKey(30);
		TimeSlot ts = new TimeSlot(id, startTime, meetingDuration, false);
		return ts;
	}
	*/
	
	public LocalDateTime getDate() {
		return this.date;
	}
	
	public static TimeSlot makeTimeSlot(LocalDateTime date, ScheduleTime startTime, int meetingDuration) {
		String id = Utilities.generateKey(30);
		TimeSlot ts = new TimeSlot(date, id, startTime, meetingDuration, true);
		return ts;
	}
	
	public TimeSlot(LocalDateTime date, String timeslotID, ScheduleTime startTime, int meetingDuration,
			boolean organizerAvailable) {
		super();
		this.date = date;
		this.timeslotID = timeslotID;
		this.startTime = startTime;
		this.meetingDuration = meetingDuration;
		this.meeting = null;
		this.organizerAvailable = organizerAvailable;
	}
	
	public boolean hasMeeting() {
		return meeting != null;
	}

	public String getTimeslotID() {
		return timeslotID;
	}

//	public void setTimeslotID(String timeslotID) {
//		this.timeslotID = timeslotID;
//	}

	public ScheduleTime getStartTime() {
		return startTime;
	}

//	public void setStartTime(ScheduleTime startTime) {
//		this.startTime = startTime;
//	}

	public int getMeetingDuration() {
		return meetingDuration;
	}

//	public void setMeetingDuration(int meetingDuration) {
//		this.meetingDuration = meetingDuration;
//	}

//	public Meeting getMeeting() {
//		return meeting;
//	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public boolean isOrganizerAvailable() {
		return organizerAvailable;
	}

//	public void setOrganizerAvailable(boolean organizerAvailable) {
//		this.organizerAvailable = organizerAvailable;
//	}
}
