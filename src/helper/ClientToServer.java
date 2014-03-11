package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

public class ClientToServer {
	public static final int HTTP_TIMEOUT = 30 * 1000;
	private static HttpClient client;
	static FileInputStream fileInputStream = null;
	static ContentBody cbFile;;
	static String sResponse;

	private static HttpClient getHttpClient() {
		if (client == null) {
			client = new DefaultHttpClient();
			final HttpParams parameterHttp = client.getParams();

			HttpConnectionParams.setConnectionTimeout(parameterHttp,
					HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(parameterHttp, HTTP_TIMEOUT);
		}
		return client;
	}

	public static String eksekusiHttpPost(String url,
			ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpPost req = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			req.setEntity(formEntity);
			Log.d("Raw responese", client.toString());
			HttpResponse respon = client.execute(req);
			Log.d("Raw responese", respon.toString());
			in = new BufferedReader(new InputStreamReader(respon.getEntity()
					.getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String hasil = sb.toString();
			return hasil;
		} finally {
			if (in != null) {
				in.close();
			}
		}

	}

	public static String eksekusiHttpPut(String URL,
			ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpPut put = new HttpPut(URL);
			put.setEntity(new UrlEncodedFormEntity(postParameters));
			HttpResponse response = client.execute(put);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String hasil = sb.toString();
			return hasil;
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static String fileUpload(String url,
			ArrayList<NameValuePair> putParameters, File photo) {
		for (int i = 0; i < putParameters.size(); i++) {
			Log.d("postParameter", String.valueOf(putParameters.get(i)));
		}
		try {
			Log.d("photo", photo.getPath());
			FileBody f_photo = new FileBody(photo);

			HttpClient client = getHttpClient();
			HttpPut req = new HttpPut(url);
			HttpContext localContext = new BasicHttpContext();

			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			for (int i = 0; i < putParameters.size(); i++) {
				entity.addPart(putParameters.get(i).getName(), new StringBody(
						putParameters.get(i).getValue()));
			}
			// entity.addPart("member[ic_uploaded]", f_ic);
			entity.addPart("member[profile_uploaded]", f_photo);

			req.setEntity(entity);
			HttpResponse response = client.execute(req, localContext);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));

			sResponse = reader.readLine();
			Log.d("coba===", sResponse);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sResponse;

	}
}