package mielon.thesift.block.entity;

import mielon.thesift.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShelfBlockEntity extends BlockEntity {
   private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

   public ShelfBlockEntity(BlockPos pos, BlockState state) {
      super(ModBlocks.SHELF_BLOCK_ENTITY, pos, state);
   }

   public ItemStack getItem(int slot) {
      return this.items.get(slot);
   }

   public void setItem(int slot, ItemStack stack) {
      this.items.set(slot, stack);
      this.setChanged();
      if (this.level != null) {
         this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
      }
   }

   @Override
   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      ContainerHelper.saveAllItems(tag, this.items);
   }

   @Override
   public void load(CompoundTag tag) {
      super.load(tag);
      this.items.clear();
      ContainerHelper.loadAllItems(tag, this.items);
   }

   @Override
   public CompoundTag getUpdateTag() {
      return this.saveWithoutMetadata();
   }

   @Override
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }
}
