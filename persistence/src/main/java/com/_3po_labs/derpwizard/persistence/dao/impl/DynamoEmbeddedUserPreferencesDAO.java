package com._3po_labs.derpwizard.persistence.dao.impl;

import java.io.IOException;

import com._3po_labs.derpwizard.persistence.dao.UserPreferencesDAO;
import com._3po_labs.derpwizard.persistence.model.preferences.UserPreferences;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DynamoEmbeddedUserPreferencesDAO implements UserPreferencesDAO {

  private ObjectMapper objectMapper;
//  private AmazonDynamoDB dynamodb;
  
//  private String tableName = "userPreferences";
  
  public DynamoEmbeddedUserPreferencesDAO() {
    objectMapper = new ObjectMapper();

    //TODO: Get the in-memory version to work for testing - has problems with missing libsqlite4 at runtime
//    dynamodb = DynamoDBEmbedded.create();
    
    init();
  }
  
  private void init() {
   /* List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
    List<KeySchemaElement> keySchemaElements = new ArrayList<KeySchemaElement>();
    ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(25L,25L);
    dynamodb.createTable(attributeDefinitions, tableName, keySchemaElements, provisionedThroughput);*/
  }

  @Override
  public void setPreferences(UserPreferences userPreferences) {
    
  }

  @Override
  public UserPreferences getPreferences(String userId) {

    try {
      return objectMapper.readValue(userId, new TypeReference<UserPreferences>(){});
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public <T> T getPreferencesByNamespace(String userId, String skillName,
      TypeReference<T> type) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T getPreferencesForDefaultNamespace(String userId,
      TypeReference<T> type) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> void setPreferencesForDefaultNamespace(String userId,
      T skillPreferences) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public <T> void setPreferencesByNamespace(String userId, String skillName,
      T skillPreferences) {
    // TODO Auto-generated method stub
    
  }

}
