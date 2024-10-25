package rip.hardcore.basic.manager

// [@] This file is authored by SnowyJS on 10/12/2024
// [@] https://snowyjs.lol
// [@] https://github.com/snowypy

import java.sql.Connection
import java.sql.DriverManager
import java.io.File
import java.util.UUID
import org.bukkit.Bukkit
import java.time.Instant
import java.time.Duration

class LifeManager(dbFile: File) {
    private val connection: Connection

    init {
        connection = DriverManager.getConnection("jdbc:sqlite:${dbFile.absolutePath}")
        createTable()
    }

    private fun createTable() {
        val statement = connection.createStatement()
        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS player_lives (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "lives INTEGER NOT NULL, " +
                    "ban_time INTEGER" +
                    ")"
        )

        // [@] Cleanup (Mem Leak Fix)
        statement.close()
    }

    fun getLives(playerUUID: UUID): Int {
        val statement = connection.prepareStatement("SELECT lives FROM player_lives WHERE uuid = ?")
        statement.setString(1, playerUUID.toString())
        val resultSet = statement.executeQuery()
        val lives = if (resultSet.next()) resultSet.getInt("lives") else 3 // Default to 3 lives

        // [@] Cleanup (Mem Leak Fix)
        resultSet.close()
        statement.close()

        return lives
    }

    fun setLives(playerUUID: UUID, lives: Int) {
        val statement = connection.prepareStatement(
            "INSERT OR REPLACE INTO player_lives (uuid, lives, ban_time) VALUES (?, ?, (SELECT ban_time FROM player_lives WHERE uuid = ?))"
        )
        statement.setString(1, playerUUID.toString())
        statement.setInt(2, lives)
        statement.setString(3, playerUUID.toString())
        statement.executeUpdate()

        // [@] Cleanup (Mem Leak Fix)
        statement.close()
    }

    // [%] Decreases a value's lives. Returns the refreshed value.
    fun decreaseLife(playerUUID: UUID): Int {
        val currentLives = getLives(playerUUID)
        val newLives = currentLives - 1
        setLives(playerUUID, newLives)
        return newLives
    }

    fun banPlayer(playerUUID: UUID) {
        val banTime = Instant.now().plus(Duration.ofHours(3)).epochSecond
        val statement = connection.prepareStatement(
            "UPDATE player_lives SET ban_time = ? WHERE uuid = ?"
        )
        statement.setLong(1, banTime)
        statement.setString(2, playerUUID.toString())
        statement.executeUpdate()

        // [@] Cleanup (Mem Leak Fix)
        statement.close()

        val player = Bukkit.getPlayer(playerUUID)
        if (player != null) {
            player.kickPlayer("You have been banned for 3 hours due to losing all your lives.")
        }
    }

    fun isBanned(playerUUID: UUID): Boolean {
        val statement = connection.prepareStatement(
            "SELECT ban_time FROM player_lives WHERE uuid = ?"
        )
        statement.setString(1, playerUUID.toString())
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            val banTime = resultSet.getLong("ban_time")
            resultSet.close()
            statement.close()
            if (banTime > Instant.now().epochSecond) {
                return true // Player is still banned
            } else {
                unbanPlayer(playerUUID)
                return false // Ban has expired
            }
        }
        // [@] Cleanup (Mem Leak Fix)
        resultSet.close()
        statement.close()

        return false // Not banned
    }

    fun unbanPlayer(playerUUID: UUID) {
        val statement = connection.prepareStatement(
            "UPDATE player_lives SET ban_time = NULL WHERE uuid = ?"
        )
        statement.setString(1, playerUUID.toString())
        statement.executeUpdate()
        // [@] Cleanup (Mem Leak Fix)
        statement.close()
    }

    // [@] Cleanup (Mem Leak Fix)
    fun close() {
        connection.close()
    }
}