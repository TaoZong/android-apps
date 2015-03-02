package com.moreapp.demo;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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

public class SignUp extends Activity {

	static String resString = "";
	TextView tv; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_sign_up);
		
		tv = (TextView) findViewById(R.id.textView3);
		EditText emailText = (EditText) findViewById(R.id.editText1);
		EditText passwordText = (EditText) findViewById(R.id.editText2);
		EditText verifypwText = (EditText) findViewById(R.id.editText3);
		EditText firstnameText = (EditText) findViewById(R.id.editText4);
		EditText lastnameText = (EditText) findViewById(R.id.editText5);
		emailText.setText("");
		passwordText.setText("");
		verifypwText.setText("");
		firstnameText.setText("");
		lastnameText.setText("");
		Button bt = (Button) findViewById(R.id.button1);
		ImageButton ibt = (ImageButton) findViewById(R.id.imageButton1);
		bt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText emailText = (EditText) findViewById(R.id.editText1);
				EditText passwordText = (EditText) findViewById(R.id.editText2);
				EditText verifypwText = (EditText) findViewById(R.id.editText3);
				EditText firstnameText = (EditText) findViewById(R.id.editText4);
				EditText lastnameText = (EditText) findViewById(R.id.editText5);
				
				String email = emailText.getText().toString();
				String password = passwordText.getText().toString();
				String verifypw = verifypwText.getText().toString();
				String firstname = firstnameText.getText().toString();
				String lastname = lastnameText.getText().toString();
				if(email.isEmpty() || password.isEmpty() || verifypw.isEmpty() 
						|| firstname.isEmpty() || lastname.isEmpty()) {
					tv.setText("Please input all information above to register.");
				}
				else if(!password.equals(verifypw)) {
					tv.setText("Please verify your password.");
				}
				else {
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {
								EditText emailText = (EditText) findViewById(R.id.editText1);
								EditText passwordText = (EditText) findViewById(R.id.editText2);
								EditText firstnameText = (EditText) findViewById(R.id.editText4);
								EditText lastnameText = (EditText) findViewById(R.id.editText5);
								String a = emailText.getText().toString();
								String b = passwordText.getText().toString();
								String c = firstnameText.getText().toString();
								String d = lastnameText.getText().toString();
								HttpPost post = new HttpPost(
										SignUpAndSignIn.urlHead + "/user/sign-up");

								// Add parameters: email and password
								ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
								pairs.add(new BasicNameValuePair("email", a));
								pairs.add(new BasicNameValuePair("password", b));
								pairs.add(new BasicNameValuePair("first_name", c));
								pairs.add(new BasicNameValuePair("last_name", d));
								UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
										pairs);
								post.setEntity(ent);

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
							mInputMethodManager = (InputMethodManager) SignUp.this
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							mInputMethodManager.hideSoftInputFromWindow(SignUp.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
							
							SaveSharedPreference.setUserInfo(email, password);
							
							String id = jobject.getString("id");
							SaveSharedPreference.id = id;
							// Send user email to SigninActivity
							Intent intent = new Intent();
							intent.setClass(SignUp.this,
									MainActivity.class);
							startActivity(intent);
							SignUp.this.finish();
						} else {
							if(jobject.get("status").toString().equals("failure")) {
								tv.setText("Register failed. Please try again.");
							}
							else if(jobject.get("status").toString().equals("duplicate")) {
								tv.setText("Email is already used. Please try another one.");
							}
						}
							
						emailText.setText("");
						passwordText.setText("");
						verifypwText.setText("");
						firstnameText.setText("");
						lastnameText.setText("");
					} catch (JSONException e) {
						// e.printStackTrace();
					}
				}
			}
		});
		ibt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SignUp.this,
						SignUpAndSignIn.class);
				startActivity(intent);
				SignUp.this.finish();
			}
		});
	}

}
