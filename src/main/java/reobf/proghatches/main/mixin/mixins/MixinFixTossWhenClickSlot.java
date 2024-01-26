package reobf.proghatches.main.mixin.mixins;

import net.minecraft.client.gui.inventory.GuiContainer;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;

@SuppressWarnings("unused")
@Mixin(value = GuiContainer.class, remap = true)
public abstract class MixinFixTossWhenClickSlot {

    @ModifyVariable(
        method = "mouseClicked",
        at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/inventory/Slot;slotNumber:I"),
        ordinal = 1)
    protected boolean mouseClicked(boolean c) {

        if (((Object) this) instanceof ModularGui) return false;// only applies to ModularUI

        return c;
    }

}
