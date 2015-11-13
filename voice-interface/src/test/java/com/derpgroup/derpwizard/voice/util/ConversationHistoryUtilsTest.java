package com.derpgroup.derpwizard.voice.util;

import java.util.ArrayDeque;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.when;

import com.derpgroup.derpwizard.voice.model.AlexaInput;
import com.derpgroup.derpwizard.voice.model.CommonMetadata;
import com.derpgroup.derpwizard.voice.model.ConversationHistoryEntry;
import com.derpgroup.derpwizard.voice.model.VoiceInput.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class ConversationHistoryUtilsTest {

  @Mock AlexaInput mockAlexaInput;
  @Mock CommonMetadata mockCommonMetadata;
  
  ObjectMapper mapper;
  
  @Before
  public void setup(){
    
    when(mockCommonMetadata.getConversationHistory()).thenReturn(new ArrayDeque<ConversationHistoryEntry>());
    
    when(mockAlexaInput.getMessageSubject()).thenReturn("subject");
    when(mockAlexaInput.getMessageType()).thenReturn(MessageType.DEFAULT);
    when(mockAlexaInput.getMessageAsMap()).thenReturn(new HashMap<String,String>());
    
    mapper = new ObjectMapper();
  }
  
  /*@Test
  public void testAppendToConversationHistory_firstEntry(){
    ConversationHistoryUtils.appendToConversationHistory(mockAlexaInput, new CommonMetadata(), mapper);
    assertNotNull(mockAlexaInput);
    assertNotNull(mockAlexaInput.getMetadata());
    assertNotNull(mockAlexaInput.getMetadata().getConversationHistory());
    assertEquals(1, mockAlexaInput.getMetadata().getConversationHistory().size());
  }*/
}
