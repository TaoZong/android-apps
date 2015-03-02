package com.moreapp.doctorapp;

import java.util.ArrayList;

import com.moreapp.doctorapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

// Incoming Case List

public class FragmentIncomingCaseList extends Fragment {

	View view;
	ListView lv;
	ArrayList<IncomCase> cases;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_b, container, false);
		lv = (ListView) view.findViewById(R.id.listView1);
		cases = new ArrayList<IncomCase>();

		// fake cases
		for (int i = 0; i < 5; i++) {
			String a = "Case " + i;
			String b = i + " hours remaning";
			int c = (i + 1) * 20;
			IncomCase cc = new IncomCase(a, b, c);
			cases.add(cc);
		}

		IncomCaseAdapter adapter = new IncomCaseAdapter(getActivity(),
				R.layout.incomingcase_list_row, cases);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityIncomingCase.class);
				startActivity(intent);
				getActivity().finish();
			}

		});
		return view;
	}

	public class IncomCase {
		String name;
		String summary;
		int time;

		public IncomCase(String name, String summary, int time) {
			this.name = name;
			this.summary = summary;
			this.time = time;
		}
	}

	public class IncomCaseHolder {
		TextView nameTxt;
		TextView summaryTxt;
		ProgressBar timeBar;
		ImageView image;
	}

	public class IncomCaseAdapter extends ArrayAdapter<IncomCase> {
		Context context;
		int layoutResourceId;
		ArrayList<IncomCase> data;

		public IncomCaseAdapter(Context context, int layoutResourceId,
				ArrayList<IncomCase> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			IncomCaseHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new IncomCaseHolder();
				holder.nameTxt = (TextView) row.findViewById(R.id.casename);
				holder.summaryTxt = (TextView) row
						.findViewById(R.id.casesummary);
				holder.timeBar = (ProgressBar) row
						.findViewById(R.id.progressBar1);
				holder.image = (ImageView) row.findViewById(R.id.imageView1);
				row.setTag(holder);

			} else {
				holder = (IncomCaseHolder) row.getTag();
			}
			IncomCase c = data.get(position);
			holder.nameTxt.setText(c.name);
			holder.summaryTxt.setText(c.summary);
			holder.timeBar.setProgress(c.time);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.setMargins(convertDpToPx(c.time * 2 + 6), convertDpToPx(65),
					0, 0);
			holder.image.setLayoutParams(params);
			if (c.time < 33) {
				holder.timeBar.setProgressDrawable(getResources().getDrawable(
						R.drawable.progress_green));
			} else if (c.time < 66)
				holder.timeBar.setProgressDrawable(getResources().getDrawable(
						R.drawable.progress_yellow));
			else
				holder.timeBar.setProgressDrawable(getResources().getDrawable(
						R.drawable.progress_red));
			return row;
		}

	}

	public int convertDpToPx(int dp) {
		Resources r = getResources();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dp, r.getDisplayMetrics());
		return px;

	}
}
