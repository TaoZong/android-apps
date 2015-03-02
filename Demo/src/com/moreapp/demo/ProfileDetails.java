package com.moreapp.demo;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.moreapp.demo.R;
import com.moreapp.demo.ProfileList.Profile;
import com.moreapp.demo.ProfileList.ProfileAdapter;
import com.moreapp.demo.ProfileList.ProfileHolder;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class ProfileDetails extends FragmentActivity {
	Boolean flag1 = true, flag2 = true;
	Boolean editflag1 = false,editflag2 = false,editflag3 = false, addflag1 = false;
	static TextView edittxt3, edittxt4, edittxt5;
	static EditText edittxt1, edittxt2, edittxt6, edittxt7, edittxt8,
		edittxt9, edittxt10,edittxt11,edittxt12;
	static String resString, url, editable;
	LinearLayout doctorLayout;
	String pid, relationship;
	Bitmap d;
	ImageView image;
	ArrayList<MedicalRecord> data1, data2, data3, data4, data5, data6;
	ArrayList<String> tIdList1, tIdList2, tIdList3, tIdList4, tIdList5, tIdList6;
	ListView listView1,listView2,listView3,listView4,listView5,listView6;
	static final String[] categories = {"allergy", "condition", "medication", 
			"supplement", "surgery", "vaccination"};
	PagerAdapter mPagerAdapter;
	ViewPager pager;
	JSONArray doctors;
	Button button1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_profile_details);
		Bundle b = this.getIntent().getExtras();
        pid = b.getString("pid");
        relationship = b.getString("relationship");
        TextView tv = (TextView)findViewById(R.id.textView2);
        tv.setText(relationship);
        image = (ImageView)findViewById(R.id.imageView1);
        edittxt3 = (TextView)findViewById(R.id.editText3);
        edittxt4 = (TextView)findViewById(R.id.editText4);
        edittxt5 = (TextView)findViewById(R.id.editText5);
        edittxt1 = (EditText)findViewById(R.id.editText1);
        edittxt2 = (EditText)findViewById(R.id.editText2);
        edittxt6 = (EditText)findViewById(R.id.editText6);
        edittxt7 = (EditText)findViewById(R.id.editText7);
        edittxt8 = (EditText)findViewById(R.id.editText8);
        edittxt9 = (EditText)findViewById(R.id.editText9);
        edittxt10 = (EditText)findViewById(R.id.editText10);
        edittxt11 = (EditText)findViewById(R.id.editText11);
        edittxt12 = (EditText)findViewById(R.id.editText12);
        doctorLayout = (LinearLayout)findViewById(R.id.LinearLayout2);
        doctorLayout.setVisibility(View.GONE);
        pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setVisibility(View.VISIBLE);
		d = null;
		url = "";
		

        
        Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					String uri = SignUpAndSignIn.urlHead + "/profile?pid=" + pid;
					HttpGet httpget = new HttpGet(uri);
				
					HttpResponse response = SignUpAndSignIn.client.execute(httpget);
					HttpEntity resEntity = response.getEntity();
			        resString = EntityUtils.toString(resEntity);
			        Log.d("PROFILE DETAILS STATUS:", resString);
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
				url = jobject.getJSONObject("basic_info").getString("avatar220");
				editable = jobject.getString("editable");
				String first_name = jobject.getJSONObject("basic_info").getString("first_name");
				String last_name = jobject.getJSONObject("basic_info").getString("last_name");
				String gender = jobject.getJSONObject("basic_info").getString("gender");
				String blood_type = jobject.getJSONObject("basic_info").getString("blood_type");
				String nationality = jobject.getJSONObject("basic_info").getString("nationality");
				String telephone = jobject.getJSONObject("basic_info").getString("telephone");
				String address = jobject.getJSONObject("basic_info").getString("address");
				String date_of_birth = jobject.getJSONObject("basic_info").getString("date_of_birth");
				
				edittxt1.setText(first_name);
				edittxt2.setText(last_name);
				if(date_of_birth.equals("no_date_of_birth")) edittxt3.setText("");
				else edittxt3.setText(date_of_birth);
				edittxt6.setText(telephone);
				edittxt7.setText(address);
				edittxt8.setText(nationality);
				edittxt4.setText(blood_type);
				edittxt5.setText(gender);
				
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
					
				}
				
				if(d != null) image.setImageBitmap(d);
				else image.setImageResource(R.drawable.profilepic);
				
				doctors = jobject.getJSONArray("physicians");
				
				data1 = new ArrayList<MedicalRecord>();
				data2 = new ArrayList<MedicalRecord>();
				data3 = new ArrayList<MedicalRecord>();
				data4 = new ArrayList<MedicalRecord>();
				data5 = new ArrayList<MedicalRecord>();
				data6 = new ArrayList<MedicalRecord>();
				tIdList1 = new ArrayList<String>();
				tIdList2 = new ArrayList<String>();
				tIdList3 = new ArrayList<String>();
				tIdList4 = new ArrayList<String>();
				tIdList5 = new ArrayList<String>();
				tIdList6 = new ArrayList<String>();
				JSONArray array1 = jobject.getJSONObject("med_info").getJSONArray("allergies");
				if(array1.length() > 0) {
					for(int i = 0; i < array1.length(); i++) {
						String en = array1.getJSONObject(i).getString("enus");
						String zh = array1.getJSONObject(i).getString("zhcn");
						MedicalRecord mr = new MedicalRecord(en, zh);
						data1.add(mr);
						tIdList1.add(array1.getJSONObject(i).getString("term_id"));					
					}
				}
				JSONArray array2 = jobject.getJSONObject("med_info").getJSONArray("conditions");
				if(array2.length() > 0) {
					for(int i = 0; i < array2.length(); i++) {
						String en = array2.getJSONObject(i).getString("enus");
						String zh = array2.getJSONObject(i).getString("zhcn");
						MedicalRecord mr = new MedicalRecord(en, zh);
						data2.add(mr);
						tIdList2.add(array2.getJSONObject(i).getString("term_id"));					
					}
				}
				JSONArray array3 = jobject.getJSONObject("med_info").getJSONArray("medications");
				if(array3.length() > 0) {
					for(int i = 0; i < array3.length(); i++) {
						String en = array3.getJSONObject(i).getString("enus");
						String zh = array3.getJSONObject(i).getString("zhcn");
						MedicalRecord mr = new MedicalRecord(en, zh);
						data3.add(mr);
						tIdList3.add(array3.getJSONObject(i).getString("term_id"));					
					}
				}
				JSONArray array4 = jobject.getJSONObject("med_info").getJSONArray("supplements");
				if(array4.length() > 0) {
					for(int i = 0; i < array4.length(); i++) {
						String en = array4.getJSONObject(i).getString("enus");
						String zh = array4.getJSONObject(i).getString("zhcn");
						MedicalRecord mr = new MedicalRecord(en, zh);
						data4.add(mr);
						tIdList4.add(array4.getJSONObject(i).getString("term_id"));					
					}
				}
				JSONArray array5 = jobject.getJSONObject("med_info").getJSONArray("surgeries");
				if(array5.length() > 0) {
					for(int i = 0; i < array5.length(); i++) {
						String en = array5.getJSONObject(i).getString("enus");
						String zh = array5.getJSONObject(i).getString("zhcn");
						MedicalRecord mr = new MedicalRecord(en, zh);
						data5.add(mr);
						tIdList5.add(array5.getJSONObject(i).getString("term_id"));					
					}
				}
				JSONArray array6 = jobject.getJSONObject("med_info").getJSONArray("vaccinations");
				if(array6.length() > 0) {
					for(int i = 0; i < array6.length(); i++) {
						String en = array6.getJSONObject(i).getString("enus");
						String zh = array6.getJSONObject(i).getString("zhcn");
						MedicalRecord mr = new MedicalRecord(en, zh);
						data6.add(mr);
						tIdList6.add(array6.getJSONObject(i).getString("term_id"));					
					}
				}
				
				disableEditProfile();
				
				MedicalRecordAdapter adapter1 = new MedicalRecordAdapter(this, R.layout.edit_medical_list_row, data1, categories[0]);
				listView1 = (ListView)findViewById(R.id.listView1);
				listView1.setAdapter(adapter1);
				setListViewHeightBasedOnChildren(listView1);
				
				MedicalRecordAdapter adapter2 = new MedicalRecordAdapter(this, R.layout.edit_medical_list_row, data2, categories[1]);
				listView2 = (ListView)findViewById(R.id.listView2);
				listView2.setAdapter(adapter2);
				setListViewHeightBasedOnChildren(listView2);
				
				
				MedicalRecordAdapter adapter3 = new MedicalRecordAdapter(this, R.layout.edit_medical_list_row, data3, categories[2]);
				listView3 = (ListView)findViewById(R.id.listView3);
				listView3.setAdapter(adapter3);
				setListViewHeightBasedOnChildren(listView3);
				
				MedicalRecordAdapter adapter4 = new MedicalRecordAdapter(this, R.layout.edit_medical_list_row, data4, categories[3]);
				listView4 = (ListView)findViewById(R.id.listView4);
				listView4.setAdapter(adapter4);
				setListViewHeightBasedOnChildren(listView4);
				
				MedicalRecordAdapter adapter5 = new MedicalRecordAdapter(this, R.layout.edit_medical_list_row, data5, categories[4]);
				listView5 = (ListView)findViewById(R.id.listView5);
				listView5.setAdapter(adapter5);
				setListViewHeightBasedOnChildren(listView5);
				
				MedicalRecordAdapter adapter6 = new MedicalRecordAdapter(this, R.layout.edit_medical_list_row, data6, categories[5]);
				listView6 = (ListView)findViewById(R.id.listView6);
				listView6.setAdapter(adapter6);
				setListViewHeightBasedOnChildren(listView6);
			}
		} catch (Exception e) {
			
		}

        
		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ProfileDetails.this,
						Layout5.class);
				Bundle bundle = new Bundle();
				bundle.putString("pid", pid);
				bundle.putString("relationship", relationship);
				intent.putExtras(bundle);
				startActivity(intent);
				ProfileDetails.this.finish();
			}
		});
		
		button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(addflag1) {
					addflag1 = false;
					button1.setText("+ Add Doctor");
					button1.setBackgroundColor(getResources().getColor(R.color.light_grey));
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {
								String d_first_name = edittxt9.getText().toString();
								String d_last_name = edittxt10.getText().toString();
								String d_telephone = edittxt11.getText().toString();
								String d_address = edittxt12.getText().toString();
								
								HttpPost post = new HttpPost(
										SignUpAndSignIn.urlHead + "/profile/physician/add");
								
								ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
								pairs.add(new BasicNameValuePair("pid", pid));
								pairs.add(new BasicNameValuePair("first_name", d_first_name));
								pairs.add(new BasicNameValuePair("last_name", d_last_name));
								pairs.add(new BasicNameValuePair("telephone", d_telephone));
								pairs.add(new BasicNameValuePair("address", d_address));
								UrlEncodedFormEntity ent = new UrlEncodedFormEntity(pairs);
								post.setEntity(ent);
	
								// Execute HTTP Post Request
								HttpResponse response = SignUpAndSignIn.client.execute(post);
								HttpEntity resEntity = response.getEntity();
						        resString = EntityUtils.toString(resEntity);
						        Log.d("ADD DOCTOR STATUS:", resString);
							} catch(Exception e) {
								
							}
						}
					});
					
					t.start();
					try {
						t.join();
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
					
					reloadDocs();
					
					
				}
				else {
					addflag1 = true;
					button1.setText("Finish");
					button1.setBackgroundColor(getResources().getColor(R.color.light_blue));
					pager.setVisibility(View.GONE);
					doctorLayout.setVisibility(View.VISIBLE);
				}
				
				
			}
		});
        
        ImageButton bt1 = (ImageButton)findViewById(R.id.imageButton1);
        bt1.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ProfileDetails.this,
						ProfileList.class);
				startActivity(intent);
				ProfileDetails.this.finish();
			}
        });
        
        ImageButton bt2 = (ImageButton)findViewById(R.id.imageButton2);       
        bt2.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(flag1) {
					LinearLayout layout1 = (LinearLayout)findViewById(R.id.layout1);
					layout1.setVisibility(View.GONE);
					flag1 = false;
				}
				else {
					LinearLayout layout1 = (LinearLayout)findViewById(R.id.layout1);
					layout1.setVisibility(View.VISIBLE);
					flag1 = true;
				}
			}
        	
        });
        
        
        
        final ImageButton bt3 = (ImageButton)findViewById(R.id.imageButton3);
        bt3.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(editflag1) {
					bt3.setImageResource(R.drawable.edit);
					saveContents();
					disableEditProfile();
					editflag1 = false;
					LinearLayout layout1 = (LinearLayout)findViewById(R.id.layout1);
					layout1.setVisibility(View.VISIBLE);
					flag1 = true;
				}
				else {
					bt3.setImageResource(R.drawable.save);
					enableEditProfile();
					editflag1 = true;
					LinearLayout layout1 = (LinearLayout)findViewById(R.id.layout1);
					layout1.setVisibility(View.VISIBLE);
					flag1 = true;
				}
			}
        	
        });
        
        
        

        final ImageButton bt5 = (ImageButton)findViewById(R.id.imageButton5);
        bt5.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(editflag3) {
					editflag3 = false;
					bt5.setImageResource(R.drawable.edit);
					listView1.invalidateViews();
					listView2.invalidateViews();
					listView3.invalidateViews();
					listView4.invalidateViews();
					listView5.invalidateViews();
					listView6.invalidateViews();
				}
				else {
					editflag3 = true;
					bt5.setImageResource(R.drawable.save);
					listView1.invalidateViews();
					listView2.invalidateViews();
					listView3.invalidateViews();
					listView4.invalidateViews();
					listView5.invalidateViews();
					listView6.invalidateViews();
				}
				
			}
        });
        
        ImageButton bt6 = (ImageButton)findViewById(R.id.imageButton6);       
        bt6.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(flag2) {
					LinearLayout layout3 = (LinearLayout)findViewById(R.id.layout3);
					layout3.setVisibility(View.GONE);
					flag2 = false;
				}
				else {
					LinearLayout layout3 = (LinearLayout)findViewById(R.id.layout3);
					layout3.setVisibility(View.VISIBLE);
					flag2 = true;
				}
			}
        	
        });
        
        
        ImageButton bt7 = (ImageButton)findViewById(R.id.imageButton7);
        bt7.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				addMR(categories[0]);
			}
        });
        
        ImageButton bt8 = (ImageButton)findViewById(R.id.imageButton8);
        bt8.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				addMR(categories[1]);
			}
        });
        
        ImageButton bt9 = (ImageButton)findViewById(R.id.imageButton9);
        bt9.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				addMR(categories[2]);
			}
        });
        
        ImageButton bt10 = (ImageButton)findViewById(R.id.imageButton10);
        bt10.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				addMR(categories[3]);
			}
        });
        
        ImageButton bt11 = (ImageButton)findViewById(R.id.imageButton11);
        bt11.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				addMR(categories[4]);
			}
        });
        
        ImageButton bt12 = (ImageButton)findViewById(R.id.imageButton12);
        bt12.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				addMR(categories[5]);
			}
        });
        
		if(relationship.equals("connected")) {
			bt3.setVisibility(View.GONE);
			if(doctors.length() > 0) {
				initialisePaging(false, doctors);
			}
			else {
				doctorLayout.setVisibility(View.VISIBLE);
			}
			button1.setVisibility(View.GONE);
			bt5.setVisibility(View.GONE);
			bt7.setVisibility(View.GONE);
			bt8.setVisibility(View.GONE);
			bt9.setVisibility(View.GONE);
			bt10.setVisibility(View.GONE);
			bt11.setVisibility(View.GONE);
			bt12.setVisibility(View.GONE);
		}
		else {
			if(doctors.length() > 0) {
				initialisePaging(true, doctors);
			}
			else {
				doctorLayout.setVisibility(View.VISIBLE);
			}
		}
		
		
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_details, menu);
		return true;
	}
	
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);;
			d.setTitle("Set Birthday");
			return d;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user			
			String selectedMonth, selectedDay;
			month = month + 1;
			if(month < 10) selectedMonth = "0" + month;
			else selectedMonth = Integer.toString(month);
			if(day < 10) selectedDay = "0" + day;
			else selectedDay = Integer.toString(day);
			String date = year + "-" + selectedMonth + "-" + selectedDay;
			edittxt3.setText(date);
			view.setVisibility(View.GONE);
		}
	}
	
	public void show1() {
		final String[] displayedValues = {"A","B","AB","O","Other"};
		final Dialog d = new Dialog(ProfileDetails.this);
        d.setTitle("Set Blood Type");
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
				edittxt4.setText(displayedValues[np.getValue()]);
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
	
	public void show2() {
		final String[] displayedValues = {"Female","Male"};
		final Dialog d = new Dialog(ProfileDetails.this);
        d.setTitle("Set Gender");
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
				edittxt5.setText(displayedValues[np.getValue()]);
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
	
	public void disableEditProfile() {
        edittxt1.setFocusable(false);
        edittxt1.setFocusableInTouchMode(false);
        edittxt1.setClickable(false);
        edittxt1.setBackgroundColor(getResources().getColor(R.color.white));
        
        edittxt2.setFocusable(false);
        edittxt2.setFocusableInTouchMode(false);
        edittxt2.setClickable(false);
        edittxt2.setBackgroundColor(getResources().getColor(R.color.white));
        
        edittxt3.setFocusable(false);
        edittxt3.setFocusableInTouchMode(false);
        edittxt3.setClickable(false);
        edittxt3.setBackgroundColor(getResources().getColor(R.color.white));
        
        edittxt4.setFocusable(false);
        edittxt4.setFocusableInTouchMode(false);
        edittxt4.setClickable(false);
        edittxt4.setBackgroundColor(getResources().getColor(R.color.white));
        
        edittxt5.setFocusable(false);
        edittxt5.setFocusableInTouchMode(false);
        edittxt5.setClickable(false);
        edittxt5.setBackgroundColor(getResources().getColor(R.color.white));
        
        edittxt6.setFocusable(false);
        edittxt6.setFocusableInTouchMode(false);
        edittxt6.setClickable(false);
        edittxt6.setBackgroundColor(getResources().getColor(R.color.white));
        
        edittxt7.setFocusable(false);
        edittxt7.setFocusableInTouchMode(false);
        edittxt7.setClickable(false);
        edittxt7.setBackgroundColor(getResources().getColor(R.color.white));
        
        edittxt8.setFocusable(false);
        edittxt8.setFocusableInTouchMode(false);
        edittxt8.setClickable(false);
        edittxt8.setBackgroundColor(getResources().getColor(R.color.white));		
	}
	
	public void enableEditProfile() {
        edittxt1.setFocusable(true);
        edittxt1.setFocusableInTouchMode(true);
        edittxt1.setClickable(true);
        edittxt1.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        edittxt2.setFocusable(true);
        edittxt2.setFocusableInTouchMode(true);
        edittxt2.setClickable(true);
        edittxt2.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        edittxt3.setClickable(true);
        edittxt3.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        edittxt4.setClickable(true);
        edittxt4.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        edittxt5.setClickable(true);
        edittxt5.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        edittxt6.setFocusable(true);
        edittxt6.setFocusableInTouchMode(true);
        edittxt6.setClickable(true);
        edittxt6.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        edittxt7.setFocusable(true);
        edittxt7.setFocusableInTouchMode(true);
        edittxt7.setClickable(true);
        edittxt7.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        edittxt8.setFocusable(true);
        edittxt8.setFocusableInTouchMode(true);
        edittxt8.setClickable(true);
        edittxt8.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        edittxt3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment();
			    newFragment.show(getFragmentManager(), "datePicker");
			}
		});
        
        
        edittxt4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				show1();
			}
		});
        
        
        edittxt5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				show2();
			}
		});
		
	}
	
	public void saveContents() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				String first_name = edittxt1.getText().toString();
		    	String last_name = edittxt2.getText().toString();
		    	String date_of_birth = edittxt3.getText().toString();
		    	String telephone = edittxt6.getText().toString();
		    	String address = edittxt7.getText().toString();
		    	String nationality = edittxt8.getText().toString();
		    	String blood_type = edittxt4.getText().toString();
		    	String gender = edittxt5.getText().toString();
		    	
		    	HttpPost post = new HttpPost(SignUpAndSignIn.urlHead + "/profile/basic-info");
		    	
//		    	MultipartEntityBuilder entity = MultipartEntityBuilder.create();
//				entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("pid", pid));
		        pairs.add(new BasicNameValuePair("first_name", first_name));
		        pairs.add(new BasicNameValuePair("last_name", last_name));
		        pairs.add(new BasicNameValuePair("date_of_birth", date_of_birth));
		        pairs.add(new BasicNameValuePair("telephone", telephone));
		        pairs.add(new BasicNameValuePair("address", address));
		        pairs.add(new BasicNameValuePair("nationality", nationality));
		        pairs.add(new BasicNameValuePair("blood_type", blood_type));
		        pairs.add(new BasicNameValuePair("gender", gender));
		        UrlEncodedFormEntity ent;
				try {
					ent = new UrlEncodedFormEntity(pairs);
					post.setEntity(ent);
					HttpResponse response = SignUpAndSignIn.client.execute(post);
					HttpEntity resEntity = response.getEntity();
			        String resString = EntityUtils.toString(resEntity);
			        Log.d("UPDATE PROFILE DETAILS STATUS:", resString);
				} catch (Exception e) {
					
				}
			
			
			}
		});
		
        t.start();
        try {
			t.join();
		} catch (InterruptedException e) {
			Log.d("UPDATE PROFILE DETAILS STATUS:", "FAIL!!!!");
		}
	}
	
	public void addMR(String category) {
		Intent intent = new Intent();
		intent.setClass(ProfileDetails.this,
				AddMedicalRecord.class);
		Bundle bundle = new Bundle();
		bundle.putString("pid", pid);
		bundle.putString("category", category);
		bundle.putString("relationship", relationship);
		intent.putExtras(bundle);
		startActivity(intent);
		ProfileDetails.this.finish();
	}
	
	public class MedicalRecord {
		String en;
		String zh;
		
		public MedicalRecord(String en, String zh) {
			this.en = en;
			this.zh = zh;
		}
	}
	
	public class MedicalRecordHolder {
		TextView entxt;
		TextView zhtxt;
		ImageButton imgbt;
		Button bt;
	}
	
	public class MedicalRecordAdapter extends ArrayAdapter<MedicalRecord> {
		Context context; 
	    int layoutResourceId;    
	    ArrayList<MedicalRecord> data;
	    String category;
	    
	    public MedicalRecordAdapter(Context context, int layoutResourceId, ArrayList<MedicalRecord> data, String category) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	        this.category = category;
	    }
	    
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	    	View row = convertView;
	    	MedicalRecordHolder holder = null;
	    	if(row == null) {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new MedicalRecordHolder();
	            holder.entxt = (TextView)row.findViewById(R.id.textView1);
	            holder.zhtxt = (TextView)row.findViewById(R.id.textView2);
	            holder.imgbt = (ImageButton)row.findViewById(R.id.imageButton1);
	            holder.bt = (Button)row.findViewById(R.id.button1);
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (MedicalRecordHolder)row.getTag();
	        }
	    	final Button fbt = holder.bt;
            final ImageButton fimgbt = holder.imgbt;
            if(!editflag3) {
            	holder.imgbt.setVisibility(View.GONE);
            	holder.bt.setVisibility(View.INVISIBLE);
            }
            else {
            	holder.imgbt.setVisibility(View.VISIBLE);
            	holder.bt.setVisibility(View.INVISIBLE);
	            holder.imgbt.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						fbt.setVisibility(View.VISIBLE);
						fimgbt.setVisibility(View.GONE);
						fbt.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(category.equals(categories[0])) {
									setListViewHeightBasedOnChildren(listView1);
									deleteMedicalRecord(category,tIdList1.get(position));
									tIdList1.remove(position);
								}
								else if(category.equals(categories[1])) {
									setListViewHeightBasedOnChildren(listView2);
									deleteMedicalRecord(category,tIdList2.get(position));
									tIdList2.remove(position);
								}
								else if(category.equals(categories[2])) {
									setListViewHeightBasedOnChildren(listView3);
									deleteMedicalRecord(category,tIdList3.get(position));
									tIdList3.remove(position);
								}
								else if(category.equals(categories[3])) {
									setListViewHeightBasedOnChildren(listView4);
									deleteMedicalRecord(category,tIdList4.get(position));
									tIdList4.remove(position);
								}
								else if(category.equals(categories[4])) {
									setListViewHeightBasedOnChildren(listView5);
									deleteMedicalRecord(category,tIdList5.get(position));
									tIdList5.remove(position);
								}
								else if(category.equals(categories[5])) {
									setListViewHeightBasedOnChildren(listView6);
									deleteMedicalRecord(category,tIdList6.get(position));
									tIdList6.remove(position);
								}
								data.remove(position);
								notifyDataSetChanged();
							}
						});
						
					}
				});
	            
	            
            }
	    	
	    	MedicalRecord p = data.get(position);
	        holder.entxt.setText(p.en);
	        holder.zhtxt.setText(p.zh);
	    	
	    	return row;
	    }
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	        return;
	    }
	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        view = listAdapter.getView(i, view, listView);
	        if (i == 0) {
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
	        }
//	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
//	        totalHeight += view.getMeasuredHeight();
	        totalHeight += 60;
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	    listView.requestLayout();
	}
	
	public void deleteMedicalRecord(final String category, final String tid) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					String uri = SignUpAndSignIn.urlHead + "/profile/medinfo/remove";
					HttpPost post = new HttpPost(uri);
					ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("pid", pid));
					pairs.add(new BasicNameValuePair("category", category));
					pairs.add(new BasicNameValuePair("tid", tid));
					UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
							pairs);
					post.setEntity(ent);
				
					HttpResponse response = SignUpAndSignIn.client.execute(post);
					HttpEntity resEntity = response.getEntity();
			        resString = EntityUtils.toString(resEntity);
			        Log.d("DELETE MEDICAL RECORD STATUS:", resString);
				} catch (Exception e) {
					
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			
		}
	}
	
	public class PagerAdapter extends FragmentPagerAdapter {
		 
	    public List<Fragment> fragments;
	    /**
	     * @param fm
	     * @param fragments
	     */
	    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
	        super(fm);
	    	if (fm.getFragments() != null) {
	            fm.getFragments().clear();
	        }
	        this.fragments = fragments;
	    }
	    /* (non-Javadoc)
	     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	     */
	    @Override
	    public Fragment getItem(int position) {
	        return this.fragments.get(position);
	    }
	    
	    public int getItemPosition(Object object) {  
	        // TODO Auto-generated method stub  
	        return PagerAdapter.POSITION_NONE;  
	    }  
	 
	    /* (non-Javadoc)
	     * @see android.support.v4.view.PagerAdapter#getCount()
	     */
	    @Override
	    public int getCount() {
	        return this.fragments.size();
	    }
	}
	
	public void initialisePaging(boolean editable1, JSONArray doctors) {
		 
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        for(int i = 0; i < doctors.length(); i++) {
        	try {
				String id = doctors.getJSONObject(i).getString("id");
	        	String first_name = doctors.getJSONObject(i).getString("first_name");
	        	String last_name = doctors.getJSONObject(i).getString("last_name");
	        	String telephone = doctors.getJSONObject(i).getString("telephone");
	        	String address = doctors.getJSONObject(i).getString("address");
		        fragments.add(ViewDoctor.newInstance(pid, id, first_name, last_name, telephone, address, editable1));
        	} catch (JSONException e) {
				
			}
        }
        
//        fragments.add(Fragment.instantiate(this, Tab1Fragment.class.getName()));
//        fragments.add(Fragment.instantiate(this, Tab2Fragment.class.getName()));
//        fragments.add(Fragment.instantiate(this, Tab3Fragment.class.getName()));
        mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        pager.setAdapter(mPagerAdapter);
    }
	
	public void reloadDocs() {
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				try {
					String uri = SignUpAndSignIn.urlHead + "/profile?pid=" + pid;
					HttpGet httpget = new HttpGet(uri);
				
					HttpResponse response = SignUpAndSignIn.client.execute(httpget);
					HttpEntity resEntity = response.getEntity();
			        resString = EntityUtils.toString(resEntity);
			        Log.d("PROFILE DETAILS STATUS AGAIN:", resString);
				} catch (Exception e) {
					
				}
			}
		});
		t2.start();
		try {
			t2.join();
		} catch (InterruptedException e) {
			
		}
		
		JSONObject jobject;
		
		try {
			jobject = new JSONObject(resString);
			if (jobject.get("status").toString().equals("success")) {
				JSONArray doctors = jobject.getJSONArray("physicians");
				if(doctors.length() > 0) {
					initialisePaging(true, doctors);
				}
			}
		} catch(Exception e) {
			
		}
		
		if(doctors.length() > 0) {
			pager.setVisibility(View.VISIBLE);
			doctorLayout.setVisibility(View.GONE);
		}
		else {
			pager.setVisibility(View.GONE);
			doctorLayout.setVisibility(View.VISIBLE);
		}
	}
}
