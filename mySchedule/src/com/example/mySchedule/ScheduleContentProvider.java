package com.example.mySchedule;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class ScheduleContentProvider extends ContentProvider {
	private DBOpenHelper dbOpenHelper = null;

	@Override
	public boolean onCreate() {
		dbOpenHelper = new DBOpenHelper(this.getContext(),
				ContentData.DATABASE_NAME, ContentData.DATABASE_VERSION);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String table = null;
		switch (ContentData.uriMatcher.match(uri)) {
		case ContentData.SCHEDULES:
			table = ContentData.USERS_TABLE_NAME;
			break;

		case ContentData.SCHEDULE:
			table = ContentData.USERS_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");// 把其它条件附加上
			break;

		case ContentData.LABELS:
			table = ContentData.LABEL_TABLE_NAME;
			break;

		case ContentData.LABEL:
			table = ContentData.LABEL_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");// 把其它条件附加上
			break;
		case ContentData.PROGRAMS:
			table = ContentData.PROGRAM_TABLE_NAME;
			break;

		case ContentData.PROGRAM:
			table = ContentData.PROGRAM_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");// 把其它条件附加上
			break;
		case ContentData.TIMERS:
			table = ContentData.TIMER_TABLE_NAME;
			break;

		case ContentData.TIMER:
			table = ContentData.TIMER_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");// 把其它条件附加上
			break;
		case ContentData.LOCATIONS:
			table=ContentData.LOCATION_TABLE_NAME;
			break;
		case ContentData.LOCATION:
			table=ContentData.LOCATION_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");// 把其它条件附加上
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		return db.query(table, projection, selection, selectionArgs, null,
				null, sortOrder);
	}

	@Override
	public String getType(Uri uri) {
		switch (ContentData.uriMatcher.match(uri)) {
		case ContentData.SCHEDULES:
			return ContentData.UserTableData.CONTENT_TYPE;
		case ContentData.SCHEDULE:
			return ContentData.UserTableData.CONTENT_TYPE_ITME;
		case ContentData.LABELS:
			return ContentData.LabelTableData.CONTENT_TYPE;
		case ContentData.LABEL:
			return ContentData.LabelTableData.CONTENT_TYPE_ITME;
		case ContentData.PROGRAMS:
			return ContentData.ProgramTableData.CONTENT_TYPE;
		case ContentData.PROGRAM:
			return ContentData.ProgramTableData.CONTENT_TYPE_ITME;
		case ContentData.TIMERS:
			return ContentData.TimerTableData.CONTENT_TYPE;
		case ContentData.TIMER:
			return ContentData.TimerTableData.CONTENT_TYPE_ITME;
		case ContentData.LOCATIONS:
			return ContentData.LocationTableData.CONTENT_TYPE;
		case ContentData.LOCATION:
			return ContentData.LocationTableData.CONTENT_TYPE_ITME;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Uri result = null;
		long id = 0;
		String table;
		String path = uri.toString();
		switch (ContentData.uriMatcher.match(uri)) {
		case ContentData.SCHEDULES:case ContentData.SCHEDULE:
			table = ContentData.USERS_TABLE_NAME;
			break;
		
		case ContentData.LABELS:case ContentData.LABEL:
			table = ContentData.LABEL_TABLE_NAME;
			break;
			
		case ContentData.PROGRAMS:case ContentData.PROGRAM:
			table=ContentData.PROGRAM_TABLE_NAME;
			break;

		case ContentData.TIMERS:case ContentData.TIMER:
			table=ContentData.TIMER_TABLE_NAME;
			break;
		case ContentData.LOCATIONS:case ContentData.LOCATION:
			table=ContentData.LOCATION_TABLE_NAME;
			break;
		
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		id=db.insert(table, null, values);
		if(path!=null){
			result= Uri.parse(path.substring(0, path.lastIndexOf("/")) + id);
		}
		else
			result= ContentUris.withAppendedId(uri, id);
		return result;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		String table;
		switch (ContentData.uriMatcher.match(uri)) {
		case ContentData.SCHEDULES:
			table = ContentData.USERS_TABLE_NAME;
			break;

		case ContentData.SCHEDULE:
			table = ContentData.USERS_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");
			break;

		case ContentData.LABELS:
			table = ContentData.LABEL_TABLE_NAME;
			break;

		case ContentData.LABEL:
			table = ContentData.LABEL_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");
			break;
		case ContentData.PROGRAMS:
			table = ContentData.PROGRAM_TABLE_NAME;
			break;

		case ContentData.PROGRAM:
			table = ContentData.PROGRAM_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");
			break;
		case ContentData.TIMERS:
			table = ContentData.TIMER_TABLE_NAME;
			break;

		case ContentData.TIMER:
			table = ContentData.TIMER_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");
			break;
		case ContentData.LOCATIONS:
			table = ContentData.LOCATION_TABLE_NAME;
			break;

		case ContentData.LOCATION:
			table = ContentData.LOCATION_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return db.delete(table, selection, selectionArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		String table;
		switch (ContentData.uriMatcher.match(uri)) {
		case ContentData.SCHEDULES:
			table = ContentData.USERS_TABLE_NAME;
			break;

		case ContentData.SCHEDULE:
			table = ContentData.USERS_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");
			break;

		case ContentData.LABELS:
			table = ContentData.LABEL_TABLE_NAME;
			break;
		case ContentData.LABEL:
			table = ContentData.LABEL_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");
			break;
		case ContentData.PROGRAMS:
			table = ContentData.PROGRAM_TABLE_NAME;
			break;
		case ContentData.PROGRAM:
			table = ContentData.PROGRAM_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");
			break;
		case ContentData.TIMERS:
			table = ContentData.TIMER_TABLE_NAME;
			break;
		case ContentData.TIMER:
			table = ContentData.TIMER_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");
			break;
		case ContentData.LOCATIONS:
			table = ContentData.LOCATION_TABLE_NAME;
			break;
		case ContentData.LOCATION:
			table = ContentData.LOCATION_TABLE_NAME;
			selection = "_ID="
					+ ContentUris.parseId(uri)
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection
							+ ")" : "");
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return db.update(table, values, selection, selectionArgs);
	}

}
