package mielon.thesift.item;

import net.minecraft.world.item.ItemStack;

public final class CharoiteEnchanting {
   private static final String USED_TAG = "the_sift:charoite_used";

   private CharoiteEnchanting() {
   }

   public static void markUsed(ItemStack stack) {
      if (!stack.isEmpty()) {
         stack.getOrCreateTag().putBoolean(USED_TAG, true);
      }
   }

   public static boolean wasUsed(ItemStack stack) {
      return stack.hasTag() && stack.getTag().getBoolean(USED_TAG);
   }

   public static boolean blocksAnvil(ItemStack base, ItemStack addition) {
      return !base.isEmpty() && !addition.isEmpty() ? wasUsed(base) || wasUsed(addition) : false;
   }
}
