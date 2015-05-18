package com.kingdom.sdk.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

/**
 * SQLite open helper.
 * 
 */
public final class DBHelper extends OrmLiteSqliteOpenHelper {

	/** single instance */
	private static DBHelper instance;

	/** whether the database can be close or not */
	private boolean canClose = false;

	/** class list of table that should be created */
	private static List<Class<?>> classList = new ArrayList<Class<?>>();

	/** database name */
	private static String DATABASE_NAME = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/jty.db";
	/** database version */
	private static final int VERSION = 1;

	/**
	 * constructor.
	 * 
	 * @param context
	 */
	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	/**
	 * set database name, this method should be called before "createInstance" .
	 * 
	 * @param databaseName
	 *            database name.
	 */
	public static void setDatabaseName(String databaseName) {
		DATABASE_NAME = databaseName;
	}

	/**
	 * create unique instance.
	 * 
	 * @param context
	 *            context.
	 */
	public static void createInstance(Context context) {
		synchronized (DBHelper.class) {
			if (instance == null) {
				instance = new DBHelper(context);
			}
		}
	}

	/**
	 * get unique instance.
	 * 
	 * @return instance.
	 */
	public static DBHelper singleInstance() {
		return instance;
	}

	/**
	 * get BaseDao.
	 * 
	 * @param modelClass
	 *            entity class.
	 * @param <T>
	 *            Template class.
	 * @param <D>
	 *            Template class.
	 * @return BaseDao.
	 */
	public <D extends Dao<T, ?>, T> Dao<T, ?> getBaseDao(Class<T> modelClass) {
		Dao<T, ?> dao = null;
		try {
			dao = getDao(modelClass);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return dao;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
		createTable(cs);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource cs,
			int oldVersion, int newVersion) {
	}

	@Override
	public void close() {
		if (canClose) {
			super.close();
		}
	}

	/**
	 * add class of table that should be created to class list.
	 * 
	 * @param cls
	 *            class of table that should be created
	 */
	public static void addClass(Class<?> cls) {
		classList.add(cls);
	}

	public static void addClass(Class<?>[] list) {
		List<Class<?>> data = Arrays.asList(list);
		classList.addAll(data);
	}

	/**
	 * create tables that in class list.
	 * 
	 * @param ConnectionSource
	 *            connection source.
	 */
	private void createTable(ConnectionSource cs) {
		try {
			for (Class<?> c : classList) {
				TableUtils.createTable(cs, c);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * create table.
	 * 
	 * @param cs
	 *            connection source.
	 * @param tableName
	 *            name of table.
	 * @param cls
	 *            class of table
	 * @throws SQLException
	 */
	protected <T> void createTable(ConnectionSource cs, String tableName,
			Class<T> cls) throws SQLException {
		DatabaseTableConfig<T> dtf = new DatabaseTableConfig<T>();
		dtf.setTableName(tableName);
		dtf.setDataClass(cls);
		dtf.extractFieldTypes(cs);

		TableUtils.createTable(cs, dtf);
	}

}
