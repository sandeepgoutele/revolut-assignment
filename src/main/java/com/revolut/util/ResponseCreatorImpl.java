package com.revolut.util;

import com.revolut.model.ApplicationOpResponse;
import spark.Response;

public class ResponseCreatorImpl implements ResponseCreator {
  public String respondToHttpEndpoint(Response response, ApplicationOpResponse applicationOpResponse) {
    response.type("application/json");
    response.status(applicationOpResponse.getStatusCode());

    String messageResponseBodyWhenDataIsEmpty = applicationOpResponse.getErrorReason();
    String messageResponseBodyWhenDataIsPresent = applicationOpResponse.getEndpointResponseBody();


    return (applicationOpResponse.getEndpointResponseBody().isEmpty())
            ? messageResponseBodyWhenDataIsEmpty
            : messageResponseBodyWhenDataIsPresent;
  }
}
