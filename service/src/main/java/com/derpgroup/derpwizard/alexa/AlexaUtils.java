package com.derpgroup.derpwizard.alexa;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;
import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlexaUtils {

  public static void validateAlexaRequest(SpeechletRequestEnvelope request, String signatureCertChainUrl, String signature) throws CertificateException{
    
    try {
      TimestampSpeechletRequestVerifier timestampVerifier = new TimestampSpeechletRequestVerifier(150, TimeUnit.SECONDS);
      if(!timestampVerifier.verify(request.getRequest(), request.getSession())){
        throw new CertificateException("BAD");  //REPLACE ME WITH OUR REAL EXCEPTION
      }
      ObjectMapper mapper = new ObjectMapper();
      byte[] serializedSpeechletRequest = mapper.writeValueAsBytes(request.getRequest());
      SpeechletRequestSignatureVerifier.checkRequestSignature(serializedSpeechletRequest, signature, signatureCertChainUrl);
      SpeechletRequestSignatureVerifier.retrieveAndVerifyCertificateChain(signatureCertChainUrl);
    } catch (CertificateException e) {
      //Throw this for realzies, once we have a legitimate exception
      e.printStackTrace();
      throw e;
    } catch (JsonProcessingException e) {
      //Throw this for realzies, once we have a legitimate exception
      e.printStackTrace();
    }
  }
}
