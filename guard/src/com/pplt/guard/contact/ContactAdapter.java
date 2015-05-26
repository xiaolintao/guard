package com.pplt.guard.contact;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jty.util.ToastHelper;
import com.pplt.guard.Jump;
import com.pplt.guard.R;
import com.pplt.guard.entity.Contact;
import com.pplt.guard.entity.ContactDataHelper;
import com.pplt.ui.DlgHelper;

/**
 * 联系人：adapter。
 */
public class ContactAdapter extends BaseAdapter {

	// ----------------------------------------------- Private data
	private Context mContext; // context
	private LayoutInflater mInflater; // layout inflater

	private List<Contact> mData; // data

	// ----------------------------------------------- Constructor & Setting
	public ContactAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<Contact> data) {
		mData = data;

		notifyDataSetChanged();
	}

	public void remove(long id) {
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

		final Contact entity = getItem(position);

		// 姓名
		holder.nameTv.setText(entity.getName());

		// 摘要
		String summary = ContactHelper.getSummary(mContext, entity);
		holder.summaryTv.setText(summary);

		// 删除
		holder.deleteTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				delete(entity);
			}
		});

		// 浏览
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Jump.toContactEdit(mContext, entity);
			}
		});

		return convertView;
	}

	// ----------------------------------------------- Private methods
	/**
	 * 删除。
	 * 
	 * @param contact
	 *            contact.
	 */
	private void delete(final Contact contact) {
		String format = mContext.getText(R.string.contact_hint_delete)
				.toString();
		String message = String.format(format, contact.getName());

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				long id = contact.getId();
				if (ContactDataHelper.delete(id) != 0) {
					remove(id);
				}else {
					ToastHelper.toast(mContext, R.string.delete_fail);
				}
			}
		};

		DlgHelper.showAlertDialog(mContext, R.string.contact_title_delete, message, listener);
	}

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
