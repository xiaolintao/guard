package com.kingdom.sdk.net.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;


/**
 * 网络请求封装
 * 
 * @author Administrator
 * 
 */
public class HttpUtils {
	/*** 网络响应成功 */
	public static final int RESULT_OK = 0;
	/*** 网络响应失败 */
	public static final int RESULT_NET_ERROR = 1;
	public static final int RESULT_SERVER_ERROR = 2;
	public static final int RESULT_TIMEOUT = 3;

	public static int RETRY_TIME = 2;

	public static int DEFAULT_SOCKET_TIMEOUT = 10000;
	// http请求头
	private static Map<String,String> mHeaders = new HashMap<String,String>();

	/**
	 * 发送GET请求
	 * 
	 * @param command
	 *            协议命令
	 * @param url
	 *            请求地址
	 * @param object
	 *            回调相应的类
	 */
	public static void HttpGet(final String url ,final IHttpResponeListener listener) {
		HttpGet(url,DEFAULT_SOCKET_TIMEOUT,listener);
	}

	public static void HttpGet(final String url,int timeoutMs,final IHttpResponeListener listener) {

		RequestQueue mQueue =  RequestQueueManager.getInstance().getRequestQueue();
		HttpRequest baseRequest = new HttpRequest(Method.GET, url,mHeaders,timeoutMs,
				new Response.Listener<ResponseEntity>() {

			@Override
			public void onResponse(ResponseEntity entity) {
				// TODO Auto-generated method stub
						// 网络有数据响应过来
				if(listener!=null){
					listener.onHttpRespone(entity);
				}
			}
		},
		new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
						// 网络出现异常
				ResponseEntity responseEntity = new ResponseEntity();
				responseEntity.setStatus(RESULT_NET_ERROR);
				//responseEntity.setCommand(command);
				responseEntity.setUrl(url);
				responseEntity.setContent(error.getMessage());

				if(listener!=null){
					listener.onHttpRespone(responseEntity);
				}
			}
		}
				);
		mQueue.add(baseRequest);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param command
	 *            协议命令
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param object
	 *            回调相应的类
	 */
	public static void HttpPost(final String url,String params,final IHttpResponeListener listener) {
		HttpPost(url,params,DEFAULT_SOCKET_TIMEOUT,listener);
	}

	public static void HttpPost(final String url ,String params,int timeoutMs,final IHttpResponeListener listener) {
		RequestQueue mQueue =  RequestQueueManager.getInstance().getRequestQueue();
		HttpRequest baseRequest = new HttpRequest(Method.POST,url,mHeaders,params,timeoutMs,
				new Response.Listener<ResponseEntity>() {

			@Override
			public void onResponse(ResponseEntity entity) {
				// TODO Auto-generated method stub
						// 网络有响应
				if(listener!=null){
					listener.onHttpRespone(entity);
				}
			}
		},
		new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
						// 判断错误类型
				int status = RESULT_OK;
				if (error instanceof TimeoutError) {
					status = RESULT_TIMEOUT;
				}
				else if (error instanceof ServerError || error instanceof AuthFailureError) {
					status = RESULT_SERVER_ERROR;
				}
				else if (error instanceof NetworkError || error instanceof NoConnectionError) {
					status = RESULT_NET_ERROR;
				}

						// 网络异常
				ResponseEntity responseEntity = new ResponseEntity();
				responseEntity.setStatus(status);
				//responseEntity.setCommand(command);
				responseEntity.setUrl(url);
				responseEntity.setContent(error.getMessage());
				if(listener!=null){
					listener.onHttpRespone(responseEntity);
				}
			}
		}
				);
		mQueue.add(baseRequest);
	}

	public static void GetBitmap(final String url ,final IGetImageListener listener)
	{
		RequestQueue mQueue = RequestQueueManager.getInstance().getRequestQueue();
		MyImageRequest imageRequest = new MyImageRequest(url,mHeaders,
				new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap response) {
				if(listener!=null){
					listener.onResponse(response);
				}
			}
		}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(listener!=null){
					listener.onResponse(null);
				}
			}
		});

		mQueue.add(imageRequest);
	}

	/**
	 * get获取图片,同步方式
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap GetBitmap_Sync(String url){

		int time = 0;
		do{
			try {
				// request
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(url);

				// send
				HttpResponse response = client.execute(request);
				if (response != null) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						Bitmap bmp = BitmapFactory.decodeStream(entity.getContent());
						return bmp;
					}
				}
			}
			catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {}
					continue;
				}
			}
		}while(time < RETRY_TIME);

		return null;
	}

	/**
	 * get获取图片,同步方式
	 * 
	 * @param url
	 * @return
	 */
	public static void SetHeaders(Map<String,String> headers){
		if(headers != null){
			for(String key : headers.keySet()){
				mHeaders.put(key,headers.get(key));
			}
		}else{
			mHeaders.clear();
		}
	}

	public static void SetHeaders(String name,String value){
		mHeaders.put(name, value);
	}
}
