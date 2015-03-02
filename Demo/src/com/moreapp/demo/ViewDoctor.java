package com.moreapp.demo;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.moreapp.demo.R;
import com.moreapp.demo.ProfileDetails.PagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class ViewDoctor extends Fragment {
	EditText txt1;
	EditText txt2;
	EditText txt3;
	EditText txt4;
	ImageButton bt, bt2;
	boolean flag = false;
	String pid, id;
	String resString;
	
	
	
	public static final ViewDoctor newInstance(String pid, String id, String first_name, 
			String last_name, String telephone, String address, boolean editable) {
		ViewDoctor vd = new ViewDoctor();
		Bundle bundle = new Bundle();
		bundle.putString("pid", pid);
        bundle.putString("id", id);
        bundle.putString("first_name", first_name);
        bundle.putString("last_name", last_name);
        bundle.putString("telephone", telephone);
        bundle.putString("address", address);
        bundle.putBoolean("editable", editable);
        vd.setArguments(bundle);
		return vd;
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_doctor, container, false);
		pid = getArguments().getString("pid"); 
		id = getArguments().getString("id");
		String first_name = getArguments().getString("first_name"); 
		String last_name = getArguments().getString("last_name"); 
		String telephone = getArguments().getString("telephone"); 
		String address = getArguments().getString("address");
		boolean editable = getArguments().getBoolean("editable");
		
		
		txt1 = (EditText)view.findViewById(R.id.editText9);
		txt2 = (EditText)view.findViewById(R.id.editText10);
		txt3 = (EditText)view.findViewById(R.id.editText11);
		txt4 = (EditText)view.findViewById(R.id.editText12);
		bt = (ImageButton)view.findViewById(R.id.imageButton4);
		bt2 = (ImageButton)view.findViewById(R.id.imageButton1);
		
		txt1.setText(first_name);
		txt2.setText(last_name);
		txt3.setText(telephone);
		txt4.setText(address);
		
		disableEdit();
		if(editable) {
			disableEdit();
		}
		else {
			bt.setVisibility(View.GONE);
		}
		
		bt.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(flag) {
					flag = false;
					disableEdit();
					saveEdit();
					bt.setImageResource(R.drawable.edit);
					
				}
				else {
					flag = true;
					enableEdit();
					bt.setImageResource(R.drawable.save);
				}
			}
		});
		
		bt2.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				removeDoc();
			}
		});
		
		return view;
	}
	
	public void disableEdit() {
        txt1.setFocusable(false);
        txt1.setFocusableInTouchMode(false);
        txt1.setClickable(false);
        txt1.setBackgroundColor(getResources().getColor(R.color.white));
        
        txt2.setFocusable(false);
        txt2.setFocusableInTouchMode(false);
        txt2.setClickable(false);
        txt2.setBackgroundColor(getResources().getColor(R.color.white));
        
        txt3.setFocusable(false);
        txt3.setFocusableInTouchMode(false);
        txt3.setClickable(false);
        txt3.setBackgroundColor(getResources().getColor(R.color.white));
        
        txt4.setFocusable(false);
        txt4.setFocusableInTouchMode(false);
        txt4.setClickable(false);
        txt4.setBackgroundColor(getResources().getColor(R.color.white));
        
        bt2.setVisibility(View.GONE);
	}
	
	public void enableEdit() {		
        txt1.setFocusable(true);
        txt1.setFocusableInTouchMode(true);
        txt1.setClickable(true);
        txt1.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        txt2.setFocusable(true);
        txt2.setFocusableInTouchMode(true);
        txt2.setClickable(true);
        txt2.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        txt3.setFocusable(true);
        txt3.setFocusableInTouchMode(true);
        txt3.setClickable(true);
        txt3.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        txt4.setFocusable(true);
        txt4.setFocusableInTouchMode(true);
        txt4.setClickable(true);
        txt4.setBackgroundColor(getResources().getColor(R.color.light_grey));
        
        bt2.setVisibility(View.VISIBLE);
		
	}
	
	public void saveEdit() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpPost post = new HttpPost(SignUpAndSignIn.urlHead + "/profile/physician/edit");
				ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
				String first_name = txt1.getText().toString();
				String last_name = txt2.getText().toString();
				String telephone = txt3.getText().toString();
				String address = txt4.getText().toString();
				pairs.add(new BasicNameValuePair("phid", id));
		        pairs.add(new BasicNameValuePair("first_name", first_name));
		        pairs.add(new BasicNameValuePair("last_name", last_name));
		        pairs.add(new BasicNameValuePair("telephone", telephone));
		        pairs.add(new BasicNameValuePair("address", address));
		        
		        
		        UrlEncodedFormEntity ent;
				try {
					ent = new UrlEncodedFormEntity(pairs);
					post.setEntity(ent);
					HttpResponse response = SignUpAndSignIn.client.execute(post);
					HttpEntity resEntity = response.getEntity();
			        String resString = EntityUtils.toString(resEntity);
			        Log.d("UPDATE DOCTOR INFO STATUS:", resString);
				} catch (Exception e) {
					
				}
			}
		});
		
		t.start();
        try {
			t.join();
		} catch (InterruptedException e) {
			Log.d("UPDATE DOCTOR INFO STATUS:", "FAIL!!!!");
		}
	}
	
	public void removeDoc() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpPost post = new HttpPost(SignUpAndSignIn.urlHead + "/profile/physician/remove");
				ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("phid", id));
		        pairs.add(new BasicNameValuePair("pid", pid));
		        
		        
		        UrlEncodedFormEntity ent;
				try {
					ent = new UrlEncodedFormEntity(pairs);
					post.setEntity(ent);
					HttpResponse response = SignUpAndSignIn.client.execute(post);
					HttpEntity resEntity = response.getEntity();
			        resString = EntityUtils.toString(resEntity);
			        Log.d("REMOVE DOCTOR STATUS:", resString);
			        Log.d("REMOVE DOCTOR:", id);
				} catch (Exception e) {
					
				}
			}
		});
		
		t.start();
        try {
			t.join();
		} catch (InterruptedException e) {
			Log.d("REMOVE DOCTOR STATUS:", "FAIL!!!!");
		}
        
        JSONObject jobject;
		
		try {
			jobject = new JSONObject(resString);
			if (jobject.get("status").toString().equals("success")) {
//				int cur = ProfileDetails.pager.getCurrentItem();
//				ProfileDetails.mPagerAdapter.fragments.remove(cur);
//				ProfileDetails.mPagerAdapter.notifyDataSetChanged();
//				ProfileDetails.pager.setAdapter(ProfileDetails.mPagerAdapter);
//				Log.d("DOCTOR NO.:", Integer.toString(cur));
//				Intent intent = getActivity().getIntent();
//				getActivity().finish();
//				startActivity(intent);
				((ProfileDetails)getActivity()).reloadDocs();
			}
		} catch(Exception e) {
			
		}
        
        
	}
}
