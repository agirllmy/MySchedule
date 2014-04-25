package com.example.mySchedule;

import com.example.myschedule.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;

public class RepeatActivity extends Activity {
    private static RadioButton no_repeat,dayly_repeat,weekly_repeat,month_repeat,year_repeat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repeat_layout);
		no_repeat = (RadioButton) findViewById(R.id.no_repeat);
		dayly_repeat = (RadioButton) findViewById(R.id.dayly_repeat);
		weekly_repeat = (RadioButton) findViewById(R.id.weekly_repeat);
		month_repeat = (RadioButton) findViewById(R.id.month_repeat);
		year_repeat = (RadioButton) findViewById(R.id.year_repeat);
	}
	// 获取提醒模式
	public static int getRepeat(){
		int repeat=0;
		if(no_repeat.isChecked()==true)
			repeat=1;
		else if(dayly_repeat.isChecked()==true)
			repeat=2;
		else if(weekly_repeat.isChecked()==true)
			repeat=3;
		else if(month_repeat.isChecked()==true)
			repeat=4;
		else if(year_repeat.isChecked()==true)
			repeat=5;
		return repeat;
	}
}
