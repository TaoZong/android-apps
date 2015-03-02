package com.moreapp.demo;

import com.moreapp.demo.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.*;

public class Payment extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_payment);
		
		ImageButton ibt = (ImageButton) findViewById(R.id.imageButton1);
		ibt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Payment.this,
						MainActivity.class);
				startActivity(intent);
				Payment.this.finish();
				
			}
		});
		
		ImageView image = (ImageView) findViewById(R.id.imageView2);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout2);
		
		image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendEmail();
				
			}
		});
		layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendEmail();
				
			}
		});
	}
	
	public void sendEmail() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@more-holdings.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "Payment");
		i.putExtra(Intent.EXTRA_TEXT   , "");
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(Payment.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}



}
