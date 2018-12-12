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
import java.util.Iterator;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import edu.wpi.cs3733.heze.entity.Schedule;
import edu.wpi.cs3733.heze.entity.ScheduleDate;
import edu.wpi.cs3733.heze.entity.TimeSlot;
import edu.wpi.cs3733.heze.lambda.api.CancelMeetingRequest;
import edu.wpi.cs3733.heze.lambda.api.CancelMeetingResponse;
import edu.wpi.cs3733.heze.lambda.api.CreateScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.CreateScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.DeleteScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.DeleteScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.GetScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.ExtendScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.ExtendScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.GetOpenSlotsRequest;
import edu.wpi.cs3733.heze.lambda.api.GetOpenSlotsResponse;
import edu.wpi.cs3733.heze.lambda.api.PostRequest;
import edu.wpi.cs3733.heze.lambda.api.RegisterMeetingRequest;
import edu.wpi.cs3733.heze.lambda.api.RegisterMeetingResponse;
import edu.wpi.cs3733.heze.lambda.api.SetAvailabilityForTimeRequest;
import edu.wpi.cs3733.heze.lambda.api.SetAvailabilityForTimeResponse;
import edu.wpi.cs3733.heze.lambda.api.SysAdminDeleteScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.SysAdminDeleteScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.SetAvailabilityForDayRequest;
import edu.wpi.cs3733.heze.lambda.api.SetAvailabilityForDayResponse;
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
        
        handler.handleRequest(input, output, createContext("get org schedule"));
        
        TestingResponse get = new Gson().fromJson(output.toString(), TestingResponse.class);
        GetScheduleResponse resp = new Gson().fromJson(get.body, GetScheduleResponse.class);
        assertEquals(resp.httpcode, 200);
        return resp.data;
	}
	
	public static Schedule participantGetSchedule() throws IOException {
		ParticipantGetScheduleHandler handler = new ParticipantGetScheduleHandler();
		String jsonRequest = "{\"queryStringParameters\": {\"id\":\"" +schedule_ID + "\"}}";
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("get part schedule"));
        
        TestingResponse get = new Gson().fromJson(output.toString(), TestingResponse.class);
        GetScheduleResponse resp = new Gson().fromJson(get.body, GetScheduleResponse.class);
        assertEquals(resp.httpcode, 200);
        return resp.data;
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
	public void testGetScheduleHandler() throws IOException {
		Schedule s = organizerGetSchedule();
		assertNotNull(s);
	}
	
	@Test
	public void testParticipantGetScheduleHandler() throws IOException {
		Schedule s = participantGetSchedule();
		assertNotNull(s);
		
	}
	
	@Test 
	public void testRegisterForMeetingAndCancel() throws IOException {
		
		String tsid = "FAKEID";
		
		Schedule s = organizerGetSchedule();
		Iterator<ScheduleDate> dateiter = s.daysIt();
		while(dateiter.hasNext()) {
			Iterator<TimeSlot> tsiter = dateiter.next().iterator();
				while(dateiter.hasNext()) {
					tsid = tsiter.next().getTimeslotID();
					break; //just one
				}
			break; //just want one of them
			
		}
		
		String secretKeyMeeting = "";
		
		RegisterMeetingHandler handler = new RegisterMeetingHandler();
		String req = new Gson().toJson(new RegisterMeetingRequest(tsid, "JUnit"));
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
        assertTrue(response2.httpCode == 200);
		
	}
	
	/**
	 * Should toggle and then un-toggle
	 * @throws IOException 
	 */
	@Test 
	public void testToggleTimeSlot() throws IOException {
		
		String tsid = "FAKEID";
		
		Schedule s = organizerGetSchedule();
		Iterator<ScheduleDate> dateiter = s.daysIt();
		while(dateiter.hasNext()) {
			Iterator<TimeSlot> tsiter = dateiter.next().iterator();
				while(dateiter.hasNext()) {
					tsid = tsiter.next().getTimeslotID();
					break; //just one
				}
			break; //just want one of them
			
		}
		
		for (int i = 0; i < 2; i++) {
			ToggleTimeSlotAvailabilityHandler handler = new ToggleTimeSlotAvailabilityHandler();
			String req = new Gson().toJson(new ToggleTimeSlotRequest(tsid));
			String postable = new Gson().toJson(new PostRequest(req));
			
			InputStream input = new ByteArrayInputStream(postable.getBytes());
	        OutputStream output = new ByteArrayOutputStream();
	        
	        handler.handleRequest(input, output, createContext("toggle meeting"));
	        
	        TestingResponse response_holder = new Gson().fromJson(output.toString(), TestingResponse.class);
	        ToggleTimeSlotResponse response = new Gson().fromJson(response_holder.body, ToggleTimeSlotResponse.class);
	        assertEquals(200, response.httpCode);
		}
        
	}
	
	@Test 
	public void testSetAvailabilityForDay() throws IOException {
		Schedule sc = organizerGetSchedule();
		String dayID = sc.getDays().get(0).getId();
		SetAvailabilityForDayHandler handler = new SetAvailabilityForDayHandler();
		
		SetAvailabilityForDayRequest req = new SetAvailabilityForDayRequest(dayID, false);
		String setAvailabilityForDayRequest = new Gson().toJson(req);
		System.out.println(setAvailabilityForDayRequest);
		String jsonRequest = new Gson().toJson(new PostRequest(setAvailabilityForDayRequest));
		System.out.println(jsonRequest);
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("set availablility for day"));
        
        TestingResponse post = new Gson().fromJson(output.toString(), TestingResponse.class);
        SetAvailabilityForDayResponse resp = new Gson().fromJson(post.body, SetAvailabilityForDayResponse.class);
        assertEquals(200, resp.httpCode);
	}
	
	@Test 
	public void testSetAvailabilityForTime() throws IOException {
		SetAvailabilityForTimeHandler handler = new SetAvailabilityForTimeHandler();
		
		SetAvailabilityForTimeRequest req = new SetAvailabilityForTimeRequest(32400000, schedule_ID, false);
		String setAvailabilityForTimeRequest = new Gson().toJson(req);
		
		String jsonRequest = new Gson().toJson(new PostRequest(setAvailabilityForTimeRequest));
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("Toggle availability for time"));
        
        TestingResponse post = new Gson().fromJson(output.toString(), TestingResponse.class);
        SetAvailabilityForTimeResponse resp = new Gson().fromJson(post.body, SetAvailabilityForTimeResponse.class);
        assertEquals(200, resp.httpCode);
	}

	@Test
	public void testExtendSchedule() throws IOException {
		int daystotest = 1;
		Schedule sc = organizerGetSchedule();
		int daysbefore = sc.getSizeofList();
		ExtendScheduleHandler handler = new ExtendScheduleHandler();
		
		ExtendScheduleRequest req = new ExtendScheduleRequest(daystotest, sc.getSchedule_secretKey());
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
        sc = organizerGetSchedule();
        assertEquals(daysbefore + daystotest, sc.getSizeofList());
	}
	
	@Test
	public void testSysAdminDeleteScheduleList() throws IOException {
		int days = 3;
		SysAdminDeleteScheduleHandler handler = new SysAdminDeleteScheduleHandler();
		String req = new Gson().toJson(new SysAdminDeleteScheduleRequest(days));
		String postable = new Gson().toJson(new PostRequest(req));
		
		InputStream input = new ByteArrayInputStream(postable.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("delete meeting list"));
        TestingResponse response_holder = new Gson().fromJson(output.toString(), TestingResponse.class);
        SysAdminDeleteScheduleResponse response = new Gson().fromJson(response_holder.body, SysAdminDeleteScheduleResponse.class);
        assertEquals(200, response.httpCode);
	}
        
	@Test 
	public void testGetOpenSlotsHandler() throws IOException {
		GetOpenSlotsHandler handler = new GetOpenSlotsHandler();
		
		GetOpenSlotsRequest req = new GetOpenSlotsRequest(schedule_ID, 12, 2018, -9999, -9999, 32400000);
		String getOpenSlotsRequest = new Gson().toJson(req);
		
		String jsonRequest = new Gson().toJson(new PostRequest(getOpenSlotsRequest));
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("Get open slots"));
        
        TestingResponse post = new Gson().fromJson(output.toString(), TestingResponse.class);
        GetOpenSlotsResponse resp = new Gson().fromJson(post.body, GetOpenSlotsResponse.class);
        List<TimeSlot> list = resp.timeslot_lst;
        System.out.println(new Gson().toJson(list).toString());
        assertEquals(200, resp.httpCode);

	}
	
	@Test
	public void testSysAdminDeleteScheduleList() throws IOException {
		int days = 3;
		SysAdminDeleteScheduleHandler handler = new SysAdminDeleteScheduleHandler();
		String req = new Gson().toJson(new SysAdminDeleteScheduleRequest(days));
		String postable = new Gson().toJson(new PostRequest(req));
		
		InputStream input = new ByteArrayInputStream(postable.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("delete meeting list"));
        TestingResponse response_holder = new Gson().fromJson(output.toString(), TestingResponse.class);
        SysAdminDeleteScheduleResponse response = new Gson().fromJson(response_holder.body, SysAdminDeleteScheduleResponse.class);
        assertEquals(200, response.httpCode);
	}
	
	@AfterClass
	public static void testDeleteSchedule() throws IOException {
//		int days = 3;
//		SysAdminDeleteScheduleHandler handler = new SysAdminDeleteScheduleHandler();
//		String req = new Gson().toJson(new SysAdminDeleteScheduleRequest(days));
//		String postable = new Gson().toJson(new PostRequest(req));
//		
//		InputStream input = new ByteArrayInputStream(postable.getBytes());
//        OutputStream output = new ByteArrayOutputStream();
//        
//        handler.handleRequest(input, output, createContext("delete meeting list"));
//        TestingResponse response_holder = new Gson().fromJson(output.toString(), TestingResponse.class);
//        SysAdminDeleteScheduleResponse response = new Gson().fromJson(response_holder.body, SysAdminDeleteScheduleResponse.class);
//        assertEquals(200, response.httpCode);
		
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
