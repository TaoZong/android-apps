package com.moreapp.doctorapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;

public class ActivitySignIn extends Activity {

	public static HttpClient client;
	TextView failLog;
	String resString = "", url = "http://ehr-more.herokuapp.com/api/user/login",
			username = "", password = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_sign_in);

		client = new DefaultHttpClient();

		failLog = (TextView) findViewById(R.id.textView2);
		failLog.setVisibility(View.GONE);
		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
//				Intent intent = new Intent();
//				intent.setClass(SignIn.this, MainActivity.class);
//				startActivity(intent);
//				SignIn.this.finish();
				
				EditText userTxt = (EditText) findViewById(R.id.editText1);
				username = userTxt.getText().toString();
				EditText pwTxt = (EditText) findViewById(R.id.editText2);
				password = pwTxt.getText().toString();
				if (username.isEmpty()) {
					failLog.setVisibility(View.VISIBLE);
					failLog.setText("Please input user name.");
				} else if (password.isEmpty()) {
					failLog.setVisibility(View.VISIBLE);
					failLog.setText("Please input password.");
				} else {
					InputMethodManager mInputMethodManager;
					mInputMethodManager = (InputMethodManager) ActivitySignIn.this
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.hideSoftInputFromWindow(ActivitySignIn.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					signIn();
				}
					
			}
		});

	}

	public void signIn() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {

					HttpPost post = new HttpPost(url);

//					MultipartEntityBuilder entity = MultipartEntityBuilder
//							.create();
//					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//					entity.addTextBody("username", username,
//							ContentType.APPLICATION_JSON);
//					entity.addTextBody("password", password,
//							ContentType.APPLICATION_JSON);
//					post.setEntity(entity.build());
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("username", username));
			        nameValuePairs.add(new BasicNameValuePair("password", password));
			        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse response = ActivitySignIn.client.execute(post);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
				} catch (Exception e) {

				}

			}

		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {

		}
		JSONObject jobject;
		try {
			jobject = new JSONObject(resString);
			String result = jobject.getString("message");
			Log.d("SIGN IN:", username + "," + password);
			Log.d("SIGN IN STATUS:", result);
			if (result.equals("ok")) {
				Intent intent = new Intent();
				intent.setClass(ActivitySignIn.this, MainActivity.class);
				startActivity(intent);
				ActivitySignIn.this.finish();
			} else {
				failLog.setVisibility(View.VISIBLE);
				failLog.setText("Sorry, sign in failed.\nPlease re-input user name and password.");
			}
		} catch (Exception e) {

		}

	}

}
