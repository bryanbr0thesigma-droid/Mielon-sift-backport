package mielon.thesift.block;

import mielon.thesift.block.entity.ShelfBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/** Backport of the 1.21.9+ shelf: an open three-slot display block. */
public class OvergrownShelfBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private static final VoxelShape[] SHAPES = new VoxelShape[4];

   static {
      VoxelShape north = Shapes.or(Block.box(0, 0, 13, 16, 16, 16), Block.box(0, 13, 0, 16, 16, 13), Block.box(0, 0, 0, 16, 3, 13));
      for (Direction dir : Direction.Plane.HORIZONTAL) {
         VoxelShape shape = north;
         for (int i = 0; i < dir.get2DDataValue() + 2; i++) {
            shape = rotate90(shape);
         }
         SHAPES[dir.get2DDataValue()] = shape;
      }
   }

   public OvergrownShelfBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(WATERLOGGED, false));
   }

   private static VoxelShape rotate90(VoxelShape shape) {
      VoxelShape[] out = {Shapes.empty()};
      shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> out[0] = Shapes.or(out[0], Shapes.box(1 - z2, y1, x1, 1 - z1, y2, x2)));
      return out[0];
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, POWERED, WATERLOGGED);
   }

   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
      return this.defaultBlockState()
         .setValue(FACING, context.getHorizontalDirection().getOpposite())
         .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()))
         .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return SHAPES[state.getValue(FACING).get2DDataValue()];
   }

   @Override
   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   @Override
   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
      if (state.getValue(WATERLOGGED)) {
         level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
      }
      return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
   }

   @Override
   public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
      return type == PathComputationType.WATER && state.getValue(WATERLOGGED);
   }

   @Override
   public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos from, boolean moving) {
      if (!level.isClientSide) {
         boolean powered = level.hasNeighborSignal(pos);
         if (powered != state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, powered), 3);
         }
      }
   }

   @Override
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new ShelfBlockEntity(pos, state);
   }

   @Override
   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (hit.getDirection() != state.getValue(FACING) && hit.getDirection() != Direction.UP) {
         return InteractionResult.PASS;
      }
      if (!(level.getBlockEntity(pos) instanceof ShelfBlockEntity shelf)) {
         return InteractionResult.PASS;
      }
      Direction facing = state.getValue(FACING);
      Direction right = facing.getCounterClockWise();
      double u = (hit.getLocation().x - pos.getX() - 0.5) * right.getStepX() + (hit.getLocation().z - pos.getZ() - 0.5) * right.getStepZ();
      int slot = Math.max(0, Math.min(2, (int)Math.floor((u + 0.5) * 3.0)));
      ItemStack held = player.getItemInHand(hand);
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      }
      ItemStack inSlot = shelf.getItem(slot);
      if (!held.isEmpty() && inSlot.isEmpty()) {
         shelf.setItem(slot, player.getAbilities().instabuild ? held.copyWithCount(1) : held.split(held.getCount()));
         level.playSound(null, pos, net.minecraft.sounds.SoundEvents.CHISELED_BOOKSHELF_INSERT, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
         return InteractionResult.CONSUME;
      }
      if (held.isEmpty() && !inSlot.isEmpty()) {
         shelf.setItem(slot, ItemStack.EMPTY);
         if (!player.getInventory().add(inSlot)) {
            player.drop(inSlot, false);
         }
         level.playSound(null, pos, net.minecraft.sounds.SoundEvents.CHISELED_BOOKSHELF_PICKUP, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
         return InteractionResult.CONSUME;
      }
      if (!held.isEmpty()) {
         // swap
         ItemStack taken = inSlot.copy();
         shelf.setItem(slot, held.split(held.getCount()));
         player.setItemInHand(hand, taken);
         return InteractionResult.CONSUME;
      }
      return InteractionResult.PASS;
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
      if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof ShelfBlockEntity shelf) {
         for (int i = 0; i < 3; i++) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), shelf.getItem(i));
         }
      }
      super.onRemove(state, level, pos, newState, moved);
   }
}
