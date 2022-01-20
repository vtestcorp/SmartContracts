package com.appveen.smartcontracts.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatDeleteResponse;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.Message;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PublishFactory {
	MethodsClient slackClient = null;
	Properties properties = null;
	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	
	private void cleanSlackNotifications(String slackBotToken, String slackChannelId, String slackBotId) {
		List<String> timestamp = new ArrayList<String>(); 
		try {
			ConversationsHistoryResponse chatHistory = slackClient.conversationsHistory(request -> request			        
							        .token(slackBotToken)
							        .channel(slackChannelId));

			for(Message message : chatHistory.getMessages()) {				
        		if(message.getBotId()!= null) {
	        		if(message.getBotId().equals(slackBotId)) {
	        			timestamp.add(message.getTs());
	        		}
        		}
        	}			
			if(timestamp.size()>5) {
				timestamp = timestamp.subList(5, timestamp.size());
				
				for(String time : timestamp) {
	        		ChatDeleteResponse deleteresponse = slackClient.chatDelete(request -> request
					        				.token(slackBotToken)
					        				.channel(slackChannelId)
					        				.ts(time));	        		
	        		System.out.println("Slack cleaner response: "+deleteresponse);
	        	}
			}
			
		} catch (IOException e) {
			System.out.println("Error while cleaning stack notification: "+e.getMessage());
		} catch (SlackApiException e) {
			System.out.println("Error while cleaning stack notification: "+e.getMessage());
		}
    	
	}
	public PublishFactory() {
		try {
			slackClient = Slack.getInstance().methods();
			
			properties=new Properties();
			File propertiesFile = new File("src/test/resources/publish/config.properties");
			FileInputStream inputStream= new FileInputStream(propertiesFile);		
			properties.load(inputStream);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String publishTestReport() {
		String targetFolder = LocalDateTime.now().toString();
		String testReportUrl = properties.getProperty("testReportUrl");
		String testReportPath = properties.getProperty("testReportPath");
		String reportAtJenkinsWorkspace = properties.getProperty("reportAtJenkinsWorkspace");
		String reportLink = "N/A";
		try {					
			if(ListenerFactory.operatingSystem == "linux") {
				reportLink = testReportUrl+"/testreport/"+targetFolder+"/index.html";
				 
				Process process = Runtime.getRuntime().exec(new String[]{"bash","-c","sudo cp -r "+reportAtJenkinsWorkspace+" "+testReportPath+targetFolder});

		        StringBuilder output = new StringBuilder();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		        String line;
		        while ((line = reader.readLine()) != null) {
		            output.append(line + "\n");
		        }

		        int exitVal = process.waitFor();
		        if (exitVal == 0) {
		            System.out.println("Test report published.");
		            System.out.println(output);				            
		        } else {
		        	System.out.println("Test report not published.");				            
		        }				
			}
		} 
		catch (Exception e) {
			System.out.println("Error while publishing test report: "+e.getMessage());
			e.printStackTrace();
		}
		return reportLink; 
	}
	
	public void publishSlackNotification(JSONObject testSummary) {	
		try {
			String slackBotToken = properties.getProperty("slackBotToken");
			String slackChannelId = properties.getProperty("slackChannelId");
			String slackBotId = properties.getProperty("slackBotId");
			ChatPostMessageResponse postResponse = null;
			boolean sendSlackNotification = (System.getProperty("notifySlack") == null) ? false : Boolean.getBoolean("notifySlack");
			
			cleanSlackNotifications(slackBotToken, slackChannelId, slackBotId);
			if(sendSlackNotification) {
				if(testSummary.getInt("failed") > 0) {	
					postResponse = slackClient.chatPostMessage(request -> request		                    
					                    .token(slackBotToken)
					                    .channel(slackChannelId)
					                    .text(" :red_circle: *Test failed* on "+new Date()+"\n"
											+ " <!channel> Please review failed test(s).\n"
											+ " *Run*: "+testSummary.getInt("executed")+" | "+"*Passed*: "+testSummary.getInt("passed")+" | "+"*Failed*: "+testSummary.getInt("failed")+"\n"
											+ "<"+testSummary.getString("report")+"|*Test Report*>"));
								
				}
				else {
					postResponse = slackClient.chatPostMessage(request -> request		                    
					                    .token(slackBotToken)
					                    .channel(slackChannelId)
					                    .text(" :large_green_circle: *Test passed* on "+new Date()+"\n"
					                    	+ " *Run*: "+testSummary.getInt("executed")+" | "+"*Passed*: "+testSummary.getInt("passed")+" | "+"*Failed*: "+testSummary.getInt("failed")+"\n"									 
											+ "<"+testSummary.getString("report")+"|*Test Report*>"));
		
					
				}
				System.out.println("Slack notificaition response: "+postResponse);
			}
		}
		catch (IOException e) {			
			System.out.println("Error while publishing stack notification: "+e.getMessage());			
		} catch (SlackApiException e) {
			System.out.println("Error while publishing stack notification: "+e.getMessage());
		}		
	}
	

	
	public void publishDataStackRecord(JSONObject testDetailedSummary) {
		try {
			OkHttpClient client = new OkHttpClient();
			boolean sendDataStackRecord = (System.getProperty("recordDataStack") == null) ? false : Boolean.getBoolean("recordDataStack");
			String dataStackUrl = properties.getProperty("dataStackUrl");			
			String dataStackLoginApi = properties.getProperty("dataStackLoginApi");
			String dataStackPublishApi = properties.getProperty("dataStackPublishApi");			
			
			if(sendDataStackRecord) {
				
				JSONObject credentials = new JSONObject();
				credentials.put("username", properties.getProperty("dataStackBotUsername"));
				credentials.put("password", properties.getProperty("dataStackBotPassword"));	
				
				RequestBody loginBody = RequestBody.create(JSON, credentials.toString());
				Request loginRequest = new Request.Builder().url(dataStackUrl+dataStackLoginApi).post(loginBody).build();
				Response loginResponse = client.newCall(loginRequest).execute();
			    String loginResponseString = loginResponse.body().string();			    
			    JSONObject loginReponseJSON = new JSONObject(loginResponseString);
			    
			    RequestBody testSummaryBody = RequestBody.create(JSON, testDetailedSummary.toString());
			    Request testSummaryRequest = new Request.Builder().header("Authorization", "JWT "+loginReponseJSON.getString("token")).
			    										url(dataStackUrl+dataStackPublishApi).post(testSummaryBody).build();
			    Response testSummaryResponse = client.newCall(testSummaryRequest).execute();
			    
			    System.out.println("Data service response: "+testSummaryResponse.toString());	
			    System.out.println("Final report:"+testDetailedSummary.toString());
			}
		}
		catch (Exception e) {			
			System.out.println("Error while publishing data service response: "+e.getMessage());
			e.printStackTrace();
		}
	}
}
