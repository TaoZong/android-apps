package com.moreapp.demo;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.moreapp.demo.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SignIn extends Activity {

	static String resString = "";
	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_sign_in);
		
		tv = (TextView) findViewById(R.id.textView22);
		EditText emailText = (EditText) findViewById(R.id.editText1);
		EditText passwordText = (EditText) findViewById(R.id.editText2);
		emailText.setText("");
		passwordText.setText("");
		Button bt = (Button) findViewById(R.id.button1);
		ImageButton ibt = (ImageButton) findViewById(R.id.imageButton1);
		bt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText emailText = (EditText) findViewById(R.id.editText1);
				EditText passwordText = (EditText) findViewById(R.id.editText2);
				String email = emailText.getText().toString();
				String password = passwordText.getText().toString();
				if(email.isEmpty() || password.isEmpty()) {
					tv.setText("Please input email and password to sign in.");
				}
				else {
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {
								EditText emailText = (EditText) findViewById(R.id.editText1);
								EditText passwordText = (EditText) findViewById(R.id.editText2);
								String a = emailText.getText().toString();
								String b = passwordText.getText().toString();

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
							InputMethodManager mInputMethodManager;
							mInputMethodManager = (InputMethodManager) SignIn.this
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							mInputMethodManager.hideSoftInputFromWindow(SignIn.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
							
							SaveSharedPreference.setUserInfo(email, password);
							
							String id = jobject.getString("id");
							SaveSharedPreference.id = id;
							// Send user email to SigninActivity
							Intent intent = new Intent();
							intent.setClass(SignIn.this,
									MainActivity.class);
							startActivity(intent);
							SignIn.this.finish();
						} else {
							if(jobject.get("status").toString()
									.equals("failure")) {
								tv.setText("Sign in failed. Please try again.");
							}
						}
							
						emailText.setText("");
						passwordText.setText("");
					} catch (JSONException e) {
						// e.printStackTrace();
					}
				}
			}
		});
		
		ibt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SignIn.this,
						SignUpAndSignIn.class);
				startActivity(intent);
				SignIn.this.finish();
			}
		});
	}

}
