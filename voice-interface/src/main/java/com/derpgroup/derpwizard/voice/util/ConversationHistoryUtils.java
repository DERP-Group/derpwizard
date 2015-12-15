package com.derpgroup.derpwizard.voice.util;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.derpgroup.derpwizard.voice.exception.DerpwizardException;
import com.derpgroup.derpwizard.voice.model.CommonMetadata;
import com.derpgroup.derpwizard.voice.model.ConversationHistoryEntry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversationHistoryUtils {

  private static ObjectMapper mapper;
  
  public static ConversationHistoryEntry getLastNonMetaRequestBySubject(Deque<ConversationHistoryEntry> conversationHistory, Set<String> metaSubjects){ //Lets talk about what this should be named
    
    for(ConversationHistoryEntry entry : conversationHistory){
      if(entry.getMessageSubject() != null && !metaSubjects.contains(entry.getMessageSubject())){
        return entry;
      }
    }
    
    return null; //Should this return null?
  }
  
  public static void registerRequestInConversationHistory(String messageSubject, Map<String,String> messageMap, CommonMetadata metadata, Deque<ConversationHistoryEntry> conversationHistory) throws DerpwizardException {
    if(conversationHistory == null){
      conversationHistory = new ArrayDeque<ConversationHistoryEntry>();
    }

    CommonMetadata metadataClone;
    try {
      String metadataString;metadataString = getMapper().writeValueAsString(metadata);
      metadataClone = getMapper().readValue(metadataString, new TypeReference<CommonMetadata>(){});
    } catch (IOException e) {
      throw new DerpwizardException("Unknown exception.",e.getMessage(),"IOException adding to conversation history.");
    }
    
    ConversationHistoryEntry entry = new ConversationHistoryEntry();
    entry.setMessageMap(new LinkedHashMap<String,String>(messageMap));
    entry.setMessageSubject(messageSubject);
    entry.setMetadata(metadataClone);
    
    conversationHistory.push(entry);
    metadata.setConversationHistory(conversationHistory); //this is only needed in case it was null coming in
  }
  
  public static ObjectMapper getMapper(){
    if(mapper == null){
      mapper = new ObjectMapper();
    }
    
    return mapper;
  }
}
