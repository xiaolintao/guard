package com.pplt.guard.chat;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.pplt.guard.Global;
import com.pplt.guard.R;

/**
 * 聊天：adapter。
 */
public class EMChatAdapter extends BaseAdapter {

	// ----------------------------------------------- Private data
	private LayoutInflater mInflater; // layout inflater

	private List<EMMessage> mData = new ArrayList<EMMessage>(); // data

	// ----------------------------------------------- Constructor & Setting
	public EMChatAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<EMMessage> data) {
		if (data != null) {
			mData = data;
		} else {
			mData = new ArrayList<EMMessage>();
		}

		notifyDataSetChanged();
	}

	public void setData(EMMessage message) {
		if (message != null) {
			mData.add(0, message);
		}

		notifyDataSetChanged();
	}

	// ----------------------------------------------- Override methods
	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public EMMessage getItem(int position) {
		return position >= 0 && position <= getCount() - 1 ? mData
				.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final EMMessage entity = getItem(position);

		int resourceId = isMine(entity) ? R.layout.chat_list_item_of_right
				: R.layout.chat_list_item_of_left;
		convertView = mInflater.inflate(resourceId, null);
		Holder holder = new Holder(convertView);

		// 头像

		// 内容
		String content = EMChatHelper.getContent(entity);
		holder.contentTv.setText(content);

		return convertView;
	}

	// ----------------------------------------------- Private methods
	class Holder {
		ImageView photoIv; // 头像

		TextView contentTv; // 内容

		public Holder(View convertView) {
			photoIv = (ImageView) convertView.findViewById(R.id.iv_photo);

			contentTv = (TextView) convertView.findViewById(R.id.tv_content);
		}
	}

	private boolean isMine(EMMessage entity) {
		int userId = Global.getUser().getUserId();

		return entity.getFrom().compareToIgnoreCase(userId + "") == 0;
	}
}
