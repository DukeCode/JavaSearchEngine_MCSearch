package entity;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

public class Query {
	private String queryID;
	private String userID;
	private String content;
	private String userTime; // local time based on user location, provided by front-end
	private String queryType;
	private long timeStamp;
	
	public Query(String queryID, String userID, String content, String userTime, String queryType) {
		this.queryID = queryID;
		this.userID = userID;
		this.content = content;
		this.userTime = userTime;
		Timestamp tempTime = new Timestamp(System.currentTimeMillis());
		this.timeStamp = tempTime.getTime();
		this.queryType = queryType;
	}
	
	// Getter
	public String getQueryID() {
		return queryID;
	}
	public String getUserID() {
		return userID;
	}
	public String getContent() {
		return content;
	}
	public String getUserTime() {
		return userTime;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public String getQueryType() {
		return queryType;
	}
	
	// Convert to JSON
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("query_id", queryID);
			obj.put("userID", userID);
			obj.put("content", content);
			obj.put("userTime", userTime);
			obj.put("timeStamp", timeStamp);
			obj.put("queryType", queryType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
