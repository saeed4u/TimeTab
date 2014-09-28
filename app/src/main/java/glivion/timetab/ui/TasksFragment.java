package glivion.timetab.ui;

import glivion.timetab.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TasksFragment extends Fragment {

	private Spinner mTaskSpinner;
	private ArrayAdapter<String> mTaskSpinnerAdapter;

	public static TasksFragment newInstance() {
		return new TasksFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.task_layout, container, false);
		mTaskSpinner = (Spinner)view.findViewById(R.id.task_spinner);
		mTaskSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{"Completed","Uncompleted"});
		mTaskSpinner.setAdapter(mTaskSpinnerAdapter);
		return view;
	}
}
