package com.revolut.model;

public class ErrorApplicationOpResponse extends ApplicationOpResponse {
  public ErrorApplicationOpResponse(int statusCode, String errorReason) {
    super(statusCode, "", errorReason);
  }
}
