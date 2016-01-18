package pneumaticCraft.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import pneumaticCraft.PneumaticCraft;
import pneumaticCraft.common.tileentity.TileEntityPressureChamberValve;
import pneumaticCraft.proxy.CommonProxy.EnumGuiId;

public class BlockPressureChamberValve extends BlockPneumaticCraft implements IBlockPressureChamber{

    public static final PropertyBool FORMED = PropertyBool.create("formed");

    public BlockPressureChamberValve(Material par2Material){
        super(par2Material);
    }

    @Override
    protected Class<? extends TileEntity> getTileEntityClass(){
        return TileEntityPressureChamberValve.class;
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, BlockPos pos, IBlockState state, EntityLivingBase par5EntityLiving, ItemStack iStack){
        super.onBlockPlacedBy(par1World, pos, state, par5EntityLiving, iStack);
        TileEntityPressureChamberValve.checkIfProperlyFormed(par1World, pos);
    }

    @Override
    public boolean isRotatable(){
        return true;
    }

    @Override
    protected boolean canRotateToTopOrBottom(){
        return true;
    }

    @Override
    protected BlockState createBlockState(){
        return new BlockState(this, ROTATION, FORMED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return super.getStateFromMeta(meta).withProperty(FORMED, meta >= 6);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return super.getMetaFromState(state) + (state.getValue(FORMED) ? 6 : 0);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float par7, float par8, float par9){
        if(player.isSneaking()) return false;
        TileEntity te = world.getTileEntity(pos);
        if(!world.isRemote && te instanceof TileEntityPressureChamberValve) {
            if(((TileEntityPressureChamberValve)te).multiBlockSize > 0) {
                player.openGui(PneumaticCraft.instance, EnumGuiId.PRESSURE_CHAMBER.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
            } else if(((TileEntityPressureChamberValve)te).accessoryValves.size() > 0) { // when
                                                                                         // this
                                                                                         // isn't
                                                                                         // the
                                                                                         // core
                                                                                         // Valve,
                                                                                         // track
                                                                                         // down
                                                                                         // the
                                                                                         // core
                                                                                         // Valve.
                //  System.out.println("size: " + ((TileEntityPressureChamberValve)te).accessoryValves.size());
                for(TileEntityPressureChamberValve valve : ((TileEntityPressureChamberValve)te).accessoryValves) {
                    if(valve.multiBlockSize > 0) {
                        player.openGui(PneumaticCraft.instance, EnumGuiId.PRESSURE_CHAMBER.ordinal(), world, valve.getPos().getX(), valve.getPos().getY(), valve.getPos().getZ());
                        break;
                    }
                }
            } else {
                return false;
            }
            return true;
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state){
        invalidateMultiBlock(world, pos);
        super.breakBlock(world, pos, state);
    }

    private void invalidateMultiBlock(World world, BlockPos pos){
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityPressureChamberValve && !world.isRemote) {
            if(((TileEntityPressureChamberValve)te).multiBlockSize > 0) {
                ((TileEntityPressureChamberValve)te).onMultiBlockBreak();
            } else if(((TileEntityPressureChamberValve)te).accessoryValves.size() > 0) {
                for(TileEntityPressureChamberValve valve : ((TileEntityPressureChamberValve)te).accessoryValves) {
                    if(valve.multiBlockSize > 0) {
                        valve.onMultiBlockBreak();
                        break;
                    }
                }
            }
        }
    }

}
