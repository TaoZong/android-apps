package com.moreapp.doctorapp;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.moreapp.doctorapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

// Current Case List

public class FragmentCurrentCaseList extends Fragment {

	View view;
	ListView lv;
	ArrayList<CurCase> cases;
	public static ArrayList<CaseDetailObj> casedetails;
	ArrayList<String> caseids;
	ArrayList<CommentObj> comments;
	ArrayList<NotificationObj> notifications;
	String resString;
	String url = "http://www.morehealthmd.com/api/user/caselist/5356460653752d08222d6314";
	String url2 = "http://www.morehealthmd.com/api/caseinfo/";
	ProgressBar pb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_a, container, false);
		lv = (ListView) view.findViewById(R.id.listView1);
		cases = new ArrayList<CurCase>();
		caseids = new ArrayList<String>();
		casedetails = new ArrayList<CaseDetailObj>();
		pb = (ProgressBar) view.findViewById(R.id.progressBar1);

		new ProgressTask().execute();

		CurCaseAdapter adapter = new CurCaseAdapter(getActivity(),
				R.layout.currentcase_list_row, cases);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						ActivityCurrentCase.class);
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putString("from", "list");
				intent.putExtras(bundle);

				startActivity(intent);
				getActivity().finish();
			}

		});
		return view;
	}

	public class CurCase {
		String name;
		String summary;
		String time;

		public CurCase(String name, String summary, String time) {
			this.name = name;
			this.summary = summary;
			this.time = time;
		}
	}

	public class CurCaseHolder {
		TextView nameTxt;
		TextView summaryTxt;
		TextView timeTxt;
		ImageView image;
	}

	public class CurCaseAdapter extends ArrayAdapter<CurCase> {
		Context context;
		int layoutResourceId;
		ArrayList<CurCase> data;

		public CurCaseAdapter(Context context, int layoutResourceId,
				ArrayList<CurCase> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View row = inflater.inflate(R.layout.currentcase_list_row, null,
					true);
			TextView nameTxt = (TextView) row.findViewById(R.id.casename);
			TextView summaryTxt = (TextView) row.findViewById(R.id.casesummary);
			TextView timeTxt = (TextView) row.findViewById(R.id.casetime);
			ImageView image = (ImageView) row.findViewById(R.id.imageView1);

			CurCase c = data.get(position);
			nameTxt.setText(c.name);
			summaryTxt.setText(c.summary);
			timeTxt.setText(c.time);
			if (c.time.isEmpty())
				image.setVisibility(View.GONE);
			return row;
		}

	}

	public void getCases() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpGet httpget = new HttpGet(url);

					HttpResponse response = ActivitySignIn.client
							.execute(httpget);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
				} catch (Exception e) {

				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {

		}

		JSONObject jobject;
		try {
			jobject = new JSONObject(resString);
			JSONArray jarray = jobject.getJSONArray("cases");
			if (jarray.length() > 0) {
				for (int i = 0; i < jarray.length(); i++) {
					String caseid = jarray.getString(i);
					caseids.add(caseid);
				}

			}

		} catch (Exception e) {

		}

		if (caseids.size() > 0) {
			for (final String caseid : caseids) {
				Thread t1 = new Thread(new Runnable() {
					public void run() {
						try {
							HttpGet httpget = new HttpGet(url2 + caseid);

							HttpResponse response = ActivitySignIn.client
									.execute(httpget);
							HttpEntity resEntity = response.getEntity();
							resString = EntityUtils.toString(resEntity);
						} catch (Exception e) {

						}
					}
				});
				t1.start();
				try {
					t1.join();
				} catch (InterruptedException e) {

				}

				JSONObject object;
				comments = new ArrayList<CommentObj>();
				notifications = new ArrayList<NotificationObj>();
				String case_name = "", uName = "", bDate = "", gender = "", nationality = "", diagnosis = "", situation = "";
				try {
					object = new JSONObject(resString);
					if (object.has("cases")) {
						JSONObject jobject2 = object.getJSONObject("cases");
						if (jobject2.has("casename"))
							case_name = jobject2.getString("casename");
						if (jobject2.has("comments_en")) {
							JSONArray commentArr = jobject2
									.getJSONArray("comments_en");
							if (commentArr.length() > 0) {
								for (int i = 0; i < commentArr.length(); i++) {
									String doctorName = "";
									String time = "";
									String content = "";
									JSONObject commentObj = commentArr
											.getJSONObject(i);
									if (commentObj.has("author"))
										doctorName = commentObj
												.getString("author");
									if (commentObj.has("date"))
										time = commentObj.getString("date")
												.split(" ")[0];
									if (commentObj.has("content"))
										content = commentObj
												.getString("content");
									CommentObj c = new CommentObj(doctorName,
											time, content);
									comments.add(c);
								}
							}
						}

						if (jobject2.has("client")) {

							JSONObject jobject3 = jobject2
									.getJSONObject("client");

							if (jobject3.has("user_info")) {
								JSONObject jobject4 = jobject3
										.getJSONObject("user_info");
								if (jobject4.has("firstname_en")
										&& jobject4.has("lastname_en"))
									uName = jobject4.getString("firstname_en")
											+ " "
											+ jobject4.getString("lastname_en");
							}
							if (jobject3.has("nation")) {
								nationality = jobject3.getString("nation");
							}
							if (jobject3.has("dob_month")
									&& jobject3.has("dob_day")
									&& jobject3.has("dob_year"))
								bDate = jobject3.getString("dob_month") + "/"
										+ jobject3.getString("dob_day") + "/"
										+ jobject3.getString("dob_year");
							if (jobject3.has("gender"))
								gender = jobject3.getString("gender");
						}
						if (jobject2.has("summary_en")) {
							JSONObject jobject5 = jobject2
									.getJSONObject("summary_en");
							if (jobject5.has("diagnosis"))
								diagnosis = jobject5.getString("diagnosis");
							if (jobject5.has("present_situation"))
								situation = jobject5
										.getString("present_situation");
						}
						if (jobject2.has("notification")) {
							JSONArray jarray = jobject2
									.getJSONArray("notification");
							if (jarray.length() > 0) {
								for (int i = 0; i < jarray.length(); i++) {
									String s = jarray.getString(i);
									String[] ss = s.split("-");
									if (ss.length == 4) {
										String content = ss[0];
										String time = ss[1] + "-" + ss[2] + "-"
												+ ss[3];
										NotificationObj n = new NotificationObj(
												content, time);
										notifications.add(n);
									}
								}
							}
						}

						CaseDetailObj casedetail = new CaseDetailObj();
						casedetail.setCaseDetail(comments, notifications,
								caseid, case_name, uName, bDate, gender,
								nationality, diagnosis, situation);
						casedetails.add(casedetail);
					}

				} catch (Exception e) {

				}
			}
		}
	}

	public void showCases() {
		if (casedetails.size() > 0) {
			for (CaseDetailObj c : casedetails) {
				String name = "", summary = "", time = "";
				name = c.case_name;
				if (c.notifications.size() > 0) {
					summary = c.notifications.get(0).content;
					time = c.notifications.get(0).time;
				}
				CurCase cc = new CurCase(name, summary, time);
				cases.add(cc);
			}
		}
	}

	private class ProgressTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pb.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Void result) {
			pb.setVisibility(View.GONE);
			showCases();			
		}

		@Override
		protected Void doInBackground(Void... params) {
			getCases();
			return null;
		}

	}
}
