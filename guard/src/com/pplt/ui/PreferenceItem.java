package com.pplt.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pplt.guard.R;


/**
 * preference item.
 */
public class PreferenceItem extends RelativeLayout {

	// ---------------------------------------------------- Constants
	/** style */
	final static int STYLE_SINGLE = 0;
	final static int STYLE_TOP = 1;
	final static int STYLE_MIDDLE = 2;
	final static int STYLE_BOTTOM = 3;

	private final int[] ID = new int[] {};

	// ---------------------------------------------------- Constructor
	public PreferenceItem(Context context) {
		super(context);
		init(context, null);
	}

	public PreferenceItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	// ---------------------------------------------------- Public methods
	public void setItemText(int resId) {
		TextView textTv = (TextView) findViewById(R.id.tv_text);
		textTv.setText(resId);
	}

	public void setItemText(CharSequence text) {
		TextView textTv = (TextView) findViewById(R.id.tv_text);
		textTv.setText(text);
	}

	public TextView getItemTextView() {
		return (TextView) findViewById(R.id.tv_text);
	}

	public TextView getItemLabelView() {
		return (TextView) findViewById(R.id.tv_label);
	}

	// ---------------------------------------------------- Private methods
	private void init(final Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.preference_item, this,
				true);

		// style
		int style = AttributeSetHelper.getInt(context, attrs,
				R.styleable.PreferenceItem, R.styleable.PreferenceItem_piStyle,
				STYLE_SINGLE);
		int styleResId = getStyleResId(style);
		if (styleResId != -1) {
			View panel = findViewById(R.id.item_panel);
			panel.setBackgroundResource(styleResId);
		}

		// 图标
		int iconResId = AttributeSetHelper.getResourceId(context, attrs,
				R.styleable.PreferenceItem, R.styleable.PreferenceItem_piIcon,
				-1);
		if (iconResId != -1) {
			ImageView iconIv = (ImageView) findViewById(R.id.iv_icon);
			iconIv.setBackgroundResource(iconResId);
		}

		// 标题
		String title = AttributeSetHelper.getString(context, attrs,
				R.styleable.PreferenceItem, R.styleable.PreferenceItem_piTitle);
		TextView titleTv = (TextView) findViewById(R.id.tv_title);
		if (!TextUtils.isEmpty(title)) {
			titleTv.setText(title);
		}

		// 箭头
		boolean showArrow =AttributeSetHelper.getBoolean(context, attrs,
				R.styleable.PreferenceItem,
				R.styleable.PreferenceItem_piShowArrow, true);
		ImageView arrowView = (ImageView) findViewById(R.id.iv_arrow);
		arrowView.setVisibility(showArrow ? View.VISIBLE : View.GONE);

		// 分隔线
		int divideVisiblity = style == STYLE_TOP || style == STYLE_MIDDLE ? View.VISIBLE
				: View.GONE;
		ImageView divideView = (ImageView) findViewById(R.id.iv_divider);
		divideView.setVisibility(divideVisiblity);

		// align
		if (iconResId == -1) {
			alignParentLeft(titleTv);
		}
		if (!showArrow) {
			TextView textTv = (TextView) findViewById(R.id.tv_text);
			alignParentRight(textTv);
		}
	}

	private int getStyleResId(int style) {
		return style >= 0 && style < ID.length ? ID[style] : -1;
	}

	private void alignParentLeft(View view) {
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if (lp == null || !(lp instanceof RelativeLayout.LayoutParams)) {
			return;
		}

		int edge = (int) getResources()
				.getDimension(R.dimen.edge_margin);

		RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) lp;
		lParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		lParams.leftMargin = edge;
	}

	private void alignParentRight(View view) {
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if (lp == null || !(lp instanceof RelativeLayout.LayoutParams)) {
			return;
		}

		int edge = (int) getResources()
				.getDimension(R.dimen.edge_margin);

		RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) lp;
		lParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.TRUE);
		lParams.rightMargin = edge;
	}

}
