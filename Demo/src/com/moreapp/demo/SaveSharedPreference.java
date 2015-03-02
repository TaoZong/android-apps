package com.moreapp.demo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;

public class SaveSharedPreference {
	static public boolean isUploadLocation = true;
	static public String id;
	static public boolean isEmergencyOn = false;
	static public double lat, lon;
	static public String email = "", password = "";

	public static void setUserInfo(String email, String password) {
		SaveSharedPreference.email = email;
		SaveSharedPreference.password = password;
	}

	public static String getUserEmail() {
		return SaveSharedPreference.email;
	}

	public static String getUserPassword() {
		return SaveSharedPreference.password;
	}

	public static void setUploadLocation(boolean flag) {
		isUploadLocation = flag;
	}

	public static void uploadLocation(Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria c = new Criteria();
		String bestProvider = locationManager.getBestProvider(c, true);
		Location currentlocation = locationManager
				.getLastKnownLocation(bestProvider);
		if (currentlocation != null)
			upload(currentlocation.getLatitude(),
					currentlocation.getLongitude());

		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				60000, 1000, locationListener);
	}

	private final static LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			if (isUploadLocation)
				upload(location.getLatitude(), location.getLongitude());
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}
	};

	static void upload(final double lat, final double lon) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					String resString;
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/property/location");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("latitude", String.valueOf(lat),
							ContentType.APPLICATION_JSON);
					entity.addTextBody("longitude", String.valueOf(lon),
							ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("UPLOAD USER LOCATION STATUS:", resString);
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
	}

}
