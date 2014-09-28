package glivion.timetab.ui;

import glivion.timetab.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExamsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.exams_layout, container, false);
		return view;
	}

	public static Fragment newInstance() {
		// TODO Auto-generated method stub
		return new ExamsFragment();
	}
}
