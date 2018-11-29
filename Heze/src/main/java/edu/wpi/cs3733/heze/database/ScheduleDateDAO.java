package edu.wpi.cs3733.heze.database;

import java.sql.*;
import java.util.Iterator;

import edu.wpi.cs3733.heze.entity.ScheduleDate;
import edu.wpi.cs3733.heze.entity.TimeSlot;

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
    
    /**
     * GET
     * @param scheduleDateID
     * @return
     * @throws Exception
     */
    public ScheduleDate getScheduleDate(String scheduleDateID) throws Exception {
    	try {
    		ScheduleDate scheduleDate = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ScheduleDate WHERE dateID = ?;");
            ps.setString(1, scheduleDateID);
            ResultSet resultSet = ps.executeQuery();
            
            Timestamp date = null;
            
            // should only be one result
            while (resultSet.next()) {
            	date = resultSet.getTimestamp("Date");
            }
            resultSet.close();
            ps.close();
            scheduleDate = new ScheduleDate(scheduleDateID, date.toLocalDateTime());
            
            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM TimeSlot WHERE DateID = ?;");
            ps2.setString(1, scheduleDateID);
            ResultSet resultSet2 = ps2.executeQuery();
            
            while (resultSet.next()) {
            	String timeSlotID = resultSet2.getString("timeSlotID");
            	scheduleDate.addSlot(new TimeSlotDAO().getTimeSlot(timeSlotID));
            }
            resultSet2.close();
            ps2.close();
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
            ps.setTimestamp(2, Timestamp.valueOf(date.getDate()));
            ps.setString(3, scheduleID);
            ps.execute();
            
            Iterator<TimeSlot> it = date.iterator();
            while (it.hasNext()) {
            	TimeSlot ts = it.next();
            	new TimeSlotDAO().addTimeSlot(ts, date.getId());
            }
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
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
