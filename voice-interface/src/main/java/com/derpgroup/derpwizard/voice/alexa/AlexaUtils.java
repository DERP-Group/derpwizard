package com.derpgroup.derpwizard.voice.alexa;

import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;
import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;
import com.derpgroup.derpwizard.voice.exception.DerpwizardException;
import com.derpgroup.derpwizard.voice.exception.DerpwizardException.DerpwizardExceptionReasons;
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
}