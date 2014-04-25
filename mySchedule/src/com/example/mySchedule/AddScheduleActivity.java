package com.example.mySchedule;

import java.util.Calendar;

import com.example.myschedule.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddScheduleActivity extends Activity {
	private String[] mTimeReminderTitleItems = { "   " + getDate(), "    不提醒" };
	private int[] mTimeReminderImageItems = { R.drawable.icon_affair_detail_time,
			R.drawable.icon_affair_detail_alert };
	
	private int mRepeateNum = 1, mReminderNum = 1;
	private String mStartDate = null, mEndDate = null, mStartTime = null,
			mEndTime = null, mLabelContent = null;
	private int mLocationNum = 0;
	private String mProgramNum = "";
	
	private ListView mTimeReminderSetting;
	private ImageButton mRecord, mRepeat, mProject, mLabel, mLocation;
	private Button mCompleteSetting;
	private EditText mScheduleInput;
	private RadioButton mRadioButton1, mRadioButton2, mRadioButton3, mRadioButton4;

	private static final int REQUEST_SET_REPEAT = 1;
	private static final int REQUEST_SET_REMINDER = 2;
	private static final int REQUEST_SET_TIEM = 3;
	private static final int REQUEST_SET_LABEL = 4;
	private static final int REQUEST_SET_PROGRAM = 5;
	private static final int REQUEST_SET_LOCATION = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_schedule_layout);

		// 设置录音按钮
		mRecord = (ImageButton) findViewById(R.id.sound_recording);
		mRecord.setImageResource(R.drawable.ic_voice_search);
		mRecord.getBackground().setAlpha(100);
		// 设置编辑的文本框信息
		mScheduleInput = (EditText) findViewById(R.id.input_schedule);
		// 获得输入的文本框的字符串
		// 设置重要程度
		mRadioButton1 = (RadioButton) findViewById(R.id.toiu);
		mRadioButton1.setClickable(true);
		mRadioButton2 = (RadioButton) findViewById(R.id.toinu);
		mRadioButton3 = (RadioButton) findViewById(R.id.toniu);
		mRadioButton4 = (RadioButton) findViewById(R.id.toninu);
		// 设置ListView的信息
		mTimeReminderSetting = (ListView) findViewById(R.id.calendar_and_eminders);
		TextImageAdapter textImageAdapter=new TextImageAdapter(this);
		mTimeReminderSetting.setAdapter(textImageAdapter);
		textImageAdapter.notifyDataSetChanged();
		

		// 设置四个ImageButton
		// 重复图标设置
		mRepeat = (ImageButton) findViewById(R.id.repeat);
		mRepeat.setImageResource(R.drawable.icon_affair_detail_repeat);
		mRepeat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent().setClass(
						AddScheduleActivity.this, SetRepeatActivity.class),
						REQUEST_SET_REPEAT);
			}
		});
		// 项目图标设置
		mProject = (ImageButton) findViewById(R.id.project);
		mProject.setImageResource(R.drawable.icon_affair_detail_folder);
		mProject.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult((new Intent().setClass(
						AddScheduleActivity.this, AddProjectActivity.class)),
						REQUEST_SET_PROGRAM);

			}
		});
		// 标签图表设置
		mLabel = (ImageButton) findViewById(R.id.label);
		mLabel.setImageResource(R.drawable.icon_affair_detail_alert);
		mLabel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent().setClass(
						AddScheduleActivity.this, AddLabelActivity.class), REQUEST_SET_LABEL);

			}
		});
		// 位置图标设置
		mLocation = (ImageButton) findViewById(R.id.location);
		mLocation.setImageResource(R.drawable.icon_affair_detail_position);
		mLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent().setClass(
						AddScheduleActivity.this, AddLocationActivity.class),
						REQUEST_SET_LOCATION);

			}
		});
		// 子任务图标设置

		// 设置点击ListView的一项触发另一个Activity
		mTimeReminderSetting.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					startActivityForResult(new Intent().setClass(
							AddScheduleActivity.this, SetTimeActivity.class),
							REQUEST_SET_TIEM);
					break;
				case 1:
					startActivityForResult(new Intent()
							.setClass(AddScheduleActivity.this,
									SetReminderActivity.class),
							REQUEST_SET_REMINDER);
					break;
				}
			}
		});

		// <完成>按钮的事件设置
		mCompleteSetting = (Button) findViewById(R.id.setting_complete);// 找到完成按钮
		// 点击完成按钮进入插入操作
		mCompleteSetting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String schedule_tittle = mScheduleInput.getText().toString();
				if(schedule_tittle.equals("")){
					Toast.makeText(AddScheduleActivity.this, "必须设置任务名称", Toast.LENGTH_SHORT).show();
					return;
				}
				if(getImportance()==0){
					Toast.makeText(AddScheduleActivity.this, "必须设置重要紧急程度", Toast.LENGTH_SHORT).show();
					return;
					
				}
				ContentResolver cr = getContentResolver();
				ContentValues cv = new ContentValues();
				// 将tittle加入数据库
				cv.put(ContentData.UserTableData.TITTLE, schedule_tittle);// 将标题插入数据库
				// 将重要度插入数据库
				cv.put(ContentData.UserTableData.IMPORTANCE, getImportance());
				// 将开始日期、结束日期、开始时间、结束时间插入数据库
				cv.put(ContentData.UserTableData.STARTDATE, mStartDate);
				cv.put(ContentData.UserTableData.ENDDATE, mEndDate);
				cv.put(ContentData.UserTableData.STARTTIME, mStartTime);
				cv.put(ContentData.UserTableData.ENDTIME, mEndTime);
				// 设置提醒的模式
				cv.put(ContentData.UserTableData.REMINDER, mReminderNum);
				// 重复设置
				cv.put(ContentData.UserTableData.REPEAT, mRepeateNum);
				// 项目设置
				cv.put(ContentData.UserTableData.PROJECT, mProgramNum);
				// 标签设置
				cv.put(ContentData.UserTableData.LABEL, mLabelContent);
				// 位置设置

				cv.put(ContentData.UserTableData.LOACTION, mLocationNum);
				cr.insert(ContentData.UserTableData.CONTENT_URI, cv);
				startActivity(new Intent().setClass(AddScheduleActivity.this,
						ScheduleActivity.class));
				finish();

			}
		});

	}

	// 获取重要程度
	public int getImportance() {
		int importance = 0;
		if (mRadioButton1.isChecked() == true)
			importance = 1;
		else if (mRadioButton2.isChecked() == true)
			importance = 2;
		else if (mRadioButton3.isChecked() == true)
			importance = 3;
		else if (mRadioButton4.isChecked() == true)
			importance = 4;
		return importance;

	}

	// 获取当前的日期
	public String getDate() {
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR); // 获取当前年份
		int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
		int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
		String mDate = mYear + "-" + mMonth + "-" + mDay + "";// 将当前日期转化成字符串
		return mDate;
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("my", "requestCode = " + requestCode + " resultCode = "
				+ resultCode);
		if (resultCode == Activity.RESULT_OK) {
			// 可以根据多个请求代码来作相应的操作
			if (REQUEST_SET_REPEAT == requestCode) {
				mRepeateNum = data.getExtras().getInt("repeatNum");
			} else if (REQUEST_SET_REMINDER == requestCode) {
				mReminderNum = data.getExtras().getInt("remindertNum");
				mTimeReminderTitleItems[2]=data.getExtras().getString("reminderMode");			
			} else if (REQUEST_SET_TIEM == requestCode) {
				mStartDate = data.getExtras().getString("startdate");
				mEndDate = data.getExtras().getString("enddate");
				mStartTime = data.getExtras().getString("starttime");
				mEndTime = data.getExtras().getString("endtime");
				
			} else if (REQUEST_SET_LABEL == requestCode) {
				mLabelContent = data.getExtras().getString("label");
			} else if (REQUEST_SET_PROGRAM == requestCode) {
				mProgramNum = data.getExtras().getString(
						ContentData.ProgramTableData.NAME);
			} else if (REQUEST_SET_LOCATION == requestCode) {
				mLocationNum = data.getExtras().getInt(
						ContentData.LocationTableData.NAME);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	// 将listView表示成一个图片加文字
	private class TextImageAdapter extends BaseAdapter {
		private Context mContext;

		public TextImageAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return mTimeReminderTitleItems.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// 优化ListView
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.calender_eminder_layout, null);
				ItemViewCache viewCache = new ItemViewCache();
				viewCache.mImageView = (ImageView) convertView
						.findViewById(R.id.CEimage);
				viewCache.mTextView = (TextView) convertView
						.findViewById(R.id.calendarOrEmind);
				convertView.setTag(viewCache);
			}
			ItemViewCache cache = (ItemViewCache) convertView.getTag();
			cache.mImageView.setImageResource(mTimeReminderImageItems[position]);
			cache.mTextView.setText(mTimeReminderTitleItems[position]);
			return convertView;
		}
	}

	// 保存了一个TextView和一个ImageView
	class ItemViewCache {
		public TextView mTextView;
		public ImageView mImageView;
	}


}
