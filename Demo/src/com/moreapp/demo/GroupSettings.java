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
import com.moreapp.demo.LayoutCircle.Contact;
import com.moreapp.demo.LayoutCircle.ContactHolder;
import com.moreapp.demo.LayoutCircle.Group;
import com.moreapp.demo.LayoutCircle.GroupAdapter;
import com.moreapp.demo.LayoutCircle.GroupHolder;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class GroupSettings extends Activity {
	String cid;
	ListView listview;
	String resString, url;
	ArrayList<GroupMember> groupmembers;
	Bitmap d;
	Switch s;
	double latitude, longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_group_settings);
		Bundle b = this.getIntent().getExtras();
		cid = b.getString("cid");
		listview = (ListView) findViewById(R.id.listView1);
		s = (Switch) findViewById(R.id.switch1);
		groupmembers = new ArrayList<GroupMember>();
		
		LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		Criteria c = new Criteria();
		String bestProvider = locationManager.getBestProvider(c, true);
        Location loc = locationManager.getLastKnownLocation(bestProvider);
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpGet httpget = new HttpGet(SignUpAndSignIn.urlHead
						+ "/sociality/circle/member/index?cid=" + cid);
				try {
					HttpResponse response = SignUpAndSignIn.client
							.execute(httpget);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
					Log.d("GROUP MEMBER LIST STATUS:", resString);
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
				JSONArray jarray = jobject.getJSONArray("members");
				if (jarray.length() > 0) {
					for (int i = 0; i < jarray.length(); i++) {
						url = jarray.getJSONObject(i).getString("avatar100");
						String id = jarray.getJSONObject(i).getString("id");
						String name = jarray.getJSONObject(i).getString(
								"full_name");
						boolean isAdmin = jarray.getJSONObject(i).getBoolean(
								"is_admin");
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

						}

						GroupMember gm = new GroupMember(id, name, d, isAdmin);
						groupmembers.add(gm);
					}
				}

			}
		} catch (Exception e) {

		}
		GroupMemberAdapter adapter = new GroupMemberAdapter(this,
				R.layout.group_member_list_row, groupmembers);
		listview.setAdapter(adapter);
		
		ImageButton ibt = (ImageButton) findViewById(R.id.imageButton1);
		ibt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GroupSettings.this,
						GroupMessage.class);
				Bundle bundle = new Bundle();
				bundle.putString("cid", cid);
				intent.putExtras(bundle);
				startActivity(intent);
				GroupSettings.this.finish();
			}
		});
		
		s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if(isChecked == true) {
		        	activate();
		        }
		        else {
		        	inactivate();
		        }
		    }
		});
		

	}

	class GroupMember {
		String id;
		String full_name;
		Bitmap avatar;
		boolean isAdmin;

		GroupMember(String id, String full_name, Bitmap avatar, boolean isAdmin) {
			this.id = id;
			this.full_name = full_name;
			this.avatar = avatar;
			this.isAdmin = isAdmin;
		}
	}

	class GroupMemberHolder {
		ImageView image;
		TextView name;
		CheckBox cb;
		Button bt;
	}

	class GroupMemberAdapter extends ArrayAdapter<GroupMember> {
		Context context;
		int layoutResourceId;
		ArrayList<GroupMember> data;

		public GroupMemberAdapter(Context context, int layoutResourceId,
				ArrayList<GroupMember> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			GroupMemberHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new GroupMemberHolder();
				holder.image = (ImageView) row.findViewById(R.id.imageView1);
				holder.name = (TextView) row.findViewById(R.id.textView1);
				holder.cb = (CheckBox) row.findViewById(R.id.checkBox1);
				holder.bt = (Button) row.findViewById(R.id.button1);
				row.setTag(holder);
			} else {
				holder = (GroupMemberHolder) row.getTag();
			}
			final GroupMember gm = data.get(position);
			if(gm.avatar != null) holder.image.setImageBitmap(gm.avatar);
			holder.name.setText(gm.full_name);
			if (gm.isAdmin)
				holder.cb.setChecked(true);
			else
				holder.cb.setChecked(false);
			holder.cb
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								authorize(cid, gm.id);
							} else {
								deauthorize(cid, gm.id);
							}
						}

					});
			holder.bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removeGM(cid, gm.id);
					data.remove(position);
					notifyDataSetChanged();
				}
			});
			
			return row;
		}
	}
	
	void authorize(final String cid, final String uid) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/member/authorize");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("cid", cid, ContentType.APPLICATION_JSON);
					entity.addTextBody("uid", uid, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("AUTHORIZE GROUP MEMBER STATUS:", resString);
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
	
	void deauthorize(final String cid, final String uid) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/member/deauthorize");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("cid", cid, ContentType.APPLICATION_JSON);
					entity.addTextBody("uid", uid, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("DEAUTHORIZE GROUP MEMBER STATUS:", resString);
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
	
	void removeGM(final String cid, final String uid) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/member/remove");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("cid", cid, ContentType.APPLICATION_JSON);
					entity.addTextBody("uid", uid, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("REMOVE GROUP MEMBER STATUS:", resString);
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
	
	void activate() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/broadcast/activate");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("cid", cid, ContentType.APPLICATION_JSON);
					entity.addTextBody("latitude", String.valueOf(latitude), ContentType.APPLICATION_JSON);
					entity.addTextBody("longitude", String.valueOf(longitude), ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("ACTIVATE GROUP STATUS:", resString);
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
	
	void inactivate() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/broadcast/deactivate");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("cid", cid, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("DEACTIVATE GROUP STATUS:", resString);
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
