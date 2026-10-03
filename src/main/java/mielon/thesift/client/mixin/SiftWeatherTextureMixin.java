package mielon.thesift.client.mixin;

import mielon.thesift.world.TheSiftDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({LevelRenderer.class})
public abstract class SiftWeatherTextureMixin {
   private static final ResourceLocation VANILLA_RAIN = new ResourceLocation("textures/environment/rain.png");
   private static final ResourceLocation ICHOR_RAIN = new ResourceLocation("the_sift", "textures/environment/ichor_rain.png");
   private static final ResourceLocation VANILLA_SNOW = new ResourceLocation("textures/environment/snow.png");
   private static final ResourceLocation ICHOR_SNOW = new ResourceLocation("the_sift", "textures/environment/ichor_snow.png");

   @Redirect(
      method = {"renderSnowAndRain"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
   )
   private Precipitation theSift$restrictSnowColumns(Biome biome, BlockPos pos) {
      Precipitation original = biome.getPrecipitationAt(pos);
      ClientLevel level = Minecraft.getInstance().level;
      if (level == null || !level.dimension().equals(TheSiftDimension.LEVEL_KEY)) {
         return original;
      } else if (level.getBiome(pos).is(TheSiftDimension.ICHOR_SNOWY_PEAKS)) {
         return Precipitation.SNOW;
      } else {
         return original == Precipitation.SNOW ? Precipitation.RAIN : original;
      }
   }

   @ModifyArg(
      method = {"renderSnowAndRain"},
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
      index = 1
   )
   private ResourceLocation theSift$useIchorWeatherTexture(ResourceLocation original) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null && level.dimension().equals(TheSiftDimension.LEVEL_KEY)) {
         if (original.equals(VANILLA_RAIN)) {
            return ICHOR_RAIN;
         }
         if (original.equals(VANILLA_SNOW)) {
            return ICHOR_SNOW;
         }
      }
      return original;
   }
}
