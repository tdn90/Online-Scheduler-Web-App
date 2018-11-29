package edu.wpi.cs3733.heze.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.wpi.cs3733.heze.util.Utilities;

public class ScheduleDate{
	String id;
	LocalDateTime date;
	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	List<TimeSlot> slots;
	
	public static ScheduleDate makeDay(LocalDateTime date, int startHour, int endHour, int meetingDuration) {
		String id = Utilities.generateKey(30);
		LocalDateTime this_day = date;
		ScheduleDate sd = new ScheduleDate(id, this_day);
		LocalDateTime start = LocalDateTime.of(this_day.getYear(), this_day.getMonth(), this_day.getDayOfMonth(), startHour, 0);
		LocalDateTime end = LocalDateTime.of(this_day.getYear(), this_day.getMonth(), this_day.getDayOfMonth(), endHour, 1);
		for (; start.isBefore(end); start = start.plusMinutes(meetingDuration)) {
			// Add time slot to sd
			int hour = start.getHour();
			int minute = start.getMinute();
			sd.addSlot(TimeSlot.makeTimeSlot(new ScheduleTime(hour, minute), meetingDuration));
		}
		return sd;
	}
	
	/**
	 * 
	 * @param id
	 * @param date: String representation of date in yyyy-mm-dd format
	 * @param schedule
	 */
	public ScheduleDate(String id, LocalDateTime date) {
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
	
	public List<TimeSlot> getSlots() {
		return slots;
	}

	public void setSlots(List<TimeSlot> slots) {
		this.slots = slots;
	}
	
	
}
