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
