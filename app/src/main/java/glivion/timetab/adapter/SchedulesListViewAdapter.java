package glivion.timetab.adapter;

import glivion.timetab.CourseSchedule;
import glivion.timetab.R;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SchedulesListViewAdapter extends ArrayAdapter<CourseSchedule> {

	private List<CourseSchedule> mScheduleData;
	private Context mContext;

	public SchedulesListViewAdapter(Context context,
			List<CourseSchedule> courseScheduleData) {
		super(context, R.layout.schedules_listview, courseScheduleData);
		mScheduleData = courseScheduleData;
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.schedules_listview, parent, false);

			holder.colorIdentifier = view.findViewById(R.id.schedule_color);
			holder.courseDuration = (TextView) view
					.findViewById(R.id.schedule_coursetime);
			holder.courseLocation = (TextView) view
					.findViewById(R.id.schedule_courselocation);
			holder.courseName = (TextView) view
					.findViewById(R.id.schedule_coursename);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		CourseSchedule cs = this.mScheduleData.get(position);
		holder.colorIdentifier.setBackgroundColor(cs.getScheduleColor());
		holder.courseDuration.setText(Long.toString(cs.getScheduleDuration()));
		holder.courseLocation.setText(cs.getCourseLocation());
		holder.courseName.setText(cs.getCourseName());
		return view;
	}

	/**
	 * A helper class to hold a reference to the views of this adapter
	 * 
	 * @author SaeedIssah
	 * 
	 */
	private class ViewHolder {
		View colorIdentifier;
		TextView courseName;
		TextView courseDuration;
		TextView courseLocation;
	}
}
