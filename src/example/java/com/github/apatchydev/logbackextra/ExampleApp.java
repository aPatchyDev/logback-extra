package com.github.apatchydev.logbackextra;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.CoreConstants;
import com.github.apatchydev.logbackextra.classic.converter.JsonKeyValueConverter;
import com.github.apatchydev.logbackextra.core.converter.WrapIfEmptyCompositeConverter;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import java.util.HashMap;
import java.util.Map;

public class ExampleApp {
    private static final Logger log = (Logger) LoggerFactory.getLogger(ExampleApp.class);

    public static void main(String[] args) {
        plainJson();

        System.out.println("\n\n");

        prettyJson();

        System.out.println("\n\n");

        cleanMarkerPrefix();

        System.out.println("\n\n");

        prettyJsonNextLine();
    }

    private static void plainJson() {
        ExampleData data1 = new ExampleData("abc", null);
        ExampleData data2 = new ExampleData("def", new int[]{1, 2, 3});

        Map<String, String> userConfig = new HashMap<>();
        userConfig.put("kvp", JsonKeyValueConverter.class.getName());

        userConfig.forEach((k, v) -> System.out.println("%" + k + " -> " + v));
        configureLoggerPattern("[%msg | %kvp]%n", userConfig);

        log.atInfo()
                .addKeyValue("data1", data1)
                .addKeyValue("data2", data2)
                .log("This log writes key-value pairs as JSON");

        log.atInfo()
                .log("This log has no key-value entries");
    }

    private static void prettyJson() {
        ExampleData data1 = new ExampleData("abc", null);
        ExampleData data2 = new ExampleData("def", new int[]{1, 2, 3});

        Map<String, String> userConfig = new HashMap<>();
        userConfig.put("kvp", JsonKeyValueConverter.class.getName());

        userConfig.forEach((k, v) -> System.out.println("%" + k + " -> " + v));
        configureLoggerPattern("[%msg | %kvp{pretty}]%n", userConfig);

        log.atInfo()
                .addKeyValue("data1", data1)
                .addKeyValue("data2", data2)
                .log("This log writes key-value pairs as JSON with pretty print");

        log.atInfo()
                .log("This log has no key-value entries");
    }

    private static void cleanMarkerPrefix() {
        Map<String, String> userConfig = new HashMap<>();
        userConfig.put("if", WrapIfEmptyCompositeConverter.class.getName());

        userConfig.forEach((k, v) -> System.out.println("%" + k + " -> " + v));
        configureLoggerPattern("[%if(%marker){<,'> '}%msg]%n", userConfig);

        log.atInfo()
                .addMarker(MarkerFactory.getMarker("someMarker"))
                .log("This log prepends the log message with '<marker> ' only if present");

        log.atInfo()
                .log("This log has no markers");
    }

    private static void prettyJsonNextLine() {
        ExampleData data1 = new ExampleData("abc", null);
        ExampleData data2 = new ExampleData("def", new int[]{1, 2, 3});

        Map<String, String> userConfig = new HashMap<>();
        userConfig.put("kvp", JsonKeyValueConverter.class.getName());
        userConfig.put("if", WrapIfEmptyCompositeConverter.class.getName());

        userConfig.forEach((k, v) -> System.out.println("%" + k + " -> " + v));
        configureLoggerPattern("[%if(%marker){<,'> '}%if(%kvp{pretty}){'\n','\n'}%msg]%n", userConfig);

        log.atInfo()
                .addMarker(MarkerFactory.getMarker("someMarker"))
                .addKeyValue("data1", data1)
                .addKeyValue("data2", data2)
                .log("This log has a marker and pretty JSON, appending this message at the end");

        log.atInfo()
                .addMarker(MarkerFactory.getMarker("someMarker"))
                .log("This log has no key-value pairs");

        log.atInfo()
                .addKeyValue("data1", data1)
                .addKeyValue("data2", data2)
                .log("This log has no markers");

        log.atInfo()
                .log("This log has only a message");
    }

    // Programmatically configure logback

    private static void configureLoggerPattern(String pattern, Map<String, String> converterUserConfig) {
        LoggerContext lc = log.getLoggerContext();
        Map<String, String> converterConfig = getConverterConfig(converterUserConfig);
        lc.reset();
        lc.putObject(CoreConstants.PATTERN_RULE_REGISTRY, converterConfig);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(lc);
        encoder.setPattern(pattern);
        encoder.setOutputPatternAsHeader(true);
        encoder.start();

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(lc);
        appender.setEncoder(encoder);
        appender.start();

        log.addAppender(appender);
        log.setLevel(Level.INFO);
    }

    private static Map<String, String> getConverterConfig(Map<String, String> userConfig) {
        LoggerContext lc = log.getLoggerContext();
        @SuppressWarnings("unchecked")
        Map<String, String> defaultConverters = (Map<String, String>) lc.getObject(CoreConstants.PATTERN_RULE_REGISTRY);
        if (defaultConverters == null || defaultConverters.isEmpty())
            return userConfig;

        Map<String, String> result = new HashMap<>();
        result.putAll(defaultConverters);
        result.putAll(userConfig);
        return result;
    }
}
