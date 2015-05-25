package com.pplt.guard.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 联系人。
 */
@DatabaseTable(tableName = "contact")
public class Contact {

	// ---------------------------------------------------- Private data
	@DatabaseField(generatedId = true)
	long id; // id

	@DatabaseField()
	String name; // 姓名

	@DatabaseField()
	String phone; // 手机号码

	@DatabaseField()
	String email; // email

	@DatabaseField()
	String user; // 密防系统的账号

	@DatabaseField()
	String qq; // QQ

	@DatabaseField()
	String sina; // 新浪微博账号

	@DatabaseField()
	long timestamp; // 时间戳

	// ---------------------------------------------------- Setter & Getter
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getSina() {
		return sina;
	}

	public void setSina(String sina) {
		this.sina = sina;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
