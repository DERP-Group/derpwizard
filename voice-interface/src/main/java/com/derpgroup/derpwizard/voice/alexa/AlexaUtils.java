package com.derpgroup.derpwizard.voice.alexa;

import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com._3po_labs.derpwizard.core.exception.DerpwizardException;
import com._3po_labs.derpwizard.core.exception.DerpwizardException.DerpwizardExceptionReasons;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;
import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlexaUtils {

  public static void validateAlexaRequest(SpeechletRequestEnvelope request, String signatureCertChainUrl, String signature) throws DerpwizardException{

    try {
      TimestampSpeechletRequestVerifier timestampVerifier = new TimestampSpeechletRequestVerifier(150, TimeUnit.SECONDS);
      if(!timestampVerifier.verify(request.getRequest(), request.getSession())){
        throw new DerpwizardException("Invalid request.", "Request timestamp sent was outside allowable margins.","Invalid request timestamp." );
      }
      ObjectMapper mapper = new ObjectMapper();
      byte[] serializedSpeechletRequest = mapper.writeValueAsBytes(request.getRequest());
      SpeechletRequestSignatureVerifier.checkRequestSignature(serializedSpeechletRequest, signature, signatureCertChainUrl);
      SpeechletRequestSignatureVerifier.retrieveAndVerifyCertificateChain(signatureCertChainUrl);
    } catch (CertificateException | SecurityException e) {
      throw new DerpwizardException(DerpwizardExceptionReasons.MISSING_INFO.getSsml(), "Speechlet request did not contain valid signature information: " + e.getMessage(),"Invalid signature." );
    } catch (JsonProcessingException e) {
      throw new DerpwizardException("An unknown exception occured.", "Unexpected exception of type: JsonProcessingException.", "Unexpected exception.");
    }
  }

  public static Map<String, String> getMessageAsMap(SpeechletRequest request) {
    if (!(request instanceof IntentRequest)) {
      return Collections.emptyMap();
    }

    IntentRequest intentRequest = (IntentRequest) request;
    Map<String, String> result = new LinkedHashMap<String, String>();
    for (Entry<String, Slot> entry : intentRequest.getIntent().getSlots().entrySet()) {
      Slot slot = entry.getValue();
      if (slot != null) {
        result.put(slot.getName(), slot.getValue());
      }
    }

    return result;
  }
  
  /**
   * An helper function to map Alexa specific "intents" into platform agnostic subjects.
   * @param request
   * @return
   */
  public static String getMessageSubject(SpeechletRequest request) {
    if(request instanceof LaunchRequest){
      return "START_OF_CONVERSATION";
    }else if(request instanceof SessionEndedRequest){
      return "END_OF_CONVERSATION";
    }else if (!(request instanceof IntentRequest)) {
      return "";
    }
    
    IntentRequest intentRequest = (IntentRequest) request;
    String intentRequestName = intentRequest.getIntent().getName();
    switch(intentRequestName){
      case "AMAZON.HelpIntent":
        return "HELP";
      case "AMAZON.CancelIntent":
        return "CANCEL";
      case "AMAZON.StopIntent":
        return "STOP";
      case "AMAZON.RepeatIntent":
        return "REPEAT";
      case "AMAZON.YesIntent":
        return "YES";
      case "AMAZON.NoIntent":
        return "NO";
      case "AMAZON.NextIntent":
        return "NEXT";
      case "AMAZON.PreviousIntent":
        return "PREVIOUS";
      default:
        return intentRequestName;
    }
  }
  
  public static SpeechletResponseEnvelope buildOutput(OutputSpeech outputSpeech, Card card, Reprompt reprompt, boolean shouldEndSession, Map<String,Object> sessionAttributes){
    SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
    
    SpeechletResponse speechletResponse = new SpeechletResponse();

    speechletResponse.setOutputSpeech(outputSpeech);
    speechletResponse.setCard(card);
    speechletResponse.setReprompt(reprompt);
    speechletResponse.setShouldEndSession(shouldEndSession);
    
    responseEnvelope.setResponse(speechletResponse);
    
    responseEnvelope.setSessionAttributes(sessionAttributes);

    return responseEnvelope;
  }
}
