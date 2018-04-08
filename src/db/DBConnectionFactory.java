package db;

import db.mongodb.MongoDBConnection;

public class DBConnectionFactory {
	private static final String DEFAULT_DB = "mongodb";

	// Create a DBConnection based on given db type.
	public static DBConnection getDBConnection(String dbPipeline) {
		switch (dbPipeline) {
		case "": // for future extention
			return null;
		case "mongodb":
			return new MongoDBConnection();
		default:
			throw new IllegalArgumentException("Invalid db " + dbPipeline);
		}
	}

	// overloading
	public static DBConnection getDBConnection() {
		return getDBConnection(DEFAULT_DB);
	}
}
