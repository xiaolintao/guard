package com.kingdom.sdk.net.http;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * 网络请求基类
 * 
 * @author Administrator
 * 
 */
public class HttpRequest extends Request<ResponseEntity> {
	private Listener<ResponseEntity> mlistener;
	private String params = null;
	private String url;
	private int command;
	private int timeoutMs;

	private Map<String,String> mHeaders=new HashMap<String,String>();

	/**
	 * 带参数的请求
	 * 
	 * @param command
	 * @param method
	 * @param url
	 * @param listener
	 * @param errorListener
	 */
	public HttpRequest(int method, String url,Map<String,String> headers,int timeoutMs,Listener<ResponseEntity> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		mlistener = listener;
		this.url = url;
		this.timeoutMs = timeoutMs;
		//this.command = command;

		if(headers != null){
			for(String key : headers.keySet()){
				mHeaders.put(key,headers.get(key));
			}
		}
		setCookie();
	}

	/**
	 * 不带参数的请求
	 * 
	 * @param command
	 * @param method
	 * @param url
	 * @param params
	 * @param listener
	 * @param errorListener
	 */
	public HttpRequest(int method, String url,Map<String,String> headers,String params,int timeoutMs,Listener<ResponseEntity> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		mlistener = listener;
		this.url = url;
		this.timeoutMs = timeoutMs;
		//this.command = command;
		this.params = params;

		if(headers != null){
			for(String key : headers.keySet()){
				mHeaders.put(key,headers.get(key));
			}
		}

		setCookie();
	}

	/**
	 * 解析网络响应的数据
	 */
	@Override
	protected Response<ResponseEntity> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		ResponseEntity responseEntity = new ResponseEntity();
		String parsed;
		try {
			saveCookies(response);

			parsed = new String(response.data,HttpHeaderParser.parseCharset(mHeaders/*response.headers*/));
			responseEntity.setStatus(HttpUtils.RESULT_OK);
		} catch (UnsupportedEncodingException e) {
			responseEntity.setStatus(HttpUtils.RESULT_NET_ERROR);
			parsed = new String(response.data);
		}
		responseEntity.setCommand(command);
		responseEntity.setUrl(url);
		responseEntity.setContent(parsed);
		return Response.success(responseEntity,HttpHeaderParser.parseCacheHeaders(response));
	}


	@Override
	public byte[] getBody() throws AuthFailureError {
		// TODO Auto-generated method stub
		return params == null ? super.getBody() : params.getBytes();
	}

	/** 获取cookies */
	private void saveCookies(NetworkResponse response) {
		String cookie = "";

		Map<String, String> responseHeaders = response.headers;
		String rawCookies = responseHeaders.get("Set-Cookie");
		if (!TextUtils.isEmpty(rawCookies)) {
			// 先取第一个值为cookie
			String[] split = rawCookies.split(";");
			if (split.length > 0) {
				cookie = split[0];
			}

			CookieHelper.setCookie(cookie);
		}
	}

	public void setCookie(){
		String cookie = CookieHelper.getCookie();
		if (!TextUtils.isEmpty(cookie)) {
			mHeaders.put("Cookie", cookie);
		}
	}

	/**
	 * 设置请求head
	 */
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(mHeaders.get("Cookie"))) {
			return mHeaders;
		}
		return super.getHeaders();
	}

	/**
	 * 设置连接超时
	 */
	@Override
	public RetryPolicy getRetryPolicy() {
		// TODO Auto-generated method stub
		if(timeoutMs>0){
			RetryPolicy retryPolicy = new DefaultRetryPolicy(timeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			return retryPolicy;
		}
		return super.getRetryPolicy();
	}

	@Override
	protected void deliverResponse(ResponseEntity response) {
		// TODO Auto-generated method stub
		mlistener.onResponse(response);
	}

}
