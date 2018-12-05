package edu.wpi.cs3733.heze.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs3733.heze.database.MeetingDAO;
import edu.wpi.cs3733.heze.database.ScheduleDAO;
import edu.wpi.cs3733.heze.database.ScheduleDateDAO;
import edu.wpi.cs3733.heze.database.TimeSlotDAO;
import edu.wpi.cs3733.heze.entity.Meeting;
import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.entity.ScheduleDate;
import edu.wpi.cs3733.heze.entity.TimeSlot;
import edu.wpi.cs3733.heze.lambda.api.RegisterMeetingResponse;
import edu.wpi.cs3733.heze.lambda.api.SetAvailabilityForDayRequest;
import edu.wpi.cs3733.heze.lambda.api.SetAvailabilityForDayResponse;
import edu.wpi.cs3733.heze.lambda.api.ToggleTimeSlotRequest;
import edu.wpi.cs3733.heze.lambda.api.ToggleTimeSlotResponse;
import edu.wpi.cs3733.heze.lambda.api.SetAvailabilityForTimeRequest;
import edu.wpi.cs3733.heze.lambda.api.SetAvailabilityForTimeResponse;
import edu.wpi.cs3733.heze.lambda.api.CancelMeetingRequest;
import edu.wpi.cs3733.heze.lambda.api.CancelMeetingResponse;
import edu.wpi.cs3733.heze.lambda.api.RegisterMeetingRequest;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SetAvailabilityForTimeHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		LambdaLogger logger = context.getLogger();

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		String body;
		boolean processed = false;
		SetAvailabilityForTimeResponse response = new SetAvailabilityForTimeResponse(200);
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
				body = (String) event.get("body");
				if (body == null) {
					body = event.toJSONString(); // this is only here to make testing easier
				}
			}
		} catch (ParseException pe) {
			logger.log("Exception parsing:" + pe.toString());
			response = new SetAvailabilityForTimeResponse(405); // unable to process input
			processed = true;
			body = null;
		}

		// if everything is fine, then process the request and return response
		if (!processed) {
			SetAvailabilityForTimeRequest req = new Gson().fromJson(body, SetAvailabilityForTimeRequest.class);
			long startTime = req.startTime;
			String scheduleID = req.scheduleID;
			boolean isAvailable = req.isAvailable;
			logger.log(req.toString());

			try {
				boolean updated = new TimeSlotDAO().toggleTimeSlotAvailability(scheduleID, isAvailable, startTime);

				response = new SetAvailabilityForTimeResponse(200);

			} catch (Exception e) {
				e.printStackTrace();
				logger.log("Exception writing to DB: " + e.toString());
				response = new SetAvailabilityForTimeResponse(405);
			}
		}

		System.out.println("Created response: " + response.toString());
		responseJson.put("body", new Gson().toJson(response));
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
	}
}