package com.kingdom.sdk.net.http;

public class CookieHelper {

	private static String cookie = "";

	public static String getCookie() {
		return cookie;
	}

	public static void setCookie(String cookies) {
		cookie = cookies;
	}
}
