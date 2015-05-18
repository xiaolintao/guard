package com.jty.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import android.text.TextUtils;

import com.jty.util.DateHelper;

/**
 * reflect utilities.
 */
public class ReflectUtils {
	// ---------------------------------------------------- Constructor
	private ReflectUtils() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 设置属性值.
	 * 
	 * @param target
	 *            目标对象.
	 * @param name
	 *            属性名称.
	 * @param value
	 *            属性值.
	 */
	public static void setField(Object target, String name, Object value) {
		// 获取类对象
		Class<?> classObj = target.getClass();

		try {
			// 获取Field
			Field field = classObj.getDeclaredField(name);
			if (field == null) {
				return;
			}

			// accessible
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			// set value
			String setMethodName = findMethodName("set", name);
			Method setMethod = classObj.getMethod(setMethodName,
					new Class[] { field.getType() });
			Object[] values = new Object[] { convert(value, field.getType()) };
			setMethod.invoke(target, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置属性值：日期.
	 * 
	 * @param target
	 *            目标对象.
	 * @param name
	 *            属性名称.
	 * @param pattern
	 *            时间格式: 例如 yyyy-MM-dd。
	 * @param value
	 *            属性值.
	 */
	public static void setDateField(Object target, String name, String pattern,
			String value) {
		// 获取类对象
		Class<?>classObj = target.getClass();

		try {
			// 获取Field
			Field field = classObj.getDeclaredField(name);
			if (field == null) {
				return;
			}

			// accessible
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			// set value
			String setMethodName = findMethodName("set", name);
			Method setMethod = classObj.getMethod(setMethodName, new Class[]{field.getType()});
			Object[] values = new Object[] { convertDate(value, pattern,
					field.getType()) };
			setMethod.invoke(target, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * * 获取属性值.
	 * 
	 * @param source
	 *            源对象.
	 * @param name
	 *            属性名称.
	 */
	public static Object getField(Object source, String name) {
		// 获取类对象
		Class<?> classObj = source.getClass();

		try {
			// 获取Field
			Field field = classObj.getDeclaredField(name);
			if (field == null) {
				return null;
			}

			// accessible
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			// get method
			String getMethodName = findMethodName("get", name);
			Method getMethod = findMethod(classObj, getMethodName,
					new Class[] {});
			if (getMethod == null) {
				getMethodName = findMethodName("is", name);
				getMethod = findMethod(classObj, getMethodName, new Class[] {});
			}
			if (getMethod == null) {
				return null;
			}

			// get value
			return getMethod.invoke(source, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * * 获取属性值.
	 * 
	 * @param source
	 *            源对象.
	 * @param name
	 *            属性名称.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getField(Object source, String name, Class<T> cls) {
		Object value = getField(source, name);

		if (value != null) {
			if (cls.isInstance(value)) {
				return (T) value;
			} else {
				return reconvert(value, cls);
			}
		}

		return null;
	}

	/**
	 * 复制属性。
	 * 
	 * @param values
	 *            属性。
	 * @param target
	 *            目标对象。
	 */
	public static void copyFields(HashMap<String, Object> values, Object target) {
		// 获取类对象
		Class<?> targetClassObj = target.getClass();

		do
		{
			// 获取对象的属性集
			Field[] fields = targetClassObj.getDeclaredFields();

			// 检验对象的属性值
			for (Field field:fields)
			{
				// 获取属性
				String key = field.getName();

				// 获取属性值
				Object value = values.get(key);
				if (value == null) {
					continue;
				}

				if (!field.isAccessible()) {
					field.setAccessible(true);
				}

				// 复制对象的属性值
				if (field.isAccessible()) {
					try {
						field.set(target, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try	{
						String setMethodName = findMethodName("set", key);

						Method setMethod = targetClassObj.getMethod(setMethodName, new Class[]{field.getType()});
						setMethod.invoke(target, new Object[]{value});
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}

			// 获取父类的class
			targetClassObj = targetClassObj.getSuperclass();
			if (targetClassObj.getName().equalsIgnoreCase("java.lang.Object")) {
				break;
			}
		}while(true);
	}

	/**
	 * 获取对象的属性。
	 * 
	 * @param obj
	 *            对象。
	 * @return - HashMap<String - 属性名称，Object - 属性值>。
	 */
	public static HashMap<String, Object> getFields(Object obj) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		// 获取类对象
		Class<?> classObj = obj.getClass();

		do
		{
			// 获取对象的属性集
			Field[] fields = classObj.getDeclaredFields();

			// 检验对象的属性值
			for (Field field:fields)
			{
				// 获取属性
				String key = field.getName();

				if (!field.isAccessible()) {
					field.setAccessible(true);
				}

				// 获取对象的属性值
				if (field.isAccessible()) {
					try {
						Object value = field.get(obj);
						map.put(key, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					try {
						String getMethodName = findMethodName("get", key);
						Method getMethod = findMethod(classObj, getMethodName, new Class[]{});
						if (getMethod == null) {
							getMethodName = findMethodName("is", key);
							getMethod = findMethod(classObj, getMethodName,
									new Class[] {});
						}
						if (getMethod == null) {
							continue;
						}

						Object value = getMethod.invoke(obj, new Object[]{});
						map.put(key, value);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}

			// 获取父类的class
			classObj = classObj.getSuperclass();
			if (classObj.getName().equalsIgnoreCase("java.lang.Object")) {
				break;
			}
		}while(true);

		return map;
	}

	/**
	 * 判断对象是否相等。
	 */
	public static boolean equals(Object obj1, Object obj2) {
		HashMap<String, Object> fields1 = getFields(obj1);
		HashMap<String, Object> fields2 = getFields(obj2);

		Iterator<String> it = fields1.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();

			Object value1 = fields1.get(key);
			Object value2 = fields2.get(key);
			if (value1 != null && !value1.equals(value2)) {
				return false;
			}
			if (value2 != null && !value2.equals(value1)) {
				return false;
			}
		}

		return true;
	}

	// ---------------------------------------------------- Public methods
	/**
	 * execute method.
	 * 
	 * @param obj
	 *            class instance.
	 * @param name
	 *            method name.
	 * @param args
	 *            method parameters。
	 */
	public static void executeMethod(Object obj, String name, Object... args) {
		Class<?> classObj = obj.getClass();

		Class<?>[] parameterTypes = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			parameterTypes[i] = args[i].getClass();
		}

		try {
			Method method = classObj.getMethod(name, parameterTypes);

			method.invoke(obj, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * execute method.
	 * 
	 * @param obj
	 *            class instance.
	 * @param name
	 *            method name.
	 */
	public static void executeMethod(Object obj, String name) {
		Class<?> classObj = obj.getClass();
		try {
			Method method = classObj.getMethod(name);

			method.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 拼方法名。
	 * 
	 * @param prefix
	 *            前缀。
	 * @param fieldName
	 *            字段名。
	 * @return 方法名。
	 */
	private static String findMethodName(String prefix, String fieldName) {
		return prefix
				+ fieldName.substring(0, 1).toUpperCase(Locale.getDefault())
				+ fieldName.substring(1);
	}

	/**
	 * 获取方法。
	 * 
	 * @param classObj
	 *            类对象。
	 * @param name
	 *            方法名。
	 * @param parameterTypes
	 *            参数。
	 * @return 方法。
	 */
	private static Method findMethod(Class<?> classObj, String name,
			Class<?>... parameterTypes) {
		try {
			return classObj.getMethod(name, parameterTypes);
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * 根据所需类型转换数值：String=>some type。
	 * 
	 * @param obj
	 *            数值。
	 * @param type
	 *            类型。
	 * @return 转换结果。
	 */
	private static Object convert(Object obj, Class<?> type) {
		if (obj instanceof String) {
			String value = obj != null ? (String) obj : null;

			if (type.equals(Long.class)
					|| type.getName().equalsIgnoreCase("long")) {
				return !TextUtils.isEmpty(value) ? Long.valueOf(value) : 0;
			} else if (type.equals(Integer.class)
					|| type.getName().equalsIgnoreCase("int")) {
				return !TextUtils.isEmpty(value) ? Integer.valueOf(value) : 0;
			} else if (type.equals(Float.class)
					|| type.getName().equalsIgnoreCase("float")) {
				return !TextUtils.isEmpty(value) ? Float.valueOf(value) : 0.0f;
			} else if (type.equals(Double.class)
					|| type.getName().equalsIgnoreCase("double")) {
				return !TextUtils.isEmpty(value) ? Double.valueOf(value) : 0.0f;
			}
		}

		return obj;
	}

	/**
	 * 转换：String=>日期。
	 * 
	 * @param obj
	 *            数值。
	 * @param pattern
	 *            时间格式: 例如 yyyy-MM-dd。
	 * @param type
	 *            类型。
	 * @return 转换结果。
	 */
	private static Object convertDate(String obj, String pattern, Class<?> type) {
		if (obj == null) {
			return null;
		}

		if (type.equals(String.class)) {
			return obj;
		}

		if (type.equals(Long.class) || type.getName().equalsIgnoreCase("long")) {
			Date date = DateHelper.toDate(obj, pattern);
			return date != null ? date.getTime() : null;
		}

		return obj;
	}

	/**
	 * 根据所需类型转换数值：some value=>String。
	 * 
	 * @param obj
	 *            数值。
	 * @param type
	 *            类型。
	 * @return 转换结果。
	 */
	@SuppressWarnings("unchecked")
	private static <T> T reconvert(Object obj, Class<T> type) {
		if (type.equals(String.class)) {
			if (obj == null) {
				return null;
			}

			if (obj instanceof Long) {
				Long value = (Long) obj;
				if (value != 0) {
					return (T) value.toString();
				}
			} else if (obj instanceof Integer) {
				Integer value = (Integer) obj;
				if (value != 0) {
					return (T) value.toString();
				}
			} else if (obj instanceof Float) {
				Float value = (Float) obj;
				if (value != 0.0f) {
					return (T) value.toString();
				}
			} else if (obj instanceof Double) {
				Double value = (Double) obj;
				if (value != 0.0f) {
					return (T) value.toString();
				}
			}

			return (T) "";
		}

		return (T) obj;
	}
}
