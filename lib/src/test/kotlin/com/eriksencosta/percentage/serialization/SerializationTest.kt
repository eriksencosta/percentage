@file:UseSerializers(PercentageSerializer::class)

package com.eriksencosta.percentage.serialization

import com.eriksencosta.percentage.Percentage
import com.eriksencosta.percentage.percent
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SerializationTest {
    @Serializable
    internal data class Stub(val percentage: Percentage)

    @Test
    fun test() {
        val stub = Stub(100.percent())
        assertEquals("{\"percentage\":100.0}", Json.encodeToString(stub))
    }

    @Test
    fun test2() {
        assertEquals(Stub(100.percent()), Json.decodeFromString<Stub>("{\"percentage\":100.0}"))

        assertNotEquals(Stub(100.percent(2)), Json.decodeFromString<Stub>("{\"percentage\":100.0}"))
    }
}
