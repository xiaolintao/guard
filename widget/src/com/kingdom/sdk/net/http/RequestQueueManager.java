package com.kingdom.sdk.net.http;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.kingdom.sdk.ioc.IocContainer;

public class RequestQueueManager {
	/** 
     * Log or request TAG 
     */  
    public static final String TAG = "VolleyPatterns";  
  
    /** 
     * Global request queue for Volley 
     */  
    private RequestQueue mRequestQueue;  
  
    /** 
     * A singleton instance of the application class for easy access in other places 
     */  
    private static RequestQueueManager sInstance;  
    
    /** 
     * @return ApplicationController singleton instance 
     */  
    public static synchronized RequestQueueManager getInstance() {  
    	if(sInstance == null){
    		sInstance = new RequestQueueManager();
    	}
        return sInstance;  
    }  
  
    /** 
     * @return The Volley Request queue, the queue will be created if it is null 
     */  
	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(IocContainer.getShare()
					.getApplicationContext());
		}

		return mRequestQueue;
	} 
  
 
    // http client instance  
//	private DefaultHttpClient mHttpClient;
//	private static String mCookie;
//
//	public RequestQueue getRequestQueue() {
//		// lazy initialize the request queue, the queue instance will be
//		// created when it is accessed for the first time
//		if (mRequestQueue == null) {
//			// Create an instance of the Http client.
//			// We need this in order to access the cookie store
//			mHttpClient = new DefaultHttpClient();
//			// create the request queue
//			mRequestQueue = Volley.newRequestQueue(IocContainer.getShare()
//					.getApplicationContext(), new HttpClientStack(mHttpClient));
//		}
//		return mRequestQueue;
//	}  
//      
//    /** 
//     * Method to set a cookie 
//     */  
//    public void setCookie(String cookies) {  
//    	mCookie = (cookies==null) ? "":cookies;
//		if (null != mHttpClient) {
//			CookieStore cs = mHttpClient.getCookieStore();
//			// create a cookie
//			cs.addCookie(new BasicClientCookie2("Cookie", mCookie));
//		}
//    } 
//    
//    private static String getCookie() {
//		return mCookie;
//	}
  
    
    /** 
     * Adds the specified request to the global queue, if tag is specified 
     * then it is used else Default TAG is used. 
     *  
     * @param req 
     * @param tag 
     */  
    public <T> void addToRequestQueue(Request<T> req, String tag) {  
        // set the default tag if tag is empty  
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);  
  
        VolleyLog.d("Adding request to queue: %s", req.getUrl());  
  
        getRequestQueue().add(req);  
    }  
  
    /** 
     * Adds the specified request to the global queue using the Default TAG. 
     *  
     * @param req 
     * @param tag 
     */  
    public <T> void addToRequestQueue(Request<T> req) {  
        // set the default tag if tag is empty  
        req.setTag(TAG);  
  
        getRequestQueue().add(req);  
    }  
  
    /** 
     * Cancels all pending requests by the specified TAG, it is important 
     * to specify a TAG so that the pending/ongoing requests can be cancelled. 
     *  
     * @param tag 
     */  
    public void cancelPendingRequests(Object tag) {  
        if (mRequestQueue != null) {  
            mRequestQueue.cancelAll(tag);  
        }  
    }  
}
