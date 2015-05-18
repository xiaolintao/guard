package com.pplt.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pplt.guard.R;


/**
 * Title bar.
 * 
 */
public class TitleBar extends RelativeLayout {

	// ---------------------------------------------------- Private data
	private TextView leftTv, rightTv;
	private ImageView leftIv, rightIv;
	private TextView titleTv;
	private ImageView refreshIv, searchIv;

	private int refreshVisibility = View.GONE;

	// ---------------------------------------------------- Constructor

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public TitleBar(Context context) {
		super(context);
		init(context, null);
	}

	// ---------------------------------------------------- Public methods
	public void setTitleText(CharSequence text) {
		titleTv.setText(text);
	}

	public void setTitleText(int resid) {
		titleTv.setText(resid);
	}

	public void setTitleView(View view) {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		addView(view, lp);
	}

	public void setLeftBtnClickListener(View.OnClickListener listener) {
		leftTv.setOnClickListener(listener);
		leftIv.setOnClickListener(listener);
	}

	public void setLeftBtnVisibility(int visibility) {
		if (!TextUtils.isEmpty(leftTv.getText())) {
			leftTv.setVisibility(visibility);
		}

		if (leftIv.getDrawable() != null) {
			leftIv.setVisibility(visibility);
		}
	}

	public void setRightBtnClickListener(View.OnClickListener listener) {
		rightTv.setOnClickListener(listener);
		rightIv.setOnClickListener(listener);
	}

	public void setRightText(int resid) {
		rightTv.setText(resid);
	}

	public void setRefreshBtnClickListener(View.OnClickListener listener) {
		refreshIv.setVisibility(View.VISIBLE);
		refreshIv.setOnClickListener(listener);
	}

	public void setSearchBtnClickListener(View.OnClickListener listener) {
		searchIv.setVisibility(View.VISIBLE);
		searchIv.setOnClickListener(listener);
	}

	public void startLoading() {
		refreshVisibility = refreshIv.getVisibility();
		refreshIv.setVisibility(View.VISIBLE);

		refreshIv.startAnimation(getRotationAnim());
		refreshIv.setClickable(false);
	}

	public void stopLoading() {
		refreshIv.setVisibility(refreshVisibility);

		refreshIv.setClickable(true);
		refreshIv.clearAnimation();
	}

	// ---------------------------------------------------- Private methods
	private void init(final Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.title_bar, this, true);

		titleTv = (TextView) findViewById(R.id.tv_title);

		leftTv = (TextView) findViewById(R.id.tv_left);
		leftIv = (ImageView) findViewById(R.id.iv_left);
		rightTv = (TextView) findViewById(R.id.tv_right);
		rightIv = (ImageView) findViewById(R.id.iv_right);

		refreshIv = (ImageView) findViewById(R.id.iv_refresh);
		searchIv = (ImageView) findViewById(R.id.iv_search);

		setAttributes(attrs);

		setDefaultListener();
	}

	private void setAttributes(AttributeSet attrs) {
		if (attrs == null) {
			return;
		}

		TypedArray tArray = null;
		try {
			// attributes
			tArray = getContext().obtainStyledAttributes(attrs,
					R.styleable.TitleBar);

			// title
			String titleText = tArray.getString(R.styleable.TitleBar_titleText);
			titleTv.setText(titleText);

			// left button
			setLeftBtn(tArray);
			setLeftImageBtn(tArray);

			// right button
			setRightBtn(tArray);
			setRightImageBtn(tArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tArray != null) {
				tArray.recycle();
				tArray = null;
			}
		}
	}

	private void setLeftBtn(TypedArray tArray) {
		// text
		String text = tArray.getString(R.styleable.TitleBar_leftText);
		if (TextUtils.isEmpty(text)) {
			return;
		}
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setText(text);

		// background
		int bg = tArray.getResourceId(R.styleable.TitleBar_leftBg, -1);
		if (bg != -1) {
			leftTv.setBackgroundResource(bg);
		}
	}

	private void setLeftImageBtn(TypedArray tArray) {
		// image
		int image = tArray.getResourceId(R.styleable.TitleBar_leftImage, -1);
		if (image == -1) {
			return;
		}
		leftIv.setVisibility(View.VISIBLE);
		leftIv.setImageResource(image);

		// background
		int bg = tArray.getResourceId(R.styleable.TitleBar_leftBg, -1);
		if (bg != -1) {
			leftIv.setBackgroundResource(bg);
		}
	}

	private void setRightBtn(TypedArray tArray) {
		// text
		String text = tArray.getString(R.styleable.TitleBar_rightText);
		if (TextUtils.isEmpty(text)) {
			return;
		}
		rightTv.setVisibility(View.VISIBLE);
		rightTv.setText(text);

		// background
		int bg = tArray.getResourceId(R.styleable.TitleBar_rightBg, -1);
		if (bg != -1) {
			rightTv.setBackgroundResource(bg);
		}
	}

	private void setRightImageBtn(TypedArray tArray) {
		// image
		int image = tArray.getResourceId(R.styleable.TitleBar_rightImage, -1);
		if (image == -1) {
			return;
		}
		rightIv.setVisibility(View.VISIBLE);
		rightIv.setImageResource(image);

		// background
		int bg = tArray.getResourceId(R.styleable.TitleBar_rightBg, -1);
		if (bg != -1) {
			rightIv.setBackgroundResource(bg);
		}
	}

	private void setDefaultListener() {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Context context = getContext();

				if (context instanceof Activity) {
					((Activity) context).finish();
					// ((Activity) context).overridePendingTransition(
					// R.anim.push_left_in, R.anim.push_right_out);
				}
			}
		};

		if (leftTv != null) {
			leftTv.setOnClickListener(listener);
		}
		if (leftIv != null) {
			leftIv.setOnClickListener(listener);
		}
	}

	private static Animation getRotationAnim() {
		RotateAnimation anim = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(1000);
		return anim;
	}

}
