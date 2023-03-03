package com.github.apatchydev.logbackextra.core.converter;

import ch.qos.logback.core.pattern.CompositeConverter;

/**
 * A composite converter that performs transformation differently based on whether the input is empty
 */
public abstract class IfEmptyCompositeConverterBase<E> extends CompositeConverter<E> {
    @Override
    public void start() {
        super.start();
        processOptions();
    }

    protected abstract void processOptions();

    @Override
    protected String transform(E event, String in) {
        if (in == null || in.isEmpty())
            return onEmpty(event);

        return onNotEmpty(event, in);
    }

    protected abstract String onEmpty(E event);
    protected abstract String onNotEmpty(E event, String in);
}
