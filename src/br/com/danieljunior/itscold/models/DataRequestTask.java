package br.com.danieljunior.itscold.models;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import br.com.danieljunior.itscold.MainActivity;

@SuppressWarnings("deprecation")
public abstract class DataRequestTask extends AsyncTask<String, Void, String>
		implements CallBackService {
	String cityName;
	private ProgressDialog dialog;
	private MainActivity activity;
	private JSONObject obj;

	public DataRequestTask(MainActivity activity) {
		dialog = new ProgressDialog(activity);
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		// super.onPreExecute();
		this.dialog.setMessage("Aguarde um momento por favor...");
		this.dialog.show();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		String url = buildUrl(params[0].replace(' ', '+'));
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		String result = null;
		try {
			response = client.execute(request);
			result = EntityUtils.toString(response.getEntity());
			obj = new JSONObject(result);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String resp = getWeather();
		return resp;
	}

	private String getWeather() {
		// TODO Auto-generated method stub
		String resp = "ERRO" ;
		try {
			JSONArray array = (JSONArray) obj.get("data");
			JSONObject obj2 = array.getJSONObject(1);
			int temp = (Integer) obj2.getInt("temperature");
			resp = temperatureRange(temp);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	private String temperatureRange(int temp) {
		// TODO Auto-generated method stub
		if (temp <= 23) {
			return "FRIO";
		} else if (temp > 23 && temp < 28) {
			return "AGRADÁVEL";
		} else {
			return "CALOR";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		dialog.dismiss();
		receiveData(result);

	}

	@SuppressLint("SimpleDateFormat")
	private String buildUrl(String cityName) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(tz);
		String timeNow = df.format(new Date());
		String url = "http://sensor-api.localdata.com/api/v1/aggregations?op=mean&over.city="
				+ cityName
				+ "&from="
				+ timeNow
				+ "T00:00:00-0800&before="
				+ timeNow + "T23:59:59-0800&resolution=1h&fields=temperature";
		return url;
	}

	public abstract void receiveData(Object object);

}
