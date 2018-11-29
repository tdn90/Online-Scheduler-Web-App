package edu.wpi.cs3733.heze.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import edu.wpi.cs3733.heze.database.ScheduleDAO;
import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.lambda.api.CreateScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.CreateScheduleResponse;
import edu.wpi.cs3733.heze.util.Utilities;

import com.amazonaws.services.lambda.runtime.LambdaLogger;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class CreateScheduleHandler implements RequestStreamHandler {

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
		CreateScheduleResponse response = new CreateScheduleResponse("NOID", "NOKEY", 200);
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
			response = new CreateScheduleResponse("NOID", "NOKEY", 405);  // unable to process input
	        processed = true;
	        body = null;
		}
		
		if (!processed) {
			CreateScheduleRequest req = new Gson().fromJson(body, CreateScheduleRequest.class);
			logger.log(req.toString());

			//Make a schedule
			Schedule s = Schedule.createSchedule(req.name, req.start, req.end, req.meetingDuration, req.startHour, req.endHour);
			try {
				new ScheduleDAO().createSchedule(s);
				response = new CreateScheduleResponse(s.getScheduleID(), s.getSchedule_secretKey(), 200);
				logger.log("Create response: " + response.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.log("Exception writing to DB: " + e.toString());
				response = new CreateScheduleResponse("NOID", "NOKEY", 405);
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
