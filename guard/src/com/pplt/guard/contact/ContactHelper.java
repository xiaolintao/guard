package com.pplt.guard.contact;

import android.content.Context;
import android.text.TextUtils;

import com.pplt.guard.R;
import com.pplt.guard.entity.Contact;

/**
 * Contact helper.
 */
public class ContactHelper {

	public static String getSummary(Context context, Contact contact) {
		StringBuffer buf = new StringBuffer();

		if (contact != null) {
			// 姓名
			// add(context, buf, R.string.contact_label_name,
			// contact.getName());

			// 手机号码
			add(context, buf, R.string.contact_label_phone, contact.getPhone());

			// email
			add(context, buf, R.string.contact_label_email, contact.getEmail());

			// 密防系统账号
			add(context, buf, R.string.contact_label_user, contact.getUser());

			// QQ
			add(context, buf, R.string.contact_label_qq, contact.getQq());

			// 新浪微博
			add(context, buf, R.string.contact_label_sina, contact.getSina());
		}

		return buf.toString();
	}

	private static void add(Context context, StringBuffer buf, int labelResId,
			String text) {
		if (TextUtils.isEmpty(text)) {
			return;
		}

		if (buf.length() != 0) {
			buf.append("\r\n");
		}

		buf.append(String.format("%s%s", context.getText(labelResId), text));
	}
}
