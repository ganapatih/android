package php.id.ganapatih;

import helper.CommonUtilities;
import helper.ConnectionDetector;
import helper.RegisterAsync;
import helper.peopleData;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Welcome extends Activity {

	peopleData p;
	String userName,userPhone,userEmail;
	
	private Context c;
	private Activity act;
	
	String SENDER_ID = CommonUtilities.SENDER_ID;
	String regid;
	
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context;
	ProgressDialog pd;
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		getConnection();
		initGCM();
		
		act = getParent();
		c = this;
		context = getApplicationContext();

		p = new peopleData(getApplicationContext());
		initData();

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		((EditText) findViewById(R.id.editText1)).setText(p.getName());
		((EditText) findViewById(R.id.editText2)).setText(p.getPhone());
		((EditText) findViewById(R.id.editText3)).setText(p.getEmail());

	}

	public void goLogin(View v) {
		userName = ((EditText) findViewById(R.id.editText1)).getText().toString(); 
		userPhone = ((EditText) findViewById(R.id.editText2)).getText().toString(); 
		userEmail = ((EditText) findViewById(R.id.editText3)).getText().toString();
		
		// set id to save in server		
		String GcmId = regid;
		
		RegisterAsync R = new RegisterAsync(c, act) {
			@Override
			public void onResponseReceived(String result) {

				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_SHORT).show();

				p.setName(userName);
				p.setPhone(userPhone);
				p.setEmail(userEmail);

				Intent intent = new Intent(Welcome.this, Home.class);
				startActivity(intent);
				finish();
				
			}
		};
		R.execute(userName,userPhone,userEmail,GcmId);	
	}

	public void initData() {
		String userEmail = "", userName = "", userPhone = "";
		
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(getApplicationContext())
				.getAccounts();

		for (Account account : accounts) {
			Log.d("data", account.type + " => " + account.name);
			if (emailPattern.matcher(account.name).matches()) {
				userEmail = account.name;
			}
			if (account.type.equals("com.whatsapp"))
				userPhone = account.name;
		}

		if (userPhone.equals("")) {
			userPhone = getTelp();
		}

		userName = getUsername();
		
		p.setName(userName);
		p.setPhone(userPhone);
		p.setEmail(userEmail);
	}

	public String getTelp() {
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String number = tm.getLine1Number();
		Toast.makeText(getApplicationContext(), number, Toast.LENGTH_SHORT)
				.show();
		return number;
	}

	public String getUsername() {
		AccountManager manager = AccountManager.get(this);
		Account[] accounts = manager.getAccountsByType("com.google");
		List<String> possibleEmails = new LinkedList<String>();

		for (Account account : accounts) {
			possibleEmails.add(account.name);
		}

		if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
			String email = possibleEmails.get(0);
			String[] parts = email.split("@");
			if (parts.length > 0 && parts[0] != null)
				return parts[0];
			else
				return null;
		} else
			return null;
	}
	
	private void getConnection(){
		Boolean connect = (new ConnectionDetector(getApplicationContext())).isConnectingToInternet();
		  
		  if(!connect)
			new AlertDialog.Builder(this)
					.setMessage(getString(R.string.warning_noconnection))
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
								}
							}).show();
	}
	
	private void initGCM(){
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);
			Log.d("regID1", regid);

			if (regid.isEmpty()) {
				registerInBackground();
			}
		} else {
			
		}
	}
	
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        return "";
	    }
	    return registrationId;
	}
	
	private SharedPreferences getGCMPreferences(Context context) {
	    return getSharedPreferences(Welcome.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
	
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	private void registerInBackground() {
		
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    Log.d("regID2", regid);
                    msg = "Device registered, registration ID=" + regid;
                    sendRegistrationIdToBackend();
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
            
            protected void onPreExecute() {
            	pd = new ProgressDialog(Welcome.this);
        		pd.setMessage("preparing system");
        		pd.show();
        		pd.setCancelable(false);
            };
            
            @Override
            protected void onPostExecute(String msg) {
            	pd.dismiss();
            }
        }.execute(null, null, null);
    }
	
	private void sendRegistrationIdToBackend() {
		// Your implementation here.
	}
	
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
}
