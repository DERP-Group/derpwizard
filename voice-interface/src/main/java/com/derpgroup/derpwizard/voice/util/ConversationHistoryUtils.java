package com.derpgroup.derpwizard.voice.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.derpgroup.derpwizard.voice.model.CommonMetadata;
import com.derpgroup.derpwizard.voice.model.ConversationHistoryEntry;
import com.derpgroup.derpwizard.voice.model.VoiceInput;
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
  
  //Should this operate on the object directly, or return something?
  public static void registerRequestInConversationHistory(String messageSubject, Map<String,String> messageMap, CommonMetadata metadata, Deque<ConversationHistoryEntry> conversationHistory) {
    //Deque<ConversationHistoryEntry> conversationHistory = metadata.getConversationHistory();
    if(conversationHistory == null){
      conversationHistory = new ArrayDeque<ConversationHistoryEntry>();
    }
    
    CommonMetadata metadataClone = getMapper().convertValue(metadata, new TypeReference<CommonMetadata>(){});
    
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
