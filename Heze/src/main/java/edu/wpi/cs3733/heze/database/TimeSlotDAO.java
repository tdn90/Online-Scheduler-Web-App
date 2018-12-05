package edu.wpi.cs3733.heze.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    		int start = 0;
    		int meetingDuration = 0;
    		int available = 0;
    		
    		boolean found = false;
    		// at most one resultSet can be retrieved
    		while (resultSet.next()) {
    			found = true;
    			timeslot_ID = resultSet.getString("timeSlotID");
    			start = resultSet.getInt("startTime");
    			meetingDuration = resultSet.getInt("meetingLength");
    			available = resultSet.getInt("organizerAvailable");
    		}
    		resultSet.close();
    		ps.close();
    		
    		if (!found) {
    			return null;
    		}
    		
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
    		ScheduleTime startTime = new ScheduleTime(start);
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
        	 e.printStackTrace();
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
     
     /** Get list of open TimeSlots filtered by month, year, day_of_week, day_of_month, or TimeSlot
      * @param month
      * @param year
      * @param day_of_week
      * @param day_of_month
      * @param time
      * @return list of open TimeSlots
      * @throws Exception
      */
     public List<TimeSlot> getOpenSlots(String scheduleID, int month, int year, int day_of_week, int day_of_month, long time_hour) throws Exception{
     	try {
    		List<TimeSlot> timeslot_lst = new ArrayList<TimeSlot>();
    		String month_query = "extract(month from Date)";
    		String year_query = "extract(year from Date)";
    		String day_of_week_query = "DAYOFWEEK(DATE)";
    		String day_of_month_query = "extract(day from Date)";
    		String startTime_query = "startTime";
    		if(month != -9999) {
    			month_query = month_query + " = " + month;
    		}
    		if(year != -9999) {
    			year_query = year_query + " = " + year;
    		}
    		if(day_of_week != -9999) {
    			day_of_week_query = day_of_week_query + " = " + day_of_week;
    		}
    		if(day_of_month != -9999) {
    			day_of_month_query = day_of_month_query + " = " + day_of_month;
    		}
    		if(time_hour != -9999) {
    			startTime_query = startTime_query + " = " + time_hour;
    		}
    		
    		String query = "select timeSlotID from TimeSlot join (select dateID from ScheduleDate" + 
    				" where scheduleID = ?" + " and " + day_of_week_query + " and " + month_query + " and " + day_of_month_query + " and " + year_query + ") " + "as sub" + 
    				" on TimeSlot.DateID = sub.DateID" + 
    				" where organizerAvailable = 1 and " + startTime_query + " ;"; 
 
    		
    		PreparedStatement ps = conn.prepareStatement(query);
    		ps.setString(1, scheduleID);
    		ResultSet resultSet = ps.executeQuery();
    		
    		boolean found = false;
    		// at most one resultSet can be retrieved
    		while (resultSet.next()) {
    			timeslot_lst.add(getTimeSlot(resultSet.getString("timeSlotID")));
    			found = true;
    		}
    		resultSet.close();
    		ps.close();
    		
    		if (!found) {
    			return null;
    		}
    		
    		return timeslot_lst;
    	} catch (Exception e) {
    		e.printStackTrace();
            throw new Exception("Failed in getting list of timeslot: " + e.getMessage());
    	}
     }
    
}
