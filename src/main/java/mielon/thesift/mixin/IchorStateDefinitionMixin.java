package mielon.thesift.mixin;

import java.util.Map;
import mielon.thesift.fluid.IchorState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Builder.class})
public abstract class IchorStateDefinitionMixin {
   @Shadow
   @Final
   private Object owner;
   @Shadow
   @Final
   private Map<String, Property<?>> properties;

   @Inject(
      method = {"create"},
      at = {@At("HEAD")}
   )
   private void theSift$addFluidState(CallbackInfoReturnable<?> cir) {
      if (this.owner instanceof Block block
         && this.properties.get("waterlogged") instanceof BooleanProperty
         && (
            block.getClass().getName().startsWith("mielon.thesift.")
               || block.getClass().getName().startsWith("net.minecraft.") && theSift$isKnownRegistration()
         )) {
         this.properties.putIfAbsent("ichorlogged", IchorState.ICHORLOGGED);
      }
   }

   @Unique
   private static final String BLOCKS_CLASS = net.fabricmc.loader.api.FabricLoader.getInstance()
      .getMappingResolver()
      .mapClassName("intermediary", "net.minecraft.class_2246");

   @Unique
   private static boolean theSift$isKnownRegistration() {
      return StackWalker.getInstance()
         .walk(
            frames -> frames.anyMatch(
               frame -> frame.getClassName().equals("mielon.thesift.block.ModBlocks")
                  || frame.getClassName().equals(BLOCKS_CLASS) && frame.getMethodName().equals("<clinit>")
            )
         );
   }
}
