package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import org.joml.Quaternionf;

/** Renders the overgrown willow boats (vanilla boat rendering with our own texture and model layer). */
public final class SiftBoatRenderer extends EntityRenderer<Boat> {
   private final ResourceLocation texture;
   private final ListModel<Boat> model;

   public SiftBoatRenderer(EntityRendererProvider.Context context, ModelLayerLocation layer, boolean chest) {
      super(context);
      this.shadowRadius = 0.8F;
      this.texture = new ResourceLocation("the_sift", (chest ? "textures/entity/chest_boat/" : "textures/entity/boat/") + "overgrown_willow.png");
      this.model = chest ? new ChestBoatModel(context.bakeLayer(layer)) : new BoatModel(context.bakeLayer(layer));
   }

   @Override
   public void render(Boat boat, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      poseStack.pushPose();
      poseStack.translate(0.0F, 0.375F, 0.0F);
      poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
      float hurt = boat.getHurtTime() - partialTicks;
      float damage = boat.getDamage() - partialTicks;
      if (damage < 0.0F) {
         damage = 0.0F;
      }

      if (hurt > 0.0F) {
         poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(hurt) * hurt * damage / 10.0F * boat.getHurtDir()));
      }

      float bubble = boat.getBubbleAngle(partialTicks);
      if (!Mth.equal(bubble, 0.0F)) {
         poseStack.mulPose(new Quaternionf().setAngleAxis(boat.getBubbleAngle(partialTicks) * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
      }

      poseStack.scale(-1.0F, -1.0F, 1.0F);
      poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
      this.model.setupAnim(boat, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
      VertexConsumer vertexConsumer = bufferSource.getBuffer(this.model.renderType(this.texture));
      this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      if (!boat.isUnderWater()) {
         VertexConsumer waterMask = bufferSource.getBuffer(RenderType.waterMask());
         if (this.model instanceof WaterPatchModel waterPatchModel) {
            waterPatchModel.waterPatch().render(poseStack, waterMask, packedLight, OverlayTexture.NO_OVERLAY);
         }
      }

      poseStack.popPose();
      super.render(boat, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
   }

   @Override
   public ResourceLocation getTextureLocation(Boat boat) {
      return this.texture;
   }
}
