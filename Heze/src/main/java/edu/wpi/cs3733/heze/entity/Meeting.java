package edu.wpi.cs3733.heze.entity;

public class Meeting {
	String id;
	TimeSlot tl;
	String participant;
	String secretKey;
	
	public Meeting(String id, TimeSlot tl, String participant, String secretKey) {
		this.id = id;
		this.tl = tl;
		this.participant = participant;
		this.secretKey = secretKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TimeSlot getTl() {
		return tl;
	}

	public void setTl(TimeSlot tl) {
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
