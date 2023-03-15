package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class MapleSpileBucketBlock extends MapleSpileCommon {

    static VoxelShape makeBucketShape() {
        VoxelShape shape = makeShape();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.25, 0, 0.0625, 0.75, 0.5625, 0.5625), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.6875, 0.0625, 0.125, 0.3125, 0.5625, 0.5), IBooleanFunction.ONLY_FIRST);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.625, -0.0625, 0.5625, 0.75, 0.25), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.625, 0.25, 0.5625, 0.6875, 0.375), IBooleanFunction.OR);
        return shape;
    }

    public MapleSpileBucketBlock() {
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(FILLING, 0));
        SHAPE_N = rotateShape(Direction.SOUTH, Direction.NORTH, makeBucketShape());
        SHAPE_E = rotateShape(Direction.SOUTH, Direction.EAST, SHAPE_N);
        SHAPE_S = rotateShape(Direction.WEST, Direction.SOUTH, SHAPE_N);
        SHAPE_W = rotateShape(Direction.WEST, Direction.WEST, SHAPE_N);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, FILLING);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        Direction dir = state.getValue(FACING);
        if (state.hasProperty(FILLING)) {
            if (world.getBlockState(pos).getValue(FILLING) == 0 && player.isCrouching()) {
                world.setBlock(pos, DTPHC2Registries.MAPLE_SPILE_BLOCK.defaultBlockState().setValue(FACING, dir), 3);
                player.addItem(new ItemStack(Items.BUCKET));
                return ActionResultType.SUCCESS;
            }
        }
        if (giveSyrup(world, pos, state, player, pos.offset(dir.getOpposite().getNormal()))) {
            return ActionResultType.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    protected boolean giveSyrup(World world, BlockPos pos, BlockState state, PlayerEntity player, BlockPos treePos) {
        Species species = TreeHelper.getExactSpecies(world, treePos);
        if (species == Species.NULL_SPECIES) return false;
        int filling = world.getBlockState(pos).getValue(FILLING);
        if (filling > 0) {
            if (!world.isClientSide() && !world.restoringBlockSnapshots) {
                int count = (filling + (filling == maxFilling ? 1 : 0)); //Adds one bonus syrup if collected when its full
                ItemStack drop = new ItemStack(getSyrupItem(species), count);
                world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop));
            }
            world.playSound(null, pos, SoundEvents.HONEY_DRINK, SoundCategory.BLOCKS, 1, 2 - filling / 3f);
            if (filling == maxFilling) world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 0.5f, 0.8f);
            world.setBlock(pos, state.setValue(FILLING, 0), 3);
            return true;
        }
        return false;
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

}
