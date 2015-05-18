package com.pplt.guard.comm;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * HTTP parameters。
 * 
 */
public class HttpParameters extends HttpUrlBase {

	// ---------------------------------------------------- Constants
	/** CODE TYPE */
	public final static int CODE_TYPE_REGISTER = 1; // 注册
	public final static int CODE_TYPE_FORGET_PWD = 2; // 找回密码

	// ---------------------------------------------------- Constructor
	private HttpParameters() {
	}

	// ---------------------------------------------------- Public methods: user
	/**
	 * 获取手机验证码。
	 * 
	 * @param phoneNum
	 *            手机号码。
	 * @param codeType
	 *            验证码类型（1：注册、2：找回密码）。
	 * @return parameters。
	 */
	public static String getVerifyPhone(String phoneNum, int codeType) {
		List<String> params = new ArrayList<String>();

		addStringParam(params, "phone_num", phoneNum);
		addIntParam(params, "code_type", codeType);

		return getParams(params.toArray(new String[] {}));
	}

	/**
	 * 注册。
	 * 
	 * @param phoneNum
	 *            手机号码。
	 * @param pwd
	 *            密码（md5）。
	 * @param verifyCode
	 *            手机验证码。
	 * @return parameters。
	 */
	public static String register(String phoneNum, String pwd, String verifyCode) {
		List<String> params = new ArrayList<String>();

		// md5
		pwd = toHex(getMD5(pwd));

		addStringParam(params, "phone_num", phoneNum);
		addStringParam(params, "passwd", pwd);
		addStringParam(params, "repasswd", pwd);
		addStringParam(params, "p_code", verifyCode);

		return getParams(params.toArray(new String[] {}));
	}

	/**
	 * 登录。
	 * 
	 * @param name
	 *            用户昵称 or 手机号码。
	 * @param pwd
	 *            密码。
	 * @param remember
	 *            是否记住登陆态（0/1）。
	 * @param verifyCode
	 *            验证码（账密输入错误3次必须此字段）。
	 * @return parameters。
	 */
	public static String login(String name, String pwd, int remember,
			String verifyCode) {
		List<String> params = new ArrayList<String>();

		// md5
		pwd = toHex(getMD5(pwd));

		addStringParam(params, "name", name);
		addStringParam(params, "passwd", pwd);
		addIntParam(params, "remember", remember);
		addStringParam(params, "t_code", verifyCode);

		return getParams(params.toArray(new String[] {}));
	}

	/**
	 * 完善用户资料。
	 * 
	 * @param uid
	 *            用户id。
	 * @param name
	 *            用户昵称。
	 * @param tags
	 *            用户标签id（用‘,’分割）
	 * @return parameters。
	 */
	public static String profile(int uid, String name, String tags) {
		List<String> params = new ArrayList<String>();

		addIntParam(params, "uid", uid);
		addStringParam(params, "name", name);
		// addStringParam(params, "tags", tags);

		return getParams(params.toArray(new String[] {}));
	}

	// ------------------------------------------------ Public methods: password
	/**
	 * 找回密码验证手机号。
	 * 
	 * @param phoneNum
	 *            手机号码。
	 * @param verifyCode
	 *            验证码。
	 * @return parameters。
	 */
	public static String retrievePwdVerify(String phoneNum, String verifyCode) {
		List<String> params = new ArrayList<String>();

		addStringParam(params, "phone_num", phoneNum);
		addStringParam(params, "p_code", verifyCode);

		return getParams(params.toArray(new String[] {}));
	}

	/**
	 * 找回密码。
	 * 
	 * @param phoneNum
	 *            手机号码。
	 * @param pwd
	 *            新密码。
	 * @return parameters。
	 */
	public static String retrievePwdFind(String phoneNum, String pwd) {
		List<String> params = new ArrayList<String>();

		// md5
		pwd = toHex(getMD5(pwd));

		addStringParam(params, "phone_num", phoneNum);
		addStringParam(params, "passwd", pwd);
		addStringParam(params, "repasswd", pwd);

		return getParams(params.toArray(new String[] {}));
	}

	/**
	 * 修改密码。
	 * 
	 * @param oldPwd
	 *            旧密码。
	 * @param newPwd
	 *            新密码。
	 * @return parameters。
	 */
	public static String changePwd(int mid, String oldPwd, String newPwd) {
		List<String> params = new ArrayList<String>();

		// md5
		oldPwd = toHex(getMD5(oldPwd));
		newPwd = toHex(getMD5(newPwd));

		addIntParam(params, "mid", mid);
		addStringParam(params, "oldpasswd", oldPwd);
		addStringParam(params, "passwd", newPwd);
		addStringParam(params, "repasswd", newPwd);

		return getParams(params.toArray(new String[] {}));
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 计算MD5。
	 * 
	 * @param data
	 *            数据。
	 * @return MD5。
	 */
	private static byte[] getMD5(String data) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data.getBytes("UTF-8"));
			return md5.digest();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将byte数组转换成16进制形式的字符串。
	 * 
	 * @param data
	 *            byte数组.
	 * @return 16进制形式的字符串。
	 */
	private static String toHex(byte[] data) {
		StringBuffer buf = new StringBuffer();

		if (data == null) {
			return null;
		}

		for (byte element : data) {
			buf.append(String.format("%02x", element));
		}

		return buf.toString();
	}
}
