package entity;

import org.json.JSONException;
import org.json.JSONObject;
import secure.Encryption;

public class User {
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private long queryCount;
	
	public User(String userId, String firstName, String lastName, String email, String password) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		try {
			this.password = Encryption.encrypt(password);
		} catch (Exception e) {
			System.err.println("Password setting error");
		}
		this.queryCount = 0;
	}
	
	// setter
	public void setFirstName(String newFirstName) {
		this.firstName = newFirstName;
	}
	public void setLastName(String newLastName) {
		this.lastName = newLastName;
	}
	public void setEmail(String newEmail) {
		this.email = newEmail;
	}
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}
	// author: Jin Dai 04102018
	// getter
	public String getUserId() {
		return userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public long getQueryCount() {
		return queryCount;
	}
	
	// Convert to JSON
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("user_id", userId);
			obj.put("firstName", firstName);
			obj.put("lastName", lastName);
			obj.put("email", email);
			obj.put("password", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
