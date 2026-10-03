package mielon.thesift.client.entity;

import mielon.thesift.entity.SingerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SingerModel extends GeoModel<SingerEntity> {
   @Override
   public ResourceLocation getModelResource(SingerEntity animatable) {
      return new ResourceLocation("the_sift", "geo/entity/singer.geo.json");
   }

   @Override
   public ResourceLocation getTextureResource(SingerEntity animatable) {
      return new ResourceLocation("the_sift", "textures/entity/singer.png");
   }

   @Override
   public ResourceLocation getAnimationResource(SingerEntity animatable) {
      return new ResourceLocation("the_sift", "animations/entity/singer.animation.json");
   }
}
