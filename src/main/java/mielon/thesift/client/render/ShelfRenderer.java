package mielon.thesift.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mielon.thesift.block.OvergrownShelfBlock;
import mielon.thesift.block.entity.ShelfBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity> {
   public ShelfRenderer(BlockEntityRendererProvider.Context context) {
   }

   @Override
   public void render(ShelfBlockEntity shelf, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
      if (shelf.getLevel() == null) {
         return;
      }
      Direction facing = shelf.getBlockState().getValue(OvergrownShelfBlock.FACING);
      Direction right = facing.getCounterClockWise();
      int packedLight = LevelRenderer.getLightColor(shelf.getLevel(), shelf.getBlockPos().relative(facing));
      for (int slot = 0; slot < 3; slot++) {
         ItemStack stack = shelf.getItem(slot);
         if (stack.isEmpty()) {
            continue;
         }
         double offset = (slot - 1) * 5.0 / 16.0;
         poseStack.pushPose();
         poseStack.translate(
            0.5 - facing.getStepX() * 0.12 + right.getStepX() * offset,
            3.0 / 16.0 + 0.25,
            0.5 - facing.getStepZ() * 0.12 + right.getStepZ() * offset
         );
         poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - facing.toYRot()));
         poseStack.scale(0.45F, 0.45F, 0.45F);
         Minecraft.getInstance().getItemRenderer().renderStatic(
            stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffers, shelf.getLevel(), slot
         );
         poseStack.popPose();
      }
   }
}
