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

	public String getTimeslotID() {
		return timeslotID;
	}

	public void setTimeslotID(String timeslotID) {
		this.timeslotID = timeslotID;
	}

	public ScheduleTime getStartTime() {
		return startTime;
	}

	public void setStartTime(ScheduleTime startTime) {
		this.startTime = startTime;
	}

	public int getMeetingDuration() {
		return meetingDuration;
	}

	public void setMeetingDuration(int meetingDuration) {
		this.meetingDuration = meetingDuration;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public boolean isOrganizerAvailable() {
		return organizerAvailable;
	}

	public void setOrganizerAvailable(boolean organizerAvailable) {
		this.organizerAvailable = organizerAvailable;
	}
}
