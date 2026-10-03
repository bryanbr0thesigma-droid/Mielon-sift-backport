package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import mielon.thesift.block.ModBlocks;
import mielon.thesift.entity.SingerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

/** Renders the soul block the Singer holds during barter and soul exchange. */
public final class SingerSoulBlockLayer extends BlockAndItemGeoLayer<SingerEntity> {
   public SingerSoulBlockLayer(GeoRenderer<SingerEntity> renderer) {
      super(renderer);
   }

   @Override
   protected BlockState getBlockForBone(GeoBone bone, SingerEntity singer) {
      return "held_soul".equals(bone.getName()) && singer.isHoldingSoulBlock() ? ModBlocks.SOUL_BLOCK.defaultBlockState() : null;
   }

   @Override
   protected void renderBlockForBone(
      PoseStack poseStack, GeoBone bone, BlockState state, SingerEntity singer, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay
   ) {
      poseStack.pushPose();
      poseStack.scale(1.75F, 1.75F, 1.75F);
      super.renderBlockForBone(poseStack, bone, state, singer, bufferSource, partialTick, 15728880, packedOverlay);
      poseStack.popPose();
   }
}
