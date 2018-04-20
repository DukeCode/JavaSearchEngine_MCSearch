package db.mongodb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

import path.AllPath;

// Create tables for MongoDB (all pipelines).
public class MongoDBTableCreation {
	// Run as Java application to create MongoDB tables with index.
	public static void main(String[] args) throws ParseException, IOException {
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
		db.getCollection("users").insertOne(new Document().append("user_id", "1111").append("first_name", "John").append("last_name", "Smith")
				.append("email", "Smith@pitt.edu").append("password", "3229c1097c00d497a0fd282d586be050"));
//		.append("favorite", "...")
		// make sure item_id is unique.
		db.getCollection("queries").createIndex(new Document("query_id", 1), indexOptions);
//		db.getCollection("queries").insertOne(new Document().append("query_id", "1111").append("userId", "1111")
//				.append("queryContent", "XXX").append("userTime", "2018-01-01 11:11:11").append("timeStamp", "111111111"));
		// make sure doc_id is unique
		db.getCollection("docs").createIndex(new Document("doc_id", 1), indexOptions);
		db.getCollection("docs").insertOne(new Document().append("doc_id", "11111").append("criticTitle", "abc").append("movieTitle", "123")
				.append("email", "Smith@pitt.edu").append("password", "3229c1097c00d497a0fd282d586be050"));
		
		File f = new File(AllPath.parsedResource);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		while ((line = br.readLine()) != null) {
			String docId = line;
			String url = br.readLine();
			String imgUrl = br.readLine();
			String movieTitle = br.readLine();
			String mpaaRate = br.readLine();
			String nytPick = br.readLine();
			String author = br.readLine();
			String criticTitle = br.readLine();
			String summary = br.readLine();
			String publicationDate = br.readLine();
			db.getCollection("docs").insertOne(new Document().append("doc_id", docId).append("url", url)
					.append("imgUrl", imgUrl).append("movieTitle", movieTitle).append("mpaaRate", mpaaRate)
					.append("nytPick", nytPick).append("author", author).append("criticTitle", criticTitle)
					.append("summary", summary).append("publicationDate", publicationDate));
		}
		br.close();
		mongoClient.close();
		System.out.println("Import is done successfully.");
	}
}
//author: Jin Dai 04102018