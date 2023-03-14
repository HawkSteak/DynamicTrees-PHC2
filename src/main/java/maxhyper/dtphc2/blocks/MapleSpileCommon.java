package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import static maxhyper.dtphc2.init.DTPHC2Registries.MAPLE_SPILE_BUCKET_BLOCK;

public abstract class MapleSpileCommon extends HorizontalBlock {

    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    public static final int maxFilling = 3;
    public static final IntegerProperty FILLING = IntegerProperty.create("filling", 0, maxFilling);

    protected static final double chanceToBreak = 0.02D;

    static VoxelShape makeShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.625, -0.0625, 0.5625, 0.75, 0.25), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.625, 0.25, 0.5625, 0.6875, 0.375), IBooleanFunction.OR);
        return shape;
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        int times = (to.ordinal() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1], VoxelShapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    public MapleSpileCommon() {
        super(Properties.of(Material.METAL).sound(SoundType.METAL).strength(0.5f).randomTicks());
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

    protected abstract boolean giveSyrup(World world, BlockPos pos, BlockState state, PlayerEntity player);

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        BlockState blockstate = this.defaultBlockState();
        IWorldReader iworldreader = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();

        for (Direction direction : pContext.getNearestLookingDirections()) {
            direction = direction.getOpposite();
            if (direction.getAxis().isHorizontal()) {
                blockstate = blockstate.setValue(FACING, direction);
                if (canSurvive(blockstate, iworldreader, blockpos)) {
                    return blockstate;
                }
            }
        }

        return blockstate.setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(@Nonnull BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random random) {
        if (!this.canBlockStay(world, pos, state)) {
            this.dropBlock(world, pos, state);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean pIsMoving) {
        if (!this.canBlockStay(world, pos, state)) {
            this.dropBlock(world, pos, state);
        }
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

    public boolean canBlockStay(IWorldReader worldIn, BlockPos pos, BlockState state) {
        BlockPos offsetPos = pos.relative(state.getValue(FACING).getOpposite());
        BlockState offsetState = worldIn.getBlockState(offsetPos);
        if (TreeHelper.isBranch(offsetState)) {
            BranchBlock branch = TreeHelper.getBranch(offsetState);

            //TODO: Make dynamic
            ResourceLocation mapleLogRes = new ResourceLocation("pamhc2trees", "pammaple");
            Block mapleLog = ForgeRegistries.BLOCKS.getValue(mapleLogRes);
            if (branch != null && branch.getFamily().getPrimitiveLog().isPresent()) {
                return branch.getFamily().getPrimitiveLog().get() == mapleLog;
            }
//            return branch.getFamily().getPrimitiveLog() == FruitRegistry.getLog(FruitRegistry.MAPLE) &&
//                    branch.getRadius(offsetState) >= 7;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        return canBlockStay(world, pos, state);
    }

//    @SuppressWarnings("deprecation")
//    @Override
//    @Nonnull
//    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
//        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
//    }

}
