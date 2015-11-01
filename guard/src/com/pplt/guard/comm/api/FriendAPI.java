package com.pplt.guard.comm.api;

import android.content.Context;

import com.android.volley.Response;
import com.jty.util.volley.VolleyHelper;

/**
 * 通信：好友。
 */
public class FriendAPI extends BaseAPI {

	/**
	 * 获取好友列表。
	 * 
	 * @param context
	 *            context.
	 * @param userId
	 *            用户id.
	 * @param lastUpdateTime
	 *            最后更新时间。
	 * @param offset
	 *            offset.
	 * @param length
	 *            length.
	 * @param listener
	 *            volley response listener.
	 */
	public static void getFriends(Context context, Integer userId,
			String lastUpdateTime, Integer offset, Integer length,
			Response.Listener<String> listener) {

		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("lastUpdateTime", lastUpdateTime);
		params.put("offset", offset);
		params.put("length", length);

		VolleyHelper.post(context, BASE_URL + "friends/getFriends",
				params.toString(), listener);
	}

}
