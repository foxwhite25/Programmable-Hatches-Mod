package reobf.proghatches.main;

import static crazypants.enderio.gui.IconEIO.WRENCH_OVERLAY_ITEM;
import static crazypants.enderio.gui.IconEIO.WRENCH_OVERLAY_ITEM_OFF;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.Proxy;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.vfyjxf.nee.nei.NEECraftingHandler;
import com.github.vfyjxf.nee.processor.GregTech5RecipeProcessor;
import com.glodblock.github.client.gui.GuiDualInterface;
import com.glodblock.github.client.gui.container.ContainerDualInterface;
import com.glodblock.github.common.parts.PartFluidP2PInterface;
import com.glodblock.github.crossmod.opencomputers.DriverLevelMaintainer;
import com.glodblock.github.inventory.FluidConvertingInventoryAdaptor;
import com.glodblock.github.loader.ItemAndBlockHolder;
import com.glodblock.github.nei.FluidPatternTerminalRecipeTransferHandler;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;

import WayofTime.alchemicalWizardry.common.achievements.AchievementTrigger;
import WayofTime.alchemicalWizardry.common.achievements.AchievementsRegistry;
import appeng.api.AEApi;
import appeng.api.config.FuzzyMode;
import appeng.api.config.Upgrades;
import appeng.api.implementations.IUpgradeableHost;
import appeng.api.storage.data.IAEFluidStack;
import appeng.client.gui.implementations.GuiPriority;
import appeng.client.gui.widgets.ITooltip;
import appeng.container.implementations.ContainerOptimizePatterns;
import appeng.container.implementations.ContainerPriority;
import appeng.container.slot.AppEngSlot;
import appeng.container.slot.SlotDisabled;
import appeng.container.slot.SlotFake;
import appeng.container.slot.SlotNormal;
import appeng.core.Api;
import appeng.core.features.registries.InterfaceTerminalRegistry;
import appeng.core.features.registries.RegistryContainer;
import appeng.core.localization.GuiText;
import appeng.crafting.v2.resolvers.CraftableItemResolver.CraftFromPatternTask;
import appeng.helpers.BlockingModeIgnoreList;
import appeng.helpers.DualityInterface;
import appeng.helpers.IInterfaceHost;
import appeng.helpers.IPriorityHost;
import appeng.items.tools.ToolMemoryCard;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.parts.AEBasePart;
import appeng.server.AECommand;
import appeng.tile.inventory.AppEngInternalAEInventory;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.item.AEFluidStack;
import appeng.util.item.AEItemStack;
import appeng.util.item.ItemList;
import codechicken.multipart.MultipartGenerator;
import codechicken.nei.recipe.StackInfo;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import crazypants.enderio.conduit.ConduitDisplayMode;
import crazypants.enderio.conduit.geom.Offset;
import crazypants.enderio.conduit.geom.Offsets;
import crazypants.enderio.conduit.geom.Offsets.Axis;
import crazypants.enderio.conduit.geom.Offsets.OffsetKey;
import crazypants.enderio.conduit.item.IItemConduit;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.net.GTPacketSendCoverData;
import gregtech.api.objects.GTDualInputs;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.blocks.BlockMachines;
import gregtech.common.covers.CoverInfo;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;

import gregtech.crossmod.waila.GregtechWailaDataProvider;
import li.cil.oc.api.Driver;
import reobf.proghatches.Tags;
import reobf.proghatches.ae.BlockAutoFillerMKII;
import reobf.proghatches.ae.BlockMolecularAssemblerInterface;
import reobf.proghatches.ae.BlockRequestTunnel;
import reobf.proghatches.block.ChunkTrackingGridCahce;
import reobf.proghatches.block.TileIOHub;
import reobf.proghatches.eio.ICraftingMachineConduit;
import reobf.proghatches.eio.ItemMAConduit;
import reobf.proghatches.eio.MASettings;
import reobf.proghatches.eucrafting.BlockEUInterface;
import reobf.proghatches.eucrafting.AECover;
import reobf.proghatches.eucrafting.AECover.IMemoryCardSensitive;
import reobf.proghatches.eucrafting.InterfaceData;
import reobf.proghatches.eucrafting.PartEUP2PInterface;
import reobf.proghatches.eucrafting.TileFluidInterface_EU;
import reobf.proghatches.gt.metatileentity.DualInputHachOC;
import reobf.proghatches.gt.metatileentity.PatternDualInputHatch;
import reobf.proghatches.gt.metatileentity.PatternDualInputHatchInventoryMappingSlave;
import reobf.proghatches.gt.metatileentity.ProgrammingCircuitProvider;
import reobf.proghatches.gt.metatileentity.ProgrammingCircuitProviderPrefabricated;
import reobf.proghatches.gt.metatileentity.multi.LargeProgrammingCircuitProvider;
import reobf.proghatches.item.ItemBookTutorial;
import reobf.proghatches.item.ItemProgrammingCircuit;
import reobf.proghatches.keybinding.KeyBindings;
import reobf.proghatches.lang.LangManager;
import reobf.proghatches.main.mixin.mixins.MixinFixPipeCoverBug;
import reobf.proghatches.main.registration.Registration;
import reobf.proghatches.net.ConnectionModeMessage;
import reobf.proghatches.net.MAFXMessage;
import reobf.proghatches.net.MasterSetMessage;
import reobf.proghatches.net.OpenPartGuiMessage;
import reobf.proghatches.net.PriorityMessage;
import reobf.proghatches.net.RenameMessage;
import reobf.proghatches.net.SwitchModeMessage;
import reobf.proghatches.net.UpgradesMessage;
import reobf.proghatches.net.VoidFXMessage;
import reobf.proghatches.net.WayPointMessage;
import reobf.proghatches.oc.ItemAPICard;
import reobf.proghatches.oc.ItemGTRedstoneCard;
import reobf.proghatches.oc.TileCardReader;
import reobf.proghatches.oc.WirelessPeripheralManager;
import reobf.proghatches.util.ProghatchesUtil;

import tconstruct.armor.player.TPlayerStats;
import thaumcraft.common.entities.golems.ItemGolemCore;

@Mod(modid = MyMod.MODID, version = Tags.VERSION, name =  MyMod.MODNAME, acceptedMinecraftVersions = "[1.7.10]", dependencies = "required-after:appliedenergistics2;required-after:gregtech;", acceptableRemoteVersions = "*"
/*
 * ,dependencies= "required-after:neenergistics;"
 */
)
public class MyMod {
	
	final public static String MODID="programmablehatches";
	final public static String MODNAME="ProgrammableHatches";
	public static final Logger LOG = LogManager.getLogger(MODID);
	
	
	public static MyMod instance;
	{
	
		//System.out.println("cccccccccccccccc");
		
		try{new GTDualInputs();
		}catch(Throwable t){
			t.printStackTrace();
			LOG.fatal("Add polyfill jar to mods.");
			FMLCommonHandler.instance().exitJava(1, false);
		}
		if((Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment") ){
		
			ProcessingLogic.class.getDeclaredFields();
			/*DualityInterface.class.getDeclaredFields();
		CraftFromPatternTask.class.getDeclaredFields();
		NEECraftingHandler.class.getDeclaredFields();
		GregTech5RecipeProcessor.class.getDeclaredFields();
		FluidPatternTerminalRecipeTransferHandler.class.getDeclaredFields();*/
		/*CraftingCPUCluster.class.getDeclaredFields();
		NBTTagCompound t=new NBTTagCompound();
		AEFluidStack.create(new FluidStack(FluidRegistry.WATER,123).writeToNBT(t));
		*/
	
		}
		
		
		instance = this;
	}

	static {
		
		//
		
		
		class test extends Item {
			@Override
			public int getItemStackLimit() {
				return 0;
			}
		}
		try {
			Boolean b1 = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
			boolean b2 = test.class.getDeclaredMethod("getItemStackLimit") != null;
			if (((b1 != null) && b1) == false && b2 == true) {

				for (int i = 0; i < 20; i++) {
					LOG.fatal("!!!ATTENTION!!!");
					LOG.fatal(
							"You are using dev version of ProgrammableHatches in obfuscated env! Use the one without '-dev' suffix!");
					LOG.fatal("Will stop the game, since it's impossible to proceed.");
				}
				;

				FMLCommonHandler.instance().exitJava(1, false);
				// throw new AssertionError("You are using dev version of
				// ProgrammableHatches in obfuscated env! Use the one without
				// '-dev' suffix!");
			}
		} catch (Exception e) {
		}
	}
	public static Deque<Runnable> scheduled = new ArrayDeque<Runnable>();
	// public static ShutDownReason ACCESS_LOOP=new
	// SimpleShutDownReason("proghatch.access_loop", true){public String getID()
	// {return "proghatch.access_loop";};};
	public static SimpleNetworkWrapper net = new SimpleNetworkWrapper( MyMod.MODID);
	public static Item progcircuit;
	public static Item toolkit;

	// static {CraftingCPUCluster.class.getDeclaredFields();}
	@SidedProxy(clientSide = "reobf.proghatches.main.ClientProxy", serverSide = "reobf.proghatches.main.CommonProxy")
	public static CommonProxy proxy;
	public static Item fakepattern;
	public static Item smartarm;
	public static Item cover;
	public static Item oc_redstone;
	public static Item oc_api;
	public static Block iohub;
	public static Block pstation;
	public static Item pitem;
	@Nullable
	public static Item euupgrade;

	public static Item eu_token;
	public static Item eu_source_part;
	public static BlockEUInterface block_euinterface;
	public static Item euinterface_p2p;
	public static Item book;
	public static Item fixer;
	// public static Item eu_tool;
	public static Item plunger;
	public static Item lazer_p2p_part;
	public static Item upgrades;
	public static Block alert;
	public static Item amountmaintainer;
	public static Block submitter;
	public static Item cpu;
	public static Block reader;
	{
	FMLCommonHandler.instance().bus().register(this);
	MinecraftForge.EVENT_BUS.register(this);
	
	}
	@Mod.EventHandler

	public void preInit(FMLPreInitializationEvent event) {
		FluidConvertingInventoryAdaptor.class.getFields();
		net.registerMessage(new OpenPartGuiMessage.Handler(), OpenPartGuiMessage.class, 0, Side.CLIENT);
		net.registerMessage(new PriorityMessage.Handler(), PriorityMessage.class, 1, Side.SERVER);
		net.registerMessage(new RenameMessage.Handler(), RenameMessage.class, 2, Side.SERVER);
		net.registerMessage(new UpgradesMessage.Handler(), UpgradesMessage.class, 3, Side.CLIENT);
		net.registerMessage(new MasterSetMessage.Handler(), MasterSetMessage.class, 4, Side.CLIENT);
		net.registerMessage(new WayPointMessage.Handler(), WayPointMessage.class, 5, Side.CLIENT);
		net.registerMessage(new VoidFXMessage.Handler(), VoidFXMessage.class, 6, Side.CLIENT);
		net.registerMessage(new ConnectionModeMessage.Handler(), ConnectionModeMessage.class, 7, Side.SERVER);
		net.registerMessage(new MAFXMessage.Handler(), MAFXMessage.class, 8, Side.CLIENT);
		net.registerMessage(new SwitchModeMessage.Handler(), SwitchModeMessage.class, 9, Side.SERVER);
		proxy.preInit(event);
	}

	public static Map<Object, Class> OCApi = new HashMap<>();

	/**
	 * @param event
	 */
	@Mod.EventHandler

	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		
		new KeyBindings();
		
		AEApi.instance().partHelper().registerNewLayer("reobf.proghatches.fmp.LazerLayer",
				"reobf.proghatches.eucrafting.ILazer");
		AEApi.instance().partHelper().registerNewLayer("reobf.proghatches.fmp.LayerCraftingMachine",
				"appeng.api.implementations.tiles.ICraftingMachine");
		

		OCApi.put(iohub, TileIOHub.OCApi.class);
		OCApi.put(oc_api, ItemAPICard.APIEnv.class);
		OCApi.put(oc_redstone, ItemGTRedstoneCard.RedstoneEnv.class);
		OCApi.put(reader, TileCardReader.class);
		OCApi.put(new ItemStack( GregTechAPI.sBlockMachines,
		         1,
		         Config.metaTileEntityOffset+Registration.DualInputHatchOCOffset), DualInputHachOC.class);
		OCApi.forEach((k, v) -> Driver.add(new li.cil.oc.api.driver.EnvironmentProvider() {

			@Override
			public Class<?> getEnvironment(ItemStack itemStack) {
				Object kk = k;
				if (kk instanceof Block) {
					kk = Item.getItemFromBlock((Block) kk);
				}
				if (itemStack != null && (itemStack.getItem() == kk||(kk instanceof ItemStack?(
						((ItemStack)kk).getItem()==itemStack.getItem()&&((ItemStack)kk).getItemDamage()==itemStack.getItemDamage()
						):false))) {
					return v;
				}
				return null;
			}
		}));
	}

	@SubscribeEvent
	public void overrideTutorialBookClickBehaviour(PlayerInteractEvent ev) {
		if (Optional.ofNullable(ev.entityPlayer.getHeldItem()).map(ItemStack::getItem)
				.orElse(Items.apple) instanceof ItemBookTutorial) {
			if (Optional.ofNullable(ev.entityPlayer.getHeldItem().stackTagCompound)
					.filter(s -> s.hasKey("proghatchesSpecialTag")).isPresent()) {

				ev.setCanceled(true);
				if (ev.world.isRemote) {
					ev.entityPlayer.displayGUIBook(tutorial(Items.written_book,
							ev.entityPlayer.getHeldItem().getTagCompound().getString("title")));
					// actual contents are localized on client side
				}

			}

		}

	}

	@SubscribeEvent
	public void join(PlayerLoggedInEvent e) {
		// if(Config.fixCircuit)
		// e.player.addChatComponentMessage(new
		// ChatComponentTranslation("proghatch.join.fixCircuit"));

		/*
		 * if
		 * (e.player.getEntityData().hasKey("ProgrammableHatchesTutorialGet3")
		 * == false) { e.player.getEntityData().setBoolean(
		 * "ProgrammableHatchesTutorialGet3", true);
		 */

		if (e.player.getExtendedProperties(GET_PROGHATCHBOOK) != null) {
			Prop p = (Prop) e.player.getExtendedProperties(GET_PROGHATCHBOOK);
			if (p.get) {
				return;
			}
			p.get = true;
			
			/*EntityItem entityitem = e.player.dropPlayerItemWithRandomChoice(
					Optional.of(tutorial("programmable_hatches.eucreafting.tutorial")).map(s -> {
						s.stackTagCompound.setString("proghatchesSpecialTag", "true");
						return s;
					}).get(), false);
			entityitem.delayBeforeCanPickup = 0;
			entityitem.func_145797_a(e.player.getCommandSenderName());
			*/
			EntityItem entityitem = e.player.dropPlayerItemWithRandomChoice(Optional.of(tutorial()).map(s -> {
				s.stackTagCompound.setString("proghatchesSpecialTag", "true");
				return s;
			}).get(), false);
			entityitem.delayBeforeCanPickup = 0;
			entityitem.func_145797_a(e.player.getCommandSenderName());

		}
		;

	}
	public static Achievement achievement;
	/**
	 * @param event
	 */
	@Mod.EventHandler

	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
		//KeyBindings
		
	
		{
			AchievementPage page = new AchievementPage(MODID, achievement=new Achievement("proghatch.toolkit", "proghatch.toolkit", 0, 0, new ItemStack(toolkit), null ).registerStat());
			AchievementPage.registerAchievementPage(page);
		    /*FMLCommonHandler.instance().bus().register(new Object(){
		    	   @SubscribeEvent
		    	    public void onItemPickedUp(PlayerEvent.ItemPickupEvent event) {
		    		   ItemStack stack = event.pickedUp.getEntityItem();

		               if (stack != null && stack.getItem() == toolkit) {
		                   Achievement achievement = tmp;

		                   if (achievement != null) {
		                       event.player.addStat(achievement, 1);
		                   }
		               }
		    	   }
		    	
		    });*/
		}
		
		
		
		//MinecraftForge.EVENT_BUS.register(this);
		// Api.INSTANCE.registries().p2pTunnel().addNewAttunement(null, null);
		// ShutDownReasonRegistry.register(ACCESS_LOOP);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		for (ItemStack s : new ItemStack[] { new ItemStack(block_euinterface), new ItemStack(euinterface_p2p) })

		{
			Upgrades.CRAFTING.registerItem(s, 1);

			Upgrades.PATTERN_CAPACITY.registerItem(s, 3);

			// Upgrades.CRAFTING.registerItem(s, 1);

			Upgrades.ADVANCED_BLOCKING.registerItem(s, 1);
		}
		InterfaceTerminalRegistry.instance().register(InterfaceData.class);
		InterfaceTerminalRegistry.instance().register(InterfaceData.FluidInterfaceData_TileFluidInterface.class);
		InterfaceTerminalRegistry.instance().register(PartEUP2PInterface.class);
		InterfaceTerminalRegistry.instance().register(PartFluidP2PInterface.class);
		InterfaceTerminalRegistry.instance().register(TileFluidInterface_EU.class);
		InterfaceTerminalRegistry.instance().register(PatternDualInputHatch.Inst.class);
		InterfaceTerminalRegistry.instance().register(PatternDualInputHatchInventoryMappingSlave.class);

		// InterfaceTerminalRegistry.instance().register(ProgrammingCircuitProvider.class);
		// InterfaceTerminalRegistry.instance().register(LargeProgrammingCircuitProvider.class);

		
		//IMultiblockInfoContainer.MULTIBLOCK_MAP.put(GET_PROGHATCHBOOK, null)
		// ItemList list=new ItemList();
		// list.add(AEItemStack.create(ItemProgrammingCircuit.wrap(new
		// ItemStack(Blocks.cactus))));
		// list.findFuzzy(AEItemStack.create(ItemProgrammingCircuit.wrap(new
		// ItemStack(Blocks.bed))), FuzzyMode.IGNORE_ALL);
		ConduitDisplayMode.registerDisplayMode(new ConduitDisplayMode(
				
				ICraftingMachineConduit.class, MASettings.Holder.getMAIcon(), MASettings.Holder.getMAIcon()));
		Map<OffsetKey, Offset> OFFSETS =null;
		{try {
			Field f=Offsets.class
			.getDeclaredField("OFFSETS");
			f.setAccessible(true);
			OFFSETS=(Map<OffsetKey, Offset>) f.get(null);
		} catch (Exception e) {e.printStackTrace();
		}
		
		
		}
		 OFFSETS.put(Offsets.key(ICraftingMachineConduit.class, Axis.NONE), Offset.SOUTH_DOWN);
         OFFSETS.put(Offsets.key(ICraftingMachineConduit.class, Axis.X), Offset.SOUTH_DOWN);
         OFFSETS.put(Offsets.key(ICraftingMachineConduit.class, Axis.Y), Offset.SOUTH_EAST);
         OFFSETS.put(Offsets.key(ICraftingMachineConduit.class, Axis.Z), Offset.EAST_DOWN);
		
		
	}

	@SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = false)
	public void tick(final TickEvent.ServerTickEvent event) {
		while (scheduled.isEmpty() == false)
			scheduled.removeLast().run();
	}

	@SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = false)
	public void playerInteract(final PlayerInteractEvent event) {

		a: if (event.action == Action.RIGHT_CLICK_BLOCK && !event.world.isRemote) {
			TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
			if (te == null || !(te instanceof ICoverable))
				break a;
			ICoverable tileEntity = (ICoverable) te;

			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
				if (tileEntity.getCoverBehaviorAtSideNew(side) instanceof AECover)
					GTValues.NW.sendToPlayer(
							new GTPacketSendCoverData(side, tileEntity.getCoverIDAtSide(side),
									tileEntity.getComplexCoverDataAtSide(side), tileEntity),
							(EntityPlayerMP) event.entityPlayer);

			}

		}

		if (event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer.isSneaking()) {
			if (Optional.ofNullable(event.entityPlayer.getHeldItem()).map(s -> s.getItem())
					.filter(s -> s instanceof ToolMemoryCard).isPresent()) {
				IMemoryCardSensitive cv = Optional.ofNullable(event.world.getTileEntity(event.x, event.y, event.z))
						.map(s -> s instanceof ICoverable ? (ICoverable) s : null)
						.map(s -> s.getComplexCoverDataAtSide(ForgeDirection.getOrientation(event.face)))
						.map(s -> s instanceof AECover.IMemoryCardSensitive ? (AECover.IMemoryCardSensitive) s : null)
						.orElse(null);

				if (cv != null) {
					cv.memoryCard(event.entityPlayer);

					event.setCanceled(true);
					return;

				}

			}

		}

	}

	@Mod.EventHandler

	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
		WirelessPeripheralManager.stations.clear();
		WirelessPeripheralManager.cards.clear();
		ChunkTrackingGridCahce.cacheinst.clear();
		
		ProgrammingCircuitProviderPrefabricated.init=false;
		ProgrammingCircuitProviderPrefabricated.prefab.clear();
		// Just in case weak references are not GCed in time
		// only useful for intergreted server?
		//event.registerServerCommand(new CommandAnchor());
		//event.registerServerCommand(new CommandAnchor2());
	}

	public static ItemStack tutorial() {
		return tutorial(book, "programmable_hatches.tutorial");
	}

	public static ItemStack tutorial(Item it) {
		return tutorial(book, "programmable_hatches.tutorial");

	}

	public static ItemStack tutorial(String key) {
		return tutorial(book, key);
	}

	public static ItemStack tutorial(Item it, String key) {

		ArrayList<String> pages = new ArrayList<>();
		int size = Integer.valueOf(LangManager.translateToLocalFormatted(key + ".pages"));
		for (int i = 0; i < size; i++) {
			// System.out.println(LangManager.translateToLocalFormatted(key +
			// ".pages." + i));
			pages.add(LangManager.translateToLocalFormatted(key + ".pages." + i).replace("\\n", "\n"));
		}
		ItemStack is = ProghatchesUtil.getWrittenBook(it, "ProgrammableHatchesTutorial", key, "programmable_hatches",
				pages.toArray(new String[0]));
		is.stackTagCompound.setString("proghatchesSpecialTag", "true");
		return is;

	}

	/*
	 * public static WeakHashMap<World,Collection<Runnable>> call=new
	 * WeakHashMap<>(); public static WeakHashMap<Chunk,Collection<Runnable>>
	 * call2=new WeakHashMap<>();
	 * 
	 * @SubscribeEvent public void onWorldUnload(WorldEvent.Unload event) {
	 * if(event.world.isRemote)return;
	 * call.get(event.world).forEach(Runnable::run); call.remove(event.world);
	 * 
	 * }
	 * 
	 * @SubscribeEvent public void onChunkUnload(ChunkEvent.Unload event) {
	 * if(event.world.isRemote)return;
	 * call2.get(event.getChunk()).forEach(Runnable::run);
	 * call2.remove(event.getChunk()); } public static void reg(TileEntity
	 * host,Runnable cb){ call.computeIfAbsent(host.getWorldObj(), s->new
	 * ArrayList<>()); call.get(host).add(cb); call2.computeIfAbsent(host.getc,
	 * s->new ArrayList<>()); call2.get(host).add(cb);
	 * 
	 * 
	 * 
	 * 
	 * }
	 */
	@SubscribeEvent
	public void breakBlock(BlockEvent.BreakEvent b) {
		// System.out.println(b.block);

		if (b.block instanceof BlockMachines) {
			TileEntity te = b.world.getTileEntity(b.x, b.y, b.z);
			if (te instanceof ICoverable) {
				ICoverable c = (ICoverable) te;
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
					Optional.ofNullable(c.getComplexCoverDataAtSide(dir)).ifPresent(s -> {
						if (s instanceof AECover.Data) {
							((AECover.Data) s).destroy();

						}

					});

				}
			}

		}

	}

	@SubscribeEvent
	public void  onUnload(WorldEvent.Unload event) {
		if(disable)return;
		if (event.world.isRemote)
			return;
		// World unloading seems to not post ChunkEvent.Unload?
		// Well, warning twice is better than not warning, right?
		try {//System.out.println(ChunkTrackingGridCahce.cacheinst.size());
			max=Math.max(max, ChunkTrackingGridCahce.cacheinst.size());
			ChunkTrackingGridCahce.cacheinst.removeIf((aa) -> {
				ChunkTrackingGridCahce a = aa;
				if(a.myGrid.getPivot()==null){
					//System.out.println(a.myGrid);
				
				a=null;}
				if(a==null){
					return true;
					}
				if (a != null) {
					a.unload(event.world);
				}
				
				return false;
			});//System.out.println(ChunkTrackingGridCahce.cacheinst.size());
		} catch (Throwable t) {
			throw new AssertionError(t);
		}

	}
	static int max=0;
	final static boolean disable=true;
	@SubscribeEvent
	public void onLoad(ChunkEvent.Load event) {
		if(disable)return;
		if (event.world.isRemote)
			return;
		
		try {
			//System.out.println(ChunkTrackingGridCahce.cacheinst.size());
			max=Math.max(max, ChunkTrackingGridCahce.cacheinst.size());
			ChunkTrackingGridCahce.cacheinst.removeIf((aa) -> {
				ChunkTrackingGridCahce a = aa;
				if(a.myGrid.getPivot()==null){
				//	System.out.println(a.myGrid);
				a=null;
				
				}
				if(a==null){
					return true;
					}
				if (a != null) {
					a.load(event.getChunk());
				}
				
				return false;
			});
			//System.out.println(ChunkTrackingGridCahce.cacheinst.size());
			
		} catch (Throwable t) {
			throw new AssertionError(t);
			
		}
	}
	@SubscribeEvent
	public void onUnload(ChunkEvent.Unload event) {
		if(disable)return;
		if (event.world.isRemote)
			return;
		// on client side, out-of-sight causes chunk unload! That's not what we
		// want, so ignore it.
		try {
			max=Math.max(max, ChunkTrackingGridCahce.cacheinst.size());
		//	System.out.println(ChunkTrackingGridCahce.cacheinst.size());
			ChunkTrackingGridCahce.cacheinst.removeIf((aa) -> {
				ChunkTrackingGridCahce a = aa;
				if(a.myGrid.getPivot()==null){
					//System.out.println(a.myGrid);
				a=null;
				}
				if(a==null){
					return true;
					}
				if (a != null) {
					a.load(event.getChunk());
				}
				
				return false;
			});//System.out.println(ChunkTrackingGridCahce.cacheinst.size());
		} catch (Throwable t) {
			throw new AssertionError(t);
		}
	}

	public static final String GET_PROGHATCHBOOK = "GET_PROGHATCHBOOK";

	@SubscribeEvent

	public void onEntityConstructing(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer
				&& ((EntityPlayer) event.entity).getExtendedProperties("GET_PROGHATCHBOOK") == null) {
			event.entity.registerExtendedProperties(GET_PROGHATCHBOOK, new Prop());
		}
	}

	public static class Prop implements IExtendedEntityProperties {
		boolean get;
		public int alert_mask;

		@Override
		public void saveNBTData(NBTTagCompound compound0) {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setInteger("alert_mask", alert_mask);
			compound.setBoolean(GET_PROGHATCHBOOK + "_get", get);
			compound0.setTag(GET_PROGHATCHBOOK, compound);
		}

		@Override
		public void loadNBTData(NBTTagCompound compound0) {
			NBTTagCompound compound = compound0.getCompoundTag(GET_PROGHATCHBOOK);
			get = compound.getBoolean(GET_PROGHATCHBOOK + "_get");
			alert_mask = compound.getInteger("alert_mask");
		}

		@Override
		public void init(Entity entity, World world) {

		}
	}

	public static WeakHashMap<Object, Runnable> callbacks = new WeakHashMap<>();
	public static Block reactorsyncer;
	public static Block storageproxy;
	public static Item partproxy;
	public static Item exciter;

	public static Block[] condensers=new Block[16];
	public static Item stockingexport;
	public static Item ma_p2p_part;
	public static ItemMAConduit ma_conduit;
	public static Block circuit_interceptor;
	public static BlockMolecularAssemblerInterface ma_iface;
	public static BlockAutoFillerMKII autofiller;
	//public static Block occonfigurator;
	public static BlockRequestTunnel request_tunnel;
	public static Item emitterpattern;
	public static boolean newGTCache;
	public static Item part_tunnel;

	@SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = false)
	public void pretick(final TickEvent.ServerTickEvent event) {
		if (event.phase == Phase.START && event.side == Side.SERVER) {
			callbacks.forEach((a, b) -> b.run());
		}
	}
	
	
}
