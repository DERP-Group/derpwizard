package com.derpgroup.derpwizard.voice.model;

import java.util.Map;

public class CommonMetadata {

  private Map<String,Object> conversationHistory;

  public Map<String, Object> getConversationHistory() {
    return conversationHistory;
  }

  public void setConversationHistory(Map<String, Object> conversationHistory) {
    this.conversationHistory = conversationHistory;
  }
}
