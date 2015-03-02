package com.moreapp.demo;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.moreapp.demo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

public class ContactMessage extends ListActivity {
	String uid;
	String name, myName;
	ArrayList<Message> messages;
	MessageAdapter adapter;
	EditText text;
	Button bt;
	String resString;
	private PullToRefreshListView mPullRefreshListView;
	String first_timestamp="", last_timestamp="";
	String myId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_contact_message);
		myId = SaveSharedPreference.id;
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.list);
		mPullRefreshListView.setMode(Mode.BOTH);

		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// String label =
						// DateUtils.formatDateTime(getApplicationContext(),
						// System.currentTimeMillis(),
						// DateUtils.FORMAT_SHOW_TIME |
						// DateUtils.FORMAT_SHOW_DATE |
						// DateUtils.FORMAT_ABBREV_ALL);
						//
						// // Update the LastUpdatedLabel
						// refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						if (mPullRefreshListView.isHeaderShown())
							// Do work to refresh the list here.
							new GetDataTask().execute();
						else if (mPullRefreshListView.isFooterShown())
							new GetDataTask2().execute();
					}
				});

		ImageButton ibt1 = (ImageButton) findViewById(R.id.imageButton1);
		Bundle b = this.getIntent().getExtras();
		uid = b.getString("uid");
		name = b.getString("name");
		messages = new ArrayList<Message>();
		text = (EditText) this.findViewById(R.id.text);
		bt = (Button) this.findViewById(R.id.sendButton);
		TextView textview = (TextView) this.findViewById(R.id.textView1);
		textview.setText(name);
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpGet httpget = new HttpGet(SignUpAndSignIn.urlHead
						+ "/sociality/contact/message?uid=" + uid);
				try {
					HttpResponse response = SignUpAndSignIn.client
							.execute(httpget);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
					Log.d("CONTACT MESSAGE STATUS:", resString);
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
				first_timestamp = jobject.getString("first_timestamp");
				last_timestamp = jobject.getString("last_timestamp");
				name = jobject.getJSONObject("target").getString(
						"full_name");
				myName = jobject.getJSONObject("user").getString(
						"full_name");
				JSONArray jarray = jobject.getJSONArray("messages");
				if (jarray.length() > 0) {
					for (int i = 0; i < jarray.length(); i++) {
						String fromMe = jarray.getJSONObject(i).getString(
								"from");
						String text = jarray.getJSONObject(i).getString("text");
						String time = jarray.getJSONObject(i).getString(
								"timestamp");
						if (fromMe.equals("true")) {
							Message m = new Message(text, myName, time,
									true);
							messages.add(m);
						}
						else {
							Message m = new Message(text, name, time,
									false);
							messages.add(m);
						}
					}
				}
			}
		} catch (Exception e) {

		}

		ListView actualListView = mPullRefreshListView.getRefreshableView();
		registerForContextMenu(actualListView);

		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InputMethodManager mInputMethodManager;
				mInputMethodManager = (InputMethodManager) ContactMessage.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				mInputMethodManager.hideSoftInputFromWindow(ContactMessage.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

			}
		});

		if (messages.size() > 0) {
			adapter = new MessageAdapter(this, messages);
			actualListView.setAdapter(adapter);
			actualListView.setSelection(actualListView.getAdapter().getCount()-1);
		}

		ibt1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager mInputMethodManager;
				mInputMethodManager = (InputMethodManager) ContactMessage.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				mInputMethodManager.hideSoftInputFromWindow(ContactMessage.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				ContactMessage.this.finish();
			}
		});

	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			Thread t = new Thread(new Runnable() {
				public void run() {
					String url = SignUpAndSignIn.urlHead
							+ "/sociality/contact/message?uid=" + uid
							+ "&timestamp="
							+ first_timestamp.replace(" ", "%20") + "&mode=P";

					// List<NameValuePair> params = new
					// LinkedList<NameValuePair>();
					// params.add(new BasicNameValuePair("cid", cid));
					// params.add(new BasicNameValuePair("timestamp",
					// first_timestamp));
					// params.add(new BasicNameValuePair("mode", "P"));
					// String paramString = URLEncodedUtils
					// .format(params, "utf-8");
					// url += paramString;
					HttpGet httpget = new HttpGet(url);
					try {
						HttpResponse response = SignUpAndSignIn.client
								.execute(httpget);
						HttpEntity resEntity = response.getEntity();
						resString = EntityUtils.toString(resEntity);
						Log.d("OLD CONTACT MESSAGE STATUS:", resString);
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
			ArrayList<Message> newMessages = new ArrayList<Message>();
			try {
				jobject = new JSONObject(resString);
				if (jobject.get("status").toString().equals("success")) {
					JSONArray jarray = jobject.getJSONArray("messages");
					if (jarray.length() > 0) {
						first_timestamp = jobject.getString("first_timestamp");

						for (int i = 0; i < jarray.length(); i++) {
							String text = jarray.getJSONObject(i).getString(
									"text");
							String time = jarray.getJSONObject(i).getString(
									"timestamp");
							String fromMe = jarray.getJSONObject(i).getString(
									"from");
							if (fromMe.equals("true")) {
								Message m = new Message(text, myName, time,
										true);
								newMessages.add(m);
							}
							else {
								Message m = new Message(text, name, time,
										false);
								newMessages.add(m);
							}
						}
					}
					newMessages.addAll(messages);
					messages.clear();
					for (Message m : newMessages) {
						messages.add(m);
					}
				}
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	private class GetDataTask2 extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			Thread t = new Thread(new Runnable() {
				public void run() {
					String url = SignUpAndSignIn.urlHead
							+ "/sociality/contact/message?uid=" + uid
							+ "&timestamp="
							+ last_timestamp.replace(" ", "%20") + "&mode=F";
					HttpGet httpget = new HttpGet(url);
					try {
						HttpResponse response = SignUpAndSignIn.client
								.execute(httpget);
						HttpEntity resEntity = response.getEntity();
						resString = EntityUtils.toString(resEntity);
						Log.d("NEW CONTACT MESSAGE STATUS:", resString);
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
					JSONArray jarray = jobject.getJSONArray("messages");
					if (jarray.length() > 0) {
						last_timestamp = jobject.getString("last_timestamp");

						for (int i = 0; i < jarray.length(); i++) {
							
							String text = jarray.getJSONObject(i).getString(
									"text");
							String time = jarray.getJSONObject(i).getString(
									"timestamp");
							String fromMe = jarray.getJSONObject(i).getString(
									"from");
							if (fromMe.equals("true")) {
								Message m = new Message(text, myName, time,
										true);
								messages.add(m);
							}
							else {
								Message m = new Message(text, name, time,
										false);
								messages.add(m);
							}
						}
					}
				}
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_message, menu);
		return true;
	}

	public void sendMessage(View v) {
		final String newMessage = text.getText().toString().trim();
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {

					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/contact/message");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("uid", uid, ContentType.APPLICATION_JSON);
					entity.addTextBody("text", newMessage,
							ContentType.APPLICATION_JSON);
					if(last_timestamp.length() > 0)
						entity.addTextBody("timestamp", last_timestamp,
								ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("SEND MESSAGE STATUS:", resString);
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
//		messages.clear();
		JSONObject jobject;
		try {
			jobject = new JSONObject(resString);
			if (jobject.get("status").toString().equals("success")) {
				last_timestamp = jobject.getString("last_timestamp");

				JSONArray jarray = jobject.getJSONArray("messages");
				if (jarray.length() > 0) {
					for (int i = 0; i < jarray.length(); i++) {
						String text = jarray.getJSONObject(i).getString("text");
						String time = jarray.getJSONObject(i).getString(
								"timestamp");
						String fromMe = jarray.getJSONObject(i).getString(
								"from");
						if (fromMe.equals("true")) {
							Message m = new Message(text, myName, time,
									true);
							messages.add(m);
						}
						else {
							Message m = new Message(text, name, time,
									false);
							messages.add(m);
						}
					}
				}
			}
		} catch (Exception e) {

		}
		adapter.notifyDataSetChanged();
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setSelection(actualListView.getAdapter().getCount() - 1);
		text.setText("");

		InputMethodManager mInputMethodManager;
		mInputMethodManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mInputMethodManager.hideSoftInputFromWindow(ContactMessage.this
				.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
