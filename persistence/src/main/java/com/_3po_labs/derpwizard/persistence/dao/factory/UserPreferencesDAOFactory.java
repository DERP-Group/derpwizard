package com._3po_labs.derpwizard.persistence.dao.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._3po_labs.derpwizard.persistence.configuration.UserPreferencesDAOConfig;
import com._3po_labs.derpwizard.persistence.dao.UserPreferencesDAO;
import com._3po_labs.derpwizard.persistence.dao.impl.DynamoUserPreferencesDAO;

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
