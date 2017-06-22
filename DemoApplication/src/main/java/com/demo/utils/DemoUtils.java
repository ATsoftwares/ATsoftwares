package com.demo.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DemoUtils {

	public static String decode(String content, String encodeIndicator) {
		URI uri = null;
		try {
			if (content.length() > 0 && !content.contains(encodeIndicator)) {
				uri = new URI(content);
				content = uri.getPath();
			}
		} catch (URISyntaxException e) {
			throw (new RuntimeException(e));
		}
		return content;
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String json, Class<T> entityClass)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		Object entity = mapper.readValue(json, entityClass);

		return (T) entity;

	}
}
