package pneumaticCraft.common.thirdparty.ic2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pneumaticCraft.common.inventory.ContainerPneumaticBase;
import pneumaticCraft.common.inventory.SlotUpgrade;
import pneumaticCraft.common.item.Itemss;

public class ContainerPneumaticGenerator extends ContainerPneumaticBase<TileEntityPneumaticGenerator>{

    public ContainerPneumaticGenerator(InventoryPlayer inventoryPlayer, TileEntityPneumaticGenerator te){
        super(te);

        // add the upgrade slots
        addSlotToContainer(new SlotUpgrade(te, 0, 48, 29));
        addSlotToContainer(new SlotUpgrade(te, 1, 66, 29));
        addSlotToContainer(new SlotUpgrade(te, 2, 48, 47));
        addSlotToContainer(new SlotUpgrade(te, 3, 66, 47));

        // Add the player's inventory slots to the container
        for(int inventoryRowIndex = 0; inventoryRowIndex < 3; ++inventoryRowIndex) {
            for(int inventoryColumnIndex = 0; inventoryColumnIndex < 9; ++inventoryColumnIndex) {
                addSlotToContainer(new Slot(inventoryPlayer, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 84 + inventoryRowIndex * 18));
            }
        }

        // Add the player's action bar slots to the container
        for(int actionBarSlotIndex = 0; actionBarSlotIndex < 9; ++actionBarSlotIndex) {
            addSlotToContainer(new Slot(inventoryPlayer, actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 142));
        }
    }

    /**
     * @param itemStack
     *            ItemStack to merge into inventory
     * @param start
     *            minimum slot to attempt fill
     * @param end
     *            maximum slot to attempt fill
     * @param backwards
     *            go backwards
     * @return true if stacks merged successfully public boolean
     *         mergeItemStack(itemStack, start, end, backwards)
     */

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2){

        ItemStack var3 = null;
        Slot var4 = (Slot)inventorySlots.get(par2);

        if(var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if(par2 < 4) {
                if(!mergeItemStack(var5, 4, 39, false)) return null;
                var4.onSlotChange(var5, var3);
            } else if(var3.getItem() == Itemss.machineUpgrade) {
                if(!mergeItemStack(var5, 0, 4, false)) return null;
                var4.onSlotChange(var5, var3);
            }

            if(var5.stackSize == 0) {
                var4.putStack((ItemStack)null);
            } else {
                var4.onSlotChanged();
            }

            if(var5.stackSize == var3.stackSize) return null;

            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }

        return var3;
    }

}
