package com.github.noamm9.noammplus

import com.github.noamm9.NoammAddons
import net.fabricmc.api.ClientModInitializer

object NoammPlus: ClientModInitializer {
    override fun onInitializeClient() {
        NoammAddons.logger.info("Hi from NoammPlus!")
    }
}
