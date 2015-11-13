package com.derpgroup.derpwizard.voice.util;

import java.util.Deque;
import java.util.Set;

import com.derpgroup.derpwizard.voice.model.ConversationHistoryEntry;

public class ConversationHistoryUtils {

  public static ConversationHistoryEntry getLastNonMetaRequestBySubject(Deque<ConversationHistoryEntry> conversationHistory, Set<String> metaSubjects){ //Lets talk about what this should be named
    
    for(ConversationHistoryEntry entry : conversationHistory){
      if(entry.getMessageSubject() != null && !metaSubjects.contains(entry.getMessageSubject())){
        return entry;
      }
    }
    
    return null; //Should this return null?
  }
}
