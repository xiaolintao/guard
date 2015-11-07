package com.hipalsports.entity;

import java.util.Date;

public class AppVersion {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column app_version.id
	 * @mbggenerated
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column app_version.sysType
	 * @mbggenerated
	 */
	private Integer sysType;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column app_version.version
	 * @mbggenerated
	 */
	private String version;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column app_version.version_code
	 * @mbggenerated
	 */
	private Integer version_code;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column app_version.downloadUrl
	 * @mbggenerated
	 */
	private String downloadUrl;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column app_version.flag
	 * @mbggenerated
	 */
	private Integer flag;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column app_version.createTime
	 * @mbggenerated
	 */
	private Date createTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column app_version.lastUpdateTime
	 * @mbggenerated
	 */
	private Date lastUpdateTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column app_version.description
	 * @mbggenerated
	 */
	private String description;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column app_version.id
	 * @return  the value of app_version.id
	 * @mbggenerated
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column app_version.id
	 * @param id  the value for app_version.id
	 * @mbggenerated
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column app_version.sysType
	 * @return  the value of app_version.sysType
	 * @mbggenerated
	 */
	public Integer getSysType() {
		return sysType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column app_version.sysType
	 * @param sysType  the value for app_version.sysType
	 * @mbggenerated
	 */
	public void setSysType(Integer sysType) {
		this.sysType = sysType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column app_version.version
	 * @return  the value of app_version.version
	 * @mbggenerated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column app_version.version
	 * @param version  the value for app_version.version
	 * @mbggenerated
	 */
	public void setVersion(String version) {
		this.version = version == null ? null : version.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column app_version.version_code
	 * @return  the value of app_version.version_code
	 * @mbggenerated
	 */
	public Integer getVersion_code() {
		return version_code;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column app_version.version_code
	 * @param version_code  the value for app_version.version_code
	 * @mbggenerated
	 */
	public void setVersion_code(Integer version_code) {
		this.version_code = version_code;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column app_version.downloadUrl
	 * @return  the value of app_version.downloadUrl
	 * @mbggenerated
	 */
	public String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column app_version.downloadUrl
	 * @param downloadUrl  the value for app_version.downloadUrl
	 * @mbggenerated
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl == null ? null : downloadUrl.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column app_version.flag
	 * @return  the value of app_version.flag
	 * @mbggenerated
	 */
	public Integer getFlag() {
		return flag;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column app_version.flag
	 * @param flag  the value for app_version.flag
	 * @mbggenerated
	 */
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column app_version.createTime
	 * @return  the value of app_version.createTime
	 * @mbggenerated
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column app_version.createTime
	 * @param createTime  the value for app_version.createTime
	 * @mbggenerated
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column app_version.lastUpdateTime
	 * @return  the value of app_version.lastUpdateTime
	 * @mbggenerated
	 */
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column app_version.lastUpdateTime
	 * @param lastUpdateTime  the value for app_version.lastUpdateTime
	 * @mbggenerated
	 */
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column app_version.description
	 * @return  the value of app_version.description
	 * @mbggenerated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column app_version.description
	 * @param description  the value for app_version.description
	 * @mbggenerated
	 */
	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}
}