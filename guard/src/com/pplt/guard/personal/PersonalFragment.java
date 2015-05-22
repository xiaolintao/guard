package com.pplt.guard.personal;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jty.util.FileHelper;
import com.jty.util.FragmentHelper;
import com.jty.util.cache.BitmapCache;
import com.jty.util.cache.BitmapKey;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.Global;
import com.pplt.guard.R;
import com.pplt.guard.comm.entity.UserEntity;

/**
 * 个人。
 */
public class PersonalFragment extends Fragment {

	// ---------------------------------------------------- Constants
	private final static int REQUEST_GET_PHOTO = 1; // 拍照&选择图片
	private final static int REQUEST_CLIP_PICTURE = 2; // 裁剪图片

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.user_panel)
	private View mUserPanel;

	@InjectView(id = R.id.iv_photo)
	private ImageView mPhotoIv; // 头像

	@InjectView(id = R.id.tv_name)
	private TextView mNameTv; // 名字

	@InjectView(id = R.id.tv_login)
	private TextView mLoginTv; // 登录

	private String mPhotoFilename; // 图片文件名

	// ---------------------------------------------------- Override methods
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.personal_view, container,
				false);
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

	// ---------------------------------------------------- Private methods
	/**
	 * initial views.
	 */
	private void initViews() {
		// panel
		mUserPanel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changePhoto();
			}
		});

		// content
		FragmentHelper.add(getChildFragmentManager(), getActivity(),
				PersonalCenterFragment.class.getName(), R.id.content_panel);
	}

	// ---------------------------------------------------- Private methods
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

			// 登录按钮
			mLoginTv.setVisibility(View.GONE);
		} else {
			mNameTv.setText(R.string.personal_anonymous);

			mLoginTv.setVisibility(View.VISIBLE);
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
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
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
}
