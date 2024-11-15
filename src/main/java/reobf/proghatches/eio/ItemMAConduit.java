package reobf.proghatches.eio;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.ModObject;
import crazypants.enderio.conduit.AbstractItemConduit;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.ItemConduitSubtype;
import crazypants.enderio.conduit.item.IItemConduit;
import crazypants.enderio.conduit.item.ItemConduit;
import crazypants.enderio.conduit.item.ItemItemConduit;
import crazypants.enderio.conduit.render.ItemConduitRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import reobf.proghatches.main.MyMod;

public class ItemMAConduit extends AbstractItemConduit implements IInit {


	   private static ItemConduitSubtype[] subtypes = new ItemConduitSubtype[] {
	            new ItemConduitSubtype("proghatch.ma.conduit", "proghatches:itemMAConduit") };

    public static ItemMAConduit create() {
    	ItemMAConduit result = new ItemMAConduit();
        //result.init();
    	 GameRegistry.registerItem(result, "proghatch.ma.conduit");
    	 ((IInit)result).doJob();
    	 
    	
    	   
    	   return result;
    }

    
    @SideOnly(Side.CLIENT)
    @Override
    public void doJob() {
    	 MinecraftForgeClient.registerItemRenderer(this, new ItemConduitRenderer());
         
    }
    
    protected ItemMAConduit() {
        super(ModObject.itemItemConduit/*this is an Enum... I have no choice! hope this will not break something!*/ 
        		,subtypes);
        setUnlocalizedName("proghatch.ma.conduit");
    }

    @Override
    public Class<? extends IConduit> getBaseConduitType() {
        return ICraftingMachineConduit.class;
    }

    @Override
    public IConduit createConduit(ItemStack item, EntityPlayer player) {
        return new MAConduit();
    }

    @Override
    public boolean shouldHideFacades(ItemStack stack, EntityPlayer player) {
        return true;
    }
}

