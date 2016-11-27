package com._3po_labs.derpwizard.persistence.dao.impl;

import com._3po_labs.derpwizard.persistence.configuration.AccountLinkingDAOConfig;
import com._3po_labs.derpwizard.persistence.dao.AccountLinkingDAO;

public class AccountLinkingDAOFactory {

  public static AccountLinkingDAO getDAO(AccountLinkingDAOConfig config){
    AccountLinkingDAO dao = null;
    switch(config.getType().toUpperCase()){
    case "H2": 
      dao = new H2EmbeddedAccountLinkingDAO(config);
      break;
      default:
        throw new RuntimeException("Unsupported AccountLinkingDAO type.");
    }
    return dao;
  }
  
}
