package com.pplt.guard.personal;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pplt.guard.comm.entity.UserTagEntity;
import com.pplt.guard.R;

/**
 * Adapter: 用户标签。
 */
public class UserTagAdapter extends BaseAdapter {

	// ---------------------------------------------------- Private data
	private LayoutInflater mInflater;

	private List<UserTagEntity> mData = new ArrayList<UserTagEntity>();

	private List<Long> mSelectedIds = new ArrayList<Long>(); // "已选中的"id

	// -------------------------------------------------- Constructor & Setting
	/**
	 * Constructor.
	 * 
	 * @param context
	 *            context.
	 */
	public UserTagAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * 设置数据。
	 * 
	 * @param data
	 *            数据。
	 */
	public void setData(List<UserTagEntity> data) {
		mData = data;

		notifyDataSetChanged();
	}

	/**
	 * 选中item。
	 * 
	 * @param id
	 *            item的id.
	 */
	public void select(long id) {
		addSelected(id);

		notifyDataSetChanged();
	}

	/**
	 * 是否被选中。
	 * 
	 * @param id
	 *            item的id.
	 * @return 是否被选中。
	 */
	public boolean isSelected(long id) {
		return mSelectedIds.indexOf(id) != -1;
	}

	/**
	 * 获取选中item的id.
	 * 
	 * @return 选中item的id.
	 */
	public List<Long> getSelectedIds() {
		return mSelectedIds;
	}

	// ---------------------------------------------------- Override methods
	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public UserTagEntity getItem(int position) {
		return position >= 0 && position < getCount() ? mData.get(position)
				: null;
	}

	@Override
	public long getItemId(int position) {
		UserTagEntity entity = getItem(position);
		return entity != null ? entity.getId() : -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.personal_profile_user_tag_item, null);
		}

		UserTagEntity entity = getItem(position);

		TextView tagTv = (TextView) convertView;
		tagTv.setText(entity.getName());

		boolean isSelected = isSelected(entity.getId());
		int bgResId = isSelected ? R.drawable.user_tag_bg_pressed
				: R.drawable.user_tag_bg_normal;
		setBackgroundResource(tagTv, bgResId);

		return convertView;
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 添加/去除"选中"。
	 * 
	 * @param id
	 *            item的id.
	 */
	private void addSelected(long id) {
		int location = mSelectedIds.indexOf(id);
		if (location == -1) {
			mSelectedIds.add(id);
		} else {
			mSelectedIds.remove(location);
		}
	}

	/**
	 * set background resource
	 * 
	 * @param view
	 *            view.
	 * @param resId
	 *            resource id.
	 */
	private void setBackgroundResource(View view, int resId) {
		int left = view.getPaddingLeft();
		int top = view.getPaddingTop();
		int right = view.getPaddingRight();
		int bottom = view.getPaddingBottom();

		view.setBackgroundResource(resId);

		view.setPadding(left, top, right, bottom);
	}
}
