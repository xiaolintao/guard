package com.pplt.guard.explorer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pplt.guard.R;

/**
 * file adapter.
 * 
 */
public class FileAdapter extends BaseAdapter {

	// ---------------------------------------------------- Private data
	private LayoutInflater mInflater;
	private ListView mListView;

	private File[] mFiles = new File[] {};
	private int mSelectedPosition = -1;

	// ---------------------------------------------------- Constructor
	/**
	 * Constructor.
	 * 
	 * @param context
	 *            context.
	 */
	public FileAdapter(Context context, ListView listView) {
		mInflater = LayoutInflater.from(context);

		mListView = listView;
	}

	// ---------------------------------------------------- Override methods
	@Override
	public int getCount() {
		return mFiles != null ? mFiles.length : 0;
	}

	@Override
	public File getItem(int position) {
		return position >= 0 && position < getCount() ? mFiles[position]
				: null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = initContentView(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final File file = getItem(position);

		// image
		int icon = file.isDirectory() ? FileSuffixHelper.ICON_DIRECTORY
				: FileSuffixHelper.getIcon(file.getName());
		holder.file_image.setImageResource(icon);

		// file name
		holder.file_name.setText(file.getName());

		// last modified date
		String lastModified = dateToString(lastModified(file),
				"yyyy-MM-dd HH:mm");
		holder.file_modify.setText(lastModified);

		return convertView;
	}

	// ---------------------------------------------------- Public methods
	/**
	 * set files.
	 * 
	 * @param files
	 *            files.
	 */
	public void setFiles(File[] files) {
		mFiles = files;

		notifyDataSetChanged();
	}

	/**
	 * whether item is selected or not
	 * 
	 * @param position
	 *            position.
	 * @return selected or not.
	 */
	public boolean isSelected(int position) {
		return mSelectedPosition == position;
	}

	/**
	 * select item.
	 * 
	 * @param position
	 *            position.
	 */
	public void select(int position, boolean selected) {
		if (position >= 0 && position < getCount()) {
			if (selected) {
				mSelectedPosition = position;
				mListView.smoothScrollToPosition(position);
			} else {
				mSelectedPosition = -1;
			}

			notifyDataSetChanged();
		}
	}

	/**
	 * select item.
	 * 
	 * @param file
	 *            file.
	 */
	public void select(File file, boolean selected) {
		for (int i = 0; i < mFiles.length; i++) {
			if (equals(file, mFiles[i])) {
				select(i, selected);

				break;
			}
		}
	}

	/**
	 * select next item.
	 */
	public void selectNext() {
		// int next = ((mSelectedPosition + 1) < getCount()) ?
		// (mSelectedPosition + 1)
		// : 0;
		// select(next, true);

		select(mSelectedPosition + 1, true);
	}

	/**
	 * set previous item.
	 */
	public void selectPrevious() {
		// int previous = (mSelectedPosition - 1) >= 0 ? (mSelectedPosition - 1)
		// : getCount() - 1;
		// select(previous, true);

		select(mSelectedPosition - 1, true);
	}

	/**
	 * get selected position.
	 * 
	 * @return position.
	 */
	public int getSelectedPosition() {
		return mSelectedPosition;
	}

	/**
	 * clear selection.
	 */
	public void clearSelection() {
		mSelectedPosition = -1;
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial content view.
	 * 
	 * @param holder
	 *            view holder.
	 * @return view.
	 */
	private View initContentView(ViewHolder holder) {
		View view;

		view = mInflater.inflate(R.layout.explorer_list_item, null);

		holder.file_list_checked = (ToggleButton) view
				.findViewById(R.id.file_list_checked);
		holder.file_image = (ImageView) view.findViewById(R.id.file_image);

		holder.file_name = (TextView) view.findViewById(R.id.file_name_f);
		holder.file_modify = (TextView) view.findViewById(R.id.file_modify);

		view.setTag(holder);
		return view;
	}

	/**
	 * convert date to string.
	 * 
	 * @param dateLong
	 *            long
	 * @return String
	 */
	private String dateToString(long value, String format) {
		String date;
		Date d = new Date(value);
		SimpleDateFormat formatter = new SimpleDateFormat(format,
				Locale.getDefault());
		date = formatter.format(d);
		if (null == date) {
			date = "";
		}
		return date;
	}

	/**
	 * get file last modified time.
	 * 
	 * @param file
	 *            file.
	 * @return last modified time.
	 */
	private long lastModified(File file) {
		return file.lastModified();
	}

	/**
	 * whether two files equals or not.
	 * 
	 * @param f1
	 *            first file.
	 * @param f2
	 *            second file.
	 * @return equal or not.
	 */
	private boolean equals(File f1, File f2) {
		if (f1.isDirectory() && f2.isDirectory()) {
			return f1.getPath().equals(f2.getPath());
		}

		if (f1.isFile() && f2.isFile()) {
			return f1.getPath().equals(f2.getPath())
					&& f1.getName().equals(f2.getName());
		}

		return false;
	}

	public static class ViewHolder {
		ToggleButton file_list_checked;
		ImageView file_image;
		TextView file_name, file_modify;
	}

}
