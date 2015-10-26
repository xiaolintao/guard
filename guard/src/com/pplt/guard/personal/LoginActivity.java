package com.pplt.guard.personal;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.android.volley.Response;
import com.hipalsports.entity.UserInfo;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.Global;
import com.pplt.guard.Jump;
import com.pplt.guard.R;
import com.pplt.guard.comm.api.AccountAPI;
import com.pplt.guard.comm.response.ResponseCodeHelper;
import com.pplt.guard.comm.response.ResponseParser;
import com.pplt.guard.personal.checker.AccountChecker;
import com.pplt.guard.personal.checker.InputChecker;
import com.pplt.guard.personal.pwd.RetrievePwdActivity;

/**
 * 登录。
 */
public class LoginActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	private final static int REQUEST_CODE_REGISTER = 100; // 注册

	/** 第三方登录 */
	private static final int MSG_AUTH_COMPLETE = 1;
	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR = 3;

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.et_account)
	private EditText mAccountEt; // 账号：手机&邮箱

	@InjectView(id = R.id.et_pwd)
	private EditText mPwdEt; // 密码

	@InjectView(id = R.id.tv_login, click = "login")
	private TextView mLoginTv; // 登录按钮

	@InjectView(id = R.id.tv_register, click = "register")
	private TextView mRegisterTv; // 注册按钮

	@InjectView(id = R.id.tv_forget_pwd, click = "forgetPwd")
	private TextView mForgetPwdTv; // 忘记密码按钮

	@InjectView(id = R.id.iv_weixin, click = "weixinLogin")
	private View mWeiXinIv; // 微信登录

	@InjectView(id = R.id.iv_sina, click = "sinaLogin")
	private View mSinaIv; // 新浪微博登录

	@InjectView(id = R.id.iv_qq, click = "qqLogin")
	private View mQQIv; // QQ登录

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_login_view);

		// IOC
		InjectUtil.inject(this);

		// initial
		initViews();

		// Share SDK
		ShareSDK.initSDK(this);

		// test : 账号&密码
		mAccountEt.setText("369024496@qq.com");
		mAccountEt.setSelection(mAccountEt.getText().length());
		mAccountEt.requestFocus();
		mPwdEt.setText("111");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_REGISTER) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial view.
	 */
	private void initViews() {
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 检查输入。
	 * 
	 * @return 输入是否完整。
	 */
	private boolean checkInput() {
		// 账号
		if (!AccountChecker.check(this, mAccountEt)) {
			return false;
		}

		// 密码
		if (!InputChecker.check(this, mPwdEt)) {
			return false;
		}

		return true;
	}

	/**
	 * 登录。
	 */
	public void login() {
		// check input
		if (!checkInput()) {
			return;
		}

		// listener
		Response.Listener<String> listener = new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (isFinishing() || response == null) {
					return;
				}

				dealLoginResponse(response);
			}
		};

		// request
		String account = mAccountEt.getText().toString();
		String pwd = mPwdEt.getText().toString();
		AccountAPI.login(this, account, pwd, listener);
	}

	/**
	 * deal : 登录响应包。
	 * 
	 * @param response
	 *            响应包。
	 */
	private void dealLoginResponse(String response) {
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			UserInfo user = ResponseParser.parse(response, UserInfo.class);
			Global.setUser(user);

			Jump.toMain(this);
			finish();
			return;
		}

		// fail
		String hint = ResponseCodeHelper.getHint(this, code);
		ToastHelper.toast(this, hint);
	}

	/**
	 * 注册。
	 */
	public void register() {
		Intent intent = new Intent(this, RegisterActivity.class);

		startActivityForResult(intent, REQUEST_CODE_REGISTER);
	}

	/**
	 * 忘记密码。
	 */
	public void forgetPwd() {
		Intent intent = new Intent(this, RetrievePwdActivity.class);

		startActivity(intent);
	}

	// ---------------------------------------------------- 第三方登录
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_AUTH_COMPLETE: // 授权成功
				toast(R.string.third_login_hint_auth_complete);

				Object[] objs = (Object[]) msg.obj;
				String platform = (String) objs[0];
				HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
				dealThirdLogin(platform, res);
				break;
			case MSG_AUTH_CANCEL: // 授权 取消
				toast(R.string.third_login_hint_auth_cancel);
				break;
			case MSG_AUTH_ERROR: // 授权失败
				toast(R.string.third_login_hint_auth_error);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 微信登录。
	 */
	public void weixinLogin() {
		Platform platform = ShareSDK.getPlatform(Wechat.NAME);
		authorize(platform);
	}

	/**
	 * 新浪微博登录。
	 */
	public void sinaLogin() {
		Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
		authorize(platform);
	}

	/**
	 * QQ登录
	 */
	public void qqLogin() {
		Platform platform = ShareSDK.getPlatform(QQ.NAME);
		authorize(platform);
	}

	// 执行授权,获取用户信息
	private void authorize(Platform plat) {
		if (plat == null) {
			return;
		}

		PlatformActionListener listener = new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int action,
					HashMap<String, Object> res) {
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = MSG_AUTH_COMPLETE;
					msg.obj = new Object[] { platform.getName(), res };
					handler.sendMessage(msg);
				}
			}

			@Override
			public void onCancel(Platform platform, int action) {
				if (action == Platform.ACTION_USER_INFOR) {
					handler.sendEmptyMessage(MSG_AUTH_CANCEL);
				}
			}

			@Override
			public void onError(Platform platform, int action, Throwable t) {
				if (action == Platform.ACTION_USER_INFOR) {
					handler.sendEmptyMessage(MSG_AUTH_ERROR);
				}

				if (t != null) {
					t.printStackTrace();
				}
			}
		};

		plat.setPlatformActionListener(listener);
		plat.SSOSetting(true); // 关闭SSO授权
		plat.showUser(null);
	}

	/**
	 * 第三方授权成功。
	 * 
	 * @param platform
	 *            平台名称。
	 * @param res
	 */
	private void dealThirdLogin(String platformName, HashMap<String, Object> res) {
		Platform platform = ShareSDK.getPlatform(platformName);
		PlatformDb db = platform.getDb();

		// listener
		Response.Listener<String> listener = new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (isFinishing() || response == null) {
					return;
				}

				dealLoginResponse(response);
			}
		};

		// request
		String userId = db.getUserId();
		String userName = db.getUserName();
		String gender = db.getUserGender();
		String userIcon = db.getUserIcon();
		AccountAPI.thirdLogin(this, userId, userName, gender, userIcon,
				listener);
	}

	private void toast(int resid) {
		ToastHelper.toast(this, resid);
	}
}
