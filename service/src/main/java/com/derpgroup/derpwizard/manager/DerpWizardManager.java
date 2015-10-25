package com.derpgroup.derpwizard.manager;

import com.derpgroup.derpwizard.voice.model.VoiceInput;

public class DerpWizardManager {

  public String handleRequest(VoiceInput vi){
    
    DerpWizardRequestTypes requestType = vi.getRequestName(DerpWizardRequestTypes.class);
    
    switch(requestType){
    case HELLO:
      return doHelloRequest();
    case HELP:
      return doHelpRequest();
    default:
      return "Unknown request type '" + requestType + "'.";
    }
  }

  private String doHelpRequest() {
    return "<speak><p><s>I'd love to help, but I don't have any help topics programmed yet.</s></p></speak>";
  }

  private String doHelloRequest() {
    return "<speak><p><s>Hi. This is DerpWizard.</s></p></speak>";
  }
  
}
