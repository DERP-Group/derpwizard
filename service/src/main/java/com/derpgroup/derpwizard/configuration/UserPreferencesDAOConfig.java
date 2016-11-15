package com.derpgroup.derpwizard.configuration;

import javax.validation.constraints.NotNull;

import com.derpgroup.derpwizard.dao.impl.UserPreferencesDAOFactory.UserPreferencesDAOTypes;

public class UserPreferencesDAOConfig {

  @NotNull
  private String accessKey;
  @NotNull
  private String secretKey;
  @NotNull
  private String tableName;
  private String namespace;
  @NotNull
  private UserPreferencesDAOTypes type;
  
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

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public UserPreferencesDAOTypes getType() {
    return type;
  }

  public void setType(UserPreferencesDAOTypes type) {
    this.type = type;
  }
}
