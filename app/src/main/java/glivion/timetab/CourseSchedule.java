package glivion.timetab;

public class CourseSchedule {

	public static final String TAG = CourseSchedule.class.getSimpleName();

	// private member variables;
	private String mCourseName;
	private String mCourseLecturerName;
	private String mCourseScheduleLocation;
	private long mScheduleDuration;
	private int mScheduleColor;

	/**
	 * The constructor for the class.
	 * 
	 * @param courseName
	 *            The course to be scheduled
	 * @param lecturerName
	 *            The lecturer of the course
	 * @param scheduleLocation
	 *            The location of the schedule
	 * @param scheduleDuration
	 *            How long will the schedule take
	 * @param color
	 *            The color identify of the course
	 */
	public CourseSchedule(String courseName, String lecturerName,
			String scheduleLocation, long scheduleDuration, int color) {
		mCourseName = courseName;
		mCourseLecturerName = lecturerName;
		mCourseScheduleLocation = scheduleLocation;
		mScheduleDuration = scheduleDuration;
		mScheduleColor = color;
	}

	// getters and setters of this class
	/**
	 * Get the course name and return it to the caller
	 * 
	 * @return The course name of the schedule
	 */
	public String getCourseName() {
		return mCourseName;
	}

	/**
	 * Get the course lecturer and return it to the caller
	 * 
	 * @return The course lecturer
	 */
	public String getCourseLecturer() {
		return mCourseLecturerName;
	}

	/**
	 * Get the course location and return it to the caller
	 * 
	 * @return The location of a particular schedule
	 */
	public String getCourseLocation() {
		return mCourseScheduleLocation;
	}

	/**
	 * Get the course schedule duration and return it to the caller
	 * 
	 * @return The duration of a particular schedule
	 */
	public long getScheduleDuration() {
		return mScheduleDuration;
	}

	/**
	 * Get the course identify and return it to the caller
	 * 
	 * @return The color identify of the course
	 */
	public int getScheduleColor() {
		return mScheduleColor;
	}
}
