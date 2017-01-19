package com._3po_labs.derpwizard.persistence.dao;

import com._3po_labs.derpwizard.persistence.model.preferences.UserPreferences;
import com.fasterxml.jackson.core.type.TypeReference;

public interface UserPreferencesDAO {

	public void setPreferences(UserPreferences userPreferences);

	public UserPreferences getPreferences(String userId);

	public <T> T getPreferencesForDefaultNamespace(String userId, TypeReference<T> type);

	public <T> T getPreferencesByNamespace(String userId, String serviceName, TypeReference<T> type);

	public <T> void setPreferencesForDefaultNamespace(String userId, T skillPreferences);

	public <T> void setPreferencesByNamespace(String userId, String skillName, T skillPreferences);

}
