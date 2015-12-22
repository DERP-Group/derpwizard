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

package com.derpgroup.derpwizard.manager;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.derpgroup.derpwizard.voice.exception.DerpwizardException;
import com.derpgroup.derpwizard.voice.model.CommonMetadata;
import com.derpgroup.derpwizard.voice.model.ServiceOutput;
import com.derpgroup.derpwizard.voice.model.VoiceInput;
import com.derpgroup.derpwizard.voice.util.ConversationHistoryUtils;

/**
 * Base class for message dispatchers.
 *
 * @author Rusty
 * @since 0.0.1
 */
public abstract class AbstractManager {

  public AbstractManager(){}
  
  /**
   * Message dispatcher.
   *
   * @param voiceInput
   *          The message to dispatch, not null
   * @param builder
   *          The document builder to append messages to, not null
   * @throws DerpwizardException 
   */
  public void handleRequest(@NonNull VoiceInput voiceInput, @NonNull ServiceOutput serviceOutput) throws DerpwizardException {
    CommonMetadata metadata = serviceOutput.getMetadata();
    ConversationHistoryUtils.registerRequestInConversationHistory(voiceInput.getMessageSubject(), voiceInput.getMessageAsMap(), metadata, metadata.getConversationHistory());
    switch (voiceInput.getMessageType()) {
      case START_OF_CONVERSATION:
        doHelloRequest(voiceInput, serviceOutput);
        break;
      case END_OF_CONVERSATION:
        doGoodbyeRequest(voiceInput, serviceOutput);
        break;
      case HELP:
        doHelpRequest(voiceInput, serviceOutput);
        break;
      case CANCEL:
        doCancelRequest(voiceInput, serviceOutput);
        break;
      case STOP:
        doStopRequest(voiceInput, serviceOutput);
        break;
      case DEFAULT:
      default:
        doConversationRequest(voiceInput, serviceOutput);
    }
  }

  

  protected abstract void doHelpRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) throws DerpwizardException;

  protected abstract void doHelloRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) throws DerpwizardException;

  protected abstract void doGoodbyeRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) throws DerpwizardException;

  protected abstract void doCancelRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) throws DerpwizardException;

  protected abstract void doStopRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) throws DerpwizardException;

  protected abstract void doConversationRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) throws DerpwizardException;
  
}
