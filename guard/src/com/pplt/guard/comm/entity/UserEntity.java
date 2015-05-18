package com.pplt.guard.comm.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 用户。
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEntity {

	@JsonProperty("userid")
	private int id;

	@JsonProperty("phone_num")
	private String phoneNum; // 手机号码

	private String name; // 昵称

	@JsonProperty("v_state")
	private int vstate; // vip

	private int state; // 状态

	private int fund;

	private String email;

	private String ctime;

	private String photo; // 图像

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVstate() {
		return vstate;
	}

	public void setVstate(int vstate) {
		this.vstate = vstate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getFund() {
		return fund;
	}

	public void setFund(int fund) {
		this.fund = fund;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
