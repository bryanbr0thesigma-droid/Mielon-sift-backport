package mielon.thesift.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;

/** Null/default-aware readers that mirror the "getXOr" accessors of newer Minecraft versions. */
public final class NbtCompat {
   private NbtCompat() {
   }

   public static int getIntOr(CompoundTag tag, String key, int fallback) {
      return tag.contains(key, 99) ? tag.getInt(key) : fallback;
   }

   public static long getLongOr(CompoundTag tag, String key, long fallback) {
      return tag.contains(key, 99) ? tag.getLong(key) : fallback;
   }

   public static double getDoubleOr(CompoundTag tag, String key, double fallback) {
      return tag.contains(key, 99) ? tag.getDouble(key) : fallback;
   }

   public static float getFloatOr(CompoundTag tag, String key, float fallback) {
      return tag.contains(key, 99) ? tag.getFloat(key) : fallback;
   }

   public static boolean getBooleanOr(CompoundTag tag, String key, boolean fallback) {
      return tag.contains(key, 99) ? tag.getBoolean(key) : fallback;
   }

   public static String getStringOr(CompoundTag tag, String key, String fallback) {
      return tag.contains(key, 8) ? tag.getString(key) : fallback;
   }

   public static void putBlockPos(CompoundTag tag, String key, BlockPos pos) {
      tag.put(key, NbtUtils.writeBlockPos(pos));
   }

   public static BlockPos getBlockPos(CompoundTag tag, String key) {
      return tag.contains(key, 10) ? NbtUtils.readBlockPos(tag.getCompound(key)) : null;
   }

   public static void putBlockPosList(CompoundTag tag, String key, java.util.Collection<BlockPos> positions) {
      ListTag list = new ListTag();
      for (BlockPos pos : positions) {
         list.add(NbtUtils.writeBlockPos(pos));
      }

      tag.put(key, list);
   }

   public static java.util.List<BlockPos> getBlockPosList(CompoundTag tag, String key) {
      java.util.List<BlockPos> result = new java.util.ArrayList<>();
      ListTag list = tag.getList(key, 10);
      for (int i = 0; i < list.size(); i++) {
         result.add(NbtUtils.readBlockPos(list.getCompound(i)));
      }

      return result;
   }

   public static java.util.Optional<CompoundTag> getCompound(CompoundTag tag, String key) {
      return tag.contains(key, 10) ? java.util.Optional.of(tag.getCompound(key)) : java.util.Optional.empty();
   }

   public static BlockPos getPackedPos(CompoundTag tag, String key) {
      return tag.contains(key, 99) ? BlockPos.of(tag.getLong(key)) : null;
   }

   public static java.util.UUID getUuidString(CompoundTag tag, String key) {
      if (!tag.contains(key, 8)) {
         return null;
      }

      try {
         return java.util.UUID.fromString(tag.getString(key));
      } catch (IllegalArgumentException ignored) {
         return null;
      }
   }
}
