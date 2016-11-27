package com._3po_labs.derpwizard.persistence.dao.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._3po_labs.derpwizard.persistence.dao.UserPreferencesDAO;
import com._3po_labs.derpwizard.persistence.model.preferences.UserPreferences;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DynamoUserPreferencesDAO implements UserPreferencesDAO {

  private static final Logger LOG = LoggerFactory.getLogger(DynamoUserPreferencesDAO.class);

  private ObjectMapper objectMapper;
  private DynamoDB dynamoDB;
  private Table table;
  
  private String defaultNamespace;
  
  protected DynamoUserPreferencesDAO(String accessKey, String secretKey, String tableName) {
    objectMapper = new ObjectMapper();
    
    BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
    AmazonDynamoDBClient client = new AmazonDynamoDBClient(creds);
    
    dynamoDB = new DynamoDB(client);

    table = dynamoDB.getTable(tableName);
  }

  @Override
  public void setPreferences(UserPreferences userPreferences) {
    String userPreferencesJson = null;
    try {
      userPreferencesJson = objectMapper.writeValueAsString(userPreferences);
    } catch (JsonProcessingException e) {
      LOG.error(e.getMessage());
    }
    
    Item item = Item.fromJSON(userPreferencesJson);
    table.putItem(item);
  }

  @Override
  public <T> void setPreferencesForDefaultNamespace(String userId, T skillPreferences) {
    setPreferencesByNamespace(userId, defaultNamespace, skillPreferences);
  }
  
  @Override
  public <T> void setPreferencesByNamespace(String userId, String skillName, T skillPreferences) {
    UserPreferences userPreferences = new UserPreferences();
    userPreferences.setUserId(userId);
    
    Map<String, T> preferences = new HashMap<String, T>();
    preferences.put(skillName, skillPreferences);
    userPreferences.setPreferences(preferences);
    
    setPreferences(userPreferences);
  }

  @Override
  public UserPreferences getPreferences(String userId) {
    return queryTable(userId, null);
  }

  @Override
  public <T> T getPreferencesForDefaultNamespace(String userId, TypeReference<T> type) {
    return getPreferencesByNamespace(userId, defaultNamespace, type);
  }
  
  @Override
  public <T> T getPreferencesByNamespace(String userId, String namespace, TypeReference<T> type){
    if(userId == null){
      throw new IllegalArgumentException("Could not retrieve preferences for null userId");
    }
    if(namespace == null){
      throw new IllegalArgumentException("Could not retrieve preferences for null namespace");
    }
    StringBuilder namespaceFilter = new StringBuilder("preferences.");
    namespaceFilter.append(namespace);
    UserPreferences userPreferences = queryTable(userId, namespaceFilter.toString());
    if(userPreferences == null || userPreferences.getPreferences() == null){
      return null;
    }
    T output = objectMapper.convertValue(userPreferences.getPreferences().get(namespace), type);
    return output;
  }
  
  public UserPreferences queryTable(String userId,String filterExpression){
    Item item = table.getItem("userId", userId, filterExpression,null);
    if(item == null){
      return createUserRecord(userId);
    }
    System.out.println(item.toJSONPretty().toString());
    
    try {
      return objectMapper.readValue(item.toJSON(), new TypeReference<UserPreferences>(){});
    } catch (IOException e) {
      LOG.error(e.getMessage());
      return null;
    }
  }
  
  public UserPreferences createUserRecord(String userId){
    ValueMap vm = new ValueMap().withMap(":val1", new HashMap<String,Object>());
    UpdateItemSpec spec = new UpdateItemSpec().withPrimaryKey("userId", userId).withUpdateExpression("set preferences = :val1").withValueMap(vm).withReturnValues(ReturnValue.ALL_NEW);
    UpdateItemOutcome outcome = table.updateItem(spec);
    try {
      return objectMapper.readValue(outcome.getItem().toJSON(), new TypeReference<UserPreferences>(){});
    } catch (IOException e) {
      LOG.error(e.getMessage());
      return null;
    }
  }

  public String getDefaultServiceName() {
    return defaultNamespace;
  }

  public void setNamespace(String defaultServiceName) {
    this.defaultNamespace = defaultServiceName;
  }
}
