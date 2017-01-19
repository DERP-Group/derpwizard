package com._3po_labs.derpwizard.core.exception;


public class DerpwizardException extends Exception {

  private static final long serialVersionUID = -7162810537121525705L;

  private String ssmlMessage; //Should this be a SsmlDocument?
  private String shortFormTextMessage;

  public DerpwizardException(String message){ //Given a single input, we make no "ssml" distinction.  It's just "the message"
    this(message, message);
  }
  
  public DerpwizardException(String ssmlMessage, String fullTextMessage){ //If no shortForm supplied, copy fullText message
    this(ssmlMessage, fullTextMessage, fullTextMessage);
  }
  
  public DerpwizardException(String ssmlMessage, String fullTextMessage, String shortFormTextMessage){
    super(fullTextMessage); //Exception.message is set to fullTextMesage, can be retrieved with e.getMessage();
    this.ssmlMessage = ssmlMessage;
    this.shortFormTextMessage = shortFormTextMessage;
  }

  public String getSsmlMessage() {
    return ssmlMessage;
  }

  public void setSsmlMessage(String ssmlMessage) {
    this.ssmlMessage = ssmlMessage;
  }

  public String getShortFormTextMessage() {
    return shortFormTextMessage;
  }

  public void setShortFormTextMessage(String shortFormTextMessage) {
    this.shortFormTextMessage = shortFormTextMessage;
  }
  
  public enum DerpwizardExceptionReasons{
    MISSING_INFO("<speak>I couldn't complete the request because some information was missing.</speak>");
    
    private String ssml;
    
    private DerpwizardExceptionReasons(String ssml){
      this.ssml = ssml;
    }
    
    public String getSsml(){
      return ssml;
    }
  }
}
