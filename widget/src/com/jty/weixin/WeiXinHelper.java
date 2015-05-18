package com.jty.weixin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

/**
 * 微信helper.
 */
public class WeiXinHelper {

	// ---------------------------------------------------- Constants
	public static final String APP_ID = "wx4fc8531ac2b9ae86";

	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001; // 支持分享到朋友圈的API版本

	private IWXAPI api;

	// ---------------------------------------------------- Constructor
	/**
	 * 构造函数。
	 * 
	 * @param context
	 *            context.
	 */
	public WeiXinHelper(Context context) {
		api = WXAPIFactory.createWXAPI(context, APP_ID, false);
		api.registerApp(APP_ID);
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 是否已安装微信。
	 * 
	 * @return 已安装 or not.
	 */
	public boolean isWXAppInstalled() {
		return api != null ? api.isWXAppInstalled() : false;
	}

	/**
	 * 打开微信。
	 * 
	 * @return 返回值。
	 */
	public boolean openWXApp() {
		return api != null ? api.openWXApp() : false;
	}

	/**
	 * API版本是否支持分享到朋友圈。
	 * 
	 * @return 支持 or not.
	 */
	public boolean isWXAppSupportAPI() {
		if (api == null) {
			return false;
		}

		int wxSdkVersion = api.getWXAppSupportAPI();
		return wxSdkVersion >= TIMELINE_SUPPORTED_VERSION;
	}

	/**
	 * 分享：文本。
	 * 
	 * @param text
	 *            文本。
	 * @return 返回值。
	 */
	public boolean sendText(String text) {

		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene = getWXScene();

		// 调用api接口发送数据到微信
		return api.sendReq(req);
	}

	/**
	 * 分享：图片。
	 * 
	 * @param path
	 *            文件路径。
	 * @param thumbWidth
	 *            缩略图宽度。
	 * @param thumbHeight
	 *            缩略图高度。
	 * @return 返回值。
	 */
	public boolean sendImage(String path, int thumbWidth, int thumbHeight) {
		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(path);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap bmp = BitmapFactory.decodeFile(path);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, thumbWidth,
				thumbHeight, true);
		bmp.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = getWXScene();

		return api.sendReq(req);
	}

	/**
	 * 发送一个web链接。
	 * 
	 * @param webUrl
	 *            URL。
	 * @param title
	 *            标题。
	 * @param description
	 *            描述。
	 * @param thumb
	 *            缩略图。
	 * @return 返回值。
	 */
	public boolean sendWebPage(String webUrl, String title, String description,
			Bitmap thumb) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = webUrl;

		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;

		if (thumb != null) {
			msg.thumbData = Util.bmpToByteArray(thumb, true);
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = getWXScene();

		return api.sendReq(req);
	}

	// ---------------------------------------------------- Private methods
	private int getWXScene() {
		return SendMessageToWX.Req.WXSceneSession; // SendMessageToWX.Req.WXSceneTimeline
	}

	private String buildTransaction(final String type) {
		return type == null ? String.valueOf(System.currentTimeMillis()) : type
				+ System.currentTimeMillis();
	}

}
