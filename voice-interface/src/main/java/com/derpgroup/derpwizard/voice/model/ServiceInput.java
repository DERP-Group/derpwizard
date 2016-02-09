package com.derpgroup.derpwizard.voice.model;

import java.util.Map;

public class ServiceInput {

  private CommonMetadata metadata;
  private Map<String, String> messageAsMap;
  private String subject;

  public CommonMetadata getMetadata() {return metadata;}
  public void setMetadata(CommonMetadata metadata) {this.metadata = metadata;}
  public Map<String, String> getMessageAsMap() {return messageAsMap;}
  public void setMessageAsMap(Map<String, String> messageAsMap) {this.messageAsMap = messageAsMap;}
  public String getSubject() {return subject;}
  public void setSubject(String subject) {this.subject = subject;}
}
