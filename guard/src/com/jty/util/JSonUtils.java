package com.jty.util;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

/**
 * Convert JSON <=> OBJECT.
 * 
 */
public class JSonUtils {
	private static ObjectMapper objectMapper;

	private JSonUtils() {
	}

	/**
	 * 将OBJECT对象转换成JSON字符串。
	 * 
	 * @param object
	 *            object对象。
	 * @return OBJECT字符串：null - 转换出错。
	 */
	public static String toJSon(Object object) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将JSON字符串转换成OBJECT对象。
	 * 
	 * @param content
	 *            JSON字符串。
	 * @param valueType
	 *            OBJECT对象的类型。
	 * @return OBJECT对象：null - 转换出错。
	 */
	public static <T> T readValue(String content, Class<T> valueType) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		objectMapper.configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return objectMapper.readValue(content, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将JSON字符串转换成OBJECT对象：用于List<T>、Map＜Ｋ，Ｔ＞等泛型OBJECT对象。
	 * 
	 * @param content
	 *            JSON字符串。
	 * @param typeReference
	 *            OBJECT对象的类型。
	 * @return OBJECT对象：null - 转换出错。
	 */
	public static <T> T readValue(String jsonStr, TypeReference<T> typeReference) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		objectMapper.configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.getDeserializationConfig().without(
				Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			return objectMapper.readValue(jsonStr, typeReference);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将JSON字符串转换成OBJECT对象：用于自定义的泛型OBJECT对象。
	 * 
	 * @param content
	 *            JSON字符串。
	 * @param typeReference
	 *            OBJECT对象的类型。
	 * @return OBJECT对象：null - 转换出错。
	 */
	public static <T> T readValue(String jsonStr, JavaType javaType) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		objectMapper.configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.getDeserializationConfig().without(
				Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			return objectMapper.readValue(jsonStr, javaType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
