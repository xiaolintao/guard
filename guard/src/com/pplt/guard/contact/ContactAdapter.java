package com.pplt.guard.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hipalsports.entity.FriendDetail;
import com.pplt.guard.R;

/**
 * 联系人：adapter。
 */
public class ContactAdapter extends BaseAdapter {

	// ----------------------------------------------- Constants
	/** 模式 */
	public final static int MODE_BROWSE = 0; // 浏览
	public final static int MODE_CHOICE = 1; // 选择

	// ----------------------------------------------- Private data
	private LayoutInflater mInflater; // layout inflater

	private List<FriendDetail> mData; // data

	private int mMode = MODE_BROWSE;
	private List<Long> mSelectedIds = new ArrayList<Long>();

	// ----------------------------------------------- Constructor & Setting
	public ContactAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<FriendDetail> data) {
		mData = data;

		notifyDataSetChanged();
	}

	public void setMode(int mode) {
		mMode = mode;

		notifyDataSetChanged();
	}

	public void setSelection(List<Long> ids) {
		mSelectedIds = ids;

		notifyDataSetChanged();
	}

	public List<Long> getSelection() {
		return mSelectedIds;
	}

	// ----------------------------------------------- Override methods
	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public FriendDetail getItem(int position) {
		return position >= 0 && position <= getCount() - 1 ? mData
				.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		FriendDetail item = getItem(position);

		return item != null ? item.getId() : -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contact_list_item, null);

			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final FriendDetail entity = getItem(position);

		// 头像

		// 昵称
		holder.nickNameTv.setText(entity.getNickName());

		// 选择
		holder.selectCb.setVisibility(mMode == MODE_CHOICE ? View.VISIBLE
				: View.GONE);
		final boolean isSelected = isSelected(entity.getId());
		holder.selectCb.setChecked(isSelected);

		// convert view
		boolean enable = mMode != MODE_BROWSE;
		enable(convertView, enable);
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMode == MODE_CHOICE) {
					switchSelection(entity.getId());
				}
			}
		});

		return convertView;
	}

	// ----------------------------------------------- Private methods
	/**
	 * 是否被选中。
	 * 
	 * @param id
	 *            联系人id.
	 * @return 是否被选中。
	 */
	private boolean isSelected(long id) {
		for (Long item : mSelectedIds) {
			if (item == id) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 切换选中状态。
	 * 
	 * @param id
	 *            联系人id.
	 */
	private void switchSelection(long id) {
		boolean has = false;
		for (Long item : mSelectedIds) {
			if (item == id) {
				has = true;
				mSelectedIds.remove(item);
				break;
			}
		}

		if (!has) {
			mSelectedIds.add(id);
		}

		notifyDataSetChanged();
	}

	// ----------------------------------------------- Private methods
	/**
	 * 设置visibility.
	 */
	void setVisibility(View convertView, int resId, int visibility) {
		View view = convertView.findViewById(resId);
		if (view != null) {
			view.setVisibility(visibility);
		}
	}

	/**
	 * 设置enable.
	 */
	private void enable(View view, boolean enable) {
		view.setEnabled(enable);
		view.setFocusable(enable);
		view.setClickable(enable);
	}

	class Holder {
		ImageView photoIv; // 头像
		TextView nickNameTv; // 昵称

		ToggleButton selectCb; // 选择
		View divider; // 分隔线

		public Holder(View convertView) {
			photoIv = (ImageView) convertView.findViewById(R.id.iv_photo);
			nickNameTv = (TextView) convertView.findViewById(R.id.tv_name);

			selectCb = (ToggleButton) convertView.findViewById(R.id.cb_select);
			divider = convertView.findViewById(R.id.iv_divider);
		}
	}

}
