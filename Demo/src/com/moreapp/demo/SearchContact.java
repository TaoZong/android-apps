package com.moreapp.demo;

import java.io.InputStream;
import java.net.URL;

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
import com.moreapp.demo.LayoutCircle.ContactHolder;
import com.moreapp.demo.ProfileList.Profile;
import com.moreapp.demo.ProfileList.ProfileAdapter;
import com.moreapp.demo.ProfileList.ProfileHolder;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

public class SearchContact extends Activity {
	SearchView sv;
	String resString, url;
	User[] users;
	Bitmap d;
	ImageButton ibt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_search_contact);
		
		ibt = (ImageButton)findViewById(R.id.imageButton1);
		ibt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sv.setVisibility(View.GONE);
				SearchContact.this.finish();
			}
		});
		sv = (SearchView)findViewById(R.id.searchView1);
		sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				searchUser(query);
				setListView();
				return false;
			}
			
		});

	}
	
	void searchUser(final String query) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpGet httpget = new HttpGet(SignUpAndSignIn.urlHead + "/sociality/contact/search?query=" + query);
				try {
					HttpResponse response = SignUpAndSignIn.client.execute(httpget);
					HttpEntity resEntity = response.getEntity();
			        resString = EntityUtils.toString(resEntity);
			        Log.d("SEARCH USER STATUS:", resString);
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
		users = new User[0];
		try {
			jobject = new JSONObject(resString);
			if (jobject.get("status").toString().equals("success")) {
				JSONArray jarray = jobject.getJSONArray("users");
				if(jarray.length() > 0) users = new User[jarray.length()];
				for(int i = 0; i < jarray.length(); i++) {
					url = jarray.getJSONObject(i).getString("avatar100");
					String id = jarray.getJSONObject(i).getString("id");
					String full_name = jarray.getJSONObject(i).getString("full_name");
					String relationship = jarray.getJSONObject(i).getJSONObject("connection").getString("relationship");
					
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
//						e.printStackTrace();
					}
					
					User u = new User(d, id, full_name, relationship);
					users[i] = u;
				}
			}
		} catch(Exception e) {
			
		}
	}
	
	void setListView() {
		UserAdapter adapter = new UserAdapter(this, R.layout.user_list_row, users);
		ListView listview = (ListView)findViewById(R.id.listView1);
		listview.setAdapter(adapter);
	}
	
	public class User {
		Bitmap avatar;
		String id;
		String full_name;
		String relationship;
		
		public User(Bitmap avatar, String id, String full_name, String relationship) {
			this.avatar = avatar;
			this.id = id;
			this.full_name = full_name;
			this.relationship = relationship;
		}
	}
	
	public class UserHolder {
		ImageView img;
		TextView full_name;
		TextView relationship;
	}
	
	public class UserAdapter extends ArrayAdapter<User> {
		Context context; 
	    int layoutResourceId;    
	    User data[] = null;
	    
	    public UserAdapter(Context context, int layoutResourceId, User[] data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View row = convertView;
	    	UserHolder holder = new UserHolder();
	    	if(row == null) {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            holder.img = (ImageView)row.findViewById(R.id.imageView1);
	            holder.full_name = (TextView)row.findViewById(R.id.textView1);
	            holder.relationship = (TextView)row.findViewById(R.id.textView2);
	            row.setTag(holder);
	            
	            final User p = data[position];
	            if(p.avatar != null) holder.img.setImageBitmap(p.avatar);
	            final Button bt = (Button)row.findViewById(R.id.button1);
	            final Button bt2 = (Button)row.findViewById(R.id.button2);
	            final Button bt3 = (Button)row.findViewById(R.id.button3);
	            if(p.relationship.equals("unconnected")) {
	            	bt.setVisibility(View.VISIBLE);
	            	bt2.setVisibility(View.GONE);
	            	bt3.setVisibility(View.GONE);
	            	
	            }
	            else if(p.relationship.equals("sent_request")){
	            	bt.setVisibility(View.VISIBLE);
	            	bt2.setVisibility(View.GONE);
	            	bt3.setVisibility(View.GONE);
	            	bt.setClickable(false);
	            	bt.setText("Sent");
	            	bt.setBackgroundColor(getResources().getColor(R.color.light_grey));
	            }
	            else if(p.relationship.equals("got_request")){
	            	bt.setVisibility(View.GONE);
	            	bt2.setVisibility(View.VISIBLE);
	            	bt3.setVisibility(View.VISIBLE);
	            	
	            }
	            else {
	            	bt.setVisibility(View.GONE);
	            	bt2.setVisibility(View.GONE);
	            	bt3.setVisibility(View.GONE);
	            }
	            
	        }
	        else
	        {
	        	holder = (UserHolder)row.getTag();
	        }
	    	
	    	final User p = data[position];
            
            final Button bt = (Button)row.findViewById(R.id.button1);
            final Button bt2 = (Button)row.findViewById(R.id.button2);
            final Button bt3 = (Button)row.findViewById(R.id.button3);
            
            
            
            
            holder.full_name.setText(p.full_name);
	        if(p.relationship.equals("connected")) holder.relationship.setText("connected");
	        else holder.relationship.setText("unconnected");
	        
	        bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
	            	addUser(p.id, bt);
				}
			});
            bt2.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					acceptRequest(p.id);
					p.relationship = "connected";
					notifyDataSetChanged();
				}
        	});
        	bt3.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					declineRequest(p.id);
					p.relationship = "unconnected";
					notifyDataSetChanged();
				}
			});
	    	return row;
	    	
	    	
	    }
	    
	    

	}
	
	void addUser(final String id, final Button bt) {
		final String[] displayedValues = {"Family","Friend"};
		final Dialog d = new Dialog(SearchContact.this);
        d.setTitle("You want to send request as?");
        d.setContentView(R.layout.dialog_blood_type);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setDisplayedValues(displayedValues);
        np.setMaxValue(displayedValues.length - 1);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							
							String status = displayedValues[np.getValue()];
							HttpPost post = new HttpPost(
									SignUpAndSignIn.urlHead + "/sociality/contact/request");
							
							MultipartEntityBuilder entity = MultipartEntityBuilder.create();
							entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
							entity.addTextBody("uid", id, ContentType.APPLICATION_JSON);
							entity.addTextBody("status", status, ContentType.APPLICATION_JSON);
							post.setEntity(entity.build());
							
							HttpResponse response = SignUpAndSignIn.client
									.execute(post);

							// Get response
							HttpEntity resEntity = response.getEntity();
							resString = EntityUtils.toString(resEntity);

							Log.d("ADD REQUEST STATUS:", resString);
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
						bt.setClickable(false);
				    	bt.setText("Sent");
				    	bt.setBackgroundColor(getResources().getColor(R.color.light_grey));
					
					}
				} catch (JSONException e) {
					// e.printStackTrace();
				}
				
				
				d.dismiss();
			}
		});
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		d.show();
		
	}
	
	void acceptRequest(final String id) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(
							SignUpAndSignIn.urlHead + "/sociality/contact/accept");
					
					MultipartEntityBuilder entity = MultipartEntityBuilder.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("uid", id, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("ACCEPT REQUEST STATUS:", resString);
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
	
	void declineRequest(final String id) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(
							SignUpAndSignIn.urlHead + "/sociality/contact/decline");
					
					MultipartEntityBuilder entity = MultipartEntityBuilder.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("uid", id, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("DECLINE REQUEST STATUS:", resString);
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
