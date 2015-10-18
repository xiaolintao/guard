package com.pplt.guard.comm.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 响应包解析器。
 */
public class ResponseParser {

	// ---------------------------------------------------- Public methods
	/**
	 * 解析 : success字段。
	 * 
	 * @param response
	 *            response数据。
	 * @return success字段。
	 */
	public static boolean parseResult(String response) {
		ResponseEntity entity = parseEntity(response);

		return entity != null ? entity.success : false;
	}

	/**
	 * 解析 : errorCode字段。
	 * 
	 * @param response
	 *            response数据。
	 * @return errorCode字段。
	 */
	public static int parseCode(String response) {
		ResponseEntity entity = parseEntity(response);

		return entity != null ? entity.errorCode : -1;
	}

	/**
	 * 解析 : data数组的第1个元素。
	 * 
	 * @param response
	 *            response数据。
	 * @return data数组的第1个元素：null - 出错。
	 */
	public static String parse(String response) {
		ResponseEntity entity = parseEntity(response);

		return entity != null && entity.data != null
				&& entity.data.size() > 0 ? entity.data.get(0) : null;
	}

	/**
	 * 解析 : data数组的第1个元素。
	 * 
	 * @param response
	 *            response数据。
	 * @param clazz
	 *            data元素的class类型。
	 * @return data数组的第1个元素：null - 出错。
	 */
	public static <T> T parse(String response, Class<T> clazz) {
		ResponseEntity entity = parseEntity(response);
		if (entity == null || entity.data == null || entity.data.size() == 0) {
			return null;
		}

		try {
			return JSON.parseObject(entity.data.get(0), clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析 : data字段。
	 * 
	 * @param response
	 *            response数据。
	 * @return data数组。
	 */
	public static List<String> parseList(String response) {
		ResponseEntity entity = parseEntity(response);

		return entity != null ? entity.data : null;
	}

	/**
	 * 解析 : data字段。
	 * 
	 * @param response
	 *            response数据。
	 * @param clazz
	 *            data元素的class类型。
	 * @return data数组。
	 */
	public static <T> List<T> parseList(String response, Class<T> clazz) {
		ResponseEntity entity = parseEntity(response);
		if (entity == null || entity.data == null) {
			return null;
		}

		List<T> list = new ArrayList<T>();
		for (String text : entity.data) {
			T item = null;

			try {
				item = JSON.parseObject(text, clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (item != null) {
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * 解析 : data字段。
	 * 
	 * @param response
	 *            response数据。
	 * @return data数组。
	 */
	public static List<Map<String, String>> parseMap(String response) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {
		};

		ResponseEntity entity = parseEntity(response);
		if (entity == null || entity.data == null) {
			return list;
		}

		for (String text : entity.data) {
			Map<String, String> item = null;

			try {
				item = JSON.parseObject(text, typeReference);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (item != null) {
				list.add(item);
			}
		}
		return list;
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 解析。
	 * 
	 * @param response
	 *            response数据。
	 * @return 结果：null - 出错。
	 */
	private static ResponseEntity parseEntity(String response) {
		try {
			return JSON.parseObject(response,
					new TypeReference<ResponseEntity>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
