package com.jty.util.volley;

import android.content.Context;

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

		if (error instanceof TimeoutError) {
			// 超时
		} else if (error instanceof ServerError
				|| error instanceof AuthFailureError) {
			// 服务端错误 & 认证错误
		} else if (error instanceof NetworkError
				|| error instanceof NoConnectionError) {
			// 网络错误 & 没有连接
		}
	}
}
