package glivion.timetab.ui;

import glivion.timetab.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimeTableFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.schedules_listview, container, false);
		return view;
	}
	
	public static TimeTableFragment newInstance(){
		return new TimeTableFragment();
	}
}
