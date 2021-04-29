package com.github.zingeer.fake_player_builder

import com.comphenix.tinyprotocol.TinyProtocol
import com.github.zingeer.fake_player_builder.events.FakePlayerInteractEvent
import com.github.zingeer.fake_player_builder.events.FakePlayerUpdateEvent
import com.github.zingeer.fake_player_builder.utils.sendPacket
import com.github.zingeer.fake_player_builder.wrapper.EntityUseAction
import io.netty.channel.Channel
import net.minecraft.server.v1_16_R3.PacketPlayInUseEntity
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

object FakePlayerBuilderManager {

    val registeredFakePlayer = HashSet<FakePlayer>()

    lateinit var packetListener: TinyProtocol
    private lateinit var updateRunnable: BukkitRunnable
    private lateinit var eventListener: Listener

    fun initialize(plugin: Plugin) {
        packetListener = object : TinyProtocol(plugin) {
            override fun onPacketInAsync(sender: Player?, channel: Channel, packet: Any?): Any? {
                if (packet is PacketPlayInUseEntity) {
                    if (sender != null) {
                        val fakePlayer = registeredFakePlayer.find {
                            it.entityPlayer.bukkitEntity.entityId == packet.entityId
                        }
                        if (fakePlayer != null) {
                            Bukkit.getServer().pluginManager.callEvent(FakePlayerInteractEvent(sender, fakePlayer, EntityUseAction.INTERACT))
                        }
                    }
                }

                return packet
            }
        }
        updateRunnable = object : BukkitRunnable() {
            override fun run() {
                registeredFakePlayer.forEach { fakePlayer ->
                    if (fakePlayer.lookAtPlayer) {
                        fakePlayer.viewers.forEach {
                            val direction = fakePlayer.location.toVector().subtract(it.location.toVector()).multiply(-1)
                            val lookLocation = fakePlayer.location.clone().setDirection(direction)

                            it.sendPacket(PacketPlayOutEntity.PacketPlayOutEntityLook(
                                fakePlayer.entityPlayer.id,
                                (lookLocation.yaw * 256.0f / 360.0f).toInt().toByte(),
                                (lookLocation.pitch * 256.0f / 360.0f).toInt().toByte(),
                                true
                            ))
                        }
                    }
                    Bukkit.getServer().pluginManager.callEvent(FakePlayerUpdateEvent(fakePlayer))
                }
            }
        }
        eventListener = object : Listener {
            @EventHandler
            fun onJoin(event: PlayerJoinEvent) {

            }

            @EventHandler
            fun onQuit(event: PlayerQuitEvent) {
                registeredFakePlayer.forEach { it.hideToPlayer(event.player) }
            }
        }
        plugin.server.pluginManager.registerEvents(eventListener, plugin)
        updateRunnable.runTaskTimerAsynchronously(plugin, 1, 1)
    }

    fun register(fakePlayer: FakePlayer) = registeredFakePlayer.add(fakePlayer)

    fun unregister(fakePlayer: FakePlayer) {
        fakePlayer.viewers.forEach { fakePlayer.hideToPlayer(it) }
        registeredFakePlayer.removeIf { npc -> npc == fakePlayer }
    }
}