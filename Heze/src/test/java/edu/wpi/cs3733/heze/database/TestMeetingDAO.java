package edu.wpi.cs3733.heze.database;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.wpi.cs3733.heze.entity.Meeting;

public class TestMeetingDAO {
	static MeetingDAO Meeting_DB;
	@BeforeClass
	public static void setUp() {
		Meeting_DB = new MeetingDAO();
	}
	
	@Test 
	public void testAdd() {
		Meeting mt = new Meeting("abc123", "john", "lkj098");
	}
	
	@Test
	public void testDelete() throws Exception {
		assertFalse(Meeting_DB.delMeetingByID("HelloWorld"));
	}
	
}
