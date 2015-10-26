package com.pplt.guard.comm.response;

import java.util.List;

/**
 * 响应包。
 */
public class ResponseEntity {

	public int errorCode;

	public String errorMessage;

	public List<String> data;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}
}
