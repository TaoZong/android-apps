package com.moreapp.demo;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.moreapp.demo.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

// Notification

public class FragmentB extends Fragment {
	ListView list;
	ArrayList<Notification> notifications;
	String resString;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_b, container, false);
		list = (ListView) root.findViewById(R.id.listView1);
		notifications = new ArrayList<Notification>();
		getNotifications();
		NotificationAdapter adapter = new NotificationAdapter(getActivity(),
				R.layout.notification_list_row, notifications);		
		list.setAdapter(adapter);
		return root;
	}

	public class Notification {
		String id;
		String text;
		String category;

		Notification(String id, String text, String category) {
			this.id = id;
			this.text = text;
			this.category = category;
		}
	}

	public class NotificationHolder {
		TextView txt;
		ImageButton bt;
	}

	public class NotificationAdapter extends ArrayAdapter<Notification> {
		Context context;
		int layoutResourceId;
		ArrayList<Notification> data;

		public NotificationAdapter(Context context, int layoutResourceId,
				ArrayList<Notification> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			NotificationHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new NotificationHolder();
				holder.txt = (TextView) row.findViewById(R.id.textView1);
				holder.bt = (ImageButton) row.findViewById(R.id.imageButton1);
				row.setTag(holder);
			} else {
				holder = (NotificationHolder) row.getTag();
			}
			holder.txt.setText(data.get(position).text);
			holder.bt.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					deleteNotification(data.get(position).id);
					data.remove(position);
					notifyDataSetChanged();
				}
			});
			return row;
		}
	}
	
	public void getNotifications() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpGet httpget = new HttpGet(SignUpAndSignIn.urlHead
						+ "/tools/notification/index");
				try {
					HttpResponse response = SignUpAndSignIn.client
							.execute(httpget);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
					Log.d("GET NOTIFICATIONS STATUS:", resString);
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
				JSONArray jarray = jobject.getJSONArray("notifications");
				if(jarray.length() > 0) {
					for(int i = 0; i < jarray.length(); i++) {
						String text = jarray.getJSONObject(i).getString("text");
						String id = jarray.getJSONObject(i).getString("id");
						String category = jarray.getJSONObject(i).getString("category");
						Notification n = new Notification(id, text, category);
						notifications.add(n);
					}
				}
			}
		}catch(Exception e) {
			
		}
	}
	
	public void deleteNotification(final String id) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/tools/notification/deactivate");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("nid", id, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("DELETE NOTIFICATION STATUS:", resString);
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
