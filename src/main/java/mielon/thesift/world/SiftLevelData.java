package mielon.thesift.world;

import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;

/**
 * Level data for The Sift: everything is inherited from the overworld except the day clock and weather,
 * which are the dimension's own (1.20.1 has no per-dimension clocks or weather).
 */
public final class SiftLevelData extends DerivedLevelData {
   private final SiftLevelState state;

   public SiftLevelData(WorldData worldData, ServerLevelData wrapped, SiftLevelState state) {
      super(worldData, wrapped);
      this.state = state;
   }

   @Override
   public long getDayTime() {
      return this.state.dayTime;
   }

   @Override
   public void setDayTime(long time) {
      long old = this.state.dayTime;
      long target = time;
      if (time != old + 1L) {
         if (time % 24000L == 0L && time > old) {
            // sleeping skips to the next dawn
            target = (Math.floorDiv(old, 24120L) + 1L) * 24120L;
         }
         boolean toNight = SiftDayNightCycle.nightBlend(target) >= 0.5F;
         if (toNight != SiftDayNightCycle.nightBlend(old) >= 0.5F) {
            // start inside the 60-tick fade so the switch is smooth instead of instant
            long periodStart = Math.floorDiv(target, 24120L) * 24120L;
            target = toNight ? periodStart + SiftDayNightCycle.DAY_TICKS : periodStart - SiftDayNightCycle.TRANSITION_TICKS;
         }
      }
      if (target != old) {
         this.state.dayTime = target;
         this.state.setDirty();
      }
   }

   @Override
   public boolean isThundering() {
      return this.state.thundering;
   }

   @Override
   public void setThundering(boolean value) {
      this.state.thundering = value;
      this.state.setDirty();
   }

   @Override
   public boolean isRaining() {
      return this.state.raining;
   }

   @Override
   public void setRaining(boolean value) {
      this.state.raining = value;
      this.state.setDirty();
   }

   @Override
   public int getRainTime() {
      return this.state.rainTime;
   }

   @Override
   public void setRainTime(int value) {
      this.state.rainTime = value;
      this.state.setDirty();
   }

   @Override
   public int getThunderTime() {
      return this.state.thunderTime;
   }

   @Override
   public void setThunderTime(int value) {
      this.state.thunderTime = value;
      this.state.setDirty();
   }

   @Override
   public int getClearWeatherTime() {
      return this.state.clearTime;
   }

   @Override
   public void setClearWeatherTime(int value) {
      this.state.clearTime = value;
      this.state.setDirty();
   }
}
