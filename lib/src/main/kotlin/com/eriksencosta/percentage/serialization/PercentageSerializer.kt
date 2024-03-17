package com.eriksencosta.percentage.serialization

import com.eriksencosta.percentage.Percentage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Configures Kotlin's Serialization for Percentage.
 */
@Serializable
object PercentageSerializer : KSerializer<Percentage> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Percentage", PrimitiveKind.DOUBLE)

    override fun serialize(encoder: Encoder, value: Percentage) = encoder.encodeDouble(value.value)

    override fun deserialize(decoder: Decoder): Percentage = Percentage.of(decoder.decodeDouble())
}
