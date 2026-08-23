package com.github.noamm9.noammplus

import com.github.noamm9.NoammAddons
import net.fabricmc.api.ClientModInitializer

object NoammPlus: ClientModInitializer {
    @JvmField
    var secretHitboxesEnabled = false

    override fun onInitializeClient() {
        NoammAddons.logger.info("Hi from NoammPlus!")
    }
}
