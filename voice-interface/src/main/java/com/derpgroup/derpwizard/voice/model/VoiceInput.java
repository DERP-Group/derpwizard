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

import java.util.Map;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Speech-to-text message.
 *
 * @author Rusty
 * @since 0.0.1
 */
public interface VoiceInput {
  /**
   * Enumeration of types of message intents.
   *
   * @author Rusty
   * @since 0.0.1
   */
  public enum MessageType {
    /**
     * User has initiated a new conversation
     */
    START_OF_CONVERSATION,
    /**
     * User has ended the current conversation
     */
    END_OF_CONVERSATION,
    /**
     * User has asked for help instructions for the system
     */
    HELP,
    /**
     * User has sent a normal message to the system
     */
    DEFAULT
  }

  /**
   * Parse the voice input as plain text.
   *
   * @return The message in plain text, never null
   */
  @NonNull String getMessageAsString();
  
  /**
   * Parse the voice input as a series of K-V pairs.
   *
   * @return A map of parameters, never null
   */
  @NonNull Map<String, String> getMessageAsMap();

  /**
   * Get all pieces of metadata associated with the request
   * 
   * @return The associated metadata, never null
   */
  @NonNull Map<String, Object> getMetadata();

  /**
   * 
   *
   * @return The intent of the message, never null
   */
  @NonNull MessageType getMessageType();
}
