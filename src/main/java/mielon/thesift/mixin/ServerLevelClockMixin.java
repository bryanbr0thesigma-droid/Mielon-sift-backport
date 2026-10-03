package mielon.thesift.mixin;

import mielon.thesift.world.SiftLevelData;
import mielon.thesift.world.SiftLevelState;
import mielon.thesift.world.TheSiftDimension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.players.PlayerList;

/** Gives The Sift its own day clock and weather by swapping in {@link SiftLevelData} as the level data. */
@Mixin({ServerLevel.class})
public abstract class ServerLevelClockMixin {
   @Shadow
   @Final
   @Mutable
   private ServerLevelData serverLevelData;
   @Shadow
   @Final
   private MinecraftServer server;
   @Shadow
   @Final
   @Mutable
   private boolean tickTime;

   @Inject(method = {"<init>"}, at = {@At("RETURN")})
   private void theSift$installOwnClock(CallbackInfo ci) {
      ServerLevel self = (ServerLevel)(Object)this;
      if (self.dimension().equals(TheSiftDimension.LEVEL_KEY)) {
         SiftLevelState state = self.getDataStorage().computeIfAbsent(SiftLevelState::load, SiftLevelState::new, SiftLevelState.NAME);
         SiftLevelData data = new SiftLevelData(this.server.getWorldData(), this.serverLevelData, state);
         this.serverLevelData = data;
         this.tickTime = true;
         ((mielon.thesift.util.LevelDataSetter)(Object)this).theSift$setLevelData(data);
         if (state.raining) {
            self.setRainLevel(1.0F);
         }
         if (state.thundering) {
            self.setThunderLevel(1.0F);
         }
      }
   }

   /** Weather events only reach players in the dimension they happen in. */
   @Redirect(
      method = {"advanceWeatherCycle"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V")
   )
   private void theSift$broadcastWeatherInsideDimension(PlayerList players, Packet<?> packet) {
      players.broadcastAll(packet, ((ServerLevel)(Object)this).dimension());
   }
}
