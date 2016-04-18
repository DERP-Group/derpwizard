package com.derpgroup.derpwizard.voice.model;

import java.util.Deque;

public class CommonMetadata {

  private String userId;
  private Deque<ConversationHistoryEntry> conversationHistory;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Deque<ConversationHistoryEntry> getConversationHistory() {
    return conversationHistory;
  }

  public void setConversationHistory(Deque<ConversationHistoryEntry> conversationHistory) {
    this.conversationHistory = conversationHistory;
  }
}
