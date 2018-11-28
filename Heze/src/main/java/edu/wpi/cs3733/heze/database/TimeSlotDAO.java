package edu.wpi.cs3733.heze.database;

import java.sql.*;
import edu.wpi.cs3733.heze.entity.Meeting;
import edu.wpi.cs3733.heze.entity.ScheduleTime;
import edu.wpi.cs3733.heze.entity.TimeSlot;

public class TimeSlotDAO {
	java.sql.Connection conn;

    public TimeSlotDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public TimeSlot getTimeSlot(String timeslotID) throws Exception {
    	try {
    		TimeSlot timeslot = null;
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlot WHERE timeSlotID=?;");
    		ps.setString(1,  timeslotID);
    		ResultSet resultSet = ps.executeQuery();
    		
    		String timeslot_ID = "";
    		Time start = null;
    		int meetingDuration = 0;
    		Meeting meeting = null;
    		int available = 0;
    		
    		// at most one resultSet can be retrieved
    		while (resultSet.next()) {
    			timeslot_ID = resultSet.getString("timeSlotID");
    			start = resultSet.getTime("startTime");
    			meetingDuration = resultSet.getInt("meetingLength");
    			available = resultSet.getInt("organizerAvailable");
    		}
    		resultSet.close();
    		ps.close();
    		
    		
    		PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM Meeting WHERE timeSlotID=?;");
    		ps2.setString(1,  timeslotID);
    		ResultSet resultSet2 = ps2.executeQuery();
   
    		boolean isEmpty = true;
    		String meeting_ID = "";
    		String participant = "";
    		String secretKey = "";
    		// at most one resultSet can be retrieved
    		while (resultSet2.next()) {
    			isEmpty = false;
    			meeting_ID = resultSet2.getString("MeetingID");
    			participant = resultSet2.getString("participantName");
    			secretKey = resultSet2.getString("secretKey");
    		}
    		
    		// There is a meeting in this timeslot
    		if (!isEmpty) {
    			meeting = new Meeting(meeting_ID, null, participant, secretKey);
    		}
    		// construct the timeslot
    		ScheduleTime startTime = new ScheduleTime(start.getTime());
    		timeslot = new TimeSlot(timeslot_ID, startTime, meetingDuration, meeting, available == 1);
    		
    		if (meeting != null) {
    			meeting.setTl(timeslot);
    		}
    		
    		resultSet2.close();
    		ps2.close();
    		return timeslot;
    	} catch (Exception e) {
    		e.printStackTrace();
            throw new Exception("Failed in getting timeslot: " + e.getMessage());
    	}
    }
    
    /* There is no delete in time slot
     * since if the schedule is deleted, 
     * dates will also be deleted, which case timeslot to delete
     */
    
    
     public boolean addTimeSlot(TimeSlot timeslot, String dateID) throws Exception {
    	 try {
             PreparedStatement ps = conn.prepareStatement("INSERT INTO TimeSlot (timeSlotID, startTime, meetingLength, DateID, organizerAvailable) values (?, ?, ?, ?, ?);");
             ps.setString(1, timeslot.getTimeslotID());
             ps.setString(2, "" + timeslot.getStartTime().convertToMilli());
             ps.setString(3, "" + timeslot.getMeetingDuration());
             ps.setString(4, dateID);
             ps.setString(5, timeslot.isOrganizerAvailable() ? "1" : "0");
             ps.execute();
             return true;
         } catch (Exception e) {
             throw new Exception("Failed to insert timeslot: " + e.getMessage());
         }
     }
    
}
