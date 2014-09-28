package glivion.timetab.ui;

import glivion.timetab.R;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DialogWithDetails extends DialogFragment {
	static String cName;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_fragment_layout,
				container, false);
		TextView tv = (TextView)view.findViewById(R.id.coursename);
		tv.setText(cName);
		return view; 
	}
	public static DialogWithDetails newInstance(String name){
		cName = name;
		return new DialogWithDetails();
	}
}
