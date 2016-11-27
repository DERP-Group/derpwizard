package com._3po_labs.derpwizard.persistence.dao;

import java.util.List;

import com._3po_labs.derpwizard.core.exception.DerpwizardException;
import com._3po_labs.derpwizard.persistence.model.accountlinking.ExternalAccountLink;
import com._3po_labs.derpwizard.persistence.model.accountlinking.UserAccount;

public interface AccountLinkingDAO {

  public UserAccount getUserByUserId(String alexaUserId) throws DerpwizardException;
  
  public UserAccount updateUser(UserAccount user) throws DerpwizardException;
  
  public String generateMappingTokenForUserId(String userId) throws DerpwizardException;
  
  public String getUserIdByMappingToken(String token) throws DerpwizardException;
  
  public void expireMappingToken(String token) throws DerpwizardException;
  
  public String generateAuthToken(String userId) throws DerpwizardException;
  
  public String getUserIdByAuthToken(String token) throws DerpwizardException;
  
  public void expireGrantedToken(String token) throws DerpwizardException;

  public ExternalAccountLink createAccountLink(ExternalAccountLink link) throws DerpwizardException;

  public ExternalAccountLink getAccountLinkByUserIdAndExternalSystemName(String userId, String externalSystemName) throws DerpwizardException;

  public ExternalAccountLink getAccountLinkByExternalUserIdAndExternalSystemName(String externalUserId, String externalSystemName) throws DerpwizardException;

  public List<ExternalAccountLink> getAccountLinksByUserId(String userId) throws DerpwizardException;
}
