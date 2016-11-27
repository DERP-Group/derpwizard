package com._3po_labs.derpwizard.persistence.model.preferences;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserPreferences {

  protected String userId;
  protected Map<String, ?> preferences;
  
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
