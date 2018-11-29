package edu.wpi.cs3733.heze.entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScheduleDate{
	String id;
	String date;
	List<TimeSlot> slots;
	
	
	
	/**
	 * 
	 * @param id
	 * @param date: String representation of date in yyyy-mm-dd format
	 * @param schedule
	 */
	public ScheduleDate(String id, String date) {
		this.id = id;
		this.date = date;
		this.slots = new ArrayList<>();
	}
	
	public boolean addSlot(TimeSlot slot) {
		slots.add(slot);
		return false;
	}
	
	/** Convenient iteration over pieces. */
	public Iterator<TimeSlot> iterator() {
		return slots.iterator();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<TimeSlot> getSlots() {
		return slots;
	}

	public void setSlots(List<TimeSlot> slots) {
		this.slots = slots;
	}
	
	
}
