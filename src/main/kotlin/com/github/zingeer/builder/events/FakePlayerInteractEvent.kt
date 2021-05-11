package com.github.zingeer.builder.events

import com.github.zingeer.builder.FakePlayer
import com.github.zingeer.builder.wrapper.EntityUseAction
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class FakePlayerInteractEvent(
    val whoClicked: Player,
    val fakePlayer: FakePlayer,
    val action: EntityUseAction
) : PlayerEvent(whoClicked) {

    private val _handlerList = HandlerList()

    override fun getHandlers(): HandlerList = _handlerList
}