package mielon.thesift.client.mixin;

import mielon.thesift.client.render.SiftSpecialEffects;
import mielon.thesift.world.SiftDayNightCycle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientLevel.class})
public abstract class ClientLevelSkyMixin {
   private boolean theSift$isSift() {
      return SiftDayNightCycle.EFFECTS_ID.equals(((ClientLevel)(Object)this).dimensionType().effectsLocation());
   }

   private Vec3 theSift$weather(Vec3 color, float partialTick) {
      ClientLevel level = (ClientLevel)(Object)this;
      float rain = level.getRainLevel(partialTick);
      if (rain > 0.0F) {
         double gray = (color.x * 0.3 + color.y * 0.59 + color.z * 0.11) * 0.6;
         double keep = 1.0 - rain * 0.75;
         color = new Vec3(color.x * keep + gray * (1.0 - keep), color.y * keep + gray * (1.0 - keep), color.z * keep + gray * (1.0 - keep));
      }
      float thunder = level.getThunderLevel(partialTick);
      if (thunder > 0.0F) {
         double gray = (color.x * 0.3 + color.y * 0.59 + color.z * 0.11) * 0.2;
         double keep = 1.0 - thunder * 0.75;
         color = new Vec3(color.x * keep + gray * (1.0 - keep), color.y * keep + gray * (1.0 - keep), color.z * keep + gray * (1.0 - keep));
      }
      return color;
   }

   @Inject(method = {"getSkyColor"}, at = {@At("HEAD")}, cancellable = true)
   private void theSift$skyColor(Vec3 pos, float partialTick, CallbackInfoReturnable<Vec3> cir) {
      if (this.theSift$isSift()) {
         cir.setReturnValue(this.theSift$weather(SiftSpecialEffects.blend(SiftSpecialEffects.DAY_SKY, SiftSpecialEffects.NIGHT_SKY), partialTick));
      }
   }

   @Inject(method = {"getCloudColor"}, at = {@At("HEAD")}, cancellable = true)
   private void theSift$cloudColor(float partialTick, CallbackInfoReturnable<Vec3> cir) {
      if (this.theSift$isSift()) {
         cir.setReturnValue(this.theSift$weather(SiftSpecialEffects.blend(SiftSpecialEffects.DAY_CLOUD, SiftSpecialEffects.NIGHT_CLOUD), partialTick));
      }
   }

   @Inject(method = {"getStarBrightness"}, at = {@At("HEAD")}, cancellable = true)
   private void theSift$noStars(float partialTick, CallbackInfoReturnable<Float> cir) {
      if (this.theSift$isSift()) {
         cir.setReturnValue(0.0F);
      }
   }
}
