package edu.wpi.cs3733.heze.database;

import java.sql.*;
import java.util.List;

import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.entity.ScheduleDate;

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
    		
    		
    		
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedule WHERE scheduleID=?;");
			ps.setString(1, id);
			ResultSet resultSet = ps.executeQuery();
			
			String timeSlotID = "";
			String participantName = "";
			String secretKey = "";

			while (resultSet.next()) {
				timeSlotID = resultSet.getString("timeSlotID");
				participantName = resultSet.getString("participantName");
				secretKey = resultSet.getString("secretKey");
			}
			resultSet.close();
			ps.close();
			
            return null;
        } catch (Exception e) {
            throw new Exception("Failed to insert timeslot: " + e.getMessage());
        }
    }
    
    //update
    
    //TODO
    public boolean deleteSchedule(String id) throws Exception {
    	return false;
    }
    
    //TODO
    public boolean createSchedule(Schedule schedule) throws Exception {
    	return false;
    }
}
