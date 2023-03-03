package com.github.apatchydev.logbackextra.core.converter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.CoreConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MarkerFactory;
import org.slf4j.event.KeyValuePair;

import static org.assertj.core.api.Assertions.*;

class WrapIfEmptyCompositeConverterTest {
    static WrapIfEmptyCompositeConverter<ILoggingEvent> converter = new WrapIfEmptyCompositeConverter<>();

    @BeforeEach
    void setUp() {
        // Reset option values to default
        converter.prefix = converter.suffix = converter.whenEmpty = CoreConstants.EMPTY_STRING;
    }

    @Test
    void emptyIgnoreEvent() {
        // Given
        LoggingEvent event = new LoggingEvent();
        event.addMarker(MarkerFactory.getMarker("any"));
        event.addKeyValuePair(new KeyValuePair("foo", "bar"));

        // When
        String result = converter.onEmpty(event);

        // Then
        assertThat(result).isEqualTo(CoreConstants.EMPTY_STRING);
    }

    @Test
    void notEmptyIgnoreEvent() {
        // Given
        LoggingEvent event = new LoggingEvent();
        event.addMarker(MarkerFactory.getMarker("any"));
        event.addKeyValuePair(new KeyValuePair("foo", "bar"));

        // When
        String result = converter.onNotEmpty(event, CoreConstants.EMPTY_STRING);

        // Then
        assertThat(result).isEqualTo(CoreConstants.EMPTY_STRING);
    }

    @Test
    void emptyIgnoreOpts() {
        // Given
        converter.prefix = converter.suffix = "foo";

        // When
        String result = converter.onEmpty(null);

        // Then
        assertThat(result).isEqualTo(CoreConstants.EMPTY_STRING);
    }

    @Test
    void notEmptyIgnoreOpt() {
        // Given
        converter.whenEmpty = "bar";

        // When
        String result = converter.onNotEmpty(null, CoreConstants.EMPTY_STRING);

        // Then
        assertThat(result).isEqualTo(CoreConstants.EMPTY_STRING);
    }

    @Test
    void onEmpty() {
        // Given
        String whenEmpty = "was empty";

        converter.whenEmpty = whenEmpty;

        // When
        String result = converter.onEmpty(null);

        // Then
        assertThat(result).isEqualTo(whenEmpty);
    }

    @Test
    void prefix() {
        // Given
        String prefix = "foo-";
        String body = "bar";

        converter.prefix = prefix;

        // When
        String result = converter.onNotEmpty(null, body);

        // Then
        assertThat(result).isEqualTo(prefix + body);
    }

    @Test
    void suffix() {
        // Given
        String suffix = "baz";
        String body = "bar";

        converter.suffix = suffix;

        // When
        String result = converter.onNotEmpty(null, body);

        // Then
        assertThat(result).isEqualTo(body + suffix);
    }

    @Test
    void wrapped() {
        // Given
        String prefix = "foo";
        String suffix = "baz";
        String body = "bar";

        converter.prefix = prefix;
        converter.suffix = suffix;

        // When
        String result = converter.onNotEmpty(null, body);

        // Then
        assertThat(result).isEqualTo(prefix + body + suffix);
    }
}
