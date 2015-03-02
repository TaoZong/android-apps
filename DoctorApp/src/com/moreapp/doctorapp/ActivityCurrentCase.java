package com.moreapp.doctorapp;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Build;

public class ActivityCurrentCase extends Activity {
	RelativeLayout layout1, layout2, layout3, layout4, layout5, layout6;
	TextView txt;
	ImageButton ibt;
	Button bt1, bt2, bt3;
	ArrayList<Integer> heightOfComments;
	ArrayList<CommentObj> comments;
	String case_id = "", uName = "", bDate = "", gender = "", nationality = "",
			diagnosis = "", situation = "";
	String fromWhere = "";

	// String url = "http://www.morehealthmd.com/api/caseinfo/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		// setContentView(R.layout.test);
		setContentView(R.layout.activity_current_case);
		CaseDetailObj cd = FragmentCurrentCaseList.casedetails.get(this.getIntent().getExtras()
				.getInt("position"));
		fromWhere = this.getIntent().getExtras().getString("from");
		// case_id = b.getString("case_id");
		// url = url + case_id;
		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		layout2 = (RelativeLayout) findViewById(R.id.layout2);
		layout3 = (RelativeLayout) findViewById(R.id.relativeLayout4);
		layout4 = (RelativeLayout) findViewById(R.id.relativeLayout5);
		layout5 = (RelativeLayout) findViewById(R.id.relativeLayout3);
		txt = (TextView) findViewById(R.id.textView6);
		ibt = (ImageButton) findViewById(R.id.ImageButton1);
		bt1 = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);
		bt3 = (Button) findViewById(R.id.button3);
		heightOfComments = new ArrayList<Integer>();
		comments = cd.comments;
		case_id = cd.case_id;
		uName = cd.name;
		bDate = cd.dob;
		gender = cd.gender;
		nationality = cd.nationality;
		diagnosis = cd.diagnosis;
		situation = cd.situation;

		layout2.setVisibility(View.GONE);
		bt1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layout2.setVisibility(View.VISIBLE);
				layout3.setVisibility(View.VISIBLE);
				layout4.setVisibility(View.GONE);
				txt.setVisibility(View.GONE);
			}
		});

		bt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layout2.setVisibility(View.GONE);
			}
		});

		bt3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layout3.setVisibility(View.GONE);
				layout4.setVisibility(View.VISIBLE);
				txt.setVisibility(View.VISIBLE);
			}
		});

		layout2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (layout4.isShown()) {
					Intent intent = new Intent();
					intent.setClass(ActivityCurrentCase.this, MainActivity.class);
					startActivity(intent);
					ActivityCurrentCase.this.finish();
				}
			}
		});

		ibt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Log.d("onClick:",Integer.toString(ibt.getHeight()));
				if (fromWhere.equals("list")) {
					Intent intent = new Intent();
					intent.setClass(ActivityCurrentCase.this, MainActivity.class);
					startActivity(intent);
					ActivityCurrentCase.this.finish();
				}
				else {
					Intent intent = new Intent();
					intent.setClass(ActivityCurrentCase.this, ActivityNotification.class);
					startActivity(intent);
					ActivityCurrentCase.this.finish();
				}
			}
		});

		layout5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityCurrentCase.this, ActivityComment.class);
				Bundle bundle = new Bundle();
				bundle.putString("case_id", case_id);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		// getCaseInfo();

		setupViews();

	}

	public int convertDpToPx(int dp) {
		Resources r = getResources();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dp, r.getDisplayMetrics());
		return px;

	}

	// public class Com {
	// String doctorName;
	// String time;
	// String content;
	//
	// public Com(String doctorName, String time, String content) {
	// this.doctorName = doctorName;
	// this.time = time;
	// this.content = content;
	// }
	// }

	// public void getCaseInfo() {
	// Thread t = new Thread(new Runnable() {
	// public void run() {
	// try {
	// HttpGet httpget = new HttpGet(url);
	//
	// HttpResponse response = SignIn.client.execute(httpget);
	// HttpEntity resEntity = response.getEntity();
	// resString = EntityUtils.toString(resEntity);
	// } catch (Exception e) {
	//
	// }
	// }
	// });
	// t.start();
	// try {
	// t.join();
	// } catch (InterruptedException e) {
	//
	// }
	//
	// JSONObject jobject;
	// try {
	// jobject = new JSONObject(resString);
	// if (jobject.has("cases")) {
	// JSONObject jobject2 = jobject.getJSONObject("cases");
	// if (jobject2.has("comments_en")) {
	// JSONArray commentArr = jobject2.getJSONArray("comments_en");
	// if (commentArr.length() > 0) {
	// for (int i = 0; i < commentArr.length(); i++) {
	// String doctorName = "";
	// String time = "";
	// String content = "";
	// JSONObject commentObj = commentArr.getJSONObject(i);
	// if (commentObj.has("author"))
	// doctorName = commentObj.getString("author");
	// if (commentObj.has("date"))
	// time = commentObj.getString("date").split(" ")[0];
	// if (commentObj.has("content"))
	// content = commentObj.getString("content");
	// Com c = new Com(doctorName, time, content);
	// comments.add(c);
	// }
	// }
	// }
	//
	// if (jobject2.has("client")) {
	//
	// JSONObject jobject3 = jobject2.getJSONObject("client");
	//
	// if (jobject3.has("user_info")) {
	// JSONObject jobject4 = jobject3
	// .getJSONObject("user_info");
	// if (jobject4.has("firstname_en")
	// && jobject4.has("lastname_en"))
	// uName = jobject4.getString("firstname_en") + " "
	// + jobject4.getString("lastname_en");
	// }
	// if (jobject3.has("nation")) {
	// nationality = jobject3.getString("nation");
	// }
	// if (jobject3.has("dob_month") && jobject3.has("dob_day")
	// && jobject3.has("dob_year"))
	// bDate = jobject3.getString("dob_month") + "/"
	// + jobject3.getString("dob_day") + "/"
	// + jobject3.getString("dob_year");
	// if (jobject3.has("gender"))
	// gender = jobject3.getString("gender");
	// }
	// if (jobject2.has("summary_en")) {
	// JSONObject jobject5 = jobject2.getJSONObject("summary_en");
	// if (jobject5.has("diagnosis"))
	// diagnosis = jobject5.getString("diagnosis");
	// if (jobject5.has("present_situation"))
	// situation = jobject5.getString("present_situation");
	// }
	//
	// }
	//
	// } catch (Exception e) {
	//
	// }
	//
	// }

	public void setupViews() {
		LayoutInflater vi = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View insertPoint = findViewById(R.id.LinearLayoutScroll);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 60);
		if (comments.size() > 0) {
			for (int i = 0; i < comments.size(); i++) {
				final int j = i;
				final View v1 = vi.inflate(R.layout.case_info_comment, null);

				TextView doc_name = (TextView) v1.findViewById(R.id.textView01);
				doc_name.setText(comments.get(i).doctorName);
				TextView time = (TextView) v1.findViewById(R.id.textView02);
				time.setText(comments.get(i).time);
				final TextView text = (TextView) v1
						.findViewById(R.id.textView03);
				text.setText(comments.get(i).content);

				text.getViewTreeObserver().addOnGlobalLayoutListener(
						new ViewTreeObserver.OnGlobalLayoutListener() {
							boolean flag = true;

							@Override
							public void onGlobalLayout() {
								heightOfComments.add(text.getHeight());
								// Log.d("onGlobalLayout:",
								// Integer.toString(text.getHeight()));
								if (flag) {
									if (text.getHeight() > convertDpToPx(32))
										text.setHeight(convertDpToPx(32));
									flag = false;
								}
							}

						});

				((ViewGroup) insertPoint).addView(v1);
				ImageButton ibtEx = (ImageButton) v1
						.findViewById(R.id.imageButton01);
				ibtEx.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						TextView text = (TextView) v1
								.findViewById(R.id.textView03);

						if (text.getHeight() > convertDpToPx(32)) {
							text.setHeight(convertDpToPx(32));
						} else {
							text.setHeight(heightOfComments.get(j));
						}
					}
				});
				RelativeLayout layout01 = new RelativeLayout(this);
				((ViewGroup) insertPoint).addView(layout01, lp);
			}
		}
		View v2 = vi.inflate(R.layout.case_info_patient, null);
		View v3 = vi.inflate(R.layout.case_info_diagnosis, null);
		View v4 = vi.inflate(R.layout.case_info_present_situation, null);

		TextView txt1 = (TextView) v2.findViewById(R.id.textView1);
		txt1.setText(uName);
		TextView txt2 = (TextView) v2.findViewById(R.id.textView3);
		txt2.setText(bDate);
		TextView txt3 = (TextView) v2.findViewById(R.id.textView4);
		txt3.setText(gender);
		TextView txt4 = (TextView) v2.findViewById(R.id.textView5);
		txt4.setText(nationality);
		TextView txt5 = (TextView) v3.findViewById(R.id.textView2);
		txt5.setText(diagnosis);
		TextView txt6 = (TextView) v4.findViewById(R.id.textView2);
		txt6.setText(situation);

		((ViewGroup) insertPoint).addView(v2);
		RelativeLayout layout02 = new RelativeLayout(this);
		((ViewGroup) insertPoint).addView(layout02, lp);
		((ViewGroup) insertPoint).addView(v3);
		RelativeLayout layout03 = new RelativeLayout(this);
		((ViewGroup) insertPoint).addView(layout03, lp);
		((ViewGroup) insertPoint).addView(v4);

	}

}
