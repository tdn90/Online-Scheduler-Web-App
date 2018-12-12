package edu.wpi.cs3733.heze.lambda;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalDateTime;

import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.lambda.api.CancelMeetingRequest;
import edu.wpi.cs3733.heze.lambda.api.CancelMeetingResponse;
import edu.wpi.cs3733.heze.lambda.api.CreateScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.CreateScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.DeleteScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.DeleteScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.GetScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.ExtendScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.ExtendScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.PostRequest;
import edu.wpi.cs3733.heze.lambda.api.RegisterMeetingRequest;
import edu.wpi.cs3733.heze.lambda.api.RegisterMeetingResponse;
import edu.wpi.cs3733.heze.lambda.api.TestingResponse;
import edu.wpi.cs3733.heze.lambda.api.ToggleTimeSlotRequest;
import edu.wpi.cs3733.heze.lambda.api.ToggleTimeSlotResponse;

public class CreateScheduleHandlerTest {
	static String schedule_ID = "UNINITIALIZED"; 
	static String schedule_secretKey = "UNINITIALIZED";
	LocalDateTime start_date; 
	LocalDateTime end_date;
	
	static Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}
	
	
	public static Schedule organizerGetSchedule() throws IOException {
		GetScheduleHandler handler = new GetScheduleHandler();
		String jsonRequest = "{\"queryStringParameters\": {\"secretKey\":\"" +schedule_secretKey + "\"}}";
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("create schedule"));
        
        TestingResponse get = new Gson().fromJson(output.toString(), TestingResponse.class);
        GetScheduleResponse resp = new Gson().fromJson(get.body, GetScheduleResponse.class);
        //assertEquals(resp.httpcode, 200);
        return resp.data;
	}
	
	public Schedule participantGetSchedule() {
		return null;
	}

	@BeforeClass
	public static void testCreateScheduleHandler() throws IOException, ParseException {
		CreateScheduleHandler handler = new CreateScheduleHandler();
		
		CreateScheduleRequest req = new CreateScheduleRequest("Meeting Schedule", "2018-12-10", "2018-12-17", 60, 9, 11);
		String createScheduleRequest = new Gson().toJson(req);
		System.out.println(createScheduleRequest);
		String jsonRequest = new Gson().toJson(new PostRequest(createScheduleRequest));
		System.out.println(jsonRequest);
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("create schedule"));
        
        TestingResponse post = new Gson().fromJson(output.toString(), TestingResponse.class);
        CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
        assertEquals(200, resp.httpCode);
        schedule_ID = resp.scheduleID;
        schedule_secretKey = resp.secretKey;
	}
	
	@Test
	public void testGetScheduleHandler() {
		GetScheduleHandler handler = new GetScheduleHandler();
		
	}
	
	@Test
	public void testParticipantGetScheduleHandler() {
		
		
	}
	
	@Test 
	public void testRegisterForMeetingAndCancel() throws IOException {
		
		String secretKeyMeeting = "";
		
		RegisterMeetingHandler handler = new RegisterMeetingHandler();
		String req = new Gson().toJson(new RegisterMeetingRequest("FAKEID", "JUnit"));
		String postable = new Gson().toJson(new PostRequest(req));
		
		InputStream input = new ByteArrayInputStream(postable.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("register for meeting"));
        
        TestingResponse response_holder = new Gson().fromJson(output.toString(), TestingResponse.class);
        RegisterMeetingResponse response = new Gson().fromJson(response_holder.body, RegisterMeetingResponse.class);
		secretKeyMeeting = response.m.getSecretKey();
        assertTrue(response.httpCode == 200);
        
        
        //////
        
        
        CancelMeetingHandler handler2 = new CancelMeetingHandler();
		String req2 = new Gson().toJson(new CancelMeetingRequest(secretKeyMeeting));
		String postable2 = new Gson().toJson(new PostRequest(req2));
		
		InputStream input2 = new ByteArrayInputStream(postable2.getBytes());
        OutputStream output2 = new ByteArrayOutputStream();
        
        handler2.handleRequest(input2, output2, createContext("cancel meeting"));
        
        TestingResponse response_holder2 = new Gson().fromJson(output2.toString(), TestingResponse.class);
        CancelMeetingResponse response2 = new Gson().fromJson(response_holder2.body, CancelMeetingResponse.class);
		
	}
	
	/**
	 * Should toggle and then un-toggle
	 * @throws IOException 
	 */
	@Test 
	public void testToggleTimeSlot() throws IOException {
		
		ToggleTimeSlotAvailabilityHandler handler = new ToggleTimeSlotAvailabilityHandler();
		String req = new Gson().toJson(new ToggleTimeSlotRequest("FAKE_TIMESLOT_ID"));
		String postable = new Gson().toJson(new PostRequest(req));
		
		InputStream input = new ByteArrayInputStream(postable.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("toggle meeting"));
        
        TestingResponse response_holder = new Gson().fromJson(output.toString(), TestingResponse.class);
        ToggleTimeSlotResponse response = new Gson().fromJson(response_holder.body, ToggleTimeSlotResponse.class);
        
	}
	
	@Test 
	public void testSetAvailabilityForDay() {
		
	}
	
	@Test 
	public void testSetAvailabilityForTime() {
		
	}

	@Test
	public void testExtendSchedule() throws IOException {
		ExtendScheduleHandler handler = new ExtendScheduleHandler();
		
		ExtendScheduleRequest req = new ExtendScheduleRequest(1, schedule_secretKey);
		String extendScheduleRequest = new Gson().toJson(req);
		System.out.println(extendScheduleRequest);
		String jsonRequest = new Gson().toJson(new PostRequest(extendScheduleRequest));
		System.out.println(jsonRequest);
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("extend schedule"));
        
        TestingResponse post = new Gson().fromJson(output.toString(), TestingResponse.class);
        ExtendScheduleResponse resp = new Gson().fromJson(post.body, ExtendScheduleResponse.class);
        assertEquals(200, resp.httpCode);
        //TODO: check that the schedule was successfully extended
	}
	
	@AfterClass
	public static void testDeleteSchedule() throws IOException {
		DeleteScheduleHandler handler = new DeleteScheduleHandler();
		String req = new Gson().toJson(new DeleteScheduleRequest(schedule_ID, schedule_secretKey));
		String postable = new Gson().toJson(new PostRequest(req));
		
		InputStream input = new ByteArrayInputStream(postable.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("delete meeting"));
        
        TestingResponse response_holder = new Gson().fromJson(output.toString(), TestingResponse.class);
        DeleteScheduleResponse response = new Gson().fromJson(response_holder.body, DeleteScheduleResponse.class);
        assertEquals(200, response.httpCode);
	}
}
