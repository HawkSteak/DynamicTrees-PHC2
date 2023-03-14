package maxhyper.dtphc2.blocks;

import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class MapleSpileBucketBlock extends MapleSpileCommon {


    protected static VoxelShape makeShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.25, 0, 0.0625, 0.75, 0.5625, 0.5625), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.6875, 0.0625, 0.125, 0.3125, 0.5625, 0.5), IBooleanFunction.ONLY_FIRST);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.625, -0.0625, 0.5625, 0.75, 0.25), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.625, 0.25, 0.5625, 0.6875, 0.375), IBooleanFunction.OR);

        return shape;
    }

    private static final VoxelShape SHAPE_N = rotateShape(Direction.SOUTH, Direction.NORTH, makeShape());
    private static final VoxelShape SHAPE_E = rotateShape(Direction.SOUTH, Direction.EAST, SHAPE_N);
    private static final VoxelShape SHAPE_S = rotateShape(Direction.WEST, Direction.SOUTH, SHAPE_N);
    private static final VoxelShape SHAPE_W = rotateShape(Direction.WEST, Direction.WEST, SHAPE_N);

    public MapleSpileBucketBlock() {
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(FILLING, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        //super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(FILLING);
    }

    @Override
    public void randomTick(@Nonnull BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random random) {
        if (!this.canBlockStay(world, pos, state)) {
            this.dropBlock(world, pos, state);
        }
    }

    @Nonnull
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (state.hasProperty(FILLING)) {
            if (world.getBlockState(pos).getValue(FILLING) == 0 && player.isCrouching()) {
                Direction dir = state.getValue(FACING);
                world.setBlock(pos, DTPHC2Registries.MAPLE_SPILE_BLOCK.defaultBlockState().setValue(FACING, dir), 3);
                player.addItem(new ItemStack(Items.BUCKET));
                return ActionResultType.SUCCESS;
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    protected boolean giveSyrup(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        int filling = worldIn.getBlockState(pos).getValue(FILLING);
        if (filling > 0) {
            if (!worldIn.isClientSide() && !worldIn.restoringBlockSnapshots) {
                //TODO: make dynamic
                ResourceLocation mapleSyrupRes = new ResourceLocation("pamhc2trees", "maplesyrupitem");
                Item mapleSyrup = ForgeRegistries.ITEMS.getValue(mapleSyrupRes);
                //ItemStack drop = new ItemStack(FruitRegistry.getLog(FruitRegistry.MAPLE).getFruitItem());
                int count = (filling + (filling == maxFilling ? 1 : 0)); //Adds one bonus syrup if collected when its full
                ItemStack drop = new ItemStack(mapleSyrup, count);
                worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), drop));
                //player.addItem(drop);
            }
            worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1 + filling / 4f, false);
            worldIn.setBlock(pos, state.setValue(FILLING, 0), 3);
            return true;
        }
        return false;
    }

    @Override
    protected void dropBlock(World worldIn, BlockPos pos, BlockState state) {
        worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.BUCKET)));
        worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.IRON_NUGGET)));
    }

    @Override
    public void playerDestroy(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity pTe, ItemStack stack) {
        if (!player.isCreative()) {
            worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.BUCKET)));
            worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.IRON_NUGGET)));
        }
        super.playerDestroy(worldIn, player, pos, state, pTe, stack);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(Items.BUCKET);
    }

    //TODO: Maybe reimplement these methods idk
//    @Override
//    public boolean hasComparatorInputOverride(BlockState state) {
//        return true;
//    }
//
//    @Override
//    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
//        return blockState.getValue(FILLING) * 5;
//    }
//
//    @Override
//    public BlockRenderLayer getBlockLayer() {
//        return BlockRenderLayer.CUTOUT_MIPPED;
//    }

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
    }

}
