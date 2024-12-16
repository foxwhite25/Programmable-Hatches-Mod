package reobf.proghatches.gt.metatileentity.util;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.forge.ItemHandlerHelper;

import gregtech.api.util.GTUtility;

public class MappingItemHandler implements com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable
,IInterhandlerGroup
{
	public Runnable update=()->{};
	public MappingItemHandler(ItemStack[] is, int index, int num) {
		this.is = is;
		this.index = index;
		this.num = num;
	}

	boolean phantom;

	public MappingItemHandler phantom() {
		phantom = true;
		return this;
	}

	ItemStack[] is;
	int index, num;

	@Override
	public int getSlots() {

		return num + index;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {

		return is[var1 - index];
	}

	protected int getStackLimit(int slot, ItemStack stack) {
		return Math.min(this.getSlotLimit(slot - index), stack.getMaxStackSize());
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		try{
		if (stack == null) {
			return null;
		} else {
			// this.validateSlotIndex(slot);
			ItemStack existing = is[slot - index];
			int limit = this.getStackLimit(slot - index, stack);
			if (existing != null) {
				if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
					return stack;
				}

				limit -= existing.stackSize;
			}

			if (limit <= 0) {
				return stack;
			} else {
				boolean reachedLimit = stack.stackSize > limit;
				if (!simulate) {
					if (existing == null) {
						is[slot - index] = (reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);

					} else {
						existing.stackSize += reachedLimit ? limit : stack.stackSize;
					}

					// this.onContentsChanged(slot);
				}

				return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) : null;
			}
		}}finally{update.run();}
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
	try{
		if (amount == 0) {
			return null;
		} else {
			// this.validateSlotIndex(slot);
			ItemStack existing = (ItemStack) is[slot - index];
			if (existing == null) {
				return null;
			} else {
				int toExtract = Math.min(amount, existing.getMaxStackSize());
				if (existing.stackSize <= toExtract) {
					if (!simulate) {
						is[slot - index] = null;
						// this.onContentsChanged(slot);
					}

					return existing;
				} else {
					if (!simulate) {
						is[slot - index] = ItemHandlerHelper.copyStackWithSize(existing,
								existing.stackSize - toExtract);
						// this.onContentsChanged(slot);
					}

					return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
				}
			}
		}}finally{update.run();}
	}

	@Override
	public int getSlotLimit(int slot) {
		return Integer.MAX_VALUE;
	}

	@Override
	public void setStackInSlot(int var1, ItemStack var2) {
		try{
		if (phantom) {
			is[var1 - index] = GTUtility.copyAmount(0, var2);

		} else
			is[var1 - index] = var2;
		}finally{update.run();}
	}
	
	public MappingItemHandler id(long i){id=i;return this;}
long id;
	@Override
	public long handlerID() {
		return id;
	}
	
}
