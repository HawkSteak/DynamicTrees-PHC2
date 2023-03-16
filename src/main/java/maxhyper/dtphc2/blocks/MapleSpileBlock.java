package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dtphc2.genfeatures.DTPHC2GenFeatures;
import maxhyper.dtphc2.genfeatures.SyrupGenFeature;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

import static maxhyper.dtphc2.init.DTPHC2Registries.MAPLE_SPILE_BUCKET_BLOCK;

public class MapleSpileBlock extends MapleSpileCommon {

    public MapleSpileBlock() {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FILLED, false));
        SHAPE_N = rotateShape(Direction.SOUTH, Direction.NORTH, makeShape());
        SHAPE_E = rotateShape(Direction.SOUTH, Direction.EAST, SHAPE_N);
        SHAPE_S = rotateShape(Direction.WEST, Direction.SOUTH, SHAPE_N);
        SHAPE_W = rotateShape(Direction.WEST, Direction.WEST, SHAPE_N);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, FILLED));
    }

        @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
            if (state.hasProperty(FILLED)) {
                Direction dir = state.getValue(FACING);
                if (player.getMainHandItem().getItem() == Items.BUCKET) {
                    world.setBlock(pos, MAPLE_SPILE_BUCKET_BLOCK.defaultBlockState()
                            .setValue(FACING, dir)
                            .setValue(MapleSpileBucketBlock.FILLING, state.getValue(FILLED) ? 1 : 0), 3);
                    if (!player.isCreative()) player.getMainHandItem().shrink(1);
                    world.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundCategory.BLOCKS, 1, 1f);
                    return ActionResultType.SUCCESS;
                }
                else if (giveSyrup(world, pos, state, player, pos.offset(dir.getOpposite().getNormal()))) {
//                if (world.random.nextFloat() <= chanceToBreak) {
//                    world.destroyBlock(pos, true);
//                    world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BREAK, SoundCategory.BLOCKS, 1, 1, false);
//                }
                    return ActionResultType.SUCCESS;
                }
            }
            return super.use(state, world, pos, player, hand, hit);
        }

    @Override
    protected boolean giveSyrup(World world, BlockPos pos, BlockState state, PlayerEntity player, BlockPos treePos) {
        Species species = TreeHelper.getExactSpecies(world, treePos);
        if (species == Species.NULL_SPECIES) return false;
        if (state.getValue(FILLED)) {
            if (!world.isClientSide() && !world.restoringBlockSnapshots)
                world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(getSyrupItem(species))));
            world.playSound(null, pos, SoundEvents.HONEY_DRINK, SoundCategory.BLOCKS, 1, 2f);
            world.setBlock(pos, state.setValue(FILLED, false), 3);
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(Items.IRON_INGOT);
    }



}
