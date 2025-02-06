package reobf.proghatches.gt.metatileentity;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.glodblock.github.common.item.ItemFluidDrop;
import com.google.common.collect.ImmutableMap;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.api.util.IInterfaceViewable;
import appeng.helpers.ICustomNameObject;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.item.AEItemStack;
import gregtech.api.GregTechAPI;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.data.ArrayUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import reobf.proghatches.block.BlockIOHub;
import reobf.proghatches.eucrafting.IInputMightBeEmptyPattern;
import reobf.proghatches.eucrafting.IInstantCompletable;
import reobf.proghatches.gt.metatileentity.util.ICircuitProvider;
import reobf.proghatches.gt.metatileentity.util.IDisallowOptimize;
import reobf.proghatches.gt.metatileentity.util.MappingItemHandler;
import reobf.proghatches.item.ItemProgrammingCircuit;
import reobf.proghatches.main.MyMod;
import reobf.proghatches.main.registration.Registration;

public class WaterProvider extends MTEHatch implements IAddUIWidgets, IPowerChannelState,
    ICraftingProvider, IGridProxyable, IInstantCompletable, ICustomNameObject, IInterfaceViewable {

   

    public WaterProvider(int aID, String aName, String aNameRegional, int aTier
       ) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
           0,
            reobf.proghatches.main.Config.get("WP", ImmutableMap.of())

        );
        Registration.items.add(new ItemStack(GregTechAPI.sBlockMachines, 1, aID));
    
    }

    private void updateValidGridProxySides() {
        if (disabled) {
            getProxy().setValidSides(EnumSet.noneOf(ForgeDirection.class));
            return;
        }
        getProxy().setValidSides(EnumSet.of(getBaseMetaTileEntity().getFrontFacing()));

    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {

        return true;
    }

    @Override
    public void onFacingChange() {
        updateValidGridProxySides();
    }

    public WaterProvider(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
       
    }

    // item returned in ae tick will not be recognized, delay to the next
    // onPostTick() call
    // ArrayList<ItemStack> toReturn = new ArrayList<>();
    private appeng.util.item.ItemList ret = new appeng.util.item.ItemList();

    @Override
    public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
        // this.getProxy().getEnergy().extractAEPower(amt, mode,
        // usePowerMultiplier)
        try {
            if (ItemProgrammingCircuit.getCircuit(patternDetails.getOutputs()[0].getItemStack())
                .map(ItemStack::getItem)
                .orElse(null) == MyMod.progcircuit) {
                this.getBaseMetaTileEntity()
                    .doExplosion(2);
                return false;
            }

        } catch (Exception e) {}

        try {
            this.getProxy()
                .getEnergy()
                .extractAEPower(10, Actionable.MODULATE, PowerMultiplier.ONE);
        } catch (GridAccessException e) {

        }
        ItemStack circuitItem = (patternDetails.getOutput(
            table,
            this.getBaseMetaTileEntity()
                .getWorld()));

        ret.add(AEItemStack.create(circuitItem));
        return true;
    }

    @Override
    public boolean isBusy() {

        return false;
    }

    final private int ran = (int) (Math.random() * 20);

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        if (aBaseMetaTileEntity.getWorld().isRemote == false) lab: if (aTick % 20 == ran) {// check every 1s
            legacy = false;
            if (snapshot != null) {
                for (int i = 0; i < snapshot.length; i++) {
                    if (ItemStack.areItemStacksEqual(snapshot[i], mInventory[i]) == false) {
                        patternDirty = true;
                        doSnapshot();
                        postEvent();
                        break lab;
                    } ;

                }
            }

        }

        //
        if (getBaseMetaTileEntity().isServerSide()) if (aTick % 20 == 0 && !disabled) {

            getBaseMetaTileEntity().setActive(isActive());
        }
        // IMEMonitor<IAEItemStack> ae = getStorageGrid().getItemInventory();
        returnItems();

        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    public void returnItems() {
        ret.forEach(
            s -> getStorageGrid().getItemInventory()
                .injectItems(s, Actionable.MODULATE, new MachineSource((IActionHost) getBaseMetaTileEntity())));
        ret.clear();

    }

    boolean legacy;

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        patternDirty = true;
        if (legacy == true) legacy = !legacy;
        postEvent();
        GTUtility.sendChatToPlayer(aPlayer, "Legacy Mode:" + legacy);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        this.getBaseMetaTileEntity()
            .sendBlockEvent((byte) 99, (byte) (disabled ? 1 : 0));
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {
        return getProxy().getNode();
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection dir) {

        return AECableType.DENSE;
    }

    @Override
    public void securityBreak() {

    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {

        return true;
    }

    /*
     * @Override public IInventory getPatterns() { // TODO Auto-generated method
     * stub return null; }
     */

    /*
     * @Override public String getCustomName() { // TODO Auto-generated method
     * stub return null; }
     * @Override public boolean hasCustomName() { // TODO Auto-generated method
     * stub return false; }
     * @Override public void setCustomName(String name) { // TODO Auto-generated
     * method stub }
     */
    AENetworkProxy gridProxy;

    @Override
    public AENetworkProxy getProxy() {

        if (gridProxy == null) {
            gridProxy = new AENetworkProxy(this, "proxy", visualStack(), true);
            gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
            updateValidGridProxySides();
            if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
                getBaseMetaTileEntity().getWorld()
                    .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
        }

        return this.gridProxy;
    }

    private ItemStack visualStack() {
        return new ItemStack(GregTechAPI.sBlockMachines, 1, getBaseMetaTileEntity().getMetaTileID());
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(
            getBaseMetaTileEntity().getWorld(),
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord());
    }

    @Override
    public void gridChanged() {

    }

    private IStorageGrid getStorageGrid() {
        try {
            return this.getProxy()
                .getGrid()
                .getCache(IStorageGrid.class);
        } catch (GridAccessException e) {
            return null;
        }
    }

    private final static Item encodedPattern = AEApi.instance()
        .definitions()
        .items()
        .encodedPattern()
        .maybeItem()
        .orNull();
    boolean patternDirty;

    private boolean postEvent() {

        try {
            this.getProxy()
                .getGrid()
                .postEvent(new MENetworkCraftingPatternChange(this, getGridNode(ForgeDirection.UNKNOWN)));
            return true;
        } catch (GridAccessException ignored) {
            return false;
        }
    }

    // no need to save to NBT
    ItemStack[] snapshot;

    private void doSnapshot() {
        snapshot = mInventory.clone();
        for (int i = 0; i < snapshot.length; i++) {
            snapshot[i] = Optional.ofNullable(snapshot[i])
                .map(ItemStack::copy)
                .orElse(null);
        }
    }

    @Override
    public void provideCrafting(ICraftingProviderHelper craftingTracker) {
      
        doSnapshot();
       {
          craftingTracker.addCraftingOption(this, new ProgrammingCircuitProvider.CircuitProviderPatternDetial(
        		  
        		  ItemFluidDrop.newStack(new FluidStack(FluidRegistry.WATER, 0+100_000_000))
        		  ));

        }

    }

    

    @Override
    public boolean isPowered() {
        if (disabled) {
            return true;// make waila info correct
        }
        return getProxy() != null && getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        if (disabled) {
            return true;// make waila info correct
        }

        return getProxy() != null && getProxy().isActive();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {

        return new WaterProvider(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {

    	ITexture[] tex= super.getTexture(aBaseMetaTileEntity, side, aFacing, colorIndex, aActive, redstoneLevel);
    	
    	ITexture[] texn=null;
    	
    	if(side==aFacing){texn=tex;
    	
    		}else{
    		texn=new ITexture[tex.length+1];
    	System.arraycopy(tex, 0, texn, 0, tex.length);
    	texn[texn.length-1]=TextureFactory.of(TexturesGtBlock.Overlay_Water);
    	}
    	
    	
    	return texn;

    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        if (disabled) {
            return new ITexture[] { aBaseTexture,
                TextureFactory.of(MyMod.iohub, BlockIOHub.magicNO_provider_in_active_overlay) };

        }

        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .setFromBlock(MyMod.iohub, BlockIOHub.magicNO_provider_active_overlay)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        if (disabled) {
            return new ITexture[] { aBaseTexture,
                TextureFactory.of(MyMod.iohub, BlockIOHub.magicNO_provider_in_overlay) };

        }

        return new ITexture[] { aBaseTexture, TextureFactory.of(MyMod.iohub, BlockIOHub.magicNO_provider_overlay) };
    }

    // @Override
    public boolean useModularUI() {

        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public void addUIWidgets(Builder builder, UIBuildContext buildContext) {
        final IItemHandlerModifiable inventoryHandler = new MappingItemHandler(this.mInventory, 0, mInventory.length);

        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 4)
                .startFromSlot(0)
                .endAtSlot(mInventory.length - 1)
                .background(new IDrawable[] { getGUITextureSet().getItemSlot() })
                .build()
                .setPos(3, 3));
    }

    private NBTTagCompound writeItem(final IAEItemStack finalOutput2) {
        final NBTTagCompound out = new NBTTagCompound();

        if (finalOutput2 != null) {
            finalOutput2.writeToNBT(out);
        }

        return out;
    }

    private appeng.util.item.ItemList readList(final NBTTagList tag) {
        final appeng.util.item.ItemList out = new appeng.util.item.ItemList();

        if (tag == null) {
            return out;
        }

        for (int x = 0; x < tag.tagCount(); x++) {
            final IAEItemStack ais = AEItemStack.loadItemStackFromNBT(tag.getCompoundTagAt(x));
            if (ais != null) {
                out.add(ais);
            }
        }

        return out;
    }

    private NBTTagList writeList(final IItemList<IAEItemStack> myList) {
        final NBTTagList out = new NBTTagList();

        for (final IAEItemStack ais : myList) {
            out.appendTag(this.writeItem(ais));
        }

        return out;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {

        super.saveNBTData(aNBT);

        int[] count = new int[1];
        aNBT.setTag("ret", this.writeList(this.ret));
        getProxy().writeToNBT(aNBT);
        Optional.ofNullable(customName)
            .ifPresent(s -> aNBT.setString("customName", s));
        // aNBT.setString("customName",customName);
        aNBT.setBoolean("disabled", disabled);
        aNBT.setBoolean("legacy", legacy);
      
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("x") == false) return;
        super.loadNBTData(aNBT);
        this.ret = this.readList((NBTTagList) aNBT.getTag("ret"));
        int[] count = new int[1];
        NBTTagCompound c;

        getProxy().readFromNBT(aNBT);;
        customName = aNBT.getString("customName");
        disabled = aNBT.getBoolean("disabled");
        legacy = false;// aNBT.getBoolean("legacy");
     
    }

    @Override
    public ItemStack getCrafterIcon() {

        return new ItemStack(GregTechAPI.sBlockMachines, 1, getBaseMetaTileEntity().getMetaTileID());
    }

   /* @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setBoolean("disabled", disabled);
        for (int i = 0; i < ((IInventory) tile).getSizeInventory(); i++) {
            ItemStack is = ((IInventory) tile).getStackInSlot(i);
            if (is != null) tag.setTag("#" + i, is.writeToNBT(new NBTTagCompound()));;
        }
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }*/

   /* @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {

        for (int i = 0; i < ((IInventory) accessor.getTileEntity()).getSizeInventory(); i++) currenttip.add(
            StatCollector.translateToLocal("proghatches.provider.waila") + Optional
                .ofNullable(
                    ItemStack.loadItemStackFromNBT(
                        accessor.getNBTData()
                            .getCompoundTag("#" + i)))
                .map(s -> s.getDisplayName() + "@" + s.getItemDamage())
                .orElse("<empty>")

        );

        if (accessor.getNBTData()
            .getBoolean("disabled")) {

            currenttip.add(StatCollector.translateToLocal("proghatches.provider.waila.disabled"));

        } ;

        super.getWailaBody(itemStack, currenttip, accessor, config);
    }
*/
    boolean disabled;

    public void disable() {
        disabled = true;
        if (getBaseMetaTileEntity().isServerSide()) this.getBaseMetaTileEntity()
            .sendBlockEvent(EVENT_DISABLE, (byte) (disabled ? 1 : 0));
        updateValidGridProxySides();
    }

    public static byte EVENT_DISABLE = (byte) 99;

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        if (aEventID == EVENT_DISABLE) {

            disabled = aValue == 1;

        }

        super.receiveClientEvent(aEventID, aValue);
    }

    
    @Override
    public void complete() {
        // returnItems();

    }

    public void clearDirty() {
        patternDirty = false;
    };

    public boolean patternDirty() {
        return patternDirty;
    }

    private String customName = null;

    @Override
    public String getCustomName() {

        return customName;
    }

    @Override
    public boolean hasCustomName() {

        return customName != null;
    }

    @Override
    public void setCustomName(String name) {
        customName = name;

    }

    @Override
    public boolean shouldDropItemAt(int index) {

        return super.shouldDropItemAt(index);
    }

    @Override
    public boolean isValidSlot(int aIndex) {

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        onBlockDestroyed();

        return super.getStackInSlot(aIndex);
    }
    // public Object getTile(){return this.getBaseMetaTileEntity();}

    @Override
    public int rows() {

        return 0;
    }

    @Override
    public int rowSize() {

        return 0;
    }

    public static IInventory EMPTY = new IInventory() {

        @Override
        public int getSizeInventory() {

            return 0;
        }

        @Override
        public ItemStack getStackInSlot(int slotIn) {

            return null;
        }

        @Override
        public ItemStack decrStackSize(int index, int count) {

            return null;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int index) {

            return null;
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {

        }

        @Override
        public String getInventoryName() {

            return "N/A";
        }

        @Override
        public boolean hasCustomInventoryName() {

            return false;
        }

        @Override
        public int getInventoryStackLimit() {

            return 0;
        }

        @Override
        public void markDirty() {

        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer player) {

            return false;
        }

        @Override
        public void openInventory() {

        }

        @Override
        public void closeInventory() {

        }

        @Override
        public boolean isItemValidForSlot(int index, ItemStack stack) {

            return false;
        }
    };;

    @Override
    public IInventory getPatterns() {

        return EMPTY;// new FakePatternInv(mInventory);
    }

    @Override
    public String getName() {

        return "N/A";
    }

    @Override
    public TileEntity getTileEntity() {

        return (TileEntity) this.getBaseMetaTileEntity();
    }

    @Override
    public boolean shouldDisplay() {

        return false;
    }

    @Override
    public boolean allowsPatternOptimization() {

        return false;
    }
}
