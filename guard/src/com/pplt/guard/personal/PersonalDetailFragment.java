package com.pplt.guard.personal;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jty.util.FileHelper;
import com.jty.util.ToastHelper;
import com.jty.util.cache.BitmapCache;
import com.jty.util.cache.BitmapKey;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.kingdom.sdk.net.http.HttpUtils;
import com.kingdom.sdk.net.http.IHttpResponeListener;
import com.kingdom.sdk.net.http.ResponseEntity;
import com.pplt.guard.Global;
import com.pplt.guard.comm.HttpUrls;
import com.pplt.guard.comm.ResponseCodeHelper;
import com.pplt.guard.comm.ResponseParser;
import com.pplt.guard.comm.entity.UserEntity;
import com.pplt.guard.personal.pwd.ChangePwdActivity;
import com.pplt.guard.R;
import com.pplt.ui.PreferenceItem;

/**
 * 个人：详细信息。
 */
public class PersonalDetailFragment extends Fragment implements OnClickListener {

	// ---------------------------------------------------- Constants
	private final static int REQUEST_GET_PHOTO = 1; // 拍照&选择图片
	private final static int REQUEST_CLIP_PICTURE = 2; // 裁剪图片

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.iv_photo)
	private ImageView mPhotoIv; // 头像

	@InjectView(id = R.id.tv_name)
	private TextView mNameTv; // 名字

	@InjectView(id = R.id.tv_logout)
	private View mLogout; // 退出登录

	private String mPhotoFilename; // 图片文件名

	// ---------------------------------------------------- Override methods
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater
				.inflate(R.layout.personal_detail_view, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// IOC
		InjectUtil.injectFragment(this, view);

		// initial views
		initViews();
	}

	@Override
	public void onResume() {
		super.onResume();

		// initial data
		initData();

		// 显示用户信息
		showUinfo();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 拍照&选择图片
		if (requestCode == REQUEST_GET_PHOTO) {
			if (FileHelper.isFileExists(mPhotoFilename)) {
				setPhoto(mPhotoFilename);
				FileHelper.deleteFile(mPhotoFilename);
			}
		}
	}

	// ---------------------------------------------------- initial
	/**
	 * initial views.
	 */
	private void initViews() {
		// click listener
		setOnClickListener();
	}

	/**
	 * set click listener.
	 */
	private void setOnClickListener() {
		int[] ids = new int[] { R.id.user_panel, R.id.item_change_pwd,
				R.id.tv_logout };

		for (int id : ids) {
			View view = getView().findViewById(id);
			if (view != null) {
				view.setOnClickListener(this);
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.user_panel: // 更改头像
			changePhoto();
			break;
		case R.id.item_change_pwd: // 修改密码
			changePwd();
			break;
		case R.id.tv_logout: // 退出登录
			logout();
			break;
		default:
			break;
		}
	}
	/**
	 * initial data.
	 */
	private void initData() {
		UserEntity user = Global.getUser();
		if (user == null) {
			return;
		}

		// 手机号码
		String phone = user.getPhoneNum();
		if (!TextUtils.isEmpty(phone)) {
			setItemText(R.id.item_phone, markPhone(phone));
		}
	}

	/**
	 * 显示用户信息。
	 */
	private void showUinfo() {
		UserEntity user = Global.getUser();

		if (user != null) {
			// 名字
			mNameTv.setText(user.getName());

			// 头像
			String photo = user.getPhoto();
			Bitmap bmp = BitmapCache.getBitmap(photo);
			if (bmp != null) {
				mPhotoIv.setImageBitmap(bmp);
			}
		} else {
			mNameTv.setText(R.string.personal_not_login);
		}
	}

	/**
	 * 修改密码。
	 */
	private void changePwd() {
		Intent intent = new Intent(getActivity(), ChangePwdActivity.class);

		startActivity(intent);
	}

	/**
	 * 退出登录。
	 */
	private void logout() {
		IHttpResponeListener listener = new IHttpResponeListener() {

			@Override
			public void onHttpRespone(ResponseEntity response) {
				dealLogout(response);
			}
		};

		HttpUtils.HttpGet(HttpUrls.URL_LOGOUT, listener);
	}

	/**
	 * 处理响应。
	 * 
	 * @param response
	 *            响应。
	 */
	private void dealLogout(ResponseEntity response) {
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			Global.setUser(null);
			exit();
			return;
		}

		// fail
		String msg = ResponseCodeHelper.getHint(getActivity(), code);
		ToastHelper.toast(getActivity(), msg);
	}

	private void exit() {
		Activity activity = getActivity();
		if (activity != null) {
			activity.finish();
		}
	}

	// ---------------------------------------------------- change photo
	/**
	 * 更换头像。
	 */
	private void changePhoto() {
		LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
		View menuView = mLayoutInflater.inflate(R.layout.popup_photo_view,
				null, true);
		final PopupWindow popupWindow = new PopupWindow(menuView,
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();

		popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);

		// listener
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				popupWindow.dismiss();

				switch (view.getId()) {
				case R.id.popup_take_photo:
					takePhoto();
					break;
				case R.id.popup_from_album:
					fromAlbum();
					break;
				default:
					break;
				}
			}
		};
		int[] ids = new int[] { R.id.popup_take_photo, R.id.popup_from_album,
				R.id.popup_cancel };
		for (int id : ids) {
			View itemView = menuView.findViewById(id);
			if (itemView != null) {
				itemView.setOnClickListener(listener);
			}
		}
	}

	/**
	 * 拍照。
	 */
	private void takePhoto() {
		File file = getTmpFile();
		mPhotoFilename = file.getPath();

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra("crop", "true");// 裁剪
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, REQUEST_GET_PHOTO);
	}

	/**
	 * 从手机图库中选择。
	 */
	private void fromAlbum() {
		File file = getTmpFile();
		mPhotoFilename = file.getPath();

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("crop", "true");// 裁剪
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, REQUEST_GET_PHOTO);
	}

	/**
	 * 裁剪图片。
	 * 
	 * @param filename
	 *            图片文件名。
	 */
	void clipPicture(String filename) {
		File file = new File(filename);
		Uri uri = Uri.fromFile(file);

		File newFile = getTmpFile();
		Uri newUri = Uri.fromFile(newFile);

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// 裁剪
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, newUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, REQUEST_CLIP_PICTURE);
	}

	/**
	 * 设置头像。
	 * 
	 * @param filename
	 *            文件名。
	 */
	private void setPhoto(String filename) {
		String key = BitmapKey.getFileKey(BitmapKey.SCHEME_PERSONAL);

		// 添加到disk缓存
		BitmapCache.addFile(key, filename);

		// 显示
		Bitmap bmp = BitmapCache.getBitmap(key);
		if (bmp != null) {
			mPhotoIv.setImageBitmap(bmp);
		}

		// 设置Global变量
		UserEntity user = Global.getUser();
		if (user != null) {
			user.setPhoto(key);
			Global.setUser(user);
		}
	}

	/**
	 * 获取临时文件。
	 * 
	 * @return 临时文件。
	 */
	private File getTmpFile() {
		String filename = Global.getTmpPath() + "/"
				+ System.currentTimeMillis() + ".jpg";

		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 部分显示手机号码。
	 * 
	 * @param phone
	 *            手机号码。
	 * @return 部分显示的手机号码。
	 */
	private String markPhone(String phone) {
		if (phone == null || phone.length() <= 6) {
			return phone;
		}

		return phone.substring(0, 2) + "****" + phone.substring(6);
	}

	private void setItemText(int resId, String text) {
		View view = getView().findViewById(resId);
		if (view != null && view instanceof PreferenceItem) {
			((PreferenceItem) view).setItemText(text);
		}
	}
}
