package com.revolut.util;

import com.google.gson.Gson;

public class JsonParserImpl implements JsonParser {
  public <T> T toJsonPOJO(String jsonString, Class<T> classType) {
    return new Gson().fromJson(jsonString, classType);
  }

  public String toJSONString(Object data){
    return new Gson().toJson(data);
  }
}
