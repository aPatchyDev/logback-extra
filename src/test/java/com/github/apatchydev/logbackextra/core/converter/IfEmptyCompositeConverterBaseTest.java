package com.github.apatchydev.logbackextra.core.converter;

import ch.qos.logback.core.CoreConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class IfEmptyCompositeConverterBaseTest {
    static class IfEmptyCompositeConverterImpl<E> extends IfEmptyCompositeConverterBase<E> {
        static String EMPTY = "E";
        static String NOT_EMPTY = "NE";

        @Override
        protected void processOptions() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected String onEmpty(E event) {
            return EMPTY;
        }

        @Override
        protected String onNotEmpty(E event, String in) {
            return NOT_EMPTY;
        }
    }

    static IfEmptyCompositeConverterImpl<?> converter = new IfEmptyCompositeConverterImpl<>();

    @Test
    void onEmpty() {
        // Given
        String input = CoreConstants.EMPTY_STRING;

        // When
        String result = converter.transform(null, input);

        // Then
        assertThat(result).isEqualTo(IfEmptyCompositeConverterImpl.EMPTY);
    }

    @Test
    void onNotEmpty() {
        // Given
        String input = "foobar";

        // When
        String result = converter.transform(null, input);

        // Then
        assertThat(result).isEqualTo(IfEmptyCompositeConverterImpl.NOT_EMPTY);
    }
}
