package glivion.timetab.ui;

import glivion.timetab.R;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CoursesFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.schedules_listview, container,
				false);
		return view;
	}

	public static CoursesFragment newInstance() {
		return new CoursesFragment();
	}
}
