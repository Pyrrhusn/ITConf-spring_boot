package utils;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		 String valueAsString = p.getValueAsString();
	        if (valueAsString == null || valueAsString.trim().isEmpty()) {
	            return null;
	        }
	        return LocalDate.parse(valueAsString, InitFormatter.LOCAL_DATE_FORMATTER);
	}

}
