package com.moreapp.demo;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.moreapp.demo.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class AddMedicalRecord extends Activity {
	ListView listview1, listview2;
	EditText edittxt;
	TextView txtview;
	String pid, category, relationship, resString;
	String[] listContent;
	ArrayList<String> listContent2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_add_medical_record);
		Bundle b = this.getIntent().getExtras();
		pid = b.getString("pid");
		relationship = b.getString("relationship");
		category = b.getString("category");
		ImageButton imgbt = (ImageButton) findViewById(R.id.imageButton1);
		edittxt = (EditText) findViewById(R.id.editText1);
		txtview = (TextView) findViewById(R.id.textView1);
		txtview.setText(category);
		Button bt = (Button) findViewById(R.id.button1);
		listview1 = (ListView) findViewById(R.id.listView1);
		listview2 = (ListView) findViewById(R.id.listView2);
		listview1.setVisibility(View.GONE);
		listview2.setVisibility(View.GONE);
		listContent2 = new ArrayList<String>();

		TextWatcher textWatcher = new TextWatcher() {
			private Handler mHandler = new Handler();
			String s;
			Runnable mFilterTask = new Runnable() {

				@Override
				public void run() {
					Log.d("INPUT CHANGES:", s.toString());
					listview1.setVisibility(View.VISIBLE);
					listview2.setVisibility(View.GONE);
					txtview.setVisibility(View.GONE);
					final String inputString = s.toString();
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {
								String uri = SignUpAndSignIn.urlHead
										+ "/repository/med-term/autocomplete?category="
										+ category + "&query=" + inputString;
								HttpGet httpget = new HttpGet(uri);

								HttpResponse response = SignUpAndSignIn.client
										.execute(httpget);
								HttpEntity resEntity = response.getEntity();
								resString = EntityUtils.toString(resEntity);
								// Log.d("SEARCH MEDICAL RECORD STATUS:",
								// resString);
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
						if (jobject.get("status").toString().equals("success")) {
							JSONArray array = jobject
									.getJSONArray("autocomplete");
							listContent = new String[array.length()];
							for (int i = 0; i < array.length(); i++) {
								listContent[i] = array.getString(i);
							}
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									AddMedicalRecord.this,
									R.layout.medical_list_row, listContent);
							listview1.setAdapter(adapter);
							listview1
									.setOnItemClickListener(new AdapterView.OnItemClickListener() {
										@Override
										public void onItemClick(
												AdapterView<?> parent,
												View view, int position, long id) {
											edittxt.setText(listContent[position]);
											listview1.setVisibility(View.GONE);
											txtview.setVisibility(View.VISIBLE);
											listview2.setVisibility(View.GONE);
										}
									});
						}
					} catch (Exception e) {

					}
				}
			};

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				this.s = s.toString();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				mHandler.removeCallbacks(mFilterTask);
				mHandler.postDelayed(mFilterTask, 1000);
			}
		};
		edittxt.addTextChangedListener(textWatcher);
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listview1.setVisibility(View.GONE);
				txtview.setVisibility(View.VISIBLE);
				listview2.setVisibility(View.VISIBLE);

				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							String uri = SignUpAndSignIn.urlHead
									+ "/profile/medinfo/add";
							HttpPost post = new HttpPost(uri);
							ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
							pairs.add(new BasicNameValuePair("pid", pid));
							pairs.add(new BasicNameValuePair("category",
									category));
							pairs.add(new BasicNameValuePair("term", edittxt
									.getText().toString()));
							UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
									pairs);
							post.setEntity(ent);

							HttpResponse response = SignUpAndSignIn.client
									.execute(post);
							HttpEntity resEntity = response.getEntity();
							resString = EntityUtils.toString(resEntity);
							Log.d("ADD MEDICAL RECORD STATUS:", resString);
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
					if (jobject.get("status").toString().equals("success")) {
						listContent2.add(edittxt.getText().toString());
					}
				} catch (Exception e) {

				}
				String[] tmplist = listContent2.toArray(new String[listContent2
						.size()]);

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						AddMedicalRecord.this, R.layout.medical_list_row,
						tmplist);
				listview2.setAdapter(adapter);

			}

		});

		imgbt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddMedicalRecord.this, ProfileDetails.class);
				Bundle bundle = new Bundle();
				bundle.putString("pid", pid);
				bundle.putString("relationship", relationship);
				intent.putExtras(bundle);
				startActivity(intent);
				AddMedicalRecord.this.finish();
			}
		});

	}

}
