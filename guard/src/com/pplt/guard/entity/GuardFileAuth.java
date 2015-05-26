package com.pplt.guard.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 密防文件：授权。
 */
@DatabaseTable(tableName = "gurad_file_auth")
public class GuardFileAuth {

	// ---------------------------------------------------- Private data
	@DatabaseField(generatedId = true)
	long id; // id

	@DatabaseField()
	long guardFileId; // 密防文件id

	@DatabaseField()
	long contactId; // 联系人id

	@DatabaseField()
	long timestamp; // 时间戳

	// ---------------------------------------------------- Setter & Getter
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGuardFileId() {
		return guardFileId;
	}

	public void setGuardFileId(long guardFileId) {
		this.guardFileId = guardFileId;
	}

	public long getContactId() {
		return contactId;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
