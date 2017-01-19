package com._3po_labs.derpwizard.persistence.configuration;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com._3po_labs.derpwizard.persistence.dao.factory.AccountLinkingDAOFactory.AccountLinkingDAOTypes;

public class AccountLinkingDAOConfig {

	@NotNull
	private String accessKey;
	@NotNull
	private String secretKey;
	@NotNull
	private String tableName;
	@NotNull
	private AccountLinkingDAOTypes type;

	private Map<String, Object> properties;

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public AccountLinkingDAOTypes getType() {
		return type;
	}

	public void setType(AccountLinkingDAOTypes type) {
		this.type = type;
	}

	@Deprecated
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Deprecated
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
