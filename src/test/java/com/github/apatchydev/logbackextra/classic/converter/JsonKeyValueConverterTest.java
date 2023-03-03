package com.github.apatchydev.logbackextra.classic.converter;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.CoreConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MarkerFactory;
import org.slf4j.event.KeyValuePair;

import static org.assertj.core.api.Assertions.*;

class JsonKeyValueConverterTest {
    static JsonKeyValueConverter converter = new JsonKeyValueConverter();

    LoggingEvent event;

    @BeforeEach
    void setUp() {
        event = new LoggingEvent();
    }

    String wrap(String s) {
        return '"' + s + '"';
    }

    @Test
    void empty() {
        // Given
        // no key-value pair added to event
        event.addMarker(MarkerFactory.getMarker("any"));

        // When
        String result = converter.convert(event);

        // Then
        assertThat(result).isEqualTo(CoreConstants.EMPTY_STRING);
    }

    @Test
    void uglyOne() {
        // Given
        String key = "exampleKey";
        String val = "exampleValue";

        event.addKeyValuePair(new KeyValuePair(key, val));

        converter.setPretty(false);

        // When
        String result = converter.convert(event);

        // Then
        assertThat(result).isEqualTo(String.format("{%s:%s}", wrap(key), wrap(val)));
    }

    @Test
    void prettyOne() {
        // Given
        String key = "exampleKey";
        String val = "exampleValue";

        event.addKeyValuePair(new KeyValuePair(key, val));

        converter.setPretty(true);

        // When
        String result = converter.convert(event);

        // Then
        assertThat(result).isEqualTo(String.format("{\n  %s : %s\n}", wrap(key), wrap(val)).replace("\n", CoreConstants.LINE_SEPARATOR));
    }

    @Test
    void uglyTwo() {
        // Given
        String key1 = "key1";
        String val1 = "value1";
        String key2 = "key2";
        String val2 = "value2";

        event.addKeyValuePair(new KeyValuePair(key1, val1));
        event.addKeyValuePair(new KeyValuePair(key2, val2));

        converter.setPretty(false);

        // When
        String result = converter.convert(event);

        // Then
        assertThat(result).isEqualTo(String.format("{%s:%s,%s:%s}",
                wrap(key1), wrap(val1),
                wrap(key2), wrap(val2)));
    }

    @Test
    void prettyTwo() {
        // Given
        String key1 = "key1";
        String val1 = "value1";
        String key2 = "key2";
        String val2 = "value2";

        event.addKeyValuePair(new KeyValuePair(key1, val1));
        event.addKeyValuePair(new KeyValuePair(key2, val2));

        converter.setPretty(true);

        // When
        String result = converter.convert(event);

        // Then
        assertThat(result).isEqualTo(String.format("{\n  %s : %s,\n  %s : %s\n}",
                wrap(key1), wrap(val1),
                wrap(key2), wrap(val2))
                .replace("\n", CoreConstants.LINE_SEPARATOR));
    }
}
