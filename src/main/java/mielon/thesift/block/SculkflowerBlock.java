package mielon.thesift.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class SculkflowerBlock extends BushBlock {
   private static final VoxelShape SHAPE = Shapes.box(0.25, 0.0, 0.25, 0.75, 0.625, 0.75);

   public SculkflowerBlock(Properties properties) {
      super(properties);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }

   @Override
   protected boolean mayPlaceOn(BlockState floor, BlockGetter level, BlockPos floorPos) {
      return floor.is(Blocks.FARMLAND)
         || floor.is(Blocks.SCULK)
         || floor.is(ModBlocks.HEALTHY_SCULK)
         || floor.is(ModBlocks.DRY_HEALTHY_SCULK)
         || super.mayPlaceOn(floor, level, floorPos);
   }
}
