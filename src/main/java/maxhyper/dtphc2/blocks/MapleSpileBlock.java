package maxhyper.dtphc2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MapleSpileBlock extends MapleSpileCommon {

    private static final VoxelShape SHAPE_N = rotateShape(Direction.SOUTH, Direction.NORTH, makeShape());
    private static final VoxelShape SHAPE_E = rotateShape(Direction.SOUTH, Direction.EAST, SHAPE_N);
    private static final VoxelShape SHAPE_S = rotateShape(Direction.WEST, Direction.SOUTH, SHAPE_N);
    private static final VoxelShape SHAPE_W = rotateShape(Direction.WEST, Direction.WEST, SHAPE_N);

    public MapleSpileBlock() {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FILLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(FILLED);
    }

    @Override
    protected boolean giveSyrup(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (state.getValue(FILLED)) {
            if (!world.isClientSide() && !world.restoringBlockSnapshots) {
                //TODO: Make dynamic
                ResourceLocation mapleSyrupRes = new ResourceLocation("pamhc2trees", "maplesyrupitem");
                Item mapleSyrup = ForgeRegistries.ITEMS.getValue(mapleSyrupRes);
                world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(mapleSyrup)));
                //player.addItem(new ItemStack(mapleSyrup));
            }
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1f, false);
            world.setBlock(pos, state.setValue(FILLED, false), 3);
            return true;
        }
        return false;
    }

    protected void dropBlock(World worldIn, BlockPos pos, BlockState state) {
        worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.IRON_NUGGET)));
    }

    @Override
    public void playerDestroy(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity pTe, ItemStack stack) {
        if (!player.isCreative()) {
            worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.IRON_NUGGET)));
        }
        super.playerDestroy(worldIn, player, pos, state, pTe, stack);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(Items.IRON_INGOT);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        //noinspection DuplicatedCode
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
        //return rotateShape(state.getValue(FACING), defaultBlockState().getValue(FACING), SHAPE);
    }

}
