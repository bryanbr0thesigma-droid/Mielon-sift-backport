package mielon.thesift.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mielon.thesift.block.entity.SonorousDeepslateBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/** The coloured beam rising from an activated sonorous deepslate block. */
public class SonorousBeamRenderer implements BlockEntityRenderer<SonorousDeepslateBlockEntity> {
   private static final float MAX_BEAM_HEIGHT = 128.0F;
   private static final float BEAM_MIN = 0.35F;
   private static final float BEAM_MAX = 0.65F;
   private static final float BEAM_ALPHA = 0.55F;
   private static final float SCROLL_TILES_PER_SECOND = 1.0F;

   public SonorousBeamRenderer(Context context) {
   }

   @Override
   public boolean shouldRenderOffScreen(SonorousDeepslateBlockEntity blockEntity) {
      return blockEntity.hasBeam();
   }

   @Override
   public int getViewDistance() {
      return 256;
   }

   @Override
   public void render(
      SonorousDeepslateBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay
   ) {
      if (!blockEntity.hasBeam()) {
         return;
      }

      float growth = blockEntity.getBeamGrowth(partialTick);
      if (growth <= 0.0F) {
         return;
      }

      int colorRGB = blockEntity.getBeamColor();
      float red = (colorRGB >> 16 & 0xFF) / 255.0F;
      float green = (colorRGB >> 8 & 0xFF) / 255.0F;
      float blue = (colorRGB & 0xFF) / 255.0F;
      float bottomY = 1.0F;
      float topY = bottomY + MAX_BEAM_HEIGHT * growth;
      float beamHeight = topY - bottomY;
      long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
      float time = (float)gameTime + partialTick;
      float vScroll = -(time / 20.0F) * SCROLL_TILES_PER_SECOND;
      PoseStack.Pose pose = poseStack.last();
      renderBeamColumn(
         bufferSource.getBuffer(SiftRenderTypes.SONOROUS_BEAM), pose, BEAM_MIN, bottomY, BEAM_MIN, BEAM_MAX, topY, BEAM_MAX, red, green, blue, vScroll, vScroll + beamHeight * 0.5F
      );

      if (blockEntity.getLevel() == null) {
         return;
      }

      VertexConsumer overlay = null;
      int openSegment = -1;
      for (int offset = 1; offset <= 128 && !(offset >= topY); offset++) {
         Fluid fluid = blockEntity.getLevel().getFluidState(blockEntity.getBlockPos().above(offset)).getType();
         boolean water = fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER;
         if (water) {
            if (openSegment < 0) {
               openSegment = offset;
            }
         } else if (openSegment >= 0) {
            if (overlay == null) {
               overlay = bufferSource.getBuffer(SiftRenderTypes.SONOROUS_BEAM_WATER_OVERLAY);
            }

            waterOverlay(overlay, pose, openSegment, Math.min((float)offset, topY), topY, red, green, blue, vScroll);
            openSegment = -1;
         }
      }

      if (openSegment >= 0) {
         if (overlay == null) {
            overlay = bufferSource.getBuffer(SiftRenderTypes.SONOROUS_BEAM_WATER_OVERLAY);
         }

         waterOverlay(overlay, pose, openSegment, topY, topY, red, green, blue, vScroll);
      }
   }

   private static void waterOverlay(VertexConsumer buffer, PoseStack.Pose pose, float minY, float maxY, float beamTopY, float red, float green, float blue, float vScroll) {
      if (maxY <= minY) {
         return;
      }

      float vBottom = vScroll + (beamTopY - minY) * 0.5F;
      float vTop = vScroll + (beamTopY - maxY) * 0.5F;
      renderBeamColumn(buffer, pose, BEAM_MIN, minY, BEAM_MIN, BEAM_MAX, maxY, BEAM_MAX, red, green, blue, vTop, vBottom);
   }

   private static void renderBeamColumn(
      VertexConsumer buffer,
      PoseStack.Pose pose,
      float minX,
      float minY,
      float minZ,
      float maxX,
      float maxY,
      float maxZ,
      float red,
      float green,
      float blue,
      float v0,
      float v1
   ) {
      Matrix4f m = pose.pose();
      Matrix3f n = pose.normal();
      vertex(buffer, m, n, minX, minY, maxZ, 0.0F, v1, red, green, blue, 0.0F, 0.0F, 1.0F);
      vertex(buffer, m, n, maxX, minY, maxZ, 1.0F, v1, red, green, blue, 0.0F, 0.0F, 1.0F);
      vertex(buffer, m, n, maxX, maxY, maxZ, 1.0F, v0, red, green, blue, 0.0F, 0.0F, 1.0F);
      vertex(buffer, m, n, minX, maxY, maxZ, 0.0F, v0, red, green, blue, 0.0F, 0.0F, 1.0F);
      vertex(buffer, m, n, maxX, minY, minZ, 0.0F, v1, red, green, blue, 0.0F, 0.0F, -1.0F);
      vertex(buffer, m, n, minX, minY, minZ, 1.0F, v1, red, green, blue, 0.0F, 0.0F, -1.0F);
      vertex(buffer, m, n, minX, maxY, minZ, 1.0F, v0, red, green, blue, 0.0F, 0.0F, -1.0F);
      vertex(buffer, m, n, maxX, maxY, minZ, 0.0F, v0, red, green, blue, 0.0F, 0.0F, -1.0F);
      vertex(buffer, m, n, minX, minY, minZ, 0.0F, v1, red, green, blue, -1.0F, 0.0F, 0.0F);
      vertex(buffer, m, n, minX, minY, maxZ, 1.0F, v1, red, green, blue, -1.0F, 0.0F, 0.0F);
      vertex(buffer, m, n, minX, maxY, maxZ, 1.0F, v0, red, green, blue, -1.0F, 0.0F, 0.0F);
      vertex(buffer, m, n, minX, maxY, minZ, 0.0F, v0, red, green, blue, -1.0F, 0.0F, 0.0F);
      vertex(buffer, m, n, maxX, minY, maxZ, 0.0F, v1, red, green, blue, 1.0F, 0.0F, 0.0F);
      vertex(buffer, m, n, maxX, minY, minZ, 1.0F, v1, red, green, blue, 1.0F, 0.0F, 0.0F);
      vertex(buffer, m, n, maxX, maxY, minZ, 1.0F, v0, red, green, blue, 1.0F, 0.0F, 0.0F);
      vertex(buffer, m, n, maxX, maxY, maxZ, 0.0F, v0, red, green, blue, 1.0F, 0.0F, 0.0F);
   }

   private static void vertex(
      VertexConsumer buffer, Matrix4f m, Matrix3f n, float x, float y, float z, float u, float v, float red, float green, float blue, float nx, float ny, float nz
   ) {
      buffer.vertex(m, x, y, z)
         .color(red, green, blue, BEAM_ALPHA)
         .uv(u, v)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(15728880)
         .normal(n, nx, ny, nz)
         .endVertex();
   }
}
