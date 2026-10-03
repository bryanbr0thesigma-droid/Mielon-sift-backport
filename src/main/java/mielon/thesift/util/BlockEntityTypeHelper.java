package mielon.thesift.util;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import mielon.thesift.mixin.BlockEntityTypeAccessor;

/** Adds extra valid blocks to a vanilla block entity type (signs, hanging signs). */
public final class BlockEntityTypeHelper {
   private BlockEntityTypeHelper() {
   }

   public static void addValidBlocks(BlockEntityType<?> type, Block... blocks) {
      BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor)type;
      ImmutableSet.Builder<Block> builder = ImmutableSet.builder();
      Set<Block> current = accessor.the_sift$getValidBlocks();
      builder.addAll(current);
      builder.add(blocks);
      accessor.the_sift$setValidBlocks(builder.build());
   }
}
