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
    
    // no update needed
    
    //TODO: implement this
    public ScheduleDate getScheduleDate(String scheduleDateID) throws Exception {
    	try {
    		ScheduleDate scheduleDate = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ScheduleDate WHERE dateID = ?;");
            ps.setString(1, scheduleDateID);
            ResultSet resultSet = ps.executeQuery();
            
            String date = "";
            String scheduleID = "";
            
            // should only be one result
            while (resultSet.next()) {
            	resultSet.getString("Date");
            	resultSet.getString("scheduleID");
            }
            
            return scheduleDate;
        } catch (Exception e) {
            throw new Exception("Failed to insert timeslot: " + e.getMessage());
        }
    }
    
    /**
     * ADD
     * @param date
     * @param scheduleID
     * @return
     * @throws Exception
     */
    public boolean addScheduleDate(ScheduleDate date, String scheduleID) throws Exception {
    	try {
    		// TODO: see if need to check whether schedule date already exists
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ScheduleDate values (?, ?, ?);");
            ps.setString(1, date.getId());
            ps.setString(2, date.getDate());
            ps.setString(3, scheduleID);
            ps.execute();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to insert date: " + e.getMessage());
        }
    }
    
    /**
     * DELETE
     * @param id
     * @return
     * @throws Exception
     */
    public boolean deleteScheduleDate(String id) throws Exception {
    	try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM ScheduleDate WHERE dateID = ?;");
            ps.setString(1, id);
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to insert constant: " + e.getMessage());
        }
    }
}
