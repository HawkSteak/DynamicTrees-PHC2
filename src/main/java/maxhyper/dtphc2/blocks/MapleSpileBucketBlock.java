package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import maxhyper.dtphc2.init.DTPHC2Blocks;
import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class MapleSpileBucketBlock extends MapleSpileCommon {

    static VoxelShape makeBucketShape() {
        VoxelShape bucket = Shapes.join(Block.box(4, 0, 1, 12, 9, 9), Block.box(5, 1, 2, 11, 9, 8), BooleanOp.ONLY_FIRST);
        return Shapes.join(bucket, makeShape(), BooleanOp.OR);
    }

    public MapleSpileBucketBlock() {
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(FILLING, 0));
        SHAPE_N = rotateShape(Direction.SOUTH, Direction.NORTH, makeBucketShape());
        SHAPE_E = rotateShape(Direction.SOUTH, Direction.EAST, SHAPE_N);
        SHAPE_S = rotateShape(Direction.WEST, Direction.SOUTH, SHAPE_N);
        SHAPE_W = rotateShape(Direction.WEST, Direction.WEST, SHAPE_N);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, FILLING));
    }



    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Direction dir = state.getValue(FACING);
        if (state.hasProperty(FILLING)) {
            if (world.getBlockState(pos).getValue(FILLING) == 0 && player.isCrouching()) {
                world.setBlock(pos, DTPHC2Registries.MAPLE_SPILE_BLOCK.get().defaultBlockState().setValue(FACING, dir), 3);
                player.addItem(new ItemStack(Items.BUCKET));
                return InteractionResult.SUCCESS;
            }
        }
        if (giveSyrup(world, pos, state, player, pos.offset(dir.getOpposite().getNormal()))) {
            return InteractionResult.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    protected boolean giveSyrup(Level world, BlockPos pos, BlockState state, Player player, BlockPos treePos) {
        Species species = TreeHelper.getExactSpecies(world, treePos);
        if (species == Species.NULL_SPECIES) return false;
        int filling = world.getBlockState(pos).getValue(FILLING);
        if (filling > 0) {
            if (!world.isClientSide() && !world.restoringBlockSnapshots) {
                int count = (filling + (filling == maxFilling ? 1 : 0)); //Adds one bonus syrup if collected when its full
                ItemStack drop = new ItemStack(getSyrupItem(species), count);
                world.addFreshEntity(new ItemEntity(world, pos.getX()+0.5f, pos.getY()+0.1f, pos.getZ()+0.5f, drop));
            }
            world.playSound(null, pos, SoundEvents.HONEY_DRINK, SoundSource.BLOCKS, 1, 2 - filling / 3f);
            if (filling == maxFilling) world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.5f, 0.8f);
            world.setBlock(pos, state.setValue(FILLING, 0), 3);
            return true;
        }
        return false;
    }

    @Override
    public Item asItem() {
        return Items.BUCKET;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        return pBlockState.getValue(FILLING);
    }

}
