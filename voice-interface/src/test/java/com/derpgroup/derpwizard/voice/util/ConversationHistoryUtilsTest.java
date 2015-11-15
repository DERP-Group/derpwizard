package com.derpgroup.derpwizard.voice.util;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.derpgroup.derpwizard.voice.model.AlexaInput;
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
  public void testAppendToConversationHistory_firstEntry(){
    
    IntentRequest intentRequest = IntentRequest.builder().withRequestId("123").withIntent(Intent.builder().withName("testSubject").build()).build();
    AlexaInput alexaInput = new AlexaInput(intentRequest);
    
    ConversationHistoryUtils.appendToConversationHistory(alexaInput, alexaInput.getMetadata());
    assertNotNull(alexaInput);
    assertNotNull(alexaInput.getMetadata());
    assertNotNull(alexaInput.getMetadata().getConversationHistory());
    assertEquals(1, alexaInput.getMetadata().getConversationHistory().size());
    assertNotNull(alexaInput.getMetadata().getConversationHistory().peek());
    assertEquals("testSubject",alexaInput.getMetadata().getConversationHistory().pop().getMessageSubject());
  }
  
  @Test
  public void testAppendToConversationHistory_existingEntry(){
    CommonMetadata commonMetadata = new CommonMetadata();
    ConversationHistoryEntry entry = new ConversationHistoryEntry();
    entry.setMessageSubject("testSubject1");
    Deque<ConversationHistoryEntry> conversationHistory = new ArrayDeque<ConversationHistoryEntry>();
    conversationHistory.push(entry);
    commonMetadata.setConversationHistory(conversationHistory);
    
    IntentRequest intentRequest = IntentRequest.builder().withRequestId("123").withIntent(Intent.builder().withName("testSubject2").build()).build();
    AlexaInput alexaInput = new AlexaInput(intentRequest, commonMetadata);
    
    ConversationHistoryUtils.appendToConversationHistory(alexaInput, alexaInput.getMetadata());
    assertNotNull(alexaInput);
    assertNotNull(alexaInput.getMetadata());
    assertNotNull(alexaInput.getMetadata().getConversationHistory());
    assertEquals(2, alexaInput.getMetadata().getConversationHistory().size());
    assertNotNull(alexaInput.getMetadata().getConversationHistory().peek());
    assertEquals("testSubject2",alexaInput.getMetadata().getConversationHistory().pop().getMessageSubject());
    assertNotNull(alexaInput.getMetadata().getConversationHistory().peek());
    assertEquals("testSubject1",alexaInput.getMetadata().getConversationHistory().pop().getMessageSubject());
  }
}
