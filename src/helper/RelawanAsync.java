package helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public abstract class RelawanAsync extends AsyncTask<String, Void, String> implements ClientInterface{

	private Context mainContext;
	private Activity CallerActivity;
	private ProgressDialog pd;
	
	public RelawanAsync(Context c, Activity a) {
		mainContext = c;
		CallerActivity = a;
	}
	
	protected void onPreExecute() {
		pd = new ProgressDialog(mainContext);
		pd.setMessage("Sending data...");
		pd.show();
		pd.setCancelable(false);
	}
	
	public abstract void onResponseReceived(String sReturn);

	protected String doInBackground(String... params) {
		
		//api relawan belum sesuai		
		
		TokenAsync t = new TokenAsync(mainContext, CallerActivity);
		String token = t.getToken();
		Log.d("token", token);
		
		String url = CommonUtilities.SERVER_URL_RELAWAN;
		String sReturn = "";
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();

		pairs.add(new BasicNameValuePair("name", params[0]));
		pairs.add(new BasicNameValuePair("phone", params[1]));
		pairs.add(new BasicNameValuePair("desc", params[2]));
		pairs.add(new BasicNameValuePair("location", params[3]));
		pairs.add(new BasicNameValuePair("status", params[4]));

		try {
			request.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {

			HttpResponse response = client.execute(request);
			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder str = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				str.append(line + "\n");
			}
			in.close();
			sReturn = str.toString();

		} catch (Exception e) {
			// writing exception to log
			e.printStackTrace();
		}

		Log.d("Callback server", sReturn);
		String callback = "false";
		if(sReturn != ""){
			try {
				JSONObject jsonObj = new JSONObject(sReturn);
				if(jsonObj.getString("success").equals("1"))
					callback = "true";
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		return callback;
	}

	protected void onCancelled() {

	}

	protected void onPostExecute(String sReturn) {
		if (pd.isShowing()) {
			pd.dismiss();
		}
		onResponseReceived(sReturn);
	}
}