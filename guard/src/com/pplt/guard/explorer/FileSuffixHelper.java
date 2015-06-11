package com.pplt.guard.explorer;

import java.util.Locale;

import com.pplt.guard.R;

/**
 * helper of file.
 *
 */
public class FileSuffixHelper {

	// ---------------------------------------------------- Constants
	public final static int FILE_UNKNOWN		= -1;
	public final static int FILE_VIDEO 			= 0;
	public final static int FILE_AUDIO			= 1;
	public final static int FILE_PICTRUE			= 2;
	public final static int FILE_WORD				= 4;
	public final static int FILE_EXCEL				= 5;
	public final static int FILE_PPT					= 6;
	public final static int FILE_PDF					= 7;
	public final static int FILE_TXT					= 8;
	public final static int FILE_HTML				= 9;
	public final static int FILE_CHM				= 10;

	public final static int FILE_GIF					= 20;
	public final static int FILE_TIF					= 21;

	public final static int FILE_APK					= 100;

	static class Suffix {
		String suffix;
		int icon;
		int large_icon;
		int type;
		String mimeType;

		public Suffix(String suffix, int icon, int largeIcon, int type, String mimeType) {
			this.suffix = suffix;
			this.icon = icon;
			large_icon = largeIcon;
			this.type = type;
			this.mimeType = mimeType;
		}
	}

	private final static Suffix[]  SUFFIX = new Suffix[] {
		new Suffix(".rm",				R.drawable.rm,		R.drawable.rm_table,			FILE_VIDEO,		"video/*"),
		new Suffix(".rmvb",			R.drawable.rmvb,	R.drawable.rmvb_table,		FILE_VIDEO,		"video/*"),
		new Suffix(".avi", 				R.drawable.avi,		R.drawable.avi_table,			FILE_VIDEO,		"video/*"),
		new Suffix(".mp4", 			R.drawable.mp4,		R.drawable.mp4_table,		FILE_VIDEO,		"video/*"),
		new Suffix(".mpg", 			R.drawable.mpg,		R.drawable.mpg_table,		FILE_VIDEO,		"video/*"),
		new Suffix(".mpeg", 			R.drawable.mpeg,	R.drawable.mpeg_table,		FILE_VIDEO,		"video/*"),
		new Suffix(".wmv", 			R.drawable.video,	R.drawable.video_table,		FILE_VIDEO,		"video/*"),
		new Suffix(".vob", 				R.drawable.video,	R.drawable.video_table,		FILE_VIDEO,		"video/*"),
		new Suffix(".flv", 				R.drawable.flv,		R.drawable.flv_table,			FILE_VIDEO,		"video/*"),

		new Suffix(".png", 				R.drawable.png,		R.drawable.png_table,		FILE_PICTRUE,		"image/*"),
		new Suffix(".jpg", 				R.drawable.jpg,		R.drawable.jpg_table,			FILE_PICTRUE,		"image/*"),
		new Suffix(".jpeg", 			R.drawable.jpeg,		R.drawable.jpeg_table,		FILE_PICTRUE,		"image/*"),
		new Suffix(".bmp", 			R.drawable.bmp,		R.drawable.bmp_table,		FILE_PICTRUE,		"image/*"),

		new Suffix(".gif", 				R.drawable.gif,		R.drawable.gif_table,			FILE_GIF,		"image/*"),

		new Suffix(".tif", 				R.drawable.tif,			R.drawable.tif_table,			FILE_TIF,		"image/*"),
		new Suffix(".tiff", 				R.drawable.tiff,		R.drawable.tiff_table,			FILE_TIF,		"image/*"),

		new Suffix(".doc|.docx", 	R.drawable.doc,		R.drawable.doc_table,		FILE_WORD,		"application/msword"),
		new Suffix(".xls|.xlsx", 		R.drawable.xls,		R.drawable.xls_table,			FILE_EXCEL,			"application/vnd.ms-excel"),
		new Suffix(".ppt|.pptx|.ppsx", 		R.drawable.ppt,		R.drawable.ppt_table,			FILE_PPT,				"application/vnd.ms-powerpoint"),

		new Suffix(".pdf", 				R.drawable.pdf,		R.drawable.pdf_table,			FILE_PDF,				"application/pdf"),
		new Suffix(".fdf", 				R.drawable.fdf,		R.drawable.fdf_table,			FILE_PDF,				"application/pdf"),

		new Suffix(".txt", 				R.drawable.txt,		R.drawable.txt_table,			FILE_TXT,				"text/plain"),

		new Suffix(".htm", 				R.drawable.htm,		R.drawable.htm_table,		FILE_HTML,			"text/html"),
		new Suffix(".html", 			R.drawable.html,		R.drawable.html_table,		FILE_HTML,			"text/html"),

		new Suffix(".chm", 				R.drawable.chm,		R.drawable.chm_table,		FILE_CHM,			"application/x-chm"),


		new Suffix(".apk", 				R.drawable.apk,		R.drawable.apk_table,		FILE_APK,				"application/vnd.android.package-archive")
	};

	// GIF, JEPG, BMP和TIFF


	public final static int ICON_DIRECTORY				= R.drawable.wj;
	public final static int ICON_DIRECTORY_LARGE 	= R.drawable.wj_table;


	// ---------------------------------------------------- Constructor
	private FileSuffixHelper() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * get icon.
	 * 
	 * @param filename
	 *            file name.
	 * @return resource id.
	 */
	public static int getIcon(String filename) {
		int index = getFileSuffixIndex(filename);

		return index != -1 ? SUFFIX[index].icon : R.drawable.wz;
	}

	/**
	 * get large icon.
	 * 
	 * @param filename
	 *            file name.
	 * @return resource id.
	 */
	public static int getLargeIcon(String filename) {
		int index = getFileSuffixIndex(filename);

		return index != -1 ? SUFFIX[index].large_icon : R.drawable.wz_table;
	}

	/**
	 * get type.
	 * 
	 * @param filename
	 *            file name.
	 * @return resource id.
	 */
	public static int getType(String filename) {
		int index = getFileSuffixIndex(filename);

		return index != -1 ? SUFFIX[index].type : FILE_UNKNOWN;
	}

	/**
	 * get mime type.
	 * 
	 * @param filename
	 *            file name.
	 * @return resource id.
	 */
	public static String getMimeType(String filename) {
		int index = getFileSuffixIndex(filename);

		return index != -1 ? SUFFIX[index].mimeType : "";
	}

	// ---------------------------------------------------- Private methods
	/**
	 * get index of file suffix array.
	 * 
	 * @param filename
	 *            file name.
	 * @return index.
	 */
	private static int getFileSuffixIndex(String filename) {
		String lowerCase = filename.toLowerCase(Locale.getDefault());

		for (int i = 0; i < SUFFIX.length; i++) {
			String[] suffix = SUFFIX[i].suffix.split("\\|");

			for (String element : suffix) {
				if (lowerCase.endsWith(element)) {
					return i;
				}
			}
		}

		return -1;
	}
}
