package com.appveen.smartcontracts.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONTokener;


public class DataFactory {
	private JSONObject testData = new JSONObject();	
	private final static Integer RANDOM_LOWER_BOUND = 100;
	private final static Integer RANDOM_UPPER_BOUND = 999999999;
	private String folderPath = null;
	private File folder = null;
	private File[] listOfFiles = null;
	private JSONObject processInput(JSONObject jsonData) {
		Random random = new Random();
		String processedString = "";
		Integer randomNumber = random.nextInt(RANDOM_UPPER_BOUND) + RANDOM_LOWER_BOUND;
		String dateFormat = "dd-MMM-yyyy";
		String monthFormat = "MMM";
		String yearFormat = "yyyy";
		String monthYearFormat = "MMM-yyyy";
		String dayFormat = "dd";
		String dayOfWeekFormat = "EEEE";
		DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern(dateFormat);
		DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern(monthFormat);
		DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern(yearFormat);
		DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern(monthYearFormat);
		DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern(dayFormat);
		DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern(dayOfWeekFormat);
		LocalDate today = LocalDate.now();
		LocalDate previousDay = LocalDate.now().minusDays(1);
		LocalDate nextDay = LocalDate.now().plusDays(1);
		LocalDate oneMonthAgoToday = LocalDate.now().minusMonths(1);
		LocalDate oneMonthFromToday = LocalDate.now().plusMonths(1);
		LocalDate oneYearFromToday = LocalDate.now().plusYears(1);
		LocalDate startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
		LocalDate endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
		
		processedString = jsonData.toString().replaceAll("\\{{2}random\\}{2}", randomNumber.toString());
		processedString = processedString.replaceAll("\\{{2}today\\}{2}", dateTimeFormatter.format(today));
		processedString = processedString.replaceAll("\\{{2}previousDay\\}{2}", dateTimeFormatter.format(previousDay));
		processedString = processedString.replaceAll("\\{{2}nextDay\\}{2}", dateTimeFormatter.format(nextDay));
		processedString = processedString.replaceAll("\\{{2}oneMonthAgoToday\\}{2}", dateTimeFormatter.format(oneMonthAgoToday));
		processedString = processedString.replaceAll("\\{{2}oneMonthFromToday\\}{2}", dateTimeFormatter.format(oneMonthFromToday));
		processedString = processedString.replaceAll("\\{{2}oneYearFromToday\\}{2}", dateTimeFormatter.format(oneYearFromToday));
		processedString = processedString.replaceAll("\\{{2}currentMonth\\}{2}", monthFormatter.format(today));
		processedString = processedString.replaceAll("\\{{2}currentYear\\}{2}", yearFormatter.format(today));
		processedString = processedString.replaceAll("\\{{2}nextYear\\}{2}", yearFormatter.format(oneYearFromToday));
		processedString = processedString.replaceAll("\\{{2}currentMonthAndYear\\}{2}", monthYearFormatter.format(today).toUpperCase());
		processedString = processedString.replaceAll("\\{{2}todaysDate\\}{2}", dayFormatter.format(today));
		processedString = processedString.replaceAll("\\{{2}dayOfWeek\\}{2}", dayOfWeekFormatter.format(today));
		processedString = processedString.replaceAll("\\{{2}startOfMonth\\}{2}", dateTimeFormatter.format(startOfMonth));
		processedString = processedString.replaceAll("\\{{2}endOfMonth\\}{2}", dateTimeFormatter.format(endOfMonth));
		
		return new JSONObject(processedString);
	}
	
	public DataFactory() throws Exception {	
		try {
			folderPath = (System.getProperty("testdata") == null) ? "src/test/resources/testdata": System.getProperty("testdata").toString();
			folder = new File(folderPath);
			listOfFiles = folder.listFiles();	
			for(int index = 0; index < listOfFiles.length; index++) {
				
				if(listOfFiles[index].isFile()) {
					String fileName = listOfFiles[index].getName();
					int extensionIndex = fileName.indexOf(".");
					String fileExtension = fileName.substring(extensionIndex+1);
					if(fileExtension.equals("json")) {
						
						File jsonFile = new File(listOfFiles[index].getAbsolutePath());
						InputStream inputStream = new FileInputStream(jsonFile);					
					    JSONTokener tokener = new JSONTokener(inputStream);						    
					    JSONObject inputObject = new JSONObject(tokener);
					    JSONObject processedObject = processInput(inputObject);
					    
						testData.put(fileName.substring(0, extensionIndex), processedObject);
					}					
				}
			}
		}
		catch (Exception e) {
			System.out.println("Error while fetching test data");
			System.out.println(e.getMessage());		
			throw new Exception("Error while fetching test data");
		}
	}
	
	
	public JSONObject getTestData(String testID) {		
		return testData.getJSONObject(testID);
	}
}
