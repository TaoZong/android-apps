package com.moreapp.demo;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	static ArrayList<MyAlarm> alarmArray = new ArrayList<MyAlarm>();
	static int num = 0;;
	@Override
	public void onReceive(Context context, Intent intent) {
		int number = intent.getExtras().getInt("number");
		
		String photoname = "";
		int hh = 0,mm = 0;
		for(MyAlarm m : alarmArray) {
			if(m.n == number) {
				photoname = m.photoName;
				hh = m.h;
				mm = m.m;
			}
		}
        
        Intent newIntent = new Intent(context, AlertAlarmActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        newIntent.putExtra("photoname", photoname);
        newIntent.putExtra("hh", hh);
        newIntent.putExtra("mm", mm);
        context.startActivity(newIntent);
		
//		Toast.makeText(context, "Time is up!!!!.", Toast.LENGTH_LONG).show();
		// Vibrate the mobile phone
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);
	}

	public void addAlarm(Context context, int h, int m, String repeat, 
			String photoName, boolean isActive) {

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra("number", num);
		PendingIntent pi = PendingIntent.getBroadcast(context, num, intent, 0);
		MyAlarm ma = new MyAlarm(pi, num, h, m, repeat, photoName, isActive);
		alarmArray.add(ma);
		num++;
		// set time
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int ap = cal.get(Calendar.AM_PM);
		if(ap > 0) hour = hour + 12;
		
		long time1 = h * 3600000 + m * 60000;
		long time2 = hour * 3600000 + minute * 60000 + second * 1000;
		long time = time1 - time2;
		if(time < 0) time = 3600000 * 24 + time;
		if(isActive) {
			if(repeat.equals("Never")) {
				am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pi);
			}
			else {
				String number = repeat.split(" ")[0];
				int repeatHour = Integer.parseInt(number);
				am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time
						, 3600000 * repeatHour , pi);
			}
		}
	}

	public void inactivateAlarm(Context context, int index) {
		PendingIntent sender = alarmArray.get(index).pi;
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
		alarmArray.get(index).setActive(false);
	}
	
	public void activateAlarm(Context context, int index) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pi = alarmArray.get(index).pi;
		int h = alarmArray.get(index).h;
		int m = alarmArray.get(index).m;
		String repeat = alarmArray.get(index).repeat;
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int ap = cal.get(Calendar.AM_PM);
		if(ap > 0) hour = hour + 12;
		
		long time1 = h * 3600000 + m * 60000;
		long time2 = hour * 3600000 + minute * 60000 + second * 1000;
		long time = time1 - time2;
		if(time < 0) time = 3600000 * 24 + time;
		if(repeat.equals("Never")) {
			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pi);
		}
		else {
			String number = repeat.split(" ")[0];
			int repeatHour = Integer.parseInt(number);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time
					, 3600000 * repeatHour , pi);
		}
		
		alarmArray.get(index).setActive(true);
	}
	
	public void deleteAlarm(Context context, int index) {

		PendingIntent sender = alarmArray.get(index).pi;
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
		alarmArray.remove(index);
	}
	
	public void updateAlarm(Context context, int index, int h, int m, String repeat, 
			String photoName, boolean isActive) {
		PendingIntent sender = alarmArray.get(index).pi;
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.cancel(sender);
		
		
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra("number", num);
		PendingIntent pi = PendingIntent.getBroadcast(context, num, intent, 0);
		MyAlarm ma = new MyAlarm(pi, num, h, m, repeat, photoName, isActive);
		alarmArray.remove(index);
		alarmArray.add(index, ma);
		num++;
		// set time
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int ap = cal.get(Calendar.AM_PM);
		if(ap > 0) hour = hour + 12;
		
		long time1 = h * 3600000 + m * 60000;
		long time2 = hour * 3600000 + minute * 60000 + second * 1000;
		long time = time1 - time2;
		if(time < 0) time = 3600000 * 24 + time;
		if(isActive) {
			if(repeat.equals("Never")) {
				am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pi);
			}
			else {
				String number = repeat.split(" ")[0];
				int repeatHour = Integer.parseInt(number);
				am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time
						, 3600000 * repeatHour , pi);
			}
		}
	}
	
	public class MyAlarm {
		PendingIntent pi;
		int n;
		int h;
		int m;
		String repeat;
		String photoName;
		boolean isActive;
		
		public MyAlarm(PendingIntent pi, int n, int h, int m, String repeat, 
				String photoName, boolean isActive) {
			this.pi = pi;
			this.n = n;
			this.h = h;
			this.m = m;
			this.repeat = repeat;
			this.photoName = photoName;
			this.isActive = isActive;
		}

		
		public void setActive(boolean flag) {
			this.isActive = flag;
		}
	}

}
