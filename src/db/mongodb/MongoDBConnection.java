package db.mongodb;

// This line needs manual import.
import static com.mongodb.client.model.Filters.eq;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import db.DBConnection;
import entity.MyDoc;
import entity.Query;
import entity.MyDoc.MyDocBuilder;

public class MongoDBConnection implements DBConnection {
	private MongoClient mongoClient;
	private MongoDatabase db;

	public MongoDBConnection() {
		// Connects to local mongodb server.
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase(MongoDBUtil.DB_NAME);
	}

	@Override
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	@Override
	public void setFavoriteDocs(String userID, List<String> docIDs) {
		db.getCollection("users").updateOne(new Document("user_id", userID),
				new Document("$push", new Document("favorite", new Document("$each", docIDs))));
	}

	@Override
	public void unsetFavoriteDocs(String userID, List<String> docIDs) {
		db.getCollection("users").updateOne(new Document("user_id", userID),
				new Document("$pullAll", new Document("favorite", docIDs)));
	}

	@Override
	public Set<String> getFavoriteDocIds(String userID) {
		Set<String> favoriteDocIds = new HashSet<String>();
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", userID));
		if (iterable.first().containsKey("favorite")) {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) iterable.first().get("favorite");
			favoriteDocIds.addAll(list);
		}
		return favoriteDocIds;
	}

	@Override
	public Set<MyDoc> getFavoriteDocs(String userID) {
		Set<String> favoriteDocIds = getFavoriteDocIds(userID);
		Set<MyDoc> favoriteDocs = new HashSet<>();
		for (String favoriteDocId : favoriteDocIds) {
			FindIterable<Document> iterable = db.getCollection("docs").find(eq("doc_id", favoriteDocIds));
			Document doc = iterable.first();
			MyDocBuilder builder = new MyDocBuilder();
			builder.setDocID(doc.getString("doc_id"));
			builder.setCriticTitle(doc.getString("criticTitle"));
			builder.setMovieTitle(doc.getString("movieTitle"));
			builder.setAuthor(doc.getString("author"));
			builder.setUrl(doc.getString("url"));
			builder.setImageUrl(doc.getString("imageUrl"));
			builder.setMpaaRate(doc.getString("mpaaRate"));
			builder.setNytPick(doc.getString("nytPick"));
			builder.setSummary(doc.getString("summary"));

			favoriteDocs.add(builder.build());
		}
		return favoriteDocs;
	}

	@Override
	public Set<Query> getQueryLog(String userID) {
		Set<Query> queryLog = new HashSet<Query>();
		FindIterable<Document> queries = db.getCollection("queries").find(eq("userID", userID));
		if (queries == null) {
			return queryLog;
		}
		for (Document doc : docs) {
			
		}
		if (iterable.first().containsKey("favorite")) {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) iterable.first().get("favorite");
			favoriteDocIds.addAll(list);
		}
		return favoriteDocIds;
	}

	@Override
	public List<Event> searchEvents(String userId, double lat, double lon, String term) {
		// Connect to external API
		API api = APIFactory.getExternalAPI(); // moved here
		List<Event> events = api.search(lat, lon, term);
		for (Event event : events) {
			saveEvent(event);
		}
		return events;
	}

	@Override
	public void saveEvent(Event event) {

		FindIterable<Document> iterable = db.getCollection("items").find(eq("item_id", event.getEventId()));
		if (iterable.first() == null) {
			db.getCollection("items")
					.insertOne(new Document().append("item_id", event.getEventId()).append("name", event.getName())
							.append("city", event.getCity()).append("state", event.getState())
							.append("country", event.getCountry()).append("zip_code", event.getZipcode())
							.append("rating", event.getRating()).append("address", event.getAddress())
							.append("latitude", event.getLatitude()).append("longitude", event.getLongitude())
							.append("description", event.getDescription()).append("snippet", event.getSnippet())
							.append("snippet_url", event.getSnippetUrl()).append("image_url", event.getImageUrl())
							.append("url", event.getUrl()).append("categories", event.getCategories()));
		}
	}

	@Override
	public String getFullname(String userId) {
		return null;
	}

	@Override
	public boolean verifyLogin(String userId, String password) {
		return false;
	}
}
