package mielon.thesift.client.mixin;

import mielon.thesift.portal.SonorousColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoteParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NoteParticle.class})
public abstract class NoteParticleMixin {
   @Inject(method = {"<init>"}, at = {@At("TAIL")})
   private void theSift$applySonorousColor(ClientLevel level, double x, double y, double z, double color, CallbackInfo ci) {
      if (!(color < 100.0)) {
         int encoded = (int)Math.round(color - 100.0);
         if (encoded >= 0 && encoded <= 23) {
            int soundIndex = encoded / 3 + 1;
            int shadeIndex = encoded % 3;
            int rgb = SonorousColors.shades(soundIndex)[shadeIndex];
            ((NoteParticle)(Object)this).setColor((rgb >> 16 & 0xFF) / 255.0F, (rgb >> 8 & 0xFF) / 255.0F, (rgb & 0xFF) / 255.0F);
         }
      }
   }
}
