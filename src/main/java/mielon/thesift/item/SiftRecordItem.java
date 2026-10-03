package mielon.thesift.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;

public final class SiftRecordItem extends RecordItem {
   public SiftRecordItem(int comparatorValue, SoundEvent sound, Properties properties, int lengthInSeconds) {
      super(comparatorValue, sound, properties, lengthInSeconds * 20);
   }
}
