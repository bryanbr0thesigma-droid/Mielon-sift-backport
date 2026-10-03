package mielon.thesift.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SiftRubbleBlock extends FallingBlock {

   public SiftRubbleBlock(Properties properties) {
      super(properties);
   }
   public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
      return 6453107;
   }
}
