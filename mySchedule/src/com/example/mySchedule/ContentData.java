package com.example.mySchedule;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * 数据库的数据结构
 * 
 * @author limy
 * 
 */
public class ContentData {
	public static final String AUTHORITY = "com.example.mySchedule";
	public static final String DATABASE_NAME = "schedule.db";
	public static final int DATABASE_VERSION = 4;
	public static final String USERS_TABLE_NAME = "schedule";
	public static final String LABEL_TABLE_NAME = "label";
	public static final String PROGRAM_TABLE_NAME = "program";
	public static final String TIMER_TABLE_NAME="timer";
	public static final String LOCATION_TABLE_NAME="location";

	public static final int SCHEDULES = 1;
	public static final int SCHEDULE = 2;
	public static final int LABELS = 3;
	public static final int LABEL = 4;
	public static final int PROGRAMS = 5;
	public static final int PROGRAM = 6;
	public static final int TIMERS = 7;
	public static final int TIMER = 8;
	public static final int LOCATIONS = 9;
	public static final int LOCATION = 10;
	public static final UriMatcher uriMatcher;
	static {
		// 常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ContentData.AUTHORITY, "schedule", SCHEDULES);
		uriMatcher.addURI(ContentData.AUTHORITY, "schedule/#", SCHEDULE);
		uriMatcher.addURI(ContentData.AUTHORITY, "label", LABELS);
		uriMatcher.addURI(ContentData.AUTHORITY, "label/#", LABEL);
		uriMatcher.addURI(ContentData.AUTHORITY, "program", PROGRAMS);
		uriMatcher.addURI(ContentData.AUTHORITY, "program/#", PROGRAM);
		uriMatcher.addURI(ContentData.AUTHORITY, "timer", TIMERS);
		uriMatcher.addURI(ContentData.AUTHORITY, "timer/#", TIMER);
		uriMatcher.addURI(ContentData.AUTHORITY, "location", LOCATIONS);
		uriMatcher.addURI(ContentData.AUTHORITY, "location/#", LOCATION);
	}

	public static final class UserTableData {
		public static final String _ID = "_id";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/schedule");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.example.mySchedule.schedules";// 数据集类型
		public static final String CONTENT_TYPE_ITME = "vnd.android.cursor.item/com.example.mySchedule.shcedule";// 单个数据类型

		// 列名
		public static final String TITTLE = "tittle";// 待办事项
		public static final String IMPORTANCE = "importance";// 重要程度
		public static final String STARTDATE = "start_date";// 开始日期
		public static final String ENDDATE = "end_date";// 结束日期
		public static final String STARTTIME = "start_time";// 开始时间
		public static final String ENDTIME = "end_time";// 结束时间
		public static final String REMINDER = "reminder";// 提醒
		public static final String REPEAT = "repeat";// 提醒
		public static final String PROJECT = "project";// 项目
		public static final String LABEL = "label";// 标签
		public static final String LOACTION = "location";// 位置
		public static final String SUBTASK = "subtask";// 子任务
		public static final String NOTE = "note";// 备注

	}

	public static final class LabelTableData {
		public static final String _ID = "_id";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/label");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.example.label.labels";// 数据集类型
		public static final String CONTENT_TYPE_ITME = "vnd.android.cursor.item/com.example.label.label";// 单个数据类型

		// 列名
		public static final String NAME = "label_name";// 标签名
	}

	public static final class ProgramTableData {
		public static final String _ID = "_id";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/program");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.example.program.programs";// 数据集类型
		public static final String CONTENT_TYPE_ITME = "vnd.android.cursor.item/com.example.program.program";// 单个数据类型

		// 列名
		public static final String NAME = "program_name";// 项目名
	}

	public static final class TimerTableData {
		public static final String _ID = "_id";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/timer");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.example.timer.timers";// 数据集类型
		public static final String CONTENT_TYPE_ITME = "vnd.android.cursor.item/com.example.timer.timer";// 单个数据类型

		// 列名
		public static final String TASK = "task";// 任务名
		public static final String TIME = "time";// 所用时长
	}
	public static final class LocationTableData{
		public static final String _ID = "_id";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/location");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.example.location.locations";// 数据集类型
		public static final String CONTENT_TYPE_ITME = "vnd.android.cursor.item/com.example.location.location";// 单个数据类型

		// 列名
		public static final String NAME = "location_name";// 项目名
	}

}
