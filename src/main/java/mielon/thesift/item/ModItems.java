package mielon.thesift.item;

import java.util.List;
import java.util.function.Function;
import mielon.thesift.block.ModBlocks;
import mielon.thesift.block.SonorousDeepslateBlock;
import mielon.thesift.entity.ModEntities;
import mielon.thesift.fluid.ModFluids;
import mielon.thesift.sound.ModSounds;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.crafting.Ingredient;

public final class ModItems {
   public static final Item SINGER_SPAWN_EGG = registerItem("singer_spawn_egg", p -> new SpawnEggItem(ModEntities.SINGER, 0x1B1230, 0x7A5CC2, p), new Properties());
   public static final Item ECHO_GOLEM_SPAWN_EGG = registerItem("echo_golem_spawn_egg", p -> new SpawnEggItem(ModEntities.ECHO_GOLEM, 0x0E3B43, 0x28D7C4, p), new Properties());
   public static final Item DARK_SNIFFER_SPAWN_EGG = registerItem("dark_sniffer_spawn_egg", p -> new SpawnEggItem(ModEntities.DARK_SNIFFER, 0x0B0B14, 0x3E7C6B, p), new Properties());
   public static final Item SIFTER_SPAWN_EGG = registerItem("sifter_spawn_egg", p -> new SpawnEggItem(ModEntities.SIFTER, 0x3A3A44, 0xB89C5E, p), new Properties());
   public static final Item OVERGROWN_WILLOW_BOAT = registerItem(
      "overgrown_willow_boat", p -> new SiftBoatItem(false, p), new Properties().stacksTo(1)
   );
   public static final Item OVERGROWN_WILLOW_CHEST_BOAT = registerItem(
      "overgrown_willow_chest_boat", p -> new SiftBoatItem(true, p), new Properties().stacksTo(1)
   );
   public static final TagKey<Item> SIFTITE_REPAIR_MATERIALS = TagKey.create(
      Registries.ITEM, new ResourceLocation("the_sift", "siftite_repair_materials")
   );
   public static final TagKey<Item> SIFTITE_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation("the_sift", "siftite_items"));
   public static final Tier SIFTITE_TIER = new Tier() {
      public int getUses() {
         return 2031 + 470;
      }

      public float getSpeed() {
         return 9.0F * 1.2F;
      }

      public float getAttackDamageBonus() {
         return 4.0F + 1.0F;
      }

      public int getLevel() {
         return 4;
      }

      public int getEnchantmentValue() {
         return 15;
      }

      public Ingredient getRepairIngredient() {
         return Ingredient.of(SIFTITE_REPAIR_MATERIALS);
      }
   };
   public static final ArmorMaterial SIFTITE_ARMOR_MATERIAL = new ArmorMaterial() {
      private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
      private static final int[] DEFENSE = new int[]{3, 6, 8, 3};

      public int getDurabilityForType(ArmorItem.Type type) {
         return BASE_DURABILITY[type.ordinal()] * 41;
      }

      public int getDefenseForType(ArmorItem.Type type) {
         return DEFENSE[type.ordinal()];
      }

      public int getEnchantmentValue() {
         return 15;
      }

      public net.minecraft.sounds.SoundEvent getEquipSound() {
         return ModSounds.SIFTITE_ARMOR_EQUIP;
      }

      public Ingredient getRepairIngredient() {
         return Ingredient.of(SIFTITE_REPAIR_MATERIALS);
      }

      public String getName() {
         return "the_sift:siftite";
      }

      public float getToughness() {
         return 3.0F;
      }

      public float getKnockbackResistance() {
         return 0.15F;
      }
   };
   public static final Item ICHOR_SHARD = registerItem("ichor_shard", new Properties());
   public static final Item CHAROITE = registerItem("charoite", new Properties());
   public static final Item SIFTITE_NUGGET = registerItem("siftite_nugget", new Properties());
   public static final Item SIFTITE_INGOT = registerItem("siftite_ingot", new Properties());
   public static final Item ICHOR_SNOWBALL = registerItem("ichor_snowball", SnowballItem::new, new Properties().stacksTo(16));
   public static final Item ICHOR_BOTTLE = registerItem("ichor_bottle", IchorBottleItem::new, new Properties().stacksTo(1));
   public static final Item RAW_SIFTER_MEAT = registerItem("raw_sifter_meat", new Properties().food(Foods.PORKCHOP));
   public static final Item COOKED_SIFTER_MEAT = registerItem("cooked_sifter_meat", new Properties().food(Foods.COOKED_PORKCHOP));
   public static final Item SIFT_RIFT = registerItem("sift_rift", SiftRiftItem::new, new Properties().stacksTo(1).rarity(Rarity.EPIC));
   public static final Item MUSIC_DISC_RIFT = registerItem(
      "music_disc_rift", p -> new SiftRecordItem(15, ModSounds.MUSIC_DISC_RIFT, p, 185), new Properties().stacksTo(1).rarity(Rarity.RARE)
   );
   public static final Item MUSIC_DISC_WELLSPRING = registerItem(
      "music_disc_wellspring", p -> new SiftRecordItem(15, ModSounds.MUSIC_DISC_WELLSPRING, p, 183), new Properties().stacksTo(1).rarity(Rarity.RARE)
   );
   public static final Item SCULKFLOWER_SEEDS = registerItem("sculkflower_seeds", p -> new BlockItem(ModBlocks.SCULKFLOWER_CROP, p), new Properties());
   public static final Item SIFTITE_SWORD = registerItem("siftite_sword", p -> new SwordItem(SIFTITE_TIER, 3, -2.4F, p), new Properties().fireResistant());
   public static final Item SIFTITE_SHOVEL = registerItem("siftite_shovel", p -> new ShovelItem(SIFTITE_TIER, 1.5F, -3.0F, p), new Properties().fireResistant());
   public static final Item SIFTITE_PICKAXE = registerItem("siftite_pickaxe", p -> new PickaxeItem(SIFTITE_TIER, 1, -2.8F, p), new Properties().fireResistant());
   public static final Item SIFTITE_AXE = registerItem("siftite_axe", p -> new AxeItem(SIFTITE_TIER, 5.0F, -3.0F, p), new Properties().fireResistant());
   public static final Item SIFTITE_HOE = registerItem("siftite_hoe", p -> new HoeItem(SIFTITE_TIER, -4, 0.0F, p), new Properties().fireResistant());
   public static final Item SIFTITE_SPEAR = registerItem("siftite_spear", p -> new SiftSpearItem(SIFTITE_TIER, p), new Properties().fireResistant());
   public static final Item SIFTITE_HELMET = registerItem(
      "siftite_helmet", p -> new ArmorItem(SIFTITE_ARMOR_MATERIAL, ArmorItem.Type.HELMET, p), new Properties().fireResistant()
   );
   public static final Item SIFTITE_CHESTPLATE = registerItem(
      "siftite_chestplate", p -> new ArmorItem(SIFTITE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, p), new Properties().fireResistant()
   );
   public static final Item SIFTITE_LEGGINGS = registerItem(
      "siftite_leggings", p -> new ArmorItem(SIFTITE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, p), new Properties().fireResistant()
   );
   public static final Item SIFTITE_BOOTS = registerItem(
      "siftite_boots", p -> new ArmorItem(SIFTITE_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, p), new Properties().fireResistant()
   );
   public static final Item SIFTITE_UPGRADE_SMITHING_TEMPLATE = registerItem(
      "siftite_upgrade_smithing_template",
      p -> new SmithingTemplateItem(
         Component.translatable("item.the_sift.smithing_template.siftite_upgrade.applies_to").withStyle(ChatFormatting.BLUE),
         Component.translatable("item.the_sift.smithing_template.siftite_upgrade.ingredients").withStyle(ChatFormatting.BLUE),
         Component.translatable("upgrade.the_sift.siftite_upgrade").withStyle(ChatFormatting.GRAY),
         Component.translatable("item.the_sift.smithing_template.siftite_upgrade.base_slot_description"),
         Component.translatable("item.the_sift.smithing_template.siftite_upgrade.additions_slot_description"),
         List.of(
            new ResourceLocation("item/empty_armor_slot_helmet"),
            new ResourceLocation("item/empty_armor_slot_chestplate"),
            new ResourceLocation("item/empty_armor_slot_leggings"),
            new ResourceLocation("item/empty_armor_slot_boots"),
            new ResourceLocation("item/empty_slot_sword"),
            new ResourceLocation("item/empty_slot_pickaxe"),
            new ResourceLocation("item/empty_slot_axe"),
            new ResourceLocation("item/empty_slot_shovel"),
            new ResourceLocation("item/empty_slot_hoe")
         ),
         List.of(new ResourceLocation("item/empty_slot_ingot"))
      ),
      new Properties()
   );
   public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = ResourceKey.create(
      BuiltInRegistries.CREATIVE_MODE_TAB.key(), new ResourceLocation("the_sift", "the_sift")
   );
   public static final CreativeModeTab CREATIVE_TAB = FabricItemGroup.builder()
      .title(Component.translatable("itemGroup.the_sift"))
      .icon(() -> new ItemStack(ModBlocks.DRY_HEALTHY_SCULK_GROWTH_ITEM))
      .displayItems(
         (params, output) -> {
            output.accept(sonorousStack(SonorousDeepslateBlock.Mode.HORN));
            output.accept(sonorousStack(SonorousDeepslateBlock.Mode.NOTE));
            output.accept(ModBlocks.REINFORCED_SIFTSLATE_ITEM);
            output.accept(ModBlocks.SIFTSLATE_ITEM);
            output.accept(ModBlocks.SIFTSLATE_COAL_ORE_ITEM);
            output.accept(ModBlocks.SIFTSLATE_DIAMOND_ORE_ITEM);
            output.accept(ModBlocks.SIFTSLATE_EMERALD_ORE_ITEM);
            output.accept(ModBlocks.SIFTSLATE_CHAROITE_ORE_ITEM);
            output.accept(ModBlocks.SIFTSLATE_SIFTITE_ORE_ITEM);
            output.accept(ModBlocks.COBBLED_SIFTSLATE_ITEM);
            output.accept(ModBlocks.COBBLED_SIFTSLATE_STAIRS_ITEM);
            output.accept(ModBlocks.COBBLED_SIFTSLATE_SLAB_ITEM);
            output.accept(ModBlocks.COBBLED_SIFTSLATE_WALL_ITEM);
            output.accept(ModBlocks.SIFTSLATE_BRICKS_ITEM);
            output.accept(ModBlocks.SIFTSLATE_BRICK_STAIRS_ITEM);
            output.accept(ModBlocks.SIFTSLATE_BRICK_SLAB_ITEM);
            output.accept(ModBlocks.SIFTSLATE_BRICK_WALL_ITEM);
            output.accept(ModBlocks.CHISELED_SIFTSLATE_ITEM);
            output.accept(ModBlocks.SIFTSLATE_GROWTH_ITEM);
            output.accept(ModBlocks.SIFT_RUBBLE_ITEM);
            output.accept(ModBlocks.HEALTHY_SCULK_ITEM);
            output.accept(ModBlocks.DRY_HEALTHY_SCULK_ITEM);
            output.accept(ModBlocks.DRY_HEALTHY_SCULK_GROWTH_ITEM);
            output.accept(ModBlocks.ICHOR_SNOW_BLOCK_ITEM);
            output.accept(ModBlocks.ICHOR_SNOW_ITEM);
            output.accept(ICHOR_SNOWBALL);
            output.accept(ModBlocks.OVERGROWN_WILLOW_LOG_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_WOOD_ITEM);
            output.accept(ModBlocks.STRIPPED_OVERGROWN_WILLOW_LOG_ITEM);
            output.accept(ModBlocks.STRIPPED_OVERGROWN_WILLOW_WOOD_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_PLANKS_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_STAIRS_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_SLAB_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_FENCE_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_FENCE_GATE_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_DOOR_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_TRAPDOOR_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_PRESSURE_PLATE_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_BUTTON_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_SIGN_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_HANGING_SIGN_ITEM);
            output.accept(OVERGROWN_WILLOW_BOAT);
            output.accept(OVERGROWN_WILLOW_CHEST_BOAT);
            output.accept(ModBlocks.SOUL_BLOCK_ITEM);
            output.accept(ModBlocks.OVERGROWN_FRONDS_ITEM);
            output.accept(ModBlocks.OVERGROWN_CHARD_ITEM);
            output.accept(ModBlocks.OVERGROWN_STALKS_ITEM);
            output.accept(ModBlocks.OVERGROWN_HANGING_ROOTS_ITEM);
            output.accept(ModBlocks.SIFTSLATE_STALKS_ITEM);
            output.accept(ModBlocks.SIFTSLATE_HANGING_ROOTS_ITEM);
            output.accept(ModBlocks.HEALTHY_SCULK_SPROUTS_ITEM);
            output.accept(ModBlocks.DRY_HEALTHY_SCULK_SPROUTS_ITEM);
            output.accept(ModBlocks.OVERGROWN_LOTUS_ITEM);
            output.accept(ModBlocks.SUNBURST_PLANT_ITEM);
            output.accept(ModBlocks.WHISPERBLOOM_ITEM);
            output.accept(SCULKFLOWER_SEEDS);
            output.accept(ModBlocks.SCULKFLOWER_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_FOLIAGE_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_SAPLING_ITEM);
            output.accept(ModBlocks.OVERGROWN_WILLOW_VINES_ITEM);
            output.accept(RAW_SIFTER_MEAT);
            output.accept(COOKED_SIFTER_MEAT);
            output.accept(ModFluids.ICHOR_BUCKET);
            output.accept(ICHOR_BOTTLE);
            output.accept(ModBlocks.ICHOR_GLASS_ITEM);
            output.accept(ModBlocks.ICHOR_GLASS_PANE_ITEM);
            output.accept(ICHOR_SHARD);
            output.accept(CHAROITE);
            output.accept(SIFTITE_INGOT);
            output.accept(SIFTITE_NUGGET);
            output.accept(SIFTITE_SWORD);
            output.accept(SIFTITE_PICKAXE);
            output.accept(SIFTITE_AXE);
            output.accept(SIFTITE_SHOVEL);
            output.accept(SIFTITE_HOE);
            output.accept(SIFTITE_SPEAR);
            output.accept(SIFTITE_HELMET);
            output.accept(SIFTITE_CHESTPLATE);
            output.accept(SIFTITE_LEGGINGS);
            output.accept(SIFTITE_BOOTS);
            output.accept(SIFTITE_UPGRADE_SMITHING_TEMPLATE);
            output.accept(MUSIC_DISC_RIFT);
            output.accept(MUSIC_DISC_WELLSPRING);
            output.accept(ModBlocks.SIFT_PORTAL_ITEM);
            output.accept(SIFT_RIFT);
            output.accept(SINGER_SPAWN_EGG);
            output.accept(ECHO_GOLEM_SPAWN_EGG);
            output.accept(DARK_SNIFFER_SPAWN_EGG);
            output.accept(BlubItems.BLUB_SPAWN_EGG);
            output.accept(SIFTER_SPAWN_EGG);
         }
      )
      .build();

   private ModItems() {
   }

   public static void initialize() {
      IchorBottleItem.registerInteractions();
      CompostingChanceRegistry.INSTANCE.add(SCULKFLOWER_SEEDS, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(ModBlocks.SCULKFLOWER_ITEM, 0.65F);
      Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_TAB_KEY, CREATIVE_TAB);
   }

   private static ItemStack sonorousStack(SonorousDeepslateBlock.Mode mode) {
      ItemStack stack = new ItemStack(ModBlocks.SONOROUS_DEEPSLATE_ITEM);
      CompoundTag state = new CompoundTag();
      state.putString(SonorousDeepslateBlock.MODE.getName(), mode.getSerializedName());
      stack.getOrCreateTag().put("BlockStateTag", state);
      return stack;
   }

   private static Item registerItem(String name, Properties properties) {
      return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("the_sift", name), new Item(properties));
   }

   private static <T extends Item> T registerItem(String name, Function<Properties, T> factory, Properties properties) {
      return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("the_sift", name), factory.apply(properties));
   }
}
