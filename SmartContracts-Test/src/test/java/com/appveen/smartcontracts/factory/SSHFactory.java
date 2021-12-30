package com.appveen.smartcontracts.factory;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class SSHFactory {
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private JSONObject sshDetails = null;
	private JSONObject mongoDetails = null;
	private JSch jsch = null;

	private static Session SSH_SESSION = null;
	private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;
	
	public SSHFactory() throws Exception {
		try {
			sshDetails = testData.getJSONArray("ssh_details").getJSONObject(0);
			mongoDetails = testData.getJSONArray("mongo_details").getJSONObject(0);
		    java.util.Properties config = new java.util.Properties();
		    config.put("StrictHostKeyChecking", "no");		    
		    
		    jsch = new JSch();
            jsch.addIdentity(sshDetails.getString("ssh_private_key_url"));
		    SSH_SESSION = jsch.getSession(sshDetails.getString("ssh_username"), sshDetails.getString("ssh_host"), sshDetails.getInt("ssh_port"));
		    
		    SSH_SESSION.setConfig(config);
		    SSH_SESSION.connect(SESSION_TIMEOUT);
		    SSH_SESSION.setPortForwardingL(mongoDetails.getInt("mongo_local_port"), mongoDetails.getString("mongo_remote_host"), mongoDetails.getInt("mongo_remote_port"));

		}
		catch (Exception e) {
			throw new Exception("Error while initiating SSH connection");
		}		
	}
	
	public void executeCommand(ExtentTest step, String command) throws Exception {
		Channel channel = null;
		try {		
	        channel = SSH_SESSION.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	         
	        InputStream input = channel.getInputStream();
	        channel.connect(CHANNEL_TIMEOUT);       
	        step.log(Status.INFO, "Command executed: "+command);
	        try{
	            InputStreamReader inputReader = new InputStreamReader(input);
	            BufferedReader bufferedReader = new BufferedReader(inputReader);
	            String line = null;
	             
	            while((line = bufferedReader.readLine()) != null){
	                System.out.println(line);
	            }
	            bufferedReader.close();
	            inputReader.close();
	        }
	        catch(IOException e){
	        	step.log(Status.FAIL, "Error while fetching output for command: "+command);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while fetching output for command: "+command);
	        }
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while executing for command: "+command);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while executing for command: "+command);
		}
		finally {
	        channel.disconnect();
	    }     
	}
	
	public void fetchFile(ExtentTest step, String filePath) throws Exception {
		Channel channel = null;
		ChannelSftp channelSftp = null;
		try {		
			channel = SSH_SESSION.openChannel("sftp");
            channel.connect(CHANNEL_TIMEOUT);
            
            channelSftp = (ChannelSftp) channel;
            channelSftp.get(filePath, "./report/"+filePath);
            step.log(Status.INFO, "Fetched file: "+filePath+" to local");
        }
		catch (Exception e) {
			step.log(Status.FAIL, "Error while fetching file to local: "+filePath);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while fetching file to local: "+filePath);
		}
		finally {
			channelSftp.exit();
	        channel.disconnect();
	    }     
	}
	
	public void closeSSH(ExtentTest step) throws Exception {
		try {
		    SSH_SESSION.delPortForwardingL(mongoDetails.getInt("mongo_local_port"));
		    SSH_SESSION.disconnect();
		    step.log(Status.INFO, "SSH connection closed");
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating SSH connection");
			throw new Exception("Error while initiating SSH connection");
		}
	}
	
	
}
