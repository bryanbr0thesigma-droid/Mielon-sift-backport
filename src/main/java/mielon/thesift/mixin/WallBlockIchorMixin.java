package mielon.thesift.mixin;

import java.util.Map;
import mielon.thesift.fluid.IchorState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Walls build their shape maps from the default (dry) state, so states carrying the extra
 * "ichorlogged" property would have no entry. Look them up through their dry counterpart.
 */
@Mixin(WallBlock.class)
public abstract class WallBlockIchorMixin {
   @Shadow
   @Final
   private Map<BlockState, VoxelShape> shapeByIndex;
   @Shadow
   @Final
   private Map<BlockState, VoxelShape> collisionShapeByIndex;

   @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
   private void theSift$dryShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
      if (state.hasProperty(IchorState.ICHORLOGGED) && state.getValue(IchorState.ICHORLOGGED)) {
         cir.setReturnValue(this.shapeByIndex.get(IchorState.dryDefault(state)));
      }
   }

   @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
   private void theSift$dryCollisionShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir
   ) {
      if (state.hasProperty(IchorState.ICHORLOGGED) && state.getValue(IchorState.ICHORLOGGED)) {
         cir.setReturnValue(this.collisionShapeByIndex.get(IchorState.dryDefault(state)));
      }
   }
}
