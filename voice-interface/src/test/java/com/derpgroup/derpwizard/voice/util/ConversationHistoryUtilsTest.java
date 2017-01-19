package com.derpgroup.derpwizard.voice.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com._3po_labs.derpwizard.core.exception.DerpwizardException;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.derpgroup.derpwizard.voice.alexa.AlexaUtils;
import com.derpgroup.derpwizard.voice.model.CommonMetadata;
import com.derpgroup.derpwizard.voice.model.ConversationHistoryEntry;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversationHistoryUtilsTest {
  
  ObjectMapper mapper;
  
  @Before
  public void setup(){
    
    mapper = new ObjectMapper();
  }
  
  @Test
  public void testAppendToConversationHistory_firstEntry() throws DerpwizardException{
    
    String subject = "testSubject";
    IntentRequest intentRequest = IntentRequest.builder().withRequestId("123").withIntent(Intent.builder().withName(subject).build()).build();
    Map<String, String> messageAsMap = AlexaUtils.getMessageAsMap(intentRequest);
    
    CommonMetadata outputMetadata = new CommonMetadata();
    
    ConversationHistoryUtils.registerRequestInConversationHistory(subject, messageAsMap, outputMetadata, outputMetadata.getConversationHistory());
    assertNotNull(outputMetadata);
    assertNotNull(outputMetadata.getConversationHistory());
    assertEquals(1, outputMetadata.getConversationHistory().size());
    assertNotNull(outputMetadata.getConversationHistory().peek());
    assertEquals(subject,outputMetadata.getConversationHistory().pop().getMessageSubject());
  }
  
  @Test
  public void testAppendToConversationHistory_existingEntry() throws DerpwizardException{
    CommonMetadata outputMetadata = new CommonMetadata();
    ConversationHistoryEntry entry = new ConversationHistoryEntry();
    String subject1 = "testSubject1";
    entry.setMessageSubject(subject1);
    Deque<ConversationHistoryEntry> conversationHistory = new ArrayDeque<ConversationHistoryEntry>();
    conversationHistory.push(entry);
    outputMetadata.setConversationHistory(conversationHistory);
    
    String subject2 = "testSubject2";
    IntentRequest intentRequest = IntentRequest.builder().withRequestId("123").withIntent(Intent.builder().withName(subject2).build()).build();
    Map<String, String> messageAsMap = AlexaUtils.getMessageAsMap(intentRequest);
    
    ConversationHistoryUtils.registerRequestInConversationHistory(subject2, messageAsMap, outputMetadata, outputMetadata.getConversationHistory());
    assertNotNull(outputMetadata);
    assertNotNull(outputMetadata.getConversationHistory());
    assertEquals(2, outputMetadata.getConversationHistory().size());
    assertNotNull(outputMetadata.getConversationHistory().peek());
    assertEquals(subject2,outputMetadata.getConversationHistory().pop().getMessageSubject());
    assertNotNull(outputMetadata.getConversationHistory().peek());
    assertEquals(subject1,outputMetadata.getConversationHistory().pop().getMessageSubject());
  }
}
