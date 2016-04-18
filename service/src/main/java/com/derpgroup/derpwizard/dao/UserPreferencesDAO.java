package com.derpgroup.derpwizard.dao;

import com.derpgroup.derpwizard.model.preferences.UserPreferences;
import com.fasterxml.jackson.core.type.TypeReference;

public interface UserPreferencesDAO {

  public void setPreferences(UserPreferences userPreferences);
  
  public UserPreferences getPreferences(String userId);
  
  public <T> T getPreferencesBySkillName(String userId, String skillName, TypeReference<T> type);
}
