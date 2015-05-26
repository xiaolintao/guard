package com.pplt.guard;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.jty.util.JSonUtils;
import com.pplt.guard.contact.ContactChoiceActivity;
import com.pplt.guard.contact.ContactEditActivity;
import com.pplt.guard.entity.Contact;

/**
 * Activity跳转。
 */
public class Jump {

	// ---------------------------------------------------- Public methods
	/**
	 * 跳转：编辑联系人。
	 * 
	 * @param context
	 *            context.
	 * @param contact
	 *            联系人。
	 */
	public static void toContactEdit(Context context, Contact contact) {
		Intent intent = new Intent(context, ContactEditActivity.class);

		if (contact != null) {
			String json = JSonUtils.toJSon(contact);
			intent.putExtra(Global.EXTRA_CONTACT, json);
		}

		context.startActivity(intent);
	}

	/**
	 * 跳转：选择联系人。
	 * 
	 * @param context
	 *            context.
	 * @param ids
	 *            ","号分隔的联系人id。
	 */
	public static void toContactChoice(Context context, String ids) {
		Intent intent = new Intent(context, ContactChoiceActivity.class);

		if (!TextUtils.isEmpty(ids)) {
			intent.putExtra(Global.EXTRA_IDS, ids);
		}

		context.startActivity(intent);
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 跳转：fragment.
	 * 
	 * @param titleResId
	 *            标题resource id.
	 * @param fname
	 *            fragment名称。
	 */
	public static void toFragment(Context context, int titleResId,
			String fname) {
		String title = context.getResources().getText(titleResId).toString();

		WrapperActivity.start(context, title, fname);
	}
}
