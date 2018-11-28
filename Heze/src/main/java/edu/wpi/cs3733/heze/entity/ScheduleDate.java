package edu.wpi.cs3733.heze.entity;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ScheduleDate{
	String id;
	Date date;
	Schedule schedule;
	List<TimeSlot> slots;
	
	public ScheduleDate(String id, Date date, Schedule schedule, List<TimeSlot> slots) {
		this.id = id;
		this.date = date;
		this.schedule = schedule;
		this.slots = slots;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public List<TimeSlot> getSlots() {
		return slots;
	}

	public void setSlots(List<TimeSlot> slots) {
		this.slots = slots;
	}
	
	
}
