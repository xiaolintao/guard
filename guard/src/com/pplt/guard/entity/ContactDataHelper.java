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
	public static List<Contact> getContacts() {
		return getContacts("");
	}

	/**
	 * 查询。
	 * 
	 * @param ids
	 *            联系人id。
	 * 
	 * @return 记录集。
	 */
	public static List<Contact> getContacts(List<Long> ids) {
		try {
			Dao<Contact, Long> dao = DBHelper.singleInstance().getDao(
					Contact.class);

			QueryBuilder<Contact, Long> qb = dao.queryBuilder();
			return qb.where().in("id", ids).query();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<Contact>();
	}

	/**
	 * 查询。
	 * 
	 * @param id
	 *            记录id.
	 * @return 记录。
	 */
	public static Contact getContact(long id) {
		try {
			Dao<Contact, Long> dao = DBHelper.singleInstance().getDao(
					Contact.class);

			return dao.queryForId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Contact();
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

	/**
	 * 更新。
	 * 
	 * @param contact
	 *            联系人。
	 * @return update的记录数.
	 */
	public static int update(Contact contact) {
		try {
			Dao<Contact, Long> dao = DBHelper.singleInstance().getDao(
					Contact.class);

			return dao.update(contact);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 更新。
	 * 
	 * @param id
	 *            记录id.
	 * @return update的记录数.
	 */
	public static int delete(long id) {
		try {
			Dao<Contact, Long> dao = DBHelper.singleInstance().getDao(
					Contact.class);

			return dao.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 查询：ids不包含的联系人id。
	 * 
	 * @return 联系人id。
	 */
	public static List<Long> filter(List<Long> ids) {
		List<Long> left = new ArrayList<Long>();

		List<Contact> contacts = getContacts();

		if (contacts != null && contacts.size() != 0) {
			for (Contact contact : contacts) {
				if (ids == null || ids.indexOf(contact.getId()) == -1) {
					left.add(contact.getId());
				}
			}
		}

		return left;
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
