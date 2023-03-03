# Logback-Extra

Custom classes for use with [Logback](https://github.com/qos-ch/logback)

## Set up

[![Release](https://jitpack.io/v/aPatchyDev/logback-extra.svg)](https://jitpack.io/#aPatchyDev/logback-extra) Add through the [JitPack](https://jitpack.io/) repo to your project:

###### Gradle build.gradle (Gradle 6.2+)

```groovy
repositories {
    exclusiveContent {
        forRepository { maven { url "https://jitpack.io"} }
        filter { includeGroup "com.github.apatchydev" }
    }
}

dependencies {
    implementation "com.github.apatchydev:logback-extra:1.0.0"
}
```

- Java 11 or higher
    - Java 8 compatible if `build.gradle` is modified to `sourceCompatibility = 8` and use logback 1.3.+

## Features

### Custom pattern layout converters

Define custom conversion rules in `logback.xml` as follows:

```xml
<configuration>
    <conversionRule conversionWord="specifier-without-percent" converterClass="fully.qualified.class.name"/>
</configuration>
```

Code for examples below are available in the [sample program](src/example/java/com/github/apatchydev/logbackextra/ExampleApp.java)
- Execute example program with `./gradlew example`

### JsonKeyValueConverter

A converter for PatternLayout to group all key-value pairs into a JSON object in order of addition,  
falling back to Map::toString if JSON-stringify fails.

Accepts 1 argument `pretty` to enable pretty-printing of JSON object.

#### Example (Override `%kvp` to use `JsonKeyValueConverter`)

```java
pattern = "[%msg | %kvp]%n";
data1 = {"text": "abc",  "object": null};
data2 = {"text" : "def", "object" : [ 1, 2, 3 ]};
        
log.atInfo()
    .addKeyValue("data1", data1)
    .addKeyValue("data2", data2)
    .log("This log writes key-value pairs as JSON");

    log.atInfo()
    .log("This log has no key-value entries");

// ...
    
pattern = "[%msg | %kvp{pretty}]%n";

log.atInfo()
    .addKeyValue("data1", data1)
    .addKeyValue("data2", data2)
    .log("This log writes key-value pairs as JSON with pretty print");

log.atInfo()
    .log("This log has no key-value entries");
```

Result

```
[This log writes key-value pairs as JSON | {"data1":{"text":"abc","object":null},"data2":{"text":"def","object":[1,2,3]}}]
[This log has no key-value entries | ]
// ...
[This log writes key-value pairs as JSON with pretty print | {
  "data1" : {
    "text" : "abc",
    "object" : null
  },
  "data2" : {
    "text" : "def",
    "object" : [ 1, 2, 3 ]
  }
}]
[This log has no key-value entries | ]
```

### WrapIfEmptyCompositeConverter

A composite converter for PatternLayout that adds a prefix/suffix when input is not empty, otherwise put a placeholder.

Arguments are ordered: `{prefix,suffix,placeholder}` with each defaulting to `ch.qos.logback.core.CoreConstants.EMPTY_STRING`

#### Example (Set `%if` to use `WrapIfEmptyCompositeConverter`)

```java
pattern = "[%if(%marker){<,'> '}%msg]%n";

log.atInfo()
    .addMarker(MarkerFactory.getMarker("someMarker"))
    .log("This log prepends the log message with '<marker> ' only if present");

log.atInfo()
    .log("This log has no markers");
```

Result

```
[<someMarker> This log prepends the log message with '<marker> ' only if present]
[This log has no markers]
```
