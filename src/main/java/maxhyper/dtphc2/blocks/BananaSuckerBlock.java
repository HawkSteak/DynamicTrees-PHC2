package maxhyper.dtphc2.blocks;

import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class BananaSuckerBlock extends HorizontalBlock implements IPlantable {

    protected static final VoxelShape SHAPE_E = VoxelShapes.create(new AxisAlignedBB(
            0 /16f,0  /16f,6  /16f,
            4 /16f,15 /16f,10 /16f));
    protected static final VoxelShape SHAPE_W = VoxelShapes.create(new AxisAlignedBB(
            12  /16f,0 /16f, 6  /16f,
            16 /16f,15 /16f,10 /16f));
    protected static final VoxelShape SHAPE_N = VoxelShapes.create(new AxisAlignedBB(
            6  /16f,0  /16f,12  /16f,
            10 /16f,15 /16f,16 /16f));
    protected static final VoxelShape SHAPE_S = VoxelShapes.create(new AxisAlignedBB(
            6  /16f,0  /16f,0 /16f,
            10 /16f,15 /16f,4 /16f));

    public BananaSuckerBlock() {
        super(Properties.of(Material.LEAVES).sound(SoundType.GRASS));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return pFacing == Direction.DOWN && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState pState, IWorldReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        if (pState.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return pLevel.getBlockState(blockpos).canSustainPlant(pLevel, blockpos, Direction.UP, this);
        return false;
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return defaultBlockState();
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACING)) {
            case EAST:
                return SHAPE_E;
            case SOUTH:
                return SHAPE_S;
            case WEST:
                return SHAPE_W;
            default:
                return SHAPE_N;
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        Item item = ForgeRegistries.ITEMS.getValue(DynamicTreesPHC2.resLoc("banana_seed"));
        if (item == null || item == Items.AIR) return ItemStack.EMPTY;
        return new ItemStack(item);
    }
}
