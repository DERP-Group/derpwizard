package com.derpgroup.derpwizard.voice.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;

@RunWith(MockitoJUnitRunner.class)
public class AlexaIntentInputTest {

  @Mock IntentRequest intentRequest;
  Intent intent;
  
  @Before
  public void setup(){
    intent = Intent.builder().withName("TESTVALUE").withSlots(Collections.emptyMap()).build();
    when(intentRequest.getIntent()).thenReturn(intent);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBadRequestType_intent(){
    new AlexaInput(LaunchRequest.builder().withRequestId("123").build());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBadRequestType_metadata(){
    new AlexaInput(IntentRequest.builder().withRequestId("123").build(),Collections.emptyList());
  }
  
  @Test
  public void testGetRequestName(){

    VoiceInput vi = new AlexaInput(intentRequest);
    assertEquals(AlexaIntentEnum.TESTVALUE,vi.getRequestName(AlexaIntentEnum.class));
  }
  
  @Test
  public void testGetParameters(){
    Map<String, Slot> slots = new LinkedHashMap<String,Slot>();
    slots.put("foo",Slot.builder().withName("foo").withValue("fooValue").build());
    slots.put("bar",Slot.builder().withName("bar").withValue("barValue").build());
    intent = Intent.builder().withName("TESTVALUE").withSlots(slots).build();

    when(intentRequest.getIntent()).thenReturn(intent);
    
    VoiceInput vi = new AlexaInput(intentRequest);
    assertNotNull(vi.getParameters());
    assertEquals(slots.size(), vi.getParameters().size());
  }
  
  @Test
  public void testGetMetadata(){
    Map<String, Object> metadata = new LinkedHashMap<String, Object>();
    metadata.put("foo", "fooValue");
    metadata.put("bar", new Integer(123));
    
    VoiceInput vi = new AlexaInput(intentRequest, metadata);
    assertNotNull(vi.getMetadata());
    assertNotNull(vi.getMetadata().get("foo"));
    assertNotNull(vi.getMetadata().get("bar"));
    assertEquals(vi.getMetadata().get("foo"),metadata.get("foo"));
    assertEquals(vi.getMetadata().get("bar"),metadata.get("bar"));
  }
  
  @Test
  public void testGetMetadata_null(){    
    VoiceInput vi = new AlexaInput(intentRequest);
    assertNull(vi.getMetadata());
    
    vi = new AlexaInput(intentRequest, null);
    assertNull(vi.getMetadata());    
  }
  
  public enum AlexaIntentEnum{
    TESTVALUE
  }
}
