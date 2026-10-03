package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.util.RenderUtils;

/** Applies an extra rotation/translation to a GeckoLib bone (and its children) at render time. */
final class BoneAdjust {
   private BoneAdjust() {
   }

   /** Pushes a pose and applies the adjustment; callers must {@code poseStack.popPose()} afterwards. */
   static void begin(PoseStack poseStack, GeoBone bone, float rotX, float rotY, float rotZ, float dx, float dy, float dz) {
      poseStack.pushPose();
      if (dx != 0.0F || dy != 0.0F || dz != 0.0F) {
         poseStack.translate(-dx / 16.0F, dy / 16.0F, dz / 16.0F);
      }

      RenderUtils.translateToPivotPoint(poseStack, bone);
      if (rotZ != 0.0F) {
         poseStack.mulPose(Axis.ZP.rotation(rotZ));
      }

      if (rotY != 0.0F) {
         poseStack.mulPose(Axis.YP.rotation(rotY));
      }

      if (rotX != 0.0F) {
         poseStack.mulPose(Axis.XP.rotation(rotX));
      }

      RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
   }
}
