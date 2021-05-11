package com.github.zingeer.builder

import com.github.zingeer.builder.skin.SkinTexture
import com.github.zingeer.builder.utils.handle
import com.mojang.authlib.GameProfile
import net.minecraft.server.v1_16_R3.EntityPlayer
import net.minecraft.server.v1_16_R3.EntityPose
import net.minecraft.server.v1_16_R3.PlayerInteractManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_16_R3.CraftServer
import java.util.*

class FakePlayerBuilder {
    private var location: Location = Location(null, 0.0, 0.0, 0.0)

    private lateinit var _texture: SkinTexture

    private var displayName: String? = null

    private var pose: EntityPose = EntityPose.STANDING

    private var lookAtPlayer: Boolean = false

    fun location(location: Location): FakePlayerBuilder = apply {
        this.location = location
    }

    fun texture(texture: SkinTexture): FakePlayerBuilder = apply {
        this._texture = texture
    }
    
    fun lookAtPlayer(lookAt: Boolean): FakePlayerBuilder = apply {
        this.lookAtPlayer = lookAt
    }

    fun build(): FakePlayer {
        val uniqueId = UUID.randomUUID()

        val fakePlayer = FakePlayer(
            EntityPlayer(
                server, location.world.handle,
                GameProfile(uniqueId, uniqueId.toString().substring(0,6)),
                PlayerInteractManager(location.world.handle)
            ), displayName, pose
        )
        fakePlayer.location = location
        if (this::_texture.isInitialized) {
            fakePlayer.texture = _texture
        }

        fakePlayer.lookAtPlayer = lookAtPlayer

        FakePlayerBuilderManager.register(fakePlayer)
        return fakePlayer
    }

    companion object {
        private val server = (Bukkit.getServer() as CraftServer).server
    }
}