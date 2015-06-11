package com.pplt.guard.entity;

import java.io.File;

import com.pplt.guard.Global;

/**
 * 模拟文件浏览器.
 */
public class GuardFileSeeker {

	// ---------------------------------------------------- Constants
	private final static String ROOT = Global.getRoot() + "/file";

	// ---------------------------------------------------- Public methods
	/**
	 * 搜索pplt/file目录下未密防的文件.
	 * 
	 * @return 文件路径名: null - 没有文件.
	 */
	public static String getFile() {
		File root = new File(ROOT);

		File[] files = root.listFiles();
		if (files == null) {
			return null;
		}

		for (File file : files) {
			String filePath = file.getPath();
			if (!GuardFileDataHelper.isExist(filePath)) {
				return filePath;
			}
		}

		return null;
	}

}
