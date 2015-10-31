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

import java.util.Arrays;

import org.junit.Test;
import org.junit.Before;

import com.derpgroup.derpwizard.voice.model.SsmlDocumentBuilder.BreakStrength;
import com.derpgroup.derpwizard.voice.model.SsmlDocumentBuilder.EmphasisLevel;

public class SsmlDocumentBuilderTest {
  SsmlDocumentBuilder builder;

  @Before
  public void before() throws Exception {
    builder = new SsmlDocumentBuilder();
  }

  @Test
  public void buildSentence() throws Exception {
    String text = "The quick brown fox jumps over the lazy dog";
    String expected = "<speak><p><s>" + text + "</s></p></speak>";

    SsmlDocument doc = builder.text(text).build();

    assertEquals(expected, doc.getSsml());
  }

  @Test
  public void buildSentenceWithEmphasis() throws Exception {
    SsmlDocument doc = builder.text("w1 ").text("w2", EmphasisLevel.STRONG).text(" w3").build();

    assertEquals("<speak><p><s>w1 <emphasis level=\"strong\">w2</emphasis> w3</s></p></speak>", doc.getSsml());
  }

  @Test
  public void buildSentenceWithBreaks() throws Exception {
    SsmlDocument doc = builder.text("w1 ").pause().text(" w2 ").pause(BreakStrength.STRONG).text(" w3 ").pause("+.1s").text(" w4 ").pause("BOGUS").build();

    assertEquals("<speak><p><s>w1 <break /> w2 <break strength=\"strong\"/> w3 <break time=\"+.1s\"/> w4 <break/></s></p></speak>", doc.getSsml());
  }

  @Test
  public void buildMultipleSentences() throws Exception {
    SsmlDocument doc = builder.text("Sentence 1").endSentence().text("Sentence 2").endSentence().text("Sentence 3").build();

    assertEquals("<speak><p><s>Sentence 1</s><s>Sentence 2</s><s>Sentence 3</s></p></speak>", doc.getSsml());
  }

  @Test
  public void buildMultipleParagraphs() throws Exception {
    SsmlDocument doc = builder.endParagraph().text("Paragraph 1a").endSentence().text("Paragraph 1b").endParagraph().text("Paragraph 2").endParagraph().text("Paragraph 3").build();

    assertEquals("<speak><p><s>Paragraph 1a</s><s>Paragraph 1b</s></p><p><s>Paragraph 2</s></p><p><s>Paragraph 3</s></p></speak>", doc.getSsml());
  }

  @Test
  public void ignoreSpeakTag() throws Exception {
    builder = new SsmlDocumentBuilder(Arrays.asList("speak"));

    SsmlDocument doc = builder.text("Hello, world!").build();

    assertEquals("<p><s>Hello, world!</s></p>", doc.getSsml());
  }

  @Test
  public void ignorePTag() throws Exception {
    builder = new SsmlDocumentBuilder(Arrays.asList("p"));

    SsmlDocument doc = builder.endParagraph().text("Line 1").endParagraph().text("Line 2").endParagraph().build();

    assertEquals("<speak><s>Line 1</s><s>Line 2</s></speak>", doc.getSsml());
  }

  @Test
  public void ignoreEmphasisTag() throws Exception {
    builder = new SsmlDocumentBuilder(Arrays.asList("emphasis"));

    SsmlDocument doc = builder.text("Hello, world!", EmphasisLevel.STRONG).build();

    assertEquals("<speak><p><s>Hello, world!</s></p></speak>", doc.getSsml());
  }

  @Test
  public void ignoreBreakTag() throws Exception {
    builder = new SsmlDocumentBuilder(Arrays.asList("break"));

    SsmlDocument doc = builder.text("Hello,").pause(BreakStrength.MEDIUM).text(" world.").build();

    assertEquals("<speak><p><s>Hello, world.</s></p></speak>", doc.getSsml());
  }

  @Test
  public void getRawTextSuccess() throws Exception {
    String result = builder.text("w1 ").text("w2", EmphasisLevel.STRONG).text(" w3").pause().text(" w4").pause(BreakStrength.STRONG).pause("500ms").getRawText();

    assertEquals("w1 w2 w3 w4", result);
  }
}
