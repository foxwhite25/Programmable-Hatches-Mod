package reobf.proghatches.gt.metatileentity;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import reobf.proghatches.lang.LangManager;

import com.glodblock.github.common.item.ItemFluidPacket;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import appeng.api.AEApi;
import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.api.util.IInterfaceViewable;
import appeng.core.AppEng;
import appeng.core.sync.GuiBridge;
import appeng.helpers.ICustomNameObject;
import appeng.items.misc.ItemEncodedPattern;
import appeng.items.tools.quartz.ToolQuartzCuttingKnife;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.Platform;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.tileentities.casings.upgrade.Inventory;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_CraftingInput_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_InputBus_ME;


public class PatternDualInputHatch extends BufferedDualInputHatch
		implements ICraftingProvider, IGridProxyable, ICustomNameObject, IInterfaceViewable, IPowerChannelState {

	public PatternDualInputHatch(String mName, byte mTier, String[] mDescriptionArray, ITexture[][][] mTextures,
			boolean mMultiFluid, int bufferNum, boolean fluid) {
		super(mName, mTier, mDescriptionArray, mTextures, mMultiFluid, bufferNum);

	}
	
	  @Override
	    public int rows() {
	        return 4;
	    }

	    @Override
	    public int rowSize() {
	        return 9;
	    }

	    @Override
	    public IInventory getPatterns() {
	        return patternMapper;
	    }
	    IInventory patternMapper=new IInventory(){

			@Override
			public int getSizeInventory() {
			
				return pattern.length;
			}

			@Override
			public ItemStack getStackInSlot(int slotIn) {
			
				return pattern[slotIn];
			}

			@Override
			public ItemStack decrStackSize(int index, int count) {
				
				if (pattern[index] != null)
		        {
		            ItemStack itemstack;

		            if (pattern[index].stackSize <= count)
		            {
		                itemstack = pattern[index];
		                pattern[index] = null;
		                this.markDirty();
		                return itemstack;
		            }
		            else
		            {
		                itemstack = pattern[index].splitStack(count);

		                if (pattern[index].stackSize == 0)
		                {
		                	pattern[index] = null;
		                }

		                this.markDirty();
		                return itemstack;
		            }
		        }
		        else
		        {
		            return null;
		        }
			}

			@Override
			public ItemStack getStackInSlotOnClosing(int index) {
			
				return null;
			}

			@Override
			public void setInventorySlotContents(int index, ItemStack stack) {
				pattern[index]=stack;
				
			}

			@Override
			public String getInventoryName() {
				
				return "";
			}

			@Override
			public boolean hasCustomInventoryName() {
			
				return false;
			}

			//@Override
			//public int stack

			@Override
			public void markDirty() {
				
				
			}

			@Override
			public boolean isUseableByPlayer(EntityPlayer player) {
				
				return true;
			}

			@Override
			public void openInventory() {}

			@Override
			public void closeInventory() {}

			@Override
			public boolean isItemValidForSlot(int index, ItemStack stack) {
			
				return true;
			}

			@Override
			public int getInventoryStackLimit() {
				
				return 1;
			}};
	@Override
	public ITexture[] getTexturesActive(ITexture aBaseTexture) {

		return new ITexture[] { aBaseTexture, TextureFactory.of(BlockIcons.OVERLAY_ME_CRAFTING_INPUT_BUFFER) };

	}

	@Override
	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		return new ITexture[] { aBaseTexture, TextureFactory.of(BlockIcons.OVERLAY_ME_CRAFTING_INPUT_BUFFER) };

	}

	public PatternDualInputHatch(int id, String name, String nameRegional, int tier, boolean mMultiFluid, int bufferNum,
			boolean fluid, String... optional) {
		super(id, name, nameRegional, tier, mMultiFluid, bufferNum,
				(optional.length > 0 ? optional
						: reobf.proghatches.main.Config
								.get("PDIH",
										ImmutableMap
												.of("bufferNum", bufferNum,
														"fluidSlots", fluidSlots(tier),/* "cap",
														format.format((int) (4000 * Math.pow(4, tier)
																/ (mMultiFluid ? 4 : 1))),*/
														"mMultiFluid", mMultiFluid, "slots",
														Math.min(16, (1 + tier) * (tier + 1))/*, "stacksize",
														(int) (64 * Math.pow(2, Math.max(tier - 3, 0)))*/))

				

				));
		this.supportFluids = fluid;
	}

	ItemStack[] pattern = new ItemStack[36];

	ButtonWidget createRefundButton(IWidgetBuilder<?> builder) {

		Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {

			PatternDualInputHatch.this.dirty = true;
			try {
				PatternDualInputHatch.this.refundAll();
			} catch (Exception e) {

				// e.printStackTrace();
			}
		}).setPlayClickSound(true).setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_EXPORT)

				.addTooltips(ImmutableList.of("Return all internally stored items back to AE"))

				.setPos(new Pos2d(getGUIWidth() - 18 - 3, 5 + 16 + 2)).setSize(16, 16);
		return (ButtonWidget) button;
	}

	MachineSource requestSource;

	private BaseActionSource getRequest() {

		if (requestSource == null)
			requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
		return requestSource;
	}

	private void refundAll() throws Exception {
		markDirty();
		dirty=true;
		BaseActionSource src = getRequest();
		IMEMonitor<IAEItemStack> sg = getProxy().getStorage().getItemInventory();
		abstract class Inv {
			abstract ItemStack[] geti();

			abstract FluidTank[] getf();
		}
		Consumer<Inv> consumer = inv -> {
			try {
				for (ItemStack itemStack : inv.geti()) {
					if (itemStack == null || itemStack.stackSize == 0)
						continue;
					IAEItemStack rest = Platform.poweredInsert(getProxy().getEnergy(), sg,
							AEApi.instance().storage().createItemStack(itemStack), src);
					itemStack.stackSize = rest != null && rest.getStackSize() > 0 ? (int) rest.getStackSize() : 0;
				}
				IMEMonitor<IAEFluidStack> fsg = getProxy().getStorage().getFluidInventory();
				for (FluidTank fluidStack : inv.getf()) {
					if (fluidStack == null || fluidStack.getFluidAmount() == 0)
						continue;
					IAEFluidStack rest = Platform.poweredInsert(getProxy().getEnergy(), fsg,
							AEApi.instance().storage().createFluidStack(fluidStack.getFluid()), src);
					fluidStack.setFluid(Optional.ofNullable(rest).map(IAEFluidStack::getFluidStack).orElse(null));
				}
				;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};

		inv0.stream().map(s -> new Inv() {
			@Override
			ItemStack[] geti() {
				return s.mStoredItemInternal;
			}

			@Override
			FluidTank[] getf() {
				return s.mStoredFluidInternal;
			}
		}).forEach(consumer);
		;
		consumer.accept(new Inv() {

			@Override
			ItemStack[] geti() {

				return mInventory;
			}

			@Override
			FluidTank[] getf() {

				return mStoredFluid;
			}
		});

	}

	@Override
	public void addUIWidgets(Builder builder, UIBuildContext buildContext) {
		buildContext.addSyncedWindow(88, this::createPatternWindow);

		builder.widget(createRefundButton(builder));
		builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
			if (widget.getContext().isClient() == false)
				widget.getContext().openSyncedWindow(88);
		}).setPlayClickSound(true).setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE)
				.addTooltips(ImmutableList.of(LangManager.translateToLocalFormatted("programmable_hatches.gt.pattern")))
				.setSize(16, 16)
				// .setPos(10 + 16 * 9, 3 + 16 * 2)
				.setPos(new Pos2d(getGUIWidth() - 18 - 3, 5 + 16 + 2 + 16 + 2)));

		super.addUIWidgets(builder, buildContext);
	}

	protected ModularWindow createPatternWindow(final EntityPlayer player) {
		final int WIDTH = 18 * 4 + 6;
		final int HEIGHT = 18 * 9 + 6;
		final int PARENT_WIDTH = getGUIWidth();
		final int PARENT_HEIGHT = getGUIHeight();

		ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
		builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
		builder.setGuiTint(getGUIColorization());
		builder.setDraggable(true);
		builder.setPos((a, b) -> new Pos2d(PARENT_WIDTH + b.getPos().getX(), PARENT_HEIGHT * 0 + b.getPos().getY()));
		for (int i = 0; i < 36; i++) {
			BaseSlot bs;
			builder.widget(new SlotWidget(bs = new BaseSlot(new MappingItemHandler(pattern, 0, 36), i)

			) {

				@Override
				protected ItemStack getItemStackForRendering(Slot slotIn) {
					ItemStack stack = slotIn.getStack();
					if (stack == null || !(stack.getItem() instanceof ItemEncodedPattern)) {
						return stack;
					}
					ItemStack output = ((ItemEncodedPattern) stack.getItem()).getOutput(stack);
					return output != null ? output : stack;

				}
			}.setFilter(itemStack -> itemStack.getItem() instanceof ICraftingPatternItem).setChangeListener(() -> {
				onPatternChange(bs.getSlotIndex(), bs.getStack());
			}).setPos((i % 4) * 18 + 3, (i / 4) * 18 + 3).setBackground(getGUITextureSet().getItemSlot(),
					GT_UITextures.OVERLAY_SLOT_PATTERN_ME));

		}

		return builder.build();
	}

	boolean needPatternSync;

	private void onPatternChange(int index, ItemStack newItem) {
		if (!getBaseMetaTileEntity().isServerSide())
			return;
		// we do not refund 'cause it's impossible to trace the item

		needPatternSync = true;
	}

	@Override
	public void gridChanged() {
		needPatternSync = true;
	}

	@Override
	public boolean canExtractItem(int aIndex, ItemStack aStack, int ordinalSide) {

		return false;
	}

	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {

		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection side, Fluid aFluid) {

		return false;
	}

	@Override
	public boolean canFill(ForgeDirection side, Fluid aFluid) {

		return false;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {

		return new PatternDualInputHatch(mName, mTier, mDescriptionArray, mTextures, mMultiFluid, bufferNum,
				this.supportFluids);

	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		NBTTagCompound tag = aNBT.getCompoundTag("patternSlots");
		if (tag != null)
			for (int i = 0; i < pattern.length; i++) {
				pattern[i] = Optional.ofNullable(tag.getCompoundTag("i" + i)).map(ItemStack::loadItemStackFromNBT)
						.orElse(null);
			}
		customName = aNBT.getString("customName");
		
		getProxy().readFromNBT(aNBT);
		super.loadNBTData(aNBT);
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		NBTTagCompound tag = new NBTTagCompound();// aNBT.getCompoundTag("patternSlots");

		for (int i = 0; i < pattern.length; i++) {
			final int ii = i;
			Optional.ofNullable(pattern[i]).map(s -> s.writeToNBT(new NBTTagCompound()))
					.ifPresent(s -> tag.setTag("i" + ii, s));
		}
		aNBT.setTag("patternSlots", tag);
		Optional.ofNullable(customName).ifPresent(s -> aNBT.setString("customName", s));
		getProxy().writeToNBT(aNBT);
		super.saveNBTData(aNBT);
	}

	boolean supportFluids = true;

	private void clearInv() {

		for (int i = 0; i < 16; i++)
			mInventory[i] = null;
		for (int i = 0; i < 4; i++)
			mStoredFluid[i].setFluid(null);
		;

	}

	private boolean postMEPatternChange() {
		// don't post until it's active
		if (!getProxy().isActive())
			return false;
		try {
			getProxy().getGrid().postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
		} catch (GridAccessException ignored) {
			return false;
		}
		return true;
	}

	long lastSync;

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		super.onPostTick(aBaseMetaTileEntity, aTimer);

		if (getBaseMetaTileEntity().isServerSide()) {
			if (needPatternSync && aTimer > lastSync + 100) {
				needPatternSync = !postMEPatternChange();
				lastSync = aTimer;
			}
			if (aTimer % 20 == 0) {
				getBaseMetaTileEntity().setActive(isActive());
			}
		}
	}
	@Override
	public int getInventoryStackLimit() {
		
		return Integer.MAX_VALUE;
	}
	
	
	
	
	@Override
	public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
		if (!isActive())
			return false;
		if (!isEmpty())
			return false;
		if (!supportFluids) {
			for (int i = 0; i < table.getSizeInventory(); ++i) {
				ItemStack itemStack = table.getStackInSlot(i);
				if (itemStack == null)
					continue;
				if (itemStack.getItem() instanceof ItemFluidPacket)
					return false;
			}
		}

		int items = 0;
		int fluids = 0;
		int size = table.getSizeInventory();
		for (int i = 0; i < size; i++) {
			ItemStack itemStack = table.getStackInSlot(i);
			if (itemStack == null)
				continue;
			if (itemStack.getItem() instanceof ItemFluidPacket) {
				fluids++;
				if (fluids > this.fluidSlots()) {
					clearInv();
					return false;
				}
				
				mStoredFluid[fluids-1].setFluid(ItemFluidPacket.getFluidStack(itemStack));
				
			} else {
				items++;
				if (items > 4) {
					clearInv();
					return false;
				}
				mInventory[items-1] = itemStack;

			}
		}
		markDirty();
		dirty = true;
		classify();

		justHadNewItems = true;
		return true;
	}

	private boolean isEmpty() {
		for (ItemStack is : mInventory) {
			if (is != null && is.stackSize > 0)
				return false;
		}
		for (FluidTank is : mStoredFluid) {
			if (is.getFluidAmount() > 0)
				return false;
		}
		return true;
	}

	@Override
	public boolean isBusy() {
		return !isEmpty();
	}

	@Override
	public IGridNode getGridNode(ForgeDirection dir) {
		return getProxy().getNode();
	}

	private AENetworkProxy gridProxy;

	@Override
	public AENetworkProxy getProxy() {
		if (gridProxy == null) {
			gridProxy = new AENetworkProxy(this, "proxy",
					new ItemStack(GregTech_API.sBlockMachines, 1, this.getBaseMetaTileEntity().getMetaTileID()), true);
			gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
			updateValidGridProxySides();
			if (getBaseMetaTileEntity().getWorld() != null)
				gridProxy.setOwner(getBaseMetaTileEntity().getWorld()
						.getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
		}

		return this.gridProxy;
	}

	@Override
	public void securityBreak() {
		// no op
	}



	@Override
	public String getName() {
		if (hasCustomName()) {
			return getCustomName();
		}
		StringBuilder name = new StringBuilder();
		if (getCrafterIcon() != null) {
			name.append(getCrafterIcon().getDisplayName());
		} else {
			name.append(getInventoryName());
		}

		/*
		 * if (mInventory[SLOT_CIRCUIT] != null) { name.append(" - ");
		 * name.append(mInventory[SLOT_CIRCUIT].getItemDamage()); } if
		 * (mInventory[SLOT_MANUAL_START] != null) { name.append(" - ");
		 * name.append(mInventory[SLOT_MANUAL_START].getDisplayName()); }
		 */// TODO
		return name.toString();
	}

	@Override
	public TileEntity getTileEntity() {
		return (TileEntity) getBaseMetaTileEntity();
	}

	@Override
	public boolean shouldDisplay() {

		return true;
	}

	String customName;

	@Override
	public String getCustomName() {

		return customName;
	}

	@Override
	public boolean hasCustomName() {

		return customName != null&&(!customName.equals(""));
	}
    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        final ItemStack is = aPlayer.inventory.getCurrentItem();
        if (is != null && is.getItem() instanceof ToolQuartzCuttingKnife) {
            if (ForgeEventFactory.onItemUseStart(aPlayer, is, 1) <= 0) return false;
             IGregTechTileEntity te = getBaseMetaTileEntity();
            aPlayer.openGui(
                AppEng.instance(),
                GuiBridge.GUI_RENAMER.ordinal() << 5 | (side.ordinal()),
                te.getWorld(),
                te.getXCoord(),
                te.getYCoord(),
                te.getZCoord());
            return true;
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }
	@Override
	public void setCustomName(String name) {
		customName = name;

	}

	@Override
	public DimensionalCoord getLocation() {

		return new DimensionalCoord(getTileEntity());
	}

	@Override
	public boolean isActive() {
		return getProxy() != null && getProxy().isActive();
	}

	@Override
	public void provideCrafting(ICraftingProviderHelper craftingTracker) {
		if (!isActive())
			return;

		for (ItemStack slot : pattern) {
			if (slot == null)
				continue;
			ICraftingPatternDetails details = null;
			try {
				details = ((ICraftingPatternItem) slot.getItem()).getPatternForItem(slot,
						this.getBaseMetaTileEntity().getWorld());

			} catch (Exception e) {

			}
			if (details == null) {
				GT_Mod.GT_FML_LOGGER.warn("Found an invalid pattern at " + getBaseMetaTileEntity().getCoords()
						+ " in dim " + getBaseMetaTileEntity().getWorld().provider.dimensionId);
				continue;
			}
			craftingTracker.addCraftingOption(this, details);
		}

	}

	@Override
	public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
		super.onFirstTick(aBaseMetaTileEntity);
		getProxy().onReady();
	}

	@Override
	public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
		return isOutputFacing(forgeDirection) ? AECableType.SMART : AECableType.NONE;
	}

	private void updateValidGridProxySides() {

		getProxy().setValidSides(EnumSet.of(getBaseMetaTileEntity().getFrontFacing()));

	}

	@Override
	public void onFacingChange() {
		updateValidGridProxySides();
	}

	@Override
	public boolean isPowered() {
		return getProxy() != null && getProxy().isPowered();
	}

	@Override
	public ItemStack getCrafterIcon() {
		ItemStack is = this.getMachineCraftingIcon();
		return is == null ? new ItemStack(GregTech_API.sBlockMachines, 1, getBaseMetaTileEntity().getMetaTileID()) : is;
	}

	@Override
	public void onBlockDestroyed() {
		try {
			refundAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onBlockDestroyed();
	}
	public int fluidLimit() {

		return Integer.MAX_VALUE;
	}

	public int itemLimit() {

		return Integer.MAX_VALUE;
	}
	  boolean createInsertion(){return false;}
	  boolean showFluidLimit() {
			
			return false;
		}
	  @Override
	public int getInventoryFluidLimit() {
		return Integer.MAX_VALUE;
	}
}
