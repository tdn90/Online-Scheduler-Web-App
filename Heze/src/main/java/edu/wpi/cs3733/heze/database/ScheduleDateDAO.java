package edu.wpi.cs3733.heze.database;

import java.sql.*;

import edu.wpi.cs3733.heze.entity.ScheduleDate;

public class ScheduleDateDAO {
	java.sql.Connection conn;

    public ScheduleDateDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    //TODO: implement this
    public ScheduleDate getScheduleDate(String id) throws Exception {
    	try {
    		ScheduleDate date = null;
            PreparedStatement ps = conn.prepareStatement("INSERT INTO TimeSlot (timeSlotID, startTime, meetingLength, DateID, organizerAvailable) values (?, ?, ?, ?, ?);");
            return date;
        } catch (Exception e) {
            throw new Exception("Failed to insert timeslot: " + e.getMessage());
        }
    }
    
    //TODO: implement this
    public boolean addScheduleDate(ScheduleDate date) throws Exception {
    	return false;
    }
    
    //TODO:;
    public boolean deleteScheduleDate(String id) throws Exception {
    	return false;
    }
}
