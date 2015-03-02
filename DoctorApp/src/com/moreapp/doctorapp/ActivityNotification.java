package com.moreapp.doctorapp;

import java.util.ArrayList;

import com.moreapp.doctorapp.FragmentCurrentCaseList.CurCase;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

// Show Notifications

public class ActivityNotification extends Activity {

	ListView lv;
	ArrayList<Note> notes;
	ImageButton ibt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_notification);
		notes = new ArrayList<Note>();


		lv = (ListView) findViewById(R.id.listView1);
		ibt = (ImageButton) findViewById(R.id.imageButton1);
		
		if(FragmentCurrentCaseList.casedetails.size() > 0) {
			for(int i = 0; i < FragmentCurrentCaseList.casedetails.size(); i++) {
				CaseDetailObj cd = FragmentCurrentCaseList.casedetails.get(i);
				String id = cd.case_id;
				String name = cd.case_name;
				int number = cd.notifications.size();
				if(number > 0) {
					Note n = new Note(id, name, number, i);
					notes.add(n);
				}
			}
		}

		NoteAdapter adapter = new NoteAdapter(this, notes);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(ActivityNotification.this, ActivityCurrentCase.class);
				int p = notes.get(position).position;
				Bundle bundle = new Bundle();
				bundle.putInt("id", p);
				bundle.putString("from", "notification");
				intent.putExtras(bundle);
				startActivity(intent);
				ActivityNotification.this.finish();
			}

		});

		ibt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityNotification.this, MainActivity.class);
				startActivity(intent);
				ActivityNotification.this.finish();
			}
		});

	}

	public class Note {
		String id;
		String name;
		int number;
		int position;

		public Note(String id, String name, int number, int position) {
			this.id = id;
			this.name = name;
			this.number = number;
			this.position = position;
		}
	}

	public class NoteAdapter extends ArrayAdapter<Note> {
		Activity context;
		ArrayList<Note> data;

		public NoteAdapter(Activity context, ArrayList<Note> data) {
			super(context, R.layout.notification_list_row, data);
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View row = inflater.inflate(R.layout.notification_list_row, null,
					true);
			TextView titleTxt = (TextView) row.findViewById(R.id.title);
			Note n = data.get(position);
			titleTxt.setText(n.name + " has " + Integer.toString(n.number) + " notification(s)");

			return row;
		}
	}

}
