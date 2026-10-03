package mielon.thesift.block;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public final class SonorousDeepslateBlockItem extends BlockItem {
   public SonorousDeepslateBlockItem(Block block, Properties properties) {
      super(block, properties);
   }

   public static SonorousDeepslateBlock.Mode modeOf(ItemStack stack) {
      if (stack.hasTag() && stack.getTag().contains("BlockStateTag", 10)) {
         String name = stack.getTag().getCompound("BlockStateTag").getString(SonorousDeepslateBlock.MODE.getName());
         for (SonorousDeepslateBlock.Mode mode : SonorousDeepslateBlock.Mode.values()) {
            if (mode.getSerializedName().equals(name)) {
               return mode;
            }
         }
      }

      return SonorousDeepslateBlock.Mode.HORN;
   }

   @Override
   public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
      SonorousDeepslateBlock.Mode mode = modeOf(stack);
      tooltip.add(Component.literal(mode == SonorousDeepslateBlock.Mode.HORN ? "Horn" : "Note").withStyle(ChatFormatting.YELLOW));
   }
}
