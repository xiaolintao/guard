package com.hipalsports.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfo extends IdEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6922651078621137257L;
	/**
	 * 
	 */
	private String userId;
	private String userName;
	private String nickName;
	private String email;

	@JSONField(serialize = false)
	private String plainPassword;

	@JSONField(serialize = false)
	private String password;

	@JSONField(serialize = false)
	private String salt;
	/**
	 * 性别
	 */
	private String gender;
	private String logoUrl;
	/**
	 * 身高
	 */
	private String height;
	/**
	 * 体重
	 */
	private String weight;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 血型 1 A 2 B 3 AB 4 O
	 */
	private String bloodType;
	private String phone;

	/**
	 * 当前用户所在的坐标
	 */
	private String currentPosition;

	/**
	 * 0 代表公制 1 代表英制
	 */
	private String metrology;

	/**
	 * 个性签名
	 */
	private String signature;

	private Timestamp createTime;
	private Timestamp lastUpdateTime;

	/**
	 * garmin绑定token
	 */
	private String oauthToken;

	/**
	 * garmin 绑定secret
	 */
	private String oauthTokenSecret;



	public String getOauthToken() {
		return oauthToken;
	}

	public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}

	public String getOauthTokenSecret() {
		return oauthTokenSecret;
	}

	public void setOauthTokenSecret(String oauthTokenSecret) {
		this.oauthTokenSecret = oauthTokenSecret;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < nickName.length(); i++) {
			char ch = nickName.charAt(i);

			if(!Character.isHighSurrogate(ch)
					&&
					!Character.isLowSurrogate(ch)
					){
				builder.append(ch);
			}
		}

		this.nickName = builder.toString();
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(String currentPosition) {
		this.currentPosition = currentPosition;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMetrology() {
		return metrology;
	}

	public void setMetrology(String metrology) {
		this.metrology = metrology;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public String toString() {
		String line = System.getProperty("line.separator");

		StringBuilder builder = new StringBuilder();

		builder.append("(");

		builder.append(" id = ");
		builder.append(getId());
		builder.append(line);

		builder.append(" userName = ");
		builder.append(getUserName());
		builder.append(line);

		builder.append(" nickName = ");
		builder.append(getNickName());
		builder.append(line);

		builder.append(" email = ");
		builder.append(getEmail());
		builder.append(line);

		builder.append(" gender = ");
		builder.append(getGender());
		builder.append(line);

		builder.append(" logoUrl = ");
		builder.append(getLogoUrl());
		builder.append(line);

		builder.append(" height = ");
		builder.append(getHeight());
		builder.append(line);

		builder.append(" weight = ");
		builder.append(getWeight());
		builder.append(line);

		builder.append(" birthday = ");
		builder.append(getBirthday());
		builder.append(line);

		builder.append(" bloodType = ");
		builder.append(getBloodType());
		builder.append(line);

		builder.append(" phoneNum = ");
		builder.append(getPhone());
		builder.append(line);

		builder.append(" currentPosition = ");
		builder.append(getCurrentPosition());
		builder.append(line);

		builder.append(" metrology = ");
		builder.append(getMetrology());
		builder.append(line);

		builder.append(" signature = ");
		builder.append(getSignature());
		builder.append(line);

		builder.append(" createTime = ");
		builder.append(getCreateTime());
		builder.append(line);

		builder.append(" lastUpdateTime = ");
		builder.append(getLastUpdateTime());
		builder.append(line);

		builder.append(" oauthToken = ");
		builder.append(getOauthToken());
		builder.append(line);

		builder.append(" oauthTokenSecret = ");
		builder.append(getOauthTokenSecret());
		builder.append(line);

		builder.append(")");

		return builder.toString();

	}

}
