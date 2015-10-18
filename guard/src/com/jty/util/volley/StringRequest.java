package com.jty.util.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Volley请求(post)。
 */
public class StringRequest extends Request<String> {

	// ---------------------------------------------------- Private data
	private final Response.Listener<String> mListener;

	private String mParams;

	// ---------------------------------------------------- Constructor
	/**
	 * 构造函数。
	 * 
	 * @param url
	 *            请求URL。
	 * @param params
	 *            请求参数。
	 * @param errorListener
	 *            出错时的listener.
	 * @param listener
	 *            处理响应的listener.
	 */
	public StringRequest(String url, String params,
			Response.ErrorListener errorListener,
			Response.Listener<String> listener) {
		super(Method.POST, url, errorListener);

		mListener = listener;

		mParams = params;
	}

	// ---------------------------------------------------- Override methods
	@Override
	public byte[] getBody() throws AuthFailureError {
		byte[] body = null;
		try {
			if (mParams != null && mParams.length() > 0) {
				body = mParams.getBytes("utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return body != null ? body : super.getBody();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String result = null;
		try {
			result = new String(response.data, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.success(result, getCacheEntry());
	}

	@Override
	protected void deliverResponse(String response) {
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}

}
