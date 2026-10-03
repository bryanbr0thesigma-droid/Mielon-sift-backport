package mielon.thesift.util;

import net.minecraft.world.level.storage.WritableLevelData;

/** Implemented on {@code Level} by a mixin so the Sift can replace its level data. */
public interface LevelDataSetter {
   void theSift$setLevelData(WritableLevelData data);
}
