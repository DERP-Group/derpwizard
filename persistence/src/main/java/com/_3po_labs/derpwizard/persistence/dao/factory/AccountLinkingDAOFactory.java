package com._3po_labs.derpwizard.persistence.dao.factory;

import com._3po_labs.derpwizard.persistence.configuration.AccountLinkingDAOConfig;
import com._3po_labs.derpwizard.persistence.dao.AccountLinkingDAO;
import com._3po_labs.derpwizard.persistence.dao.impl.DynamoAccountLinkingDAO;
import com._3po_labs.derpwizard.persistence.dao.impl.H2EmbeddedAccountLinkingDAO;

public class AccountLinkingDAOFactory {

	public static AccountLinkingDAO getDAO(AccountLinkingDAOConfig config) {
		AccountLinkingDAO dao = null;
		switch (config.getType()) {
		case H2:
			dao = new H2EmbeddedAccountLinkingDAO(config);
			break;
		case DYNAMO:
		    	dao = new DynamoAccountLinkingDAO(config);
			break;
		default:
			throw new RuntimeException("Unsupported AccountLinkingDAO type.");
		}
		return dao;
	}

	public enum AccountLinkingDAOTypes {
		H2, DYNAMO
	}

}
