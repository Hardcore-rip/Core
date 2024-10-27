package rip.hardcore.basic.utils

import org.bukkit.Material
import org.apache.commons.lang3.text.WordUtils

fun String.formatMaterial(): String {
    return WordUtils.capitalizeFully(this.replace('_', ' ').lowercase())
}

fun formatMaterialName(material: Material): String {
    return material.name.formatMaterial()
}