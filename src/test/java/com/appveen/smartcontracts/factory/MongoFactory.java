package com.appveen.smartcontracts.factory;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.MongoDatabase;

public class MongoFactory {
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private JSONObject mongoDetails = null;
	private MongoDatabase database = null;
	
	@SuppressWarnings("deprecation")
	public MongoFactory() throws Exception {
		try {
			mongoDetails = testData.getJSONArray("mongo_details").getJSONObject(0);
			
			MongoClient mongoClient = new MongoClient(mongoDetails.getString("mongo_local_host"), mongoDetails.getInt("mongo_local_port"));
		    mongoClient.setReadPreference(ReadPreference.nearest());
		    database = mongoClient.getDatabase(mongoDetails.getString("mongo_database"));		    
		}
		catch (Exception e) {
			throw new Exception("Error while connecting to MongoDB");
		}
	}
	
	public JSONObject findOneFromCollection(String collectionName, String key, String value) throws Exception {
		JSONObject result = null;
		try {
			MongoCollection<Document> collection = database.getCollection(collectionName);			
			Document document = collection.find(eq(key, value)).first();			
			result = new JSONObject(document.toJson());
		}
		catch (Exception e) {
			throw new Exception("Error while querying collection: "+collectionName);
		}
		return result;		
	}
	
	
	
	

}
