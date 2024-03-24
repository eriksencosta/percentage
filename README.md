# Percentage

[![Codacy code quality](https://app.codacy.com/project/badge/Grade/5feda3d6ceb54ec58806b144bc77f606)](https://app.codacy.com/gh/eriksencosta/percentage/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy code coverage](https://app.codacy.com/project/badge/Coverage/5feda3d6ceb54ec58806b144bc77f606)](https://app.codacy.com/gh/eriksencosta/percentage/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)

Percentage calculations made easy in Kotlin.

## Installation

Add Percentage to your Gradle build script:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.eriksencosta.math:percentage:${percentageVersion}")
}
```

If you're using Maven, add to your POM xml file:

```xml
<dependency>
    <groupId>com.eriksencosta.math</groupId>
    <artifactId>percentage</artifactId>
    <version>${percentageVersion}</version>
</dependency>
```

## Usage

The library provides the `Percentage` type: an immutable and thread-safe class that makes percentage calculations easy.

```kotlin
val percentage = 5.5.percent() // 5.5%

150 * percentage          // 8.25
150 decreaseBy percentage // 141.75
150 increaseBy percentage // 158.25
```

Other convenience functions are also available. To create a percentage based on a ratio, use the `ratioOf` function:

```kotlin
1 ratioOf 4 // 25%
```

To calculate the relative percentage change between two numbers, use the `relativeChange` function: 

```kotlin
1 relativeChange 3 // 200%
```

And to discover the base value of a number when its represents a given percentage, use the `valueWhen` function:

```kotlin
5 valueWhen 20.percent() // 25.0 (i.e., 5 is 20% of 25)
```

Read the [API documentation](https://blog.eriksen.com.br/opensource/math-percentage/) for further details.

## License

[Apache License 2.0](https://choosealicense.com/licenses/apache/)
