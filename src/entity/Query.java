package entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Query {
	private Long queryId;
	private String userId;
	private String content;
	private Long userTime; // local time based on user location, provided by front-end
	private String queryType;
	private long timeStamp;
	
	public Query(Long queryId, String userId, String content, Long userTime, Long timeStamp, String queryType) {
		this.queryId = queryId;
		this.userId = userId;
		this.content = content;
		this.userTime = userTime;
		this.timeStamp = timeStamp; // timeStamp is generated based on milis
		this.queryType = queryType;
	}
	
	public Query(String userId, String content, Long userTime, Long timeStamp, String queryType) {
		this.queryId = 0L;
		this.userId = userId;
		this.content = content;
		this.userTime = userTime;
		this.timeStamp = timeStamp; // timeStamp is generated based on milis
		this.queryType = queryType;
	}
	// author: Jin Dai 04102018
	// Getter
	public Long getQueryId() {
		return queryId;
	}
	public String getUserId() {
		return userId;
	}
	public String getContent() {
		return content;
	}
	public Long getUserTime() {
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
			obj.put("query_id", queryId);
			obj.put("userId", userId);
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
