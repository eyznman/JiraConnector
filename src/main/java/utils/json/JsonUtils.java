package utils.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {
	private static ObjectMapper jsonMapper = null;
	private static ObjectMapper jsonMapperIgnoreUnkownProperties = null;

	public static ObjectMapper newJsonMapper() {
		return (new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).findAndRegisterModules()
				.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.disable(SerializationFeature.CLOSE_CLOSEABLE).enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
				.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	}

	public static ObjectMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = newJsonMapper();
		}

		return jsonMapper;
	}

	public static ObjectMapper getIgnoreUnknownPropertiesJsonMapper() {
		if (jsonMapperIgnoreUnkownProperties == null) {
			jsonMapperIgnoreUnkownProperties = newJsonMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		}

		return jsonMapperIgnoreUnkownProperties;
	}

	public static <A, B> B castViaJson(A source, Class<B> destClazz)
			throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
		ObjectMapper mapper = getIgnoreUnknownPropertiesJsonMapper();
		return mapper.readValue(mapper.writeValueAsBytes(source), destClazz);
	}

	@SuppressWarnings("unchecked")
	public static <A> A cloneViaJson(A source) throws Exception {
		ObjectMapper mapper = getIgnoreUnknownPropertiesJsonMapper();
		return (A) mapper.readValue(mapper.writeValueAsBytes(source), source.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <A, B> List<B> castMembersViaJson(Iterable<A> source, Class<B> destClazz)
			throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
		ObjectMapper mapper = getIgnoreUnknownPropertiesJsonMapper();
		@SuppressWarnings("rawtypes")
		ArrayList results = new ArrayList();
		@SuppressWarnings("rawtypes")
		Iterator iterator = source.iterator();

		while (iterator.hasNext()) {
			Object a = iterator.next();
			Object b = mapper.readValue(mapper.writeValueAsBytes(a), destClazz);
			results.add(b);
		}

		return results;
	}
}