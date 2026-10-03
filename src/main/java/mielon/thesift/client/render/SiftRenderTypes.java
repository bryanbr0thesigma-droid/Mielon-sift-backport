package mielon.thesift.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

/** Custom render types and shader registration. Extends RenderType to reach its protected render state shards. */
public final class SiftRenderTypes extends RenderType {
   private static ShaderInstance portalShader;
   private static ShaderInstance riftShader;
   private static final ShaderStateShard PORTAL_SHADER = new ShaderStateShard(() -> portalShader);
   private static final ShaderStateShard RIFT_SHADER = new ShaderStateShard(() -> riftShader);
   public static final RenderType SIFT_PORTAL = create(
      "the_sift_portal", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder().setShaderState(PORTAL_SHADER).createCompositeState(false)
   );
   private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("the_sift", "textures/block/sonorous_deepslate_beam.png");
   public static final RenderType SONOROUS_BEAM = create(
      "the_sift_sonorous_beam",
      DefaultVertexFormat.BLOCK,
      VertexFormat.Mode.QUADS,
      256,
      false,
      true,
      CompositeState.builder()
         .setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
         .setTextureState(new TextureStateShard(BEAM_TEXTURE, false, false))
         .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
         .setCullState(NO_CULL)
         .createCompositeState(false)
   );
   public static final RenderType SONOROUS_BEAM_WATER_OVERLAY = create(
      "the_sift_sonorous_beam_water_overlay",
      DefaultVertexFormat.BLOCK,
      VertexFormat.Mode.QUADS,
      256,
      false,
      true,
      CompositeState.builder()
         .setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
         .setTextureState(new TextureStateShard(BEAM_TEXTURE, false, false))
         .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
         .setDepthTestState(NO_DEPTH_TEST)
         .setWriteMaskState(COLOR_WRITE)
         .setCullState(NO_CULL)
         .createCompositeState(false)
   );
   public static final RenderType RIFT_SIFT = riftSurface("textures/entity/rift/sift.png");
   public static final RenderType RIFT_OVERWORLD = riftSurface("textures/entity/rift/overworld.png");
   public static final RenderType RIFT_GLOW = create(
      "the_sift_rift_glow",
      DefaultVertexFormat.POSITION_TEX_COLOR,
      VertexFormat.Mode.QUADS,
      256,
      false,
      true,
      CompositeState.builder()
         .setShaderState(RIFT_SHADER)
         .setTextureState(new TextureStateShard(new ResourceLocation("the_sift", "textures/entity/rift/sift.png"), false, false))
         .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
         .setDepthTestState(LEQUAL_DEPTH_TEST)
         .setWriteMaskState(COLOR_WRITE)
         .createCompositeState(false)
   );

   private SiftRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setup, Runnable clear) {
      super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setup, clear);
   }

   private static RenderType riftSurface(String texture) {
      return create(
         "the_sift_rift_" + texture.hashCode(),
         DefaultVertexFormat.POSITION_TEX_COLOR,
         VertexFormat.Mode.QUADS,
         256,
         false,
         false,
         CompositeState.builder()
            .setShaderState(RIFT_SHADER)
            .setTextureState(new TextureStateShard(new ResourceLocation("the_sift", texture), false, false))
            .setCullState(CULL)
            .createCompositeState(false)
      );
   }

   public static void registerShaders() {
      CoreShaderRegistrationCallback.EVENT.register(context -> {
         context.register(new ResourceLocation("the_sift", "sift_portal"), DefaultVertexFormat.POSITION, shader -> portalShader = shader);
         context.register(new ResourceLocation("the_sift", "rift"), DefaultVertexFormat.POSITION_TEX_COLOR, shader -> riftShader = shader);
      });
   }
}
