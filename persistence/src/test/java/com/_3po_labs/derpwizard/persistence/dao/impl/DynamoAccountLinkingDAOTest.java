package com._3po_labs.derpwizard.persistence.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com._3po_labs.derpwizard.core.exception.DerpwizardException;
import com._3po_labs.derpwizard.persistence.model.accountlinking.ExternalAccountLink;

public class DynamoAccountLinkingDAOTest {

	  private DynamoAccountLinkingDAO accountLinkingDAO;
	  private String userId = null;
	  
	  @Before
	  public void setup(){
	    userId = "testUserId_" + UUID.randomUUID().toString();
	    accountLinkingDAO = new DynamoAccountLinkingDAO("*******","*********","AccountLinks_Test");
	  }
	  
	  @Test
	  public void testCreateLink_newLink() throws DerpwizardException{	    
	    ExternalAccountLink accountLink = new ExternalAccountLink();
	    accountLink.setUserId(userId);
	    accountLink.setExternalSystemName("AMAZON_ALEXA");
	    accountLink.setExternalUserId(UUID.randomUUID().toString());
	    
	    accountLinkingDAO.createAccountLink(accountLink);
	    
	    /*UserPreferences result = accountLinkingDAO.getPreferences(userId);
	    assertNotNull(result);
	    assertEquals(userId,result.getUserId());
	    assertNotNull(result.getPreferences());
	    
	    Map<String,Map<String,Object>> preferencesResult = (Map<String, Map<String, Object>>) result.getPreferences();
	    assertTrue(preferencesResult.containsKey("DERPWIZARD"));
	    assertNotNull(preferencesResult.get("DERPWIZARD"));
	    
	    Map<String,Object> derpwizardPreferencesResult = preferencesResult.get("DERPWIZARD");
	    assertTrue(derpwizardPreferencesResult.containsKey("fakePreference"));
	    assertEquals(derpwizardPreferencesResult.get("fakePreference"),"fakePreferenceValue");*/
	  }
	  
	  @Test
	  public void testGetAccountLinkByUserIdAndExternalSystemName() throws DerpwizardException{	    
		    ExternalAccountLink accountLink = new ExternalAccountLink();
		    accountLink.setUserId(userId);
		    accountLink.setExternalSystemName("AMAZON_ALEXA");
		    accountLink.setExternalUserId(UUID.randomUUID().toString());
		    
		    accountLinkingDAO.createAccountLink(accountLink);
		    ExternalAccountLink retrievedLink = accountLinkingDAO.getAccountLinkByUserIdAndExternalSystemName(accountLink.getUserId(), accountLink.getExternalSystemName());
		    assertNotNull(retrievedLink);
		    assertNotNull(retrievedLink.getUserId());
		    assertNotNull(retrievedLink.getExternalSystemName());
		    assertNotNull(retrievedLink.getExternalUserId());
		    
		    assertEquals(retrievedLink.getUserId(), accountLink.getUserId());
		    assertEquals(retrievedLink.getExternalSystemName(), accountLink.getExternalSystemName());
		    assertEquals(retrievedLink.getExternalUserId(), accountLink.getExternalUserId());
	  }
	  
	  @Test
	  public void testGetAccountLinkByUserIdAndExternalSystemName_twoExternalIds() throws DerpwizardException{	    
		    ExternalAccountLink accountLink1 = new ExternalAccountLink();
		    accountLink1.setUserId(userId);
		    accountLink1.setExternalSystemName("AMAZON_ALEXA");
		    accountLink1.setExternalUserId(UUID.randomUUID().toString());  
		    
		    ExternalAccountLink accountLink2 = new ExternalAccountLink();
		    accountLink2.setUserId(userId);
		    accountLink2.setExternalSystemName("OTHER_SYSTEM");
		    accountLink2.setExternalUserId(UUID.randomUUID().toString());
		    
		    accountLinkingDAO.createAccountLink(accountLink1);
		    accountLinkingDAO.createAccountLink(accountLink2);
		    
		    ExternalAccountLink retrievedLink1 = accountLinkingDAO.getAccountLinkByUserIdAndExternalSystemName(accountLink1.getUserId(), accountLink1.getExternalSystemName());
		    ExternalAccountLink retrievedLink2 = accountLinkingDAO.getAccountLinkByUserIdAndExternalSystemName(accountLink2.getUserId(), accountLink2.getExternalSystemName());
		    assertNotNull(retrievedLink1);
		    assertNotNull(retrievedLink1.getUserId());
		    assertNotNull(retrievedLink1.getExternalSystemName());
		    assertNotNull(retrievedLink1.getExternalUserId());
		    
		    assertEquals(retrievedLink1.getUserId(), accountLink1.getUserId());
		    assertEquals(retrievedLink1.getExternalSystemName(), accountLink1.getExternalSystemName());
		    assertEquals(retrievedLink1.getExternalUserId(), accountLink1.getExternalUserId());

		    assertNotNull(retrievedLink2);
		    assertNotNull(retrievedLink2.getUserId());
		    assertNotNull(retrievedLink2.getExternalSystemName());
		    assertNotNull(retrievedLink2.getExternalUserId());
		    
		    assertEquals(retrievedLink2.getUserId(), accountLink2.getUserId());
		    assertEquals(retrievedLink2.getExternalSystemName(), accountLink2.getExternalSystemName());
		    assertEquals(retrievedLink2.getExternalUserId(), accountLink2.getExternalUserId());
	  }
	  
	  @Test
	  public void testGetAccountLinkByExternalUserIdAndExternalSystemName() throws DerpwizardException{
		    
		    ExternalAccountLink accountLink = new ExternalAccountLink();
		    accountLink.setUserId(userId);
		    accountLink.setExternalSystemName("AMAZON_ALEXA");
		    accountLink.setExternalUserId(UUID.randomUUID().toString());
		    
		    accountLinkingDAO.createAccountLink(accountLink);
		    ExternalAccountLink retrievedLink = accountLinkingDAO.getAccountLinkByExternalUserIdAndExternalSystemName(accountLink.getExternalUserId(), accountLink.getExternalSystemName());
		    assertNotNull(retrievedLink);
		    assertNotNull(retrievedLink.getUserId());
		    assertNotNull(retrievedLink.getExternalSystemName());
		    assertNotNull(retrievedLink.getExternalUserId());
		    
		    assertEquals(retrievedLink.getUserId(), accountLink.getUserId());
		    assertEquals(retrievedLink.getExternalSystemName(), accountLink.getExternalSystemName());
		    assertEquals(retrievedLink.getExternalUserId(), accountLink.getExternalUserId());
	  }
	  
	  @Test
	  public void testGetAccountLinkByExternalUserIdAndExternalSystemName_twoUserIds() throws DerpwizardException{	    
		    ExternalAccountLink accountLink1 = new ExternalAccountLink();
		    accountLink1.setUserId(UUID.randomUUID().toString());
		    accountLink1.setExternalSystemName("AMAZON_ALEXA");
		    accountLink1.setExternalUserId(userId);  
		    
		    ExternalAccountLink accountLink2 = new ExternalAccountLink();
		    accountLink2.setUserId(UUID.randomUUID().toString());
		    accountLink2.setExternalSystemName("AMAZON_ALEXA");
		    accountLink2.setExternalUserId(userId);
		    
		    accountLinkingDAO.createAccountLink(accountLink1);
		    accountLinkingDAO.createAccountLink(accountLink2);
		    
		    ExternalAccountLink retrievedLink1 = accountLinkingDAO.getAccountLinkByUserIdAndExternalSystemName(accountLink1.getUserId(), accountLink1.getExternalSystemName());
		    ExternalAccountLink retrievedLink2 = accountLinkingDAO.getAccountLinkByUserIdAndExternalSystemName(accountLink2.getUserId(), accountLink2.getExternalSystemName());
		    assertNotNull(retrievedLink1);
		    assertNotNull(retrievedLink1.getUserId());
		    assertNotNull(retrievedLink1.getExternalSystemName());
		    assertNotNull(retrievedLink1.getExternalUserId());
		    
		    assertEquals(retrievedLink1.getUserId(), accountLink1.getUserId());
		    assertEquals(retrievedLink1.getExternalSystemName(), accountLink1.getExternalSystemName());
		    assertEquals(retrievedLink1.getExternalUserId(), accountLink1.getExternalUserId());

		    assertNotNull(retrievedLink2);
		    assertNotNull(retrievedLink2.getUserId());
		    assertNotNull(retrievedLink2.getExternalSystemName());
		    assertNotNull(retrievedLink2.getExternalUserId());
		    
		    assertEquals(retrievedLink2.getUserId(), accountLink2.getUserId());
		    assertEquals(retrievedLink2.getExternalSystemName(), accountLink2.getExternalSystemName());
		    assertEquals(retrievedLink2.getExternalUserId(), accountLink2.getExternalUserId());
	  }

}
