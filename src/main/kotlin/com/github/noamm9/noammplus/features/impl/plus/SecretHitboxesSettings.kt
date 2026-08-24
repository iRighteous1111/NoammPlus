package com.github.noamm9.noammplus.features.impl.plus

import com.github.noamm9.features.Feature
import com.github.noamm9.NoammAddons.mc

object SecretHitboxesSettings {

    val isNewVersion = try {
        Class.forName("com.github.noamm9.config.types.ToggleSetting", false, SecretHitboxesSettings::class.java.classLoader)
        true
    } catch (e: Throwable) {
        false
    }

    private val toggleSettingClass = if (isNewVersion) {
        Class.forName("com.github.noamm9.config.types.ToggleSetting")
    } else {
        Class.forName("com.github.noamm9.ui.clickgui.components.impl.ToggleSetting")
    }

    private val sliderSettingClass = if (isNewVersion) {
        Class.forName("com.github.noamm9.config.types.SliderSetting")
    } else {
        Class.forName("com.github.noamm9.ui.clickgui.components.impl.SliderSetting")
    }

    // Instantiation helpers using robust constructor scanning
    private fun createToggle(name: String, defaultValue: Boolean): Any {
        val constructor = toggleSettingClass.constructors.find {
            it.parameterTypes.size == 2 &&
            it.parameterTypes[0] == String::class.java &&
            (it.parameterTypes[1] == Boolean::class.javaPrimitiveType || it.parameterTypes[1] == Boolean::class.java)
        } ?: throw NoSuchMethodException("Could not find ToggleSetting constructor with 2 parameters")
        return constructor.newInstance(name, defaultValue)
    }

    private fun createSlider(name: String, defaultValue: Float, min: Float, max: Float, step: Float): Any {
        // T: Number erases to java.lang.Number. The constructor has 6 parameters: (String, Number, Number, Number, Number, String)
        val constructor = sliderSettingClass.constructors.find {
            it.parameterTypes.size == 6 &&
            it.parameterTypes[0] == String::class.java &&
            Number::class.java.isAssignableFrom(it.parameterTypes[1])
        } ?: throw NoSuchMethodException("Could not find SliderSetting constructor with 6 parameters")
        return constructor.newInstance(name, defaultValue, min, max, step, "")
    }

    // Value helper
    private fun getValue(setting: Any): Any {
        val method = setting.javaClass.getMethod("getValue")
        return method.invoke(setting)
    }

    // Builder helpers
    private fun withDescription(setting: Any, desc: String, feature: Feature): Any {
        if (isNewVersion) {
            val providerClass = Class.forName("com.github.noamm9.config.SettingProvider")
            val defaultImplsClass = Class.forName("com.github.noamm9.config.SettingProvider\$DefaultImpls")
            val configHolderClass = Class.forName("com.github.noamm9.config.ConfigHolder")
            val method = defaultImplsClass.getMethod("withDescription", providerClass, configHolderClass, String::class.java)
            return method.invoke(null, feature, setting, desc)
        } else {
            val companionClass = Class.forName("com.github.noamm9.ui.clickgui.components.Setting\$Companion")
            val settingClass = Class.forName("com.github.noamm9.ui.clickgui.components.Setting")
            val companionInstance = Class.forName("com.github.noamm9.ui.clickgui.components.Setting").getField("Companion").get(null)
            val method = companionClass.getMethod("withDescription", settingClass, String::class.java)
            return method.invoke(companionInstance, setting, desc)
        }
    }

    private fun showIf(setting: Any, condition: () -> Boolean, feature: Feature): Any {
        val functionClass = Class.forName("kotlin.jvm.functions.Function0")
        if (isNewVersion) {
            val providerClass = Class.forName("com.github.noamm9.config.SettingProvider")
            val defaultImplsClass = Class.forName("com.github.noamm9.config.SettingProvider\$DefaultImpls")
            val configHolderClass = Class.forName("com.github.noamm9.config.ConfigHolder")
            val method = defaultImplsClass.getMethod("showIf", providerClass, configHolderClass, functionClass)
            return method.invoke(null, feature, setting, condition)
        } else {
            val companionClass = Class.forName("com.github.noamm9.ui.clickgui.components.Setting\$Companion")
            val settingClass = Class.forName("com.github.noamm9.ui.clickgui.components.Setting")
            val companionInstance = Class.forName("com.github.noamm9.ui.clickgui.components.Setting").getField("Companion").get(null)
            val method = companionClass.getMethod("showIf", settingClass, functionClass)
            return method.invoke(companionInstance, setting, condition)
        }
    }

    private fun onChange(setting: Any, listener: () -> Unit, feature: Feature): Any {
        val functionClass = Class.forName("kotlin.jvm.functions.Function1")
        val listenerWrapper = object : kotlin.jvm.functions.Function1<Any?, Unit> {
            override fun invoke(p1: Any?) {
                listener()
            }
        }
        if (isNewVersion) {
            val providerClass = Class.forName("com.github.noamm9.config.SettingProvider")
            val defaultImplsClass = Class.forName("com.github.noamm9.config.SettingProvider\$DefaultImpls")
            val configHolderClass = Class.forName("com.github.noamm9.config.ConfigHolder")
            val method = defaultImplsClass.getMethod("onChange", providerClass, configHolderClass, functionClass)
            return method.invoke(null, feature, setting, listenerWrapper)
        } else {
            val companionClass = Class.forName("com.github.noamm9.ui.clickgui.components.Setting\$Companion")
            val settingClass = Class.forName("com.github.noamm9.ui.clickgui.components.Setting")
            val companionInstance = Class.forName("com.github.noamm9.ui.clickgui.components.Setting").getField("Companion").get(null)
            val method = companionClass.getMethod("onChange", settingClass, functionClass)
            return method.invoke(companionInstance, setting, listenerWrapper)
        }
    }

    // Actual settings instances (instantiated in initSettings)
    lateinit var lever: Any
    lateinit var leverWidth: Any
    lateinit var leverHeight: Any
    lateinit var leverLength: Any
    lateinit var button: Any
    lateinit var buttonSize: Any
    lateinit var skull: Any
    lateinit var mushroom: Any

    fun initSettings(feature: Feature) {
        lever = createToggle("Lever", false)
        withDescription(lever, "Full block Lever hitbox.", feature)
        onChange(lever, { mc.levelRenderer?.allChanged() }, feature)

        leverWidth = createSlider("Lever Width", 1.0f, 0.0f, 1.0f, 0.05f)
        showIf(leverWidth, { getValue(lever) as Boolean }, feature)
        withDescription(leverWidth, "Lever hitbox width (X axis). 0.0 means vanilla size.", feature)
        onChange(leverWidth, { mc.levelRenderer?.allChanged() }, feature)

        leverHeight = createSlider("Lever Height", 1.0f, 0.0f, 1.0f, 0.05f)
        showIf(leverHeight, { getValue(lever) as Boolean }, feature)
        withDescription(leverHeight, "Lever hitbox height (Y axis). 0.0 means vanilla size.", feature)
        onChange(leverHeight, { mc.levelRenderer?.allChanged() }, feature)

        leverLength = createSlider("Lever Length", 1.0f, 0.0f, 1.0f, 0.05f)
        showIf(leverLength, { getValue(lever) as Boolean }, feature)
        withDescription(leverLength, "Lever hitbox length (Z axis). 0.0 means vanilla size.", feature)
        onChange(leverLength, { mc.levelRenderer?.allChanged() }, feature)

        button = createToggle("Button", false)
        withDescription(button, "Full block button hitbox.", feature)
        onChange(button, { mc.levelRenderer?.allChanged() }, feature)

        buttonSize = createSlider("Button Size", 1.0f, 0.0f, 1.0f, 0.05f)
        showIf(buttonSize, { getValue(button) as Boolean }, feature)
        withDescription(buttonSize, "Button hitbox size ratio. 0.0 means vanilla size.", feature)
        onChange(buttonSize, { mc.levelRenderer?.allChanged() }, feature)

        skull = createToggle("Skulls", false)
        withDescription(skull, "Full block Skull hitbox.", feature)
        onChange(skull, { mc.levelRenderer?.allChanged() }, feature)

        mushroom = createToggle("Mushroom", false)
        withDescription(mushroom, "Full block Mushroom hitbox.", feature)
        onChange(mushroom, { mc.levelRenderer?.allChanged() }, feature)
    }

    val leverValue: Boolean get() = getValue(lever) as Boolean
    val leverWidthValue: Float get() = getValue(leverWidth) as Float
    val leverHeightValue: Float get() = getValue(leverHeight) as Float
    val leverLengthValue: Float get() = getValue(leverLength) as Float
    val buttonValue: Boolean get() = getValue(button) as Boolean
    val buttonSizeValue: Float get() = getValue(buttonSize) as Float
    val skullValue: Boolean get() = getValue(skull) as Boolean
    val mushroomValue: Boolean get() = getValue(mushroom) as Boolean

    @Suppress("UNCHECKED_CAST")
    fun register(feature: Feature) {
        initSettings(feature)
        val configSettingsSet = try {
            val getter = feature.javaClass.getMethod("getConfigSettings")
            getter.invoke(feature) as MutableSet<Any>
        } catch (e: Exception) {
            val field = feature.javaClass.getField("configSettings")
            field.get(feature) as MutableSet<Any>
        }
        configSettingsSet.add(lever)
        configSettingsSet.add(leverWidth)
        configSettingsSet.add(leverHeight)
        configSettingsSet.add(leverLength)
        configSettingsSet.add(button)
        configSettingsSet.add(buttonSize)
        configSettingsSet.add(skull)
        configSettingsSet.add(mushroom)
    }
}
