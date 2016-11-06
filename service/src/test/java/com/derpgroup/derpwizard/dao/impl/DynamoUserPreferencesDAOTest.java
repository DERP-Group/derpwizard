package com.derpgroup.derpwizard.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.derpgroup.derpwizard.model.preferences.DerpwizardTestPreferences;
import com.derpgroup.derpwizard.model.preferences.UserPreferences;
import com.fasterxml.jackson.core.type.TypeReference;

public class DynamoUserPreferencesDAOTest {

  private DynamoUserPreferencesDAO userPreferencesDAO;
  private String userId = null;
  
  @Before
  public void setup(){
    userId = UUID.randomUUID().toString();
    userPreferencesDAO = new DynamoUserPreferencesDAO();
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testGetPreferences(){

    UserPreferences userPreferences = userPreferencesDAO.getPreferences("fake");
    assertNotNull(userPreferences);
    assertEquals("fake",userPreferences.getUserId());
    assertNotNull(userPreferences.getPreferences());
    
    Map<String,Object> preferences = (Map<String, Object>) userPreferences.getPreferences();
    assertTrue(preferences.containsKey("DERPWIZARD"));
    assertNotNull(preferences.get("DERPWIZARD"));
    assertTrue(preferences.get("DERPWIZARD") instanceof Map<?,?>);
    Map<String,Object> derpwizardPreferences = (Map<String, Object>) preferences.get("DERPWIZARD");
    assertTrue(derpwizardPreferences.containsKey("fakePreference"));
    assertEquals(derpwizardPreferences.get("fakePreference"),"fakeValue");
  }
  
  @Test
  public void testGetPreferences_unknownUser(){
    
    UserPreferences userPreferences = userPreferencesDAO.getPreferences(userId);
    assertNotNull(userPreferences);
    assertEquals(userId,userPreferences.getUserId());
    assertNotNull(userPreferences.getPreferences());
    assertEquals(0,userPreferences.getPreferences().size());
  }
     
  @Test
  public void testGetPreferencesBySkillName(){
    userPreferencesDAO.getPreferencesBySkillName("fake", "DERPWIZARD", new TypeReference<DerpwizardTestPreferences>(){});
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testSetPreferences_newUser(){
    UserPreferences userPreferences = new UserPreferences();
    userPreferences.setUserId(userId);
    Map<String,Map<String,Object>> preferences = new HashMap<String,Map<String,Object>>();
    Map<String, Object> derpwizardPreferences = new HashMap<String,Object>();
    String fakePreference = "fakePreferenceValue";
    derpwizardPreferences.put("fakePreference", fakePreference);
    preferences.put("DERPWIZARD",derpwizardPreferences);
    userPreferences.setPreferences(preferences);
    
    userPreferencesDAO.setPreferences(userPreferences);
    
    UserPreferences result = userPreferencesDAO.getPreferences(userId);
    assertNotNull(result);
    assertEquals(userId,result.getUserId());
    assertNotNull(result.getPreferences());
    
    Map<String,Map<String,Object>> preferencesResult = (Map<String, Map<String, Object>>) result.getPreferences();
    assertTrue(preferencesResult.containsKey("DERPWIZARD"));
    assertNotNull(preferencesResult.get("DERPWIZARD"));
    
    Map<String,Object> derpwizardPreferencesResult = preferencesResult.get("DERPWIZARD");
    assertTrue(derpwizardPreferencesResult.containsKey("fakePreference"));
    assertEquals(derpwizardPreferencesResult.get("fakePreference"),"fakePreferenceValue");
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testSetPreferences_existingUser(){
    userPreferencesDAO.createUserRecord(userId);
    
    UserPreferences userPreferences = new UserPreferences();
    userPreferences.setUserId(userId);
    Map<String,Map<String,Object>> preferences = new HashMap<String,Map<String,Object>>();
    Map<String, Object> derpwizardPreferences = new HashMap<String,Object>();
    String fakePreference = "fakePreferenceValue";
    derpwizardPreferences.put("fakePreference", fakePreference);
    preferences.put("DERPWIZARD",derpwizardPreferences);
    userPreferences.setPreferences(preferences);
    
    userPreferencesDAO.setPreferences(userPreferences);
    
    UserPreferences result = userPreferencesDAO.getPreferences(userId);
    assertNotNull(result);
    assertEquals(userId,result.getUserId());
    assertNotNull(result.getPreferences());
    
    Map<String,Map<String,Object>> preferencesResult = (Map<String, Map<String, Object>>) result.getPreferences();
    assertTrue(preferencesResult.containsKey("DERPWIZARD"));
    assertNotNull(preferencesResult.get("DERPWIZARD"));
    
    Map<String,Object> derpwizardPreferencesResult = preferencesResult.get("DERPWIZARD");
    assertTrue(derpwizardPreferencesResult.containsKey("fakePreference"));
    assertEquals(derpwizardPreferencesResult.get("fakePreference"),"fakePreferenceValue");
  }
}
