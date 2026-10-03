package mielon.thesift.network;

import mielon.thesift.world.SiftTeleportManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

/** Serverbound: the client finished loading and is ready for the first-visit intro. */
public record SiftIntroReadyPayload() {
   public static final ResourceLocation ID = new ResourceLocation("the_sift", "intro_ready");

   public static void register() {
      ServerPlayNetworking.registerGlobalReceiver(
         ID, (server, player, handler, buf, responseSender) -> server.execute(() -> SiftTeleportManager.playFirstIntro(player))
      );
   }
}
