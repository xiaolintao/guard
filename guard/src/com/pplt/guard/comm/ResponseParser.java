package com.pplt.guard.comm;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import android.util.Log;

import com.jty.util.JSonUtils;
import com.kingdom.sdk.net.http.HttpUtils;
import com.kingdom.sdk.net.http.ResponseEntity;

/**
 * 解析响应包。
 */
public class ResponseParser {

	// ---------------------------------------------------- Constants
	final static String TAG = "ResponseParser";

	// ---------------------------------------------------- Constructor
	private ResponseParser() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 解析：code。
	 * 
	 * @param response
	 *            响应包。
	 * @return code。
	 */
	public static int parseCode(ResponseEntity response) {
		if (response.getStatus() != HttpUtils.RESULT_OK) {
			return -1;
		}
		long start = System.currentTimeMillis();

		TypeReference<ResultEntity<Map<String, Object>>> typeReference = new TypeReference<ResultEntity<Map<String, Object>>>() {
		};
		ResultEntity<Map<String, Object>> entity = JSonUtils.readValue(
				response.getContentAsString(), typeReference);

		Log.d(TAG, "parseCode use:" + (System.currentTimeMillis() - start));

		return entity != null ? entity.getCode() : -1;
	}

	/**
	 * 解析：message。
	 * 
	 * @param response
	 *            响应包。
	 * @return message： "" - 没有message。
	 */
	public String parseMsg(ResponseEntity response) {
		if (response.getStatus() != HttpUtils.RESULT_OK) {
			return "";
		}

		TypeReference<ResultEntity<Map<String, Object>>> typeReference = new TypeReference<ResultEntity<Map<String, Object>>>() {
		};
		ResultEntity<Map<String, Object>> entity = JSonUtils.readValue(
				response.getContentAsString(), typeReference);

		return entity != null ? entity.getMsg() : "";
	}

	/**
	 * 解析：data。
	 * 
	 * @param response
	 *            响应包。
	 * @param cls
	 *            data的类型。
	 * @return data。
	 */
	public static <T> T parseData(ResponseEntity response, Class<T> cls) {
		if (response.getStatus() != HttpUtils.RESULT_OK) {
			return null;
		}

		TypeFactory typeFactory = TypeFactory.defaultInstance();
		JavaType javaType = typeFactory.constructParametricType(
				ResultEntity.class, cls);
		ResultEntity<T> entity = JSonUtils.readValue(
				response.getContentAsString(), javaType);

		return entity != null ? entity.getData() : null;
	}

	/**
	 * 解析：integer field。
	 * 
	 * @param response
	 *            响应包。
	 * @param filedName
	 *            field名字。
	 * 
	 * @param defalultValue
	 *            缺省值。
	 * @return 值。
	 */
	public static int parseInt(ResponseEntity response, String filedName,
			int defalultValue) {
		if (response.getStatus() != HttpUtils.RESULT_OK) {
			return defalultValue;
		}

		TypeReference<ResultEntity<Map<String, Object>>> typeReference = new TypeReference<ResultEntity<Map<String, Object>>>() {
		};
		ResultEntity<Map<String, Object>> entity = JSonUtils.readValue(
				response.getContentAsString(), typeReference);
		if (entity != null) {
			Map<String, Object> data = entity.getData();
			if (data.containsKey(filedName)) {
				Object value = data.get(filedName);
				if (value instanceof String) {
					try {
						return Integer.parseInt((String) value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return defalultValue;
	}

	/**
	 * 解析：data。
	 * 
	 * @param response
	 *            响应包。
	 * @param cls
	 *            data的类型。
	 * @return data。
	 */
	public static <T> List<T> parseList(ResponseEntity response, Class<T> cls) {
		if (response.getStatus() != HttpUtils.RESULT_OK) {
			return null;
		}
		long start = System.currentTimeMillis();

		TypeFactory typeFactory = TypeFactory.defaultInstance();
		JavaType javaType = typeFactory.constructParametricType(
				ResultListEntity.class, cls);
		ResultListEntity<T> entity = JSonUtils.readValue(
				response.getContentAsString(), javaType);

		Log.d(TAG, "parseList use:" + (System.currentTimeMillis() - start));

		return entity != null && entity.getData() != null ? entity.getData()
				.getList() : null;
	}
}
