package glivion.timetab.ui;

import glivion.timetab.R;
import glivion.timetab.adapter.DrawerItemsAdapter;
import glivion.timetab.helper.TimeTabDatabase;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("InflateParams")
public class MainActivity extends FragmentActivity {

	public static final String TAG = MainActivity.class.getSimpleName();
	private static final int HOME = 0;
	private static final int TASKS = 1;
	private static final int EXAMS = 2;
	private static final int COURSES = 3;
	private static final int TIMETABLES = 4;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public MenuItem mMenuItem;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mDrawerItmes;

	private String mTaskTitle = "Tasks";
	private String mCourseTitle = "Courses";
	private String mExamsTitle = "Exams";
	private String mTimeTableTitle = "Time Tables";
	private String mHomeTitle = "Home";

	private static final int WIRELESSSETTINGS = 0;

	private DrawerItemsAdapter mDrawerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = getTitle();
		mDrawerItmes = getResources().getStringArray(R.array.drawer_list_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// set a custom shadow that overlays the main content when the drawer
		// oepns
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerList.post(new Runnable() {
			@Override
			public void run() {
				Resources resources = getResources();
				float width = TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 250,
						resources.getDisplayMetrics());
				float height = TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 450,
						resources.getDisplayMetrics());

				DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mDrawerList
						.getLayoutParams();
				params.height = (int) (height);
				params.width = (int) (width);
				mDrawerList.setLayoutParams(params);
			}
		});
		// Add items to the ListView
		mDrawerAdapter = new DrawerItemsAdapter(this, mDrawerItmes);
		mDrawerList.setAdapter(mDrawerAdapter);
		// Set the OnItemClickListener so something happens when a
		// user clicks on an item.
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Set the default content area to item 0
		// when the app opens for the first time
		if (savedInstanceState == null) {
			navigateTo(HOME);
		}

		new TimeTabDatabase(this).getCourses();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		boolean isAvailable = false;

		if (networkInfo != null && networkInfo.isConnected()) {
			isAvailable = true;
		}
		return isAvailable;

	}

	private void updateDisplayForError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.title);
		builder.setMessage(R.string.message);
		builder.setPositiveButton(android.R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				try {
					Intent intent = new Intent(
							android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					startActivityForResult(intent, WIRELESSSETTINGS);
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
				}

			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/*
	 * If you do not have any menus, you still need this function in order to
	 * open or close the NavigationDrawer when the user clicking the ActionBar
	 * app icon.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		int menuItemId = item.getItemId();
		switch (menuItemId) {
		case R.id.action_sync:
			if (isNetworkAvailable()) {
				Intent intent = new Intent(this, SyncActivity.class);
				startActivity(intent);
			} else {
				updateDisplayForError();
			}
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == WIRELESSSETTINGS) {
			if (resultCode == RESULT_CANCELED) {
				if (isNetworkAvailable()) {
					Intent intent = new Intent(this, SyncActivity.class);
					startActivity(intent);
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		// mMenuItem = menu.findItem(R.id.action_edit);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int menuItemId = item.getItemId();
		switch (menuItemId) {
		case R.id.action_sync:
			if (isNetworkAvailable()) {
				Intent intent = new Intent(this, SyncActivity.class);
				startActivity(intent);
			} else {
				updateDisplayForError();
			}
			break;
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		for (int i = 0; i < menu.size(); i++) {
			MenuItem menuItem = menu.getItem(i);
			String title = menuItem.getTitle().toString();
			Spannable newTitle = new SpannableString(title);
			newTitle.setSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)),
					0, newTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			menuItem.setTitle(newTitle);
		}
		return true;

	}

	/*
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			navigateTo(position);
		}
	}

	public void navigateTo(int position) {
		Log.v(TAG, "List View Item: " + position);

		switch (position) {
		case HOME:
			mTitle = mHomeTitle;
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, TabbedActivity.newInstance(),
							TabbedActivity.TAG).commit();

			break;
		case TASKS:
			mTitle = mTaskTitle;
			android.support.v4.app.FragmentTransaction taskFragmentTransac = getSupportFragmentManager()
					.beginTransaction();
			taskFragmentTransac.replace(R.id.content_frame,
					TasksFragment.newInstance());
			taskFragmentTransac.commit();
			break;
		case EXAMS:
			mTitle = mExamsTitle;
			FragmentTransaction examsFragmentTransac = getSupportFragmentManager()
					.beginTransaction();
			examsFragmentTransac.replace(R.id.content_frame,
					ExamsFragment.newInstance());
			examsFragmentTransac.commit();
			break;
		case COURSES:
			mTitle = mCourseTitle;
			FragmentTransaction coursesFragmentTransac = getSupportFragmentManager()
					.beginTransaction();
			coursesFragmentTransac.replace(R.id.content_frame,
					ExamsFragment.newInstance());
			coursesFragmentTransac.commit();
			break;
		case TIMETABLES:
			mTitle = mTimeTableTitle;
			FragmentTransaction timetableFragmentTransac = getSupportFragmentManager()
					.beginTransaction();
			timetableFragmentTransac.replace(R.id.content_frame,
					ExamsFragment.newInstance());
			timetableFragmentTransac.commit();
			break;

		}

		mDrawerLayout.closeDrawer(mDrawerList);
		mDrawerList.setItemChecked(position, true);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

}
