package com.example.mySchedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.myschedule.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private TextView mDate;
	private Button mCreateTask, mClearAllTask;
	private Button mSchedule, mHelp, mSetting;// 主界面下方的按钮
	private Chronometer mChronometer;
	private int mTimeTag = 1;
	private LoaderManager mManager;
	private ListView mTimeItems;
	private SharedPreferences mShareprefences;
	private LinearLayout mLogin;
	private EditText mLoginPassword;
	private AlertDialog.Builder mLoginDailog;
	private LoaderCallbacks<Cursor> TimeLoader = new LoaderCallbacks<Cursor>() {
		// 创建一个新的CursorLoader对象
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Uri uri = ContentData.TimerTableData.CONTENT_URI;
			return new CursorLoader(MainActivity.this, uri, null, null, null,
					null);
		}

		// 加载完后 更新UI信息
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			// 此处为测试所用
			if (cursor == null) {
				return;
			}
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			// 将指定数据插入
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				String task = cursor.getString(cursor
						.getColumnIndex(ContentData.TimerTableData.TASK));
				String time = cursor.getString(cursor
						.getColumnIndex(ContentData.TimerTableData.TIME));
				map.put(ContentData.TimerTableData.TASK, task);
				map.put(ContentData.TimerTableData.TIME, time);
				list.add(map);
			}
			// // 新建一个适配器对象
			TimerAdapter timerAdapter = new TimerAdapter();
			timerAdapter.setList(list);
			mTimeItems.setAdapter(timerAdapter);
			timerAdapter.notifyDataSetChanged();
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {

		}
	};

	// 适配器
	class TimerAdapter extends BaseAdapter {
		private List<Map<String, String>> list;// 存放数据的集合

		public void setList(List<Map<String, String>> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		// 生成View
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			if (convertView == null) {
				v = LayoutInflater.from(MainActivity.this).inflate(
						R.layout.item_time_layout, null);
			} else {
				v = convertView;
			}
			// 得到相关空间
			TextView task, time;
			task = (TextView) v.findViewById(R.id.task);
			time = (TextView) v.findViewById(R.id.time);
			// 设置控件信息
			task.setText(list.get(position)
					.get(ContentData.TimerTableData.TASK));
			time.setText(list.get(position)
					.get(ContentData.TimerTableData.TIME));
			return v;
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);
		mShareprefences = getSharedPreferences("mSharedPreferences",
				MODE_PRIVATE);

		// 如果设置了密码
		if (!mShareprefences.getString("PASSWORD", "").equals("")) {
			mLogin = (LinearLayout) getLayoutInflater().inflate(
					R.layout.dailog_login_layout, null);
			mLoginPassword = (EditText) mLogin.findViewById(R.id.loginpass);
			mLoginDailog = new AlertDialog.Builder(MainActivity.this);
			mLoginDailog.setTitle("输入密码").setView(mLogin)
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (!mLoginPassword
									.getText()
									.toString()
									.equals(mShareprefences.getString(
											"PASSWORD", ""))) {
								Toast.makeText(MainActivity.this, "密码输入错误",
										Toast.LENGTH_SHORT).show();
								InputMethodManager imm = (InputMethodManager)getSystemService(AddLocationActivity.INPUT_METHOD_SERVICE);  
								imm.hideSoftInputFromWindow(mLoginPassword.getWindowToken(), 0);
								finish();
							}
							
						}
					}).setNegativeButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							InputMethodManager imm = (InputMethodManager)getSystemService(AddLocationActivity.INPUT_METHOD_SERVICE);  
							imm.hideSoftInputFromWindow(mLoginPassword.getWindowToken(), 0);
							finish();
						}
					}).create();
			mLoginDailog.setCancelable(false);
			Log.d("hahahah",
					"setCanceledOnTouchOutside =" + mLoginDailog.getContext());
			mLoginDailog.show();
		}
		// 创建一个LoaderManager
		mManager = this.getLoaderManager();
		mManager.initLoader(0, null, TimeLoader);

		mDate = (TextView) findViewById(R.id.date);
		mCreateTask = (Button) findViewById(R.id.click);
		mSchedule = (Button) findViewById(R.id.schedule);
		mHelp = (Button) findViewById(R.id.help);
		mSetting = (Button) findViewById(R.id.setting);
		// 获取当前的日期
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); // 获取当前年份
		int month = c.get(Calendar.MONTH) + 1;// 获取当前月份
		int day = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
		String date = year + "-" + month + "-" + day + "";// 将当前日期转化成字符串
		mDate.setText(date);
		mDate.getBackground().setAlpha(100);// 设置透明

		// 记录绑定监听器
		mCreateTask.setOnClickListener(new createTask());
		// 设置计时器
		mChronometer = (Chronometer) findViewById(R.id.chronometer);
		mClearAllTask = (Button) findViewById(R.id.clearall);
		mClearAllTask.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ContentResolver cr = getContentResolver();
				Log.d("shanchu", "cr=" + cr);
				cr.delete(ContentData.TimerTableData.CONTENT_URI, null, null);
				mManager.restartLoader(0, null, TimeLoader);
			}
		});

		mTimeItems = (ListView) findViewById(R.id.timers);
		Log.d("lmy", "mTimeItems" + mTimeItems);
		// 日程Activity
		mSchedule.setOnClickListener(new ChooseMenu());
		// 帮助Activity
		mHelp.setOnClickListener(new ChooseMenu());
		mSetting.setOnClickListener(new ChooseMenu());

	}

	private class ChooseMenu implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.help:
				startActivity(new Intent().setClass(MainActivity.this,
						HelpActivity.class));
				break;
			case R.id.schedule:
				startActivity(new Intent().setClass(MainActivity.this,
						ScheduleActivity.class));
				break;
			case R.id.setting:
				Log.d("lmy", "setting=" + mSetting);
				startActivity(new Intent().setClass(MainActivity.this,
						SettingActivity.class));
				break;
			}
		}
	}

	private class createTask implements View.OnClickListener {
		private LinearLayout mStartTask, mPauseTask, mRestartSaveTask;
		private EditText input_task;
		private TextView output_task, isSave_task;

		@Override
		public void onClick(View v) {
			switch (mTimeTag) {
			case 1:// start
				mStartTask = (LinearLayout) getLayoutInflater().inflate(
						R.layout.dailog_starttask_layout, null);
				input_task = (EditText) mStartTask
						.findViewById(R.id.input_task);
				new AlertDialog.Builder(MainActivity.this).setTitle("输入任务")
						.setView(mStartTask)
						.setPositiveButton("开始", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 改变名字

								mCreateTask.setText(input_task.getText()
										.toString());
								mChronometer.setBase(SystemClock
										.elapsedRealtime());
								mChronometer.start();
								mTimeTag = 2;
							}
						}).setNegativeButton("取消", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).create().show();// .setCanceledOnTouchOutside(false);
				break;
			case 2:// 计时状态进入暂停状态
					// 重新初始化一个LinearLayout
				mPauseTask = (LinearLayout) getLayoutInflater().inflate(
						R.layout.dailog_pausetask_layout, null);
				output_task = (TextView) mPauseTask
						.findViewById(R.id.output_task);
				output_task.setText(mCreateTask.getText().toString());
				new AlertDialog.Builder(MainActivity.this).setTitle("当前任务")
						.setView(mPauseTask)
						.setPositiveButton("暂停", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 改变名字

								mChronometer.stop();
								mTimeTag = 3;
							}
						}).setNegativeButton("取消", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).create().show();
				break;
			case 3:// 从暂停状态开始进入
				mRestartSaveTask = (LinearLayout) getLayoutInflater().inflate(
						R.layout.dailog_restartsave_layout, null);
				isSave_task = (TextView) mRestartSaveTask
						.findViewById(R.id.restart_save_task);
				isSave_task.setText(mCreateTask.getText().toString());
				new AlertDialog.Builder(MainActivity.this).setTitle("当前任务")
						.setView(mRestartSaveTask)
						.setPositiveButton("重启", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 改变名字
								mChronometer.start();
								mTimeTag = 2;
							}
						}).setNeutralButton("保存", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 将数据插入
								ContentResolver cr = getContentResolver();
								ContentValues cv = new ContentValues();
								cv.put(ContentData.TimerTableData.TASK,
										isSave_task.getText().toString());
								cv.put(ContentData.TimerTableData.TIME,
										mChronometer.getText().toString());
								Log.d("wl", "cv=" + cv);
								Uri result = cr.insert(
										ContentData.TimerTableData.CONTENT_URI,
										cv);
								mManager.restartLoader(0, null, TimeLoader);
								Log.d("wl", "cr=" + cr + " result = " + result);
								mTimeTag = 1;
							}
						}).setNegativeButton("取消", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).create().show();
				break;
			}

		}
	}
}
