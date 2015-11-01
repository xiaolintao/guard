package com.pplt.guard;

import android.content.Context;
import android.os.Environment;

import com.hipalsports.entity.UserInfo;
import com.jty.util.FileHelper;
import com.jty.util.PrefHelper;

public class Global {

	// ---------------------------------------------------- Constants
	/** 根目录 */
	private final static String ROOT = Environment
			.getExternalStorageDirectory().getPath() + "/pplt";
	/** 临时文件目录 */
	private final static String TMP_PATH = getRoot() + "/tmp";
	/** 文件：数据库 **/
	public final static String DB_FILENAME = getRoot() + "/pplt.db";

	/** PREF: 版本 */
	private final static String PREF_NEW_VERSION = "new version"; // 新版本name
	private final static String PREF_NEW_VERSION_CODE = "new version code"; // 新版本code

	/** PREF: 账号 */
	final static String PREF_ACCOUNT_LOGIN = "account login"; // 登录用的账号

	/** PREF: 界面 */
	private final static String PREF_MAIN_LAST_TAB = "main last tab"; // 主页面最后"访问"的tab
	private final static String PREF_SUPER_SCRIPT = "super script"; // 角标

	/** Action */
	public static final String ACTION_NEW_VERSION = "com.pplt.guard.newversion"; // 新版本
	public static final String ACTION_LOGIN = "com.pplt.guard.logout"; // 登录
	public static final String ACTION_LOGOUT = "com.pplt.guard.logout"; // 退出登录
	public static final String ACTION_VOLLEY_ABNORMAL = "com.pplt.guard.volley.abnormal"; // volley异常

	/** Extra **/
	public final static String EXTRA_UID = "extra uid"; // 用户id
	public final static String EXTRA_PHONE = "extra phone"; // 手机号码
	public final static String EXTRA_FILE_ID = "extra file id"; // 文件id
	public final static String EXTRA_CONTACT = "extra contact"; // 联系人
	public final static String EXTRA_IDS = "extra ids"; // id

	// ---------------------------------------------------- Global data
	private static Context mContext; // context

	/** 用户信息 */
	private static UserInfo mUser;

	// ---------------------------------------------------- initial
	/**
	 * initial.
	 */
	public static void init(Context context) {
		mContext = context;
	}

	/**
	 * load.
	 */
	public static void load() {
		// 角标
		SuperScript.load();
	}

	/**
	 * 获取根目录。
	 * 
	 * @return 根目录。
	 */
	public static String getRoot() {
		FileHelper.checkDirectory(ROOT);

		return ROOT;
	}

	/**
	 * 获取临时文件目录。
	 * 
	 * @return 临时文件目录。
	 */
	public static String getTmpPath() {
		FileHelper.checkDirectory(TMP_PATH);

		return TMP_PATH;
	}

	// ---------------------------------------------------- 版本
	/**
	 * 设置新版本name。
	 * 
	 * @param name
	 *            新版本name。
	 */
	public static void setNewVersion(String name) {
		PrefHelper.setString(mContext, PREF_NEW_VERSION, name);
	}

	/**
	 * 获取新版本name。
	 * 
	 * @return 新版本name。
	 */
	public static String getNewVersion() {
		return PrefHelper.getString(mContext, PREF_NEW_VERSION, "");
	}

	/**
	 * 设置新版本code。
	 * 
	 * @param code
	 *            新版本code。
	 */
	public static void setNewVersionCode(int code) {
		PrefHelper.setInt(mContext, PREF_NEW_VERSION_CODE, code);
	}

	/**
	 * 获取新版本code。
	 * 
	 * @return 新版本code。
	 */
	public static int getNewVersionCode() {
		return PrefHelper.getInt(mContext, PREF_NEW_VERSION_CODE, 1);
	}


	// ---------------------------------------------------- 用户信息
	/**
	 * 获取用户。
	 * 
	 * @return 用户。
	 */
	public static UserInfo getUser() {
		return mUser;
	}

	/**
	 * 设置用户信息。
	 * 
	 * @param user
	 *            用户信息。
	 */
	public static void setUser(UserInfo user) {
		mUser = user;
	}

	/**
	 * 置空用户信息。
	 */
	public static void resetUser() {
		mUser = null;
	}

	// ---------------------------------------------------- 界面
	/**
	 * 设置主页面最后"访问"的tab。
	 * 
	 * @param tab
	 *            最后"访问"的tab。
	 */
	public static void setMainLastTab(int tab) {
		PrefHelper.setInt(mContext, PREF_MAIN_LAST_TAB, tab);
	}

	/**
	 * 获取主页面最后"访问"的tab。
	 * 
	 * @return 最后"访问"的tab。
	 */
	public static int getMainLastTab() {
		return PrefHelper.getInt(mContext, PREF_MAIN_LAST_TAB, 0);
	}

	/**
	 * 设置角标。
	 * 
	 * @param superScript
	 *            角标。
	 */
	public static void setSuperScript(long superScript) {
		PrefHelper.setLong(mContext, PREF_SUPER_SCRIPT, superScript);
	}

	/**
	 * 获取角标。
	 * 
	 * @return 角标。
	 */
	public static long getSuperScript() {
		return PrefHelper.getLong(mContext, PREF_SUPER_SCRIPT, 0);
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 转换：Integer[] => “逗号"分隔的String
	 * 
	 * @param arr
	 * @return
	 */
	static String toString(Integer[] arr) {
		StringBuffer sb = new StringBuffer();

		for (Integer item : arr) {
			if (sb.length() != 0) {
				sb.append(",");
			}
			sb.append(item);
		}

		return sb.toString();
	}

	/**
	 * 转换：String[] = > Integer[].
	 * 
	 * @param arr
	 *            String[].
	 * @return Integer[].
	 */
	static Integer[] toInt(String[] arr) {
		if (arr == null || arr.length == 0) {
			return new Integer[0];
		}

		Integer[] result = new Integer[arr.length];
		for (int i = 0; i < arr.length; i++) {
			try {
				result[i] = Integer.valueOf(arr[i]);
			} catch (Exception e) {
			}
		}

		return result;
	}
}
