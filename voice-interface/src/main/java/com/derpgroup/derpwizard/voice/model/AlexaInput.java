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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;

class AlexaInput implements VoiceInput {
  private IntentRequest request;
  private Map<String,Object> metadata;
  
  public AlexaInput(Object object) {
    this(object, null);
  }

  @SuppressWarnings("unchecked")
  public AlexaInput(Object request, Object metadata) {
    if (!(request instanceof IntentRequest)) {
      throw new IllegalArgumentException("First argument is not an instance of IntentRequest: " + request);
    }

    this.request = (IntentRequest) request;
    
    if(metadata != null){
      if (!(metadata instanceof Map<?,?>)) {
        throw new IllegalArgumentException("Second argument is not an instance of Map<?,?>: " + metadata);
      }
      this.metadata = (Map<String, Object>) metadata;
    }
  }

  @Override
  public String getMessage() {
    if (request.getIntent().getSlots() == null) {
      return request.getIntent().getName();
    }

    StringBuilder buffer = new StringBuilder(request.getIntent().getName());
    for (Entry<String, Slot> entry : request.getIntent().getSlots().entrySet()) {
      buffer.append(' ');
      buffer.append(entry.getValue().getValue());
    }

    return buffer.toString();
  }

  @Override
  public Map<String, String> getParameters() {
    Map<String,String> slots = new HashMap<String,String>();
    Intent intent = request.getIntent();
    if(intent.getSlots() == null){
      return slots;
    }
    
    for(Entry<String,Slot> entry : intent.getSlots().entrySet()){
      slots.put(entry.getKey(), entry.getValue().getValue());
    }
    
    return slots;
  }

  //TODO: We should put some code in here to map built in AMAZON intents to other request types
  @Override
  public <E extends Enum<E>> E getRequestName(Class<E> enumClass) {
    return (E)(Enum.valueOf(enumClass, request.getIntent().getName()));
  }

  @Override
  public Map<String, Object> getMetadata() {
    return metadata;
  }
}
