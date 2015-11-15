package com.derpgroup.derpwizard.voice.model;

import java.util.Deque;

public class CommonMetadata {

  private Deque<ConversationHistoryEntry> conversationHistory;

  public Deque<ConversationHistoryEntry> getConversationHistory() {
    return conversationHistory;
  }

  public void setConversationHistory(Deque<ConversationHistoryEntry> conversationHistory) {
    this.conversationHistory = conversationHistory;
  }
}
