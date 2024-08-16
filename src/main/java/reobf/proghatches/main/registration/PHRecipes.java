package reobf.proghatches.main.registration;

import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.print.DocFlavor.INPUT_STREAM;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.github.technus.tectech.thing.CustomItemList;
import com.glodblock.github.loader.ItemAndBlockHolder;

import appeng.core.Api;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_Utility;
import mods.railcraft.common.core.Railcraft;
import reobf.proghatches.item.ItemProgrammingCircuit;
import reobf.proghatches.main.Config;
import reobf.proghatches.main.MyMod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.common.config.ConfigItems;
//spotless:off
public class PHRecipes implements Runnable {
    ItemStack[] arms = { Robot_Arm_LV.get(1), // GT++ deprecated ulv tier arm
            Robot_Arm_LV.get(1), Robot_Arm_MV.get(1), Robot_Arm_HV.get(1), Robot_Arm_EV.get(1), Robot_Arm_IV.get(1),
            Robot_Arm_LuV.get(1), Robot_Arm_ZPM.get(1), Robot_Arm_UV.get(1), Robot_Arm_UHV.get(1), Robot_Arm_UEV.get(1),
            Robot_Arm_UIV.get(1), Robot_Arm_UMV.get(1), Robot_Arm_UXV.get(1), Robot_Arm_MAX.get(1) };
      Materials[][] mat = { 
        		{ Primitive }, 
        		{ Basic }, 
        		{ Good }, 
        		{ Advanced }, 
        		{ Data }, 
        		{ Elite }, 
        		{ Master },
        		{ Ultimate }, 
        		{ SuperconductorUHV }, 
        		{ Infinite }, 
        		{ Bio },
        		{ Optical/*,Nano,*/ },
        		{ Piko/*,Exotic,*/  },//dreamcraft circuit
        		{ Quantum/*,Cosmic,*/  }, 
        		{ Quantum /*Transcendent*/ } };
     
      Materials[][] matNewVersion = { 
      		{ Primitive }, 
      		{ Basic }, 
      		{ Good }, 
      		{ Advanced }, 
      		{ Data }, 
      		{ Elite }, 
      		{ Master },
      		{ Ultimate }, 
      		{ SuperconductorUHV }, 
      		{ Infinite }, 
      		{ Bio },
      		{ Optical },
      		{ Exotic  },
      		{ Cosmic  }, 
      		{ Cosmic/*Transcendent*/ }}; 
      
   
 ItemList[] multi = { null, null, null, null, Hatch_Input_Multi_2x2_EV, Hatch_Input_Multi_2x2_IV,
        	            Hatch_Input_Multi_2x2_LuV, Hatch_Input_Multi_2x2_ZPM, Hatch_Input_Multi_2x2_UV, Hatch_Input_Multi_2x2_UHV,
        	            Hatch_Input_Multi_2x2_UEV,

        	            Hatch_Input_Multi_2x2_UIV, Hatch_Input_Multi_2x2_UMV, Hatch_Input_Multi_2x2_UXV,
        	            Hatch_Input_Multi_2x2_Humongous };
    @Override
    public void run() {

       
if(Config.skipRecipeAdding)return;

if(
GameRegistry.findItem("dreamcraft","item.PolychromePikoCircuit")!=null
){
	  MyMod.LOG.info("Found new dreamcraft Nano-Piko-Quantum circuit, use oredict: Exotic-Cosmic-Transcendent.");
	mat=matNewVersion;}
else{
	 MyMod.LOG.info("Old version.");
	
}
//You just like breaking changes, isn't that true, GTNH dev?


IRecipe rec = new ShapedOreRecipe(new ItemStack(MyMod.plunger),"CRR","TSR","Q F",
		'R',"plateAnyRubber",
		'C',"craftingToolWireCutter",
		'F',"craftingToolFile",
		'Q',"stickCertusQuartz",
		'S',
		new ItemStack(
				GameRegistry.findItem("appliedenergistics2","item.ToolChargedStaff"),1,
				OreDictionary.WILDCARD_VALUE
				)
		
		//Api.INSTANCE.definitions().items().chargedStaff().maybeStack(1).get()
		//ApiItems.chargedStaff() returns memoryCard, not chargedStaff
		//What are you doing GTNH dev?
		
		,
		'T',
		
		new ItemStack(
				GameRegistry.findItem("appliedenergistics2","item.ToolWirelessTerminal"),1,
				OreDictionary.WILDCARD_VALUE
				)
	
		);
CraftingManager.getInstance().getRecipeList().add(rec);







        ArrayList<ItemStack> pc0 = new ArrayList<>();
        HashMap<ItemStack, Integer/* item->productivity */> pc = new HashMap<>();// List<Pair<ItemStack,Integer>>

        no: {
            Item im = GameRegistry.findItem("bartworks", "gt.BWCircuitProgrammer");
            if (im == null) {
                MyMod.LOG.fatal("BW Programmer not found! Did you install GTNH properly?");
                break no;
            }
            pc0.add(new ItemStack(im, 0));
            pc.put(new ItemStack(im, 1), 4);

        }
        no: {
            Item im = GameRegistry.findItem("miscutils", "blockCircuitProgrammer");
            if (im == null) {
                MyMod.LOG.fatal("GT++ Programmer not found! Did you install GTNH properly?");
                break no;
            }

            pc0.add(new ItemStack(im, 0));
            pc.put(new ItemStack(im, 1), 1);
        }
        // in dev env, use bedrock to replace programmer to debug
        Optional.of(pc0)
            .filter(List::isEmpty)
            .ifPresent(s -> s.add(new ItemStack(Blocks.bedrock, 0)));
        Optional.of(pc)
            .filter(Map::isEmpty)
            .ifPresent((s) -> s.put(new ItemStack(Blocks.bedrock, 1), 1));

        pc.forEach((s, i) -> {
        	
            GT_Values.RA.stdBuilder()
                .itemInputs(s, Materials.Titanium.getPlates(1))
                .fluidInputs(Materials.TungstenSteel.getMolten(144*20L))
                .itemOutputs(new ItemStack(MyMod.cover, 4 * i, 0))
                .duration(1 * SECONDS)
                .eut(480)
                .addTo(RecipeMaps.mixerRecipes);
            GT_Values.RA.stdBuilder()
            .itemInputs(s, Materials.Titanium.getPlates(1))
            .fluidInputs(Materials.StainlessSteel.getMolten(144*20L))
            .itemOutputs(new ItemStack(MyMod.cover, 2 * i, 0))
            .duration(1 * SECONDS)
            .eut(480)
            .addTo(RecipeMaps.mixerRecipes);
            
        });
        pc0.forEach(s -> {
            GregTech_API.getConfigurationCircuitList(100)
                .stream()
                .forEach(ss -> {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(s, GT_Utility.copyAmount(0, ss))

                        .itemOutputs(ItemProgrammingCircuit.wrap(ss))
                        .duration(1 * SECONDS)
                        .eut(120)
                        .addTo(RecipeMaps.hammerRecipes);

                });;

        });
       
        ArrayList<ItemStack> is = Arrays.stream(HATCHES_INPUT)
            .map(s -> s.get(1))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        for (int i = 12097; i <= 12102; i++) {
            is.add(new ItemStack(GregTech_API.sBlockMachines, 1, i));
        } // stuff from gtnhcoremod
        ItemStack[] single_fluid = is.toArray(new ItemStack[0]);
        is = Arrays.stream(HATCHES_INPUT_BUS)
            .map(s -> s.get(1))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        is.add(new ItemStack(GregTech_API.sBlockMachines, 1, 30030));// gt++ superbus
        is.add(new ItemStack(GregTech_API.sBlockMachines, 1, 30030));
        is.add(new ItemStack(GregTech_API.sBlockMachines, 1, 30030));
        is.add(new ItemStack(GregTech_API.sBlockMachines, 1, 30030));
        is.add(new ItemStack(GregTech_API.sBlockMachines, 1, 30030));
        ItemStack[] single_item = is.toArray(new ItemStack[0]);

        for (int i = 4; i < GT_Values.VN.length - 1; i++) {

            GT_Values.RA.stdBuilder()
                .itemInputs(multi[i].get(1), single_item[i], new ItemStack(MyMod.cover, 1, 0))
                .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
                .itemOutputs(
                    new ItemStack(
                        GregTech_API.sBlockMachines,
                        1,
                        Config.metaTileEntityOffset + Registration.QuadDualInputHatchOffset + i))
                .duration(20 * SECONDS)
                .eut(GT_Values.VP[i])
                .addTo(RecipeMaps.mixerRecipes);
        }

        for (int i = 0; i < GT_Values.VN.length - 1; i++) {

            GT_Values.RA.stdBuilder()
                .itemInputs(single_fluid[i], single_item[i], new ItemStack(MyMod.cover, 1, 0))
                .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
                .itemOutputs(
                    new ItemStack(
                        GregTech_API.sBlockMachines,
                        1,
                        Config.metaTileEntityOffset + Registration.DualInputHatchOffset + i))
                .duration(20 * SECONDS)
                .eut(GT_Values.VP[i])
                .addTo(RecipeMaps.mixerRecipes);
        }

        for (int i = 0; i < GT_Values.VN.length - 1; i++) {
            Object circuit = OrePrefixes.circuit.get(mat[i][0]);
            Object circuitP = OrePrefixes.circuit.get(mat[Math.min(i + 1, mat.length - 2)][0]);
            if (circuit == null || GT_OreDictUnificator.get(circuit, 1) == null) {
                MyMod.LOG.fatal("Circuit not found for " + GT_Values.VN[i] + "!");
                continue;
            }

            /*
             * //GT OreDict handling is too magic for me to understand
             * GT_Values.RA.stdBuilder()
             * .itemInputs(
             * new
             * ItemStack(GregTech_API.sBlockMachines,1,Config.metaTileEntityOffset+Registration.DualInputHatchOffset+i)
             * , new Object[] {circuit,2}
             * ,new ItemStack(MyMod.smartarm,1,i)
             * )
             * .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
             * .itemOutputs(
             * new ItemStack(GregTech_API.sBlockMachines,1,Config.metaTileEntityOffset+Registration.
             * BufferedDualInputHatchOffset+i)
             * )
             * .duration(20 * SECONDS)
             * .eut(GT_Values.VP[i])
             * .addTo(RecipeMaps.assemblerRecipes);
             */

            RecipeMaps.assemblerRecipes.add(
                new GT_Recipe/*.GT_Recipe_WithAlt*/(
                    false,
                    new ItemStack[] { GT_OreDictUnificator.get((circuit), 2), single_item[i],
                        new ItemStack(
                            GregTech_API.sBlockMachines,
                            1,
                            Config.metaTileEntityOffset + Registration.DualInputHatchOffset + i)

                        , new ItemStack(MyMod.smartarm, 1, i) }// II
                    ,
                    new ItemStack[] { new ItemStack(
                        GregTech_API.sBlockMachines,
                        1,
                        Config.metaTileEntityOffset + Registration.BufferedDualInputHatchOffset + i) }
                    // IO
                    ,
                    null// SP
                    ,
                    null// CHANCE
                    ,
                    new FluidStack[] { Materials.AdvancedGlue.getFluid(4000) }// FI
                    ,
                    null// FO
                    ,
                    20 * SECONDS,
                    (int) GT_Values.VP[i],
                    0/*,
                    new ItemStack[][] {

                        Arrays.stream(mat[i])
                            .map(s -> GT_OreDictUnificator.getOres(OrePrefixes.circuit, s))
                            .flatMap(ArrayList::stream)
                            .map(s -> GT_Utility.copyAmount(2, s))
                            .toArray(ItemStack[]::new) } */// ALT
                ));
            RecipeMaps.assemblerRecipes.add(
                new GT_Recipe(
                    false,
                    new ItemStack[] { GT_OreDictUnificator.get((circuit), 2), single_item[i],
                        new ItemStack(
                            GregTech_API.sBlockMachines,
                            1,
                            Config.metaTileEntityOffset + Registration.QuadDualInputHatchOffset + i)

                        , new ItemStack(MyMod.smartarm, 1, i) }// II
                    ,
                    new ItemStack[] { new ItemStack(
                        GregTech_API.sBlockMachines,
                        1,
                        Config.metaTileEntityOffset + Registration.BufferedQuadDualInputHatchOffset + i) }
                    // IO
                    ,
                    null// SP
                    ,
                    null// CHANCE
                    ,
                    new FluidStack[] { Materials.Osmiridium.getMolten(144*20) }// FI
                    ,
                    null// FO
                    ,
                    20 * SECONDS,
                    (int) GT_Values.VP[i],
                    0/*,
                    new ItemStack[][] {

                        Arrays.stream(mat[i])
                            .map(s -> GT_OreDictUnificator.getOres(OrePrefixes.circuit, s))
                            .flatMap(ArrayList::stream)
                            .map(s -> GT_Utility.copyAmount(2, s))
                            .toArray(ItemStack[]::new) } */// ALT
                ));
            RecipeMaps.assemblerRecipes.add(
                new GT_Recipe(
                    false,
                    new ItemStack[] { GT_OreDictUnificator.get(circuitP, 2),
                        GT_Utility.copyAmount(5, single_item[i]),
                        new ItemStack(
                            GregTech_API.sBlockMachines,
                            1,
                            Config.metaTileEntityOffset + Registration.BufferedQuadDualInputHatchOffset + i),
                        new ItemStack(MyMod.smartarm, 5, i) }// II
                    ,
                    new ItemStack[] { new ItemStack(
                        GregTech_API.sBlockMachines,
                        1,
                        Config.metaTileEntityOffset + Registration.BufferedQuadDualInputHatchMKIIOffset + i) }
                    // IO
                    ,
                    null// SP
                    ,
                    null// CHANCE
                    ,
                    new FluidStack[] { Materials.Neutronium.getMolten(144*60) }// FI
                    ,
                    null// FO
                    ,
                    20 * SECONDS,
                    (int) GT_Values.VP[i],
                    0/*,
                    new ItemStack[][] {

                        Arrays.stream(mat[Math.min(i + 1, mat.length - 1)])
                            .map(s -> GT_OreDictUnificator.getOres(OrePrefixes.circuit, s))
                            .flatMap(ArrayList::stream)
                            .map(s -> GT_Utility.copyAmount(2, s))
                            .toArray(ItemStack[]::new) } */// ALT
                ));
        }

        pc.forEach((s, i) -> {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    s,
                    new ItemStack(ItemAndBlockHolder.INTERFACE),
                    new ItemStack(MyMod.toolkit, 0, OreDictionary.WILDCARD_VALUE),
                    	(
                        Api.INSTANCE.definitions()
                            .blocks()
                            .craftingStorage256k()
                            .maybeStack(1)
                            .get()),
                    	(
                        Api.INSTANCE.definitions()
                            .blocks()
                            .craftingAccelerator4x()
                            .maybeStack(1)
                            .get()),
                    new ItemStack(
                            Api.INSTANCE.definitions()
                                .blocks()
                                .molecularAssembler()
                                .maybeItem()
                                .get())
                        
                		
                		)
                .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
                .itemOutputs(
                    new ItemStack(
                        GregTech_API.sBlockMachines,
                        1,
                        Config.metaTileEntityOffset + Registration.CircuitProviderOffset))
                .duration(20 * SECONDS)
                .eut(GT_Values.VP[5])
                .addTo(RecipeMaps.assemblerRecipes);
//T0
            GT_Values.RA.stdBuilder()
            .itemInputs(
                s,
              Api.INSTANCE.definitions().blocks().iface().maybeStack(1).get(),
                new ItemStack(MyMod.toolkit, 0, OreDictionary.WILDCARD_VALUE),
                	(
                    Api.INSTANCE.definitions()
                        .blocks()
                        .craftingStorage1k()
                        .maybeStack(1)
                        .get()),
                	(
                    Api.INSTANCE.definitions()
                        .blocks()
                        .craftingAccelerator()
                        .maybeStack(1)
                        .get()),
                	
                               Machine_HV_Assembler.get(1)
                    
            		
            		)
            .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
            .itemOutputs(
                new ItemStack(
                    GregTech_API.sBlockMachines,
                    1,
                    Config.metaTileEntityOffset + Registration.CircuitProviderOffsetT0))
            .duration(20 * SECONDS)
            .eut(GT_Values.VP[3])
            .addTo(RecipeMaps.assemblerRecipes);
        });

        pc0.forEach((s) -> {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    s,
                    Hatch_CraftingInput_Bus_Slave.get(1),

                    GT_Utility.getIntegratedCircuit(13))
                .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
                .itemOutputs(
                    new ItemStack(
                        GregTech_API.sBlockMachines,
                        1,
                        Config.metaTileEntityOffset + Registration.SlaveOffset))
                .duration(20 * SECONDS)
                .eut(GT_Values.VP[7])
                .addTo(RecipeMaps.assemblerRecipes);

        });

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(GameRegistry.findItem("RIO", "tile.remote_interface")),
                new ItemStack(GameRegistry.findItem("RIO", "item.chip.transfer"), 1, 0),
                single_item[6])
            .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
            .itemOutputs(
                new ItemStack(
                    GregTech_API.sBlockMachines,
                    1,
                    Config.metaTileEntityOffset + Registration.RemoteInputBusOffset))
            .duration(20 * SECONDS)
            .eut(GT_Values.VP[5])
            .addTo(RecipeMaps.assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(GameRegistry.findItem("RIO", "tile.remote_interface")),
                new ItemStack(GameRegistry.findItem("RIO", "item.chip.transfer"), 1, 1),
                single_fluid[6]

            )
            .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
            .itemOutputs(
                new ItemStack(
                    GregTech_API.sBlockMachines,
                    1,
                    Config.metaTileEntityOffset + Registration.RemoteInputHatchOffset))
            .duration(20 * SECONDS)
            .eut(GT_Values.VP[5])
            .addTo(RecipeMaps.assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(

                new ItemStack(
                    GregTech_API.sBlockMachines,
                    1,
                    Config.metaTileEntityOffset + Registration.BufferedQuadDualInputHatchOffset + 8),
                Quantum_Tank_LV.get(24),
                Electric_Pump_UV.get(24),
                Cover_Shutter.get(24))
            .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
            .itemOutputs(
                new ItemStack(
                    GregTech_API.sBlockMachines,
                    1,
                    Config.metaTileEntityOffset + Registration.SuperFluidHatch))
            .duration(20 * SECONDS)
            .eut(GT_Values.VP[8])
            .addTo(RecipeMaps.assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Hatch_CraftingInput_Bus_ME.get(1),
                new ItemStack(
                    GregTech_API.sBlockMachines,
                    1,
                    Config.metaTileEntityOffset + Registration.BufferedQuadDualInputHatchMKIIOffset + 8))
            .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
            .itemOutputs(
                new ItemStack(GregTech_API.sBlockMachines, 1, Config.metaTileEntityOffset + Registration.PatternOffset))
            .duration(20 * SECONDS)
            .eut(GT_Values.VP[9])
            .addTo(RecipeMaps.assemblerRecipes);
        
        GT_Values.RA.stdBuilder()
        .itemInputs(
        		Hatch_CraftingInput_Bus_ME_ItemOnly.get(1),
            new ItemStack(
                MyMod.smartarm,4,6))
        .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
        .itemOutputs(
            new ItemStack(GregTech_API.sBlockMachines, 1, Config.metaTileEntityOffset + Registration.PatternOffsetBus))
        .duration(20 * SECONDS)
        .eut(GT_Values.VP[6])
        .addTo(RecipeMaps.assemblerRecipes);
        
        
        for (int i = 0; i < 4; i++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(single_item[i], new ItemStack(GameRegistry.findItem("Automagy", "blockTenaciousChest"))

                )
                .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
                .itemOutputs(
                    new ItemStack(
                        GregTech_API.sBlockMachines,
                        1,
                        Config.metaTileEntityOffset + Registration.TenaciousOffset + i))
                .duration(20 * SECONDS)
                .eut(GT_Values.VP[i])
                .addTo(RecipeMaps.assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(GameRegistry.findItem("Automagy", "glyph"), 1, 3),
                    new ItemStack(
                        GregTech_API.sBlockMachines,
                        1,
                        Config.metaTileEntityOffset + Registration.TenaciousOffset + i)

                )
                .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
                .itemOutputs(
                    new ItemStack(
                        GregTech_API.sBlockMachines,
                        1,
                        Config.metaTileEntityOffset + Registration.FilterOffset + i)

                )
                .duration(20 * SECONDS)
                .eut(GT_Values.VP[i])
                .addTo(RecipeMaps.assemblerRecipes);

        }

        GT_Values.RA.stdBuilder()
            .itemInputs(Cover_Controller.get(1), Cover_AdvancedRedstoneReceiverInternal.get(1)

            )
            .fluidInputs(Materials.AdvancedGlue.getFluid(4000))
            .itemOutputs(new ItemStack(MyMod.cover, 1, 1)

            )
            .duration(20 * SECONDS)
            .eut(GT_Values.VP[2])
            .addTo(RecipeMaps.mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(GameRegistry.findItem("OpenComputers", "item"), 1, 33),
                GT_Utility.getIntegratedCircuit(14),
                Machine_HV_Scanner.get(1),
                new ItemStack(GameRegistry.findItem("OpenComputers", "item"), 2, 25),
                new ItemStack(GameRegistry.findItem("OpenComputers", "item"), 2, 78))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144*20))
            .itemOutputs(new ItemStack(MyMod.oc_api, 1)

            )
            .duration(20 * SECONDS)
            .eut(GT_Values.VP[2])
            .addTo(RecipeMaps.circuitAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(GameRegistry.findItem("OpenComputers", "item"), 1, 33),
                GT_Utility.getIntegratedCircuit(15),
                Cover_AdvancedRedstoneReceiverInternal.get(1),
                Cover_AdvancedRedstoneTransmitterInternal.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144*20))
            .itemOutputs(new ItemStack(MyMod.oc_redstone, 1)

            )
            .duration(20 * SECONDS)
            .eut(GT_Values.VP[2])
            .addTo(RecipeMaps.circuitAssemblerRecipes);
        pc.forEach((s, i) -> {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    s,
                    ItemList.Tool_Scanner.get(1),
                    new ItemStack(
                        Api.INSTANCE.definitions()
                            .items()
                            .networkTool()
                            .maybeItem()
                            .get()))

                .itemOutputs(new ItemStack(MyMod.toolkit, 1)

                )
                .duration(20 * SECONDS)
                .eut(GT_Values.VP[3])
                .addTo(RecipeMaps.assemblerRecipes);
        });

        pc0.forEach((s) -> {
            GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(Items.book), s)
                .fluidInputs(Materials.SolderingAlloy.getMolten(144))
                .itemOutputs(MyMod.tutorial()

                )
                .duration(20 * SECONDS)
                .eut(GT_Values.VP[2])
                .addTo(RecipeMaps.assemblerRecipes);
        });
      
            GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(Items.book), new ItemStack(GameRegistry.findItem("IC2","itemBatREDischarged"),0,OreDictionary.WILDCARD_VALUE))
                .fluidInputs(Materials.BatteryAlloy.getMolten(144))
                .itemOutputs(MyMod.tutorial("programmable_hatches.eucreafting.tutorial")

                )
                .duration(20 * SECONDS)
                .eut(GT_Values.VP[2])
                .addTo(RecipeMaps.assemblerRecipes);
            GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.book), new ItemStack(GameRegistry.findItem("IC2","itemBatRE"),0,OreDictionary.WILDCARD_VALUE))
            .fluidInputs(Materials.BatteryAlloy.getMolten(144))
            .itemOutputs(MyMod.tutorial("programmable_hatches.eucreafting.tutorial")

            )
            .duration(20 * SECONDS)
            .eut(GT_Values.VP[2])
            .addTo(RecipeMaps.assemblerRecipes);
      //////////////
      
       
  
            /////////
            smartArm();
 Item oc=GameRegistry.findItem("OpenComputers", "item");
 Item ocae=GameRegistry.findItem("OpenComputers", "item.ae");      
 GT_Values.RA.stdBuilder()
            .itemInputs(
            		new ItemStack(oc,2,53),//inv
            		new ItemStack(oc,1,61),//inv upgrade
            		new ItemStack(oc,2,76),//tank
            		new ItemStack(oc,1,77),//tank upgrade
            		new ItemStack(GameRegistry.findItem("OpenComputers", "transposer"))
            		,new ItemStack[]{
            				new ItemStack(ocae,1,0),
            				new ItemStack(ocae,1,1),
            				new ItemStack(ocae,1,2)//any tier
            				},
            		new ItemStack(GameRegistry.findItem("ae2fc", "fluid_interface"),1,77)
            		)
            .itemOutputs(new ItemStack(MyMod.iohub, 1, 0))
            .duration(1 * SECONDS)
            .eut(480)
            .addTo(RecipeMaps.mixerRecipes);
            
 GT_Values.RA.stdBuilder()
 .itemInputs(
     new ItemStack(GameRegistry.findItem("OpenComputers", "item"), 1, 33),
     new ItemStack(GameRegistry.findItem("OpenComputers", "item"), 1, 51),
     new ItemStack(GameRegistry.findItem("OpenComputers", "cable"), 4, 0),
     Emitter_EV.get(1),
     GT_Utility.getIntegratedCircuit(16)
     
)
 .fluidInputs(Materials.Enderium.getMolten(144*16))
 .itemOutputs(new ItemStack(MyMod.pitem, 1)

 ) .duration(40 * SECONDS)
 .eut(480)
 .addTo(RecipeMaps.assemblerRecipes);;
 
 GT_Values.RA.stdBuilder()
 .itemInputs(
		 Casing_EV.get(1),
		 Sensor_EV.get(1),
		 new ItemStack(GameRegistry.findItem("OpenComputers", "cable"), 32, 0),
		 new ItemStack(GameRegistry.findItem("OpenComputers", "item"), 1, 51),
	new ItemStack(GameRegistry.findItem("OpenComputers", "item"), 1, 33),
     GT_Utility.getIntegratedCircuit(17)
     
)
 .fluidInputs(Materials.Enderium.getMolten(144*16))
 .itemOutputs(new ItemStack(MyMod.pstation, 1)

 ) .duration(40 * SECONDS)
 .eut(480)
 .addTo(RecipeMaps.assemblerRecipes);;
 
 
 

 
 for(ItemStack[] io:new ItemStack[][]{
	 {Api.INSTANCE.definitions().parts().iface().maybeStack(1).get(),new ItemStack(MyMod.cover, 1,32)},
	 {new ItemStack(ItemAndBlockHolder.FLUID_INTERFACE),new ItemStack(MyMod.cover, 1,33)},
	 {Api.INSTANCE.definitions().parts().p2PTunnelMEInterface().maybeStack(1).get(),new ItemStack(MyMod.cover, 1,34)},
	 {new ItemStack(ItemAndBlockHolder.FLUID_INTERFACE_P2P),new ItemStack(MyMod.cover, 1,35)}
		
	 
 })
 GT_Values.RA.stdBuilder()
 .itemInputs(
		 io[0] ,
		 new ItemStack(MyMod.cover, 1,37),
     GT_Utility.getIntegratedCircuit(18)
     
)
 
 .itemOutputs(io[1])

  .duration(40 * SECONDS)
 .eut(480)
 .addTo(RecipeMaps.formingPressRecipes);;
 
 
 GT_Values.RA.stdBuilder()
 .itemInputs(
 GT_Utility.getIntegratedCircuit(19),
 Api.INSTANCE.definitions().blocks().fluix().maybeStack(1).get()
)
 
 .itemOutputs( new ItemStack(MyMod.cover, 4,37))

  .duration(256 * SECONDS)
 .eut(480)
 .addTo(RecipeMaps.cutterRecipes);;
 
 GT_Values.RA.stdBuilder()
 .itemInputs(
 GT_Utility.getIntegratedCircuit(19),
 Materials.Iron.getPlates(1),
 new ItemStack(Items.comparator),
 Emitter_MV.get(1)
)
 .fluidInputs(Iron.getMolten(144*2))
 .itemOutputs( new ItemStack(MyMod.cover, 1,3))

  .duration(40 * SECONDS)
 .eut(480)
 .addTo(RecipeMaps.assemblerRecipes);;
 
 
 GT_Values.RA.stdBuilder()
 .itemInputs(
new ItemStack(ItemAndBlockHolder.BUFFER),
 Conveyor_Module_HV.get(1),
 Electric_Pump_HV.get(1),
 Casing_HV.get(1)
)
 
 .itemOutputs( new ItemStack(
         GregTech_API.sBlockMachines,
         1,
         Config.metaTileEntityOffset + Registration.IngBufferOffset))

  .duration(10 * SECONDS)
 .eut(480)
 .addTo(RecipeMaps.assemblerRecipes);
 
 GT_Values.RA.stdBuilder()
 .itemInputs(
new ItemStack(ItemAndBlockHolder.LARGE_BUFFER),
 Conveyor_Module_IV.get(1),
 Electric_Pump_IV.get(1),
 Casing_IV.get(1)
)
 
 .itemOutputs( new ItemStack(
         GregTech_API.sBlockMachines,
         1,
         Config.metaTileEntityOffset + Registration.IngBufferOffset+1))

  .duration(10 * SECONDS)
 .eut(480*4*4)
 .addTo(RecipeMaps.assemblerRecipes);
 
 
 
 GT_Values.RA.stdBuilder()
 .itemInputs(
new ItemStack(ItemAndBlockHolder.LEVEL_MAINTAINER),
//Casing_EV.get(1),
Casing_CleanStainlessSteel.get(1),
new ItemStack(
        GregTech_API.sBlockMachines,
        1,
        Config.metaTileEntityOffset + Registration.CircuitProviderOffset)

)
 
 .itemOutputs( new ItemStack(
         GregTech_API.sBlockMachines,
         1,
         Config.metaTileEntityOffset + Registration.LargeProviderOffset))

  .duration(600 * SECONDS)
 .eut(480*4*4)
 .addTo(RecipeMaps.assemblerRecipes);
 
 GT_Values.RA.stdBuilder()
 .itemInputs(
new ItemStack(ItemAndBlockHolder.LEVEL_MAINTAINER),
Casing_EV.get(1),
Api.INSTANCE.definitions()
.blocks()
.craftingStorage64k()
.maybeStack(4)
.get(),
Api.INSTANCE.definitions()
.blocks()
.craftingAccelerator()
.maybeStack(4)
.get()
)
 
 .itemOutputs( new ItemStack(
         GregTech_API.sBlockMachines,
         1,
         Config.metaTileEntityOffset + Registration.ChainerOffset))

  .duration(60 * SECONDS)
 .eut(480*4*4)
 .addTo(RecipeMaps.assemblerRecipes);
 
 
 GT_Values.RA.stdBuilder()
 .itemInputs(
		 GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Terbium, 4)
		 ,new ItemStack(MyMod.smartarm,32,4)
		 ,  Materials.TungstenSteel.getPlates(16)
		 
		 
)
 
 .itemOutputs( new ItemStack(
         GregTech_API.sBlockMachines,
         1,
         Config.metaTileEntityOffset + Registration.IngredientDistributorOffset))

  .duration(60 * SECONDS)
 .eut(480*4*4)
 .addTo(RecipeMaps.assemblerRecipes);
 
 
 
 for(int i=1;i<=10;i++){
	 
	 ItemList [][] list={
			 { 
	   
	    Super_Tank_LV,
	    Super_Tank_MV,
	    Super_Tank_HV,
	    Super_Tank_EV,
	    Super_Tank_IV,
	    Quantum_Tank_LV,
	    Quantum_Tank_MV,
	    Quantum_Tank_HV,
	    Quantum_Tank_EV,
	    Quantum_Tank_IV,  },{ 
	    Super_Chest_LV,
	    Super_Chest_MV,
	    Super_Chest_HV,
	    Super_Chest_EV,
	    Super_Chest_IV,
	    Quantum_Chest_LV,
	    Quantum_Chest_MV,
	    Quantum_Chest_HV,
	    Quantum_Chest_EV,
	    Quantum_Chest_IV,
	   }
	    
	 };
	 
	 
	 GT_Values.RA.stdBuilder()
	 .itemInputs(
			 list[1][i-1].get(1),
			 Api.INSTANCE.definitions().parts().storageBus().maybeStack(1).get()
			 
	)
	 
	 .itemOutputs( new ItemStack(
	         GregTech_API.sBlockMachines,
	         1,
	         Config.metaTileEntityOffset+Registration.MEChest + i-1)			 )

	  .duration(10 * SECONDS)
	 .eut((int) (30*Math.pow(4, i-1)))
	 .addTo(RecipeMaps.formingPressRecipes);
	
	 GT_Values.RA.stdBuilder()
	 .itemInputs(
			 list[0][i-1].get(1),
			 ItemAndBlockHolder.FLUID_STORAGE_BUS.stack(1)
			 
	)
	 
	 .itemOutputs( new ItemStack(
	         GregTech_API.sBlockMachines,
	         1,
	         Config.metaTileEntityOffset+Registration.METank + i-1)			 )

	  .duration(10 * SECONDS)
	 .eut((int) (30*Math.pow(4, i-1)))
	 .addTo(RecipeMaps.formingPressRecipes);
	 
 }
 
 

 prefab();
 
 GT_Values.RA.stdBuilder()
 .itemInputs( new ItemStack(MyMod.cover, 1, 0))
 .fluidInputs(Materials.Enderium.getMolten(144*10))
 .itemOutputs(
		 new ItemStack(MyMod.cover, 1, 4))
 .duration(20 * SECONDS)
 .eut(GT_Values.VP[3])
 .addTo(RecipeMaps.assemblerRecipes);
 
 
 GT_Values.RA.stdBuilder()
 .itemInputs(
		 Hatch_Input_Bus_ME_Advanced.get(1),
		 Hatch_DataAccess_UV.get(1),
		 com.github.technus.tectech.thing. CustomItemList.Machine_Multi_DataBank.get(1),
		 com.github.technus.tectech.thing. CustomItemList.dataInAss_Hatch.get(1),
		 com.github.technus.tectech.thing. CustomItemList.dataOutAss_Hatch.get(1),
		 com.github.technus.tectech.thing. CustomItemList.LASERpipe.get(4)
		 )
 //.fluidInputs(Materials.Enderium.getMolten(144*10))
 .itemOutputs(
		 new ItemStack(
		         GregTech_API.sBlockMachines,
		         1,
		         Config.metaTileEntityOffset+Registration.DataHatchMEOffset))
 .duration(20 * SECONDS)
 .eut(GT_Values.VP[8])
 .addTo(RecipeMaps.assemblerRecipes);
 
 
 GT_Values.RA.stdBuilder()
 .itemInputs(
		 new ItemStack(
				 ConfigItems.itemGolemCore,1,8),
		 ItemList.Duct_Tape.get(64)
		 )
 .fluidInputs(Materials.AdvancedGlue.getFluid(8000))
 .itemOutputs(
		 new ItemStack(
				 ConfigItems.itemGolemCore,1,120))
 .duration(20 * SECONDS)
 .eut(GT_Values.VP[3])
 .addTo(RecipeMaps.assemblerRecipes);
 
 
 
 
 
  }

        ///////////////////////////////////
 public void prefab(){
	 GT_Values.RA.stdBuilder()
	 .itemInputs(
			 new ItemStack(
			         GregTech_API.sBlockMachines,
			         1,
			         Config.metaTileEntityOffset+Registration.CircuitProviderOffset)	
			 ,    GT_Utility.getIntegratedCircuit(1)
			 
	)
	 
	 .itemOutputs( new ItemStack(
	         GregTech_API.sBlockMachines,
	         1,
	         Config.metaTileEntityOffset+Registration.PrefabOffset)			 )

	  .duration(10 * SECONDS)
	 .eut(480*4*4*4)
	 .addTo(RecipeMaps.laserEngraverRecipes);
	 
	 
	for(int i=1;i<=3;i++)
	 GT_Values.RA.stdBuilder()
	 .itemInputs(
			 new ItemStack(
			         GregTech_API.sBlockMachines,
			         1,
			         Config.metaTileEntityOffset+Registration.CircuitProviderOffset)	
			 ,    GT_Utility.getIntegratedCircuit(i+1)
			 
	)
	 
	 .itemOutputs( new ItemStack(
	         GregTech_API.sBlockMachines,
	         1,
	         Config.metaTileEntityOffset+Registration.PrefabOffset+i)			 )

	  .duration(10 * SECONDS)
	 .eut(480*4*4*4*4)
	 .addTo(RecipeMaps.laserEngraverRecipes);
	 
	 
	 GT_Values.RA.stdBuilder()
	 .itemInputs(
			 new ItemStack(
			         GregTech_API.sBlockMachines,
			         1,
			         Config.metaTileEntityOffset+Registration.CircuitProviderOffset)	
			 ,    GT_Utility.getIntegratedCircuit(24)
			 
	)
	 
	 .itemOutputs( new ItemStack(
	         GregTech_API.sBlockMachines,
	         1,
	         Config.metaTileEntityOffset+Registration.PrefabOffset+5)			 )

	  .duration(10 * SECONDS)
	 .eut(480*4*4*4*4*4)
	 .addTo(RecipeMaps.laserEngraverRecipes);
	 
	 
	 GT_Values.RA.stdBuilder()
	 .itemInputs(
			 new ItemStack(
			        MyMod.toolkit,0,OreDictionary.WILDCARD_VALUE)	,
			 new ItemStack(Items.diamond)
			 ,    GT_Utility.getIntegratedCircuit(7)
			 
	)
	 
	 .itemOutputs( new ItemStack(  MyMod.fixer)			 )

	  .duration(10 * SECONDS)
	 .eut(480)
	 .addTo(RecipeMaps.assemblerRecipes);
	 
	 
	 
	 
	 
	 if(flag){
		 GT_Values.RA.stdBuilder()
		 .itemInputs(
				 new ItemStack(
				       GameRegistry.findItem("computronics","computronics.chatBox")),
				 Api.INSTANCE.definitions().blocks().spatialPylon().maybeStack(1).get(),
				 new ItemStack(Items.ender_pearl,16)
		)
		 .fluidInputs(Materials.Enderium.getMolten(144))
		 .itemOutputs( new ItemStack(  MyMod.alert)			 )

		  .duration(100 * SECONDS)
		 .eut(480)
		 .addTo(RecipeMaps.assemblerRecipes);
		 
		 
		 GT_Values.RA.stdBuilder()
		 .itemInputs(Hatch_Input_Bus_ME.get(1),
				 Materials.Steel.getPlates(1)
				 )
		 .itemOutputs( new ItemStack(  MyMod.upgrades,1,1)			 )
		 .duration(100 * SECONDS)
		 .eut(480*4*4)
		 .addTo(RecipeMaps.formingPressRecipes);
		 
		 GT_Values.RA.stdBuilder()
		 .itemInputs(Hatch_Input_ME.get(1),
				 Materials.Steel.getPlates(1)
				 )
		 .itemOutputs( new ItemStack(  MyMod.upgrades,1,2)			 )
		 .duration(100 * SECONDS)
		 .eut(480*4*4)
		 .addTo(RecipeMaps.formingPressRecipes);
		 
		 GT_Values.RA.stdBuilder()
		 .itemInputs(Hatch_Input_Bus_ULV.get(16),
				 Materials.Steel.getPlates(1)
				 )
		 .itemOutputs( new ItemStack(  MyMod.upgrades,1,0)			 )
		 .duration(100 * SECONDS)
		 .eut(480*4*4)
		 .addTo(RecipeMaps.formingPressRecipes);
		 
		
		 
		 for(int i=0;i<4;i++)
		 GT_Values.RA.stdBuilder()
		 .itemInputs(
				 new ItemStack(  MyMod.upgrades,3,0),
				 HATCHES_INPUT_BUS[i].get(1)
				 
		)
		.itemOutputs( new ItemStack(
                GregTech_API.sBlockMachines,
                1,
                Config.metaTileEntityOffset + Registration.MultiCircuitBusOffset + i))		 

		  .duration(15 * SECONDS)
		 .eut(GT_Values.VP[i])
		 .addTo(RecipeMaps.assemblerRecipes);
		
	 }
	 
	 
	 
	 
	 
	 
 }
    boolean flag=true;
    public void smartArm(){  
    	for (int i = 0; i < GT_Values.VP.length - 1; i++) {
            int ii = i;
   	    
            
      /*  Object circuit = OrePrefixes.circuit.get(mat[i][0]);
        Object circuitP = OrePrefixes.circuit.get(mat[Math.min(i + 1, mat.length - 2)][0]);
        Object circuitM1 = OrePrefixes.circuit.get(mat[Math.max(i-1,0)][0]);
        Object circuitM2 = OrePrefixes.circuit.get(mat[Math.max(i-2,0)][0]);*/
            Object circuitP =  OrePrefixes.circuit.get(mat[i][0]);
            Object circuit =   OrePrefixes.circuit.get(mat[Math.max(i-1,0)][0]);
            Object circuitM1 = OrePrefixes.circuit.get(mat[Math.max(i-2,0)][0]);
            Object circuitM2 = OrePrefixes.circuit.get(mat[Math.max(i-3,0)][0]);  
            
            
            
        Fluid solderIndalloy = GTPlusPlus.isModLoaded() ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        
        /*
          ItemStack[] t0 = Arrays.stream(mat[i])
                .map(s -> GT_OreDictUnificator.getOres(OrePrefixes.circuit, s))
                .flatMap(ArrayList::stream)
                .map(s -> GT_Utility.copyAmount(4, s))
                .toArray(ItemStack[]::new);
        ItemStack[] t1 = Arrays.stream(mat[Math.min(i + 1, mat.length - 1)])
                .map(s -> GT_OreDictUnificator.getOres(OrePrefixes.circuit, s))
                .flatMap(ArrayList::stream)
                .map(s -> GT_Utility.copyAmount(2, s))
                .toArray(ItemStack[]::new);
       
        ItemStack[] tm1=null,tm2=null;
       if(ii>6){ tm1 = Arrays.stream(mat[i-1])
                .map(s -> GT_OreDictUnificator.getOres(OrePrefixes.circuit, s))
                .flatMap(ArrayList::stream)
                .map(s -> GT_Utility.copyAmount(8, s))
                .toArray(ItemStack[]::new);
         tm2 = Arrays.stream(mat[i-2])
                .map(s -> GT_OreDictUnificator.getOres(OrePrefixes.circuit, s))
                .flatMap(ArrayList::stream)
                .map(s -> GT_Utility.copyAmount(16, s))
                .toArray(ItemStack[]::new);
        }*/
        
        int amountAmp=(int) Math.pow(2,ii-6);
        
       // GT_RecipeConstants.AssemblyLine.doAdd(builder)
       if(ii>6){
    	   
    	   if(
    	  null!= (GT_OreDictUnificator.get(circuitM2,1))&&
    	  null!=(GT_OreDictUnificator.get(circuitM1,1))&&
    	  null!=(GT_OreDictUnificator.get(circuit,1))&&
    	  null!= (GT_OreDictUnificator.get(circuitP,1))
    	   ){
    		   
    		   
    	   }else{
    		   MyMod.LOG.fatal("OreDict Item not found! Are you in Dev?");
    		   MyMod.LOG.fatal(circuitM2+" "+circuitM1 +" "+circuit+" "+circuitP);
    		   MyMod.LOG.fatal( (GT_OreDictUnificator.get(circuitM2,1))+" "+
    	    	  (GT_OreDictUnificator.get(circuitM1,1))+" "+
    	    	  (GT_OreDictUnificator.get(circuit,1))+" "+
    	    	  (GT_OreDictUnificator.get(circuitP,1)));
    		   continue;
    	   }
    	   
    	   
    	   
        GT_RecipeBuilder.builder()
        .metadata(RESEARCH_ITEM, new ItemStack(MyMod.smartarm, 1, ii-1))
        .metadata(RESEARCH_TIME, 1 * HOURS)
        .itemInputs(
        		new Object[]{circuitM2,12},
        		new Object[]{circuitM1, 6}, 
        		new Object[]{circuit, 2},
				new Object[]{circuitP, 1}, 
                
                Circuit_Chip_Stemcell.get(32)
                 , arms[i]
                		 
                		 
          )
        .fluidInputs(
        		new FluidStack(solderIndalloy, 4000*amountAmp),
        		Materials.BioMediumSterilized.getFluid(2000*amountAmp),
        		Materials.UUMatter.getFluid(1000*amountAmp)
        		)
        .itemOutputs( new ItemStack(MyMod.smartarm, 1, ii))
        .eut(TierEU.RECIPE_IV)
        .duration(600)
        .addTo( GT_RecipeConstants.AssemblyLine);
       }
       else
        
        RecipeMaps.assemblerRecipes.add(
            new GT_Recipe/*.GT_Recipe_WithAlt*/(
                false,
                new ItemStack[] {
                	GT_OreDictUnificator.get(circuit, 4),
                    GT_OreDictUnificator.get(circuitP, 2), 
                    Circuit_Chip_Stemcell.get(32)

                    , arms[i]

                }// II
                ,
                new ItemStack[] { new ItemStack(MyMod.smartarm, 1, ii) }
                // IO
                ,
                null// SP
                ,
                null// CHANCE
                ,
                new FluidStack[] { new FluidStack(solderIndalloy, 4000) }// FI
                ,
                null// FO
                ,
                20 * SECONDS,
                (int) GT_Values.VP[i],
                0/*,
                new ItemStack[][] { t0,t1} // ALT*/
            )

        );
   }
    }
    
}

