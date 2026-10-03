package mielon.thesift.client;

import mielon.thesift.client.entity.BlubRenderer;
import mielon.thesift.client.entity.DarkSnifferRenderer;
import mielon.thesift.client.entity.EchoGolemRenderer;
import mielon.thesift.client.entity.SiftBoatRenderer;
import mielon.thesift.client.entity.SifterRenderer;
import mielon.thesift.client.entity.SingerRenderer;
import mielon.thesift.client.entity.MiniRiftRenderer;
import mielon.thesift.client.entity.RiftRenderer;
import mielon.thesift.client.render.SiftPortalRenderer;
import mielon.thesift.client.render.SonorousBeamRenderer;
import mielon.thesift.client.render.SiftRenderTypes;
import mielon.thesift.client.particle.IchorFluidParticle;
import mielon.thesift.client.particle.IchorSurfaceParticle;
import mielon.thesift.client.particle.SiftNoteParticle;
import mielon.thesift.client.particle.SiftParallaxParticle;
import mielon.thesift.client.particle.SingerSoundWaveParticle;
import mielon.thesift.client.particle.SoulParticle;
import mielon.thesift.client.particle.SoundWaveParticle;
import mielon.thesift.block.ModBlocks;
import mielon.thesift.block.SonorousDeepslateBlock;
import mielon.thesift.block.SonorousDeepslateBlockItem;
import mielon.thesift.entity.ModEntities;
import mielon.thesift.fluid.ModFluids;
import mielon.thesift.item.ModItems;
import mielon.thesift.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public final class TheSiftClient implements ClientModInitializer {
   private static final ModelLayerLocation OVERGROWN_WILLOW_BOAT_LAYER = new ModelLayerLocation(new ResourceLocation("the_sift", "boat/overgrown_willow"), "main");
   private static final ModelLayerLocation OVERGROWN_WILLOW_CHEST_BOAT_LAYER = new ModelLayerLocation(
      new ResourceLocation("the_sift", "chest_boat/overgrown_willow"), "main"
   );

   @Override
   public void onInitializeClient() {
      SiftRenderTypes.registerShaders();
      registerRenderLayers();
      BlockEntityRenderers.register(ModBlocks.SIFT_PORTAL_BLOCK_ENTITY, SiftPortalRenderer::new);
      BlockEntityRenderers.register(ModBlocks.SONOROUS_DEEPSLATE_BLOCK_ENTITY, SonorousBeamRenderer::new);
      EntityRendererRegistry.register(ModEntities.RIFT, RiftRenderer::new);
      EntityRendererRegistry.register(ModEntities.MINI_RIFT, MiniRiftRenderer::new);
      EntityRendererRegistry.register(ModEntities.SIFTITE_RETURN, context -> new ThrownItemRenderer<>(context, 1.0F, true));
      registerFluids();
      registerItemPredicates();
      EntityModelLayerRegistry.registerModelLayer(OVERGROWN_WILLOW_BOAT_LAYER, BoatModel::createBodyModel);
      EntityModelLayerRegistry.registerModelLayer(OVERGROWN_WILLOW_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
      EntityRendererRegistry.register(ModEntities.OVERGROWN_WILLOW_BOAT, context -> new SiftBoatRenderer(context, OVERGROWN_WILLOW_BOAT_LAYER, false));
      EntityRendererRegistry.register(ModEntities.OVERGROWN_WILLOW_CHEST_BOAT, context -> new SiftBoatRenderer(context, OVERGROWN_WILLOW_CHEST_BOAT_LAYER, true));
      EntityRendererRegistry.register(ModEntities.SINGER, SingerRenderer::new);
      EntityRendererRegistry.register(ModEntities.ECHO_GOLEM, EchoGolemRenderer::new);
      EntityRendererRegistry.register(ModEntities.DARK_SNIFFER, DarkSnifferRenderer::new);
      EntityRendererRegistry.register(ModEntities.BLUB, BlubRenderer::new);
      EntityRendererRegistry.register(ModEntities.SIFTER, SifterRenderer::new);
      ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
      registry.register(ModParticles.SIFT_PARALLAX, SiftParallaxParticle.Provider::new);
      registry.register(ModParticles.SIFT_NOTE, SiftNoteParticle.Provider::new);
      registry.register(ModParticles.SOUND_WAVE, SoundWaveParticle.Provider::new);
      registry.register(ModParticles.SINGER_SOUND_WAVE, SingerSoundWaveParticle.Provider::new);
      registry.register(ModParticles.CANYON_SOUL, SoulParticle.RiseProvider::new);
      registry.register(ModParticles.SOUL_FRAGMENT, SoulParticle.FragmentProvider::new);
      registry.register(ModParticles.ICHOR_SURFACE_MIST, IchorSurfaceParticle.Provider::new);
      registry.register(ModParticles.ICHOR_BUBBLE, IchorFluidParticle.BubbleProvider::new);
      registry.register(ModParticles.ICHOR_SPLASH, IchorFluidParticle.SplashProvider::new);
      registry.register(ModParticles.ICHOR_RAIN_SPLASH, IchorFluidParticle.RainSplashProvider::new);
   }

   private static void registerRenderLayers() {
      BlockRenderLayerMap map = BlockRenderLayerMap.INSTANCE;
      Block[] cutout = new Block[]{
         ModBlocks.OVERGROWN_CHARD,
         ModBlocks.OVERGROWN_STALKS,
         ModBlocks.OVERGROWN_FRONDS,
         ModBlocks.OVERGROWN_LOTUS,
         ModBlocks.SUNBURST_PLANT,
         ModBlocks.WHISPERBLOOM,
         ModBlocks.SIFTSLATE_STALKS,
         ModBlocks.HEALTHY_SCULK_SPROUTS,
         ModBlocks.DRY_HEALTHY_SCULK_SPROUTS,
         ModBlocks.SIFTSLATE_HANGING_ROOTS,
         ModBlocks.OVERGROWN_HANGING_ROOTS,
         ModBlocks.OVERGROWN_WILLOW_SAPLING,
         ModBlocks.OVERGROWN_WILLOW_VINES,
         ModBlocks.OVERGROWN_WILLOW_VINES_PLANT,
         ModBlocks.OVERGROWN_WILLOW_DOOR,
         ModBlocks.OVERGROWN_WILLOW_TRAPDOOR,
         ModBlocks.SCULKFLOWER,
         ModBlocks.SCULKFLOWER_CROP,
         ModBlocks.ICHOR_CAULDRON
      };
      map.putBlocks(RenderType.cutout(), cutout);
      map.putBlock(ModBlocks.OVERGROWN_WILLOW_FOLIAGE, RenderType.cutoutMipped());
      map.putBlocks(RenderType.translucent(), ModBlocks.ICHOR_GLASS, ModBlocks.ICHOR_GLASS_PANE);
      map.putFluids(RenderType.translucent(), ModFluids.ICHOR, ModFluids.FLOWING_ICHOR);
   }

   private static void registerFluids() {
      FluidRenderHandlerRegistry.INSTANCE
         .register(
            ModFluids.ICHOR,
            ModFluids.FLOWING_ICHOR,
            new SimpleFluidRenderHandler(
               new ResourceLocation("the_sift", "block/ichor_still"),
               new ResourceLocation("the_sift", "block/ichor_flow"),
               new ResourceLocation("minecraft", "block/water_overlay"),
               0xFFFFFF
            )
         );
   }

   private static void registerItemPredicates() {
      FabricModelPredicateProviderRegistry.register(
         ModBlocks.SONOROUS_DEEPSLATE_ITEM,
         new ResourceLocation("the_sift", "note"),
         (stack, level, entity, seed) -> SonorousDeepslateBlockItem.modeOf(stack) == SonorousDeepslateBlock.Mode.NOTE ? 1.0F : 0.0F
      );
   }
}
