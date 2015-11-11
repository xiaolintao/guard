package com.pplt.guard.chat;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.hipalsports.entity.FriendDetail;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.Global;
import com.pplt.guard.R;
import com.pplt.ui.DlgHelper;
import com.pplt.ui.EmbededListView;
import com.pplt.ui.TitleBar;

/**
 * 聊天界面。
 */
public class EMChatActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	static String TAG = "EMChatAcitivity";

	/** 消息 */
	private final static int MESSAGE_REFRESH = 100; // 刷新
	private final static int MESSAGE_SEND_FAIL = 101; // 发送出错

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.list_view)
	private EmbededListView mListView; // list view

	@InjectView(id = R.id.et_content)
	private EditText mContentEt; // 内容

	@InjectView(id = R.id.tv_send, click = "sendMessage")
	private TextView mSendTv; // 发送按钮

	private EMChatAdapter mAdapter; // adapter

	private FriendDetail mFriend; // 好友信息

	private BroadcastReceiver mReceiver;

	@SuppressLint("HandlerLeak")
	private Handler mHander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_REFRESH: // 刷新
				refresh();
				break;
			case MESSAGE_SEND_FAIL: // 发送出错
				ToastHelper.toast(EMChatActivity.this,
						R.string.chat_hint_send_message_fail);
				break;
			default:
				break;
			}
		}
	};

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_view);

		// Extra
		Intent intent = getIntent();
		if (intent.hasExtra(Global.EXTRA_FRIEND_DETAIL)) {
			String data = intent.getStringExtra(Global.EXTRA_FRIEND_DETAIL);
			mFriend = JSON.parseObject(data, FriendDetail.class);
		}
		if (mFriend == null) {
			return;
		}

		// IOC
		InjectUtil.inject(this);

		// initial
		initTitleBar();
		initViews();

		// broadcast
		registerBroadcastReceiver();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// refresh
		refresh();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// broadcast
		unregisterBroadcastReceiver();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial title bar
	 */
	private void initTitleBar() {
		// title
		mTitleBar.setTitleText(mFriend.getNickName());

		// right button
		mTitleBar.setRightBtnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				clear();
			}
		});
	}

	/**
	 * initial view.
	 */
	private void initViews() {
		// adapter
		mAdapter = new EMChatAdapter(this);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 刷新。
	 */
	private void refresh() {
		int friendUserId = mFriend.getFriendUserId();
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(friendUserId + "");
		List<EMMessage> messages = conversation.getAllMessages();

		mAdapter.setData(messages);

		mListView.setSelection(mListView.getBottom());
	}

	/**
	 * 发送消息。
	 */
	public void sendMessage() {
		String content = mContentEt.getText().toString();
		if (TextUtils.isEmpty(content)) {
			ToastHelper.toast(this, R.string.chat_hint_input_content);
			return;
		}

		EMCallBack callback = new EMCallBack() {

			@Override
			public void onError(int code, String message) {
				Log.d(TAG, "onError code=" + code + ",message=" + message);
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onSuccess() {
				mHander.sendEmptyMessage(MESSAGE_REFRESH);
			}
		};

		int friendUserId = mFriend.getFriendUserId();
		EMChatHelper.sendMessage(friendUserId + "", content, callback);
	}

	/**
	 * 清空消息记录
	 */
	private void clearMessages() {
		int friendUserId = mFriend.getFriendUserId();
		EMChatManager.getInstance().clearConversation(friendUserId + "");

		refresh();
	}

	/**
	 * 清空。
	 */
	private void clear() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				clearMessages();
			}
		};

		DlgHelper.showAlertDialog(this, R.string.clear,
				R.string.chat_hint_clear_messages, listener);
	}

	// ---------------------------------------------------- broadcast
	private void registerBroadcastReceiver() {
		if (mReceiver != null) {
			return;
		}

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d(TAG, "onReceive.");

				refresh();
			}
		};

		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(mReceiver, intentFilter);
	}

	private void unregisterBroadcastReceiver() {
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
}
