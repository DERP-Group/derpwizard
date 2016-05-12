package com.derpgroup.derpwizard.model.preferences;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserPreferences {

  private String userId;
  private Map<String, ?> preferences;
  
  public String getUserId() {
    return userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public Map<String, ?> getPreferences() {
    return preferences;
  }
  
  public void setPreferences(Map<String, ?> preferences) {
    this.preferences = preferences;
  }
}
