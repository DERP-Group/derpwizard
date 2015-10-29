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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;

@RunWith(MockitoJUnitRunner.class)
public class AlexaInputTest {

  @Mock IntentRequest intentRequest;
  Intent intent;
  
  @Before
  public void setup(){
    intent = Intent.builder().withName("TESTVALUE").withSlots(Collections.emptyMap()).build();
    when(intentRequest.getIntent()).thenReturn(intent);
  }

  @Test
  public void constructorLaunchRequest() {
    new AlexaInput(LaunchRequest.builder().withRequestId("123").build());
  }

  @Test
  public void constructorIntentRequest() {
    new AlexaInput(IntentRequest.builder().withRequestId("123").build());
  }

  @Test
  public void constructorSessionEndedRequest() {
    new AlexaInput(SessionEndedRequest.builder().withRequestId("123").build());
  }

  @Test
  public void getMessageAsMapSuccessFullSlots(){
    Map<String, Slot> slots = new LinkedHashMap<String,Slot>();
    slots.put("foo",Slot.builder().withName("foo").withValue("fooValue").build());
    slots.put("bar",Slot.builder().withName("bar").withValue("barValue").build());
    intent = Intent.builder().withName("TESTVALUE").withSlots(slots).build();

    when(intentRequest.getIntent()).thenReturn(intent);

    VoiceInput vi = new AlexaInput(intentRequest);
    assertNotNull(vi.getMessageAsMap());
    assertEquals(slots.size(), vi.getMessageAsMap().size());
  }

  @Test
  public void getMessageAsMapSuccessNullSlots(){
    Map<String, Slot> slots = new LinkedHashMap<String,Slot>();
    slots.put("foo",Slot.builder().withName("foo").withValue("fooValue").build());
    slots.put("bar",Slot.builder().withName("bar").withValue("barValue").build());
    slots.put("nullSlot", null);
    intent = Intent.builder().withName("TESTVALUE").withSlots(slots).build();

    when(intentRequest.getIntent()).thenReturn(intent);

    VoiceInput vi = new AlexaInput(intentRequest);
    assertNotNull(vi.getMessageAsMap());
    assertEquals(slots.size() -1, vi.getMessageAsMap().size());
  }

  @Test
  public void constructorWithMetadata(){
    JSONObject metadata = new JSONObject();
    metadata.put("foo", "fooValue");
    metadata.put("bar", 123);

    VoiceInput vi = new AlexaInput(intentRequest, metadata);
    assertNotNull(vi.getMetadata());
    assertNotNull(vi.getMetadata().get("foo"));
    assertNotNull(vi.getMetadata().get("bar"));
    assertEquals(vi.getMetadata().get("foo"),metadata.get("foo"));
    assertEquals(vi.getMetadata().get("bar"),metadata.get("bar"));
  }

  @Test
  public void constructorNoMetadata(){    
    VoiceInput vi = new AlexaInput(intentRequest);
    assertNotNull(vi.getMetadata());

    vi = new AlexaInput(intentRequest, null);
    assertNotNull(vi.getMetadata());    
  }
}
