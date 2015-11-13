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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.derpgroup.derpwizard.voice.model.CommonMetadata;
import com.derpgroup.derpwizard.voice.model.ConversationHistoryEntry;
import com.derpgroup.derpwizard.voice.model.SsmlDocumentBuilder;
import com.derpgroup.derpwizard.voice.model.VoiceInput;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Base class for message dispatchers.
 *
 * @author Rusty
 * @since 0.0.1
 */
public abstract class AbstractManager {
  
  protected Module mapperModule;

  public AbstractManager(){
    mapperModule = new SimpleModule();
  }
  
  /**
   * Message dispatcher.
   *
   * @param voiceInput
   *          The message to dispatch, not null
   * @param builder
   *          The document builder to append messages to, not null
   */
  public void handleRequest(@NonNull VoiceInput voiceInput, @NonNull SsmlDocumentBuilder builder) {
    appendToConversationHistory(voiceInput);
    switch (voiceInput.getMessageType()) {
      case START_OF_CONVERSATION:
        doHelloRequest(voiceInput, builder);
        break;
      case END_OF_CONVERSATION:
        doGoodbyeRequest(voiceInput, builder);
        break;
      case HELP:
        doHelpRequest(voiceInput, builder);
        break;
      case CANCEL:
        doCancelRequest(voiceInput, builder);
        break;
      case STOP:
        doStopRequest(voiceInput, builder);
        break;
      case DEFAULT:
      default:
        doConversationRequest(voiceInput, builder);
    }
  }

  private void appendToConversationHistory(@NonNull VoiceInput voiceInput) {
    CommonMetadata metadata = voiceInput.getMetadata();
    Deque<ConversationHistoryEntry> conversationHistory = metadata.getConversationHistory();
    if(conversationHistory == null){
      conversationHistory = new ArrayDeque<ConversationHistoryEntry>();
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(mapperModule);
    CommonMetadata metadataClone = mapper.convertValue(metadata, new TypeReference<CommonMetadata>(){});
    
    ConversationHistoryEntry entry = new ConversationHistoryEntry();
    entry.setMessageMap(new LinkedHashMap<String,String>(voiceInput.getMessageAsMap()));
    entry.setMessageSubject(voiceInput.getMessageSubject());
    entry.setMetadata(metadataClone);
    
    conversationHistory.push(entry);
    metadata.setConversationHistory(conversationHistory); //this is only needed in case it was null coming in
  }

  protected abstract void doHelpRequest(VoiceInput voiceInput, SsmlDocumentBuilder builder);

  protected abstract void doHelloRequest(VoiceInput voiceInput, SsmlDocumentBuilder builder);

  protected abstract void doGoodbyeRequest(VoiceInput voiceInput, SsmlDocumentBuilder builder);

  protected abstract void doCancelRequest(VoiceInput voiceInput, SsmlDocumentBuilder builder);

  protected abstract void doStopRequest(VoiceInput voiceInput, SsmlDocumentBuilder builder);

  protected abstract void doConversationRequest(VoiceInput voiceInput, SsmlDocumentBuilder builder);
  
}
