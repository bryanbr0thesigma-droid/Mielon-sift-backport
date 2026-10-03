package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import mielon.thesift.client.render.SiftRenderTypes;
import mielon.thesift.entity.RiftEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public final class RiftRenderer extends EntityRenderer<RiftEntity> {
   private static final RiftMesh OPEN = new RiftMesh(RiftAssetModel.INSTANCE.boxes(1.0F));
   private static final ResourceLocation TEXTURE = new ResourceLocation("the_sift", "textures/entity/rift/sift.png");

   public RiftRenderer(Context context) {
      super(context);
      this.shadowRadius = 0.0F;
   }

   @Override
   public void render(RiftEntity rift, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      float openScale = rift.getOpenScale(partialTicks);
      if (!(openScale <= 0.0F)) {
         RiftMesh mesh = openScale >= 1.0F ? OPEN : new RiftMesh(RiftAssetModel.INSTANCE.boxes(openScale));
         boolean alongX = rift.isLongAlongX();
         var pose = poseStack.last().pose();
         mesh.draw(bufferSource.getBuffer(rift.targetsSift() ? SiftRenderTypes.RIFT_SIFT : SiftRenderTypes.RIFT_OVERWORLD), pose, alongX, false);
         mesh.draw(bufferSource.getBuffer(SiftRenderTypes.RIFT_GLOW), pose, alongX, true);
      }
   }

   @Override
   public boolean shouldRender(RiftEntity rift, net.minecraft.client.renderer.culling.Frustum frustum, double camX, double camY, double camZ) {
      return frustum.isVisible(rift.getPortalBounds().inflate(4.0));
   }

   @Override
   public ResourceLocation getTextureLocation(RiftEntity rift) {
      return TEXTURE;
   }
}
