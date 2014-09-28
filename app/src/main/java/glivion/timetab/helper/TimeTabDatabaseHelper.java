package glivion.timetab.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimeTabDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ TimeTabConstants.TIMETABLE.toString() + " (" + TimeTabConstants.TIMETABLE_ID
			+ " integer primary key autoincrement, "
			+ TimeTabConstants.TIMETABLE_NAME + " text not null );";

	private static final String DAY_CREATE = "CREATE TABLE if not exists "
			+ TimeTabConstants.DAYS.toString() + " (" + TimeTabConstants.DAY_ID
			+ " integer primary key autoincrement, "
			+ TimeTabConstants.DAY_NAME.toString() + " text not null, "
			+ TimeTabConstants.DAY_ENABLED.toString() + "integer not null );";

	private static final String COURSE_CREATE = "CREATE TABLE if not exists "
			+ TimeTabConstants.COURSE.toString() + " (" + TimeTabConstants.COURSE_ID
			+ " integer primary key autoincrement, "
			+ TimeTabConstants.COURSE_NAME.toString() + " text, "
			+ TimeTabConstants.COURSE_LECTURER.toString() + " text, "
			+ TimeTabConstants.COURSE_COLOR.toString() + " integer, "
			+ TimeTabConstants.COURSE_TIMETABLE_ID.toString() + " integer );";

	private static final String EXAMS_CREATE = "CREATE TABLE if not exists "
			+ TimeTabConstants.EXAMS.toString() + " (" + TimeTabConstants.EXAMS_ID
			+ " integer primary key autoincrement, "
			+ TimeTabConstants.EXAMS_COURSE.toString() + " text not null, "
			+ TimeTabConstants.EXAM_PLACE.toString() + " text, "
			+ TimeTabConstants.EXAM_DATE.toString() + " long, "
			+ TimeTabConstants.EXAM_TIME.toString() + " long, "
			+ TimeTabConstants.EXAM_NOTE.toString() + " text);";
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
	private static final String TASKS_CREATE = "CREATE TABLE if not exists "
			+ TimeTabConstants.TASKS.toString() + " (" + TimeTabConstants.TASK_ID
			+ " integer primary key autoincrement, "
			+ TimeTabConstants.TASK_TITLE.toString() + " text not null, "
			+ TimeTabConstants.TASK_COURSE.toString() + " text not null, "
			+ TimeTabConstants.TASK_DATE.toString() + " long, "
			+ TimeTabConstants.TASK_NOTE.toString() + " text not null, "
			+ TimeTabConstants.TASK_STATE.toString() + " long, "
			+ TimeTabConstants.TASK_TIMETABLE_ID.toString() + " long);";

	TimeTabDatabaseHelper(Context context) {
		super(context, TimeTabConstants.DATABASE_NAME.toString(), null,
				TimeTabConstants.DATABASE_VERSION.getDBVersion());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		db.execSQL(DAY_CREATE);
		db.execSQL(COURSE_CREATE);
		db.execSQL(EXAMS_CREATE);
		db.execSQL(TASKS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion) {
		db.execSQL(DROP_TABLE + TimeTabConstants.TIMETABLE.toString());
		db.execSQL(DROP_TABLE + TimeTabConstants.DAYS.toString());
		db.execSQL(DROP_TABLE + TimeTabConstants.COURSE.toString());
		db.execSQL(DROP_TABLE + TimeTabConstants.EXAMS.toString());
		db.execSQL(DROP_TABLE + TimeTabConstants.TASKS.toString());
		onCreate(db);
	}
}
