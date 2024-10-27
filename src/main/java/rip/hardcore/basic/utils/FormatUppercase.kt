package rip.hardcore.basic.utils

import org.bukkit.Material
import org.apache.commons.lang3.text.WordUtils

fun String.formatUppercase(): String {
    return WordUtils.capitalizeFully(this)
}

fun formatUppercaseString(uppercase: String): String {
    return WordUtils.capitalizeFully(uppercase)
}