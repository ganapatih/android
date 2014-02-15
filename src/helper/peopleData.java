package helper;

import android.content.Context;
import android.content.SharedPreferences;

public class peopleData {
	
	String name;
	String phone;
	String email;
	
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	public static final String XML_NAME 	= "appPrefs";
	public static final String PREFS_NAME 	= "prefName";
	public static final String PREFS_PHONE 	= "prefPhone";
	public static final String PREFS_EMAIL 	= "prefEmail";
	
	public peopleData(Context c) {
		// TODO Auto-generated constructor stub
		settings = c.getSharedPreferences(XML_NAME, 0);
		name 	= settings.getString(PREFS_NAME, "");
		phone	= settings.getString(PREFS_PHONE, "");
		email	= settings.getString(PREFS_EMAIL, "");
	}
	
	public Boolean isLogin(){
		if (name.equals("") && phone.equals("") && email.equals(""))
			return false;
		else return true;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setName(String name) {
		this.name = name;
		updatePrefs();
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
		updatePrefs();
	}
	
	public void setEmail(String email) {
		this.email = email;
		updatePrefs();
	}
	
	public void updatePrefs(){
		editor = settings.edit();
		editor.putString(PREFS_NAME, name);
		editor.putString(PREFS_EMAIL, email);
		editor.putString(PREFS_PHONE, phone);
		editor.commit();
	}
}