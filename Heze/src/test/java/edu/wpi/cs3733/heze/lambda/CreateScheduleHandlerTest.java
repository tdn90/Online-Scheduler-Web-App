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

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import edu.wpi.cs3733.heze.lambda.api.CreateScheduleRequest;
import edu.wpi.cs3733.heze.lambda.api.CreateScheduleResponse;
import edu.wpi.cs3733.heze.lambda.api.PostRequest;
import edu.wpi.cs3733.heze.lambda.api.PostResponse;

public class CreateScheduleHandlerTest {
	String schedule_ID; 
	String schedule_secretKey;
	
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testCreateScheduleHandler() throws IOException {
		CreateScheduleHandler handler = new CreateScheduleHandler();
		
		CreateScheduleRequest req = new CreateScheduleRequest("Meeting Schedule", "2018-12-10", "2018-12-17", 60, 9, 11);
		String createScheduleRequest = new Gson().toJson(req);
		System.out.println(createScheduleRequest);
		String jsonRequest = new Gson().toJson(new PostRequest(createScheduleRequest));
		System.out.println(jsonRequest);
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        handler.handleRequest(input, output, createContext("create schedule"));
        
        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
        assertEquals(200, resp.httpCode);
        schedule_ID = resp.scheduleID;
        schedule_secretKey = resp.secretKey;
	}
	
	@Test
	public void testGetScheduleHandler() {
		GetScheduleHandler handler = new GetScheduleHandler();
		
	}

}
