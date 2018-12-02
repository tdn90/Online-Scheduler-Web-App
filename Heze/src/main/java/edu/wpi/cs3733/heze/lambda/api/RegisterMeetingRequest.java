package edu.wpi.cs3733.heze.lambda.api;

public class RegisterMeetingRequest {
	public String id;
	public String name;
	
	public RegisterMeetingRequest( String id, String name ) {
		this.id = id;
		this.name = name;
	}
	public String toString() {
		return "CreateMeetingWithName(NAME=" + name + ", TSID=" + id + ")";
	}
}
