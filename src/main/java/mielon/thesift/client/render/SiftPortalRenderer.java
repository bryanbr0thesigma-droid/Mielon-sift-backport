package mielon.thesift.client.render;

import mielon.thesift.block.entity.SiftPortalBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;

/** End-portal style cube rendered with The Sift's procedural portal shader. */
public class SiftPortalRenderer extends TheEndPortalRenderer<SiftPortalBlockEntity> {
   public SiftPortalRenderer(BlockEntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   protected RenderType renderType() {
      return SiftRenderTypes.SIFT_PORTAL;
   }

   @Override
   public int getViewDistance() {
      return 512;
   }
}
