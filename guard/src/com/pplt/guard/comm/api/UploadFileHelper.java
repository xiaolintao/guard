package com.pplt.guard.comm.api;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.ContentBody;
import internal.org.apache.http.entity.mime.content.FileBody;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 上传文件.
 */
public class UploadFileHelper {

	/**
	 * 上传文件。
	 * 
	 * @param filepath
	 *            文件路径名。
	 * @return 服务端响应包。
	 */
	public static String upload(String filepath) {
		File file = new File(filepath);
		if (!file.exists() || !file.isFile()) {
			return null;
		}

		// entity
		MultipartEntity multipartEntity = new MultipartEntity();
		ContentBody filebody = new FileBody(file);
		multipartEntity.addPart("file", filebody);

		// http host
		String url = BaseAPI.BASE_URL + "upload/uploadFile";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(multipartEntity);

		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shutdown(httpClient);
		}

		return null;
	}

	private static void shutdown(HttpClient httpClient) {
		try {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
