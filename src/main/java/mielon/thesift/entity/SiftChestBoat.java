package mielon.thesift.entity;

import mielon.thesift.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/** The overgrown willow chest boat. */
public class SiftChestBoat extends ChestBoat {
   public SiftChestBoat(EntityType<? extends Boat> type, Level level) {
      super(type, level);
   }

   @Override
   public Item getDropItem() {
      return ModItems.OVERGROWN_WILLOW_CHEST_BOAT;
   }
}
