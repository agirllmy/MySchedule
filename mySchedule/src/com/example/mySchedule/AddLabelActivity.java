package com.example.mySchedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.myschedule.R;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AddLabelActivity extends Activity {
	private TextView label_attention;
	private ListView labels;
	private Button add_new_label, label_complete;
	private EditText input_label;
	private LoaderManager manager;
	protected String all_label = "";

	public String getAllLabel() {
		return all_label;
	}

	public void resetAllLabel() {
		all_label = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_label_layout);
		label_attention = (TextView) findViewById(R.id.label_attention);
		label_attention.setBackgroundColor(Color.BLUE);
		label_attention.setAlpha(100);
		label_attention.setTextColor(Color.GRAY);
		input_label = (EditText) findViewById(R.id.input_label);
		add_new_label = (Button) findViewById(R.id.add_new_label);
		label_complete = (Button) findViewById(R.id.label_complete);
		input_label = (EditText) findViewById(R.id.input_label);
		labels = (ListView) findViewById(R.id.labels);
		// 创建一个LoaderManager
		manager = this.getLoaderManager();
		manager.initLoader(0, null, LabelLoader);

		add_new_label.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * if (input_label.getText() == null) new
				 * Toast(AddLabelActivity.this).makeText( AddLabelActivity.this,
				 * "请输入一个标签", 1).show();
				 */
				String new_label = input_label.getText().toString();
				ContentResolver cr = getContentResolver();
				ContentValues cv = new ContentValues();
				cv.put(ContentData.LabelTableData.NAME, new_label);
				cr.insert(ContentData.LabelTableData.CONTENT_URI, cv);
				manager.restartLoader(0, null, LabelLoader);
				// startActivity(new Intent().setClass(AddLabelActivity.this,
				// AddScheduleActivity.class));
			}
		});
		label_complete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("label", getAllLabel());
				Log.d("my", "data = " + data);
				setResult(Activity.RESULT_OK, data);
				resetAllLabel();
				finish();
			}
		});

	}

	// 重写LoaderCallbacks相关方法
	private LoaderCallbacks<Cursor> LabelLoader = new LoaderCallbacks<Cursor>() {
		// 创建一个新的CursorLoader对象
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Uri uri = ContentData.LabelTableData.CONTENT_URI;
			return new CursorLoader(AddLabelActivity.this, uri, null, null,
					null, null);
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
				String name = cursor.getString(cursor
						.getColumnIndex(ContentData.LabelTableData.NAME));
				map.put(ContentData.LabelTableData.NAME, name);
				list.add(map);
			}
			// 新建一个适配器对象
			MyAdapter labelAdapter = new MyAdapter();
			labelAdapter.setList(list);
			labels.setAdapter(labelAdapter);
			labelAdapter.notifyDataSetChanged();
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {

		}
	};

	// 适配器的重写
	class MyAdapter extends BaseAdapter {
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
				v = LayoutInflater.from(AddLabelActivity.this).inflate(
						R.layout.item_label_layout, null);
			} else {
				v = convertView;
			}
			// 得到相关空间
			final CheckBox label_item = (CheckBox) v
					.findViewById(R.id.label_item);
			// 设置控件信息
			label_item.setText(list.get(position).get(
					ContentData.LabelTableData.NAME));
			label_item
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							all_label += "," + label_item.getText();
						}
					});
			return v;
		}
	}

}