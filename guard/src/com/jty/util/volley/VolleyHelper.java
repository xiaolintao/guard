package com.jty.util.volley;

import android.content.Context;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pplt.guard.Global;

/**
 * Volley helper.
 */
public class VolleyHelper {

	// ---------------------------------------------------- Constants
	/*** 网络响应失败 */
	public static final int RESULT_UNKNOWN_ERROR = -1;
	public static final int RESULT_NET_ERROR = -2;
	public static final int RESULT_SERVER_ERROR = -3;
	public static final int RESULT_AUTH_ERROR = -4;
	public static final int RESULT_TIMEOUT = -5;

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
	public static void post(final Context context, String url, String params,
			final Response.Listener<String> listener) {
		RequestQueue queue = getRequestQueue(context);

		Response.ErrorListener errListner = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				if (listener != null) {
					listener.onResponse(null);
				}

				dealVolleyError(context, volleyError);
			}
		};

		StringRequest request = new StringRequest(url, params, errListner,
				listener);
		request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		queue.add(request);
	}

	// ---------------------------------------------------- Private methods
	// 获取request queue.
	private static RequestQueue getRequestQueue(Context context) {
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context.getApplicationContext());
		}

		return mQueue;
	}

	// 处理volley error
	private static void dealVolleyError(Context context, VolleyError error) {
		if (error == null) {
			return;
		}

		int status = RESULT_UNKNOWN_ERROR;
		if (error instanceof TimeoutError) {
			status = RESULT_TIMEOUT;
		} else if (error instanceof ServerError) {
			status = RESULT_SERVER_ERROR;
		} else if (error instanceof AuthFailureError) {
			status = RESULT_AUTH_ERROR;
		} else if (error instanceof NetworkError
				|| error instanceof NoConnectionError) {
			status = RESULT_NET_ERROR;
		}

		sendBroadcast(context, status);
	}

	// 发送广播
	private static void sendBroadcast(Context context, int status) {
		Intent intent = new Intent();
		intent.setAction(Global.ACTION_VOLLEY_ABNORMAL);
		intent.putExtra("status", status);

		context.sendBroadcast(intent);
	}
}
