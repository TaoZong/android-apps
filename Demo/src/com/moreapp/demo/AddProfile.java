package com.moreapp.demo;

import java.io.File;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;

import com.moreapp.demo.R;
import com.moreapp.demo.ProfileDetails.DatePickerFragment;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class AddProfile extends Activity {
	static TextView edittxt3, edittxt4, edittxt5;
	static EditText edittxt1, edittxt2, edittxt6, edittxt7, edittxt8;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0;
	String PhotoName = "";
	ImageView image;
	Bitmap b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_add_profile);

		ImageButton bt1 = (ImageButton) findViewById(R.id.imageButton1);
		image = (ImageView) findViewById(R.id.imageView1);
		edittxt1 = (EditText) findViewById(R.id.editText1);
		edittxt2 = (EditText) findViewById(R.id.editText2);
		edittxt3 = (TextView) findViewById(R.id.editText3);
		edittxt4 = (TextView) findViewById(R.id.editText4);
		edittxt5 = (TextView) findViewById(R.id.editText5);
		edittxt6 = (EditText) findViewById(R.id.editText6);
		edittxt7 = (EditText) findViewById(R.id.editText7);
		edittxt8 = (EditText) findViewById(R.id.editText8);
		Button bt2 = (Button) findViewById(R.id.button1);

		bt1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddProfile.this, ProfileList.class);
				startActivity(intent);
				AddProfile.this.finish();
			}
		});

		image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				getAlbum.setType(IMAGE_TYPE);
				startActivityForResult(getAlbum, IMAGE_CODE);
			}
		});
		
		edittxt3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment();
			    newFragment.show(getFragmentManager(), "datePicker");
			}
		});
        
        
        edittxt4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				show1();
			}
		});
        
        
        edittxt5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				show2();
			}
		});
        
        bt2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							HttpPost post = new HttpPost(
									SignUpAndSignIn.urlHead + "/profile");
							MultipartEntityBuilder entity = MultipartEntityBuilder.create();
							entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
							if(!PhotoName.isEmpty()) {
								File file = new File(PhotoName);
								entity.addPart("avatar", new FileBody(file));
							}
							entity.addTextBody("first_name", edittxt1.getText().toString(), ContentType.APPLICATION_JSON);
							entity.addTextBody("last_name", edittxt2.getText().toString(), ContentType.APPLICATION_JSON);
							entity.addTextBody("date_of_birth", edittxt3.getText().toString(), ContentType.APPLICATION_JSON);
							entity.addTextBody("telephone", edittxt4.getText().toString(), ContentType.APPLICATION_JSON);
							entity.addTextBody("address", edittxt5.getText().toString(), ContentType.APPLICATION_JSON);
							entity.addTextBody("nationality", edittxt6.getText().toString(), ContentType.APPLICATION_JSON);
							entity.addTextBody("blood_type", edittxt7.getText().toString(), ContentType.APPLICATION_JSON);
							entity.addTextBody("gender", edittxt8.getText().toString(), ContentType.APPLICATION_JSON);
							post.setEntity(entity.build());
							HttpResponse response = SignUpAndSignIn.client.execute(post);
							HttpEntity resEntity = response.getEntity();
					        String resString = EntityUtils.toString(resEntity);
					        Log.d("ADD PROFILE STATUS:", resString);
						} catch(Exception e) {
							
						}
					
					}
				});
				t.start();
	            try {
					t.join();
				} catch (InterruptedException e) {
//					e.printStackTrace();
				}
	            
				Intent intent = new Intent();
				intent.setClass(AddProfile.this,
						ProfileList.class);
				startActivity(intent);
				AddProfile.this.finish();
				
			}
		});

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != -1) {
			return;
		}
		if (requestCode == IMAGE_CODE) {
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				PhotoName = picturePath;
				b = BitmapFactory.decodeFile(picturePath);
				image.setImageBitmap(b);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			DatePickerDialog d = new DatePickerDialog(getActivity(), this,
					year, month, day);
			;
			d.setTitle("Set Birthday");
			return d;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			String selectedMonth, selectedDay;
			month = month + 1;
			if (month < 10)
				selectedMonth = "0" + month;
			else
				selectedMonth = Integer.toString(month);
			if (day < 10)
				selectedDay = "0" + day;
			else
				selectedDay = Integer.toString(day);
			String date = year + "-" + selectedMonth + "-" + selectedDay;
			edittxt3.setText(date);
			view.setVisibility(View.GONE);
		}
	}
	
	public void show1() {
		final String[] displayedValues = {"A","B","AB","O","Other"};
		final Dialog d = new Dialog(AddProfile.this);
        d.setTitle("Set Blood Type");
        d.setContentView(R.layout.dialog_blood_type);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setDisplayedValues(displayedValues);
        np.setMaxValue(displayedValues.length - 1);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				edittxt4.setText(displayedValues[np.getValue()]);
				d.dismiss();
			}
		});
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		d.show();

	}
	
	public void show2() {
		final String[] displayedValues = {"Female","Male"};
		final Dialog d = new Dialog(AddProfile.this);
        d.setTitle("Set Gender");
        d.setContentView(R.layout.dialog_blood_type);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setDisplayedValues(displayedValues);
        np.setMaxValue(displayedValues.length - 1);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				edittxt5.setText(displayedValues[np.getValue()]);
				d.dismiss();
			}
		});
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		d.show();

	}
}
