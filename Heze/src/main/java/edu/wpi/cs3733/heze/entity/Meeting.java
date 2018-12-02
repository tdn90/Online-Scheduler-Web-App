package edu.wpi.cs3733.heze.entity;

import edu.wpi.cs3733.heze.util.Utilities;

public class Meeting {
	String id;
	transient TimeSlot tl; // Naming this field transient prevents gson from creating an infinite loop
	String participant;
	String secretKey;
	
	public static Meeting createMeeting(String participant) {
		String id = Utilities.generateKey(30);
		String secret = Utilities.generateKey(6);
		return new Meeting(id, participant, secret);
	}
	
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
