package mielon.thesift.block;

import java.util.Map;
import mielon.thesift.block.entity.SonorousDeepslateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class SonorousDeepslateBlock extends BaseEntityBlock {
   public static final EnumProperty<SonorousDeepslateBlock.Mode> MODE = EnumProperty.create("mode", SonorousDeepslateBlock.Mode.class);

   public SonorousDeepslateBlock(Properties properties) {
      super(properties);
      this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(MODE, SonorousDeepslateBlock.Mode.HORN));
   }
   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{MODE});
   }

   public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
      ItemStack stack = new ItemStack(ModBlocks.SONOROUS_DEEPSLATE_ITEM);
      CompoundTag blockState = new CompoundTag();
      blockState.putString(MODE.getName(), state.getValue(MODE).getSerializedName());
      stack.getOrCreateTag().put("BlockStateTag", blockState);
      return stack;
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new SonorousDeepslateBlockEntity(pos, state);
   }

   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return createTickerHelper(type, ModBlocks.SONOROUS_DEEPSLATE_BLOCK_ENTITY, SonorousDeepslateBlockEntity::tick);
   }

   public static enum Mode implements StringRepresentable {
      HORN("horn"),
      NOTE("note");

      private final String name;

      private Mode(String name) {
         this.name = name;
      }

      public String getSerializedName() {
         return this.name;
      }
   }
}
