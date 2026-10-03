package mielon.thesift.client.render;

import mielon.thesift.world.SiftDayNightCycle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Dimension effects for The Sift: high clouds, no sunrise glow, pink day / blue night fog. */
public final class SiftSpecialEffects extends DimensionSpecialEffects {
   public static final Vec3 DAY_SKY = rgb(0xF7CFE0);
   public static final Vec3 NIGHT_SKY = rgb(0x176DB5);
   public static final Vec3 DAY_FOG = rgb(0xC9EFF1);
   public static final Vec3 NIGHT_FOG = rgb(0x0D3D78);
   public static final Vec3 DAY_CLOUD = rgb(0xFFF0EF);
   public static final Vec3 NIGHT_CLOUD = rgb(0x6E8FC4);

   public SiftSpecialEffects() {
      super(270.0F, true, SkyType.NORMAL, false, false);
   }

   public static Vec3 rgb(int color) {
      return new Vec3((color >> 16 & 0xFF) / 255.0, (color >> 8 & 0xFF) / 255.0, (color & 0xFF) / 255.0);
   }

   public static float night() {
      var level = Minecraft.getInstance().level;
      return level == null ? 0.0F : SiftDayNightCycle.nightBlend(level.getDayTime());
   }

   public static Vec3 blend(Vec3 day, Vec3 night) {
      float t = night();
      return new Vec3(Mth.lerp(t, day.x, night.x), Mth.lerp(t, day.y, night.y), Mth.lerp(t, day.z, night.z));
   }

   @Override
   public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
      return blend(DAY_FOG, NIGHT_FOG);
   }

   @Override
   public boolean isFoggyAt(int x, int z) {
      return false;
   }

   @Nullable
   @Override
   public float[] getSunriseColor(float timeOfDay, float partialTick) {
      return null;
   }
}
