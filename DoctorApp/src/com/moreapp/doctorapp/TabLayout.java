package com.moreapp.doctorapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.moreapp.doctorapp.FragmentCurrentCaseList;
import com.moreapp.doctorapp.FragmentIncomingCaseList;
import com.moreapp.doctorapp.R;

public class TabLayout extends Fragment {
	private View mRoot;
	private FragmentTabHost mTabHost;
	private final String tab_1 = "Current Case";
	private final String tab_2 = "Incoming Case";
	private static int flag = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.fragment_tablayout, null);

		mTabHost = (FragmentTabHost) mRoot.findViewById(android.R.id.tabhost);
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.realtabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("fragmentA").setIndicator(tab_1),
				FragmentCurrentCaseList.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("fragmentB").setIndicator(tab_2),
				FragmentIncomingCaseList.class, null);
		mTabHost.setCurrentTab(flag);
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if (flag == 0)
					flag = 1;
				else
					flag = 0;
			}
		});
		// for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
		// mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 150;
		// }
		return mRoot;
	}
}
