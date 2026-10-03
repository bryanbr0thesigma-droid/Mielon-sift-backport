package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import mielon.thesift.client.render.SiftRenderTypes;
import mielon.thesift.entity.MiniRiftEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public final class MiniRiftRenderer extends EntityRenderer<MiniRiftEntity> {
   private static final RiftMesh CUBE = new RiftMesh(List.of(new RiftAssetModel.Box(-0.4F, -0.4F, -0.4F, 0.4F, 0.4F, 0.4F)));
   private static final ResourceLocation TEXTURE = new ResourceLocation("the_sift", "textures/entity/rift/sift.png");

   public MiniRiftRenderer(Context context) {
      super(context);
      this.shadowRadius = 0.0F;
   }

   @Override
   public void render(MiniRiftEntity rift, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      float open = rift.getOpenScale(partialTicks);
      if (!(open <= 0.0F)) {
         poseStack.pushPose();
         float size = open * open * (3.0F - 2.0F * open);
         poseStack.scale(size, size, size);
         var pose = poseStack.last().pose();
         CUBE.draw(bufferSource.getBuffer(SiftRenderTypes.RIFT_SIFT), pose, true, false);
         CUBE.draw(bufferSource.getBuffer(SiftRenderTypes.RIFT_GLOW), pose, true, true);
         poseStack.popPose();
      }
   }

   @Override
   public boolean shouldRender(MiniRiftEntity rift, Frustum frustum, double camX, double camY, double camZ) {
      return frustum.isVisible(rift.getBoundingBox().inflate(1.0));
   }

   @Override
   public ResourceLocation getTextureLocation(MiniRiftEntity rift) {
      return TEXTURE;
   }
}
