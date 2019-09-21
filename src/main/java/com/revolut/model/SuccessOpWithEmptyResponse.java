package com.revolut.model;

public class SuccessOpWithEmptyResponse extends ApplicationOpResponse {
  public SuccessOpWithEmptyResponse(int statusCode) {
    super(statusCode, "", "");
  }
}
