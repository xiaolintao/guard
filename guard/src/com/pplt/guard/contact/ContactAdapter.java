package com.pplt.guard.contact;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pplt.guard.R;
import com.pplt.guard.entity.Contact;

/**
 * 联系人：adapter。
 */
public class ContactAdapter extends BaseAdapter {

	// ----------------------------------------------- Private data
	private LayoutInflater mInflater; // layout inflater

	private List<Contact> mData; // data

	// ----------------------------------------------- Constructor & Setting
	public ContactAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<Contact> data) {
		mData = data;

		notifyDataSetChanged();
	}

	// ----------------------------------------------- Override methods
	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public Contact getItem(int position) {
		return position >= 0 && position <= getCount() - 1 ? mData
				.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
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

		Contact entity = getItem(position);

		// 姓名
		holder.nameTv.setText(entity.getName());

		// 删除
		holder.deleteTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		// 浏览
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		return convertView;
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

	class Holder {
		TextView nameTv; // 姓名

		TextView summaryTv; // 摘要

		TextView deleteTv; // 删除

		public Holder(View convertView) {
			nameTv = (TextView) convertView.findViewById(R.id.tv_name);

			summaryTv = (TextView) convertView.findViewById(R.id.tv_summary);

			deleteTv = (TextView) convertView.findViewById(R.id.tv_delete);
		}
	}

}
