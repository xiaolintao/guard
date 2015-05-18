package com.pplt.guard.comm;

/**
 * 响应包。
 */
public class ResultListEntity<T> {

	private int code;
	private String msg;
	private DataList<T> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public DataList<T> getData() {
		return data;
	}

	public void setData(DataList<T> data) {
		this.data = data;
	}
}
