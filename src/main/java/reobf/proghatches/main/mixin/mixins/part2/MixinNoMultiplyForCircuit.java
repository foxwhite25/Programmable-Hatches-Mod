package reobf.proghatches.main.mixin.mixins.part2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.util.PatternMultiplierHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import reobf.proghatches.gt.metatileentity.util.IDisallowOptimize;
import reobf.proghatches.main.MyMod;

@Mixin(remap=false,value=PatternMultiplierHelper.class)
public class MixinNoMultiplyForCircuit {
	
	
	
	@Inject(method="applyModification",remap=false, at = { @At("RETUEN") })
	private static void applyModification(ItemStack stack, int bitMultiplier,CallbackInfo c) {
		 
	
    NBTTagCompound encodedValue = stack.stackTagCompound;
    final NBTTagList inTag = encodedValue.getTagList("in", 10);
  
    for (int x = 0; x < inTag.tagCount(); x++) {
        final NBTTagCompound tag = inTag.getCompoundTagAt(x);
        if(tag.getInteger("id")!=Item.getIdFromItem(MyMod.progcircuit)){continue;}
        
        if (tag.hasNoTags()) continue;
        if (tag.hasKey("Count", 3)) {
            tag.setInteger(
                    "Count",
                   1);
        }
        // Support for IAEItemStack (ae2fc patterns)
        if (tag.hasKey("Cnt", 4)) {
            tag.setLong(
                    "Cnt",
                  1);
        }
    }
		 
	}
	 }






