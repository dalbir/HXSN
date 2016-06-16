package com.hxsn.iot.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxsn.iot.R;

public class TopFragment extends Fragment{

	private View view;
	
	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	public void startFragment(Fragment fragment){
		
	}
	
	public void startFragmentInA (Fragment fragment){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.layout_demotop, container, false);
		
//		view.findViewById(R.id.btn).setonClick(
//			ilistener.onAclik();	
//		);
		return view;
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
	}

	
}
