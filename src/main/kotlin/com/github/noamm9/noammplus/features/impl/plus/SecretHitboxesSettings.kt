package com.github.noamm9.noammplus.features.impl.plus

import com.github.noamm9.ui.clickgui.components.impl.ToggleSetting
import com.github.noamm9.ui.clickgui.components.impl.SliderSetting
import com.github.noamm9.ui.clickgui.components.Setting.Companion.withDescription
import com.github.noamm9.ui.clickgui.components.Setting.Companion.showIf
import com.github.noamm9.ui.clickgui.components.Setting.Companion.onChange
import com.github.noamm9.features.Feature
import com.github.noamm9.NoammAddons.mc

object SecretHitboxesSettings {
    val lever = ToggleSetting("Lever").withDescription("Full block Lever hitbox.")
        .onChange { mc.levelRenderer?.allChanged() }

    val leverWidth = SliderSetting("Lever Width", 1.0f, 0.0f, 1.0f, 0.05f)
        .showIf { lever.value }
        .withDescription("Lever hitbox width (X axis). 0.0 means vanilla size.")
        .onChange { mc.levelRenderer?.allChanged() }

    val leverHeight = SliderSetting("Lever Height", 1.0f, 0.0f, 1.0f, 0.05f)
        .showIf { lever.value }
        .withDescription("Lever hitbox height (Y axis). 0.0 means vanilla size.")
        .onChange { mc.levelRenderer?.allChanged() }

    val leverLength = SliderSetting("Lever Length", 1.0f, 0.0f, 1.0f, 0.05f)
        .showIf { lever.value }
        .withDescription("Lever hitbox length (Z axis). 0.0 means vanilla size.")
        .onChange { mc.levelRenderer?.allChanged() }

    val button = ToggleSetting("Button").withDescription("Full block button hitbox.")
        .onChange { mc.levelRenderer?.allChanged() }

    val buttonSize = SliderSetting("Button Size", 1.0f, 0.0f, 1.0f, 0.05f)
        .showIf { button.value }
        .withDescription("Button hitbox size ratio. 0.0 means vanilla size.")
        .onChange { mc.levelRenderer?.allChanged() }

    val skull = ToggleSetting("Skulls").withDescription("Full block Skull hitbox.")
        .onChange { mc.levelRenderer?.allChanged() }

    val mushroom = ToggleSetting("Mushroom").withDescription("Full block Mushroom hitbox.")
        .onChange { mc.levelRenderer?.allChanged() }

    fun register(feature: Feature) {
        feature.configSettings.add(lever)
        feature.configSettings.add(leverWidth)
        feature.configSettings.add(leverHeight)
        feature.configSettings.add(leverLength)
        feature.configSettings.add(button)
        feature.configSettings.add(buttonSize)
        feature.configSettings.add(skull)
        feature.configSettings.add(mushroom)
    }
}
