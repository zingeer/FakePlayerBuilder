package com.github.zingeer.builder.events

import com.github.zingeer.builder.FakePlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class FakePlayerUpdateEvent(
    val fakePlayer: FakePlayer
) : Event() {

    private val _handlerList = HandlerList()

    override fun getHandlers(): HandlerList = _handlerList
}