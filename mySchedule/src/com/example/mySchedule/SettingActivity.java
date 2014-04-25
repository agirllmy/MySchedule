package com.example.mySchedule;

import java.util.ArrayList;

import com.example.myschedule.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends Activity {
	private ListView mSettings;
	private ArrayList<String> mSettingMenu = new ArrayList<String>();
	private LinearLayout mPasswordSetting;
	private Switch mProtectedSetting;
	private String mPassword = "";
	private LinearLayout mCurrentPasswd, mNewPasswd, mSurePasswd;
	private EditText mEdit1, mEdit2, mEdit3;
	private SharedPreferences mSharedPreferences;
	private Editor mEditor;
	private boolean mIsProtected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_layout);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_setting_layout, R.id.setting_item, mSettingMenu);
		mSharedPreferences = getSharedPreferences("mSharedPreferences",
				MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
        
		if(!mSharedPreferences.getString("PASSWORD", "").equals("")){
			mIsProtected=true;
			mPassword= mSharedPreferences.getString("PASSWORD", "");
		}
		
		mSettingMenu.add("密码设置");
		mSettingMenu.add("绑定微博");
		mSettingMenu.add("界面显示");
		mSettingMenu.add("备份");
		mSettingMenu.add("关于");

		mSettings = (ListView) findViewById(R.id.settings);
		mSettings.setAdapter((ListAdapter) adapter);

		mSettings.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (mSettingMenu.get(position).equals("密码设置")) {
					mPasswordSetting = (LinearLayout) getLayoutInflater()
							.inflate(R.layout.password_setting_layout, null);
					mProtectedSetting = (Switch) mPasswordSetting
							.findViewById(R.id.switch1);

					mCurrentPasswd = (LinearLayout) mPasswordSetting
							.findViewById(R.id.oldpassword);
					mNewPasswd = (LinearLayout) mPasswordSetting
							.findViewById(R.id.newpassword);
					mSurePasswd = (LinearLayout) mPasswordSetting
							.findViewById(R.id.surepassword);
					mEdit1 = (EditText) mPasswordSetting
							.findViewById(R.id.mEdit1);
					//mEdit1.setText(null);
					mEdit2 = (EditText) mPasswordSetting
							.findViewById(R.id.mEdit2);
					mEdit3 = (EditText) mPasswordSetting
							.findViewById(R.id.mEdit3);
					mProtectedSetting
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {
									if (isChecked) {
										mIsProtected = isChecked;
										if (mPassword == "") {
											mNewPasswd
													.setVisibility(View.VISIBLE);
											mSurePasswd
													.setVisibility(View.VISIBLE);

										} else {
											mCurrentPasswd
													.setVisibility(View.VISIBLE);
											mNewPasswd
													.setVisibility(View.VISIBLE);
											mSurePasswd
													.setVisibility(View.VISIBLE);
										}
									} else {
										//关闭密码设置
										mCurrentPasswd.setVisibility(View.GONE);
										mNewPasswd.setVisibility(View.GONE);
										mSurePasswd.setVisibility(View.GONE);
										mIsProtected=false;
									}

								}
							});
					if (mIsProtected) {
						mProtectedSetting.setChecked(true);
					} else {
						mProtectedSetting.setChecked(false);
					}
					new AlertDialog.Builder(SettingActivity.this)
							.setTitle("密码设置").setView(mPasswordSetting)
							.setPositiveButton("确定", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 点击确定之后的各种不同的反应
									// 当前密码值为空时
									if(!mIsProtected){
										mEditor.putString("PASSWORD", "");
										mEditor.commit();
										Toast.makeText(SettingActivity.this, "已取消密码设置",  Toast.LENGTH_SHORT).show();
										return;
									}
									if(mPassword=="")
									{
										//1.密码输入为空
										if(mEdit2.getText().toString().equals("")){
											Toast.makeText(SettingActivity.this, "设置了一个空密码",Toast.LENGTH_SHORT).show();
										}
										else{
											//2.两次密码输入一致
											if(mEdit2.getText().toString().equals(mEdit3.getText().toString())){
												mPassword=mEdit2.getText().toString();
												mEditor.putString("PASSWORD", mPassword);
												mEditor.commit();
												 Log.d("mSharedPreferences", mSharedPreferences.getString("STRING_KEY", "none"));
												Toast.makeText(SettingActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
											}
											//3.两次输入的密码不一致
											else
											{
												Toast.makeText(SettingActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
											}
										}
									}
									//当前密码不为空时
									else{
										//4.如果没有输入旧密码
										if(mEdit1.getText().toString().equals(""))
											Toast.makeText(SettingActivity.this, "请先输入旧密码", Toast.LENGTH_SHORT).show();
										else{
											//5.旧密码输入正确
											if(mEdit1.getText().toString().equals(mPassword)){
												//5-1.新密码设置为空
												if(mEdit2.getText().toString().equals("")){
													Toast.makeText(SettingActivity.this, "设置了一个空密码",Toast.LENGTH_SHORT).show();
												}
												else{
													//5-2.两次密码输入一致
													if(mEdit2.getText().toString().equals(mEdit3.getText().toString())){
														mPassword=mEdit2.getText().toString();
														mEditor.putString("PASSWORD", mPassword);
														mEditor.commit();
														Toast.makeText(SettingActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
													}
													//5-3.两次输入的密码不一致
													else
													{
														Toast.makeText(SettingActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
													}
												}
											}
											//6.旧密码输入错误
											else
											{
												Toast.makeText(SettingActivity.this, "旧密码输入错误", Toast.LENGTH_SHORT).show();
											}
										}
									}
								}
							}).setNegativeButton("取消", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).create().show();
				}
			}
		});
	}
}
