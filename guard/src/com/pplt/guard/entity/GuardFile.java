package com.pplt.guard.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 密防文件。
 */
@DatabaseTable(tableName = "gurad_file")
public class GuardFile {

	// ---------------------------------------------------- Private data
	@DatabaseField(generatedId = true)
	long id; // id

	@DatabaseField()
	String fileName; // 文件名称

	@DatabaseField()
	String filePath; // 文件路径名

	@DatabaseField()
	String summary; // 摘要

	@DatabaseField()
	int openTimes; // 打开次数

	@DatabaseField()
	long authBeginTime; // 授权开始时间

	@DatabaseField()
	long authEndTime; // 授权截止时间

	@DatabaseField()
	long timestamp; // 时间戳

	// ---------------------------------------------------- Setter & Getter
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getOpenTimes() {
		return openTimes;
	}

	public void setOpenTimes(int openTimes) {
		this.openTimes = openTimes;
	}

	public long getAuthBeginTime() {
		return authBeginTime;
	}

	public void setAuthBeginTime(long authBeginTime) {
		this.authBeginTime = authBeginTime;
	}

	public long getAuthEndTime() {
		return authEndTime;
	}

	public void setAuthEndTime(long authEndTime) {
		this.authEndTime = authEndTime;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
