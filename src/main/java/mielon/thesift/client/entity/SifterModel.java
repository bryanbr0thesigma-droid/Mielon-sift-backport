package mielon.thesift.client.entity;

import mielon.thesift.entity.SifterEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public final class SifterModel extends GeoModel<SifterEntity> {
   @Override
   public ResourceLocation getModelResource(SifterEntity animatable) {
      return new ResourceLocation("the_sift", "geo/entity/sifter.geo.json");
   }

   @Override
   public ResourceLocation getTextureResource(SifterEntity animatable) {
      return new ResourceLocation("the_sift", "textures/entity/sifter.png");
   }

   @Override
   public ResourceLocation getAnimationResource(SifterEntity animatable) {
      return new ResourceLocation("the_sift", "animations/entity/sifter.animation.json");
   }
}
