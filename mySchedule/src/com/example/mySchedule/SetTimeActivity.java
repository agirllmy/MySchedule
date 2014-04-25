package com.example.mySchedule;

import com.example.myschedule.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

@SuppressLint("NewApi")
public class SetTimeActivity extends Activity {
	private DatePicker mStartdate, mEnddate;
	private TimePicker mStarttime, mEndtime;
	private TextView mCurrentStartDate, mCurrentEndDate, mCurrentStartTime,
			mCurrentEndTime;
	private Button setStartDate, setEndDate, setStartTime, setEndTime,
			complete;
	private LinearLayout setstarttime_layout, setendtime_layout,
			setstartdate_layout, setenddate_layout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settime_layout);
		// 设置日期时间的View
		setStartDate = (Button) findViewById(R.id.setStartDate);
		setEndDate = (Button) findViewById(R.id.setEndDate);
		setStartTime = (Button) findViewById(R.id.setStartTime);
		setEndTime = (Button) findViewById(R.id.setEndTime);
		mCurrentStartDate = (TextView) findViewById(R.id.current_startdate);
		mCurrentEndDate = (TextView) findViewById(R.id.current_enddate);
		mCurrentStartTime = (TextView) findViewById(R.id.current_starttime);
		mCurrentEndTime = (TextView) findViewById(R.id.current_endtime);
		// 开始日期对话框的View
		

		// 点击按钮可设置开始时间
		setStartDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setstartdate_layout = (LinearLayout) getLayoutInflater().inflate(
						R.layout.setstartdate_layout, null);
				mStartdate = (DatePicker) setstartdate_layout
						.findViewById(R.id.date_setting1);
				new AlertDialog.Builder(SetTimeActivity.this)
						.setTitle("设置开始日期")
						.setView(setstartdate_layout)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mCurrentStartDate
												.setText(getStartDate());
									}
								}).create().show();

			}
		});
		// 结束日期对话框的View
	
		setEndDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setenddate_layout = (LinearLayout) getLayoutInflater().inflate(
						R.layout.setenddate_layout, null);
				mEnddate = (DatePicker) setenddate_layout.findViewById(R.id.date_setting2);
				new AlertDialog.Builder(SetTimeActivity.this)
						.setTitle("设置结束日期")
						.setView(setenddate_layout)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mCurrentEndDate.setText(getEndDate());
									}
								}).create().show();

			}
		});
		// 开始时间的对话框View

		setStartTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setstarttime_layout = (LinearLayout) getLayoutInflater().inflate(
						R.layout.setstarttime_layout, null);
				mStarttime = (TimePicker) setstarttime_layout.findViewById(R.id.starttime_setting);
				new AlertDialog.Builder(SetTimeActivity.this)
						.setTitle("设置开始时间")
						.setView(setstarttime_layout)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mCurrentStartTime.setText(getStartTime());
									}
								}).create().show();

			}
		});
		//结束时间的对话框View

		setEndTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setendtime_layout = (LinearLayout) getLayoutInflater().inflate(
						R.layout.setendtime_layout, null);
				mEndtime = (TimePicker) setendtime_layout.findViewById(R.id.endtime_setting);
				new AlertDialog.Builder(SetTimeActivity.this)
						.setTitle("设置结束")
						.setView(setendtime_layout)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mCurrentEndTime.setText(getEndTime());
									}
								}).create().show();

			}
		});
		//确认设置
		complete = (Button) findViewById(R.id.settime_complete);
		complete.setOnClickListener(new TimeSetCompleteListener());

	}

	public String getStartDate() {
		int day = mStartdate.getDayOfMonth();
		int month = mStartdate.getMonth()+1;
		int year = mStartdate.getYear();
		Log.d("my", "day = " + day + " month = " + month + " year = " + year);
		return day + "" + "-" + month + "" + "-" + year + "";
	}

	public String getEndDate() {
		int day = mEnddate.getDayOfMonth();
		int month = mEnddate.getMonth()+1;
		int year = mEnddate.getYear();
		return day + "" + "-" + month + "" + "-" + year + "";
	}

	public String getStartTime() {
		int hour = mStarttime.getCurrentHour();
		int minute = mStarttime.getCurrentMinute();
		return hour + "" + ":" + minute + "";
	}

	public String getEndTime() {
		int hour = mEndtime.getCurrentHour();
		int minute = mEndtime.getCurrentMinute();
		return hour + "" + ":" + minute + "";
	}

	private class TimeSetCompleteListener implements View.OnClickListener {
		public void onClick(View v) {
			Intent data = new Intent();
			data.putExtra("startdate",mCurrentStartDate.getText());
			data.putExtra("enddate", mCurrentEndDate.getText());
			data.putExtra("starttime", mCurrentStartTime.getText());
			data.putExtra("endtime", mCurrentEndTime.getText());
			Log.d("my", "data = " + data);
			setResult(Activity.RESULT_OK, data);
			finish();
		}
	}

}
