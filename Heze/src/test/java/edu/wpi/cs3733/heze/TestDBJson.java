package edu.wpi.cs3733.heze;

import static org.junit.Assert.*;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.entity.ScheduleDate;
import edu.wpi.cs3733.heze.entity.ScheduleTime;
import edu.wpi.cs3733.heze.entity.TimeSlot;

public class TestDBJson {

	@Test
	public void test() {
		Schedule s = new Schedule("asd", "dsa", "name", 10, 20, 30);
		ScheduleDate d = new ScheduleDate("asd", "dssa");
		ScheduleTime t = new ScheduleTime(0);
		d.addSlot(new TimeSlot("asd", t, 10, true));
		s.addDays(d);
		try {
			System.out.println(s.toJSON());
			assertTrue(s.toJSON().toString().length() > 2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("There was an exception while parsing json");
		}
	}

}
