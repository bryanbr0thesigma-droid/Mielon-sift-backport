package mielon.thesift.client.mixin;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({DimensionSpecialEffects.class})
public interface DimensionSpecialEffectsAccessor {
   @Accessor("EFFECTS")
   static Object2ObjectMap<ResourceLocation, DimensionSpecialEffects> theSift$effects() {
      throw new AssertionError();
   }
}
