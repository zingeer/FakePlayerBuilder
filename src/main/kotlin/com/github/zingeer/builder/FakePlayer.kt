package com.github.zingeer.builder

import com.github.zingeer.builder.skin.SkinTexture
import com.github.zingeer.builder.utils.handle
import com.github.zingeer.builder.utils.sendPacket
import com.github.zingeer.builder.utils.texture
import com.mojang.authlib.properties.Property
import net.minecraft.server.v1_16_R3.*
import org.bukkit.Location
import org.bukkit.entity.Player

class FakePlayer(
    var entityPlayer: EntityPlayer,
    var displayName: String? = null,
    var pose: EntityPose = EntityPose.STANDING,
) {
    var texture: SkinTexture
        get () = entityPlayer.texture
        set(value) {
            entityPlayer.profile.properties.put("textures", Property(value.value, value.signature))
        }

    var location: Location
        get() = entityPlayer.origin
        set(value) {
            entityPlayer.apply {
                setLocation(value.x, value.y, value.z, value.yaw, value.pitch)
                world = value.world.handle
            }
        }

    var lookAtPlayer: Boolean = false

    var viewers: HashSet<Player> = hashSetOf()

    private val watcher: DataWatcher = DataWatcher(entityPlayer)

    private val scoreboardTeam = ScoreboardTeam(Scoreboard(), "npc=${entityPlayer.uniqueID.toString().substring(0, 6)}").apply {
        nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.NEVER
    }

    fun showToPlayer(player: Player) {
        val playerAddPacket =
            PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer)

        val spawnEntityPacket = PacketPlayOutNamedEntitySpawn(entityPlayer)

        val playerRemovePacket = PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer)
        val playerHeadRotation =
            PacketPlayOutEntityHeadRotation(entityPlayer, ((location.yaw * 256.0F) / 360.0F).toInt().toByte())
        val playerBodyRotate = PacketPlayOutAnimation(entityPlayer, 0)

        player.sendPacket(playerAddPacket)
        player.sendPacket(spawnEntityPacket)
        player.sendPacket(playerHeadRotation)
        player.sendPacket(playerBodyRotate)
        player.sendPacket(playerRemovePacket)
        player.sendPacket(PacketPlayOutScoreboardTeam(scoreboardTeam, 0))
        player.sendPacket(PacketPlayOutScoreboardTeam(scoreboardTeam, listOf(entityPlayer.name), 3))
    }

    fun hideToPlayer(player: Player) {
        player.sendPacket(PacketPlayOutEntityDestroy(entityPlayer.id))
        viewers.removeIf { viewer -> viewer == player }
    }

    fun teleport(location: Location) {
        TODO("Not yet implemented")
    }

    fun moveTo(location: Location) {

    }
}