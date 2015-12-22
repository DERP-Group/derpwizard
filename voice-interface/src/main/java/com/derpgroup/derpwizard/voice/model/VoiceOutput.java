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

public class VoiceOutput {
  
  private String plaintext;
  private String ssmltext;
  
  public VoiceOutput(){}
  public VoiceOutput(String text){
    this.plaintext = text;
    this.ssmltext = text;
  }
  public VoiceOutput(String plaintext, String ssmltext){
    this.plaintext = plaintext;
    this.ssmltext = ssmltext;
  }
  
  public String getPlaintext() {return plaintext;}
  public void setPlaintext(String plaintext) {this.plaintext = plaintext;}
  public String getSsmltext() {return ssmltext;}
  public void setSsmltext(String ssmltext) {this.ssmltext = ssmltext;}
}
