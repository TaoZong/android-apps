package com.moreapp.doctorapp;

import com.moreapp.doctorapp.MainLayout;
import com.moreapp.doctorapp.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class MainActivity extends FragmentActivity {

	public MainLayout mLayout;
	private ListView lvMenu;

	private final String[] lvMenuItems = new String[] { "Archived Cases",
			"Privacy & Terms", "Settings" };
	private ListView lvMenu2;
	private final String[] lvMenuItems2 = new String[] { "Log Out" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_main);

		mLayout = (MainLayout) this.getLayoutInflater().inflate(
				R.layout.activity_main, null);
		setContentView(mLayout);

		// lvMenuItems = getResources().getStringArray(R.array.menu_items);
		lvMenu = (ListView) findViewById(R.id.menu_listview);
		lvMenu.setAdapter(new MenuAdapter(this, lvMenuItems));
		// lvMenu.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, lvMenuItems));
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMenuItemClick(parent, view, position, id);
			}

		});

		// lvMenuItems2 = getResources().getStringArray(R.array.menu_items2);
		lvMenu2 = (ListView) findViewById(R.id.menu_listview2);
		lvMenu2.setAdapter(new MenuAdapter2(this, lvMenuItems2));
		// lvMenu2.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, lvMenuItems2));
		lvMenu2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMenuItemClick2(parent, view, position, id);
			}

		});

		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		TabLayout fragment = new TabLayout();
		ft.add(R.id.activity_main_content_fragment, fragment);
		ft.commit();

	}

	public void toggleMenu(View v) {
		mLayout.toggleMenu();
	}

	@Override
	public void onBackPressed() {
		if (mLayout.isMenuShown()) {
			mLayout.toggleMenu();
		} else {
			super.onBackPressed();
		}
	}

	public class MenuAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;

		public MenuAdapter(Context context, String[] values) {
			super(context, R.layout.menu_list_row, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.menu_list_row, parent,
					false);
			TextView textView = (TextView) rowView.findViewById(R.id.label);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
			textView.setText(values[position]);

			// Change icon based on name
			String s = values[position];

			if (s.equals("Archived Cases")) {
				imageView.setImageResource(R.drawable.archive);
			} else if (s.equals("Privacy & Terms")) {
				imageView.setImageResource(R.drawable.privacy);
			} else if (s.equals("Settings")) {
				imageView.setImageResource(R.drawable.setting);
			}

			return rowView;
		}
	}

	public class MenuAdapter2 extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;

		public MenuAdapter2(Context context, String[] values) {
			super(context, R.layout.menu_list_row, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.menu_list_row, parent,
					false);
			TextView textView = (TextView) rowView.findViewById(R.id.label);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
			textView.setText(values[position]);
			imageView.setImageResource(R.drawable.logout);
			// Change icon based on name
			String s = values[position];

			// if (s.equals("Terms and Privacy")) {
			// imageView.setImageResource(R.drawable.terms);
			// } else {
			// imageView.setImageResource(R.drawable.logout);
			// }

			return rowView;
		}
	}

	private void onMenuItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		String selectedItem = lvMenuItems[position];
		// String currentItem = tvTitle.getText().toString();
		// if (selectedItem.compareTo(currentItem) == 0) {
		// mLayout.toggleMenu();
		// return;
		// }

		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		mLayout.toggleMenu();
	}

	private void onMenuItemClick2(AdapterView<?> parent, View view,
			int position, long id) {
		String selectedItem = lvMenuItems2[position];
		// String currentItem = tvTitle.getText().toString();
		// if (selectedItem.compareTo(currentItem) == 0) {
		// mLayout.toggleMenu();
		// return;
		// }

		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		mLayout.toggleMenu();
	}

	public void viewNotifications(View v) {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ActivityNotification.class);
		startActivity(intent);
		MainActivity.this.finish();
	}

}
