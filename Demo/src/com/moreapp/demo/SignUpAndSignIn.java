package com.moreapp.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.moreapp.demo.R;

import android.location.Address;
import android.location.Criteria;

import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpAndSignIn extends Activity {
	static String urlHead = "http://moreapp.moredev.webfactional.com/api";
	private String cityName = "";
	private TextView txt;
	public static HttpClient client;
	public static Context c;
	public String resString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_sign_up_and_sign_in);
		c = this;
		client = new DefaultHttpClient();

		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SignUpAndSignIn.this, SignUp.class);
				startActivity(intent);
				SignUpAndSignIn.this.finish();
			}

		});

		Button bt2 = (Button) findViewById(R.id.button2);
		bt2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SignUpAndSignIn.this, SignIn.class);
				startActivity(intent);
				SignUpAndSignIn.this.finish();
			}

		});

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria c = new Criteria();
		String bestProvider = locationManager.getBestProvider(c, true);
		Location loc = locationManager.getLastKnownLocation(bestProvider);
		Geocoder gcd = new Geocoder(SignUpAndSignIn.this, Locale.getDefault());
		SaveSharedPreference.uploadLocation(SignUpAndSignIn.this);
		// List<String> addresses;
		if (loc != null) {
			cityName = getCityFromLocation(loc.getLatitude(),
					loc.getLongitude(), 1);
			// addresses = gcd.getFromLocation(loc.getLatitude(),
			// loc.getLongitude(), 1);

			txt = (TextView) findViewById(R.id.textView1);
			if (cityName != null)
				txt.setText(cityName);
			else
				txt.setText("");
		}

		String time = "";

		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR);
		int minute = cal.get(Calendar.MINUTE);
		int ap = cal.get(Calendar.AM_PM);
		TimeZone tz = cal.getTimeZone();

		String ampm = "";
		if (ap == 1) {
			ampm = "PM";
			if (hour == 0)
				hour = 12;
		} else
			ampm = "AM";
		if (minute > 9)
			time = hour + ":" + minute + " " + ampm + " " + tz.getDisplayName();
		else
			time = hour + ":0" + minute + " " + ampm + " "
					+ tz.getDisplayName();
		((TextView) findViewById(R.id.textView2)).setText(time);

		int x = 0;
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			x = bundle.getInt("Clear");
		}
		if (x == 1) {
			// Clear shared preference
			SaveSharedPreference.setUserInfo("", "");
		}
		if (SaveSharedPreference.getUserEmail().length() > 0) {
			String a = SaveSharedPreference.getUserEmail();
			String b = SaveSharedPreference
					.getUserPassword();
			signIn(a, b);
		}
	}
	
	public void signIn(final String a, final String b) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(
							SignUpAndSignIn.urlHead + "/user/sign-in");
					
					MultipartEntityBuilder entity = MultipartEntityBuilder.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("email", a, ContentType.APPLICATION_JSON);
					entity.addTextBody("password", b, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());

					// Execute HTTP Post Request
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("SIGN IN STATUS:", resString);
				} catch (Exception e) {
					// e.printStackTrace();
				}

			}

		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		
		try {
			JSONObject jobject = new JSONObject(resString);
			if (jobject.get("status").toString()
							.equals("success")) {
				String id = jobject.getString("id");
				SaveSharedPreference.id = id;
				// Send user email to SigninActivity
				Intent intent = new Intent();
				intent.setClass(SignUpAndSignIn.this,
						MainActivity.class);
				startActivity(intent);
				SignUpAndSignIn.this.finish();
				
			}
		} catch(Exception e) {
			
		}
	}
	
	public String getCityFromLocation(final double lat, final double lng,
			int maxResult) {
		String city = null;
		Log.d("LATLON:", String.valueOf(lat) + "," + String.valueOf(lng));
		Log.d("LOCALE:", Locale.getDefault().toString());
		// final String address = String
		// .format(Locale.getDefault(),
		// "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
		// + Locale.getDefault().getCountry(), lat, lng);
		Thread t = new Thread(new Runnable() {
			public void run() {
				// HttpGet httpGet = new HttpGet(address);
				HttpGet httpGet = new HttpGet(
						"http://maps.googleapis.com/maps/api/geocode/json?latlng="
								+ lat + "," + lng + "&sensor=true&language="
								+ Locale.getDefault().toString());
				HttpClient client = new DefaultHttpClient();
				HttpResponse response;

				try {
					response = client.execute(httpGet);
					HttpEntity entity = response.getEntity();
					resString = EntityUtils.toString(entity);
					Log.d("get city name status:", resString);
				} catch (Exception e) {

				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {

		}

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(resString);

			if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
				JSONArray results = jsonObject.getJSONArray("results");
				if (results.length() > 0) {
					JSONObject ob = results.getJSONObject(0);
					JSONArray array = ob.getJSONArray("address_components");
					if (array.length() > 0) {
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							if (obj.getString("types").contains("locality")) {
								city = obj.getString("short_name");
								return city;
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return city;
	}
}
