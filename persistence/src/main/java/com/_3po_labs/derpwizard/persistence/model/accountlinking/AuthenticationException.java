package com._3po_labs.derpwizard.persistence.model.accountlinking;

public class AuthenticationException extends Exception{
  private static final long serialVersionUID = 7195441307828380575L;
  private String error;
  
  public AuthenticationException(String error){
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}