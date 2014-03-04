package helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

public class TokenAsync {
	
	private Context mainContext;
	private Activity CallerActivity;
	
	public TokenAsync(Context c, Activity a) {
		mainContext = c;
		CallerActivity = a;
	}
	
	public String getToken(){
		
		String url = CommonUtilities.SERVER_URL_TOKEN;
		String sReturn = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

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
		String token = "";
		if(sReturn != ""){
			try {
				JSONObject jsonObj = new JSONObject(sReturn);
				token = jsonObj.getString("_token");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return token;
	}
}