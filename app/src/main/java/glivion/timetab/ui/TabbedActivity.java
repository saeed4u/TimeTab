package glivion.timetab.ui;

import glivion.timetab.CourseSchedule;
import glivion.timetab.R;
import glivion.timetab.adapter.SchedulesListViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TabbedActivity extends Fragment {

	public static final String TAG = TabbedActivity.class.getSimpleName();
	private Calendar mCalendar = Calendar.getInstance(Locale.getDefault());
	ViewPager mViewPager;
	SectionsPagerAdapter mSectionsPagerAdapter;

	public static TabbedActivity newInstance() {
		return new TabbedActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_item_one, container, false);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getChildFragmentManager());

		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		setSelectedTabToTheDayOfTheWeek();
		return v;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Log.v(TAG, Integer.toString(position));
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment(position);
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 7 total pages.
			return 7;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();

			switch (position) {
			case 0:
				return getString(R.string.monday).toUpperCase(l);
			case 1:
				return getString(R.string.tuesday).toUpperCase(l);
			case 2:
				return getString(R.string.wednesday).toUpperCase(l);
			case 3:
				return getString(R.string.thursday).toUpperCase(l);
			case 4:
				return getString(R.string.friday).toUpperCase(l);
			case 5:
				return getString(R.string.saturday).toUpperCase(l);
			case 6:
				return getString(R.string.sunday).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment implements
			MyColorPickerDialog.OnMyCustomDialogListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private TextView mTrying;
		private ListView mScheduleListView;
		private List<CourseSchedule> mScheduleData;
		private CourseSchedule cs;
		public MainActivity activity;
		private int mNumberOfSelections = 0;

		@Override
		public void onAttach(Activity myActivity) {
			super.onAttach(myActivity);
			activity = (MainActivity) myActivity;
		}

		/*private void trying() {
			switch (mIndex) {
			case 0:
				mTrying.setText("Tab 1");
				break;
			case 1:
				mTrying.setText("Tab 2");
				break;
			case 2:
				mTrying.setText("Tab 3");
				break;
			case 3:
				mTrying.setText("Tab 4");
				break;
			case 4:
				mTrying.setText("Tab 5");
				break;
			case 5:
				mTrying.setText("Tab 6");
				break;
			case 6:
				mTrying.setText("Tab 7");
				break;
			}
		}*/

		public DummySectionFragment(int position) {
			cs = new CourseSchedule("Computer Science", "Saeed", "LR2", 12365,
					Color.RED);
			mScheduleData = new ArrayList<CourseSchedule>();
			mScheduleData.add(cs);
		}

		public DummySectionFragment(){
			
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tabbed,
					container, false);
			mTrying = (TextView) rootView.findViewById(R.id.tryingtext);
			mScheduleListView = (ListView) rootView
					.findViewById(R.id.tab_listview);
			mScheduleListView.setAdapter(new SchedulesListViewAdapter(
					getActivity(), mScheduleData));
			mScheduleListView
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, int position, long id) {
							mScheduleListView.setItemChecked(position, true);
							return true;
						}
					});
			mScheduleListView
					.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			mScheduleListView
					.setMultiChoiceModeListener(new MultiChoiceModeListener() {

						@Override
						public boolean onPrepareActionMode(ActionMode mode,
								Menu menu) {
							return false;
						}

						@Override
						public void onDestroyActionMode(ActionMode mode) {
							

						}

						@Override
						public boolean onCreateActionMode(ActionMode mode,
								Menu menu) {
							mode.getMenuInflater().inflate(R.menu.tabbed, menu);
							return true;
						}

						@Override
						public boolean onActionItemClicked(ActionMode mode,
								MenuItem item) {
							return false;
						}

						@Override
						public void onItemCheckedStateChanged(ActionMode mode,
								int position, long id, boolean checked) {
							if (checked) {
								mNumberOfSelections++;
							} else {
								mNumberOfSelections--;
							}
							String title = (mNumberOfSelections > 1) ? Integer
									.toString(mNumberOfSelections)
									+ " Items Selected" : Integer
									.toString(mNumberOfSelections)
									+ " Item Selected";

							Spannable newTitle = new SpannableString(title);
							newTitle.setSpan(
									new ForegroundColorSpan(Color.rgb(0, 106,
											249)), 0, newTitle.length(),
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							mode.setTitle(newTitle);

						}
					});
			mScheduleListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					/*
					 * CourseSchedule cs = (CourseSchedule) parent
					 * .getItemAtPosition(position); AlertDialog.Builder builder
					 * = new AlertDialog.Builder(getActivity()); View rootView =
					 * getActivity().getLayoutInflater().inflate(R.layout.
					 * dialog_fragment_layout, null); TextView tv =
					 * (TextView)rootView.findViewById(R.id.coursename);
					 * tv.setText(Long.toString(cs.getScheduleDuration()));
					 * builder.setView(rootView); AlertDialog dialog =
					 * builder.create(); dialog.show();
					 */

					// getActivity().setContentView(R.layout.dialog_fragment_layout);
					MyColorPickerDialog dialog = new MyColorPickerDialog(
							getActivity(), getResources()
									.getColor(R.color.blue),
							DummySectionFragment.this);
					dialog.show();

					/*
					 * DialogWithDetails.newInstance(cs.getCourseLecturer());
					 * DialogWithDetails dwd = new DialogWithDetails();
					 * dwd.show(getFragmentManager(), "");
					 * 
					 * FragmentManager fm = getActivity()
					 * .getSupportFragmentManager(); fm.beginTransaction()
					 * .(R.id.content_frame, DialogWithDetails.newInstance(cs
					 * .getCourseLecturer())).commit();
					 */
				}
			});
			//trying();
			return rootView;
		}

		@Override
		public void onCancel(MyColorPickerDialog dialog) {
		}

		@Override
		public void onOk(MyColorPickerDialog dialog, int color) {
			mTrying.setTextColor(color);
		}
	}

	/**
	 * Set the current tab to be the day of the week
	 */
	private void setSelectedTabToTheDayOfTheWeek() {
		// Get the current day of the week
		int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		Log.e("Trying", Integer.toString(dayOfWeek));
		// Check if the day of the week is Sunday
		if (dayOfWeek == 1) {
			dayOfWeek = 6;
		}
		// Check if the day of the week is Monday
		else if (dayOfWeek == 2) {
			dayOfWeek = 0;
		}
		// Check if the day of the week is Tuesday
		else if (dayOfWeek == 3) {
			dayOfWeek = 1;
		}
		// Check if the day of the week is Wednesday
		else if (dayOfWeek == 4) {
			dayOfWeek = 2;
		}
		// Check if the day of the week is Thursday
		else if (dayOfWeek == 5) {
			dayOfWeek = 3;
		}
		// Check if the day of the week is Friday
		else if (dayOfWeek == 6) {
			dayOfWeek = 4;
		}
		// Check if the day of the week is Sat
		else if (dayOfWeek == 7) {
			dayOfWeek = 5;
		}
		mViewPager.setCurrentItem(dayOfWeek, true);
	}
}
