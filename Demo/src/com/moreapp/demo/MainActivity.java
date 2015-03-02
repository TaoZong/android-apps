package com.moreapp.demo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.moreapp.demo.R;
import com.moreapp.demo.MySlipSwitch.OnSwitchListener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	public static Context c;
	public MainLayout mLayout;
	private ListView lvMenu;
	private final String[] lvMenuItems = new String[] { "Home", "Profile",
			"Payment", "Setting" };
	private ListView lvMenu2;
	private final String[] lvMenuItems2 = new String[] { "Terms and Privacy",
			"Log Out" };
	ImageButton btMenu;
	ImageView btEmergency;

	// TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = this;
		// no title
		requestWindowFeature(1);

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

		btMenu = (ImageButton) findViewById(R.id.ImageButton1);
		btMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Show/hide the menu
				toggleMenu(v);
			}
		});

		btEmergency = (ImageView) findViewById(R.id.ImageView1);
		btEmergency.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showEmergencyDialog();
			}
		});

		// tvTitle = (TextView) findViewById(R.id.activity_main_content_title);

		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Layout1 fragment = new Layout1();
		ft.add(R.id.activity_main_content_fragment, fragment);
		ft.commit();

		LinearLayout button1 = (LinearLayout) findViewById(R.id.LayoutButton1);
		// LinearLayout button2 = (LinearLayout)
		// findViewById(R.id.LayoutButton2);
		LinearLayout button3 = (LinearLayout) findViewById(R.id.LayoutButton3);
		LinearLayout button4 = (LinearLayout) findViewById(R.id.LayoutButton4);
		LinearLayout button5 = (LinearLayout) findViewById(R.id.LayoutButton5);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.RelativeLayout2);
//		layout.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				toggleMenu(v);
//			}
//		});

		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView image1 = (ImageView) findViewById(R.id.imageView2);
				image1.setImageResource(R.drawable.home_active);
				// ImageView image2 = (ImageView) findViewById(R.id.imageView3);
				// image2.setImageResource(R.drawable.note);
				ImageView image3 = (ImageView) findViewById(R.id.imageView4);
				image3.setImageResource(R.drawable.map);
				ImageView image4 = (ImageView) findViewById(R.id.imageView5);
				image4.setImageResource(R.drawable.circle);
				ImageView image5 = (ImageView) findViewById(R.id.imageView6);
				image5.setImageResource(R.drawable.reminder);

				FragmentManager fm = MainActivity.this
						.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Layout1 fragment = new Layout1();
				ft.replace(R.id.activity_main_content_fragment, fragment);
				ft.commit();
			}
		});

		// button2.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// ImageView image1 = (ImageView) findViewById(R.id.imageView2);
		// image1.setImageResource(R.drawable.home);
		// ImageView image2 = (ImageView) findViewById(R.id.imageView3);
		// image2.setImageResource(R.drawable.note_active);
		// ImageView image3 = (ImageView) findViewById(R.id.imageView4);
		// image3.setImageResource(R.drawable.map);
		// ImageView image4 = (ImageView) findViewById(R.id.imageView5);
		// image4.setImageResource(R.drawable.circle);
		// ImageView image5 = (ImageView) findViewById(R.id.imageView6);
		// image5.setImageResource(R.drawable.reminder);
		//
		// }
		// });

		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView image1 = (ImageView) findViewById(R.id.imageView2);
				image1.setImageResource(R.drawable.home);
				// ImageView image2 = (ImageView) findViewById(R.id.imageView3);
				// image2.setImageResource(R.drawable.note);
				ImageView image3 = (ImageView) findViewById(R.id.imageView4);
				image3.setImageResource(R.drawable.map_active);
				ImageView image4 = (ImageView) findViewById(R.id.imageView5);
				image4.setImageResource(R.drawable.circle);
				ImageView image5 = (ImageView) findViewById(R.id.imageView6);
				image5.setImageResource(R.drawable.reminder);

				FragmentManager fm = MainActivity.this
						.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				LayoutMap fragment = new LayoutMap();
				ft.replace(R.id.activity_main_content_fragment, fragment);
				ft.commit();
			}
		});

		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView image1 = (ImageView) findViewById(R.id.imageView2);
				image1.setImageResource(R.drawable.home);
				// ImageView image2 = (ImageView) findViewById(R.id.imageView3);
				// image2.setImageResource(R.drawable.note);
				ImageView image3 = (ImageView) findViewById(R.id.imageView4);
				image3.setImageResource(R.drawable.map);
				ImageView image4 = (ImageView) findViewById(R.id.imageView5);
				image4.setImageResource(R.drawable.circle_active);
				ImageView image5 = (ImageView) findViewById(R.id.imageView6);
				image5.setImageResource(R.drawable.reminder);

				FragmentManager fm = MainActivity.this
						.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				LayoutCircle fragment = new LayoutCircle();
				ft.replace(R.id.activity_main_content_fragment, fragment);
				ft.commit();

			}
		});

		button5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView image1 = (ImageView) findViewById(R.id.imageView2);
				image1.setImageResource(R.drawable.home);
				// ImageView image2 = (ImageView) findViewById(R.id.imageView3);
				// image2.setImageResource(R.drawable.note);
				ImageView image3 = (ImageView) findViewById(R.id.imageView4);
				image3.setImageResource(R.drawable.map);
				ImageView image4 = (ImageView) findViewById(R.id.imageView5);
				image4.setImageResource(R.drawable.circle);
				ImageView image5 = (ImageView) findViewById(R.id.imageView6);
				image5.setImageResource(R.drawable.reminder_active);

				FragmentManager fm = MainActivity.this
						.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				LayoutReminder fragment = new LayoutReminder();
				ft.replace(R.id.activity_main_content_fragment, fragment);
				ft.commit();
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void toggleMenu(View v) {
		mLayout.toggleMenu();
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

			if (s.equals("Home")) {
				imageView.setImageResource(R.drawable.terms);
			} else if (s.equals("Payment")) {
				imageView.setImageResource(R.drawable.payment);
			} else if (s.equals("Help Center")) {
				imageView.setImageResource(R.drawable.help);
			} else if (s.equals("Setting")) {
				imageView.setImageResource(R.drawable.setting);
			} else {
				imageView.setImageResource(R.drawable.profile);
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

			// Change icon based on name
			String s = values[position];

			if (s.equals("Terms and Privacy")) {
				imageView.setImageResource(R.drawable.terms);
			} else {
				imageView.setImageResource(R.drawable.logout);
			}

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

		if (selectedItem.compareTo("Profile") == 0) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ProfileList.class);
			startActivity(intent);
			MainActivity.this.finish();
		} else if (selectedItem.compareTo("Home") == 0) {
			ImageView image1 = (ImageView) findViewById(R.id.imageView2);
			image1.setImageResource(R.drawable.home_active);
			ImageView image3 = (ImageView) findViewById(R.id.imageView4);
			image3.setImageResource(R.drawable.map);
			ImageView image4 = (ImageView) findViewById(R.id.imageView5);
			image4.setImageResource(R.drawable.circle);
			ImageView image5 = (ImageView) findViewById(R.id.imageView6);
			image5.setImageResource(R.drawable.reminder);
			fragment = new Layout1();
		} else if (selectedItem.compareTo("Setting") == 0) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, Settings.class);
			startActivity(intent);
			MainActivity.this.finish();
		} else if (selectedItem.compareTo("Payment") == 0) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, Payment.class);
			startActivity(intent);
			MainActivity.this.finish();
		}

		if (fragment != null) {
			ft.replace(R.id.activity_main_content_fragment, fragment);
			ft.commit();
			// tvTitle.setText(selectedItem);
		}
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

		if (selectedItem.compareTo("Terms and Privacy") == 0) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, TermsAndPrivacy.class);
			startActivity(intent);
			MainActivity.this.finish();
		}

		if (selectedItem.compareTo("Log Out") == 0) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						HttpClient client = SignUpAndSignIn.client;
						HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
								+ "/user/sign-out");

						// Execute HTTP Post Request
						HttpResponse response = client.execute(post);

						HttpEntity resEntity = response.getEntity();
						String resString = EntityUtils.toString(resEntity);
						try {
							JSONObject jobject = new JSONObject(resString);
							Log.d("SIGN OUT STATUS:", jobject.get("status")
									.toString());
							if (jobject.length() == 1
									&& jobject.get("status").toString()
											.equals("success")) {

								// Tell MainActivity to clear shared preference
								Intent intent = new Intent();
								intent.setClass(MainActivity.this,
										SignUpAndSignIn.class);
								Bundle bundle = new Bundle();
								bundle.putInt("Clear", 1);
								intent.putExtras(bundle);
								startActivity(intent);
								MainActivity.this.finish();
							}
						} catch (Exception e) {
							// e.printStackTrace();
						}
					} catch (Exception e) {
						// e.printStackTrace();
					}

				}

			});
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}

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

	public void showEmergencyDialog() {
		final Dialog d = new Dialog(MainActivity.this);
		d.setTitle("Emergency Alert");
		d.setContentView(R.layout.dialog_emergency);
		MySlipSwitch slidingButton = (MySlipSwitch) d
				.findViewById(R.id.slideButton);
		// slidingButton.setImageResource(R.drawable.emergency_icon_reference_bg,
		// R.drawable.emergency_icon_reference_bg,
		// R.drawable.emergency_icon_reference);
		if (SaveSharedPreference.isEmergencyOn)
			slidingButton.setSwitchState(true);
		else
			slidingButton.setSwitchState(false);
		Button b1 = (Button) d.findViewById(R.id.button1);
		Button b2 = (Button) d.findViewById(R.id.button2);

		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		slidingButton.setOnSwitchListener(new OnSwitchListener() {

			@Override
			public void onSwitched(boolean isSwitchOn) {
				if (isSwitchOn) {
					btEmergency.setImageResource(R.layout.emergency_active);
					final AnimationDrawable myAnimationDrawable = (AnimationDrawable) btEmergency
							.getDrawable();
					btEmergency.post(new Runnable() {

						@Override
						public void run() {
							myAnimationDrawable.start();
						}
					});

					SaveSharedPreference.isEmergencyOn = true;
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {

								HttpPost post = new HttpPost(
										SignUpAndSignIn.urlHead
												+ "/property/emergency");

								MultipartEntityBuilder entity = MultipartEntityBuilder
										.create();
								entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
								entity.addTextBody("mode", "ON",
										ContentType.APPLICATION_JSON);
								post.setEntity(entity.build());

								HttpResponse response = SignUpAndSignIn.client
										.execute(post);

								// Get response
								HttpEntity resEntity = response.getEntity();
								String resString = EntityUtils
										.toString(resEntity);

								Log.d("SET EMERGENCY STATUS:", resString);
							} catch (Exception e) {
								// e.printStackTrace();
							}

						}

					});
					t.start();
					try {
						t.join();
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
				} else {
					btEmergency.setImageResource(R.drawable.emergency);
					SaveSharedPreference.isEmergencyOn = false;
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {

								HttpPost post = new HttpPost(
										SignUpAndSignIn.urlHead
												+ "/property/emergency");

								MultipartEntityBuilder entity = MultipartEntityBuilder
										.create();
								entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
								entity.addTextBody("mode", "OFF",
										ContentType.APPLICATION_JSON);
								post.setEntity(entity.build());

								HttpResponse response = SignUpAndSignIn.client
										.execute(post);

								// Get response
								HttpEntity resEntity = response.getEntity();
								String resString = EntityUtils
										.toString(resEntity);

								Log.d("SET EMERGENCY STATUS:", resString);
							} catch (Exception e) {
								// e.printStackTrace();
							}

						}

					});
					t.start();
					try {
						t.join();
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
				}

			}

		});
		d.show();
	}
}
