package mielon.thesift.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/**
 * 1.20.1 has no spear mechanics; the Siftite spear is a slightly slower, harder-hitting melee weapon.
 */
public final class SiftSpearItem extends SwordItem {
   public SiftSpearItem(Tier tier, Properties properties) {
      super(tier, 1, -2.6F, properties);
   }
}
