
package com.jty.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.os.StatFs;

/**
 * 读写文件的工具类。
 */
public class FileHelper {

	// ------------------------------------------------------------ Constructor
	protected FileHelper() {
	}

	// ------------------------------------------------------------ Public methods
	/**
	 * 目录是否存在。
	 * @param dirName 目录名。
	 * @return 目录是否存在。
	 */
	public static boolean checkDirectory(String dirName) {
		File path = new File(dirName);
		if (!path.exists()) {
			if (!path.mkdirs()) {
				return false;
			}
		}

		return path.exists() && path.isDirectory();
	}

	/**
	 * 文件是否存在。
	 * @param fileName 文件名。
	 * @return 文件是否存在。
	 */
	public static boolean isFileExists(String fileName) {
		File file = new File(fileName);

		return file.exists() && file.isFile();
	}

	/**
	 * 获取剩余空间大小。
	 * @param dirName 目录名。
	 * @return 剩余空间大小。
	 */
	@SuppressWarnings("deprecation")
	public static long getLeftSpace(String dirName) {
		StatFs sf = new StatFs(dirName);

		File path = new File(dirName);
		if (!path.exists() || !path.isDirectory()) {
			return 0;
		}

		return (long)sf.getAvailableBlocks() * (long)sf.getBlockSize();
	}

	/**
	 * 获取文件大小。
	 * @param fileName 文件名。
	 * @return 文件大小。
	 */
	public static long getFileSize(String fileName) {
		File file = new File(fileName);
		if (!file.exists() || !file.isFile()) {
			return 0;
		}

		return file.length();
	}

	/**
	 * 获取文件名。
	 * @param filePath 路径名。
	 * @return 文件名。
	 */
	public static String getFilename(String filePath) {
		File path = new File(filePath);
		return path.getName();
	}

	/**
	 * 设置文件的最后修改时间。
	 * @param filename 文件名。
	 * @param time 时间。
	 * @return true - 成功 false - 出错。
	 */
	public static boolean setLastModified(String filename, long time) {
		File file = new File(filename);
		if (file.exists()) {
			return file.setLastModified(time);
		}

		return false;
	}

	/**
	 * 复制文件。
	 * @param srcFile 源文件名。
	 * @param targetFile 目标文件名。
	 * @throws Exception 异常。
	 * @return true - 成功 false - 出错。
	 */
	public static boolean copyFile(String srcFile, String targetFile) {
		try {
			File tmpFile = new File(targetFile);
			if (!tmpFile.exists()) {
				if (!tmpFile.createNewFile()) {
					return false;
				}
			}

			FileInputStream fis = new FileInputStream(srcFile);
			BufferedInputStream is = new BufferedInputStream(fis);

			FileOutputStream fos = new FileOutputStream(targetFile);
			BufferedOutputStream os = new BufferedOutputStream(fos);

			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}

			os.flush();
			os.close();
			is.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 删除。
	 * @param filePath 路径名。
	 * @return true - 成功 false - 出错。
	 */
	public static boolean delete(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return true;
		}

		return file.isFile() ? file.delete() : deleteDirectory(filePath);
	}

	/**
	 * 删除目录。
	 * @param filePath 目录名。
	 * @return true - 成功 false - 出错。
	 */
	public static boolean deleteDirectory(String filePath) {
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return true;
		}

		// 删除子目录
		File files[]=dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				deleteDirectory(file.getAbsolutePath());
			} else {
				file.delete();
			}
		}

		// 删除目录本身
		dir.delete();

		return true;
	}

	/**
	 * 删除文件。
	 * @param fileName 文件名。
	 * @return true - 成功 false - 出错。
	 */
	public static boolean deleteFile(String fileName) {
		try {
			File file = new File(fileName);
			if (!file.exists() || !file.isFile()) {
				return true;
			}

			return file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 重命名。
	 * @param oldName 原名。
	 * @param newName 新名。
	 * @return true - 成功 false - 出错。
	 */
	public static boolean renameFile(String oldName, String newName) {
		File file = new File(oldName);
		if (!file.exists() || !file.isFile()) {
			return false;
		}

		return file.renameTo(new File(newName));
	}

	/**
	 * 移动目录下的文件/目录。
	 * @param srcDirname 源目录。
	 * @param tarDirname 目标目录。
	 * @return true - 成功 false - 出错。
	 */
	public static boolean moveDirectory(String srcDirname, String tarDirname) {
		// 检查src目录
		File srcDir = new File(srcDirname);
		if (!srcDir.exists() || !srcDir.isDirectory()) {
			return false;
		}

		// 检查tar目录
		File tarDir = new File(tarDirname);
		if (!tarDir.exists() || !tarDir.isDirectory()) {
			tarDir.mkdirs();
		}
		if (!tarDir.exists() || !tarDir.isDirectory()) {
			return false;
		}

		// 检查tar是否是src的子目录
		if (isSubDirectory(srcDirname, tarDirname)) {
			return false;
		}

		// move *.*
		File srcSubFiles[] = new File(srcDirname).listFiles();
		for (File srcSubFile : srcSubFiles) {
			if (srcSubFile.isDirectory()) {
				String srcSubDirname =  srcSubFile.getAbsolutePath();
				String tarSubDirname = tarDirname + File.separator + srcSubDirname.substring(srcDirname.length());
				moveDirectory(srcSubDirname, tarSubDirname);
			}else {
				String tarSubFilename = tarDirname + File.separator + srcSubFile.getName();
				srcSubFile.renameTo(new File(tarSubFilename));
			}
		}

		// delete src directory
		srcDir.delete();

		return true;
	}

	/**
	 * 判断tarDir是否是srcDir的子目录。
	 * @param srcDirname 目录名。
	 * @param tarDirname 目录。
	 * @return 是否是子目录。
	 */
	public static boolean isSubDirectory(String srcDirname, String tarDirname) {
		File tarDir = new File(tarDirname);
		while (tarDir.getParent() != null) {
			if (tarDir.getParent().equalsIgnoreCase(srcDirname)) {
				return true;
			}

			tarDir = new File(tarDir.getParent());
		}

		return false;
	}

	/**
	 * 读取文件。
	 * @param filename 文件名。
	 * @return 内容。
	 */
	public static byte[] readFile(String filename) {
		try {
			File file = new File(filename);
			FileInputStream is = new FileInputStream(file);

			int fileSize = (int) file.length();
			byte[] data = new byte[fileSize];
			is.read(data);
			is.close();

			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 读取文件内容。
	 * @param filename 文件名。
	 * @param charset 编码方式。
	 * @return 文件内容。
	 */
	public static String readFile(String filename, String charset) {
		StringBuffer buf = new StringBuffer();

		try {
			FileInputStream in = new FileInputStream(new File(filename));

			BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
			do {
				String data = reader.readLine();
				if (data == null) {
					break;
				}

				buf.append(data);
			} while (true);

			reader.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buf.toString();
	}

	/**
	 * 读取文件内容。
	 * 
	 * @param filename
	 *            文件名。
	 * @param charset
	 *            编码方式。
	 * @return 文件内容。
	 */
	public static String readAssertFile(Context context, String filename,
			String charset) {
		StringBuffer buf = new StringBuffer();

		try {
			InputStream in = context.getAssets().open(filename);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, charset));
			do {
				String data = reader.readLine();
				if (data == null) {
					break;
				}

				buf.append("\r\n");
				buf.append(data);
			} while (true);

			reader.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buf.toString();
	}

	/**
	 * 写文件。
	 * @param filename 文件名。
	 * @param data 数据。
	 * @param charset 编码方式。
	 * @return true - 成功 false - 出错。
	 */
	public static boolean writeFile(String filename, String data, String charset) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			OutputStreamWriter writer = new OutputStreamWriter(fos, charset);

			writer.write(data);

			writer.flush();
			writer.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 写文件。
	 * @param filename 文件名。
	 * @param data 数据。
	 * @param charsetName 编码方式。
	 * @return true - 成功 false - 出错。
	 */
	public static boolean writeFile(String filename, byte[] data){
		OutputStream out = null;

		try {
			out = new FileOutputStream(filename);
			out.write(data);

			out.flush();
			out.close();

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}
}
