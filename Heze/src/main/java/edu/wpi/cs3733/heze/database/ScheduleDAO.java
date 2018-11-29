package edu.wpi.cs3733.heze.database;

import java.sql.*;
import java.util.Date;
import java.util.List;

import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.entity.ScheduleDate;
import edu.wpi.cs3733.heze.entity.TimeSlot;

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
    		Schedule schedule = null;
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedule WHERE scheduleID=?;");
			ps.setString(1, id);
			ResultSet resultSet = ps.executeQuery();
			
			String schedule_secretKey = ""; 
			String name = ""; 
			int startTime = 0; 
			int endTime = 0; 
			int meetingDuration = 0; 

			while (resultSet.next()) {
				schedule_secretKey = resultSet.getString("scheduleID");
				name = resultSet.getString("name");
				startTime = resultSet.getInt("startTime");
				endTime = resultSet.getInt("endTime");
				meetingDuration = resultSet.getInt("meetingLength");
			}
			resultSet.close();
			ps.close();
			schedule = new Schedule(id, schedule_secretKey, name, startTime, endTime, meetingDuration);
			
			PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM ScheduleDate WHERE scheduleID=?;");
			ps2.setString(1, id);
			ResultSet resultSet2 = ps2.executeQuery();
			
			while(resultSet2.next()) {
				ScheduleDate sd = new ScheduleDate(resultSet2.getString("dateID"), resultSet2.getString("Date"));
				
				PreparedStatement ps_TimeSlot = conn.prepareStatement("SELECT * FROM TimeSlot WHERE DateID=?;");
				ps_TimeSlot.setString(1, resultSet2.getString("dateID"));
				ResultSet resultSet_TimeSlot = ps_TimeSlot.executeQuery();
				
				while(resultSet_TimeSlot.next()) {
					TimeSlotDAO tsd = new TimeSlotDAO();
					sd.addSlot(tsd.getTimeSlot(resultSet_TimeSlot.getString("timeSlotID")));
				}
				resultSet_TimeSlot.close();
				ps_TimeSlot.close();
				
				schedule.addDays(sd);
			}
			resultSet2.close();
			ps2.close();
			
            return schedule;
        } catch (Exception e) {
            throw new Exception("Failed to insert timeslot: " + e.getMessage());
        }
    }
    
    //update
    
    //TODO
    public boolean deleteSchedule(String id) throws Exception {
    	try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Schedule WHERE scheduleID = ?;");
            ps.setString(1, id);
            int numAffected = ps.executeUpdate();
            ps.close();
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to insert constant: " + e.getMessage());
        }
    }
    
    //TODO
    public boolean createSchedule(Schedule schedule) throws Exception {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedule WHERE name = ?;");
        ps.setString(1, schedule.getScheduleID());
        ResultSet resultSet = ps.executeQuery();
        
        // already present?
        while (resultSet.next()) {
            resultSet.close();
            return false;
        }
    	
    	ps = conn.prepareStatement("INSERT INTO Schedule values(?,?,?,?,?,?);");
    	ps.setString(1, schedule.getScheduleID());
    	ps.setString(2, schedule.getSchedule_secretKey());
    	ps.setString(3, schedule.getName());
    	ps.setInt(4, schedule.getStartTime());
    	ps.setInt(5, schedule.getEndTime());
    	ps.setInt(6, schedule.getMeetingDuration());
    	ps.execute();
    	
    	ScheduleDateDAO sdd = new ScheduleDateDAO();
    	for(ScheduleDate sd : schedule.getDays()) {
    		sdd.addScheduleDate(sd, sd.getId());
    	}
    	
    	return true;
    	
    }
}
