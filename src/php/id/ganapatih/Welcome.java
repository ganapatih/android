package php.id.ganapatih;

import helper.RegisterAsync;
import helper.peopleData;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Welcome extends Activity {

	peopleData p;
	String userName,userPhone,userEmail;
	
	private Context c;
	private Activity act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		act = getParent();
		c = this;

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
		String GcmId = "NULL";
		
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
}
