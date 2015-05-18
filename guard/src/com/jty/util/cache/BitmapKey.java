package com.jty.util.cache;

/**
 * 图片缓存：key。
 */
public class BitmapKey {

	public final static String SCHEME_PERSONAL = "personal";

	public static String getFileKey(String scheme) {
		return "file:///" + scheme;
	}

	public static String getFileKey(String scheme, int id) {
		return "file:///" + scheme + "/" + id;
	}
}
