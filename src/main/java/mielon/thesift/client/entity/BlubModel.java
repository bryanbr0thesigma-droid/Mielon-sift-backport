package mielon.thesift.client.entity;

import mielon.thesift.entity.BlubEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public final class BlubModel extends GeoModel<BlubEntity> {
   private static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation("the_sift", "textures/entity/blub.png");
   private static final ResourceLocation TAMED_TEXTURE = new ResourceLocation("the_sift", "textures/entity/tamed_blub.png");
   private static final ResourceLocation MIELON_TEXTURE = new ResourceLocation("the_sift", "textures/entity/blub_mielon.png");
   private static final ResourceLocation MIELON_TAMED_TEXTURE = new ResourceLocation("the_sift", "textures/entity/blub_mielon_tamed.png");

   @Override
   public ResourceLocation getModelResource(BlubEntity blub) {
      return new ResourceLocation("the_sift", "geo/entity/blub.geo.json");
   }

   @Override
   public ResourceLocation getTextureResource(BlubEntity blub) {
      if (isMielon(blub)) {
         return blub.isTame() ? MIELON_TAMED_TEXTURE : MIELON_TEXTURE;
      } else {
         return blub.isTame() ? TAMED_TEXTURE : NORMAL_TEXTURE;
      }
   }

   private static boolean isMielon(BlubEntity blub) {
      if (blub != null && blub.getCustomName() != null) {
         String name = blub.getCustomName().getString();
         return "mielon".equals(name) || "Mielon".equals(name) || "MIELON".equals(name);
      } else {
         return false;
      }
   }

   @Override
   public ResourceLocation getAnimationResource(BlubEntity blub) {
      return new ResourceLocation("the_sift", "animations/entity/blub.animation.json");
   }
}
