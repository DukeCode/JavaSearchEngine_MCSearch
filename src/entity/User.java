package entity;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String userID;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	public User(String userID, String firstName, String lastName, String email, String password) {
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
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
	
	// getter
	public String getUserID() {
		return userID;
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
	
	// Convert to JSON
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("user_id", userID);
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
