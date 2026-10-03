package mielon.thesift.block;

import mielon.thesift.fluid.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.material.Fluid;

public final class IchorCauldronBlock extends LayeredCauldronBlock {
   public static final java.util.Map<net.minecraft.world.item.Item, net.minecraft.core.cauldron.CauldronInteraction> INTERACTIONS = net.minecraft.core.cauldron.CauldronInteraction.newInteractionMap();

   public IchorCauldronBlock(Properties properties) {
      super(properties, precipitation -> false, INTERACTIONS);
   }

   protected boolean canReceiveStalactiteDrip(Fluid fluid) {
      return fluid == ModFluids.ICHOR || fluid == ModFluids.FLOWING_ICHOR;
   }

   protected void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid) {
      if (this.canReceiveStalactiteDrip(fluid) && !this.isFull(state)) {
         BlockState next = (BlockState)state.setValue(LEVEL, (Integer)state.getValue(LEVEL) + 1);
         level.setBlockAndUpdate(pos, next);
         level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(next));
         level.levelEvent(1047, pos, 0);
      }
   }
}
