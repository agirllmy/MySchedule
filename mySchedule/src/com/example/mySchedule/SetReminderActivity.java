package com.example.mySchedule;

import com.example.myschedule.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

public class SetReminderActivity extends Activity {
	private RadioButton no_remind, timely_remind, head_of_5min, head_of_15min,
			head_of_30min, head_of_1hour;
	private Button reminder_complete;
	private String reminderMode = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setreminder);
		// 绑定RadioButton按钮
		no_remind = (RadioButton) findViewById(R.id.no_remind);
		timely_remind = (RadioButton) findViewById(R.id.timely_remind);
		head_of_5min = (RadioButton) findViewById(R.id.head_of_5min);
		head_of_15min = (RadioButton) findViewById(R.id.head_of_15min);
		head_of_30min = (RadioButton) findViewById(R.id.head_of_30min);
		head_of_1hour = (RadioButton) findViewById(R.id.head_of_1hour);
		no_remind.setChecked(true);
		reminder_complete = (Button) findViewById(R.id.reminder_complete);
		reminder_complete.setOnClickListener(new ReminderCompleteListener());
	}

	// 获取提醒模式
	public int getReminder() {
		int reminder = 1;
		
		if (no_remind.isChecked() == true) {
			reminder = 1;
			reminderMode = "不提醒";
		} else if (timely_remind.isChecked() == true) {
			reminder = 2;
			reminderMode = "准时提醒";
		} else if (head_of_5min.isChecked() == true) {
			reminder = 3;
			reminderMode = "提前5分钟提醒";
		} else if (head_of_15min.isChecked() == true) {
			reminder = 4;
			reminderMode = "提前15分钟提醒";
		} else if (head_of_30min.isChecked() == true) {
			reminder = 5;
			reminderMode = "提前30分钟提醒";
		} else if (head_of_1hour.isChecked() == true) {
			reminder = 6;
			reminderMode = "提前一个小时提醒";
		}
		return reminder;
	}

	private class ReminderCompleteListener implements View.OnClickListener {
		public void onClick(View v) {
			int choseNum = getReminder();
			Intent data = new Intent();
			data.putExtra("reminderNum", choseNum);
			data.putExtra("reminderMode", reminderMode);
			// 请求代码可以自己设置，这里设置成20
			setResult(2, data);
			// 关闭掉这个Activity
			finish();
		}
	}
}
