package edu.wpi.cs3733.heze.database;

import java.sql.*;
import java.time.LocalDateTime;

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
    
    //TODO
    public LocalDateTime getFirstDate(String scheduleID) throws Exception {
    	try {
    		LocalDateTime firstDate = null;
    		PreparedStatement ps = conn.prepareStatement("select min(Date) as minDate from ScheduleDate where scheduleID = ?;");
			ps.setString(1, scheduleID);
			ResultSet resultSet = ps.executeQuery();
			
			boolean found = false;
			Timestamp DB_date = null;
			while (resultSet.next()) {
				found = true;
				DB_date = resultSet.getTimestamp("minDate");
			}

			if (!found) {
				return null;
			}
			firstDate = DB_date.toLocalDateTime();
			
			resultSet.close();
			ps.close();
            return firstDate;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to insert timeslot: " + e.getMessage());
        } 	
    }
    
    //TODO
    public LocalDateTime getLastDate(String scheduleID) throws Exception {
    	try {
    		LocalDateTime lastDate;
    		PreparedStatement ps = conn.prepareStatement("select max(Date) as maxDate from ScheduleDate where scheduleID = ?;");
			ps.setString(1, scheduleID);
			ResultSet resultSet = ps.executeQuery();
			
			boolean found = false;
			Timestamp DB_date = null;
			while (resultSet.next()) {
				found = true;
				DB_date = resultSet.getTimestamp("maxDate");
			}
			
			if (!found) {
				return null;
			}
			
			lastDate = DB_date.toLocalDateTime();
			resultSet.close();
			ps.close();
            return lastDate;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to insert timeslot: " + e.getMessage());
        }
    }
    
    //TODO: implement this
    public Schedule getScheduleByID(String id) throws Exception {
    	try {
    		Schedule schedule = null;
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedule WHERE scheduleID=?;");
			ps.setString(1, id);
			ResultSet resultSet = ps.executeQuery();
			
			String secretKey = ""; 
			String name = ""; 
			int startTime = 0; 
			int endTime = 0; 
			int meetingDuration = 0; 

			boolean hasSchedule = false; 
			while (resultSet.next()) {
				hasSchedule = true;
				secretKey = resultSet.getString("secretKey");
				name = resultSet.getString("name");
				startTime = resultSet.getInt("startTime");
				endTime = resultSet.getInt("endTime");
				meetingDuration = resultSet.getInt("meetingLength");
			}
			if (!hasSchedule) {
				return null;
			}
			resultSet.close();
			ps.close();
			schedule = new Schedule(id, secretKey, name, startTime, endTime, meetingDuration);
			
			PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM ScheduleDate WHERE scheduleID=? ORDER BY Date;");
			ps2.setString(1, id);
			ResultSet resultSet2 = ps2.executeQuery();
			
			while(resultSet2.next()) {
				ScheduleDate sd = new ScheduleDateDAO().getScheduleDate(resultSet2.getString("dateID"));
				/*
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
				*/
				schedule.addDays(sd);
			}
			resultSet2.close();
			ps2.close();
            return schedule;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to insert timeslot: " + e.getMessage());
        }
    }
    
    //update
    public Schedule getScheduleBySecretKey(String secretKey) throws Exception {
    	try {
    		Schedule schedule = null;
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedule WHERE secretKey=?;");
			ps.setString(1, secretKey);
			ResultSet resultSet = ps.executeQuery();
			
			String scheduleID = ""; 
			String name = ""; 
			int startTime = 0; 
			int endTime = 0; 
			int meetingDuration = 0; 

			boolean hasSchedule = false; 
			while (resultSet.next()) {
				hasSchedule = true;
				scheduleID = resultSet.getString("scheduleID");
				name = resultSet.getString("name");
				startTime = resultSet.getInt("startTime");
				endTime = resultSet.getInt("endTime");
				meetingDuration = resultSet.getInt("meetingLength");
			}
			if (!hasSchedule) {
				return null;
			}
			resultSet.close();
			ps.close();
			schedule = new Schedule(scheduleID, secretKey, name, startTime, endTime, meetingDuration);
			
			PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM ScheduleDate WHERE scheduleID=? ORDER BY Date;");
			ps2.setString(1, scheduleID);
			ResultSet resultSet2 = ps2.executeQuery();
			
			while(resultSet2.next()) {
				ScheduleDate sd = new ScheduleDateDAO().getScheduleDate(resultSet2.getString("dateID"));
				/*
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
				*/
				schedule.addDays(sd);
			}
			resultSet2.close();
			ps2.close();
            return schedule;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to insert timeslot: " + e.getMessage());
        }
    }
    
    
    
    //TODO
    public boolean deleteSchedule(String id) throws Exception {
    	try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Schedule WHERE scheduleID = ?;");
            ps.setString(1, id);
            int numAffected = ps.executeUpdate();
            ps.close();
            return (numAffected == 1);

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to insert constant: " + e.getMessage());
        }
    }
    
    //TODO
    public boolean createSchedule(Schedule schedule) throws Exception {
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedule WHERE scheduleID = ?;");
            ps.setString(1, schedule.getScheduleID());
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            while (resultSet.next()) {
                resultSet.close();
                return false;
            }
            
            LocalDateTime current_time = LocalDateTime.now();
        	
        	ps = conn.prepareStatement("INSERT INTO Schedule values(?,?,?,?,?,?,?);");
        	
        	ps.setString(1, schedule.getScheduleID());
        	ps.setString(2, schedule.getSchedule_secretKey());
        	ps.setString(3, schedule.getName());
        	ps.setInt(4, schedule.getStartTime());
        	ps.setInt(5, schedule.getEndTime());
        	ps.setInt(6, schedule.getMeetingDuration());
        	ps.setTimestamp(7, Timestamp.valueOf(current_time));
        	ps.execute();
        	
        	ScheduleDateDAO sdd = new ScheduleDateDAO();
        	for(ScheduleDate sd : schedule.getDays()) {
        		sdd.addScheduleDate(sd, schedule.getScheduleID());
        	}
        	
        	return true;
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new Exception("Failed to insert constant: " + e.toString());
    	}
    }
    
    public boolean deleteScheduleList(int days) throws Exception {
    	try {
    		String query = "select scheduleID from Schedule where (dateCreated + interval ? day) <= current_timestamp;";
    		PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, days);
			ResultSet resultSet = ps.executeQuery();

			boolean hasSchedule = false; 
			while (resultSet.next()) {
				deleteSchedule(resultSet.getString("scheduleID"));
				hasSchedule = true;
			}
			resultSet.close();
			ps.close();
			return hasSchedule;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to insert timeslot: " + e.getMessage());
        }
    }
}
