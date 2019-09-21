package com.revolut.util;

import com.revolut.model.ApplicationOpResponse;
import spark.Response;

public interface ResponseCreator {
  String respondToHttpEndpoint(Response response, ApplicationOpResponse applicationOpResponse);
}
