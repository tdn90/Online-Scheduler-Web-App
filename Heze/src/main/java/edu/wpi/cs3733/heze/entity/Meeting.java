package edu.wpi.cs3733.heze.entity;

public class Meeting {
	String id;
	TimeSlot tl;
	String participant;
	String secretKey;
	
	public Meeting(String id, String participant, String secretKey) {
		this.id = id;
		this.tl = null;
		this.participant = participant;
		this.secretKey = secretKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TimeSlot getTimeSlot() {
		return tl;
	}

	public void setTimeSlot(TimeSlot tl) {
		this.tl = tl;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	
	
}
