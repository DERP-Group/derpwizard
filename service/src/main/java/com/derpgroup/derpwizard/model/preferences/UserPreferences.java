package com.derpgroup.derpwizard.model.preferences;

import java.util.Map;

public class UserPreferences {

  private String userId;
  private Map<String, Map<String,Object>> preferences;
  
  public String getUserId() {
    return userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public Map<String, Map<String, Object>> getPreferences() {
    return preferences;
  }
  
  public void setPreferences(Map<String, Map<String, Object>> preferences) {
    this.preferences = preferences;
  }
}
