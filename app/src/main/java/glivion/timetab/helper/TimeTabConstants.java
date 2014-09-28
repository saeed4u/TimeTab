package glivion.timetab.helper;

public enum TimeTabConstants {
	// Database constants
	DATABASE_NAME("Timetab"), TIMETABLE("Timetable"), DAYS("Days"), COURSE(
			"Course"), COURSE_SCHEDULER("Course Sch"), EXAMS("Exams"), TASKS(
			"Tasks"),

	DATABASE_VERSION(1),

	TAG("Database"),

	TIMETABLE_ID("_id"), TIMETABLE_NAME("tt_name"),

	DAY_ID("_id"), DAY_NAME("day_name"), DAY_ENABLED("day_enabled"),

	COURSE_ID("_id"), COURSE_NAME("co_name"), COURSE_LECTURER("lecturer"), COURSE_COLOR(
			"color"), COURSE_TIMETABLE_ID("tt_id"),

	COURSE_SCH_ID("_id"), COURSE_SCH_COURSE("c_id"), COURSE_SCH_LOC("location"), COURSE_SCH_DAY_ID(
			"day_id"), COURSE_SCH_ST_HR("starthr"), COURSE_SCH_ST_MIN(
			"startmin"), COURSE_END_HR("endhr"), COURSE_SCH_NOTE("notes"),

	EXAMS_ID("_id"), EXAMS_COURSE("course_id"), EXAM_DATE("date"), EXAM_PLACE(
			"place"), EXAM_NOTE("note"), EXAM_TIME("time"),

	TASK_ID("_id"), TASK_COURSE("course_id"), TASK_DATE("date"), TASK_TITLE(
			"title"), TASK_NOTE("note"), TASK_STATE("state"), TASK_TIMETABLE_ID(
			"timetable_id"),

	ID("id"), NAME("name"), COLOR("color"), DATE("date"), PLACE("place"), NOTES(
			"notes"), TITLE("title"), STATUS("status"), TIME("time"),

	// API CONSTANTS
	MAIN_URL("http://timetab.glivion.com/ttapi/"), LEVEL("levelID="), SEMESTER(
			"semesterID="), INSTITUE("getInstitutes"), PROGAMS_INSTITUTE(
			"getPrograms&instituteID="), LEVELS_INSTITUTE(
			"getLevels&instituteID="), SEMESTER_INSTITUTE(
			"getSemesters&instituteID="), ACTION("?action="), COURSE_PROGRAMME(
			"getCourses&programID="), COURSESCHEDULE_PROGRAMME(
			"getCourseSchedules&programID="), LEVELID("&levelID="), SEMESTERID(
			"&semesterID="),

	// JSON DATA CONSTANTS
	SUCCESS("success"), INSITUTES("institutes"), INSTITUTENAME("institute_name"), INSTITUTEID(
			"instituteID"), PROGRAMS("programs"), PROGRAMNAME("program_name"), LEVELS(
			"levels"), LEVELNAME("level_name"), SEMESTERS("semesters"), SEMESTERNAME(
			"semester_name"), COURSES("courses"), COURSENAME("course_name"), COURSEID(
			"courseID"), COURSESCHEDULES("courseschedules"), DAYID("dayID"), STARTHOUR(
			"starthour"), ENDHOUR("endhour"), STARTMIN("startmin"), ENDMIN(
			"endmin"), LOCATION("location");

	private String databaseAndSereverConString;
	private int dbVersion;

	private TimeTabConstants(String databaseAndSereverConString) {
		this.databaseAndSereverConString = databaseAndSereverConString;
	}

	private TimeTabConstants(int dbVersion) {
		this.dbVersion = dbVersion;
	}

	public int getDBVersion() {
		return dbVersion;
	}

	@Override
	public String toString() {
		return databaseAndSereverConString;
	}
}
