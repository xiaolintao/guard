package com.pplt.guard.daemon;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.FileObserver;
import android.util.Log;

/**
 * nest file observer to monitor whether file is opened.
 * 
 */
public class FileMonitor {

	// ---------------------------------------------------- Constants
	private final static String TAG = "FileMonitor";

	// mask : create & delete
	public final static int MASK_CREATE_DELETE = FileObserver.CREATE
			| FileObserver.MOVE_SELF | FileObserver.MOVED_TO
			| FileObserver.MOVED_FROM | FileObserver.DELETE
			| FileObserver.DELETE_SELF;

	// mask : open & close
	public final static int MASK_OPEN_CLOSE = FileObserver.OPEN
			| FileObserver.CLOSE_WRITE | FileObserver.CLOSE_NOWRITE;

	public interface OnEventListener {
		void onEvent(int event, String path);
	}

	public interface OnFileEventListener {
		void onOpenFile(String path);

		void onCloseFile(String path);
	}

	// ---------------------------------------------------- Private data
	// root directory.
	private String mRoot;

	// Observers : key - directory value - observer
	private Map<String, FileObserver> mObservers = new HashMap<String, FileObserver>();

	// opened files : path.
	private List<String> mOpenedFiles = new ArrayList<String>();

	// event listener
	private OnEventListener mListener;

	// file event listener
	private OnFileEventListener mFileListener;

	private int mMask = MASK_CREATE_DELETE | MASK_OPEN_CLOSE;

	// ---------------------------------------------------- Constructor
	/**
	 * Constructor.
	 * 
	 * @param root
	 *            root directory.
	 */
	public FileMonitor(String root) {
		mRoot = root;
	}

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            root directory.
	 */
	public FileMonitor(String root, int mask) {
		mRoot = root;

		mMask = mask;
	}

	/**
	 * set OnEventListener.
	 * 
	 * @param listener
	 *            listener.
	 */
	public void setOnEventListener(OnEventListener listener) {
		mListener = listener;
	}

	/**
	 * set OnFileEventListener.
	 * 
	 * @param listener
	 *            listener
	 */
	public void setOnFileEventListener(OnFileEventListener listener) {
		mFileListener = listener;
	}

	// ---------------------------------------------------- Public methods
	/**
	 * start watching.
	 */
	public void startWatching() {
		// check
		File file = new File(mRoot);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}

		// add observer
		addObserver(mRoot);

		// add observer : sub directory
		startWatching(mRoot);
	}

	/**
	 * stop watching.
	 */
	public void stopWatching() {
		Iterator<Map.Entry<String, FileObserver>> it = mObservers.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, FileObserver> entry = it.next();

			entry.getValue().stopWatching();
		}

		mObservers.clear();
	}

	/**
	 * whether a file or directory is opened or not.
	 * 
	 * @param path
	 *            file or directory path.
	 * @return opened or not.
	 */
	public boolean isOpened(String path) {
		// check exists
		File file = new File(path);
		if (!file.exists()) {
			return false;
		}

		// file
		if (file.isFile()) {
			return mOpenedFiles.contains(path);
		}

		// directory
		if (file.isDirectory()) {
			for (String item : mOpenedFiles) {
				if (item.startsWith(path)) {
					return true;
				}
			}
			return false;
		}

		return false;
	}

	// ---------------------------------------------------- Private methods
	/**
	 * start watching.
	 * 
	 * @param dir
	 *            directory.
	 */
	private void startWatching(String dir) {
		File[] files = new File(dir).listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				addObserver(file.getPath());

				startWatching(file.getPath());
			}
		}
	}

	/**
	 * add observer.
	 * 
	 * @param dir
	 *            directory.
	 */
	private void addObserver(final String dir) {
		if (mObservers.containsKey(dir)) {
			return;
		}

		FileObserver observer = new FileObserver(dir, mMask) {
			@Override
			public void onEvent(int event, String path) {
				if (path == null) {
					return;
				}

				File file = new File(combine(dir,path));
				if (mListener != null) {
					mListener.onEvent(event, file.getPath());
				}

				Log.d(TAG, "event=" + event + ",dir=" + dir +",path=" + file.getPath() + ",time=" + Calendar.getInstance().getTimeInMillis());

				switch (event & 0xffff) {
				case FileObserver.OPEN:
					if (file.isFile()) {
						mOpenedFiles.add(file.getPath());

						if (mFileListener != null) {
							mFileListener.onOpenFile(file.getPath());
						}

						Log.d(TAG, "open file =" + file.getPath());
					}
					break;
				case FileObserver.CLOSE_WRITE:
				case FileObserver.CLOSE_NOWRITE:
					if (file.isFile()) {
						mOpenedFiles.remove(file.getPath());

						if (mFileListener != null) {
							mFileListener.onCloseFile(file.getPath());
						}

						Log.d(TAG, "close file =" + file.getPath());
					}
					break;
				case FileObserver.CREATE:
				case FileObserver.MOVED_TO:
					if (file.isDirectory()) {
						addObserver(file.getPath());

						Log.d(TAG, "new directory =" + file.getPath());
					}
					break;
				case FileObserver.DELETE:
				case FileObserver.DELETE_SELF:
				case FileObserver.MOVED_FROM:
					if (mObservers.containsKey(file.getPath())) {
						mObservers.remove(file.getPath());
					}

					Log.d(TAG, "delete  =" + file.getPath());
					break;
				default:
					//Log.d(TAG, "onEvent event =" + event + ",path=" + file.getPath());
					break;
				}
			}
		};

		observer.startWatching();
		mObservers.put(dir, observer);
		Log.d(TAG, "add observer : dir =" + dir);
	}

	private String combine(String root, String path) {
		String prefix = root != null && root.length() > 0 && root.charAt(root
				.length() - 1) == File.separatorChar ? root.substring(0,
						root.length() - 1) : root;
				String suffix = path != null && path.length() > 0 && path.charAt(0) == File.separatorChar ? path
						.substring(1) : path;

						return prefix + File.separator + suffix;
	}
}
