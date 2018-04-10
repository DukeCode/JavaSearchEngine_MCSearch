package db.mongodb;

import java.text.ParseException;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

// Create tables for MongoDB (all pipelines).
public class MongoDBTableCreation {
	// Run as Java application to create MongoDB tables with index.
	public static void main(String[] args) throws ParseException {
		MongoClient mongoClient = new MongoClient();
		MongoDatabase db = mongoClient.getDatabase(MongoDBUtil.DB_NAME);

		// Step 1: remove old tables.
		db.getCollection("users").drop();
		db.getCollection("queries").drop();
		db.getCollection("docs").drop();
		
		// Step 2: create new tables, populate data and create index.

		// make sure IDs are unique.
		IndexOptions indexOptions = new IndexOptions().unique(true);

		// use 1 for ascending index , -1 for descending index
		db.getCollection("users").createIndex(new Document("user_id", 1), indexOptions);

		// make sure item_id is unique.
		db.getCollection("queries").createIndex(new Document("query_id", 1), indexOptions);
		
		// make sure doc_id is unique
		db.getCollection("docs").createIndex(new Document("doc_id", 1), indexOptions);

		mongoClient.close();
		System.out.println("Import is done successfully.");
	}
}
//author: Jin Dai 04102018