package com.kingdom.sdk.net.http;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.ImageRequest;

/**
 * 网络请求基类
 * 
 * @author Administrator
 * 
 */
public class MyImageRequest extends ImageRequest {
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
	public MyImageRequest(String url,Map<String,String> headers,Listener<Bitmap> listener,int maxWidth,int maxHeight,Config decodeConfig,ErrorListener errorListener) {
		super(url,listener,maxWidth,maxHeight,decodeConfig,errorListener);

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
	protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		saveCookies(response);
		return super.parseNetworkResponse(response);
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
		return super.getRetryPolicy();
	}

	@Override
	protected void deliverResponse(Bitmap response) {
		// TODO Auto-generated method stub
		super.deliverResponse(response);
	}

}
