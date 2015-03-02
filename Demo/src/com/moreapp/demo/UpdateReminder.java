package com.moreapp.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.moreapp.demo.R;
import com.moreapp.demo.AlarmReceiver.MyAlarm;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

public class UpdateReminder extends Activity {
	private AlarmReceiver alarm;
	Context context;
	TextView txt4;
	int i;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0, IMAGE_CODE2 = 1;
	ImageView image;
	String photoName;
	public static int count = 0;
	TimePicker timepicker;
	Switch s;
	boolean flag;
	int pos;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_add_reminder);
		Bundle b = this.getIntent().getExtras();
		pos = b.getInt("index");
		context = this.getApplicationContext();
		alarm = new AlarmReceiver();
		txt4 = (TextView) findViewById(R.id.textView4);
		photoName = "";
		flag = false;
		timepicker = (TimePicker) findViewById(R.id.timePicker1);
		image = (ImageView) findViewById(R.id.imageView1);
		s = (Switch) findViewById(R.id.switch1);
		
		ArrayList<MyAlarm> array = AlarmReceiver.alarmArray;
		MyAlarm ma = array.get(pos);
		int h = ma.h;
		int m = ma.m;
		String repeat = ma.repeat;
		String photoName2 = ma.photoName;
		
		timepicker.setCurrentHour(h);
		timepicker.setCurrentMinute(m);
		txt4.setText(repeat);
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		image.setImageBitmap(BitmapFactory.decodeFile(photoName2, options));
		
		RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.relativeLayout2);
		layout1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final ArrayList<String> displayedValues = new ArrayList<String>(
						Arrays.asList("Never", "1 hour", "2 hours", "3 hours",
								"4 hours", "5 hours", "6 hours", "7 hours",
								"8 hours", "9 hours", "10 hours", "11 hours",
								"12 hours", "13 hours", "14 hours", "15 hours",
								"16 hours", "17 hours", "18 hours", "19 hours",
								"20 hours", "21 hours", "22 hours", "23 hours",
								"24 hours"));
				final Dialog d = new Dialog(UpdateReminder.this);
				String repeat = txt4.getText().toString();
				i = displayedValues.indexOf(repeat);
				d.setTitle("Alarm Repeat Every:");
				d.setContentView(R.layout.dialog_reminder_repeat);
				final TextView txt = (TextView) d.findViewById(R.id.textView1);
				final Button bt1 = (Button) d.findViewById(R.id.button1);
				final Button bt2 = (Button) d.findViewById(R.id.button2);
				Button bt3 = (Button) d.findViewById(R.id.button3);
				Button bt4 = (Button) d.findViewById(R.id.button4);

				txt.setText(displayedValues.get(i));
				if (i == 0) {
					bt1.setVisibility(View.GONE);
					bt2.setVisibility(View.VISIBLE);
				} else if (i == 24) {
					bt2.setVisibility(View.GONE);
					bt1.setVisibility(View.VISIBLE);
				} else {
					bt1.setVisibility(View.VISIBLE);
					bt2.setVisibility(View.VISIBLE);
				}
				bt1.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (i == 1) {
							bt1.setVisibility(View.GONE);
							bt2.setVisibility(View.VISIBLE);
						} else {
							bt1.setVisibility(View.VISIBLE);
							bt2.setVisibility(View.VISIBLE);
						}
						txt.setText(displayedValues.get(--i));
					}
				});

				bt2.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (i == 23) {
							bt2.setVisibility(View.GONE);
							bt1.setVisibility(View.VISIBLE);
						} else {
							bt1.setVisibility(View.VISIBLE);
							bt2.setVisibility(View.VISIBLE);
						}
						txt.setText(displayedValues.get(++i));
					}
				});

				bt3.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						txt4.setText(txt.getText());
						d.dismiss();
					}
				});

				bt4.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						d.dismiss();
					}
				});

				d.show();

			}
		});

		
		RelativeLayout layout2 = (RelativeLayout) findViewById(R.id.relativeLayout3);
		layout2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog d = new Dialog(UpdateReminder.this);
				d.setTitle("Add Image");
				d.setContentView(R.layout.dialog_reminder_picture);

				Button bt1 = (Button) d.findViewById(R.id.button1);
				Button bt2 = (Button) d.findViewById(R.id.button2);
				Button bt3 = (Button) d.findViewById(R.id.button3);

				bt1.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
						getAlbum.setType(IMAGE_TYPE);
						startActivityForResult(getAlbum, IMAGE_CODE);
						d.dismiss();
					}
				});

				final String dir = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
						+ "/picFolder/";
				File newdir = new File(dir);
				newdir.mkdirs();
				bt2.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						count++;
						String file = dir + count + ".jpg";
						File newfile = new File(file);
						try {
							newfile.createNewFile();
						} catch (IOException e) {
						}

						Uri outputFileUri = Uri.fromFile(newfile);
						Intent cameraIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								outputFileUri);

						startActivityForResult(cameraIntent, IMAGE_CODE2);
						d.dismiss();

					}
				});
				
				bt3.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						d.dismiss();
					}
				});

				d.show();
			}
		});
		
		ImageButton ibt = (ImageButton) findViewById(R.id.imageButton1);
		ibt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UpdateReminder.this.finish();
			}
		});
		
		Button bt1 = (Button) findViewById(R.id.button1);
		bt1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alarm.deleteAlarm(context, pos);
				UpdateReminder.this.finish();
			}
		});
		
		
		s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if(isChecked == true) {
		        	flag = true;
		        }
		        else flag = false;
		    }
		});
		
		Button bt2 = (Button) findViewById(R.id.button2);
		bt2.setText("Update Alarm");
		bt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int h = timepicker.getCurrentHour();
				int m = timepicker.getCurrentMinute();
				alarm.updateAlarm(context, pos, h, m, txt4.getText().toString(), photoName, flag);				
				UpdateReminder.this.finish();
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
				photoName = picturePath;
				cursor.close();
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				image.setImageBitmap(BitmapFactory.decodeFile(picturePath, options));
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		
		if(requestCode == IMAGE_CODE2) {
			final String dir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
					+ "/picFolder/";
			String picturePath = dir + count + ".jpg";
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			image.setImageBitmap(BitmapFactory.decodeFile(picturePath, options));
			photoName = picturePath;
		}
	}



}
