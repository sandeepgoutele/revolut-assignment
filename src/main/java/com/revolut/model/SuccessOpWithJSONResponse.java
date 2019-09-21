package com.revolut.model;

public class SuccessOpWithJSONResponse extends ApplicationOpResponse {
  public SuccessOpWithJSONResponse(int statusCode, String endpointResponseBody) {
    super(statusCode, endpointResponseBody, "");
  }
}
