package edu.wpi.cs3733.heze.database;

import java.sql.*;

import edu.wpi.cs3733.heze.entity.Meeting;
import edu.wpi.cs3733.heze.entity.ScheduleTime;
import edu.wpi.cs3733.heze.entity.TimeSlot;

public class MeetingDAO {
	java.sql.Connection conn;

    public MeetingDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    // NO NEED FOR UPDATE 
    
    // get, add, delete
    public boolean addMeeting(Meeting meeting, String timeSlotID) throws Exception {
    	String meetingID = meeting.getId();
    	String participantName = meeting.getParticipant();
    	String secretKey = meeting.getSecretKey();
    	
    	try {
			PreparedStatement ps = conn.prepareStatement("Insert into Meeting values (?, ?, ?, ?);");
			ps.setString(1, meetingID);
			ps.setString(2, timeSlotID);
			ps.setString(3, participantName);
			ps.setString(4, secretKey);
			int resultSet = ps.executeUpdate();
			ps.close();
			return resultSet == 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting constant: " + e.getMessage());
		}
    }
    
    /**
     * DELETE
     * @param meetingID
     * @throws Exception
     */
    public boolean delMeeting(String meetingID) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("delete * FROM Meeting WHERE MeetingID=?;");
			ps.setString(1, meetingID);
			int numAffected = ps.executeUpdate();
			ps.close();
			return (numAffected == 1);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting constant: " + e.getMessage());
		}
    }
    
    /**
     * GET
     * @param meetingID
     * @return
     * @throws Exception
     */
	public Meeting getMeeting(String meetingID) throws Exception {
		try {
			Meeting meeting = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meeting WHERE MeetingID=?;");
			ps.setString(1, meetingID);
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
			
			// Query one associated time slot
			PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM TimeSlot WHERE timeSlotID=?;");
			ps2.setString(1, timeSlotID);
			ResultSet resultSet2 = ps2.executeQuery();
		
			
			Time startTime = null;
			//int id = 0; no need for now
			int meetingLength = 0;
			int organizerAvailable = 0;
			
			while (resultSet2.next()) {
				startTime = resultSet2.getTime("startTime");
				//id = resultSet2.getInt("id"); 
				meetingLength = resultSet2.getInt("meetingLength");
				organizerAvailable = resultSet2.getInt("organizerAvailable");
			}
			
			// get the time 
			ScheduleTime time = new ScheduleTime(startTime.getTime());
			TimeSlot ts = new TimeSlot(timeSlotID, time, meetingLength, organizerAvailable == 1);
			ts.setMeeting(meeting);
			meeting = new Meeting(meetingID, participantName, secretKey);
			meeting.setTimeSlot(ts);
			resultSet2.close();
			ps2.close();
			return meeting;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting meeting: " + e.getMessage());
		}
	}
	
}