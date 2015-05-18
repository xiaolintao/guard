package com.pplt.guard.comm;

import java.util.List;

/**
 * HTTP URL base.
 */
public class HttpUrlBase {

	// ---------------------------------------------------- function
	/**
	 * add string parameter.
	 * 
	 * @param params
	 *            parameters.
	 * @param name
	 *            name.
	 * @param value
	 *            value.
	 */
	static void addStringParam(List<String> params, String name, String value) {
		if (value != null && value.length() != 0) {
			params.add(name);
			params.add(value);
		}
	}

	/**
	 * add integer parameter.
	 * 
	 * @param params
	 *            parameters.
	 * @param name
	 *            name.
	 * @param value
	 *            value.
	 */
	static void addIntParam(List<String> params, String name, int value) {
		if (value != -1l) {
			params.add(name);
			params.add(String.valueOf(value));
		}
	}

	/**
	 * add integer parameter.
	 * 
	 * @param params
	 *            parameters.
	 * @param name
	 *            name.
	 * @param value
	 *            value.
	 */
	static void addLongParam(List<String> params, String name, long value) {
		if (value != -1l) {
			params.add(name);
			params.add(String.valueOf(value));
		}
	}

	/**
	 * add boolean parameter.
	 * 
	 * @param params
	 *            parameters.
	 * @param name
	 *            name.
	 * @param value
	 *            value.
	 */
	static void addBooleanParam(List<String> params, String name, boolean value) {
		params.add(name);
		params.add(value ? "1" : "0");
	}

	/**
	 * get parameters.
	 * 
	 * @param params
	 *            parameters.
	 * @return parameters.
	 */
	static String getParams(String... params) {
		StringBuffer sb = new StringBuffer();

		int len = params.length;
		for (int i = 0; i < len / 2; i++) {
			if (params[2 * i] != null && params[2 * i + 1] != null) {
				if (sb.length() != 0) {
					sb.append("&");
				}

				sb.append(params[2 * i] + "=" + params[2 * i + 1]);
			}
		}

		return sb.toString();
	}

}
