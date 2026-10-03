package mielon.thesift.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public final class OvergrownWillowFoliageBlock extends LeavesBlock {
   private static final int FALLING_LEAF_COLOR = 4063205;

   public OvergrownWillowFoliageBlock(float leafParticleChance, Properties properties) {
      super(properties);
   }
}
