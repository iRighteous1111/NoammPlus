package com.github.noamm9.noammplus.mixins;

import com.github.noamm9.noammplus.features.impl.plus.SecretHitboxesPlus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ButtonBlock.class, priority = 1500)
public class MixinButtonBlock {
    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    private void modifyShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (SecretHitboxesPlus.INSTANCE.enabled && SecretHitboxesPlus.getButton().getValue()) {
            float size = SecretHitboxesPlus.getButtonSize().getValue();
            if (size > 0.0f) {
                cir.setReturnValue(SecretHitboxesPlus.getButtonShape(state, size));
            }
        }
    }
}
