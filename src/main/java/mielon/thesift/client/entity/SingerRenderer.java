package mielon.thesift.client.entity;

import mielon.thesift.entity.SingerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SingerRenderer extends GeoEntityRenderer<SingerEntity> {
   public SingerRenderer(Context context) {
      super(context, new SingerModel());
      this.addRenderLayer(new SingerSoulBlockLayer(this));
      this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
      this.shadowRadius = 0.0F;
      this.withScale(1.2F);
   }
}
