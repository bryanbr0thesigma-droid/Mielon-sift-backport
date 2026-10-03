package mielon.thesift.client.entity;

import mielon.thesift.entity.EchoGolemEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public final class EchoGolemModel extends GeoModel<EchoGolemEntity> {
   @Override
   public ResourceLocation getModelResource(EchoGolemEntity animatable) {
      return new ResourceLocation("the_sift", "geo/entity/echo_golem.geo.json");
   }

   @Override
   public ResourceLocation getTextureResource(EchoGolemEntity animatable) {
      return new ResourceLocation("the_sift", "textures/entity/echo_golem.png");
   }

   @Override
   public ResourceLocation getAnimationResource(EchoGolemEntity animatable) {
      return new ResourceLocation("the_sift", "animations/entity/echo_golem.animation.json");
   }
}
