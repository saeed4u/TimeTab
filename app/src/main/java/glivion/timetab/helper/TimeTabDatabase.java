package glivion.timetab.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class TimeTabDatabase {

	public static final String TAG = TimeTabDatabase.class.getSimpleName();

	private static final String DELETE_ALL = "DELETE * FROM ";
	private TimeTabDatabaseHelper mDatabaseHelper;
	private SQLiteDatabase mDatabase;
	private Context mContext;
	private Cursor mCursor = null;
	private ContentValues mContentValues;

	public TimeTabDatabase(Context context) {
		mContext = context;
		mDatabaseHelper = new TimeTabDatabaseHelper(mContext);
		open();
	}

	private void open() throws SQLException {
		mDatabase = mDatabaseHelper.getWritableDatabase();
	}

	public void close() {
		mDatabase.close();
	}

	public void deleteTables(String tableName) {
		if (!tableName.isEmpty()) {
			mDatabase.execSQL(DELETE_ALL + tableName);
		}
	}

	public void deleteTableRow(String tableName, String tableId, int rowIndex) {
		if (!tableName.isEmpty() && !tableId.isEmpty()) {
			mDatabase.delete(tableName, tableId + "=" + rowIndex, null);
		}
	}

	public void createTimeTable(String tableName, String key, String value) {
		mContentValues = new ContentValues();
		mContentValues.put(key, value);
		mDatabase.insert(tableName, null, mContentValues);
	}

	public String[] getTTInfo(String tableName, String name) {
		String[] timeTableInfoList = null;
		String[] columns = { name };
		try {
			mCursor = mDatabase.query(tableName, columns, null, null, null,
					null, null);
			if (mCursor != null) {
				timeTableInfoList = new String[mCursor.getCount()];
				mCursor.moveToFirst();
				while (!mCursor.isAfterLast()) {
					for (int i = 0; i < mCursor.getCount(); i++) {
						timeTableInfoList[i] = mCursor.getString(0);
					}
				}
			}
		} catch (SQLException e) {
			Log.v(TAG, "Excepttion ", e);
			return null;
		}

		return timeTableInfoList;
	}

	public void updateTimeTable(int id, String tableName, String name,
			String key) {
		int timeTableSize = getTTInfo(tableName, name).length;
		if (!(id > timeTableSize)) {
			ContentValues values = new ContentValues();
			values.put(key, name);
			mDatabase.update(tableName, values,
					TimeTabConstants.TIMETABLE_ID.toString() + "=" + id, null);
		} else {
			Log.v(TAG, "ID specified wasnt found");
		}
	}

	public List<String> getTTName(String tableName, String name) {
		List<String> timeTableIdList = new ArrayList<String>();
		String[] columns = { name };
		try {
			mCursor = mDatabase.query(tableName, columns, null, null, null,
					null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				while (!mCursor.isAfterLast()) {
					for (int i = 0; i < mCursor.getCount(); i++) {
						timeTableIdList.add(mCursor.getString(0));
					}
				}
			}
		} catch (SQLException e) {
			Log.v(TAG, "Excepttion ", e);
			return null;
		}

		return timeTableIdList;
	}

	public void getCourses() {
		List<String> coursesId = new ArrayList<String>();
		List<String> courses = new ArrayList<String>();
		String[] columns = { TimeTabConstants.COURSE_NAME.toString(),
				TimeTabConstants.COURSE_TIMETABLE_ID.toString() };
		mCursor = mDatabase.query(TimeTabConstants.COURSE.toString(), columns,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			while (!mCursor.isAfterLast()) {
				for (int i = 0; i < mCursor.getCount(); i++) {
					courses.add(mCursor.getString(mCursor
							.getColumnIndex(TimeTabConstants.COURSE_NAME
									.toString())));
					coursesId
							.add(mCursor.getString(mCursor
									.getColumnIndex(TimeTabConstants.COURSE_TIMETABLE_ID
											.toString())));
					mCursor.moveToNext();
				}
			}
		}
		Log.d(TAG, courses.toString());
	}

	private boolean insertIntoCourseTable(String courseName, int courseId) {
		boolean successfull = false;
		mContentValues = new ContentValues();
		mContentValues.put(TimeTabConstants.COURSE_NAME.toString(), courseName);
		mContentValues.put(TimeTabConstants.COURSE_TIMETABLE_ID.toString(),
				courseId);

		long rowId = mDatabase.insert(TimeTabConstants.COURSE.toString(), null,
				mContentValues);
		if (rowId != -1) {
			successfull = true;
			return successfull;
		}
		return successfull;

	}

	public void getCourseData(List<String> courseName, List<Integer> courseId) {
		String cName;
		int courseID;
		for (int i = 0; i < courseName.size(); i++) {
			cName = courseName.get(i);
			courseID = courseId.get(i);
			if (!insertIntoCourseTable(cName, courseID)) {
				break;
			} else {
				Toast.makeText(mContext, "Inserted", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
