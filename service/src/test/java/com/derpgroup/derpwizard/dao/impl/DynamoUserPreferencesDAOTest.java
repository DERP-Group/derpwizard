package com.derpgroup.derpwizard.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.derpgroup.derpwizard.model.preferences.DerpwizardTestPreferences;
import com.derpgroup.derpwizard.model.preferences.UserPreferences;
import com.fasterxml.jackson.core.type.TypeReference;

public class DynamoUserPreferencesDAOTest {

  private DynamoUserPreferencesDAO userPreferencesDAO;
  
  @Before
  public void setup(){
    userPreferencesDAO = new DynamoUserPreferencesDAO();
  }
  
  @Test
  public void testInit(){
    TableDescription description = userPreferencesDAO.describeTable();
    System.out.println(description.toString());
  }
  
  @Test
  public void testGetPreferences(){

    UserPreferences userPreferences = userPreferencesDAO.getPreferences("fake");
    assertNotNull(userPreferences);
    assertEquals("fake",userPreferences.getUserId());
    assertNotNull(userPreferences.getPreferences());
    
    Map<String,Map<String,Object>> preferences = userPreferences.getPreferences();
    assertTrue(preferences.containsKey("DERPWIZARD"));
    assertNotNull(preferences.get("DERPWIZARD"));
    
    Map<String,Object> derpwizardPreferences = preferences.get("DERPWIZARD");
    assertTrue(derpwizardPreferences.containsKey("fakePreference"));
    assertEquals(derpwizardPreferences.get("fakePreference"),"fakeValue");
  }
  
  @Test
  public void testGetPreferences_unknownUser(){
    
    String userId = UUID.randomUUID().toString();
    UserPreferences userPreferences = userPreferencesDAO.getPreferences(userId);
    assertNotNull(userPreferences);
    assertEquals(userId,userPreferences.getUserId());
    assertNotNull(userPreferences.getPreferences());
  }
     
  @Test
  public void testGetPreferencesBySkillName(){
    userPreferencesDAO.getPreferencesBySkillName("fake", "DERPWIZARD", new TypeReference<DerpwizardTestPreferences>(){});
  }
}
