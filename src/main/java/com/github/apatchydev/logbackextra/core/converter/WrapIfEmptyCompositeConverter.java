package com.github.apatchydev.logbackextra.core.converter;

import ch.qos.logback.core.CoreConstants;

import java.util.List;

/**
 * A composite converter that prepends / appends a string to the input, or a placeholder
 *
 * The first option specifies the prefix string to be added when input is not empty
 * The second option specifies the suffix string to be added when input is not empty
 * The third option specifies the placeholder string to be used when input is empty
 */
public class WrapIfEmptyCompositeConverter<E> extends IfEmptyCompositeConverterBase<E> {
    protected String prefix;
    protected String suffix;
    protected String whenEmpty;

    @Override
    protected void processOptions() {
        // Default
        prefix = suffix = whenEmpty = CoreConstants.EMPTY_STRING;

        List<String> options = getOptionList();
        int optLength;
        if (options == null || (optLength = options.size()) == 0) {
            return;
        }

        switch (optLength) {
            default:    // 3 or greater
            case 3:
                whenEmpty = options.get(2);
                // fallthrough
            case 2:
                suffix = options.get(1);
                //fallthrough
            case 1:
                prefix = options.get(0);
        }
    }

    @Override
    protected String onEmpty(E event) {
        return whenEmpty;
    }

    @Override
    protected String onNotEmpty(E event, String in) {
        return prefix + in + suffix;
    }
}
