package com.moreapp.doctorapp;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Build;

public class ActivityIncomingCase extends Activity {
	RelativeLayout layout1, layout2, layout3, layout4, layout5;
	TextView txt;
	ImageButton ibt;
	Button bt1, bt2, bt3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_incoming_case);

		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		layout2 = (RelativeLayout) findViewById(R.id.layout2);
		layout3 = (RelativeLayout) findViewById(R.id.relativeLayout4);
		layout4 = (RelativeLayout) findViewById(R.id.relativeLayout5);
		txt = (TextView) findViewById(R.id.textView6);
		ibt = (ImageButton) findViewById(R.id.ImageButton1);
		bt1 = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);
		bt3 = (Button) findViewById(R.id.button3);

		layout2.setVisibility(View.GONE);
		bt1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layout2.setVisibility(View.VISIBLE);
				layout3.setVisibility(View.VISIBLE);
				layout4.setVisibility(View.GONE);
				txt.setVisibility(View.GONE);
			}
		});

		bt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layout2.setVisibility(View.GONE);
			}
		});

		bt3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layout3.setVisibility(View.GONE);
				layout4.setVisibility(View.VISIBLE);
				txt.setVisibility(View.VISIBLE);
			}
		});

		layout2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (layout4.isShown()) {
					Intent intent = new Intent();
					intent.setClass(ActivityIncomingCase.this, MainActivity.class);
					startActivity(intent);
					ActivityIncomingCase.this.finish();
				}
			}
		});

		ibt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityIncomingCase.this, MainActivity.class);
				startActivity(intent);
				ActivityIncomingCase.this.finish();
			}
		});

		Button bt4 = (Button) findViewById(R.id.button4);
		bt4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityIncomingCase.this, PDFView.class);
				startActivity(intent);
				ActivityIncomingCase.this.finish();
			}
		});
	}

}
