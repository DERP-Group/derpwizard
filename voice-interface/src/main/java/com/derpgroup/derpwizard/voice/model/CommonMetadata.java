package com.derpgroup.derpwizard.voice.model;

import java.util.List;

public class CommonMetadata {

  private List<ConversationHistoryEntry> conversationHistory;

  public List<ConversationHistoryEntry> getConversationHistory() {
    return conversationHistory;
  }

  public void setConversationHistory(List<ConversationHistoryEntry> conversationHistory) {
    this.conversationHistory = conversationHistory;
  }
}
