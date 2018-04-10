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
import entity.User;
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
	public void addFavoriteDocs(String userId, List<String> docIds) {
		db.getCollection("users").updateOne(new Document("user_id", userId),
				new Document("$push", new Document("favorite", new Document("$each", docIds))));
	}

	@Override
	public void deleteFavoriteDocs(String userId, List<String> docIds) {
		db.getCollection("users").updateOne(new Document("user_id", userId),
				new Document("$pullAll", new Document("favorite", docIds)));
	}

	@Override
	public Set<String> getFavoriteDocIds(String userId) {
		Set<String> favoriteDocIds = new HashSet<String>();
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", userId));
		if (iterable.first().containsKey("favorite")) {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) iterable.first().get("favorite");
			favoriteDocIds.addAll(list);
		}
		return favoriteDocIds;
	}

	@Override
	public Set<MyDoc> getFavoriteDocs(String userId) {
		Set<String> favoriteDocIds = getFavoriteDocIds(userId);
		Set<MyDoc> favoriteDocs = new HashSet<>();
		for (String favoriteDocId : favoriteDocIds) {
			FindIterable<Document> iterable = db.getCollection("docs").find(eq("doc_id", favoriteDocId));
			Document doc = iterable.first();
			MyDocBuilder builder = new MyDocBuilder();
			builder.setDocId(doc.getString("doc_id"));
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
	public Set<String> getQueryIds(String userId) {
		Set<String> queryIds = new HashSet<String>();
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", userId));
		if (iterable.first().containsKey("query")) {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) iterable.first().get("query");
			queryIds.addAll(list);
		}
		return queryIds;
	}
	
	@Override
	public Set<Query> getQueryLog(String userId) {
		Set<String> queryIds = getQueryIds(userId);
		Set<Query> queryLog = new HashSet<Query>();
		for (String queryId : queryIds) {
			FindIterable<Document> iterable = db.getCollection("queries").find(eq("query_id", queryId));
			Document query = iterable.first();
			Query q = new Query(query.getLong("query_id"), query.getString("user_id"), query.getString("content"),
					query.getLong("userTime"), query.getLong("timeStamp"), query.getString("queryType"));

			queryLog.add(q);
		}
		return queryLog;
	}

	@Override
	public void addQuery(Query query) {
		FindIterable<Document> iterable = db.getCollection("queries").find(eq("query_id", query.getQueryId()));
		if (iterable.first() == null) {
			FindIterable<Document> iterableUser = db.getCollection("users").find(eq("user_id", query.getQueryId()));
			long queryCount = iterableUser.first().getLong("queryCount");
			queryCount++;
			db.getCollection("users").updateOne(eq("user_id", query.getUserId()), new Document("$set", new Document("queryCount", queryCount)));
			db.getCollection("users").updateOne(new Document("user_id", query.getUserId()),
					new Document("$push", new Document("query", new Document("$each", query.getQueryId()))));
			db.getCollection("queries")
					.insertOne(new Document().append("query_id", queryCount).append("userId", query.getUserId())
							.append("content", query.getContent()).append("userTime", query.getUserTime()).append("timeStamp", query.getTimeStamp())
							.append("queryType", query.getQueryType()));
		}
	}
	
	@Override
	public void deleteQuery(String userId, List<String> queryIds) {
		db.getCollection("users").updateOne(new Document("user_id", userId),
				new Document("$pullAll", new Document("query", queryIds)));
		for (String queryId : queryIds) {
			db.getCollection("queries").deleteOne(new Document("query_id", queryId));
		}
	}
	
	@Override
	public void addDoc(MyDoc doc) {
		FindIterable<Document> iterable = db.getCollection("docs").find(eq("doc_id", doc.getDocId()));
		if (iterable.first() == null) {
			db.getCollection("docs")
					.insertOne(new Document().append("doc_id", doc.getDocId()).append("criticTitle", doc.getCriticTitle())
							.append("movieTitle", doc.getMovieTitle()).append("author", doc.getAuthor())
							.append("url", doc.getUrl()).append("imageUrl", doc.getImageUrl())
							.append("mpaaRate", doc.getMpaaRate()).append("nytPick", doc.getNytPick())
							.append("summary", doc.getSummary()));
		}
	}
	
	@Override
	public void addUser(User user) {
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", user.getUserId()));
		if (iterable.first() == null) {
			db.getCollection("users")
			.insertOne(new Document().append("user_id", user.getUserId()).append("firstName", user.getFirstName())
					.append("lastName", user.getLastName()).append("email", user.getEmail())
					.append("password", user.getPassword()).append("queryCount",user.getQueryCount()));
		}
	}

	@Override
	public String getFullname(String userId) {
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", userId));
		StringBuffer sb = new StringBuffer();
		Document user = iterable.first();
		sb.append(user.get("firstName")).append(user.get("lastName"));
		return sb.toString();
	}

	@Override
	public boolean verifyLogin(String userId, String password) {
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", userId));
		String pwd = iterable.first().getString("password");
		if (password.equals(pwd)) {
			return true;
		}
		return false;
	}
}
//author: Jin Dai 04102018