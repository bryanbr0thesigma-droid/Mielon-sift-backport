package mielon.thesift.fluid;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public final class ModFluids {
   public static final FlowingFluid ICHOR = Registry.register(BuiltInRegistries.FLUID, id("ichor"), new IchorFluid.Source());
   public static final FlowingFluid FLOWING_ICHOR = Registry.register(BuiltInRegistries.FLUID, id("flowing_ichor"), new IchorFluid.Flowing());
   public static final LiquidBlock ICHOR_BLOCK = Registry.register(
      BuiltInRegistries.BLOCK,
      id("ichor"),
      new IchorLiquidBlock(FLOWING_ICHOR, Properties.copy(Blocks.WATER).lightLevel(state -> 12).noLootTable())
   );
   public static final Item ICHOR_BUCKET = Registry.register(
      BuiltInRegistries.ITEM,
      id("ichor_bucket"),
      new IchorBucketItem(ICHOR, new net.minecraft.world.item.Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET))
   );

   private ModFluids() {
   }

   public static void initialize() {
      IchorState.source = ICHOR.getSource(false);
   }

   private static ResourceLocation id(String path) {
      return new ResourceLocation("the_sift", path);
   }
}
