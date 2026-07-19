package com.github.noamm9.noammplus.mixins;

import com.github.noamm9.noammplus.features.impl.plus.SecretHitboxesPlus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LeverBlock.class, priority = 1500)
public class MixinLeverBlock {
    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    private void modifyShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (SecretHitboxesPlus.INSTANCE.isValidLever(pos)) {
            float w = SecretHitboxesPlus.getLeverWidth().getValue();
            float h = SecretHitboxesPlus.getLeverHeight().getValue();
            float l = SecretHitboxesPlus.getLeverLength().getValue();
            if (w > 0.0f && h > 0.0f && l > 0.0f) {
                cir.setReturnValue(SecretHitboxesPlus.getLeverShape(state, w, h, l));
            }
        }
    }
}
