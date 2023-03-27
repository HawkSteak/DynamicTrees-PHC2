package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;

public class MapleSpileBlock extends MapleSpileCommon {

    public MapleSpileBlock() {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FILLED, false));
        SHAPE_N = rotateShape(Direction.SOUTH, Direction.NORTH, makeShape());
        SHAPE_E = rotateShape(Direction.SOUTH, Direction.EAST, SHAPE_N);
        SHAPE_S = rotateShape(Direction.WEST, Direction.SOUTH, SHAPE_N);
        SHAPE_W = rotateShape(Direction.WEST, Direction.WEST, SHAPE_N);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, FILLED));
    }

        @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (state.hasProperty(FILLED)) {
                Direction dir = state.getValue(FACING);
                if (player.getMainHandItem().getItem() == Items.BUCKET) {
                    world.setBlock(pos, ModBlocks.MAPLE_SPILE_BUCKET_BLOCK.get().defaultBlockState()
                            .setValue(FACING, dir)
                            .setValue(MapleSpileBucketBlock.FILLING, state.getValue(FILLED) ? 1 : 0), 3);
                    if (!player.isCreative()) player.getMainHandItem().shrink(1);
                    world.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 1, 1f);
                    return InteractionResult.SUCCESS;
                }
                else if (giveSyrup(world, pos, state, player, pos.offset(dir.getOpposite().getNormal()))) {
//                if (world.random.nextFloat() <= chanceToBreak) {
//                    world.destroyBlock(pos, true);
//                    world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BREAK, SoundCategory.BLOCKS, 1, 1, false);
//                }
                    return InteractionResult.SUCCESS;
                }
            }
            return super.use(state, world, pos, player, hand, hit);
        }

    @Override
    protected boolean giveSyrup(Level world, BlockPos pos, BlockState state, Player player, BlockPos treePos) {
        Species species = TreeHelper.getExactSpecies(world, treePos);
        if (species == Species.NULL_SPECIES) return false;
        if (state.getValue(FILLED)) {
            if (!world.isClientSide() && !world.restoringBlockSnapshots)
                world.addFreshEntity(new ItemEntity(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, new ItemStack(getSyrupItem(species))));
            world.playSound(null, pos, SoundEvents.HONEY_DRINK, SoundSource.BLOCKS, 1, 2f);
            world.setBlock(pos, state.setValue(FILLED, false), 3);
            return true;
        }
        return false;
    }

    //TODO
//    @Override
//    public ItemStack getPickBlock(BlockState state, HitResult target, LevelReader world, BlockPos pos, Player player) {
//        return new ItemStack(Items.IRON_INGOT);
//    }



}
