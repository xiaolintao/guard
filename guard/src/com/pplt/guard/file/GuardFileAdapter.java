package com.pplt.guard.file;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pplt.guard.Jump;
import com.pplt.guard.R;
import com.pplt.guard.entity.GuardFile;

/**
 * 密防文件：adapter。
 */
public class GuardFileAdapter extends BaseAdapter {

	// ----------------------------------------------- Private data
	private Context mContext; // context
	private LayoutInflater mInflater; // layout inflater

	private List<GuardFile> mData; // data

	// ----------------------------------------------- Constructor & Setting
	public GuardFileAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<GuardFile> data) {
		mData = data;

		notifyDataSetChanged();
	}

	// ----------------------------------------------- Override methods
	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public GuardFile getItem(int position) {
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
			convertView = mInflater
					.inflate(R.layout.guard_file_list_item, null);

			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final GuardFile entity = getItem(position);

		// 文件名
		holder.filenameTv.setText(entity.getFileName());

		// 摘要
		holder.summaryTv.setText(entity.getSummary());

		// 授权
		holder.authTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Jump.toGuardFileAuth(mContext, entity.getId());
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
		TextView filenameTv; // 文件名

		TextView summaryTv; // 摘要

		TextView authTv; // 授权

		public Holder(View convertView) {
			filenameTv = (TextView) convertView.findViewById(R.id.tv_filename);

			summaryTv = (TextView) convertView.findViewById(R.id.tv_summary);

			authTv = (TextView) convertView.findViewById(R.id.tv_auth);
		}
	}

}
