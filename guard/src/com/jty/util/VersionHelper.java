package com.jty.util;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.pplt.guard.Global;

/**
 * 新版本：helper.
 * 
 * @author Administrator
 * 
 */
public class VersionHelper {

	// ---------------------------------------------------- Constructor
	private VersionHelper() {
	}

	// ---------------------------------------------------- check
	/**
	 * 检测新版本。
	 * 
	 * @param context
	 *            context.
	 * @return APK路径：null - 无新版本。
	 */
	public static String checkNewVersion(Context context) {
		// APK文件路径
		String apkPath = getApkPath();
		if (apkPath == null) {
			return null;
		}

		// package name
		String apkPackageName = VersionHelper.getApkPackageName(context,
				apkPath);
		if (apkPackageName == null
				|| !apkPackageName.equals(context.getPackageName())) {
			return null;
		}

		// version code
		int apkVersionCode = VersionHelper.getApkVersionCode(context, apkPath);
		int appVersionCode = VersionHelper.getAppVersionCode(context);
		if (apkVersionCode <= appVersionCode) {
			return null;
		}

		// version name
		String apkVersionName = VersionHelper.getApkVersionName(context,
				apkPath);
		Global.setNewVersion(apkVersionName);

		return apkPath;
	}

	/**
	 * 获取新版本APK文件路径。
	 * 
	 * @return 文件路径。
	 */
	private static String getApkPath() {
		// 目录
		String root = Global.getRoot() + "/version";
		File dir = new File(root);

		// 目录下的文件
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null && files.length != 0) {
				return files[0].getPath();
			}
		}

		return null;
	}

	// ---------------------------------------------------- Public methods {
	/**
	 * 获取APP的version code.
	 * 
	 * @param context
	 *            context.
	 * @return version code.
	 */
	public static int getAppVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();

		String packageName = context.getPackageName();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);

			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 获取APP的version name.
	 * 
	 * @param context
	 *            context.
	 * @return version name.
	 */
	public static String getAppVersionName(Context context) {
		PackageManager pm = context.getPackageManager();

		String packageName = context.getPackageName();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);

			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 获取APP的package name.
	 * 
	 * @param context
	 *            context.
	 * @param apkPath
	 *            APK文件路径。
	 * @return package name.
	 */
	public static String getApkPackageName(Context context, String apkPath) {
		PackageManager pm = context.getPackageManager();

		PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);

		return packageInfo.packageName;
	}

	/**
	 * 获取APK的version code.
	 * 
	 * @param context
	 *            context.
	 * @param apkPath
	 *            APK文件路径。
	 * @return version code.
	 */
	public static int getApkVersionCode(Context context, String apkPath) {
		PackageManager pm = context.getPackageManager();

		PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);

		return packageInfo.versionCode;
	}

	/**
	 * 获取APP的version name.
	 * 
	 * @param context
	 *            context.
	 * @param apkPath
	 *            APK文件路径。
	 * @return version name.
	 */
	public static String getApkVersionName(Context context, String apkPath) {
		PackageManager pm = context.getPackageManager();

		PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);

		return packageInfo.versionName;
	}
}
