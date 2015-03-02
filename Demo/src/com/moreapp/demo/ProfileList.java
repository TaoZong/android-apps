package com.moreapp.demo;

import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.moreapp.demo.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProfileList extends Activity {
	static String resString;
	private String url;
	private Bitmap d;
	Profile[] pdata;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_profile_list);
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpGet httpget = new HttpGet(SignUpAndSignIn.urlHead + "/profile/index");
				try {
					HttpResponse response = SignUpAndSignIn.client.execute(httpget);
					HttpEntity resEntity = response.getEntity();
			        resString = EntityUtils.toString(resEntity);
			        Log.d("PROFILE LIST STATUS:", resString);
				} catch (Exception e) {
					
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			
		}
		Profile[] pdata2;
		JSONObject jobject;
		
		try {
			jobject = new JSONObject(resString);
			if (jobject.get("status").toString().equals("success")) {
				JSONArray jarray = jobject.getJSONArray("profiles");
				pdata = new Profile[jarray.length()];
				if(jarray != null) {
					for(int i = 0; i < jarray.length(); i++) {
						JSONObject object = jarray.getJSONObject(i);
						url = object.get("avatar100").toString();
						d = null;
						Thread t2 = new Thread( new Runnable() {
							public void run() {
								try {
							        InputStream is = (InputStream) new URL(url).getContent();
							        d = BitmapFactory.decodeStream(is);
							        is.close();
							    } catch (Exception e) {
							    	Log.d("EXCEPTION:",e.toString());
							    }
							}
						});
						
						t2.start();
						try {
							t2.join();
						} catch (InterruptedException e) {
//							e.printStackTrace();
						}
						String id = object.get("id").toString();
						String full_name = object.get("full_name").toString();
						String relationship = object.get("status").toString();
						Profile p = new Profile(d, id, full_name, relationship);
						pdata[i] = p;
					}
					
				}
				
			}
		} catch (JSONException e) {

		}
		if(pdata != null) {
			Bitmap b2 = pdata[0].avatar;
			String fullName = pdata[0].full_name;
			ImageView myImg = (ImageView) findViewById(R.id.imageView1);
			TextView myFullName = (TextView) findViewById(R.id.textView21);
			if(b2 != null) myImg.setImageBitmap(b2);
			myFullName.setText(fullName);			
		}
		if(pdata.length == 1) {
			pdata2 = null;
		}
		else {
			pdata2 = new Profile[pdata.length - 1];
			for(int i = 1; i < pdata.length; i++) {
				pdata2[i - 1] = pdata[i];
			}
		}
		if(pdata2 != null) {
			ProfileAdapter adapter = new ProfileAdapter(this, R.layout.profile_list_row, pdata2);
			ListView listView = (ListView)findViewById(R.id.listView1);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					onMenuItemClick(parent, view, position, id);
				}

			});
		}
		

		
		ImageButton ibt1 = (ImageButton)findViewById(R.id.imageButton1);
		ibt1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ProfileList.this,
						MainActivity.class);
				startActivity(intent);
				ProfileList.this.finish();
			}
		});
		
		ImageButton ibt2 = (ImageButton)findViewById(R.id.imageButton2);
		ibt2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ProfileList.this,
						AddProfile.class);
				startActivity(intent);
				ProfileList.this.finish();
			}
		});
		
		RelativeLayout rlayout = (RelativeLayout)findViewById(R.id.RelativeLayout1);
		rlayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ProfileList.this,
						ProfileDetails.class);
				Bundle bundle = new Bundle();
				String myId = pdata[0].id;
				bundle.putString("pid", myId);
				bundle.putString("relationship", "self");
				intent.putExtras(bundle);
				startActivity(intent);
				ProfileList.this.finish();
			}
		});
	}
	
	protected void onMenuItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		Intent intent = new Intent();
		intent.setClass(ProfileList.this,
				ProfileDetails.class);
		Bundle bundle = new Bundle();
		String myId = pdata[position + 1].id;
		String relationship = pdata[position + 1].relationship;
		bundle.putString("pid", myId);
		bundle.putString("relationship", relationship);
		intent.putExtras(bundle);
		startActivity(intent);
		ProfileList.this.finish();
		
	}

	public class Profile {
		Bitmap avatar;
		String id;
		String full_name;
		String relationship;
		
		public Profile(Bitmap avatar, String id, String full_name, String relationship) {
			this.avatar = avatar;
			this.id = id;
			this.full_name = full_name;
			this.relationship = relationship;
		}
	}
	
	public class ProfileHolder {
		ImageView img;
		TextView full_name;
		TextView relationship;
	}
	
	public class ProfileAdapter extends ArrayAdapter<Profile> {
		Context context; 
	    int layoutResourceId;    
	    Profile data[] = null;
	    
	    public ProfileAdapter(Context context, int layoutResourceId, Profile[] data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View row = convertView;
	    	ProfileHolder holder = null;
	    	if(row == null) {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new ProfileHolder();
	            holder.img = (ImageView)row.findViewById(R.id.imageView1);
	            holder.full_name = (TextView)row.findViewById(R.id.textView21);
	            holder.relationship = (TextView)row.findViewById(R.id.textView22);
	            row.setTag(holder);
	            
	            Profile p = data[position];
		        if(p.avatar != null) holder.img.setImageBitmap(p.avatar);
	        }
	        else
	        {
	            holder = (ProfileHolder)row.getTag();
	        }
	        
	        Profile p = data[position];
	        holder.full_name.setText(p.full_name);
	        holder.relationship.setText(p.relationship);
	    	
	    	return row;
	    	
	    	
	    }
	    
	    

	}

}
