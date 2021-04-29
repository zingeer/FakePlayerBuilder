package com.github.zingeer.fake_player_builder.events

import com.github.zingeer.fake_player_builder.FakePlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class FakePlayerUpdateEvent(
    val fakePlayer: FakePlayer
) : Event() {

    private val _handlerList = HandlerList()

    override fun getHandlers(): HandlerList = _handlerList
}