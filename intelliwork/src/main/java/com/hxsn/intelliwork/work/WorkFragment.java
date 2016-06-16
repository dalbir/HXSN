package com.hxsn.intelliwork.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.hxsn.intelliwork.R;
import com.hxsn.intelliwork.base.BaseFrgament;
import com.hxsn.intelliwork.utils.ImgBtnEffact;


public class WorkFragment extends BaseFrgament {

	Button workDJBtn, workCXBtn, workNCCZDJBtn, workYCCZDJBtn;
	Intent inte = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_work_fragment,
				container, false);
		inintView(view);
		initListener();
		return view;
	}

	private void inintView(View view) {
		// TODO Auto-generated method stub
		workDJBtn = (Button) view.findViewById(R.id.workDJBtn);
		workCXBtn = (Button) view.findViewById(R.id.workCXBtn);
		workNCCZDJBtn = (Button) view.findViewById(R.id.workNCCZDJBtn);
		workYCCZDJBtn = (Button) view.findViewById(R.id.workYCCZDJBtn);
	}

	private void initListener() {
		// TODO Auto-generated method stub
		workDJBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		workCXBtn.setOnTouchListener(ImgBtnEffact.btnTL);
        workNCCZDJBtn.setOnTouchListener(ImgBtnEffact.btnTL);
        workYCCZDJBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		
		workDJBtn.setOnClickListener(new NewListener());
		workCXBtn.setOnClickListener(new NewListener());
		workNCCZDJBtn.setOnClickListener(new NewListener());
		workYCCZDJBtn.setOnClickListener(new NewListener());
	}

	class NewListener implements OnClickListener {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.workDJBtn:
				inte = new Intent(getActivity(),WorkRecordActivity.class);
				WorkRecordActivity.isFarm=false;
				startActivity(inte);
				break;
			case R.id.workCXBtn:
				inte = new Intent(getActivity(),WorkSelectActivity.class);
				startActivity(inte);
				break;
			case R.id.workNCCZDJBtn:
				inte = new Intent(getActivity(),WorkRecordActivity.class);
				WorkRecordActivity.isFarm=true;
				startActivity(inte);
				break;
			case R.id.workYCCZDJBtn:
				inte = new Intent(getActivity(),WorkFarCheckInActivity.class);
				startActivity(inte);
				break;
			default:
				break;
			}

		}

	}

}