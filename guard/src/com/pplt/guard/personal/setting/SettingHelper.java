package com.pplt.guard.personal.setting;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pplt.guard.R;


/**
 * 设置：helper.
 */
public class SettingHelper {

	// ---------------------------------------------------- Constructor
	private SettingHelper() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 设置：信息刷新频率。
	 */
	public static void setRefreshFrequence(final Context context,
			final OnClickListener listener) {
		final int frequence = 6;

		LayoutInflater inflater = LayoutInflater.from(context);
		View contentView = inflater.inflate(
				R.layout.set_refresh_frequence_view, null);
		final TextView labelTv = (TextView) contentView
				.findViewById(R.id.tv_label);
		final SeekBar seekBar = (SeekBar) contentView
				.findViewById(R.id.seek_bar);
		seekBar.setProgress(frequence);
		seekBar.setMax(60);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				anchorSeekBar(context, seekBar, labelTv, progress);
			}
		});

		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.personal_item_refresh_frequence);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (listener != null) {
					listener.onClick(null);
				}
			}
		});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		builder.setView(contentView);
		seekBar.postDelayed(new Runnable() {

			@Override
			public void run() {
				anchorSeekBar(context, seekBar, labelTv, frequence);
			}
		}, 100);

		builder.create().show();
	}

	private static void anchorSeekBar(Context context, SeekBar seekBar,
			TextView labelTv, int progress) {
		// set text
		String value = String.valueOf(progress)
				+ context.getText(R.string.second);
		labelTv.setText(value);

		// set position
		int barWidth = seekBar.getWidth();
		int labelWidth = labelTv.getWidth();
		if (labelWidth == 0) {
			labelWidth = (int) getTextViewLength(labelTv, value);
		}
		int thumbOffset = seekBar.getThumbOffset();

		int offset = (barWidth - 2 * thumbOffset) * progress / seekBar.getMax()
				+ thumbOffset - labelWidth / 2;
		ViewGroup.LayoutParams lp = labelTv.getLayoutParams();
		if (lp != null && lp instanceof RelativeLayout.LayoutParams) {
			((RelativeLayout.LayoutParams) lp).setMargins(offset, 0, 0, 0);
			labelTv.setLayoutParams(lp);
		}
	}

	private static float getTextViewLength(TextView textView, String text) {
		TextPaint paint = textView.getPaint();
		return paint.measureText(text);
	}
}
