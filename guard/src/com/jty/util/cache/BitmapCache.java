package com.jty.util.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import libcore.io.DiskLruCache;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 图片缓存。
 */
public class BitmapCache {

	// ---------------------------------------------------- Private data
	private static DiskLruCache mDiskLruCache = null;

	// ---------------------------------------------------- Constructor
	private BitmapCache() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 初始化。
	 */
	public static void init(Context context) {
		// disk缓存
		initDiskLruCache(context);
	}

	/**
	 * 清除。
	 */
	public static void clear() {
		// disk缓存
		clearDiskLruCache();
	}

	/**
	 * 获取使用大小。
	 * 
	 * @return 大小 - 单位：M。
	 */
	public static float getUsedSize() {
		return mDiskLruCache != null ? mDiskLruCache.size()
				/ (1024.0f * 1024.0f) : 0.0f;
	}

	/**
	 * 将文件加入缓存。
	 * 
	 * @param key
	 *            key。
	 * 
	 * @param filename
	 *            文件名。
	 */
	public static void addFile(String key, String filename) {
		addFileToDiskCache(key, filename);
	}

	/**
	 * 获取bitmap。
	 * 
	 * @param key
	 *            key。
	 * @return bitmap。
	 */
	public static Bitmap getBitmap(String key) {
		// 文件缓存
		Bitmap bmp = getBitmapFromDiskCache(key);
		if (bmp != null) {
			return bmp;
		}

		return null;
	}

	// ---------------------------------------------------- disk cache
	/**
	 * 初始化：disk缓存。
	 * 
	 * @param context
	 *            context.
	 */
	private static void initDiskLruCache(Context context) {
		try {
			String dirName = getDiskCacheRoot(context) + "/tmp";
			File cacheDir = new File(dirName);
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}

			mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context),
					1, 10 * 1024 * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清除：disk缓存。
	 */
	private static void clearDiskLruCache() {
		try {
			if (mDiskLruCache != null) {
				mDiskLruCache.delete();
				mDiskLruCache.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取disk缓存根目录。
	 * 
	 * @param context
	 *            context.
	 * @return 目录名。
	 */
	private static String getDiskCacheRoot(Context context) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return context.getExternalCacheDir().getPath();
		} else {
			return context.getCacheDir().getPath();
		}
	}

	/**
	 * 获取application版本。
	 * 
	 * @param context
	 *            context。
	 * @return 版本。
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 1;
	}

	/**
	 * 将文件添加到文件缓存。
	 * 
	 * @param key
	 *            key。
	 * @param filename
	 *            文件名。
	 */
	private static void addFileToDiskCache(String key, String filename) {
		String hashKey = hashKey(key);

		try {
			DiskLruCache.Editor editor = mDiskLruCache.edit(hashKey);
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				if (writeFileToStream(filename, outputStream)) {
					editor.commit();
				} else {
					editor.abort();
				}
			}
			mDiskLruCache.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将文件写入流。
	 * 
	 * @param filename
	 *            文件名。
	 * @param os
	 *            流。
	 * @return true - 成功 false - 出错。
	 */
	private static boolean writeFileToStream(String filename, OutputStream os) {
		File file = new File(filename);
		if (!file.exists() || !file.isFile()) {
			return false;
		}

		FileInputStream is = null;
		byte[] buffer = new byte[1024 * 1024];
		try {
			is = new FileInputStream(file);

			do {
				int len = is.read(buffer);
				if (len <= 0) {
					break;
				}

				os.write(buffer, 0, len);
			} while (true);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(is);
		}

		return false;
	}

	/**
	 * 关闭流。
	 * 
	 * @param is
	 *            流。
	 */
	private static void close(FileInputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从disk缓存中获取bitmap。
	 * 
	 * @param key
	 *            key。
	 * @return bitmap。
	 */
	private static Bitmap getBitmapFromDiskCache(String key) {
		String hashKey = hashKey(key);

		if (mDiskLruCache == null) {
			return null;
		}

		try {
			DiskLruCache.Snapshot snapShot = mDiskLruCache.get(hashKey);

			if (snapShot != null) {
				InputStream is = snapShot.getInputStream(0);
				return BitmapFactory.decodeStream(is);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 计算md5值。
	 * 
	 * @param key
	 *            key。
	 * @return md5值。
	 */
	private static String hashKey(String key) {
		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());

			return bytesToHexString(mDigest.digest());
		} catch (Exception e) {
		}
		return key;
	}

	/**
	 * 转换：byte[]=>string
	 * 
	 * @param bytes
	 *            byte[].
	 * @return string.
	 */
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xFF & b);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
