package com.derpgroup.derpwizard.voice.model;

public class ServiceOutput {
  private VoiceOutput voiceOutput;
  private VisualOutput visualOutput;
  private CommonMetadata metadata;
  boolean conversationEnded = false;
  private VoiceOutput delayedVoiceOutput;

  public ServiceOutput(){
    voiceOutput = new VoiceOutput();
    visualOutput = new VisualOutput();
    metadata = new CommonMetadata();
    delayedVoiceOutput = new VoiceOutput();
  }

  public VoiceOutput getVoiceOutput() {return voiceOutput; }
  public void setVoiceOutput(VoiceOutput voiceOutput) {this.voiceOutput = voiceOutput;}
  public VisualOutput getVisualOutput() {return visualOutput;}
  public void setVisualOutput(VisualOutput visualOutput) {this.visualOutput = visualOutput;}
  public CommonMetadata getMetadata() {return metadata;}
  public void setMetadata(CommonMetadata metadata) {this.metadata = metadata;}
  public boolean isConversationEnded() {return conversationEnded;}
  public void setConversationEnded(boolean conversationEnded) {this.conversationEnded = conversationEnded;}
  public VoiceOutput getDelayedVoiceOutput() {return delayedVoiceOutput;}
  public void setDelayedVoiceOutput(VoiceOutput delayedVoiceOutput) {this.delayedVoiceOutput = delayedVoiceOutput;}
}
