package com.github.apatchydev.logbackextra.classic.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.event.KeyValuePair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonKeyValueConverter extends ClassicConverter {
    public static final String PRETTY_JSON = "pretty";

    public final ObjectMapper objMapper;
    protected boolean isPretty;

    public JsonKeyValueConverter() {
        this.objMapper = new ObjectMapper();
    }

    @Override
    public void start() {
        super.start();
        isPretty = PRETTY_JSON.equalsIgnoreCase(getFirstOption());
        setPretty(isPretty);
    }

    public void setPretty(boolean isPretty) {
        if (isPretty)
            objMapper.enable(SerializationFeature.INDENT_OUTPUT);
        else
            objMapper.disable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public String convert(ILoggingEvent event) {
        List<KeyValuePair> entries = event.getKeyValuePairs();
        if (entries == null || entries.isEmpty())
            return CoreConstants.EMPTY_STRING;

        int mapSize = entries.size() * 4 / 3;
        Map<String, Object> data = new LinkedHashMap<>(mapSize + 1);
        for (KeyValuePair pair : entries) {
            data.put(pair.key, pair.value);
        }

        try {
            return objMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            addError("JsonFormatter failed. Defaulting to Map::toString.");
            return data.toString();
        }
    }
}
