package edu.wpi.cs3733.heze.entity;

public class TimeSlot {
	String timeslotID;
	ScheduleTime startTime; 
	int meetingDuration; 
	Meeting meeting; 
	boolean organizerAvailable; 
	
	public TimeSlot(String timeslotID, ScheduleTime startTime, int meetingDuration, Meeting meeting,
			boolean organizerAvailable) {
		super();
		this.timeslotID = timeslotID;
		this.startTime = startTime;
		this.meetingDuration = meetingDuration;
		this.meeting = meeting;
		this.organizerAvailable = organizerAvailable;
	}
	
	boolean hasMeeting() {
		return meeting != null;
	}
}
