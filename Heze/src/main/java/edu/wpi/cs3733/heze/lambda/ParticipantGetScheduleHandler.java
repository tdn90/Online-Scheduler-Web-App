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

import edu.wpi.cs3733.heze.database.ScheduleDAO;
import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.lambda.api.GetScheduleResponse;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ParticipantGetScheduleHandler implements RequestStreamHandler {

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
		GetScheduleResponse response = new GetScheduleResponse(200);
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
				body = (String)qparams.get("id");
				if (body == null) {
					response = new GetScheduleResponse(409);  // unable to process input
			        processed = true;
				}
			}
		} catch (ParseException pe) {
			logger.log("Exception parsing:" + pe.toString());
			response = new GetScheduleResponse(405);  // unable to process input
	        processed = true;
	        body = null;
		}
		
		if (!processed) {
			
			logger.log(body);
						
			try {
				Schedule s = new ScheduleDAO().getScheduleByID(body);
				if (s == null) {
					response = new GetScheduleResponse(405);
				} else {
					s.setSchedule_secretKey(null); //invalidate the secrete key so that the participant can't see it.
					response = new GetScheduleResponse(200, s);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = new GetScheduleResponse(410);
			}
			logger.log(response.toString());
		}
		
		GsonBuilder gsonBuilder = new GsonBuilder();  
		gsonBuilder.serializeNulls();  
		Gson gson = gsonBuilder.create();
		
		responseJson.put("body", gson.toJson(response));
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
		
    }

}