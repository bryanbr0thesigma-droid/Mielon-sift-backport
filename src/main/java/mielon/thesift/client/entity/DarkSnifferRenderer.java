package mielon.thesift.client.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.SnifferRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.sniffer.Sniffer;

public final class DarkSnifferRenderer extends SnifferRenderer {
   private static final ResourceLocation TEXTURE = new ResourceLocation("the_sift", "textures/entity/dark_sniffer.png");

   public DarkSnifferRenderer(Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(Sniffer sniffer) {
      return TEXTURE;
   }
}
