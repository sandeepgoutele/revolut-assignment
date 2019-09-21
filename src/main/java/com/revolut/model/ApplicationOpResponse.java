package com.revolut.model;

public class ApplicationOpResponse {
  private int statusCode;
  private String endpointResponseBody;
  private String errorReason;

  public ApplicationOpResponse(int statusCode, String endpointResponseBody) {
    this.statusCode = statusCode;
    this.endpointResponseBody = endpointResponseBody;
  }

  public ApplicationOpResponse(int statusCode, String endpointResponseBody, String errorReason) {
    this.statusCode = statusCode;
    this.endpointResponseBody = endpointResponseBody;
    this.errorReason = errorReason;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public ApplicationOpResponse setStatusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public String getEndpointResponseBody() {
    return endpointResponseBody;
  }

  public ApplicationOpResponse setEndpointResponseBody(String endpointResponseBody) {
    this.endpointResponseBody = endpointResponseBody;
    return this;
  }

  public String getErrorReason() {
    return errorReason;
  }

  public ApplicationOpResponse setErrorReason(String errorReason) {
    this.errorReason = errorReason;
    return this;
  }
}