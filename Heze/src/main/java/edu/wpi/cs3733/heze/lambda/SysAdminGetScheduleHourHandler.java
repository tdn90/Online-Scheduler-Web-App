package edu.wpi.cs3733.heze.lambda;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import edu.wpi.cs3733.heze.database.ScheduleDAO;
import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.lambda.api.SysAdminGetScheduleHourResponse;

public class SysAdminGetScheduleHourHandler implements RequestStreamHandler {
public LambdaLogger logger = null;
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	LambdaLogger logger = context.getLogger();
        // TODO: Implement your stream handler. See https://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-stream.html for more information.
        // This demo implementation capitalizes the characters from the input stream.
    	
    	JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		String body;
		boolean processed = false;
		
		SysAdminGetScheduleHourResponse response = new SysAdminGetScheduleHourResponse (null, 400);
		
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
				JSONObject qparams = (JSONObject) event.get("queryStringParameters");
				body = (String)qparams.get("hours");
				
				if (body == null) {
					response = new SysAdminGetScheduleHourResponse(null, 409);  // unable to process input
					body = event.toJSONString();  // this is only here to make testing easier
				}
			}
		} catch (ParseException pe) {
			logger.log("Exception parsing:" + pe.toString());
			response = new SysAdminGetScheduleHourResponse(null, 405);  // unable to process input
	        processed = true;
	        body = null;
		}
		
		if (!processed) {
			ArrayList<Schedule> schedule_lst = new ArrayList<Schedule>();
		
			try {
				schedule_lst = (ArrayList<Schedule>) new ScheduleDAO().getScheduleListHour(Integer.parseInt(body));
				response = new SysAdminGetScheduleHourResponse(schedule_lst, 200);
				logger.log("Create response: " + response.toString());
				logger.log("Number of hours" + body);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.log("Exception writing to DB: " + e.toString());
				response = new SysAdminGetScheduleHourResponse(schedule_lst, 405);
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
