package com.pplt.guard.entity;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.kingdom.sdk.db.DBHelper;

/**
 * 密防文件：授权。
 */
public class GuardFileAuthDataHelper {

	/**
	 * 查询：已授权。
	 * 
	 * @param guardFileId
	 *            密防文件id.
	 * 
	 * @return 已授权的联系id.
	 */
	public static List<Long> getAuthTo(long guardFileId) {
		List<Long> ids = new ArrayList<Long>();

		try {
			Dao<GuardFileAuth, Long> dao = DBHelper.singleInstance().getDao(
					GuardFileAuth.class);

			QueryBuilder<GuardFileAuth, Long> qb = dao.queryBuilder();
			qb.where().eq("guardFileId", guardFileId);

			List<GuardFileAuth> auths = qb.query();
			if (auths != null && auths.size() != 0) {
				for (GuardFileAuth auth : auths) {
					ids.add(auth.getContactId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ids;
	}

	/**
	 * 添加。
	 * 
	 * @param auth
	 *            授权。
	 * @return insert的记录数.
	 */
	public static int insert(GuardFileAuth auth) {
		try {
			Dao<GuardFileAuth, Long> dao = DBHelper.singleInstance().getDao(
					GuardFileAuth.class);

			return dao.create(auth);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 删除。
	 * 
	 * @param guardFileId
	 *            密防文件id.
	 * @param contactId
	 *            联系人id.
	 * @return delete的记录数。
	 */
	public static int delete(long guardFileId, long contactId) {
		try {
			Dao<GuardFileAuth, Long> dao = DBHelper.singleInstance().getDao(
					GuardFileAuth.class);

			DeleteBuilder<GuardFileAuth, Long> db = dao.deleteBuilder();
			db.where().eq("guardFileId", guardFileId).and()
			.eq("contactId", contactId);

			return db.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
}
