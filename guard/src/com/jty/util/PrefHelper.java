package com.jty.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * helper of shared preferences.
 * 
 */
public class PrefHelper {
	// ---------------------------------------------------- Constants
	private final static String PREF_NAME = "com.jty.stock";

	// ---------------------------------------------------- Constructor
	/**
	 * Constructor.
	 */
	private PrefHelper() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * set string value.
	 * 
	 * @param context
	 *            context.
	 * @param name
	 *            key name.
	 * @param value
	 *            value.
	 */
	public static void setString(Context context, String name, String value) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 1);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(name, value);
		editor.commit();
	}

	/**
	 * get string value.
	 * 
	 * @param context
	 *            context.
	 * @param name
	 *            key name.
	 * @param defaultValue
	 *            default value.
	 * @return value.
	 */
	public static String getString(Context context, String name,
			String defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 1);

		return settings.getString(name, defaultValue);
	}

	/**
	 * set integer value.
	 * 
	 * @param context
	 *            context.
	 * @param name
	 *            key name.
	 * @param value
	 *            value.
	 */
	public static void setInt(Context context, String name, int value) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 1);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(name, value);
		editor.commit();
	}

	/**
	 * get integer value.
	 * 
	 * @param context
	 *            context.
	 * @param name
	 *            key name.
	 * @param defaultValue
	 *            default value.
	 * @return value.
	 */
	public static int getInt(Context context, String name, int defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 1);

		return settings.getInt(name, defaultValue);
	}

	/**
	 * set long value.
	 * 
	 * @param context
	 *            context.
	 * @param name
	 *            key name.
	 * @param value
	 *            value.
	 */
	public static void setLong(Context context, String name, long value) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 1);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(name, value);
		editor.commit();
	}

	/**
	 * get long value.
	 * 
	 * @param context
	 *            context.
	 * @param name
	 *            key name.
	 * @param defaultValue
	 *            default value.
	 * @return value.
	 */
	public static long getLong(Context context, String name, long defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 1);

		return settings.getLong(name, defaultValue);
	}

	/**
	 * set float value.
	 * 
	 * @param context
	 *            context.
	 * @param name
	 *            key name.
	 * @param value
	 *            value.
	 */
	public static void setFloat(Context context, String name, float value) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 1);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(name, value);
		editor.commit();
	}

	/**
	 * get float value.
	 * 
	 * @param context
	 *            context.
	 * @param name
	 *            key name.
	 * @param defaultValue
	 *            default value.
	 * @return value.
	 */
	public static float getFloat(Context context, String name,
			float defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 1);

		return settings.getFloat(name, defaultValue);
	}

	/**
	 * set boolean value.
	 * 
	 * @param context
	 *            context.
	 * @param name
	 *            key name.
	 * @param value
	 *            value.
	 */
	public static void setBoolean(Context context, String name, boolean value) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 1);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}

	/**
	 * get boolean value.
	 * 
	 * @param context
	 *            context.
	 * @param name
	 *            key name.
	 * @param defaultValue
	 *            default value.
	 * @return value.
	 */
	public static boolean getBoolean(Context context, String name,
			boolean defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 1);

		return settings.getBoolean(name, defaultValue);
	}
}
