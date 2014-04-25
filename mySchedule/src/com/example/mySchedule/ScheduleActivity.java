package com.example.mySchedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.myschedule.R;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleActivity extends Activity {
	private ArrayList<String> mNavigationMenu = new ArrayList<String>();
	private ImageButton mAddScheduleButton, mNavigation;
	private TextView mShowToday, mShowDate, mShowWeek;
	private ListView mList1, mList2, mList3, mList4,mNavigationInfo;
	private String mWeek = "";

	// 动态加载消息
	private LoaderManager manager;

	private String getWeekday() {
		switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) {
		case 1:
			mWeek = "Monday";
			break;
		case 2:
			mWeek = "Tuesday";
			break;
		case 3:
			mWeek = "Wednesday";
			break;
		case 4:
			mWeek = "Thursday";
			break;
		case 5:
			mWeek = "Friday";
			break;
		case 6:
			mWeek = "Saturday";
			break;
		case 0:
			mWeek = "Sunday";
			break;
		}
		return mWeek;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.schedule_layout);
		mNavigationInfo = (ListView) findViewById(R.id.navigation_drawer);
		Log.d("wlmy", "mNavigationMenu ="+mNavigationMenu);
		
		
		mNavigationMenu.add("今日日程");
		mNavigationMenu.add("项目");
		mNavigationMenu.add("标签");
		Log.d("wlmy", "mNavigationMenu ="+mNavigationMenu);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_navigation_layout, R.id.navigation_item, mNavigationMenu);
		mNavigationInfo.setAdapter((ListAdapter) adapter);
		mNavigationInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mNavigationMenu.get(position).equals("项目")){
					startActivity(new Intent().setClass(ScheduleActivity.this, ProjectActivity.class));
				}
				
				if (mNavigationMenu.get(position).equals("今日日程")){
					startActivity(new Intent().setClass(ScheduleActivity.this, ScheduleActivity.class));
				}
			}
		});
		
		
		
		// 设置“今天”的日期和星期
		mShowToday = (TextView) findViewById(R.id.today_show);
		mShowToday.setText("今天");
		mShowDate = (TextView) findViewById(R.id.date_show);
		mShowDate.setText((Calendar.getInstance().get(Calendar.MONTH) + 1)
				+ "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		mShowWeek = (TextView) findViewById(R.id.week_show);

		mShowWeek.setText(getWeekday());

		mShowToday.setTextColor(Color.WHITE);
		mShowDate.setTextColor(Color.WHITE);
		mShowWeek.setTextColor(Color.WHITE);
		// 将日程分类并放相应的View
		mList1 = (ListView) findViewById(R.id.iu_list);
		mList2 = (ListView) findViewById(R.id.inu_list);
		mList3 = (ListView) findViewById(R.id.niu_list);
		mList4 = (ListView) findViewById(R.id.ninu_list);
		// 创建一个LaodManager
		manager = this.getLoaderManager();
		manager.initLoader(0, null, ScheduleLoader);
		manager.restartLoader(0, null, ScheduleLoader);
		// 设置导航按钮
		mNavigation = (ImageButton) findViewById(R.id.navigation);
		mNavigation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 弹出一个navigation drawer

			}
		});
		// 设置添加按钮的点击事件
		mAddScheduleButton = (ImageButton) findViewById(R.id.add_item);
		mAddScheduleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.add_item:
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							AddScheduleActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
	}

	// 重写LoaderCallbacks相关方法
	private LoaderCallbacks<Cursor> ScheduleLoader = new LoaderCallbacks<Cursor>() {
		// 创建一个新的CursorLoader对象
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Uri uri = ContentData.UserTableData.CONTENT_URI;
			return new CursorLoader(ScheduleActivity.this, uri, null, null,
					null, null);
		}

		// 加载完后 更新UI信息
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			if (cursor == null) {
				return;
			}
			List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
			List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
			List<Map<String, String>> list3 = new ArrayList<Map<String, String>>();
			List<Map<String, String>> list4 = new ArrayList<Map<String, String>>();

			// 将指定数据插入
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				String name = cursor.getString(cursor
						.getColumnIndex(ContentData.UserTableData.TITTLE));
				int importance = cursor.getInt(cursor
						.getColumnIndex(ContentData.UserTableData.IMPORTANCE));
				map.put(ContentData.UserTableData.TITTLE, name);
				if (importance == 1)
					list1.add(map);
				else if (importance == 2)
					list2.add(map);
				else if (importance == 3)
					list3.add(map);
				else if (importance == 4)
					list4.add(map);
			}
			// 新建一个适配器对象
			MyAdapter adapter1 = new MyAdapter();
			adapter1.setList(list1);
			MyAdapter adapter2 = new MyAdapter();
			adapter2.setList(list2);
			MyAdapter adapter3 = new MyAdapter();
			adapter3.setList(list3);
			MyAdapter adapter4 = new MyAdapter();
			adapter4.setList(list4);
			mList1.setAdapter(adapter1);
			adapter1.notifyDataSetChanged();
			mList2.setAdapter(adapter2);
			adapter1.notifyDataSetChanged();
			mList3.setAdapter(adapter3);
			adapter1.notifyDataSetChanged();
			mList4.setAdapter(adapter4);
			adapter1.notifyDataSetChanged();

		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {

		}
	};

	// 适配器的重写
	public class MyAdapter extends BaseAdapter {
		private List<Map<String, String>> list;// 存放数据的集合

		public void setList(List<Map<String, String>> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// 生成View
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			if (convertView == null) {
				v = LayoutInflater.from(ScheduleActivity.this).inflate(
						R.layout.item_label_layout, null);
			} else {
				v = convertView;
			}
			// 得到相关空间
			CheckBox input_schedule = (CheckBox) v
					.findViewById(R.id.label_item);
			// 设置控件信息
			final int pos = position;
			input_schedule.setText(list.get(position).get(
					ContentData.UserTableData.TITTLE));
			input_schedule
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// 从数据库删除
							ContentResolver cr = getContentResolver();
							Log.d("lmy", "cr" + cr.toString());
							cr.delete(
									ContentData.UserTableData.CONTENT_URI,
									ContentData.UserTableData.TITTLE + "=?",
									new String[] { list.get(pos).get(
											ContentData.UserTableData.TITTLE) });
							// 从listView中移走
							list.remove(pos);
							notifyDataSetChanged();
						}
					});
			return v;
		}
	}


}
