/**
 * Copyright (C) 2015 David Phillips
 * Copyright (C) 2015 Eric Olson
 * Copyright (C) 2015 Rusty Gerard
 * Copyright (C) 2015 Paul Winters
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.derpgroup.derpwizard.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.dropwizard.setup.Environment;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.SimpleCard;
import com.derpgroup.derpwizard.configuration.MainConfig;
import com.derpgroup.derpwizard.manager.DerpWizardManager;
import com.derpgroup.derpwizard.voice.model.SsmlDocumentBuilder;
import com.derpgroup.derpwizard.voice.model.VoiceInput;
import com.derpgroup.derpwizard.voice.model.VoiceInput.MessageType;
import com.derpgroup.derpwizard.voice.model.VoiceMessageFactory;
import com.derpgroup.derpwizard.voice.model.VoiceMessageFactory.InterfaceType;
import com.derpgroup.derpwizard.voice.model.VoiceOutput;

/**
 * REST APIs for requests generating from Amazon Alexa
 *
 * @author Eric
 * @author Rusty
 * @since 0.0.1
 */
@Path("/alexa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlexaResource {

  private static final List<String> UNSUPPORTED_SSML_TAGS = Collections.unmodifiableList(Arrays.asList(
      "emphasis"
      ));

  private DerpWizardManager manager;

  public AlexaResource(MainConfig config, Environment env) {
    manager = new DerpWizardManager();
  }

  /**
   * Generates a welcome message.
   *
   * @return The message, never null
   * @throws IOException 
   */
  @POST
  public SpeechletResponseEnvelope doAlexaRequest(@NotNull @Valid SpeechletRequestEnvelope request) throws IOException{
    if (request.getRequest() == null) {
      throw new RuntimeException("Missing request body."); //TODO: create AlexaException
    }

    SsmlDocumentBuilder builder = new SsmlDocumentBuilder(UNSUPPORTED_SSML_TAGS);
    VoiceInput voiceInput = VoiceMessageFactory.buildInputMessage(request.getRequest(), InterfaceType.ALEXA);
    manager.handleRequest(voiceInput, builder);

    SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
    responseEnvelope.setSessionAttributes(request.getSession().getAttributes());

    // Create a VoiceOutput object with the SSML content generated by the manater
    if ((voiceInput.getMessageType() != MessageType.END_OF_CONVERSATION) && (voiceInput.getMessageType() != MessageType.STOP)) {
      SimpleCard card = new SimpleCard();
      card.setContent(builder.getRawText());
      card.setTitle("Alexa + DERP Wizard");

      @SuppressWarnings("unchecked")
      VoiceOutput<SpeechletResponse> voiceOutput = (VoiceOutput<SpeechletResponse>) VoiceMessageFactory.buildOutputMessage(builder.build(), InterfaceType.ALEXA);
      SpeechletResponse speechletResponse = (SpeechletResponse) voiceOutput.getImplInstance();
      speechletResponse.setCard(card);

      responseEnvelope.setResponse(speechletResponse);
    }

    return responseEnvelope;
  }
}
