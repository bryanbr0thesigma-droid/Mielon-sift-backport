package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mielon.thesift.entity.BlubEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class BlubRenderer extends GeoEntityRenderer<BlubEntity> {
   private static final float DEGREES = (float) (Math.PI / 180.0);

   public BlubRenderer(Context context) {
      super(context, new BlubModel());
      this.shadowRadius = 0.38F;
   }

   @Override
   public void renderRecursively(
      PoseStack poseStack,
      BlubEntity blub,
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
      float rotX = 0.0F;
      float dy = 0.0F;
      float dz = 0.0F;
      boolean sitting = blub.isBlubSitting();
      switch (bone.getName()) {
         case "head" -> {
            if (sitting) {
               dy = -3.25F;
               dz = 0.65F;
               rotX = 17.5F * DEGREES;
            }
         }
         case "front_left_leg", "front_right_leg" -> {
            if (sitting) {
               rotX = 42.0F * DEGREES;
               dz = -0.55F;
            }
         }
         case "back_left_leg", "back_right_leg" -> {
            if (sitting) {
               rotX = -68.0F * DEGREES;
               dy = 0.55F;
               dz = 0.75F;
            }
         }
         case "left_ear", "right_ear" -> {
            float healthRatio = Mth.clamp(blub.getHealth() / blub.getMaxHealth(), 0.0F, 1.0F);
            rotX = (1.0F - healthRatio) * 72.0F * DEGREES;
         }
         default -> {
         }
      }

      if (rotX == 0.0F && dy == 0.0F && dz == 0.0F) {
         super.renderRecursively(poseStack, blub, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
      } else {
         BoneAdjust.begin(poseStack, bone, rotX, 0.0F, 0.0F, 0.0F, dy, dz);
         super.renderRecursively(poseStack, blub, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
         poseStack.popPose();
      }
   }
}
