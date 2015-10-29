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

package com.derpgroup.derpwizard.voice.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SpeechletRequest;

class AlexaInput implements VoiceInput {
  private SpeechletRequest request;
  private JSONObject metadata = new JSONObject();

  public AlexaInput(Object object) {
    this(object, null);
  }

  public AlexaInput(Object object, JSONObject metadata) {
    if (!(object instanceof SpeechletRequest)) {
      throw new IllegalArgumentException("Argument is not an instance of SpeechletRequest: " + object);
    }

    request = (SpeechletRequest) object;

    if (metadata != null) {
      this.metadata = metadata;
    }
  }

  @Override
  public String getMessageSubject() {
    if (!(request instanceof IntentRequest)) {
      return "";
    }

    IntentRequest intentRequest = (IntentRequest) request;
    return intentRequest.getIntent().getName();
  }

  @Override
  public Map<String, String> getMessageAsMap() {
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

  @Override
  public MessageType getMessageType() {
    if (request instanceof LaunchRequest) {
      return MessageType.START_OF_CONVERSATION;
    }

    if (request instanceof SessionEndedRequest) {
      return MessageType.END_OF_CONVERSATION;
    }

    // TODO: Check the metadata to see if this is actually a HELP message
    return MessageType.DEFAULT;
  }

  @Override
  public JSONObject getMetadata() {
    return metadata;
  }
}
