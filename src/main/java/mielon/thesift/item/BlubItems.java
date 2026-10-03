package mielon.thesift.item;

import mielon.thesift.entity.ModEntities;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Item.Properties;

public final class BlubItems {
   public static final Item BLUB_SPAWN_EGG = Registry.register(
      BuiltInRegistries.ITEM,
      new ResourceLocation("the_sift", "blub_spawn_egg"),
      new SpawnEggItem(ModEntities.BLUB, 0x2D6B63, 0xE0C36A, new Properties())
   );

   private BlubItems() {
   }

   public static void initialize() {
   }
}
