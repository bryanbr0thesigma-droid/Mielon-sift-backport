package mielon.thesift.item;

import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Backport of the 1.21.11 spear: a quick jab on left click, and a charged lunge on hold-right-click that
 * dashes forward and pierces everything along the way, hitting harder the faster the wielder is moving.
 */
public final class SiftSpearItem extends SwordItem {
   private static final int MIN_CHARGE_TICKS = 8;
   private static final double LUNGE_REACH = 4.5;

   public SiftSpearItem(Tier tier, Properties properties) {
      super(tier, 1, -2.4F, properties);
   }

   @Override
   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.SPEAR;
   }

   @Override
   public int getUseDuration(ItemStack stack) {
      return 72000;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (player.getCooldowns().isOnCooldown(this)) {
         return InteractionResultHolder.fail(stack);
      }
      player.startUsingItem(hand);
      return InteractionResultHolder.consume(stack);
   }

   @Override
   public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
      int charge = this.getUseDuration(stack) - timeLeft;
      if (charge < MIN_CHARGE_TICKS || !(user instanceof Player player)) {
         return;
      }
      Vec3 look = user.getLookAngle();
      Vec3 start = user.getEyePosition();
      Vec3 end = start.add(look.scale(LUNGE_REACH));
      double speed = user.getDeltaMovement().horizontalDistance() * 20.0;
      float base = this.getDamage() + 1.0F;
      float damage = base + (float)Math.min(speed, 12.0) * 0.55F + Math.min(charge, 30) * 0.05F;
      if (!level.isClientSide) {
         List<Entity> targets = level.getEntities(user, new AABB(start, end).inflate(1.0), e -> e instanceof LivingEntity && e.isPickable() && e.isAlive());
         boolean hit = false;
         for (Entity target : targets) {
            if (target.getBoundingBox().inflate(0.3).clip(start, end).isPresent() || target.getBoundingBox().inflate(0.3).contains(end)) {
               if (target.hurt(level.damageSources().playerAttack(player), damage)) {
                  hit = true;
                  if (target instanceof LivingEntity living) {
                     living.knockback(0.8, -look.x, -look.z);
                  }
               }
            }
         }
         level.playSound(null, user.getX(), user.getY(), user.getZ(), hit ? SoundEvents.PLAYER_ATTACK_STRONG : SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.2F);
         stack.hurtAndBreak(hit ? 2 : 1, user, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
      }
      user.setDeltaMovement(user.getDeltaMovement().add(look.x * 1.1, Math.max(look.y, 0.0) * 0.4 + 0.1, look.z * 1.1));
      user.hurtMarked = true;
      user.fallDistance = 0.0F;
      player.getCooldowns().addCooldown(this, 50);
      player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));
   }
}
