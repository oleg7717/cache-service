/*
package ru.interprocom.axioma.cache.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.json.JsonParseException;

import java.io.IOException;

public class BooleanValueDeserializer extends JsonDeserializer<Boolean> {

	@Override
	public Boolean deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
		try {
			JsonToken t = jsonParser.currentToken();
			if (t == JsonToken.VALUE_TRUE || jsonParser.getValueAsString().equals("1")) {
				return true;
			} else if (t == JsonToken.VALUE_FALSE || jsonParser.getValueAsString().equals("0")) {
				return false;
			} else {
				throw new com.fasterxml.jackson.core.JsonParseException(String.format("Current token (%s) not of boolean type", t));
			}
		}catch(JsonParseException exception) {
			throw new RuntimeException(String.format("%s is not a valid Boolean value for %s", jsonParser.getText(), jsonParser.getCurrentName()));
		}
	}
}
*/
