package mielon.thesift.client.particle;

import net.minecraft.client.particle.SplashParticle;
import net.minecraft.client.particle.BubbleParticle;
import mielon.thesift.fluid.ModFluids;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluid;

public final class IchorFluidParticle {
   private static final float[][] ICHOR_PALETTE = new float[][]{
      {0.0F, 0.98F, 1.0F}, {0.23F, 0.43F, 1.0F}, {0.94F, 0.03F, 1.0F}, {1.0F, 0.02F, 0.58F}, {1.0F, 0.46F, 0.01F}, {0.15F, 1.0F, 0.57F}
   };

   private IchorFluidParticle() {
   }

   public static void tintIchor(Particle particle, ClientLevel level, double x, double y, double z, boolean vivid) {
      double time = level.getGameTime() * 0.006;
      double broad = Math.sin(x * 0.075 + z * 0.052 + time);
      double detail = Math.sin(x * -0.041 + z * 0.091 - time * 0.73);
      double noise = Math.sin(x * 12.9898 + y * 78.233 + z * 37.719 + level.getGameTime() * 0.173) * 43758.5453;
      noise -= Math.floor(noise);
      double selector = 0.5 + broad * 0.22 + detail * 0.13 + (noise - 0.5) * (vivid ? 0.92 : 0.3);
      selector -= Math.floor(selector);
      float palettePosition = (float)selector * ICHOR_PALETTE.length;
      int firstIndex = Mth.floor(palettePosition) % ICHOR_PALETTE.length;
      int secondIndex = (firstIndex + 1) % ICHOR_PALETTE.length;
      float blend = palettePosition - Mth.floor(palettePosition);
      blend = blend * blend * (3.0F - 2.0F * blend);
      float red = Mth.lerp(blend, ICHOR_PALETTE[firstIndex][0], ICHOR_PALETTE[secondIndex][0]);
      float green = Mth.lerp(blend, ICHOR_PALETTE[firstIndex][1], ICHOR_PALETTE[secondIndex][1]);
      float blue = Mth.lerp(blend, ICHOR_PALETTE[firstIndex][2], ICHOR_PALETTE[secondIndex][2]);
      float saturation = vivid ? 1.18F : 1.04F;
      float average = (red + green + blue) / 3.0F;
      particle.setColor(
         Mth.clamp(average + (red - average) * saturation, 0.0F, 1.0F),
         Mth.clamp(average + (green - average) * saturation, 0.0F, 1.0F),
         Mth.clamp(average + (blue - average) * saturation, 0.0F, 1.0F)
      );
   }

   public static final class BubbleProvider implements ParticleProvider<SimpleParticleType> {
      private final ParticleProvider<SimpleParticleType> vanilla;

      public BubbleProvider(SpriteSet sprites) {
         this.vanilla = new BubbleParticle.Provider(sprites);
      }

      @Override
      public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
         Particle particle = this.vanilla.createParticle(type, level, x, y, z, xd, yd, zd);
         if (particle != null) {
            IchorFluidParticle.tintIchor(particle, level, x, y, z, false);
         }

         return particle;
      }
   }

   public static final class RainSplashProvider implements ParticleProvider<SimpleParticleType> {
      private final ParticleProvider<SimpleParticleType> vanilla;

      public RainSplashProvider(SpriteSet sprites) {
         this.vanilla = new WaterDropParticle.Provider(sprites);
      }

      @Override
      public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
         Particle particle = this.vanilla.createParticle(type, level, x, y, z, xd, yd, zd);
         if (particle != null) {
            IchorFluidParticle.tintIchor(particle, level, x, y, z, true);
         }

         return particle;
      }
   }

   public static final class SplashProvider implements ParticleProvider<SimpleParticleType> {
      private final ParticleProvider<SimpleParticleType> vanilla;

      public SplashProvider(SpriteSet sprites) {
         this.vanilla = new SplashParticle.Provider(sprites);
      }

      @Override
      public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
         Particle particle = this.vanilla.createParticle(type, level, x, y, z, xd, yd, zd);
         if (particle != null) {
            IchorFluidParticle.tintIchor(particle, level, x, y, z, true);
         }

         return particle;
      }
   }
}
