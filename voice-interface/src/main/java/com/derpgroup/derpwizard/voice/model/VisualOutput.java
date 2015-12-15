package com.derpgroup.derpwizard.voice.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class VisualOutput {
  
  private Map<String,Object> output = new LinkedHashMap<String, Object>();
  private String title;
  private String text;
  
  public VisualOutput(){}

  public Map<String, Object> getOutput() {return output;}
  public void setOutput(Map<String, Object> output) {this.output = output;}
  public String getTitle() {return title;}
  public void setTitle(String title) {this.title = title;}
  public String getText() {return text;}
  public void setText(String text) {this.text = text;}
}
