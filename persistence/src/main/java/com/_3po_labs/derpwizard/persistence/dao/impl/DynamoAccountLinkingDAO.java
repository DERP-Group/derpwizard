package com._3po_labs.derpwizard.persistence.dao.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._3po_labs.derpwizard.core.exception.DerpwizardException;
import com._3po_labs.derpwizard.persistence.configuration.AccountLinkingDAOConfig;
import com._3po_labs.derpwizard.persistence.dao.AccountLinkingDAO;
import com._3po_labs.derpwizard.persistence.model.accountlinking.ExternalAccountLink;
import com._3po_labs.derpwizard.persistence.model.accountlinking.UserAccount;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DynamoAccountLinkingDAO implements AccountLinkingDAO {

	  private static final Logger LOG = LoggerFactory.getLogger(DynamoAccountLinkingDAO.class);

	private ObjectMapper mapper;
	private DynamoDB dynamoDB;
	private Table table;
	
	public DynamoAccountLinkingDAO(AccountLinkingDAOConfig config){
		this(config.getAccessKey(), config.getSecretKey(), config.getTableName());
	}

	public DynamoAccountLinkingDAO(String accessKey, String secretKey, String tableName) {
	    mapper = new ObjectMapper();
	    
	    BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
	    AmazonDynamoDBClient client = new AmazonDynamoDBClient(creds);
	    
	    dynamoDB = new DynamoDB(client);

	    table = dynamoDB.getTable(tableName);
	}

	@Override
	public UserAccount getUserByUserId(String alexaUserId) throws DerpwizardException {
		throw new DerpwizardException(
				"This method is not supported on this implementation of the AccountLinking interface.");
	}

	@Override
	public UserAccount updateUser(UserAccount user) throws DerpwizardException {
		throw new DerpwizardException(
				"This method is not supported on this implementation of the AccountLinking interface.");
	}

	@Override
	public String generateMappingTokenForUserId(String userId) throws DerpwizardException {
		throw new DerpwizardException(
				"This method is not supported on this implementation of the AccountLinking interface.");
	}

	@Override
	public String getUserIdByMappingToken(String token) throws DerpwizardException {
		throw new DerpwizardException(
				"This method is not supported on this implementation of the AccountLinking interface.");
	}

	@Override
	public void expireMappingToken(String token) throws DerpwizardException {
		throw new DerpwizardException(
				"This method is not supported on this implementation of the AccountLinking interface.");
	}

	@Override
	public String generateAuthToken(String userId) throws DerpwizardException {
		throw new DerpwizardException(
				"This method is not supported on this implementation of the AccountLinking interface.");
	}

	@Override
	public String getUserIdByAuthToken(String token) throws DerpwizardException {
		throw new DerpwizardException("This method is not supported on this implementation of the AccountLinking interface.");
	}

	@Override
	public void expireGrantedToken(String token) throws DerpwizardException {
		throw new DerpwizardException("This method is not supported on this implementation of the AccountLinking interface.");
	}

	@Override
	public ExternalAccountLink createAccountLink(ExternalAccountLink link) throws DerpwizardException {
		if(link == null || link.getUserId() == null || link.getExternalSystemName() == null || link.getExternalUserId() == null){
			throw new DerpwizardException("Invalid input. External account link must contain at least a userId, externalSystemName, and externalUserId.");
		}
		if(link.getAuthToken() != null || link.getRefreshToken() != null){
			throw new DerpwizardException("Invalid input. This implementation does not yet support storing OAuth related data.");
		}

	    String externalAccountLinkJson = null;
	    try {
	      externalAccountLinkJson = mapper.writeValueAsString(link);
	    } catch (JsonProcessingException e) {
	      LOG.error(e.getMessage());
	      throw new DerpwizardException("Could not convert external account link to JSON.");
	    }
	    
	    Item item = Item.fromJSON(externalAccountLinkJson);
	    table.putItem(item);
	    return link;
	}

	@Override
	public ExternalAccountLink getAccountLinkByUserIdAndExternalSystemName(String userId, String externalSystemName) throws DerpwizardException {
		String filterExpression = "userId = :userId and externalSystemName = :externalSystemName";
		return queryTable(filterExpression, new ValueMap().withString(":userId",userId).withString(":externalSystemName", externalSystemName));
	}

	@Override
	public ExternalAccountLink getAccountLinkByExternalUserIdAndExternalSystemName(String externalUserId, String externalSystemName) throws DerpwizardException {
		String filterExpression = "externalUserId = :externalUserId and externalSystemName = :externalSystemName";
		return queryTable(filterExpression, new ValueMap().withString(":externalUserId",externalUserId).withString(":externalSystemName", externalSystemName));
	}

	@Override
	public List<ExternalAccountLink> getAccountLinksByUserId(String userId) throws DerpwizardException {
		throw new DerpwizardException("This method is not supported on this implementation of the AccountLinking interface.");
	}

	public ExternalAccountLink queryTable(String filterExpression, ValueMap valueMap) throws DerpwizardException {
		
		ScanSpec scanSpec = new ScanSpec().withFilterExpression(filterExpression).withValueMap(valueMap);
		ItemCollection<ScanOutcome> items = table.scan(scanSpec);
		Item item;
		
		if(items.getTotalCount() > 1){
			throw new DerpwizardException("Too many items returned for provided expression.");
		}else{
			Iterator<Item> iter = items.iterator();
			if(iter.hasNext()){
				item = iter.next();
			}else{
				return null;
			}
		}

		try {
			return mapper.readValue(item.toJSON(), new TypeReference<ExternalAccountLink>() {
			});
		} catch (IOException e) {
			LOG.error(e.getMessage());
			throw new DerpwizardException("Couldn't retrieve external account link due to exception.");
		}
	}
}
