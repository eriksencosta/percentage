# Percentage library

Percentage is a library that introduces the `Percentage` type: a class make percentage calculations in Kotlin easier.

Let's say you want to calculate a 5% discount of a value. In vanilla Kotlin, you would do:

```kotlin
val value = 100
val percentage = 5 / 100.0
val discountedValue = value - value * percentage

println(discountedValue) // Prints: 95.0
```

With `Percentage`, you just do:

```kotlin
val value = 100
val percentage = 5.percent()
val discountedValue = value - percentage

println(discountedValue) // Prints: 95.0
```

You may also simply multiply a value by a `Percentage` to calculate its result:

```kotlin
val result = 100 * Percentage(10)

println(result) // Prints: 10.0
```

## Other calculations

### Percentage change

```kotlin
val initial = 33
val ending = 77
val change = initial changeOf ending

println(change) // Prints: 133%
```

## Percentage calculation

```kotlin
val percentage = 4 ratioOf 5

println(percentage) // Prints: 80%
```

## The number represented by a percentage

```kotlin
val value = 5
val percentage = 20.percent()
val result = value numberOf percentage

println(result) // Prints: 25.0
                // 5 is 20% of 25
```

## Development setup

This project uses JDK 18 for development. You IDE of choice may use Gradle for building and executing the project code.

## Running locally

You need Gradle 8.6+ to run the project locally and Java 18. If you don't have them, you may use Docker to build the
project and check if its working properly:

```bash
docker run --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle gradle build
```

## TODO

- Decide the library name
- Decide if the `Number` extension methods should throw their own exceptions instead of just throwing `Percentage`'s
  exceptions
- Review scale and precision on `Percentage`
- Review method names: `ratioOf`, `rangeOf`, and `numberOf`
- Configure static code analysis (Sonacloud, Codacy, CodeRabbit, Snyk, detekt?)

## Disclaimer

This is WIP, published for close friends reviews only.