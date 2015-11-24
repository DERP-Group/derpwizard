package com.derpgroup.derpwizard.voice.exception;

import java.util.Map;

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

public class DerpwizardExceptionAlexaWrapper extends SpeechletResponseEnvelope {
  
  public DerpwizardExceptionAlexaWrapper(DerpwizardException exception){
    this(exception,null,null);
  }
  
  public DerpwizardExceptionAlexaWrapper(DerpwizardException exception, String version){
    this(exception, version, null);
  }
  
  public DerpwizardExceptionAlexaWrapper(DerpwizardException exception, Map<String,Object> sessionAttributes){
    this(exception, null, sessionAttributes);
  }
  
  public DerpwizardExceptionAlexaWrapper(DerpwizardException exception, String version, Map<String,Object> sessionAttributes){
    this.setVersion(version);
    this.setSessionAttributes(sessionAttributes);
    
    SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
    outputSpeech.setSsml(exception.getSsmlMessage());
    
    SimpleCard card = new SimpleCard();
    card.setContent(exception.getMessage());
    card.setTitle(exception.getShortFormTextMessage());
    
    SpeechletResponse response = new SpeechletResponse();
    response.setCard(card);
    response.setOutputSpeech(outputSpeech);
    response.setShouldEndSession(true); //At least until we decide on a way to pass this
    this.setResponse(response);
  }
}
