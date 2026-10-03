package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mielon.thesift.entity.EchoGolemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class EchoGolemRenderer extends GeoEntityRenderer<EchoGolemEntity> {
   public EchoGolemRenderer(Context context) {
      super(context, new EchoGolemModel());
      this.shadowRadius = 0.72F;
   }

   @Override
   public void renderRecursively(
      PoseStack poseStack,
      EchoGolemEntity golem,
      GeoBone bone,
      RenderType renderType,
      MultiBufferSource bufferSource,
      VertexConsumer buffer,
      boolean isReRender,
      float partialTick,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      if (bone.getName().equals("soul_block") && !golem.hasSoulBlock()) {
         return;
      }

      if (bone.getName().equals("head")) {
         float headYaw = Mth.rotLerp(partialTick, golem.yHeadRotO, golem.getYHeadRot());
         float bodyYaw = Mth.rotLerp(partialTick, golem.yBodyRotO, golem.yBodyRot);
         float relativeYaw = Mth.clamp(Mth.wrapDegrees(headYaw - bodyYaw), -55.0F, 55.0F);
         BoneAdjust.begin(poseStack, bone, 0.0F, -relativeYaw * (float) (Math.PI / 180.0), 0.0F, 0.0F, 0.0F, 0.0F);
         super.renderRecursively(poseStack, golem, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
         poseStack.popPose();
      } else {
         super.renderRecursively(poseStack, golem, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
      }
   }
}
