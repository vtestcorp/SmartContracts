package com.appveen.smartcontracts.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.JSONTokener;

public class LocatorFactory {
	private JSONObject locators = new JSONObject();
		
	public void setLocators() throws Exception {		
		try {
			String folderPath = (System.getProperty("locator") == null) ? "src/test/resources/locators": System.getProperty("locator").toString();
			File folder = new File(folderPath);
			File[] listOfFiles = folder.listFiles();
			
			for(int index = 0; index < listOfFiles.length; index++) {
				
				if(listOfFiles[index].isFile()) {
					String fileName = listOfFiles[index].getName();
					int extensionIndex = fileName.indexOf(".");
					String fileExtension = fileName.substring(extensionIndex+1);
					if(fileExtension.equals("json")) {
						
						File jsonFile = new File(listOfFiles[index].getAbsolutePath());
						InputStream inputStream = new FileInputStream(jsonFile);			    
					
					    JSONTokener tokener = new JSONTokener(inputStream);
					    JSONObject object = new JSONObject(tokener);
						locators.put(fileName.substring(0, extensionIndex), object);
					}
					
				}
			}
			}
			catch (Exception e) {
				throw new Exception("Error while fetching locator");
			}
		
	}

	
	public JSONObject getLocators(String pageName) {
		return locators.getJSONObject(pageName).getJSONArray("locators").getJSONObject(0);
	}
}
