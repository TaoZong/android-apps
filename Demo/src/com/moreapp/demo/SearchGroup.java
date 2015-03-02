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
import org.json.JSONObject;

import com.moreapp.demo.R;
import com.moreapp.demo.LayoutCircle.ContactAdapter;
import com.moreapp.demo.LayoutCircle.Group;
import com.moreapp.demo.LayoutCircle.GroupHolder;
import com.moreapp.demo.SearchContact.User;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class SearchGroup extends Activity {
	EditText edittxt;
	String resString, url;
	User[] users;
	Bitmap d;
	ImageButton ibt;
	ListView listview;
	TextView txtview;
	ArrayList<Group> groups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_search_group);
		listview = (ListView) findViewById(R.id.listView1);
		txtview = (TextView) findViewById(R.id.textView1);
		edittxt = (EditText) findViewById(R.id.editText1);
		ibt = (ImageButton) findViewById(R.id.imageButton1);
		ibt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager mInputMethodManager;
				mInputMethodManager = (InputMethodManager) SearchGroup.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				mInputMethodManager.hideSoftInputFromWindow(SearchGroup.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
				SearchGroup.this.finish();
			}
		});
		showNearbyGroups();

		TextWatcher textWatcher = new TextWatcher() {
			private Handler mHandler = new Handler();
			String s;
			Runnable mFilterTask = new Runnable() {

				@Override
				public void run() {
					if (s.length() == 0) {
						showNearbyGroups();

					} else {
						showSearchResult(s);

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

	}

	class Group {
		String id;
		String name;
		Bitmap[] imgs;

		Group(String id, String name, Bitmap[] imgs) {
			this.id = id;
			this.name = name;
			this.imgs = imgs;
		}
	}

	class GroupHolder {
		ImageView img1, img2, img3, img4, img5, img6;
		TextView full_name;
		Button bt1, bt2;
	}

	public class GroupAdapter extends ArrayAdapter<Group> {
		Context context;
		int layoutResourceId;
		ArrayList<Group> data;

		public GroupAdapter(Context context, int layoutResourceId,
				ArrayList<Group> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			GroupHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new GroupHolder();
				holder.full_name = (TextView) row.findViewById(R.id.textView1);
				holder.img1 = (ImageView) row.findViewById(R.id.imageView1);
				holder.img2 = (ImageView) row.findViewById(R.id.imageView2);
				holder.img3 = (ImageView) row.findViewById(R.id.imageView3);
				holder.img4 = (ImageView) row.findViewById(R.id.imageView4);
				holder.img5 = (ImageView) row.findViewById(R.id.imageView5);
				holder.img6 = (ImageView) row.findViewById(R.id.imageView6);
				holder.bt1 = (Button) row.findViewById(R.id.button1);
				holder.bt2 = (Button) row.findViewById(R.id.button2);
				row.setTag(holder);
			} else {
				holder = (GroupHolder) row.getTag();
			}

			final Group g = data.get(position);
			final GroupHolder holder2 = (GroupHolder) row.getTag();
			holder2.full_name.setText(g.name);
			int num = g.imgs.length;
			if (num > 0) {
				if (g.imgs[0] != null)
					holder2.img1.setImageBitmap(g.imgs[0]);
				else
					holder2.img1.setImageResource(R.drawable.profilepic);
				if (num > 1) {
					if (g.imgs[1] != null)
						holder2.img2.setImageBitmap(g.imgs[1]);
					else
						holder2.img2.setImageResource(R.drawable.profilepic);
					if (num > 2) {
						if (g.imgs[2] != null)
							holder2.img3.setImageBitmap(g.imgs[2]);
						else
							holder2.img3
									.setImageResource(R.drawable.profilepic);
						if (num > 3) {
							if (g.imgs[3] != null)
								holder2.img4.setImageBitmap(g.imgs[3]);
							else
								holder2.img4
										.setImageResource(R.drawable.profilepic);
							if (num > 4) {
								if (g.imgs[4] != null)
									holder2.img5.setImageBitmap(g.imgs[4]);
								else
									holder2.img5
											.setImageResource(R.drawable.profilepic);
								if (num > 5) {
									if (g.imgs[5] != null)
										holder2.img6.setImageBitmap(g.imgs[5]);
									else
										holder2.img6
												.setImageResource(R.drawable.profilepic);
								}
							}
						}
					}
				}
			}

			holder2.bt1.setText("Join");
			holder2.bt1.setBackgroundColor(getResources().getColor(
					R.color.green));
			holder2.bt1.setVisibility(View.VISIBLE);
			holder2.bt2.setText("Quit");
			holder2.bt2
					.setBackgroundColor(getResources().getColor(R.color.red));
			holder2.bt2.setVisibility(View.GONE);

			holder2.bt1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					holder2.bt2.setVisibility(View.VISIBLE);
					holder2.bt1.setVisibility(View.GONE);
					joinGroup(data.get(position).id);
				}
			});

			holder2.bt2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					holder2.bt1.setVisibility(View.VISIBLE);
					holder2.bt2.setVisibility(View.GONE);
					quitGroup(data.get(position).id);
				}
			});
			return row;
		}
	}

	void joinGroup(final String id) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/join");
					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("cid", id, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("JOIN GROUP STATUS:", resString);
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

	void quitGroup(final String id) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/quit");
					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("cid", id, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("QUIT GROUP STATUS:", resString);
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

	void showNearbyGroups() {
		txtview.setText("Nearby Group");
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria c = new Criteria();
		String bestProvider = locationManager.getBestProvider(c, true);
		Location loc = locationManager.getLastKnownLocation(bestProvider);
		final String latitude = Double.toString(loc.getLatitude());
		final String longitude = Double.toString(loc.getLongitude());
		groups = new ArrayList<Group>();
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/nearby");
					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("latitude", latitude,
							ContentType.APPLICATION_JSON);
					entity.addTextBody("longitude", longitude,
							ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("NEARBY GROUP STATUS:", resString);
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
			if (jobject.get("status").toString().equals("success")) {
				JSONArray jarray = jobject.getJSONArray("circles");
				if (jarray.length() > 0) {
					for (int i = 0; i < jarray.length(); i++) {
						String id = jarray.getJSONObject(i).getString("id");
						String full_name = jarray.getJSONObject(i).getString(
								"full_name");

						JSONArray jarray2 = jarray.getJSONObject(i)
								.getJSONArray("admins");
						Bitmap[] imgs = new Bitmap[jarray2.length()];
						for (int j = 0; j < jarray2.length(); j++) {
							url = jarray2.getJSONObject(j).getString(
									"avatar100");
							d = null;
							Thread t2 = new Thread(new Runnable() {
								public void run() {
									try {
										InputStream is = (InputStream) new URL(
												url).getContent();
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

							}
							imgs[j] = d;
						}
						Group g = new Group(id, full_name, imgs);
						groups.add(g);
					}
				}

			}
		} catch (Exception e) {

		}

		GroupAdapter adapter = new GroupAdapter(SearchGroup.this,
				R.layout.group_list_row, groups);
		listview.setAdapter(adapter);
	}

	void showSearchResult(final String s) {
		txtview.setText("Search Result");
		groups = new ArrayList<Group>();
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/search");
					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("query", s, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("SEARCH GROUP STATUS:", resString);
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
			if (jobject.get("status").toString().equals("success")) {
				JSONArray jarray = jobject.getJSONArray("circles");
				if (jarray.length() > 0) {
					for (int i = 0; i < jarray.length(); i++) {
						String id = jarray.getJSONObject(i).getString("id");
						String full_name = jarray.getJSONObject(i).getString(
								"full_name");

						JSONArray jarray2 = jarray.getJSONObject(i)
								.getJSONArray("admins");
						Bitmap[] imgs = new Bitmap[jarray2.length()];
						for (int j = 0; j < jarray2.length(); j++) {
							url = jarray2.getJSONObject(j).getString(
									"avatar100");
							d = null;
							Thread t2 = new Thread(new Runnable() {
								public void run() {
									try {
										InputStream is = (InputStream) new URL(
												url).getContent();
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

							}
							imgs[j] = d;
						}
						Group g = new Group(id, full_name, imgs);
						groups.add(g);
					}
				}

			}
		} catch (Exception e) {

		}

		GroupAdapter adapter = new GroupAdapter(SearchGroup.this,
				R.layout.group_list_row, groups);
		listview.setAdapter(adapter);
	}
}
