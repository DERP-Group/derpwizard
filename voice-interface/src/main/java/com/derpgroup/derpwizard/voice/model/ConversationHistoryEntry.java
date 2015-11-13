package com.derpgroup.derpwizard.voice.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ConversationHistoryEntry {

  @JsonIgnoreProperties({ "conversationHistory" })
  private CommonMetadata metadata;
  private Map<String,String> messageMap;
  private String messageSubject;
  
  public CommonMetadata getMetadata() {
    return metadata;
  }
  
  public void setMetadata(CommonMetadata metadata) {
    this.metadata = metadata;
  }

  public Map<String, String> getMessageMap() {
    return messageMap;
  }

  public void setMessageMap(Map<String, String> messageMap) {
    this.messageMap = messageMap;
  }

  public String getMessageSubject() {
    return messageSubject;
  }

  public void setMessageSubject(String messageSubject) {
    this.messageSubject = messageSubject;
  }
}
