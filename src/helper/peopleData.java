package helper;

import android.content.Context;
import android.content.SharedPreferences;

public class peopleData {
	
	String name;
	String phone;
	String email;
	String isRegister;
	String lastLocation;
	String regId;
	
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	public static final String XML_NAME 			= "appPrefs";
	public static final String PREFS_NAME 			= "prefName";
	public static final String PREFS_PHONE 			= "prefPhone";
	public static final String PREFS_EMAIL 			= "prefEmail";
	public static final String PREFS_IS_REGISTER 	= "prefIsRegister";
	public static final String PREFS_LAST_LOCATION 	= "prefLastLocation";
	public static final String PREFS_REG_ID 		= "prefRegId";
	
	public peopleData(Context c) {
		// TODO Auto-generated constructor stub
		settings = c.getSharedPreferences(XML_NAME, 0);
		name 		= settings.getString(PREFS_NAME, "");
		phone		= settings.getString(PREFS_PHONE, "");
		email		= settings.getString(PREFS_EMAIL, "");
		isRegister	= settings.getString(PREFS_IS_REGISTER, "");
		lastLocation= settings.getString(PREFS_LAST_LOCATION, "");
		regId		= settings.getString(PREFS_REG_ID, "");
	}
	
	public Boolean isLogin(){
		if ((name.equals("") && phone.equals("") && email.equals("")) || isRegister.equals(""))
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
	
	public String getIsRegister() {
		return isRegister;
	}
	
	public String getLastLocation() {
		return lastLocation;
	}
	
	public String getRegId() {
		return regId;
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
	
	public void setIsRegister(String isRegister) {
		this.isRegister = isRegister;
		updatePrefs();
	}
	
	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
		updatePrefs();
	}
	
	public void setRegId(String regId) {
		this.regId = regId;
		updatePrefs();
	}
	
	public void updatePrefs(){
		editor = settings.edit();
		editor.putString(PREFS_NAME, name);
		editor.putString(PREFS_EMAIL, email);
		editor.putString(PREFS_PHONE, phone);
		editor.putString(PREFS_IS_REGISTER, isRegister);
		editor.putString(PREFS_REG_ID, regId);
		editor.putString(PREFS_LAST_LOCATION, lastLocation);
		editor.commit();
	}
}