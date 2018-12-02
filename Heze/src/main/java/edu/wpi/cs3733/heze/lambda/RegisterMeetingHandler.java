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
import com.google.gson.GsonBuilder;

import edu.wpi.cs3733.heze.database.MeetingDAO;
import edu.wpi.cs3733.heze.database.ScheduleDAO;
import edu.wpi.cs3733.heze.entity.Meeting;
import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.lambda.api.RegisterMeetingResponse;
import edu.wpi.cs3733.heze.lambda.api.RegisterMeetingRequest;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class RegisterMeetingHandler implements RequestStreamHandler {

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
		RegisterMeetingResponse response = new RegisterMeetingResponse(200);
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
			response = new RegisterMeetingResponse(405);  // unable to process input
	        processed = true;
	        body = null;
		}
		
		if (!processed) {
			RegisterMeetingRequest req = new Gson().fromJson(body, RegisterMeetingRequest.class);
			logger.log(req.toString());

			//Make a meeting
			Meeting m = Meeting.createMeeting(req.name);
			try {
				MeetingDAO dao = new MeetingDAO();
				if (req.name.length() < 1) {
					response = new RegisterMeetingResponse(405);
				} else if (dao.getMeeting(req.id) == null) {
					dao.addMeeting(m, req.id);
					response = new RegisterMeetingResponse(200, m);
					logger.log("Create response: " + response.toString());
				} else {
					response = new RegisterMeetingResponse(304);
					logger.log("Create response: " + response.toString());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.log("Exception writing to DB: " + e.toString());
				response = new RegisterMeetingResponse(405);
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