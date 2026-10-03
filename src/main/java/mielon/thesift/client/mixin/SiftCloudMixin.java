package mielon.thesift.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mielon.thesift.world.TheSiftDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** In The Sift, clouds only gather while it is raining. */
@Mixin({LevelRenderer.class})
public abstract class SiftCloudMixin {
   @Inject(method = {"renderClouds"}, at = {@At("HEAD")}, cancellable = true)
   private void theSift$cloudsOnlyInRain(PoseStack poseStack, org.joml.Matrix4f projection, float partialTick, double x, double y, double z, CallbackInfo ci) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null && level.dimension().equals(TheSiftDimension.LEVEL_KEY)) {
         float rain = level.getRainLevel(partialTick);
         if (rain < 0.02F) {
            ci.cancel();
         } else {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Math.min(1.0F, rain * 1.5F));
         }
      }
   }

   @Inject(method = {"renderClouds"}, at = {@At("RETURN")})
   private void theSift$resetCloudAlpha(PoseStack poseStack, org.joml.Matrix4f projection, float partialTick, double x, double y, double z, CallbackInfo ci) {
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
