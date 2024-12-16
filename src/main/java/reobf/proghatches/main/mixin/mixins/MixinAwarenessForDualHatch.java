package reobf.proghatches.main.mixin.mixins;

import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import reobf.proghatches.gt.cover.ProgrammingCover;
import reobf.proghatches.gt.metatileentity.util.IRecipeProcessingAwareDualHatch;

@SuppressWarnings("unused")
@Pseudo
@Mixin(
		
		value = MTEMultiBlockBase.class,
		targets={
		
		"com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.MultiExecutionCoreMachineBase"
		}
		,
		remap = false)
public abstract class MixinAwarenessForDualHatch {
	
	
	//@Shadow
	//public ArrayList<IDualInputHatch> mDualInputHatches = new ArrayList<>();
	//@Shadow
	//public CheckRecipeResult checkRecipeResult;
	//@Shadow
	//public abstract void setResultIfFailure(CheckRecipeResult result) ;
	
	@Unique
	private static MethodHandle MH_mDualInputHatches;
	@Unique
	private static MethodHandle MH_setResultIfFailure;
	static{
		
		try {
			MH_mDualInputHatches=MethodHandles.lookup().findGetter(	
					MTEMultiBlockBase.class,
					"mDualInputHatches", ArrayList.class);
			MH_setResultIfFailure=MethodHandles.lookup().findVirtual(MTEMultiBlockBase.class,
					"setResultIfFailure", MethodType.methodType(void.class,CheckRecipeResult.class));			
			
			
		} catch (Exception e) {
		e.printStackTrace();	
		throw new AssertionError(e);
		}
			
				
	}@Unique
	public void setResultIfFailure0(CheckRecipeResult endRecipeProcessing) {
		try {
			 MH_setResultIfFailure.invoke((MTEMultiBlockBase)(Object)this,endRecipeProcessing);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new AssertionError(e);
		}
		
	}@Unique
	public ArrayList<IDualInputHatch> mDualInputHatches0(){
		try {
			return (ArrayList<IDualInputHatch>) MH_mDualInputHatches.invoke((MTEMultiBlockBase)(Object)this);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new AssertionError(e);
		}
	} 
	
	@Inject(method = "startRecipeProcessing", at = { @At(value = "RETURN") }/*,require=1*/)
	public void a(CallbackInfo c) {
		
		
		for (IDualInputHatch hatch : (mDualInputHatches0())) {
			if (hatch == null || !((MetaTileEntity) hatch).isValid())
				continue;
			if (hatch instanceof IRecipeProcessingAwareDualHatch) {
				((IRecipeProcessingAwareDualHatch) hatch).startRecipeProcessing();
			}
		}
	}

	@Inject(method = "endRecipeProcessing", at = { @At(value = "RETURN") }/*,require=1*/)
	public void b(CallbackInfo c) {
		/*Consumer<CheckRecipeResult> setResultIfFailure = result -> {
			if (!result.wasSuccessful()) {
				this.checkRecipeResult = result;
			}
		};*/

		for (IDualInputHatch hatch : (mDualInputHatches0())) {
			if (hatch == null || !((MetaTileEntity) hatch).isValid())
				continue;
			if (hatch instanceof IRecipeProcessingAwareDualHatch) {
				setResultIfFailure0(((IRecipeProcessingAwareDualHatch) hatch)
						.endRecipeProcessing((MTEMultiBlockBase) (Object) this));
			}
		}
	}

	

}
