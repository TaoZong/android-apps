package com.moreapp.demo;

import com.moreapp.demo.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabWidget;
import android.widget.TextView;

import android.os.Build;

public class TermsAndPrivacy extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(1);
		setContentView(R.layout.activity_terms_and_privacy);

		ImageButton ibt = (ImageButton) findViewById(R.id.imageButton1);
		ibt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TermsAndPrivacy.this, MainActivity.class);
				startActivity(intent);
				TermsAndPrivacy.this.finish();
			}
		});

		FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabhost);

		tabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);
		tabHost.addTab(
				tabHost.newTabSpec("fragmentA").setIndicator(
						"Privacy Agreement"), Privacy.class, null);
		tabHost.addTab(
				tabHost.newTabSpec("fragmentB").setIndicator("Term of Use"),
				Terms.class, null);
		
		View tabView = tabHost.getTabWidget().getChildTabViewAt(0);
		TextView lf = (TextView) tabView.findViewById(android.R.id.title);
		Log.d("tab title:", lf.getText().toString());
		lf.setTextSize(8);
		
		View tabView2 = tabHost.getTabWidget().getChildTabViewAt(1);
		TextView lf2 = (TextView) tabView2.findViewById(android.R.id.title);
		Log.d("tab title:", lf2.getText().toString());
		lf2.setTextSize(8);

	}

}
