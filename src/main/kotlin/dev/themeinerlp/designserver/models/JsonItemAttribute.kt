package dev.themeinerlp.designserver.models

import kotlinx.serialization.Serializable
import net.minestom.server.attribute.AttributeOperation
import net.minestom.server.item.attribute.AttributeSlot

@Serializable
data class JsonItemAttribute(
    val uuid: String,
    val internalName: String,
    val attribute: String,
    val operation: AttributeOperation,
    val value: Double,
    val slot: AttributeSlot
)
