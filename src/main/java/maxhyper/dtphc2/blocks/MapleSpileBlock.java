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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static maxhyper.dtphc2.init.DTPHC2Registries.MAPLE_SPILE_BUCKET_BLOCK;

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

        @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (state.hasProperty(FILLED)) {
            if (!world.getBlockState(pos).getValue(FILLED) && player.isCrouching()) {
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                if (world.random.nextFloat() <= chanceToBreak) {
                    world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1, 1, false);
                    world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BREAK, SoundCategory.BLOCKS, 1, 1, false);
                    player.addItem(new ItemStack(Items.IRON_NUGGET));
                } else {
                    world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1, 1, false);
                    player.addItem(new ItemStack(Items.IRON_INGOT));
                }
                return ActionResultType.SUCCESS;
            }
            if (player.getMainHandItem().getItem() == Items.BUCKET) {
                Direction dir = state.getValue(FACING);
                world.setBlock(pos, MAPLE_SPILE_BUCKET_BLOCK.defaultBlockState()
                        .setValue(FACING, dir)
                        .setValue(MapleSpileBucketBlock.FILLING, state.getValue(FILLED) ? 1 : 0), 3);
                if (!player.isCreative()) {
                    player.getMainHandItem().shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (giveSyrup(world, pos, state, player)) {
                return ActionResultType.SUCCESS;
            }
        }
        return super.use(state, world, pos, player, hand, hit);
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
