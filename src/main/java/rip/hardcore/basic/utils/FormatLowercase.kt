package rip.hardcore.basic.utils

import org.bukkit.Material
import org.apache.commons.lang3.text.WordUtils

fun String.formatLowercase(): String {
    return WordUtils.uncapitalize(this)
}

fun formatLowercaseString(lowercase: String): String {
    return WordUtils.uncapitalize(lowercase)
}