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
import android.widget.Button;
import android.widget.ImageButton;
import android.os.Build;

// Write A Comment

public class ActivityComment extends Activity {
	ImageButton ibt;
	Button bt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_comment);

		ibt = (ImageButton) findViewById(R.id.imageButton1);
		bt = (Button) findViewById(R.id.button1);

		ibt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityComment.this.finish();
			}
		});

		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityComment.this.finish();
			}
		});
	}

}
