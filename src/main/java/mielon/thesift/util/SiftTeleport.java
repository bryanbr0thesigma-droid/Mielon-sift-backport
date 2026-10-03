package mielon.thesift.util;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

public final class SiftTeleport {
   private SiftTeleport() {
   }

   /** Teleports an entity (possibly across dimensions) and returns the resulting entity, or null on failure. */
   public static Entity teleport(Entity entity, ServerLevel destination, Vec3 pos, Vec3 motion, float yaw, float pitch) {
      if (entity.level() == destination) {
         if (entity instanceof ServerPlayer player) {
            player.teleportTo(destination, pos.x, pos.y, pos.z, yaw, pitch);
         } else {
            entity.moveTo(pos.x, pos.y, pos.z, yaw, pitch);
            entity.setDeltaMovement(motion);
         }

         return entity;
      }

      return FabricDimensions.teleport(entity, destination, new PortalInfo(pos, motion, yaw, pitch));
   }
}
