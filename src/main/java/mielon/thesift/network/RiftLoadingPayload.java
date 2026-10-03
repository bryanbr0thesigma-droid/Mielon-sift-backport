package mielon.thesift.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Clientbound: start/stop the rift or portal loading transition. */
public record RiftLoadingPayload(boolean start, boolean rift) {
   public static final ResourceLocation ID = new ResourceLocation("the_sift", "rift_loading");

   public static RiftLoadingPayload read(FriendlyByteBuf buf) {
      return new RiftLoadingPayload(buf.readBoolean(), buf.readBoolean());
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeBoolean(this.start);
      buf.writeBoolean(this.rift);
   }

   public static void register() {
   }

   public static void send(ServerPlayer player, boolean start) {
      send(player, start, true);
   }

   public static void send(ServerPlayer player, boolean start, boolean rift) {
      if (ServerPlayNetworking.canSend(player, ID)) {
         FriendlyByteBuf buf = PacketByteBufs.create();
         new RiftLoadingPayload(start, rift).write(buf);
         ServerPlayNetworking.send(player, ID, buf);
      }
   }
}
