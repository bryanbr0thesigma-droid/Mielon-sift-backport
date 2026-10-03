package mielon.thesift.util;

import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public final class EntityLookup {
   private EntityLookup() {
   }

   /** Finds an entity by UUID in any loaded dimension of the level's server. */
   public static Entity findInAnyDimension(ServerLevel origin, UUID id) {
      Entity local = origin.getEntity(id);
      if (local != null) {
         return local;
      }

      for (ServerLevel level : origin.getServer().getAllLevels()) {
         Entity found = level.getEntity(id);
         if (found != null) {
            return found;
         }
      }

      return null;
   }
}
