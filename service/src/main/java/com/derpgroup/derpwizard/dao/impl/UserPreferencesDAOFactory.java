package com.derpgroup.derpwizard.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.derpgroup.derpwizard.configuration.UserPreferencesDAOConfig;
import com.derpgroup.derpwizard.dao.UserPreferencesDAO;

public abstract class UserPreferencesDAOFactory {
  
  private static final Logger LOG = LoggerFactory.getLogger(UserPreferencesDAOFactory.class);

  public static UserPreferencesDAO build(UserPreferencesDAOConfig config){
    switch(config.getType()){
    case DYNAMO:
      DynamoUserPreferencesDAO dao = new DynamoUserPreferencesDAO(config.getAccessKey(), config.getSecretKey(), config.getTableName());
      dao.setNamespace(config.getNamespace());
      return dao;
      default:
        LOG.error("Unknown DAO type for user preferences DB.");
    }
    return null;
  }
  
  public enum UserPreferencesDAOTypes{
    DYNAMO
  }
}
