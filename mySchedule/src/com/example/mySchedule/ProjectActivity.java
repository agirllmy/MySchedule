package com.example.mySchedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myschedule.R;

public class ProjectActivity extends Activity {
	private ImageButton mAddProject;
	private ListView mProject;
	private LoaderManager mManager;
	private ProjectAdapter mAdapter = new ProjectAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.project_layout);
		mAddProject = (ImageButton) findViewById(R.id.add_project);
		mProject = (ListView) findViewById(R.id.mproject);
		mManager = this.getLoaderManager();
		mManager.initLoader(0, null, ProjectLoader);
		mAddProject.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 使用一个dialog进行添加
				LinearLayout newproject;
				final EditText inputproject;
				newproject = (LinearLayout) getLayoutInflater().inflate(
						R.layout.item_project_layout, null);
				inputproject = (EditText) newproject
						.findViewById(R.id.new_project_dailog);
				new AlertDialog.Builder(ProjectActivity.this)
						.setTitle("设置开始日期")
						.setView(newproject)
						.setPositiveButton("添加",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										ContentResolver cr = getContentResolver();
										ContentValues cv = new ContentValues();
										cv.put(ContentData.ProgramTableData.NAME,
												inputproject.getText()
														.toString());
										cr.insert(
												ContentData.ProgramTableData.CONTENT_URI,
												cv);
										mManager.restartLoader(0, null,
												ProjectLoader);
										// 此刻代码已经执行
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).create().show();
			}

		});
		mProject.setOnItemLongClickListener(new OnItemLongClickListener() {
			// 长按列表某一项 就会弹出属于该项目的日程
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				String project="";
				// 点击了某项 就将某一项的日程现实出来
				ContentResolver cr = getContentResolver();
				Cursor cursor = cr.query(ContentData.UserTableData.CONTENT_URI,
						new String[]{ContentData.UserTableData.TITTLE},
						ContentData.UserTableData.PROJECT + "=?",
						new String[] {((Map<String,String>)mAdapter.getItem(position)).get(ContentData.ProgramTableData.NAME)}, null);
				
		
				while (cursor.moveToNext())
				{
					Log.d("xuanxuan", cursor.getString(cursor.getColumnIndex(ContentData.UserTableData.TITTLE)));
					project=project+cursor.getString(cursor.getColumnIndex(ContentData.UserTableData.TITTLE))+"\n";
				}
				Toast.makeText(ProjectActivity.this, project, Toast.LENGTH_LONG)
						.show();
				return true;
			}
		});

	}

	// 动态将项目名称加载到listView中
	private LoaderCallbacks<Cursor> ProjectLoader = new LoaderCallbacks<Cursor>() {
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Uri uri = ContentData.ProgramTableData.CONTENT_URI;
			return new CursorLoader(ProjectActivity.this, uri, null, null,
					null, null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			if (cursor == null) {
				return;
			}
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				String name = cursor.getString(cursor
						.getColumnIndex(ContentData.ProgramTableData.NAME));
				map.put(ContentData.ProgramTableData.NAME, name);
				list.add(map);
			}
			
			mAdapter.setList(list);
			mProject.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();

		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {

		}
	};

	// 适配器用于装载数据库中信息
	private class ProjectAdapter extends BaseAdapter {

		private List<Map<String, String>> list;

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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			if (convertView == null) {
				v = LayoutInflater.from(ProjectActivity.this).inflate(
						R.layout.item_projectshow_layout, null);
			} else {
				v = convertView;
			}
			TextView input_project = (TextView) v
					.findViewById(R.id.project_item_show);
			input_project
					.setText("   "
							+ list.get(position).get(
									ContentData.ProgramTableData.NAME));
			return v;
		}
	}
	

}
