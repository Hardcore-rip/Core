package rip.hardcore.basic.storage

import org.bukkit.Location
import java.util.UUID

data class Home(
    val playerUUID: UUID,
    val name: String,
    val location: Location
)
