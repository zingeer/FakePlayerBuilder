package com.github.zingeer.fake_player_builder.utils

import com.github.zingeer.fake_player_builder.FakePlayerBuilderManager
import com.github.zingeer.fake_player_builder.skin.SkinTexture
import net.minecraft.server.v1_16_R3.EntityPlayer
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.craftbukkit.v1_16_R3.CraftServer
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

val World.handle
    get() = (this as CraftWorld).handle
val Server.handle
    get() = (this as CraftServer).handle
val Entity.handle
    get() = (this as CraftEntity).handle
val Player.handle
    get() = (this as CraftPlayer).handle

fun Player.sendPacket(packet: Any) = FakePlayerBuilderManager.packetListener.sendPacket(player, packet)


val EntityPlayer.texture
    get() = SkinTexture(
        profile.properties.get("texture").iterator().next().value,
        profile.properties.get("texture").iterator().next().signature
    )