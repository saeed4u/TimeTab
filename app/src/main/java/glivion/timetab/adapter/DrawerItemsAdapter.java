package glivion.timetab.adapter;

import glivion.timetab.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DrawerItemsAdapter extends ArrayAdapter<String> {

	private String[] mDrawerItems;
	// private int[] mDrawerImages;
	private Context mContext;
	public DrawerItemsAdapter(Context context, String[] drawerItems) {
		super(context, R.layout.custom_drawer_list_items, drawerItems);
		// mDrawerImages = drawerImages;
		mDrawerItems = drawerItems;
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.custom_drawer_list_items, parent,false);
			holder = new ViewHolder();
			// holder.imageView = (ImageView)
			// view.findViewById(R.id.drawer_image);
			holder.textView = (TextView) view
					.findViewById(R.id.drawer_textview);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/*View v = super.getView(position, convertView, parent);
		if(position ==0 ){
			v.setBackgroundResource(R.drawable.gradient_pressed);
		}*/
		// holder.imageView.setImageResource(mDrawerImages[position]);
		holder.textView.setText(mDrawerItems[position]);
		return view;
	}

	private static class ViewHolder {
		// ImageView imageView;
		TextView textView;
	}

}
