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
    		Time startT = null;
    		int meetingDuration = 0;
    		int available = 0;
    		Timestamp date = null;
    		
    		boolean found = false;
    		// at most one resultSet can be retrieved
    		while (resultSet.next()) {
    			found = true;
    			date = resultSet.getTimestamp("date");
    			timeslot_ID = resultSet.getString("timeSlotID");
    			startT = resultSet.getTime("startTime");
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
    		ScheduleTime startTime = new ScheduleTime(startT.getTime());
    		timeslot = new TimeSlot(date.toLocalDateTime(), timeslot_ID, startTime, meetingDuration, available == 1);
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
             PreparedStatement ps = conn.prepareStatement("INSERT INTO TimeSlot values (?, ?, ?, ?, ?, ?);");
             ps.setString(1, timeslot.getTimeslotID());
             
             Time startTime = new Time(timeslot.getStartTime().convertToMilli());
             ps.setTime(2, startTime);
             ps.setInt(3, timeslot.getMeetingDuration());
             ps.setTimestamp(4, Timestamp.valueOf(timeslot.getDate()));
             ps.setString(5, dateID);
             ps.setInt(6, timeslot.isOrganizerAvailable() ? 1 : 0);
             
             ps.execute();
             return true;
         } catch (Exception e) {
        	 e.printStackTrace();
             throw new Exception("Failed to insert timeslot: " + e.getMessage());
         }
     }
     
     public boolean toggleTimeSlotAvailability(String scheduleID, boolean isAvailable, long startTime) throws Exception {
    	 try {
    		 
    		PreparedStatement ps = conn.prepareStatement("SELECT timeSlotID FROM TimeSlot T JOIN ScheduleDate SD ON T.DateID = SD.dateID WHERE scheduleID = ? AND startTime = ?;");
     		ps.setString(1, scheduleID);
     		Time startT = new Time(startTime);
     		ps.setTime(2, startT);
     		ResultSet resultSet = ps.executeQuery();
     		
     		boolean found = false;
     		while (resultSet.next()) {
     			found = true;
     			String current_TS_ID = resultSet.getString("timeSlotID");
     			toggleTimeSlotAvailability(current_TS_ID, isAvailable);
     		}
     		resultSet.close();
     		ps.close();
     		
     		return found;
      	} catch (Exception e) {
      		e.printStackTrace();
              throw new Exception("Failed in getting timeslot: " + e.getMessage());
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
     		
     		// if this is set to false, then meeting will have to also be deleted
     		/*
     		if (!isAvailable) {
     			TimeSlot ts = this.getTimeSlot(timeSlotID);
     			// check if there is a meeting within this timeslot
     			if (ts.getMeeting() != null) {
     				// there is! Then delete it...
     				new MeetingDAO().delMeetingByID(ts.getMeeting().getId());
     			}
     		}
     		*/
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
    		/*
    		if(time_hour != -9999) {
    			startTime_query = startTime_query + " = " + time_hour;
    		}
    		*/
    		
    		if(time_hour != -9999) {
    			startTime_query = startTime_query + " = ?";
    		}
    		
    		String query = "select timeSlotID from TimeSlot join (select dateID from ScheduleDate" + 
    				" where scheduleID = ?" + " and " + day_of_week_query + " and " + month_query + " and " + day_of_month_query + " and " + year_query + ") " + "as sub" + 
    				" on TimeSlot.DateID = sub.DateID" + 
    				" where organizerAvailable = 1 and " + startTime_query + " order by date;"; 
    		
    		
    		
    		PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, scheduleID);
			
			Time startTime = new Time(time_hour); 
			if (time_hour != -9999) {
				ps.setTime(2, startTime);
			}
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
