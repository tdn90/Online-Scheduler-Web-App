package edu.wpi.cs3733.heze.database;

import java.sql.*;

import edu.wpi.cs3733.heze.entity.Schedule;

public class ScheduleDAO {
	java.sql.Connection conn;

    public ScheduleDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    //TODO: implement this
    public Schedule getSchedule(String id) throws Exception {
    	try {
    		Schedule schedule = null;
            PreparedStatement ps = conn.prepareStatement("INSERT INTO TimeSlot (timeSlotID, startTime, meetingLength, DateID, organizerAvailable) values (?, ?, ?, ?, ?);");
            return schedule;
        } catch (Exception e) {
            throw new Exception("Failed to insert timeslot: " + e.getMessage());
        }
    }
    
    //TODO
    public boolean deleteSchedule(String id) throws Exception {
    	return false;
    }
    
    //TODO
    public boolean createSchedule(Schedule schedule) throws Exception {
    	return false;
    }
}
