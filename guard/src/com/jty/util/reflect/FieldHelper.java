package com.jty.util.reflect;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.jty.util.DateHelper;

/**
 * helper reflect field value to control.
 * 
 */
public class FieldHelper {

	// ---------------------------------------------------- Constructor.
	private FieldHelper() {
	}

	// ---------------------------------------------------- check
	/**
	 * 添加监视check状态变化的watcher.
	 * 
	 * @param view
	 *            view.
	 * @param object
	 *            保存状态值的object.
	 * @param filedName
	 *            状态值的field name.
	 */
	public static void addCheckWatcher(Activity activity, int resId,
			Object object, String filedName) {
		View view = activity.findViewById(resId);

		if (view != null && view instanceof CompoundButton) {
			addCheckWatcher((CompoundButton) view, object, filedName);
		}
	}

	public static void addCheckWatcher(CompoundButton view, Object object,
			String filedName) {
		Boolean checked = ReflectUtils.getField(object, filedName,
				Boolean.class);
		if (checked != null) {
			view.setChecked(checked);
		}

		view.setOnCheckedChangeListener(new ReflectCheckWatcher(object,
				filedName));
	}

	// ---------------------------------------------------- switch

	/**
	 * 添加监视check状态变化的watcher.
	 * 
	 * @param group
	 *            view group.
	 * @param map
	 *            key - 子view的resource id; value - 子view对应的值。
	 * @param object
	 *            保存状态值的object.
	 * @param filedName
	 *            状态值的field name.
	 */
	public static void addSwitchWatcher(Activity activity, int resId,
			final Map<Integer, Integer> map, Object object, String filedName) {
		View view = activity.findViewById(resId);

		if (view != null && view instanceof ViewGroup) {
			addSwitchWatcher((ViewGroup) view, map, object, filedName);
		}
	}

	public static void addSwitchWatcher(final ViewGroup group,
			final Map<Integer, Integer> map, final Object object,
			final String filedName) {
		Object current = ReflectUtils.getField(object, filedName);

		Iterator<Entry<Integer, Integer>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<Integer, Integer> entry = it.next();

			View itemView = group.findViewById(entry.getKey());
			if (itemView == null) {
				continue;
			}

			if (current != null && current.equals(entry.getValue())) {
				setItemChecked(itemView, true);
			}

			itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					setItemChecked(group, map, view.getId());

					Integer value = entry.getValue();
					ReflectUtils.setField(object, filedName, value);
				}
			});
		}
	}

	// ---------------------------------------------------- text
	/**
	 * 添加监视文本值变化的watcher.
	 * 
	 * @param view
	 *            view.
	 * @param object
	 *            保存状态值的object.
	 * @param filedName
	 *            状态值的field name.
	 */
	public static void addTextWatcher(Activity activity, int resId,
			Object object, String filedName) {
		View view = activity.findViewById(resId);

		if (view != null && view instanceof TextView) {
			addTextWatcher((TextView) view, object, filedName);
		}
	}

	public static void addTextWatcher(TextView view, Object object,
			String filedName) {
		String text = ReflectUtils.getField(object, filedName, String.class);
		view.setText(text);

		view.addTextChangedListener(new ReflectTextWatcher(object, filedName));
	}

	// ---------------------------------------------------- date
	/**
	 * 添加监视日期值变化的watcher.
	 * 
	 * @param view
	 *            view.
	 * @param object
	 *            保存状态值的object.
	 * @param filedName
	 *            状态值的field name.
	 * @param pattern
	 *            时间格式: 例如 yyyy-MM-dd。
	 */
	public static void addDateWatcher(Activity activity, int resId,
			Object object, String filedName, String pattern) {
		View view = activity.findViewById(resId);

		if (view != null && view instanceof TextView) {
			addDateWatcher((TextView) view, object, filedName, pattern);
		}
	}

	public static void addDateWatcher(TextView view, Object object,
			String filedName, String pattern) {
		Object value = ReflectUtils.getField(object, filedName);
		if (value != null) {
			if (value instanceof String) {
				view.setText((String) value);
			} else if (value instanceof Long
					|| value.getClass().getName().equalsIgnoreCase("long")) {
				long value2 = ((Long) value).longValue();
				if (value2 != 0) {
					String text = DateHelper.toString(value2, pattern);
					view.setText(text);
				}
			}
		} else {
			String text = DateHelper.getDatetime(pattern);
			view.setText(text);
		}

		view.addTextChangedListener(new ReflectDateWatcher(object, filedName,
				pattern));
	}

	/**
	 * 添加选择时间的picker.
	 * 
	 * @param context
	 *            context.
	 * @param view
	 *            view.
	 * @param titleResId
	 *            标题的resource id.
	 * @param pattern
	 *            时间格式: 例如 yyyy-MM-dd。
	 */
	public static void addDatePicker(final Context context,
			final TextView view, final int titleResId, final String pattern) {
		if (view != null && view instanceof TextView) {
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickDatePicker(context, view, titleResId,
							pattern);
				}
			});
		}
	}

	@SuppressLint({ "NewApi", "DefaultLocale" })
	public static void onClickDatePicker(Context context, final TextView view,
			int titleResId, String pattern) {
		// time
		String text = view.getText().toString();
		Date date = DateHelper.toDate(text, pattern);
		if (date == null) {
			date = new Date();
		}

		// calendar
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		// callback
		OnDateSetListener callback = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker picker, int year, int monthOfYear,
					int dayOfMonth) {
				String time = String.format("%d-%d-%d", year, monthOfYear + 1,
						dayOfMonth);
				view.setText(time);
			}
		};

		// date picker
		DatePickerDialog dlg = new DatePickerDialog(context, callback,
				year, month, day);
		dlg.setTitle(titleResId);

		if (Build.VERSION.SDK_INT >= 11) {
			dlg.getDatePicker().setCalendarViewShown(false);
		}

		dlg.show();
	}

	// ---------------------------------------------------- Class
	/**
	 * use reflect to watch check status changed..
	 * 
	 */
	static class ReflectCheckWatcher implements OnCheckedChangeListener {

		Object mObject;
		String mFieldName;

		public ReflectCheckWatcher(Object object, String filedName) {
			mObject = object;
			mFieldName = filedName;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			ReflectUtils.setField(mObject, mFieldName,
					Boolean.valueOf(isChecked));
		}
	}

	/**
	 * use reflect to watch text changed..
	 * 
	 */
	static class ReflectTextWatcher implements TextWatcher {
		Object mObject;
		String mFieldName;

		public ReflectTextWatcher(Object object, String filedName) {
			mObject = object;
			mFieldName = filedName;
		}

		@Override
		public void afterTextChanged(Editable s) {
			ReflectUtils.setField(mObject, mFieldName, s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	/**
	 * use reflect to watch date changed.
	 * 
	 */
	static class ReflectDateWatcher implements TextWatcher {
		Object mObject;
		String mFieldName;
		String mPattern; // 时间格式：比如yyyy-MM-dd

		public ReflectDateWatcher(Object object, String filedName,
				String pattern) {
			mObject = object;
			mFieldName = filedName;
			mPattern = pattern;
		}

		@Override
		public void afterTextChanged(Editable s) {
			ReflectUtils.setDateField(mObject, mFieldName, mPattern,
					s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	// ---------------------------------------------------- Private methods
	private static void setItemChecked(ViewGroup group,
			Map<Integer, Integer> map, int id) {

		Iterator<Entry<Integer, Integer>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Integer> entry = it.next();

			View view = group.findViewById(entry.getKey());
			if (view == null) {
				continue;
			}

			setItemChecked(view, view.getId() == id);
		}
	}

	private static void setItemChecked(View view, boolean checked) {
		if (view instanceof CompoundButton) {
			((CompoundButton) view).setChecked(checked);
		}

		if (view instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup) view;
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				setItemChecked(viewGroup.getChildAt(i), checked);
			}
		}
	}

}
