package com.moreapp.demo;

import java.io.InputStream;
import java.net.URL;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.moreapp.demo.R;
import com.moreapp.demo.R.id;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.moreapp.demo.ProfileList.Profile;
import com.moreapp.demo.ProfileList.ProfileAdapter;
import com.moreapp.demo.ProfileList.ProfileHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class LayoutMap extends Fragment {
	private MapView mMapView;
	private GoogleMap mMap;
	private Bundle mBundle;
	private String url;
	private Bitmap d;
	LinearLayout layoutButton, layoutButton2, layoutButton3;
	TextView txt1, txt2, txt3;
	ImageButton ibt;
	double userLatitude, userLongitude;
	String resString;
	ArrayList<Hospital> hospitals;
	Hospital[] hospitalArray;
	ArrayList<MContact> contacts, emer_contacts;
	SlidingDrawer sd;
	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflatedView = inflater.inflate(R.layout.fragment_layout_map,
				container, false);
		getCurrentLocation();
		hospitals = new ArrayList<Hospital>();
		contacts = new ArrayList<MContact>();
		emer_contacts = new ArrayList<MContact>();
		getHospitals();
		getContacts();
		getEmerContacts();
		if (hospitals.size() > 0) {
			hospitalArray = new Hospital[hospitals.size()];
			for (int i = 0; i < hospitals.size(); i++) {
				hospitalArray[i] = hospitals.get(i);
			}
		} else {
			hospitalArray = new Hospital[0];
		}

		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
			// TODO handle this situation
		}
		mBundle = savedInstanceState;
		mMapView = (MapView) inflatedView.findViewById(R.id.map);
		mMapView.onCreate(mBundle);
		setUpMapIfNeeded(inflatedView);

		layoutButton = (LinearLayout) inflatedView
				.findViewById(R.id.layoutButton);
		layoutButton2 = (LinearLayout) inflatedView
				.findViewById(R.id.layoutButton2);
		layoutButton3 = (LinearLayout) inflatedView
				.findViewById(R.id.layoutButton3);
		txt1 = (TextView) inflatedView.findViewById(R.id.textView1);
		txt2 = (TextView) inflatedView.findViewById(R.id.textView2);
		txt3 = (TextView) inflatedView.findViewById(R.id.textView3);
		ibt = (ImageButton) inflatedView.findViewById(R.id.imageButton1);
		sd = (SlidingDrawer) inflatedView.findViewById(R.id.slidingDrawer1);
		sd.close();
		listView = (ListView) inflatedView.findViewById(R.id.listView1);

		ibt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sd.animateOpen();
			}
		});

		layoutButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutButton.setBackgroundColor(getResources().getColor(
						R.color.black));
				txt1.setTextColor(getResources().getColor(R.color.white));
				layoutButton2.setBackgroundColor(getResources().getColor(
						R.color.white));
				txt2.setTextColor(getResources().getColor(R.color.black));
				layoutButton3.setBackgroundColor(getResources().getColor(
						R.color.white));
				txt3.setTextColor(getResources().getColor(R.color.black));
				listView.setAdapter(null);
				HospitalAdapter adapter = new HospitalAdapter(getActivity(),
						R.layout.hospital_list_row, hospitalArray);
				listView.setAdapter(adapter);
				setUpMap(1);
				if (sd.isOpened()) {
					sd.animateClose();
				}
			}
		});

		layoutButton2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutButton2.setBackgroundColor(getResources().getColor(
						R.color.black));
				txt2.setTextColor(getResources().getColor(R.color.white));
				layoutButton.setBackgroundColor(getResources().getColor(
						R.color.white));
				txt1.setTextColor(getResources().getColor(R.color.black));
				layoutButton3.setBackgroundColor(getResources().getColor(
						R.color.white));
				txt3.setTextColor(getResources().getColor(R.color.black));
				listView.setAdapter(null);
				MContactAdapter adapter = new MContactAdapter(getActivity(),
						R.layout.map_contact_list_row, contacts);
				listView.setAdapter(adapter);
				setUpMap(2);
				if (sd.isOpened()) {
					sd.animateClose();
				}
			}
		});

		layoutButton3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutButton3.setBackgroundColor(getResources().getColor(
						R.color.black));
				txt3.setTextColor(getResources().getColor(R.color.white));
				layoutButton.setBackgroundColor(getResources().getColor(
						R.color.white));
				txt1.setTextColor(getResources().getColor(R.color.black));
				layoutButton2.setBackgroundColor(getResources().getColor(
						R.color.white));
				txt2.setTextColor(getResources().getColor(R.color.black));
				listView.setAdapter(null);
				MContactAdapter adapter = new MContactAdapter(getActivity(),
						R.layout.map_contact_list_row, emer_contacts);
				listView.setAdapter(adapter);
				setUpMap(3);
				if (sd.isOpened()) {
					sd.animateClose();
				}
			}
		});

		HospitalAdapter adapter = new HospitalAdapter(getActivity(),
				R.layout.hospital_list_row, hospitalArray);
		listView = (ListView) inflatedView.findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		
		setMapTransparent((ViewGroup) inflatedView);
		return inflatedView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;
	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (mMap == null) {
			mMap = mMapView.getMap();
			if (mMap != null) {
				setUpMap(1);
			}
		}
	}

	private void getCurrentLocation() {
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Activity.LOCATION_SERVICE);
		Criteria c = new Criteria();
		String bestProvider = locationManager.getBestProvider(c, true);
		Location loc = locationManager.getLastKnownLocation(bestProvider);
		userLatitude = loc.getLatitude();
		userLongitude = loc.getLongitude();
	}

	private void setUpMap(int x) {
		// GoogleMapOptions op = new GoogleMapOptions();
		// op.zOrderOnTop(true);
		// SupportMapFragment.newInstance(op);
		LatLng currentLocation = new LatLng(userLatitude, userLongitude);
		mMap.clear();
		mMap.addMarker(new MarkerOptions().position(currentLocation).title(
				"current location"));
		if (x == 1) {
			if (hospitals.size() > 0) {
				for (int i = 0; i < hospitals.size(); i++) {
					LatLng hospitalLocation = new LatLng(
							hospitals.get(i).latitude,
							hospitals.get(i).longitude);
					mMap.addMarker(new MarkerOptions()
							.position(hospitalLocation)
							.title(hospitals.get(i).name)
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.pin2)));
				}
			}
		} else if (x == 2) {
			if (contacts.size() > 0) {
				for (int i = 0; i < contacts.size(); i++) {
					LatLng hospitalLocation = new LatLng(
							contacts.get(i).latitude, contacts.get(i).longitude);
					mMap.addMarker(new MarkerOptions()
							.position(hospitalLocation)
							.title(contacts.get(i).name)
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.pin2)));
				}
			}
		} else if (x == 3) {
			if (emer_contacts.size() > 0) {
				for (int i = 0; i < emer_contacts.size(); i++) {
					LatLng hospitalLocation = new LatLng(
							emer_contacts.get(i).latitude,
							emer_contacts.get(i).longitude);
					mMap.addMarker(new MarkerOptions()
							.position(hospitalLocation)
							.title(emer_contacts.get(i).name)
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.pin2)));
				}
			}
		}
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		mMap.getUiSettings().setZoomControlsEnabled(false);

		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				if (sd.isOpened()) {
					sd.animateClose();
				}

			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

	public class Hospital {
		String name, address, city, state, zipcode, telephone;
		double latitude, longitude;
		int distance;

		Hospital(String name, String address, String city, String state,
				String zipcode, String telephone, double latitude,
				double longitude, int distance) {
			this.name = name;
			this.address = address;
			this.city = city;
			this.state = state;
			this.zipcode = zipcode;
			this.telephone = telephone;
			this.latitude = latitude;
			this.longitude = longitude;
			this.distance = distance;
		}
	}

	public void getHospitals() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					String uri = SignUpAndSignIn.urlHead
							+ "/repository/hospital/index?latitude="
							+ String.valueOf(userLatitude) + "&longitude="
							+ String.valueOf(userLongitude);
					HttpGet httpget = new HttpGet(uri);

					HttpResponse response = SignUpAndSignIn.client
							.execute(httpget);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
					Log.d("Current Latitude", String.valueOf(userLatitude));
					Log.d("Current Logitude", String.valueOf(userLongitude));
					Log.d("GET HOSPITALS STATUS:", resString);
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
		try {
			JSONObject jobject = new JSONObject(resString);
			if (jobject.get("status").toString().equals("success")) {
				JSONArray jarray = jobject.getJSONArray("hospitals");
				if (jarray.length() > 0) {
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject ob = jarray.getJSONObject(i);
						String name = ob.getJSONObject("enus")
								.getString("name");
						String address = ob.getJSONObject("enus").getString(
								"address");
						String city = ob.getJSONObject("enus")
								.getString("city");
						String state = ob.getJSONObject("enus").getString(
								"state");
						String zipcode = ob.getString("zip_code");
						String telephone = ob.getString("telphone");
						double latitude = Double.parseDouble(ob
								.getString("latitude"));
						double longitude = Double.parseDouble(ob
								.getString("longitude"));
						int distance = Integer.parseInt(ob
								.getString("distance_in_mile"));
						Hospital h = new Hospital(name, address, city, state,
								zipcode, telephone, latitude, longitude,
								distance);
						hospitals.add(h);
					}
				}
			}
		} catch (Exception e) {

		}
	}

	public class MContact {
		Bitmap avatar;
		String name;
		String telephone;
		double latitude, longitude;

		MContact(Bitmap avatar, String name, String telephone, double latitude,
				double longitude) {
			this.avatar = avatar;
			this.name = name;
			this.telephone = telephone;
			this.latitude = latitude;
			this.longitude = longitude;
		}
	}

	public void getContacts() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					String uri = SignUpAndSignIn.urlHead
							+ "/sociality/contact/location/index?emergency=OFF";
					HttpGet httpget = new HttpGet(uri);

					HttpResponse response = SignUpAndSignIn.client
							.execute(httpget);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
					Log.d("GET CONTACTS STATUS:", resString);
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

		try {
			JSONObject jobject = new JSONObject(resString);
			JSONArray jarray = jobject.getJSONArray("contacts");
			if (jarray.length() > 0) {
				for (int i = 0; i < jarray.length(); i++) {
					url = jarray.getJSONObject(i).getString("avatar100");
					d = null;
					Thread t2 = new Thread(new Runnable() {
						public void run() {
							try {
								InputStream is = (InputStream) new URL(url)
										.getContent();
								d = BitmapFactory.decodeStream(is);
								is.close();
							} catch (Exception e) {
								Log.d("EXCEPTION:", e.toString());
							}
						}
					});

					t2.start();
					try {
						t2.join();
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
					String name = jarray.getJSONObject(i)
							.getString("full_name");
					String tele = jarray.getJSONObject(i)
							.getString("telephone");
					if (!jarray.getJSONObject(i).getString("latitude")
							.equals("null")
							&& !jarray.getJSONObject(i).getString("longitude")
									.equals("null")) {
						double latitude = Double.parseDouble(jarray
								.getJSONObject(i).getString("latitude"));
						double longitude = Double.parseDouble(jarray
								.getJSONObject(i).getString("longitude"));
						MContact mc = new MContact(d, name, tele, latitude,
								longitude);
						contacts.add(mc);
					}

				}
			}
		} catch (JSONException e) {
		}

	}

	public void getEmerContacts() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					String uri = SignUpAndSignIn.urlHead
							+ "/sociality/contact/location/index?emergency=ON";
					HttpGet httpget = new HttpGet(uri);

					HttpResponse response = SignUpAndSignIn.client
							.execute(httpget);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
					Log.d("GET EMERGENCY CONTACTS STATUS:", resString);
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

		try {
			JSONObject jobject = new JSONObject(resString);
			JSONArray jarray = jobject.getJSONArray("contacts");
			if (jarray.length() > 0) {
				for (int i = 0; i < jarray.length(); i++) {
					url = jarray.getJSONObject(i).getString("avatar100");
					d = null;
					Thread t2 = new Thread(new Runnable() {
						public void run() {
							try {
								InputStream is = (InputStream) new URL(url)
										.getContent();
								d = BitmapFactory.decodeStream(is);
								is.close();
							} catch (Exception e) {
								Log.d("EXCEPTION:", e.toString());
							}
						}
					});

					t2.start();
					try {
						t2.join();
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
					String name = jarray.getJSONObject(i)
							.getString("full_name");
					String tele = jarray.getJSONObject(i)
							.getString("telephone");
					if (jarray.getJSONObject(i).getString("latitude").length() > 0
							&& jarray.getJSONObject(i).getString("longitude")
									.length() > 0) {
						double latitude = Double.parseDouble(jarray
								.getJSONObject(i).getString("latitude"));
						double longitude = Double.parseDouble(jarray
								.getJSONObject(i).getString("longitude"));
						MContact mc = new MContact(d, name, tele, latitude,
								longitude);
						emer_contacts.add(mc);
					}
				}
			}
		} catch (JSONException e) {
		}

	}

	public class HospitalHolder {
		TextView distance;
		TextView name;
		TextView address1;
		TextView address2;
	}

	public class HospitalAdapter extends ArrayAdapter<Hospital> {
		Context context;
		int layoutResourceId;
		Hospital data[] = null;

		public HospitalAdapter(Context context, int layoutResourceId,
				Hospital[] data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("POSITION:", Integer.toString(position));

			View row = convertView;
			HospitalHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new HospitalHolder();
				holder.distance = (TextView) row.findViewById(R.id.textView1);
				holder.name = (TextView) row.findViewById(R.id.textView2);
				holder.address1 = (TextView) row.findViewById(R.id.textView3);
				holder.address2 = (TextView) row.findViewById(R.id.textView4);
				row.setTag(holder);
			} else {
				holder = (HospitalHolder) row.getTag();
			}

			SlidingDrawer sd2 = (SlidingDrawer) row
					.findViewById(R.id.slidingDrawer1);
			sd2.close();

			Hospital p = data[position];
			holder.distance.setText(p.distance + " mi");
			holder.name.setText(p.name);
			holder.address1.setText(p.address);
			holder.address2.setText(p.city + ", " + p.state + " " + p.zipcode);
			return row;
		}
	}

	public class MContactHolder {
		ImageView image;
		TextView name;
		TextView distance;
	}

	public class MContactAdapter extends ArrayAdapter<MContact> {
		Context context;
		int layoutResourceId;
		ArrayList<MContact> data;

		public MContactAdapter(Context context, int layoutResourceId,
				ArrayList<MContact> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("POSITION:", Integer.toString(position));

			View row = convertView;
			MContactHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new MContactHolder();
				holder.name = (TextView) row.findViewById(R.id.textView2);
				holder.image = (ImageView) row.findViewById(R.id.imageView2);
				row.setTag(holder);
			} else {
				holder = (MContactHolder) row.getTag();
			}

			SlidingDrawer sd2 = (SlidingDrawer) row
					.findViewById(R.id.slidingDrawer1);
			sd2.close();

			MContact p = data.get(position);

			holder.name.setText(p.name);
			holder.image.setImageBitmap(p.avatar);
			return row;
		}
	}

	private void setMapTransparent(ViewGroup group) {
		int childCount = group.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = group.getChildAt(i);
			if (child instanceof ViewGroup) {
				setMapTransparent((ViewGroup) child);
			} else if (child instanceof SurfaceView) {
				child.setBackgroundColor(0x00000000);
			}
		}
	}

}
