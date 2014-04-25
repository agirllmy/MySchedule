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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddLocationActivity extends Activity {
    private ListView mLocations;
    private int mCurrentLocation;
    private LoaderManager mManager;
    private Button mAddLocation,mComplete,mCancel;
    private EditText mInputLoction;
    public int getCurrentLocation() {
		return mCurrentLocation;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_location_layout);
        Log.d("lmyyyy", "cursor =");
        mAddLocation=(Button)findViewById(R.id.add_location);
        mInputLoction=(EditText)findViewById(R.id.input_location);
        mComplete=(Button)findViewById(R.id.complete_addlocation);
        mLocations=(ListView)findViewById(R.id.locations);
        mLocations.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mManager= this.getLoaderManager();
        mManager.initLoader(0, null, LocationLoader);
        Log.d("lmyyyy", "cursor1 =");
       
        //添加一个位置
        mAddLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String new_loction = mInputLoction.getText().toString();
				ContentResolver cr = getContentResolver();
				ContentValues cv = new ContentValues();
				cv.put(ContentData.LocationTableData.NAME, new_loction);
				cr.insert(ContentData.LocationTableData.CONTENT_URI, cv);
				mManager.restartLoader(0, null, LocationLoader);
				
			}
		}); 
        //完成日程添加
        mComplete.setOnClickListener(new View.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent data = new Intent();
				data.putExtra(ContentData.LocationTableData.NAME, getCurrentLocation());
				setResult(Activity.RESULT_OK, data);
				InputMethodManager imm = (InputMethodManager)getSystemService(AddLocationActivity.INPUT_METHOD_SERVICE);  
				imm.hideSoftInputFromWindow(mInputLoction.getWindowToken(), 0);
				finish();
			}
        });
        //取消日程添加
        mCancel=(Button)findViewById(R.id.cancel_addlocation);
        mCancel.setOnClickListener(new View.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		setResult(Activity.RESULT_CANCELED,null);
        		finish();
				
			}
        });
	}
	
	//重写LoaderCallbacks
	private LoaderCallbacks<Cursor> LocationLoader = new LoaderCallbacks<Cursor>() {
		// 创建一个新的CursorLoader对象
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Uri uri = ContentData.LocationTableData.CONTENT_URI;
			return new CursorLoader(AddLocationActivity.this, uri, null, null,
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
						.getColumnIndex(ContentData.LocationTableData.NAME));
				map.put(ContentData.LocationTableData.NAME, name);
				list.add(map);
			}
			// 新建一个适配器对象
			MyAdapter locationAdapter = new MyAdapter();
			locationAdapter.setList(list);
			mLocations.setAdapter(locationAdapter);
			locationAdapter.notifyDataSetChanged();
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
				v = LayoutInflater.from(AddLocationActivity.this).inflate(
						R.layout.item_location_layout, null);
			} else {
				v = convertView;
			}
			// 得到相关空间
			RadioButton location_item = (RadioButton) v
					.findViewById(R.id.location_item);
			// 设置控件信息
			final int pos = position;
			location_item.setText(list.get(position).get(
					ContentData.LocationTableData.NAME));
			location_item
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if(isChecked==true)
								mCurrentLocation=pos;
						}
					});
			return v;
		}
	}
}
