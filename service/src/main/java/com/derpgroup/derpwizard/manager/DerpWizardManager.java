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

import com.derpgroup.derpwizard.voice.model.SsmlDocumentBuilder;
import com.derpgroup.derpwizard.voice.model.SsmlDocumentBuilder.EmphasisLevel;
import com.derpgroup.derpwizard.voice.model.VoiceInput;

/**
 * Manager class for dispatching input messages.
 *
 * @author Eric
 * @author Rusty
 * @since 0.0.1
 */
public class DerpWizardManager extends AbstractManager {

  @Override
  protected void doHelpRequest(VoiceInput voiceInput, SsmlDocumentBuilder builder) {
    builder.endParagraph().text("I'd love to help, but I don't have any help topics programmed yet.").endParagraph();
  }

  @Override
  protected void doHelloRequest(VoiceInput voiceInput, SsmlDocumentBuilder builder) {
    builder.endParagraph().text("Hi. This is DerpWizard.").endParagraph();
  }

  @Override
  protected void doGoodbyeRequest(VoiceInput voiceInput, SsmlDocumentBuilder builder) {
    builder.endParagraph().text("Goodbye!", EmphasisLevel.MODERATE).endParagraph();
  }

  @Override
  protected void doConversationRequest(VoiceInput voiceInput, SsmlDocumentBuilder builder) {
    builder.endParagraph().text("I'd love to help, but I'm not programmed to have conversations yet.").endParagraph();
  }
}
