package edu.wpi.cs3733.heze.lambda.api;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs3733.heze.entity.TimeSlot;

public class GetOpenSlotsResponse {
	public int httpCode;
	public List<TimeSlot> timeslot_lst;
	
	public GetOpenSlotsResponse(ArrayList<TimeSlot> timeslot_lst, int httpCode) {
		this.timeslot_lst = timeslot_lst;
		this.httpCode = httpCode;
	}
	
	public String toString() {
		String result = "";
//		if(this.timeslot_lst.isEmpty()) {
//			for(TimeSlot tl : this.timeslot_lst) {
//				result = tl.getTimeslotID() + ", ";
//
//			}
//		}
		return "List of timeSlotID: " + result;
	}
}
