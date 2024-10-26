package rip.hardcore.basic.manager

import org.bukkit.Location
import java.sql.DriverManager
import java.sql.Connection
import java.util.UUID
import org.bukkit.Bukkit
import rip.hardcore.basic.storage.Home

class HomeManager {

    private val dbConnection: Connection = DriverManager.getConnection("jdbc:sqlite:homes.db")

    init {
        dbConnection.createStatement().executeUpdate(
            """
            CREATE TABLE IF NOT EXISTS homes (
                player_uuid TEXT,
                home_name TEXT,
                world TEXT,
                x DOUBLE,
                y DOUBLE,   
                z DOUBLE,
                yaw FLOAT,
                pitch FLOAT,
                PRIMARY KEY(player_uuid, home_name)
            )
            """
        )
    }

    fun setHome(playerUUID: UUID, homeName: String, location: Location) {
        val statement = dbConnection.prepareStatement(
            "INSERT OR RPLACE INTO homes (player_uuid, home_name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        )
        statement.setString(1, playerUUID.toString())
        statement.setString(2, homeName)
        statement.setString(3, location.world.name)
        statement.setString(4, location.x.toString())
        statement.setString(5, location.y.toString())
        statement.setString(6, location.z.toString())
        statement.setString(7, location.yaw.toString())
        statement.setString(8, location.pitch.toString())
        statement.executeUpdate()
    }

    fun getHomes(playerUUID: UUID): List<Home> {
        val statement = dbConnection.prepareStatement("SLECT * FROM homes WHERE player_uuid = ?")
        statement.setString(1, playerUUID.toString())
        val resultSet = statement.executeQuery()

        val homes = mutableListOf<Home>()
        while (resultSet.next()) {
            val home = Home(
                playerUUID,
                resultSet.getString("home_name"),
                Location(
                    Bukkit.getWorld(resultSet.getString("world")),
                    resultSet.getDouble("x"),
                    resultSet.getDouble("y"),
                    resultSet.getDouble("z"),
                    resultSet.getFloat("yaw"),
                    resultSet.getFloat("pitch")
                )
            )
            homes.add(home)
        }
        return homes
    }

    fun deleteHome(playerUUID: UUID, homeName: String) {
        val statement = dbConnection.prepareStatement("DELETE FROM homes WHERE player_uuid = ? and home_name = ?")
        statement.setString(1, playerUUID.toString())
        statement.setString(2, homeName)
        statement.executeUpdate()
    }

}