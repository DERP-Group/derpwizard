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

import com.derpgroup.derpwizard.voice.model.ServiceOutput;
import com.derpgroup.derpwizard.voice.model.VoiceInput;

/**
 * Manager class for dispatching input messages.
 *
 * @author David
 * @author Eric
 * @author Rusty
 * @author Paul
 * @since 0.0.1
 */
public class DerpWizardManager extends AbstractManager {

  @Override
  protected void doHelpRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) {
    serviceOutput.getVoiceOutput().setSsmltext("I'd love to help, but I don't have any help topics programmed yet.");
    serviceOutput.getVoiceOutput().setPlaintext("I'd love to help, but I don't have any help topics programmed yet.");
    serviceOutput.setConversationEnded(true);
  }

  @Override
  protected void doHelloRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) {
    serviceOutput.getVoiceOutput().setSsmltext("Hi. This is DerpWizard.");
    serviceOutput.getVoiceOutput().setPlaintext("Hi. This is DerpWizard.");
    serviceOutput.setConversationEnded(true);
  }

  @Override
  protected void doGoodbyeRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) {
    serviceOutput.getVoiceOutput().setSsmltext("Goodbye!");
    serviceOutput.getVoiceOutput().setPlaintext("Goodbye!");
    serviceOutput.setConversationEnded(true);
  }

  @Override
  protected void doConversationRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) {
    serviceOutput.getVoiceOutput().setSsmltext("I'd love to help, but I'm not programmed to have conversations yet.");
    serviceOutput.getVoiceOutput().setPlaintext("I'd love to help, but I'm not programmed to have conversations yet.");
    serviceOutput.setConversationEnded(true);
  }

  @Override
  protected void doCancelRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) {
    serviceOutput.setConversationEnded(true);
  }

  @Override
  protected void doStopRequest(VoiceInput voiceInput, ServiceOutput serviceOutput) {
    serviceOutput.setConversationEnded(true);
  }
}
