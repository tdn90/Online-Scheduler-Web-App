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
    
	public Meeting getMeeting(String meetingID) throws Exception {

		try {
			Meeting meeting = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Constants WHERE MeetingID=?;");
			ps.setString(1, meetingID);
			ResultSet resultSet = ps.executeQuery();
			
			String ID = "";
			String timeSlotID = "";
			String participantName = "";
			String secretKey = "";

			while (resultSet.next()) {
				ID = resultSet.getString("MeetingID");
				timeSlotID = resultSet.getString("timeSlotID");
				participantName = resultSet.getString("participantName");
				secretKey = resultSet.getString("secretKey");
			}
			resultSet.close();
			ps.close();
			
//			query list of time slots
			PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM Constants WHERE MeetingID=?;");
			ps2.setString(1, meetingID);
			ResultSet resultSet2 = ps2.executeQuery();
			
			String timeslotID = ""; 
			Time startTime = null;
//			int id = 0; no need for now
			int meetingLength = 0;
			String meeting_ID = "";
			String dateID = ""; 
			int organizerAvailable = 0;
			
			while (resultSet2.next()) {
				timeslotID = resultSet2.getString("timeSlotID"); 
				startTime = resultSet2.getTime("startTime");
				//id = resultSet2.getInt("id"); 
				meetingLength = resultSet2.getInt("meetingLength");
				meeting_ID = resultSet2.getString("meetingID");
				dateID = resultSet2.getString("DateID"); 
				organizerAvailable = resultSet2.getInt("organizerAvailable");
			}
			
			ScheduleTime time = new ScheduleTime(startTime.getTime());
			TimeSlot ts = new TimeSlot(timeslotID, time, meetingLength, null, organizerAvailable == 1);
			meeting = new Meeting(meeting_ID, ts, participantName, secretKey);
			resultSet2.close();
			ps.close();
			return meeting;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting constant: " + e.getMessage());
		}
	}
	
}
