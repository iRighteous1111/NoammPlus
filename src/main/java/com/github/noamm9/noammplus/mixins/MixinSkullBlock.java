package com.github.noamm9.noammplus.mixins;

import com.github.noamm9.noammplus.NoammPlus;
import com.github.noamm9.noammplus.features.impl.plus.SecretHitboxesPlus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = SkullBlock.class, priority = 1500)
public class MixinSkullBlock {
    @org.spongepowered.asm.mixin.Overwrite
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (NoammPlus.secretHitboxesEnabled && SecretHitboxesPlus.INSTANCE.enabled && SecretHitboxesPlus.isSkullEnabled()) {
            return Shapes.block();
        }
        return Shapes.empty();
    }
}
