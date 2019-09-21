package com.revolut.util;

public interface JsonParser {
  <T> T toJsonPOJO(String jsonString, Class<T> classType);
  String toJSONString(Object data);
}
