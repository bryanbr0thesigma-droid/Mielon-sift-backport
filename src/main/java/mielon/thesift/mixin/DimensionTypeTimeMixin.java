package mielon.thesift.mixin;

import mielon.thesift.world.SiftDayNightCycle;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({DimensionType.class})
public abstract class DimensionTypeTimeMixin {
   @Inject(method = {"timeOfDay"}, at = {@At("HEAD")}, cancellable = true)
   private void theSift$ownTimeOfDay(long dayTime, CallbackInfoReturnable<Float> cir) {
      if (SiftDayNightCycle.EFFECTS_ID.equals(((DimensionType)(Object)this).effectsLocation())) {
         cir.setReturnValue(SiftDayNightCycle.timeOfDay(dayTime));
      }
   }

   @Inject(method = {"moonPhase"}, at = {@At("HEAD")}, cancellable = true)
   private void theSift$noMoonPhase(long dayTime, CallbackInfoReturnable<Integer> cir) {
      if (SiftDayNightCycle.EFFECTS_ID.equals(((DimensionType)(Object)this).effectsLocation())) {
         cir.setReturnValue(0);
      }
   }
}
