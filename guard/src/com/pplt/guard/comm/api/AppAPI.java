package com.pplt.guard.comm.api;

import android.content.Context;

import com.android.volley.Response;
import com.jty.util.volley.VolleyHelper;

/**
 * 通信：app.
 */
public class AppAPI extends BaseAPI {

	/**
	 * 获取最新版本。
	 * 
	 * @param context
	 *            context.
	 * @param listener
	 *            volley response listener.
	 */
	public void getLastVersion(Context context,
			Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("sysType", 0); // app 平台 0 : android

		VolleyHelper.post(context, BASE_URL + "app/getLastVersion",
				params.toString(), listener);
	}
}
