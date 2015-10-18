package com.jty.util.volley;

import java.util.Map;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * Volley helper.
 */
public class VolleyHelper {

	// ---------------------------------------------------- Private data
	private static RequestQueue mQueue;

	// ---------------------------------------------------- Public methods
	/**
	 * POST.
	 * 
	 * @param context
	 *            context.
	 * @param url
	 *            请求URL.
	 * @param params
	 *            请求参数。
	 * @param listener
	 *            处理响应/错误的listener.
	 */
	public static void post(Context context, String url, String params,
			final Response.Listener<String> listener) {
		RequestQueue queue = getRequestQueue(context);

		Response.ErrorListener errListner = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				if (listener != null) {
					listener.onResponse(null);
				}
			}
		};

		StringRequest request = new StringRequest(url, params, errListner,
				listener);
		request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		queue.add(request);
	}

	public static void post(Context context, String url, Map<String, String> params,
			final Response.Listener<String> listener) {
		String params2 = getParams(params);

		post(context, url, params2, listener);
	}

	private static RequestQueue getRequestQueue(Context context) {
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context.getApplicationContext());
		}

		return mQueue;
	}

	private static String getParams(Map<String, String> params) {
		if (params == null) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		return buf.toString();
	}

	// ---------------------------------------------------- Test methods
	public static void test(Context context) {
		String url = "http://test2.hipalsports.com/HiPalServices/videos/searchVideos";
		String params = "offset=0&length=10";

		Response.Listener<String> listener = new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				System.out.println(response);
			}
		};

		post(context, url, params, listener);
	}
}
