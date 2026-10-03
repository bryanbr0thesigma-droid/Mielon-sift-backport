package mielon.thesift.world;

import net.minecraft.resources.ResourceLocation;

public final class SiftDayNightCycle {
   public static final ResourceLocation CLOCK_ID = new ResourceLocation("the_sift", "the_sift");
   public static final int DAY_TICKS = 12000;
   public static final int TRANSITION_TICKS = 60;
   public static final int NIGHT_TICKS = 12000;
   public static final int NIGHT_START = 12060;
   public static final int NIGHT_END = 24060;
   public static final int PERIOD_TICKS = 24120;

   public static final ResourceLocation EFFECTS_ID = new ResourceLocation("the_sift", "the_sift");
   /** Vanilla time-of-day fraction at which the sky is as dark as the Sift's night (about 60% sky light). */
   private static final float NIGHT_TIME_OF_DAY = 0.254F;

   private SiftDayNightCycle() {
   }

   public static float nightBlend(long totalTicks) {
      long tick = Math.floorMod(totalTicks, 24120L);
      if (tick < 12000L) {
         return 0.0F;
      } else if (tick < 12060L) {
         return smooth((float)(tick - 12000L) / 60.0F);
      } else {
         return tick < 24060L ? 1.0F : 1.0F - smooth((float)(tick - 24060L) / 60.0F);
      }
   }

   private static float smooth(float value) {
      float clamped = Math.max(0.0F, Math.min(1.0F, value));
      return clamped * clamped * (3.0F - 2.0F * clamped);
   }

   /** Replaces the vanilla sun-angle curve so only skylight (not a sun orbit) changes between day and night. */
   public static float timeOfDay(long totalTicks) {
      return NIGHT_TIME_OF_DAY * nightBlend(totalTicks);
   }

   public static boolean isNightTarget(long vanillaStyleTime) {
      return nightBlend(vanillaStyleTime) >= 0.5F;
   }
}
