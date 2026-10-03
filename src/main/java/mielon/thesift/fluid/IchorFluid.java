package mielon.thesift.fluid;

import java.util.Optional;
import mielon.thesift.particle.ModParticles;
import mielon.thesift.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;

public abstract class IchorFluid extends WaterFluid {
   private static final int REGENERATION_TICKS_AFTER_EXIT = 101;

   public Fluid getFlowing() {
      return ModFluids.FLOWING_ICHOR;
   }

   public Fluid getSource() {
      return ModFluids.ICHOR;
   }

   public Item getBucket() {
      return ModFluids.ICHOR_BUCKET;
   }

   public Optional<SoundEvent> getPickupSound() {
      return Optional.of(ModSounds.ICHOR_BUCKET_FILL);
   }

   protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState state, Direction direction, FluidState fluid) {
      if (IchorWaterlogging.canIchorlog(state)) {
         IchorWaterlogging.fill(level, pos, state);
      } else {
         super.spreadTo(level, pos, state, direction, fluid);
      }
   }

   public BlockState createLegacyBlock(FluidState state) {
      return (BlockState)ModFluids.ICHOR_BLOCK.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
   }

   public boolean isSame(Fluid fluid) {
      return fluid == ModFluids.ICHOR || fluid == ModFluids.FLOWING_ICHOR;
   }

   protected boolean canConvertToSource() {
      return false;
   }

   public int getSlopeFindDistance(LevelReader level) {
      return 6;
   }

   protected FluidState getNewLiquid(Level level, BlockPos pos, BlockState state) {
      FluidState next = super.getNewLiquid(level, pos, state);
      return !next.isEmpty() && next.getType().isSame(this) && !next.isSource() && !next.getValue(FALLING) && next.getAmount() > 6
         ? this.getFlowing(6, false)
         : next;
   }

   public int getDropOff(LevelReader level) {
      return 1;
   }

   public int getTickDelay(LevelReader level) {
      return 7;
   }

   public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
      super.animateTick(level, pos, state, random);
      if (state.isSource() && level.getBlockState(pos.above()).isAir() && random.nextInt(8) == 0) {
         double surfaceY = pos.getY() + state.getHeight(level, pos) + 0.00625;
         level.addParticle(
            ModParticles.ICHOR_SURFACE_MIST,
            pos.getX() + 0.1 + random.nextDouble() * 0.8,
            surfaceY,
            pos.getZ() + 0.1 + random.nextDouble() * 0.8,
            0.0,
            0.0,
            0.0
         );
      }
   }

   public static final class Flowing extends IchorFluid {
      protected void createFluidStateDefinition(Builder<Fluid, FluidState> builder) {
         super.createFluidStateDefinition(builder);
         builder.add(new Property[]{LEVEL});
      }

      public int getAmount(FluidState state) {
         return (Integer)state.getValue(LEVEL);
      }

      public boolean isSource(FluidState state) {
         return false;
      }
   }

   public static final class Source extends IchorFluid {
      public int getAmount(FluidState state) {
         return 8;
      }

      public boolean isSource(FluidState state) {
         return true;
      }
   }
}
