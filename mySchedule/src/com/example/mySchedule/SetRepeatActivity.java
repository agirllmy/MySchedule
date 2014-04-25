package com.example.mySchedule;

import com.example.myschedule.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class SetRepeatActivity extends Activity {
	private RadioButton no_repeat, dayly_repeat, weekly_repeat, month_repeat,
			year_repeat;
	private Button repeat_complete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repeat_layout);
		no_repeat = (RadioButton) findViewById(R.id.no_repeat);
		no_repeat.setClickable(true);
		dayly_repeat = (RadioButton) findViewById(R.id.dayly_repeat);
		weekly_repeat = (RadioButton) findViewById(R.id.weekly_repeat);
		month_repeat = (RadioButton) findViewById(R.id.month_repeat);
		year_repeat = (RadioButton) findViewById(R.id.year_repeat);
		repeat_complete = (Button) findViewById(R.id.repeate_complete);
		repeat_complete.setOnClickListener(new RepeatCompleteListener());

	}

	// 获取提醒模式
	public int getRepeat() {
		int repeat = 1;
		if (no_repeat.isChecked() == true)
			repeat = 1;
		else if (dayly_repeat.isChecked() == true)
			repeat = 2;
		else if (weekly_repeat.isChecked() == true)
			repeat = 3;
		else if (month_repeat.isChecked() == true)
			repeat = 4;
		else if (year_repeat.isChecked() == true)
			repeat = 5;
		return repeat;
	}

	private class RepeatCompleteListener implements View.OnClickListener {
		public void onClick(View v) {
			int choseNum = getRepeat();

			// 判断空，我就不判断了。。。。
			Intent data = new Intent();
			data.putExtra("repeatNum", choseNum);
			// 请求代码可以自己设置，这里设置成20
			setResult(20, data);
			// 关闭掉这个Activity
			finish();
		}
	}
}
