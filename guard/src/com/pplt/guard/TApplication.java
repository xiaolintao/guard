package com.pplt.guard;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.hipalsports.entity.FriendDetail;
import com.hipalsports.entity.UserInfo;
import com.jty.util.ToastHelper;
import com.jty.util.volley.VolleyCodeHelper;
import com.kingdom.sdk.db.DBHelper;
import com.kingdom.sdk.ioc.IocContainer;
import com.pplt.chat.EMChatHelper;
import com.pplt.guard.daemon.DaemonService;

public class TApplication extends Application {

	// ---------------------------------------------------- Private data
	final static String TAG = "Application";

	private BroadcastReceiver mReceiver; // 广播receiver

	// ---------------------------------------------------- Override methods
	@Override
	public void onCreate() {
		super.onCreate();

		// Global
		Global.init(this);
		Global.load();

		// Density
		DisplayMetrics dm = getResources().getDisplayMetrics();
		Log.d(TAG, "DisplayMetrics=" + dm);

		// IOC的初始化
		IocContainer.getShare().initApplication(this);

		// 图片缓存
		// BitmapCache.init(this);

		// database
		initDB();

		// daemon service
		startDaemon();

		// 环信
		EMChatHelper.init(this);

		// broadcast receiver
		registerBroadcastReceiver();
	}

	@Override
	public void onTerminate() {
		// daemon service
		stopDaemon();

		// 环信
		EMChatManager.getInstance().logout();

		// broadcast receiver
		unregisterBroadcastReceiver();

		super.onTerminate();
	}

	// ---------------------------------------------------- initial
	/**
	 * initial database.
	 */
	private void initDB() {
		// tables
		Class<?>[] list = new Class<?>[] { FriendDetail.class };
		DBHelper.addClass(list);

		// single instance
		DBHelper.setDatabaseName(Global.DB_FILENAME);
		DBHelper.createInstance(this);
	}

	// 启动daemon服务
	private void startDaemon() {
		Intent service = new Intent(this, DaemonService.class);
		startService(service);
	}

	// 停止daemon服务
	private void stopDaemon() {
		Intent service = new Intent(this, DaemonService.class);
		stopService(service);
	}

	// ---------------------------------------------------- broadcast
	/**
	 * register broadcast receiver.
	 */
	private void registerBroadcastReceiver() {
		if (mReceiver != null) {
			return;
		}

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// 登录
				if (intent.getAction().equals(Global.ACTION_LOGIN)) {
					dealLogin(intent);
				}

				// 退出登录
				if (intent.getAction().equals(Global.ACTION_LOGOUT)) {
					dealLogout(intent);
				}

				// volley异常
				if (intent.getAction().equals(Global.ACTION_VOLLEY_ABNORMAL)) {
					dealVolleyAbnormal(intent);
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.ACTION_LOGIN); // 登录
		filter.addAction(Global.ACTION_LOGOUT); // 退出登录
		filter.addAction(Global.ACTION_VOLLEY_ABNORMAL); // volley异常
		registerReceiver(mReceiver, filter);
	}

	/**
	 * unregister broadcast receiver.
	 */
	private void unregisterBroadcastReceiver() {
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	/**
	 * deal : 登录。
	 */
	private void dealLogin(Intent intent) {
		// check user information
		UserInfo userInfo = Global.getUser();
		if (userInfo == null) {
			return;
		}

		// 环信登录
		final Integer userId = userInfo.getUserId();
		final String pwd = EMChatHelper.getPwd(userId);
		EMChatLogin(userId + "", pwd);
	}

	/**
	 * deal : 退出登录。
	 */
	private void dealLogout(Intent intent) {
		// 环信
		EMChatManager.getInstance().logout();
	}

	/**
	 * deal : volley异常
	 */
	private void dealVolleyAbnormal(Intent intent) {
		int status = -1;
		if (intent.hasExtra("status")) {
			status = intent.getIntExtra("status", -1);
		}
		String hint = VolleyCodeHelper.getHint(this, status);
		ToastHelper.toast(this, hint);
	}

	// ---------------------------------------------------- 环信
	private final static int MESSAGE_SHOW = 100;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MESSAGE_SHOW) {
				int resId = msg.arg1;
				ToastHelper.toast(getApplicationContext(), resId);
			}
		}
	};

	/**
	 * 环信：登录。
	 */
	private void EMChatLogin(final String userId, final String pwd) {
		EMChatManager.getInstance().login(userId, pwd, new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "EMChatLogin onSuccess.");

				onEMChatLoginSuccess();
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(int code, String message) {
				if (code == EMError.INVALID_PASSWORD_USERNAME) {
					EMChatRegister(userId, pwd);
				} else {
					showMessage(R.string.chat_hint_login_fail);
				}
			}
		});
	}

	/**
	 * 环信：注册。
	 */
	private void EMChatRegister(final String userId, final String pwd) {
		EMChatHelper.register(userId, pwd, new EMCallBack() {

			@Override
			public void onSuccess() {
				EMChatLoginAgain(userId, pwd);
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(int code, String message) {
				showMessage(R.string.chat_hint_register_fail);
			}
		});
	}

	/**
	 * 环信：登录。
	 */
	private void EMChatLoginAgain(final String userId, final String pwd) {
		EMChatManager.getInstance().login(userId, pwd, new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "EMChatLoginAgain onSuccess.");

				onEMChatLoginSuccess();
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(int code, String message) {
				showMessage(R.string.chat_hint_login_fail);
			}
		});
	}

	/**
	 * 环信：login成功。
	 */
	private void onEMChatLoginSuccess() {
		// load
		EMGroupManager.getInstance().loadAllGroups();
		EMChatManager.getInstance().loadAllConversations();

		// listener
		setEMChatListener();
	}

	/**
	 * 环信：退出登录。
	 */
	void EMChatLogout() {
		EMChatManager.getInstance().logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "EMChatLogout onSuccess ");
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(int code, String message) {
				Log.d(TAG, "EMChatLogout onError: code =  " + code
						+ ",message = " + message);
			}
		});
	}

	/**
	 * 环信：listener.
	 */
	private void setEMChatListener() {
		// connect
		setEMConnectionListener();

		// contact
		setEMContactListener();
	}

	/**
	 * 环信：connect listener
	 */
	private void setEMConnectionListener() {
		EMConnectionListener listener = new EMConnectionListener() {

			@Override
			public void onDisconnected(final int error) {
				if (error == EMError.USER_REMOVED) {
					showMessage(R.string.chat_hint_user_removed);
				} else if (error == EMError.CONNECTION_CONFLICT) {
					showMessage(R.string.chat_hint_connect_conflict);
				}

				EMChatManager.getInstance().logout();
				Jump.sendLogoutBroadcast(getApplicationContext());
			}

			@Override
			public void onConnected() {
			}
		};

		EMChatManager.getInstance().addConnectionListener(listener);
	}

	/**
	 * 环信：contact listener
	 */
	private void setEMContactListener() {
		EMContactListener listener = new EMContactListener() {

			@Override
			public void onContactAdded(List<String> usernameList) {
				Log.d(TAG, "onContactAdded = " + usernameList);
			}

			@Override
			public void onContactDeleted(List<String> usernameList) {
				Log.d(TAG, "onContactDeleted = " + usernameList);
			}

			@Override
			public void onContactInvited(String username, String reason) {
				Log.d(TAG, "onContactInvited username = " + username
						+ ", reason=" + reason);
			}

			@Override
			public void onContactAgreed(String username) {
				Log.d(TAG, "onContactAgreed = " + username);
			}

			@Override
			public void onContactRefused(String username) {
				Log.d(TAG, "onContactRefused = " + username);
			}
		};

		EMContactManager.getInstance().setContactListener(listener);
	}

	private void showMessage(int resId) {
		Message msg = mHandler.obtainMessage();
		msg.what = MESSAGE_SHOW;
		msg.arg1 = resId;

		mHandler.sendMessage(msg);
	}
}
