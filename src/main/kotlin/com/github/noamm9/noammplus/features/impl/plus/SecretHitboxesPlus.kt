@file:Suppress("UNNECESSARY_SAFE_CALL")
package com.github.noamm9.noammplus.features.impl.plus

import com.github.noamm9.features.Feature
import com.github.noamm9.utils.location.LocationUtils
import com.github.noamm9.noammplus.NoammPlus
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.AttachFace
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

object SecretHitboxesPlus : Feature("Changes the hitboxes of secret blocks to be larger.", "Secret Hitboxes Plus") {

    val isClickGuiPresent = try {
        Class.forName("com.github.noamm9.ui.clickgui.components.impl.ToggleSetting")
        true
    } catch (e: ClassNotFoundException) {
        false
    }

    @JvmStatic
    fun isButtonEnabled(): Boolean {
        if (!isClickGuiPresent) return false
        return SecretHitboxesSettings.button.value
    }

    @JvmStatic
    fun getButtonSizeValue(): Float {
        if (!isClickGuiPresent) return 0.0f
        return SecretHitboxesSettings.buttonSize.value
    }

    @JvmStatic
    fun isLeverEnabled(): Boolean {
        if (!isClickGuiPresent) return false
        return SecretHitboxesSettings.lever.value
    }

    @JvmStatic
    fun getLeverWidthValue(): Float {
        if (!isClickGuiPresent) return 0.0f
        return SecretHitboxesSettings.leverWidth.value
    }

    @JvmStatic
    fun getLeverHeightValue(): Float {
        if (!isClickGuiPresent) return 0.0f
        return SecretHitboxesSettings.leverHeight.value
    }

    @JvmStatic
    fun getLeverLengthValue(): Float {
        if (!isClickGuiPresent) return 0.0f
        return SecretHitboxesSettings.leverLength.value
    }

    @JvmStatic
    fun isSkullEnabled(): Boolean {
        if (!isClickGuiPresent) return false
        return SecretHitboxesSettings.skull.value
    }

    @JvmStatic
    fun isMushroomEnabled(): Boolean {
        if (!isClickGuiPresent) return false
        return SecretHitboxesSettings.mushroom.value
    }

    override fun init() {
        if (isClickGuiPresent) {
            SecretHitboxesSettings.register(this)
        }
        ClientLifecycleEvents.CLIENT_STARTED.register { disableBlockstateCulling() }
    }

    override fun onEnable() {
        super.onEnable()
        NoammPlus.secretHitboxesEnabled = true
        disableBlockstateCulling()
        mc.levelRenderer?.allChanged()
    }

    override fun onDisable() {
        super.onDisable()
        NoammPlus.secretHitboxesEnabled = false
        mc.levelRenderer?.allChanged()
    }

    private fun disableBlockstateCulling() {
        if (! FabricLoader.getInstance().isModLoaded("moreculling")) return
        val main = Class.forName("ca.fxco.moreculling.MoreCulling")
        val config = main.getDeclaredField("CONFIG").get(null)

        val blockStateCulling = config?.javaClass?.getDeclaredField("useBlockStateCulling")
        blockStateCulling?.isAccessible = true
        blockStateCulling?.setBoolean(config, false)
    }

    @JvmStatic
    fun getButtonShape(state: BlockState, size: Float): VoxelShape {
        val face = state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE)
        val direction = state.getValue(FaceAttachedHorizontalDirectionalBlock.FACING)
        val powered = state.getValue(ButtonBlock.POWERED)

        val f2 = (if (powered) 1 else 2) / 16.0
        val baseSize = 0.375
        val lateralSize = baseSize + size.toDouble() * (1.0 - baseSize)
        val low = 0.5 - (lateralSize / 2.0)
        val high = 0.5 + (lateralSize / 2.0)

        return when (face) {
            AttachFace.CEILING -> Shapes.box(low, 1.0 - f2, low, high, 1.0, high)
            AttachFace.FLOOR -> Shapes.box(low, 0.0, low, high, 0.0 + f2, high)
            else -> when (direction) {
                Direction.EAST -> Shapes.box(0.0, low, low, f2, high, high)
                Direction.WEST -> Shapes.box(1.0 - f2, low, low, 1.0, high, high)
                Direction.SOUTH -> Shapes.box(low, low, 0.0, high, high, f2)
                Direction.NORTH -> Shapes.box(low, low, 1.0 - f2, high, high, 1.0)
                Direction.UP -> Shapes.box(low, 0.0, low, high, 0.0 + f2, high)
                Direction.DOWN -> Shapes.box(low, 1.0 - f2, low, high, 1.0, high)
            }
        }
    }

    @JvmStatic
    fun getLeverShape(state: BlockState, w: Float, h: Float, l: Float): VoxelShape {
        val face = state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE)
        val direction = state.getValue(FaceAttachedHorizontalDirectionalBlock.FACING)

        val baseHeight = 0.375
        val baseWidthLength = 0.44

        val effW = baseWidthLength + w.toDouble() * (1.0 - baseWidthLength)
        val effH = baseHeight + h.toDouble() * (1.0 - baseHeight)
        val effL = baseWidthLength + l.toDouble() * (1.0 - baseWidthLength)

        val lowW = 0.5 - (effW / 2.0)
        val highW = 0.5 + (effW / 2.0)
        val lowL = 0.5 - (effL / 2.0)
        val highL = 0.5 + (effL / 2.0)

        return when (face) {
            AttachFace.FLOOR -> {
                if (direction == Direction.EAST || direction == Direction.WEST) {
                    Shapes.box(lowL, 0.0, lowW, highL, effH, highW)
                } else {
                    Shapes.box(lowW, 0.0, lowL, highW, effH, highL)
                }
            }
            AttachFace.CEILING -> {
                if (direction == Direction.EAST || direction == Direction.WEST) {
                    Shapes.box(lowL, 1.0 - effH, lowW, highL, 1.0, highW)
                } else {
                    Shapes.box(lowW, 1.0 - effH, lowL, highW, 1.0, highL)
                }
            }
            else -> { // AttachFace.WALL
                when (direction) {
                    Direction.EAST -> Shapes.box(0.0, lowL, lowW, effH, highL, highW)
                    Direction.WEST -> Shapes.box(1.0 - effH, lowL, lowW, 1.0, highL, highW)
                    Direction.SOUTH -> Shapes.box(lowW, lowL, 0.0, highW, highL, effH)
                    Direction.NORTH -> Shapes.box(lowW, lowL, 1.0 - effH, highW, highL, 1.0)
                    else -> Shapes.box(lowW, 0.0, lowL, highW, effH, highL)
                }
            }
        }
    }

    private val blackListedLevers = listOf(
        BlockPos(61, 136, 142), BlockPos(60, 136, 142), BlockPos(59, 136, 142),
        BlockPos(62, 135, 142), BlockPos(61, 135, 142), BlockPos(59, 135, 142),
        BlockPos(58, 135, 142), BlockPos(62, 134, 142), BlockPos(61, 134, 142),
        BlockPos(59, 134, 142), BlockPos(58, 134, 142), BlockPos(61, 133, 142),
        BlockPos(60, 133, 142), BlockPos(59, 133, 142)
    )

    @JvmStatic
    fun isValidLever(pos: BlockPos): Boolean {
        if (! enabled) return false
        if (! isLeverEnabled()) return false
        if (pos in blackListedLevers && LocationUtils.dungeonFloorNumber == 7) return false
        return true
    }
}
