package com.pplt.guard.explorer;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.pplt.guard.Global;
import com.pplt.guard.R;

/**
 * browse file.
 * 
 */
public class FileBrowseActivity extends Activity {

	// ---------------------------------------------------- Constants
	final static String TAG = "FileBrowseActivity";

	// ---------------------------------------------------- Private data
	private ListView mListView;
	private FileAdapter mAdapter;

	private String mCurrentDirectory;

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.explorer_view);

		// initial views
		initViews();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				back();
				return true;
			}
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial views.
	 */
	private void initViews() {
		// list view
		mListView = (ListView) findViewById(R.id.fileList);
		mAdapter = new FileAdapter(this, mListView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int selectedPosition = mAdapter.getSelectedPosition();
				if (selectedPosition != position) {
					mAdapter.select(position, true);
				} else {
					enter();
				}
			}
		});

		// show root directory
		showDirectory(mCurrentDirectory != null ? mCurrentDirectory
				: getRoot());
	}

	/**
	 * show directory.
	 * 
	 * @param directory
	 *            directory name.
	 * @param selectedPosition
	 *            selected position.
	 */
	private void showDirectory(String directory) {
		mCurrentDirectory = directory;

		// clear selection
		mAdapter.clearSelection();

		// show navigator
		String base = getBase();
		String tag = directory.startsWith(base) ? directory.substring(base
				.length()) : directory;
		showNavigators(tag);

		// set adapter
		setAdapter(directory);
	}

	/**
	 * set adapter.
	 * 
	 * @param directory
	 *            directory.
	 */
	private void setAdapter(String directory) {
		// list file
		File[] files = new File(directory).listFiles();
		if (files == null) {
			files = new File[] {};
		}
		Arrays.sort(files, new FileComparator());

		// set adapter
		mAdapter.setFiles(files);
	}


	/**
	 * show navigators.
	 * 
	 * @param fullName
	 *            directory path name.
	 */
	private void showNavigators(String path) {
		int pos = 0;
		String[] names = path.startsWith("/") ? path.substring(1).split("/")
				: path.split("/");

		clearNavigators();
		for (String name : names) {
			if (name != null && name.length() > 0) {
				int marginLeft = pos++ == 0 ? 0 : -21;
				showNavigator(name, marginLeft);
			}
		}
	}

	/**
	 * show navigator.
	 * 
	 * @param name
	 *            directory name.
	 */
	private void showNavigator(String name, int marginLeft) {
		LayoutInflater inflater = getLayoutInflater();
		TextView folder = (TextView) inflater.inflate(
				R.layout.explorer_navigation_right_button, null);
		folder.setText(name);

		LinearLayout.LayoutParams params = new LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		params.setMargins(marginLeft, 1, 0, 0);

		LinearLayout folderLevel = (LinearLayout) findViewById(R.id.folder_level);
		folderLevel.addView(folder, params);
	}

	/**
	 * clear navigators.
	 */
	private void clearNavigators() {
		LinearLayout folderLevel = (LinearLayout) findViewById(R.id.folder_level);
		folderLevel.removeAllViews();
	}

	/**
	 * enter.
	 */
	private void enter() {
		int selected = mAdapter.getSelectedPosition();
		if (selected != -1) {
			File file = mAdapter.getItem(selected);
			onClick(file);
		}
	}

	/**
	 * to parent..
	 */
	private void back() {
		if (mCurrentDirectory == null
				|| mCurrentDirectory.equalsIgnoreCase(getRoot())) {
			return;
		}

		File current = new File(mCurrentDirectory);
		showDirectory(current.getParent());
		mAdapter.select(0, true);
	}

	/**
	 * call on click
	 * 
	 * @param file
	 *            file.
	 */
	private void onClick(File file) {
		// directory
		if (file.isDirectory()) {
			showDirectory(file.getPath());
			mAdapter.select(0, true);
			return;
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * file comparator.
	 */
	class FileComparator implements Comparator<File> {

		@Override
		public int compare(File lhs, File rhs) {
			if (lhs.isDirectory() && rhs.isFile()) {
				return -1;
			} else if (lhs.isFile() && rhs.isDirectory()) {
				return 1;
			} else {
				return lhs.getName().compareToIgnoreCase(rhs.getName());
			}
		}
	}

	/**
	 * get root directory.
	 * 
	 * @return root directory.
	 */
	private String getRoot() {
		return Global.getRoot();
	}

	/**
	 * get base directory.
	 * 
	 * @return base directory.
	 */
	private String getBase() {
		return Global.getRoot();
	}
}
