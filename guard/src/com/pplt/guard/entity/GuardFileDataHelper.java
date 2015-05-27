package com.pplt.guard.entity;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.kingdom.sdk.db.DBHelper;

/**
 * 密防文件。
 */
public class GuardFileDataHelper {

	/**
	 * 查询。
	 * 
	 * @return 记录。
	 */
	public static GuardFile getFile(long id) {
		try {
			Dao<GuardFile, Long> dao = DBHelper.singleInstance().getDao(
					GuardFile.class);

			return dao.queryForId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 查询。
	 * 
	 * @return 记录集。
	 */
	public static List<GuardFile> getFiles() {
		try {
			Dao<GuardFile, Long> dao = DBHelper.singleInstance().getDao(
					GuardFile.class);

			QueryBuilder<GuardFile, Long> qb = dao.queryBuilder();
			return qb.query();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<GuardFile>();
	}

	/**
	 * 添加。
	 * 
	 * @param file
	 *            密防文件。
	 * @return insert的记录数.
	 */
	public static int insert(GuardFile file) {
		try {
			Dao<GuardFile, Long> dao = DBHelper.singleInstance().getDao(
					GuardFile.class);

			return dao.create(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 文件是否已密防.
	 * 
	 * @param filePath
	 *            文件路径名
	 * @return 是否已密防.
	 */
	public static boolean isExist(String filePath) {
		try {
			Dao<GuardFile, Long> dao = DBHelper.singleInstance().getDao(
					GuardFile.class);

			QueryBuilder<GuardFile, Long> qb = dao.queryBuilder();
			GuardFile file = qb.where().eq("filePath", filePath)
					.queryForFirst();
			return file != null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
