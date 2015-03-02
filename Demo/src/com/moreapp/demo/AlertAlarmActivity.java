package com.moreapp.demo;

import com.moreapp.demo.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class AlertAlarmActivity extends Activity {
	String photoname = "";
	int hh = 0,mm = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert_alarm);
		Bundle b = this.getIntent().getExtras();
        photoname = b.getString("photoname");
		hh = b.getInt("hh");
		mm = b.getInt("mm");
        ImageView image = (ImageView) findViewById(R.id.imageView1);
        TextView txt = (TextView) findViewById(R.id.textView1);
        Button bt = (Button) findViewById(R.id.button1);
        if(photoname.length() > 0)
        	image.setImageBitmap(BitmapFactory.decodeFile(photoname));
        if(mm < 10) txt.setText(hh+":0"+mm);
        else txt.setText(hh+":"+mm);
//        d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertAlarmActivity.this.finish();				
			}
		});
		
	}
}
