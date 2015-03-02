package com.moreapp.demo;

import com.moreapp.demo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Default fragment

public class Layout1 extends Fragment  {
	private View mRoot;
	private FragmentTabHost mTabHost;
	private final String tab_1 = "Featured News";
	private final String tab_2 = "Notifications";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.fragment_layout1, null);
		
		mTabHost = (FragmentTabHost)mRoot.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("fragmentA").setIndicator(tab_1),
                FragmentA.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentB").setIndicator(tab_2),
                FragmentB.class, null);
//        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
//        	mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 150;
//        }
		return mRoot;
	}
	
	

	
}
