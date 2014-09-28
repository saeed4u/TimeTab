package glivion.timetab.ui;

import glivion.timetab.R;
import glivion.timetab.helper.TimeTabConstants;
import glivion.timetab.helper.TimeTabDatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SyncActivity extends FragmentActivity {

	private Spinner mInstituteSpinner;
	private Spinner mProgrammeSpinner;
	private Spinner mLevelSpinner;
	private Spinner mSemesterSpinner;

	private int mProgrammeSpinnerSelectedIndex;
	private int mLevelSpinnerSelectedIndex;
	private int mSemesterSpinnerSelectedIndex;
	private int mInstituteSpinnerSelectedIndex;

	private TimeTabDatabase mTTDatabse;

	private MenuItem mSyncMenuItem;

	public void setmInstituteSpinnerSelectedIndex(
			int mInstituteSpinnerSelectedIndex) {
		this.mInstituteSpinnerSelectedIndex = mInstituteSpinnerSelectedIndex;
	}

	public void setmProgrammeSpinnerSelectedIndex(
			int mProgrammeSpinnerSelectedIndex) {
		this.mProgrammeSpinnerSelectedIndex = mProgrammeSpinnerSelectedIndex;
	}

	public void setmLevelSpinnerSelectedIndex(int mLevelSpinnerSelectedIndex) {
		this.mLevelSpinnerSelectedIndex = mLevelSpinnerSelectedIndex;
	}

	public void setmSemesterSpinnerSelectedIndex(
			int mSemesterSpinnerSelectedIndex) {
		this.mSemesterSpinnerSelectedIndex = mSemesterSpinnerSelectedIndex;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_sync);

		mTTDatabse = new TimeTabDatabase(this);
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);

		setProgressBarIndeterminateVisibility(true);
		new ServerConnector(this);

		mInstituteSpinner = (Spinner) findViewById(R.id.institute_spinner);
		mInstituteSpinner.setVisibility(View.INVISIBLE);
		mInstituteSpinner.setOnItemSelectedListener(mInstitueItemSelected);
		mProgrammeSpinner = (Spinner) findViewById(R.id.institute_programme);
		mProgrammeSpinner.setVisibility(View.INVISIBLE);
		mProgrammeSpinner.setOnItemSelectedListener(mProgrammeItemSelected);
		mLevelSpinner = (Spinner) findViewById(R.id.institute_level);
		mLevelSpinner.setVisibility(View.INVISIBLE);
		mLevelSpinner.setOnItemSelectedListener(mLevelItemSelected);
		mSemesterSpinner = (Spinner) findViewById(R.id.institute_semester);
		mSemesterSpinner.setVisibility(View.INVISIBLE);
		mSemesterSpinner.setOnItemSelectedListener(mSemesterItemSelected);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sync, menu);
		mSyncMenuItem = menu.findItem(R.id.action_sync_courses);
		mSyncMenuItem.setVisible(false);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_sync_courses:
			syncCoursewithSchedules();
			break;
		}
		return true;
	}

	private void syncCoursewithSchedules() {
		new ServerConnector(mProgrammeSpinnerSelectedIndex, SyncActivity.this,
				mLevelSpinnerSelectedIndex, mSemesterSpinnerSelectedIndex);
		new ServerConnector(this, mProgrammeSpinnerSelectedIndex,
				mLevelSpinnerSelectedIndex, mSemesterSpinnerSelectedIndex);
	}

	public void setSpinnerData(Spinner spinner, List<String> data) {
		spinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, data));
		setProgressBarIndeterminateVisibility(false);
		spinner.setVisibility(View.VISIBLE);
	}

	private OnItemSelectedListener mSemesterItemSelected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			setmSemesterSpinnerSelectedIndex(position + 1);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	private OnItemSelectedListener mInstitueItemSelected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			new ServerConnector(SyncActivity.this, position + 1);
			setmInstituteSpinnerSelectedIndex(position + 1);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	private OnItemSelectedListener mProgrammeItemSelected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			setmProgrammeSpinnerSelectedIndex(position + 1);
			mLevelSpinner.setAdapter(new ArrayAdapter<String>(
					getApplicationContext(),
					android.R.layout.simple_spinner_dropdown_item,
					new String[] { "100", "200", "300", "400" }));
			mLevelSpinner.setVisibility(View.VISIBLE);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	private OnItemSelectedListener mLevelItemSelected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			setmLevelSpinnerSelectedIndex(position + 1);
			new ServerConnector(mInstituteSpinnerSelectedIndex,
					SyncActivity.this);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	private class ServerConnector {

		public final String TAG = ServerConnector.class.getSimpleName();

		private String mInsituteQueryUrl = "";
		private String mProgrammeQueryUrl = "";
		private String mSemesterQueryUrl = "";
		private String mCourseQueryUrl = "";
		private String mCourseScheduleQueryUrl = "";
		private int mResponseCode = -1;
		private Context mContext;

		private ConnectToServerForInstitutes mConnectForInstitues;
		private ConnectToServerForProgrammes mConnectForProgrammes;
		private ConnectToServerForSemesters mConnectForSemesters;

		private JSONArray mInstituteJsonArray;
		private JSONArray mProgramJsonArray;
		private JSONArray mSemesterJsonArray;
		private JSONArray mCourseJsonArray;
		private JSONArray mCourseScheduleArray;
		private JSONObject mJsonObject;

		private static final int INSTITUTES = 1;
		private static final int PROGRAMMES = 2;
		private static final int SEMESTER = 3;
		private static final int COURSES = 4;
		private static final int COURSE_SCHEDULES = 5;

		private List<String> mProgrammes;
		private List<String> mSemesters;
		private List<String> mCourseName;
		private List<Integer> mCourseId;
		private List<String> mCourseStartHour;
		private List<String> mCourseEndHour;
		private List<String> mCourseStartMin;
		private List<String> mCourseEndMin;
		private List<String> mCourseLocation;
		private List<String> mCourseDayId;

		private void getInstituteUrl() {
			mInsituteQueryUrl = TimeTabConstants.MAIN_URL.toString()
					.concat(TimeTabConstants.ACTION.toString())
					.concat(TimeTabConstants.INSTITUE.toString());
		}

		private void getProgrammeUrl(int instituteId) {
			mProgrammeQueryUrl = TimeTabConstants.MAIN_URL.toString()
					.concat(TimeTabConstants.ACTION.toString())
					.concat(TimeTabConstants.PROGAMS_INSTITUTE.toString())
					.concat(Integer.toString(instituteId));
			Log.v("Programme URL", mProgrammeQueryUrl);
		}

		private void getSemesterUrl(int instituteId) {
			mSemesterQueryUrl = TimeTabConstants.MAIN_URL.toString()
					.concat(TimeTabConstants.ACTION.toString())
					.concat(TimeTabConstants.SEMESTER_INSTITUTE.toString())
					.concat(Integer.toString(instituteId));
			Log.v("Programme URL", mSemesterQueryUrl);
		}

		private void getCourseUrl(int programmeId, int levelId, int semesterId) {
			mCourseQueryUrl = TimeTabConstants.MAIN_URL.toString()
					.concat(TimeTabConstants.ACTION.toString())
					.concat(TimeTabConstants.COURSE_PROGRAMME.toString())
					.concat(Integer.toString(programmeId))
					.concat(TimeTabConstants.LEVELID.toString())
					.concat(Integer.toString(levelId))
					.concat(TimeTabConstants.SEMESTERID.toString())
					.concat(Integer.toString(semesterId));
			Log.v("Course URL", mCourseQueryUrl);
		}

		private void getCourseScheduleUrl(int programmeId, int levelId,
				int semesterId) {
			mCourseQueryUrl = TimeTabConstants.MAIN_URL
					.toString()
					.concat(TimeTabConstants.ACTION.toString())
					.concat(TimeTabConstants.COURSESCHEDULE_PROGRAMME
							.toString()).concat(Integer.toString(programmeId))
					.concat(TimeTabConstants.LEVELID.toString())
					.concat(Integer.toString(levelId))
					.concat(TimeTabConstants.SEMESTERID.toString())
					.concat(Integer.toString(semesterId));
		}

		public ServerConnector(Context context, int programmeId, int levelId,
				int semesterId) {
			mContext = context;
			mCourseStartHour = new ArrayList<String>();
			mCourseEndHour = new ArrayList<String>();
			mCourseStartMin = new ArrayList<String>();
			mCourseEndMin = new ArrayList<String>();
			mCourseLocation = new ArrayList<String>();
			mCourseDayId = new ArrayList<String>();
			getCourseScheduleUrl(programmeId, levelId, semesterId);
			new ConnectToServerForCourseSchedules().execute();
		}

		public ServerConnector(int programmeId, Context context, int levelId,
				int semesterId) {
			mContext = context;
			getCourseUrl(programmeId, levelId, semesterId);
			new ConnectToServerForCourses().execute();
		}

		public ServerConnector(Context context) {
			getInstituteUrl();
			mContext = context;
			mConnectForInstitues = new ConnectToServerForInstitutes();
			mConnectForInstitues.execute();

		}

		public ServerConnector(Context context, int instituteId) {
			mContext = context;
			getProgrammeUrl(instituteId);
			mProgrammes = new ArrayList<String>();
			mConnectForProgrammes = new ConnectToServerForProgrammes();
			mConnectForProgrammes.execute();
			setProgressBarIndeterminateVisibility(true);

		}

		public ServerConnector(int instituteId, Context context) {
			mContext = context;
			getSemesterUrl(instituteId);
			mSemesters = new ArrayList<String>();
			mConnectForSemesters = new ConnectToServerForSemesters();
			mConnectForSemesters.execute();
			setProgressBarIndeterminateVisibility(true);
		}

		public List<String> getInstitutesFromArray() {
			List<String> institutes = new ArrayList<String>();
			String institute;
			if (mInstituteJsonArray != null) {
				try {
					for (int i = 0; i < mInstituteJsonArray.length(); i++) {

						mJsonObject = mInstituteJsonArray.getJSONObject(i);
						institute = mJsonObject
								.getString(TimeTabConstants.INSTITUTENAME
										.toString());
						institutes.add(institute);
					}
				} catch (JSONException e) {
					return null;
				}
			}
			return institutes;
		}

		public void getCoursesFromArray() {
			String courses;
			int courseId;
			mCourseName = new ArrayList<String>();
			mCourseId = new ArrayList<Integer>();

			if (mCourseJsonArray != null) {
				try {
					for (int i = 0; i < mCourseJsonArray.length(); i++) {
						JSONObject jObj = mCourseJsonArray.getJSONObject(i);
						courseId = Integer
								.parseInt(jObj
										.getString(TimeTabConstants.COURSEID
												.toString()));
						courses = jObj.getString(TimeTabConstants.COURSENAME
								.toString());
						mCourseId.add(courseId);
						mCourseName.add(courses);
						Log.v("Courses", mCourseName.toString() + " = "
								+ mCourseId.toString());
					}
					mTTDatabse.getCourseData(mCourseName, mCourseId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.v(TAG, "It is null");
			}

		}

		private List<String> getProgrammesFromArray() {
			String program;
			try {
				for (int i = 0; i < mProgramJsonArray.length(); i++) {

					mJsonObject = mProgramJsonArray.getJSONObject(i);
					program = mJsonObject
							.getString(TimeTabConstants.PROGRAMNAME.toString());
					mProgrammes.add(program);
				}
				return mProgrammes;
			} catch (JSONException e) {
				return null;
			}

		}

		private List<String> getSemestersFromArray() {
			String program;
			try {
				for (int i = 0; i < mSemesterJsonArray.length(); i++) {

					mJsonObject = mSemesterJsonArray.getJSONObject(i);
					program = mJsonObject
							.getString(TimeTabConstants.SEMESTERNAME.toString());
					mSemesters.add(program);
				}
				return mSemesters;
			} catch (JSONException e) {
				return null;
			}

		}

		private void getCourseSchedules() {
			JSONObject jObject = null;
			if (mCourseScheduleArray != null) {
				try {
					for (int i = 0; i < mCourseScheduleArray.length(); i++) {
						jObject = mCourseScheduleArray.getJSONObject(i);
						mCourseStartHour.add(jObject
								.getString(TimeTabConstants.STARTHOUR
										.toString()));
						mCourseStartMin
								.add(jObject
										.getString(TimeTabConstants.STARTMIN
												.toString()));
						mCourseEndHour
								.add(jObject.getString(TimeTabConstants.ENDHOUR
										.toString()));
						mCourseEndMin.add(jObject
								.getString(TimeTabConstants.ENDMIN.toString()));
						mCourseLocation
								.add(jObject
										.getString(TimeTabConstants.LOCATION
												.toString()));
						mCourseId.add(jObject.getInt(TimeTabConstants.COURSEID
								.toString()));
						mCourseDayId.add(jObject
								.getString(TimeTabConstants.DAYID.toString()));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}

		private JSONArray getJsonArrayFromObject(JSONObject jObject, int type) {
			JSONArray jArray = null;
			if (jObject != null) {
				try {
					if (Integer.parseInt(jObject
							.getString(TimeTabConstants.SUCCESS.toString())) == 1) {
						switch (type) {
						case INSTITUTES:
							jArray = jObject
									.getJSONArray(TimeTabConstants.INSITUTES
											.toString());
							if (jArray.length() != 0) {
								Toast.makeText(mContext, "ready",
										Toast.LENGTH_SHORT).show();
								return jArray;
							}
							break;
						case PROGRAMMES:
							jArray = jObject
									.getJSONArray(TimeTabConstants.PROGRAMS
											.toString());
							if (jArray.length() != 0) {
								return jArray;
							}
							break;
						case SEMESTER:
							jArray = jObject
									.getJSONArray(TimeTabConstants.SEMESTERS
											.toString());
							if (jArray.length() != 0) {
								Toast.makeText(mContext, "ready",
										Toast.LENGTH_SHORT).show();
								return jArray;
							}
							break;
						case COURSES:
							jArray = jObject
									.getJSONArray(TimeTabConstants.COURSES
											.toString());
							if (jArray.length() != 0) {
								return jArray;
							}
							break;
						case COURSE_SCHEDULES:
							jArray = jObject
									.getJSONArray(TimeTabConstants.COURSESCHEDULES
											.toString());
							if (jArray.length() != 0) {
								return jArray;
							}
							break;
						}
					}

				} catch (JSONException e) {
					return null;
				}
			}
			return null;
		}

		private class ConnectToServerForInstitutes extends
				AsyncTask<Object, Void, JSONObject> {

			JSONObject jObject = null;
			BufferedReader reader = null;

			@Override
			protected JSONObject doInBackground(Object... params) {
				if (!mInsituteQueryUrl.isEmpty()) {
					try {
						URL urlConnector = new URL(mInsituteQueryUrl);
						HttpURLConnection connection = (HttpURLConnection) urlConnector
								.openConnection();
						connection.connect();
						mResponseCode = connection.getResponseCode();
						if (mResponseCode == HttpURLConnection.HTTP_OK) {
							InputStream inputstrem = connection
									.getInputStream();
							StringBuilder builder = new StringBuilder();
							reader = new BufferedReader(new InputStreamReader(
									inputstrem));

							String line;
							while ((line = reader.readLine()) != null) {
								builder.append(line + "\n");
							}
							String responseData = builder.toString();
							jObject = new JSONObject(responseData);
						} else {
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(
										mContext,
										"The request is taking too long to be processed. Please try again later",
										Toast.LENGTH_LONG).show();
								finish();
							}
						});

					} catch (JSONException e) {
						e.printStackTrace();
						return null;
					}

				}
				return jObject;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (result != null) {
					mInstituteJsonArray = getJsonArrayFromObject(result,
							INSTITUTES);
					if (mInstituteJsonArray != null) {
						setSpinnerData(mInstituteSpinner,
								getInstitutesFromArray());
					}
				}
			}
		}

		private class ConnectToServerForProgrammes extends
				AsyncTask<Object, Void, JSONObject> {
			BufferedReader reader = null;
			JSONObject jsonObject = null;

			@Override
			protected JSONObject doInBackground(Object... params) {
				if (!mProgrammeQueryUrl.isEmpty()) {
					try {
						URL programmerUrl = new URL(mProgrammeQueryUrl);
						HttpURLConnection connection = (HttpURLConnection) programmerUrl
								.openConnection();
						connection.connect();
						mResponseCode = connection.getResponseCode();
						if (mResponseCode == HttpURLConnection.HTTP_OK) {
							reader = new BufferedReader(new InputStreamReader(
									connection.getInputStream()));
							StringBuilder sb = new StringBuilder();
							String line;
							while ((line = reader.readLine()) != null) {
								sb.append(line + "\n");
							}
							String responseData = sb.toString();
							jsonObject = new JSONObject(responseData);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						return null;
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
				return jsonObject;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (result != null) {
					mProgramJsonArray = getJsonArrayFromObject(result,
							PROGRAMMES);
					if (mProgramJsonArray != null) {
						setSpinnerData(mProgrammeSpinner,
								getProgrammesFromArray());
					} else {
						Toast.makeText(mContext,
								"The search returned no results",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}

		private class ConnectToServerForSemesters extends
				AsyncTask<Object, Void, JSONObject> {
			BufferedReader reader = null;
			JSONObject jsonObject = null;

			@Override
			protected JSONObject doInBackground(Object... params) {
				if (!mSemesterQueryUrl.isEmpty()) {
					try {
						URL programmerUrl = new URL(mSemesterQueryUrl);
						HttpURLConnection connection = (HttpURLConnection) programmerUrl
								.openConnection();
						connection.connect();
						mResponseCode = connection.getResponseCode();
						if (mResponseCode == HttpURLConnection.HTTP_OK) {
							reader = new BufferedReader(new InputStreamReader(
									connection.getInputStream()));
							StringBuilder sb = new StringBuilder();
							String line;
							while ((line = reader.readLine()) != null) {
								sb.append(line + "\n");
							}
							String responseData = sb.toString();
							jsonObject = new JSONObject(responseData);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						return null;
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
				return jsonObject;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (result != null) {
					mSemesterJsonArray = getJsonArrayFromObject(result,
							SEMESTER);
					if (mSemesterJsonArray != null) {
						setSpinnerData(mSemesterSpinner,
								getSemestersFromArray());
					} else {
						Toast.makeText(mContext,
								"The search returned no results",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}

		private class ConnectToServerForCourses extends
				AsyncTask<Object, Void, JSONObject> {
			private JSONObject jObject = null;
			private BufferedReader reader = null;

			@Override
			protected JSONObject doInBackground(Object... params) {
				if (!mCourseQueryUrl.isEmpty()) {
					try {
						URL courseUrl = new URL(mCourseQueryUrl);
						HttpURLConnection connection = (HttpURLConnection) courseUrl
								.openConnection();
						connection.connect();
						mResponseCode = connection.getResponseCode();
						if (mResponseCode == HttpURLConnection.HTTP_OK) {
							reader = new BufferedReader(new InputStreamReader(
									connection.getInputStream()));
							String line;
							StringBuilder sb = new StringBuilder();
							while ((line = reader.readLine()) != null) {
								sb.append(line + "\n");
							}
							String responseData = sb.toString();
							jObject = new JSONObject(responseData);
							Log.v(TAG, jObject.toString());
						}
					} catch (JSONException e) {
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
				return jObject;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (result != null) {
					mCourseJsonArray = getJsonArrayFromObject(result, COURSES);
					getCoursesFromArray();
					mSyncMenuItem.setVisible(true);
				}
			}
		}

		private class ConnectToServerForCourseSchedules extends
				AsyncTask<Object, Void, JSONObject> {

			BufferedReader reader = null;
			JSONObject jObject = null;

			@Override
			protected JSONObject doInBackground(Object... params) {
				if (!mCourseScheduleQueryUrl.isEmpty()) {
					try {
						URL connector = new URL(mCourseScheduleQueryUrl);
						HttpURLConnection connection = (HttpURLConnection) connector
								.openConnection();
						connection.connect();
						mResponseCode = connection.getResponseCode();
						if (mResponseCode == HttpURLConnection.HTTP_OK) {
							reader = new BufferedReader(new InputStreamReader(
									connection.getInputStream()));
							StringBuffer sb = new StringBuffer();
							String line;
							while ((line = reader.readLine()) != null) {
								sb.append(line + "\n");
							}
							String responseData = sb.toString();
							jObject = new JSONObject(responseData);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return jObject;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (result != null) {
					mCourseScheduleArray = getJsonArrayFromObject(result,
							COURSE_SCHEDULES);
				}
			}
		}
	}
}
