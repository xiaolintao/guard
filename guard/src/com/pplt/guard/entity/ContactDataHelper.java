package com.pplt.guard.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.kingdom.sdk.db.DBHelper;

/**
 * 联系人。
 */
public class ContactDataHelper {

	/**
	 * 查询。
	 * 
	 * @param key
	 *            关键字。
	 * 
	 * @return 记录集。
	 */
	public static List<Contact> getContacts(String key) {
		try {
			Dao<Contact, Long> dao = DBHelper.singleInstance().getDao(
					Contact.class);

			QueryBuilder<Contact, Long> qb = dao.queryBuilder();
			setKey(qb, key);
			return qb.query();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<Contact>();
	}

	/**
	 * 添加。
	 * 
	 * @param contact
	 *            联系人。
	 * @return insert的记录数.
	 */
	public static int insert(Contact contact) {
		try {
			Dao<Contact, Long> dao = DBHelper.singleInstance().getDao(
					Contact.class);

			return dao.create(contact);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	private static void setKey(QueryBuilder<Contact, Long> qb, String key)
			throws SQLException {
		if (TextUtils.isEmpty(key)) {
			return;
		}

		String clause = key;
		qb.where().like("name", clause).or().like("phone", clause).or()
		.like("user", clause).or().like("qq", clause).or()
		.like("sina", clause);
	}

}
