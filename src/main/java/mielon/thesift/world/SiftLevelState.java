package mielon.thesift.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

/** The Sift's own clock and weather, stored in the dimension's data folder. */
public final class SiftLevelState extends SavedData {
   public static final String NAME = "the_sift_level_state";
   public long dayTime;
   public boolean raining;
   public boolean thundering;
   public int rainTime;
   public int thunderTime;
   public int clearTime;

   public static SiftLevelState load(CompoundTag tag) {
      SiftLevelState state = new SiftLevelState();
      state.dayTime = tag.getLong("day_time");
      state.raining = tag.getBoolean("raining");
      state.thundering = tag.getBoolean("thundering");
      state.rainTime = tag.getInt("rain_time");
      state.thunderTime = tag.getInt("thunder_time");
      state.clearTime = tag.getInt("clear_time");
      return state;
   }

   @Override
   public CompoundTag save(CompoundTag tag) {
      tag.putLong("day_time", this.dayTime);
      tag.putBoolean("raining", this.raining);
      tag.putBoolean("thundering", this.thundering);
      tag.putInt("rain_time", this.rainTime);
      tag.putInt("thunder_time", this.thunderTime);
      tag.putInt("clear_time", this.clearTime);
      return tag;
   }
}
