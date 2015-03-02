package com.moreapp.demo;

import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;

import com.moreapp.demo.R;

import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class Settings extends Activity {
	EditText txt1, txt2, txt3;
	Switch switch1;
	Button bt;
	String resString;
	static boolean flag = true;
	double lat, lon;
	TextView errorTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_settings);

		txt1 = (EditText) findViewById(R.id.editText1);
		txt2 = (EditText) findViewById(R.id.editText2);
		txt3 = (EditText) findViewById(R.id.editText3);
		switch1 = (Switch) findViewById(R.id.switch1);
		bt = (Button) findViewById(R.id.button1);
		errorTxt = (TextView) findViewById(R.id.textView5);

		ImageButton ibt = (ImageButton) findViewById(R.id.imageButton1);
		ibt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager mInputMethodManager;
				mInputMethodManager = (InputMethodManager) Settings.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				mInputMethodManager.hideSoftInputFromWindow(Settings.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				Intent intent = new Intent();
				intent.setClass(Settings.this, MainActivity.class);
				startActivity(intent);
				Settings.this.finish();

			}
		});

		if (flag) {
			switch1.setChecked(true);
		} else
			switch1.setChecked(false);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String pw1 = txt1.getText().toString();
				String pw2 = txt2.getText().toString();
				String pw3 = txt3.getText().toString();
				if (pw1.length() == 0) {
					errorTxt.setText("Please input old password");
				} else if (pw2.length() == 0) {
					errorTxt.setText("Please input new password");
				} else if (pw3.length() == 0 || !pw2.equals(pw3)) {
					errorTxt.setText("Please confirm new password");
				} else
					updateSettings();
			}
		});

		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked == true) {
					flag = true;
				} else {
					flag = false;
				}
			}
		});
	}

	public void updateSettings() {
		if (flag) {
			SaveSharedPreference.setUploadLocation(true);
		} else {
			SaveSharedPreference.setUploadLocation(false);
		}
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					String pw1 = txt1.getText().toString();
					String pw2 = txt2.getText().toString();
					String geo_sharing;
					if (flag)
						geo_sharing = "ON";
					else
						geo_sharing = "OFF";
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/user/settings");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("current_password", pw1,
							ContentType.APPLICATION_JSON);
					entity.addTextBody("new_password", pw2,
							ContentType.APPLICATION_JSON);
					entity.addTextBody("geo_sharing", geo_sharing,
							ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());

					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("CHANGE SETTINGS STATUS:", resString);
				} catch (Exception e) {

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
