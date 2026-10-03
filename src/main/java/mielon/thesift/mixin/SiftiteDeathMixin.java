package mielon.thesift.mixin;

import mielon.thesift.world.SiftiteRecoveryManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerPlayer.class})
public abstract class SiftiteDeathMixin {
   @Inject(
      method = {"die"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/server/level/ServerPlayer;dropAllDeathLoot(Lnet/minecraft/world/damagesource/DamageSource;)V"
      )}
   )
   private void theSift$captureConfirmedDeath(DamageSource source, CallbackInfo ci) {
      SiftiteRecoveryManager.captureOnDeath((ServerPlayer)(Object)this);
   }
}
