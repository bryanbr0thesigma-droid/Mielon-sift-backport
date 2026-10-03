package mielon.thesift.entity;

import mielon.thesift.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/** The overgrown willow boat. Renders with the willow texture on the client. */
public class SiftBoat extends Boat {
   public SiftBoat(EntityType<? extends Boat> type, Level level) {
      super(type, level);
   }

   @Override
   public Item getDropItem() {
      return ModItems.OVERGROWN_WILLOW_BOAT;
   }
}
