package com.moreapp.demo;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.moreapp.demo.R;
import com.moreapp.demo.FragmentB.Notification;
import com.moreapp.demo.FragmentB.NotificationAdapter;


import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

// Featured News

public class FragmentA extends Fragment {
	private String cityName = "";
	private TextView txt;
	View view;
	ListView list;
	ArrayList<News> news;
	String resString;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_a, container, false);
		
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
		Criteria c = new Criteria();
		String bestProvider = locationManager.getBestProvider(c, true);
        Location loc = locationManager.getLastKnownLocation(bestProvider);
        cityName = getCityFromLocation(loc.getLatitude(), loc.getLongitude(), 1);


//        Geocoder gcd = new Geocoder(MainActivity.c, Locale.getDefault());
//		List<Address>  addresses;
//		try {
//			addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
//			
//			if (addresses.size() > 0)
//				cityName = addresses.get(0).getLocality();
//				
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		txt = (TextView)view.findViewById(R.id.textView21);
		if(!cityName.isEmpty())
			txt.setText(cityName);
		else
			txt.setText("");
		
		String time = "";

		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR);
		int minute = cal.get(Calendar.MINUTE);
		int ap = cal.get(Calendar.AM_PM);
		TimeZone tz = cal.getTimeZone();
		
		String ampm = "";
		if(ap == 1) {
			ampm = "PM";
			if(hour == 0) hour = 12;
		}
		else ampm = "AM";
		if(minute > 9) time = hour + ":" + minute + " " + ampm + " " + tz.getDisplayName();
		else time = hour + ":0" + minute + " " + ampm + " " + tz.getDisplayName();

		((TextView)view.findViewById(R.id.textView22)).setText(time);
		
		news = new ArrayList<News>();
		list = (ListView) view.findViewById(R.id.listView1);
		getNews();
		NewsAdapter adapter = new NewsAdapter(getActivity(),
				R.layout.news_list_row, news);		
		list.setAdapter(adapter);
	    return view;
	}
	
	public class News {
		String id;
		String title;
		String summary;
		String time;
		
		News(String id, String title, String summary, String time) {
			this.id = id;
			this.title = title;
			this.summary = summary;
			this.time = time;
		}
	}
	
	public class NewsHolder {
		TextView txt1, txt2, txt3;
	}
	
	public class NewsAdapter extends ArrayAdapter<News> {
		Context context;
		int layoutResourceId;
		ArrayList<News> data;

		public NewsAdapter(Context context, int layoutResourceId,
				ArrayList<News> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			NewsHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new NewsHolder();
				holder.txt1 = (TextView) row.findViewById(R.id.textView1);
				holder.txt2 = (TextView) row.findViewById(R.id.textView2);
				holder.txt3 = (TextView) row.findViewById(R.id.textView3);
				row.setTag(holder);
			} else {
				holder = (NewsHolder) row.getTag();
			}
			holder.txt1.setText(data.get(position).title);
			holder.txt2.setText(data.get(position).summary);
			holder.txt3.setText(data.get(position).time);
			
			return row;
		}
	}
	
	public void getNews() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpGet httpget = new HttpGet(SignUpAndSignIn.urlHead
						+ "/repository/news/index");
				try {
					HttpResponse response = SignUpAndSignIn.client
							.execute(httpget);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
					Log.d("GET NEWS STATUS:", resString);
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
				JSONArray jarray = jobject.getJSONArray("news");
				if(jarray.length() > 0) {
					for(int i = 0; i < jarray.length(); i++) {
						final String id = jarray.getJSONObject(i).getString("id");
						String title = jarray.getJSONObject(i).getString("title");
						String summary = jarray.getJSONObject(i).getString("summary").substring(3);
						String time = jarray.getJSONObject(i).getString("timestamp");
						
						Thread t2 = new Thread(new Runnable() {
							public void run() {
								HttpGet httpget = new HttpGet(SignUpAndSignIn.urlHead
										+ "/repository/news?nid=" + id);
								try {
									HttpResponse response = SignUpAndSignIn.client
											.execute(httpget);
									HttpEntity resEntity = response.getEntity();
									resString = EntityUtils.toString(resEntity);
									Log.d("GET NEWS DETAIL STATUS:", resString);
								} catch (Exception e) {

								}
							}
						});
						t2.start();
						try {
							t2.join();
						} catch (InterruptedException e) {

						}
						JSONObject jobject2;
						jobject2 = new JSONObject(resString);
						if (jobject2.get("status").toString().equals("success")) {
							String raw_text = jobject2.getJSONObject("news").getString("text");
							String[] paragraphs = raw_text.split("<p>");
							String text = "";
							for(String p : paragraphs) {
								if(p.length() > 0) {
									text = text+ p.replace("</p>", "") + "\n\n";
								}
							}
							News n = new News(id, title, text, time);
							news.add(n);
						}
					}
				}
			}
		}catch(Exception e) {
			
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
					if(array.length() > 0) {
						for(int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							if(obj.getString("types").contains("locality")) {
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
