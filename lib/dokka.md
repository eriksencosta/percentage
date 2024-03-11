# Module Percentage

The Posostos library makes percentage calculations easy:

    150 * 5.5.percent()          // 8.25
    150 decreaseBy 5.5.percent() // 141.75
    150 increaseBy 5.5.percent() // 158.25

Under the hood, all calculations are done by the immutable and thread-safe
[Percentage][com.eriksencosta.percentage.Percentage] class. You can always query for the percentage's original value,
and it's decimal representation (i.e., its value divided by 100):

    val percentage = 5.5.percent()
    percentage.decimal // 0.055
    percentage.value   // 5.5

## Rounding

If you need to round your percentage value and its calculations, just pass an instance of the
[Rounding][com.eriksencosta.percentage.Rounding] class to the [percent][com.eriksencosta.percentage.Number.percent]
method. Use the [Rounding.to][com.eriksencosta.percentage.Rounding.to] factory method to create the object, passing the
number of decimal places and the rounding mode desired to round for:

    val percentage = (615.0 / 53.0).percent()
    percentage.decimal // 0.1160377358490566
    percentage.value   // 11.60377358490566

    val roundedOff = (615.0 / 53.0).percent(Rounding.to(2, RoundingMode.FLOOR))
    roundedOff.decimal // 0.11
    roundedOff.value   // 11.60377358490566

    val value = 127
    value * percentage // 14.736792452830189
    value * roundedOff // 13.97

The rounding mode to use is defined by one of [RoundingMode][java.math.RoundingMode] enum values. If you need to use
[HALF_UP][java.math.RoundingMode], just pass the number of desired decimal places:

    val roundedUp = (615.0 / 53.0).percent(2)
    roundedUp.decimal  // 0.12
    roundedUp.value    // 11.60377358490566

    value * roundedUp  // 15.24

Note that [Percentage.value][com.eriksencosta.percentage.Percentage.value] is never rounded. This way you can query the
original value of the percentage.

## Other utilities

### Create a Percentage based on a ratio

To create a [Percentage][com.eriksencosta.percentage.Percentage] based on a ratio (e.g. 1/2, 1/3, 1/4, and so on), use
the [percent][com.eriksencosta.percentage.Number.ratioOf] function:

    1 ratioOf 4 // 25%
    1 ratioOf 3 // 33.333333%

The function also has overloaded versions to control the rounding strategy:

    // rounds using 2 decimal places
    1.ratioOf(3, 2) // 33.00%

    // rounds using 2 decimal places and away from zero (UP)
    1.ratioOf(3, Rounding.to(2, RoundingMode.UP)) // 34.00%

### Calculate the relative change as a Percentage for two numbers

To calculate the relative change between two numbers, use the
[relativeChange][com.eriksencosta.percentage.Number.relativeChange] function:

    1 relativeChange 3 // 200%
    3 relativeChange 1 // -66.666667%

The function also has overloaded versions to control the rounding strategy:

    // rounds using 2 decimal places
    3.relativeChange(1, 2) // -67.00%

    // rounds using 2 decimal places and away from zero (UP)
    3.relativeChange(1, Rounding.to(2, RoundingMode.UP)) // -67.00%

### Calculate the base value of a number when it's a given Percentage

To calculate the base value of a number when it's a given Percentage, use the
[valueWhen][com.eriksencosta.percentage.Number.valueWhen] function:

    5 valueWhen 20.percent() // 25.0

In other words, the function helps to answer the question "5 is 20% of what number?"

# Package com.eriksencosta.percentage

Core functions and types.
