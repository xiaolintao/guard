package com.pplt.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.pplt.guard.R;


/**
 * Dialog helper.
 *
 */
public class DlgHelper {

	// ---------------------------------------------------- Constructor
	private DlgHelper() {
	}

	// ---------------------------------------------------- progress
	public static Dialog showProgressDialog(Context context, CharSequence msg) {
		ProgressDialog	progressDialog=new ProgressDialog(context);
		progressDialog.setMessage(msg);
		progressDialog.show();
		progressDialog.setCancelable(true);
		return progressDialog;
	}

	public static Dialog showProgressDialog(Context context, int resId) {
		CharSequence msg = context.getText(resId);

		return showProgressDialog(context, msg);
	}

	public static Dialog showProgressDialog(Context context) {
		ProgressDialog	progressDialog=new ProgressDialog(context);
		progressDialog.show();
		progressDialog.setCancelable(true);
		return progressDialog;
	}

	// ---------------------------------------------------- alert
	public static void showAlertDialog(Context context, CharSequence title,
			CharSequence message) {

		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		builder.create().show();
	}

	public static void showAlertDialog(Context context, int titleResId,
			int messageResId) {
		CharSequence title = context.getText(titleResId);
		CharSequence message = context.getText(messageResId);

		showAlertDialog(context, title, message);
	}
}
