package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import static maxhyper.dtphc2.init.DTPHC2Registries.MAPLE_SPILE_BUCKET_BLOCK;

public class MapleSpileBlock extends HorizontalBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty FILLED = BooleanProperty.create("filled");

    private static final double chanceToBreak = 0.02D;

    private static VoxelShape makeShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.625, -0.0625, 0.5625, 0.75, 0.25), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.625, 0.25, 0.5625, 0.6875, 0.375), IBooleanFunction.OR);

        return shape;
    }

    private static final VoxelShape SHAPE = makeShape();

    public MapleSpileBlock() {
        super(Properties.of(Material.METAL).sound(SoundType.METAL).strength(0.5f).randomTicks());
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FILLED, false));
        //registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(FILLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(FILLED);
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        }
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
        }
        if (giveSyrup(world, pos, state, player)) {
            return ActionResultType.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    protected boolean giveSyrup(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (state.getValue(FILLED)) {
            if (world.isClientSide() && !world.restoringBlockSnapshots) {
                //TODO: Make dynamic
                ResourceLocation mapleSyrupRes = new ResourceLocation("pamhc2trees", "maplesyrupitem");
                Item mapleSyrup = ForgeRegistries.ITEMS.getValue(mapleSyrupRes);
                player.addItem(new ItemStack(mapleSyrup));
            }
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1f, false);
            world.setBlock(pos, state.setValue(FILLED, false), 3);
            return true;
        }
        return false;
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

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(Items.IRON_INGOT);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

}
