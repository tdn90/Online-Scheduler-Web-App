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
    
    /**
     * GET
     * @param timeslotID
     * @return
     * @throws Exception
     */
    public TimeSlot getTimeSlot(String timeslotID) throws Exception {
    	try {
    		TimeSlot timeslot = null;
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlot WHERE timeSlotID=?;");
    		ps.setString(1,  timeslotID);
    		ResultSet resultSet = ps.executeQuery();
    		
    		String timeslot_ID = "";
    		Time start = null;
    		int meetingDuration = 0;
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
    		
    		// Attempt to get meeting associated with time slot above
    		Meeting meeting = null;
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
    			meeting = new Meeting(meeting_ID, participant, secretKey);
    		}
    		
    		// construct the timeslot
    		ScheduleTime startTime = new ScheduleTime(start.getTime());
    		timeslot = new TimeSlot(timeslot_ID, startTime, meetingDuration, available == 1);
    		timeslot.setMeeting(meeting);
    		
    		if (meeting != null) {
    			meeting.setTimeSlot(timeslot);
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
    
    /**
     * ADD
     * @param timeslot
     * @param dateID
     * @return
     * @throws Exception
     */
     public boolean addTimeSlot(TimeSlot timeslot, String dateID) throws Exception {
    	 try {
    		 //TODO: decide whether to add a check to see if timeslot already existed
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
     
     /**
      * Update timeslot to set organizer available
      * @param timeSlotID
      * @param isAvailable
      * @return
      * @throws Exception
      */
     public boolean toggleTimeSlotAvailability(String timeSlotID, boolean isAvailable) throws Exception {
    	 try {
     		String query = "UPDATE TimeSlot SET organizerAvailable=? WHERE timeSlotID=?;";
     		PreparedStatement ps = conn.prepareStatement(query);
     		ps.setInt(1, isAvailable ? 1 : 0);
     		ps.setString(2, timeSlotID);
     		
     		int numAffected = ps.executeUpdate();
     		ps.close();
     		return (numAffected == 1);
     	} catch (Exception e) {
     		e.printStackTrace();
             throw new Exception("Failed in getting timeslot: " + e.getMessage());
     	}
     }
     
    
}
