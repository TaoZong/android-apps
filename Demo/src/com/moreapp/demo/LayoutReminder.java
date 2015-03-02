package com.moreapp.demo;

import java.util.ArrayList;

import com.moreapp.demo.R;
import com.moreapp.demo.AlarmReceiver.MyAlarm;
import com.moreapp.demo.ProfileDetails.MedicalRecord;
import com.moreapp.demo.ProfileDetails.MedicalRecordHolder;
import com.moreapp.demo.ProfileList.ProfileAdapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class LayoutReminder extends Fragment {
	ListView lv;
	AlarmReceiver alarm;
	Context context2;
	ArrayList<MyAlarm> alarms;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflatedView = inflater.inflate(R.layout.fragment_layout_reminder,
				container, false);
		context2 = this.getActivity().getApplicationContext();
		alarm = new AlarmReceiver();
		alarms = AlarmReceiver.alarmArray;
		lv = (ListView)inflatedView.findViewById(R.id.listView1);
		ImageButton ibt = (ImageButton) inflatedView
				.findViewById(R.id.imageButton1);
		ibt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AddReminder.class);
				startActivity(intent);
			}
		});

		showAlarms();

		return inflatedView;
	}

	public void showAlarms() {
		if(alarms.size() > 0) {
			AlarmAdapter adapter = new AlarmAdapter(getActivity(), R.layout.alarm_list_row, alarms);
			
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							UpdateReminder.class);
					Bundle bundle = new Bundle();
					bundle.putInt("index", position);
					intent.putExtras(bundle);
					startActivity(intent);
				}
	
			});
		}

	}

	public class AlarmHolder {
		TextView time;
		TextView repeat;
		Switch s;
		ImageView image;
	}

	public class AlarmAdapter extends ArrayAdapter<MyAlarm> {
		Context context;
		int layoutResourceId;
		ArrayList<MyAlarm> data;

		public AlarmAdapter(Context context, int layoutResourceId,
				ArrayList<MyAlarm> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			AlarmHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new AlarmHolder();
				holder.time = (TextView) row.findViewById(R.id.textView1);
				holder.repeat = (TextView) row.findViewById(R.id.textView2);
				holder.image = (ImageView) row.findViewById(R.id.imageView1);
				holder.s = (Switch) row.findViewById(R.id.switch1);
				row.setTag(holder);
			} else {
				holder = (AlarmHolder) row.getTag();
			}

			int h = data.get(position).h;
			int m = data.get(position).m;
			String photoName = data.get(position).photoName;
			boolean isActive = data.get(position).isActive;
			String repeat = data.get(position).repeat;
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			holder.image.setImageBitmap(BitmapFactory.decodeFile(photoName,options));
			holder.s.setChecked(isActive);
			String hh = Integer.toString(h);
			String mm = Integer.toString(m);
			if (m < 10)
				mm = "0" + Integer.toString(m);
			holder.time.setText(hh + ":" + mm);
			if(repeat.equals("Never")) holder.repeat.setText("0 h");
			else holder.repeat.setText(repeat.split(" ")[0] + " h");
			holder.s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			        if(isChecked == true) {
			        	alarm.activateAlarm(context2, position);
			        }
			        else {
			        	alarm.inactivateAlarm(context2, position);
			        }
			        
			    }
			});
			return row;
		}
	}
	
	public void onStart() {
		super.onStart();
		showAlarms();
	}
	
	public void onResume() {
		super.onResume();
		showAlarms();
	}

}
