package mielon.thesift.mixin;

import mielon.thesift.util.LevelDataSetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Level.class})
public abstract class LevelAccess implements LevelDataSetter {
   @Shadow
   @Final
   @Mutable
   protected WritableLevelData levelData;

   @Override
   public void theSift$setLevelData(WritableLevelData data) {
      this.levelData = data;
   }
}
