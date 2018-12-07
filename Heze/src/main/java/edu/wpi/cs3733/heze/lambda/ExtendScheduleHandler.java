package edu.wpi.cs3733.heze.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import edu.wpi.cs3733.heze.database.ScheduleDAO;
import edu.wpi.cs3733.heze.database.ScheduleDateDAO;
import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.entity.ScheduleDate;
import edu.wpi.cs3733.heze.lambda.api.CreateScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.CreateScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.ExtendScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.ExtendScheduleResponse;
import edu.wpi.cs3733.heze.util.Utilities;

import com.amazonaws.services.lambda.runtime.LambdaLogger;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ExtendScheduleHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	LambdaLogger logger = context.getLogger();
        // TODO: Implement your stream handler. See https://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-stream.html for more information.
        // This demo implementation capitalizes the characters from the input stream.
    	
    	JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		String body;
		boolean processed = false;
		ExtendScheduleResponse response = new ExtendScheduleResponse(200);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());
			
			String method = (String) event.get("httpMethod");
			if (method != null && method.equalsIgnoreCase("OPTIONS")) {
				logger.log("Got an Options Request");
				// OPTIONS needs a 200 response
		        processed = true;
		        body = null;
			} else {
				body = (String)event.get("body");
				if (body == null) {
					body = event.toJSONString();  // this is only here to make testing easier
				}
			}
		} catch (ParseException pe) {
			logger.log("Exception parsing:" + pe.toString());
			response = new ExtendScheduleResponse(405);  // unable to process input
	        processed = true;
	        body = null;
		}
		
		if (!processed) {
			ExtendScheduleRequest req = new Gson().fromJson(body, ExtendScheduleRequest.class);
			int numDays = req.numDays;
			String secretKey = req.secretKey;
			logger.log(req.toString());

			
			try {
				// get the schedule by secret key
				ScheduleDAO Schedule_DB = new ScheduleDAO();
				Schedule req_schedule = Schedule_DB.getScheduleBySecretKey(secretKey);
				String scheduleID = req_schedule.getScheduleID();
				int startHour = req_schedule.getStartTime();
				int endHour = req_schedule.getEndTime();
				int meetingDuration = req_schedule.getMeetingDuration();
				ScheduleDateDAO ScheduleDate_DB = new ScheduleDateDAO();
				
				
				if (numDays < 0) { // add days backward, not including weekends
					/*
					 * TODO: Query the first day of schedule
					 * Do a while loop going from numDays to 0, incr each time IFF 
					 * it is not weekend
					 * 
					 * For each, make a scheduleDate and add it to schedule
					 * 
					 *  Add that scheduleDate to database
					 */
					LocalDateTime firstDay = Schedule_DB.getFirstDate(scheduleID);
					while (numDays < 0) {
						firstDay = firstDay.minusDays(1);
						if (firstDay.getDayOfWeek() != java.time.DayOfWeek.SUNDAY && firstDay.getDayOfWeek() != java.time.DayOfWeek.SATURDAY) {
							ScheduleDate_DB.addScheduleDate(ScheduleDate.makeDay(firstDay, startHour, endHour, meetingDuration), scheduleID);
							numDays++;
						}
					}
				} else { // add days forward, not including weekend
					/*
					 * TODO: Query the last day of schedule
					 * Do a while loop going from numDays to 0, decr each time IFF 
					 * it is not weekend
					 * 
					 * 
					 * For each, make a scheduleDate and add it to schedule
					 * 
					 * Add that scheduleDate to database
					 */
					LocalDateTime lastDay = Schedule_DB.getLastDate(scheduleID);
					while (numDays > 0) {
						lastDay = lastDay.plusDays(1);
						if (lastDay.getDayOfWeek() != java.time.DayOfWeek.SUNDAY && lastDay.getDayOfWeek() != java.time.DayOfWeek.SATURDAY) {
							ScheduleDate_DB.addScheduleDate(ScheduleDate.makeDay(lastDay, startHour, endHour, meetingDuration), scheduleID);
							numDays--;
						}
					}
				}
				// get startTime, endTime, and duration
				response = new ExtendScheduleResponse(200);
				logger.log("Create response: " + response.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.log("Exception writing to DB: " + e.toString());
				response = new ExtendScheduleResponse(405);
			}
			//logger.log("Create a schedule with the name: " + req.name);

			// compute proper response
			
		}
		
		responseJson.put("body", new Gson().toJson(response));
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
		
    }

}
