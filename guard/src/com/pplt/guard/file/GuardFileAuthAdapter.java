package com.pplt.guard.file;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pplt.guard.R;
import com.pplt.guard.contact.ContactHelper;
import com.pplt.guard.entity.Contact;

/**
 * 联系人：adapter。
 */
public class GuardFileAuthAdapter extends BaseAdapter {

	// ----------------------------------------------- Private data
	private Context mContext; // context
	private LayoutInflater mInflater; // layout inflater

	private List<Contact> mData; // data

	public interface OnClickItemListener {
		void onReverse(Contact contact);
	}

	private OnClickItemListener mListener;

	// ----------------------------------------------- Constructor & Setting
	public GuardFileAuthAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<Contact> data) {
		mData = data;

		notifyDataSetChanged();
	}

	public void appendData(List<Contact> data) {
		if (mData != null) {
			mData.addAll(data);
		} else {
			mData = data;
		}

		notifyDataSetChanged();
	}

	public void setOnClickItemListener(OnClickItemListener listener) {
		mListener = listener;
	}

	/**
	 * 获取：授权。
	 * 
	 * @return 授权的联系人id.
	 */
	public List<Long> getAuthTo() {
		List<Long> ids = new ArrayList<Long>();
		if (mData != null && mData.size() != 0) {
			for (Contact contact : mData) {
				ids.add(contact.getId());
			}
		}

		return ids;
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
			convertView = mInflater.inflate(R.layout.guard_file_auth_list_item,
					null);

			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final Contact entity = getItem(position);

		// 姓名
		holder.nameTv.setText(entity.getName());

		// 摘要
		String summary = ContactHelper.getSummary(mContext, entity);
		holder.summaryTv.setText(summary);

		// 取消授权
		holder.reverseTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				remove(entity.getId());

				if (mListener != null) {
					mListener.onReverse(entity);
				}
			}
		});

		return convertView;
	}

	// ----------------------------------------------- Private methods
	/**
	 * 移除。
	 * 
	 * @param id
	 *            联系人id.
	 */
	private void remove(long id) {
		if (mData == null || mData.size() == 0) {
			return;
		}

		for (Contact contact : mData) {
			if (contact.getId() == id) {
				mData.remove(contact);
				break;
			}
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

	class Holder {
		TextView nameTv; // 姓名
		TextView summaryTv; // 摘要

		TextView reverseTv; // 取消授权

		public Holder(View convertView) {
			nameTv = (TextView) convertView.findViewById(R.id.tv_name);
			summaryTv = (TextView) convertView.findViewById(R.id.tv_summary);

			reverseTv = (TextView) convertView.findViewById(R.id.tv_reverse);
		}
	}

}
