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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

public class AddProjectActivity extends Activity {
	private Button add_program, program_complete;
	private EditText input_program;
	private ListView programs;
	private LoaderManager manager;
	private String belong_program;

	public String getProgram() {
		return belong_program;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_program_layout);

		add_program = (Button) findViewById(R.id.add_new_program);
		input_program = (EditText) findViewById(R.id.input_program);
		program_complete = (Button) findViewById(R.id.program_complete);
		programs = (ListView) findViewById(R.id.program);
		programs.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		// 创建一个LoaderManager
		manager = this.getLoaderManager();
		manager.initLoader(0, null, ProgramLoader);
		Log.d("limengying", "manager=" + manager);

		// 点击添加按钮新建项目
		add_program.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String new_program = input_program.getText().toString();
				ContentResolver cr = getContentResolver();
				ContentValues cv = new ContentValues();
				Log.d("limengying", "cv=" + cv);
				// 插入项目数据库
				cv.put(ContentData.ProgramTableData.NAME, new_program);
				Log.d("limengying", "cv=" + cv);
				cr.insert(ContentData.ProgramTableData.CONTENT_URI, cv);
				manager.restartLoader(0, null, ProgramLoader);
			}
		});
		program_complete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra(ContentData.ProgramTableData.NAME, getProgram());
				setResult(Activity.RESULT_OK, data);
				finish();

			}
		});
		
	}

	// 重写LoaderCallbacks相关方法
	private LoaderCallbacks<Cursor> ProgramLoader = new LoaderCallbacks<Cursor>() {
		// 创建一个新的CursorLoader对象
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Uri uri = ContentData.ProgramTableData.CONTENT_URI;
			return new CursorLoader(AddProjectActivity.this, uri, null, null,
					null, null);
		}

		// 加载完后 更新UI信息
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			if (cursor == null) {
				return;
			}
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			// 将指定数据插入
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				String name = cursor.getString(cursor
						.getColumnIndex(ContentData.ProgramTableData.NAME));
				map.put(ContentData.ProgramTableData.NAME, name);
				list.add(map);
			}
			// 新建一个适配器对象
			MyAdapter programAdapter = new MyAdapter();
			programAdapter.setList(list);
			programs.setAdapter(programAdapter);
			programAdapter.notifyDataSetChanged();
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
			return list.size() == 0 ? 0 : list.size();
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
				v = LayoutInflater.from(AddProjectActivity.this).inflate(
						R.layout.item_program_layout, null);
			} else {
				v = convertView;
			}
			// 得到相关空间
			RadioButton program_item = (RadioButton) v
					.findViewById(R.id.program_item);
			// 设置控件信息
			final int pos = position;
			program_item.setText(list.get(position).get(
					ContentData.ProgramTableData.NAME));
			program_item
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if(isChecked==true)
								belong_program=list.get(pos).get(ContentData.ProgramTableData.NAME);

						}
					});
			return v;
		}
	}
}
