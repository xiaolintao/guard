package com.pplt.guard.contact;

import java.util.ArrayList;
import java.util.List;

import com.hipalsports.entity.FriendDetail;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.kingdom.sdk.db.DBHelper;

/**
 * 好友。
 */
public class FriendDataHelper {

	// ---------------------------------------------------- Public methods
	/**
	 * 查询。
	 * 
	 * @param key
	 *            关键字。
	 * 
	 * @return 记录集。
	 */
	public static List<FriendDetail> queryList(String key) {
		try {
			Dao<FriendDetail, Integer> dao = DBHelper.singleInstance().getDao(
					FriendDetail.class);

			QueryBuilder<FriendDetail, Integer> qb = dao.queryBuilder();
			qb.where().like("nickName", key).or().like("phone", key).or()
			.like("email", key).or().like("signature", key);
			return qb.query();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<FriendDetail>();
	}

	/**
	 * 查询。
	 * 
	 * @param userId
	 *            用户id。
	 * @param status
	 *            状态。
	 * @return 记录集。
	 */
	public static List<FriendDetail> queryList(int userId, int status) {
		try {
			Dao<FriendDetail, Integer> dao = DBHelper.singleInstance().getDao(
					FriendDetail.class);

			QueryBuilder<FriendDetail, Integer> qb = dao.queryBuilder();
			return qb.where().eq("userId", userId).and().eq("status", status)
					.query();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<FriendDetail>();
	}

	/**
	 * 查询。
	 * 
	 * @param id
	 *            用户id.
	 * @param friendUserId
	 *            好友的用户id.
	 * @return 记录。
	 */
	public static FriendDetail query(int userId, int friendUserId) {
		try {
			Dao<FriendDetail, Integer> dao = DBHelper.singleInstance().getDao(
					FriendDetail.class);

			QueryBuilder<FriendDetail, Integer> qb = dao.queryBuilder();
			List<FriendDetail> list = qb.where().eq("userId", userId).and()
					.eq("friendUserId", friendUserId).query();
			if (list != null && list.size() != 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 添加。
	 * 
	 * @param record
	 *            好友。
	 * @return add的记录数.
	 */
	public static int add(FriendDetail record) {
		int userId = record.getUserId();
		int friendUserId = record.getFriendUserId();

		FriendDetail oldRecord = query(userId, friendUserId);
		if (oldRecord == null) {
			return create(record);
		} else {
			return update(record);
		}
	}

	/**
	 * 添加。
	 * 
	 * @param list
	 *            好友列表。
	 */
	public static void add(List<FriendDetail> list) {
		if (list == null || list.size() == 0) {
			return;
		}

		for (FriendDetail record : list) {
			add(record);
		}
	}

	/**
	 * 获取好友列表的最后更新时间。
	 * 
	 * @param userId
	 *            用户id.
	 * @return 最后更新时间。null - 没有记录。
	 */
	public static String getLastUpdateTime(int userId) {
		String sql = "select max(lastUpdateTime) from friend_detail where userId = "
				+ userId;
		try {
			Dao<FriendDetail, Integer> dao = DBHelper.singleInstance().getDao(
					FriendDetail.class);

			QueryBuilder<FriendDetail, Integer> qb = dao.queryBuilder()
					.selectRaw(
					sql);
			String[] list = qb.queryRawFirst();
			if (list != null&& list.length > 0) {
				return list[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 添加。
	 * 
	 * @param contact
	 *            联系人。
	 * @return insert的记录数.
	 */
	private static int create(FriendDetail record) {
		try {
			Dao<FriendDetail, Integer> dao = DBHelper.singleInstance().getDao(
					FriendDetail.class);

			return dao.create(record);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 更新。
	 * 
	 * @param contact
	 *            联系人。
	 * @return update的记录数.
	 */
	private static int update(FriendDetail record) {
		int userId = record.getUserId();
		int friendUserId = record.getFriendUserId();

		try {
			Dao<FriendDetail, Integer> dao = DBHelper.singleInstance().getDao(
					FriendDetail.class);

			UpdateBuilder<FriendDetail, Integer> ub = dao.updateBuilder();
			ub.where().eq("userId", userId).and()
			.eq("friendUserId", friendUserId);

			return dao.update(record);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
}
