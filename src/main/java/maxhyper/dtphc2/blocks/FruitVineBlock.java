package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.util.BlockStates;
import com.ferreusveritas.dynamictrees.util.WorldContext;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import static com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper.isSeasonBetween;

public class FruitVineBlock extends VineBlock {

    public static final int maxAge = 4;

    private static final float baseFruitingChance = 0.002f;
    private static float fruitGrowChance = 0.2f;
    private static float fruitOverripenChance = 0.005f;

    private static final float vineSpreadUpChance = 0.005f;
    private static final float attemptSpread = 0.01f;

    public static final IntegerProperty ageProperty = IntegerProperty.create("age", 0, maxAge);

    private ItemStack fruitStack;
    private ItemStack overripeFruitStack;

    //private Integer fruitingOffset;
    private int matureAge = maxAge;

    @Nullable
    private Float seasonOffset = 0f;

    private float flowerHoldPeriodLength = 0.5F;

    private float minProductionFactor = 0.3F;

    private int maxFruitsAround = 2;

    public FruitVineBlock() {
        super(AbstractBlock.Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().strength(0.2F).sound(SoundType.VINE));
        registerDefaultState(defaultBlockState().setValue(ageProperty, 0));
    }

    public void setAge(World world, BlockPos pos, BlockState state, int age, boolean destroy) {
        state = state.setValue(ageProperty, age);
        //Spawn breaking particles and breaking sound
        if (destroy) world.levelEvent(2001, pos, Block.getId(state));
        world.setBlock(pos, state, 2);
    }

    public FruitVineBlock setMatureAge(int age) {
        if (age <= maxAge && age > 0)
            matureAge = age;
        return this;
    }
    public FruitVineBlock setFruitGrowChance(float chance) {
        if (chance <= 1 && chance >= 0)
            fruitGrowChance = chance;
        return this;
    }
    public void setMaxFruitsAround(int maxFruitsAround) {
        this.maxFruitsAround = maxFruitsAround;
    }
    public void setFlowerHoldPeriodLength(float flowerHoldPeriodLength) {
        this.flowerHoldPeriodLength = flowerHoldPeriodLength;
    }
    public void setMinProductionFactor(float minProductionFactor) {
        this.minProductionFactor = minProductionFactor;
    }
    public FruitVineBlock setFruitOverripenChance(float chance) {
        if (chance <= 1 && chance >= 0)
            fruitOverripenChance = chance;
        return this;
    }
    public FruitVineBlock setFruitStack(ItemStack stack) {
        fruitStack = stack;
        return this;
    }
    public FruitVineBlock setOverripeFruitStack(ItemStack stack) {
        overripeFruitStack = stack;
        return this;
    }
    public FruitVineBlock setSeasonOffset(Float seasonOffset){
        this.seasonOffset = seasonOffset;
        return this;
    }

    public Float getSeasonOffset (){
        return seasonOffset;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ageProperty);
    }

    @Override
    public Item asItem() {
        return super.asItem();
    }

    public void doTick(BlockState state, World world, BlockPos pos, Random random) {
        final Integer age = getAge(state);
        if (age == null) return;
        final Float season = SeasonHelper.getSeasonValue(WorldContext.create(world), pos);

        if (season != null) { // Non-Null means we are season capable.
            if (isOutOfSeason(world, pos)) {
                this.outOfSeason(world, pos, state); // Destroy the block or similar action.
                return;
            }
            if (age == 1 && isInFlowerHoldPeriod(world, pos, season)) {
                return;
            }
        }
        if (age < maxAge) {
            tryGrow(state, world, pos, random, age, season);
        }
    }

    private void tryGrow(BlockState state, World world, BlockPos pos, Random random, int age,
                         @Nullable Float season) {
        float chance = age == 0 ? getFruitingChance(world, pos)
                : ((matureAge != maxAge && age >= matureAge) ? fruitOverripenChance : fruitGrowChance);

        final boolean doGrow = random.nextFloat() < chance;
        final boolean eventGrow = ForgeHooks.onCropsGrowPre(world, pos, state, doGrow);
        // Prevent a seasons mod from canceling the growth, we handle that ourselves.
        if (season != null ? doGrow || eventGrow : eventGrow) {
            //We look for fruit blocks around. If there is more than two we cancel the fruit growth
            int fruitFoundAround = 0;
            for (Direction dir : Direction.values()) {
                Integer sideAge = getAge(world.getBlockState(pos.offset(dir.getNormal())));
                if (sideAge != null && sideAge > 0) {
                    fruitFoundAround++;
                }
            }
            if (fruitFoundAround >= maxFruitsAround) {
                //changeVineWithProperties(world, pos, getStateFromAge(0), state);
                return;
            }
            setAge(world, pos, state, age + 1, false);
            //changeVineWithProperties(worldIn, pos, getStateFromAge(age + 1), state);
            ForgeHooks.onCropsGrowPost(world, pos, state);
        }
    }

    public float seasonalFruitProductionFactor(WorldContext worldContext, BlockPos pos) {
        return seasonOffset != null ?
                SeasonHelper.globalSeasonalFruitProductionFactor(worldContext, pos, -seasonOffset, false)
                : 1.0F;
    }

    private boolean isOutOfSeason(World world, BlockPos pos) {
        return seasonalFruitProductionFactor(WorldContext.create(world), pos) < minProductionFactor;
    }

    private void outOfSeason(World world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state.setValue(ageProperty, 0), 2);
    }

    public final boolean isInFlowerHoldPeriod(IWorld world, BlockPos rootPos, Float seasonValue) {
        if (seasonOffset == null) {
            return false;
        }
        final Float peakSeasonValue = SeasonHelper.getSeasonManager()
                .getPeakFruitProductionSeasonValue(WorldContext.create(world).level(), rootPos, seasonOffset);
        if (peakSeasonValue == null || flowerHoldPeriodLength == 0.0F) {
            return false;
        }
        final float min = peakSeasonValue - 1.5F;
        final float max = min + flowerHoldPeriodLength;
        return isSeasonBetween(seasonValue, min, max);
    }

    private float getFruitingChance(World world, BlockPos pos) {
        if (seasonOffset == null) return baseFruitingChance;
        float fruitFactor = SeasonHelper.globalSeasonalFruitProductionFactor(WorldContext.create(world), pos, seasonOffset, true);
        return baseFruitingChance * Math.max((fruitFactor + 0.25f), 1);
    }

    public Integer getAge(BlockState state) {
        if (!state.hasProperty(ageProperty)) return null;
        return state.getValue(ageProperty);
    }

    public int getMatureAge() {
        return matureAge;
    }

    @Nonnull
    private BlockState getStateFromAge(int age) {
        return defaultBlockState().setValue(ageProperty, age);
    }

    @Nullable
    private ItemStack getFruit() {
        if (fruitStack == null) return null;
        return fruitStack.copy();
    }

    @Nullable
    private ItemStack getOverripeFruit() {
        if (overripeFruitStack == null) return null;
        return overripeFruitStack.copy();
    }

    private boolean spawnItemFruitIfRipe(World world, BlockPos pos, BlockState state) {
        Integer age = getAge(state);
        if (!world.isClientSide() && age != null) {
            if (age >= matureAge) {
                ItemStack fruit = (age == matureAge) ? getFruit() : getOverripeFruit();
                if (fruit == null) return false;
                world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, fruit));
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                                BlockRayTraceResult hit) {
        Integer age = getAge(state);
        if (age == null) return ActionResultType.PASS;
        // Drop fruit if mature.
        if (age >= matureAge) {
            if (spawnItemFruitIfRipe(world, pos, state)) {
                setAge(world, pos, state, 0, true);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean canSupportAtFace(IBlockReader pLevel, BlockPos pPos, Direction pDirection) {
        if (pDirection == Direction.DOWN) {
            return false;
        } else {
            BlockPos blockpos = pPos.relative(pDirection);
            if (isAcceptableNeighbour(pLevel, blockpos, pDirection)) {
                return true;
            } else if (pDirection.getAxis() == Direction.Axis.Y) {
                return false;
            } else {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(pDirection);
                BlockState blockstate = pLevel.getBlockState(pPos.above());
                return blockstate.is(this) && blockstate.getValue(booleanproperty);
            }
        }
    }

    public static boolean isAcceptableNeighbour(IBlockReader pBlockReader, BlockPos pLevel, Direction pNeighborPos) {
        BlockState blockstate = pBlockReader.getBlockState(pLevel);
        return Block.isFaceFull(blockstate.getCollisionShape(pBlockReader, pLevel), pNeighborPos.getOpposite()) || TreeHelper.isBranch(blockstate);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        doTick(state, world, pos, random);

        if (world.random.nextFloat() < attemptSpread && world.isAreaLoaded(pos, 4)) { // Forge: check area to prevent loading unloaded chunks
            Direction randDir = Direction.getRandom(random);
            BlockPos upPos = pos.above();
            if (randDir.getAxis().isHorizontal() && !state.getValue(getPropertyForFace(randDir))) {
                if (this.canSpread(world, pos)) {
                    BlockPos offsetPos = pos.relative(randDir);
                    BlockState offsetState = world.getBlockState(offsetPos);
                    if (offsetState.isAir(world, offsetPos)) {
                        Direction rightDir = randDir.getClockWise();
                        Direction leftDir = randDir.getCounterClockWise();
                        boolean hasFaceRight = state.getValue(getPropertyForFace(rightDir));
                        boolean hasFaceLeft = state.getValue(getPropertyForFace(leftDir));
                        BlockPos rightPos = offsetPos.relative(rightDir);
                        BlockPos leftPos = offsetPos.relative(leftDir);
                        if (hasFaceRight && isAcceptableNeighbour(world, rightPos, rightDir)) {
                            world.setBlock(offsetPos, this.defaultBlockState().setValue(getPropertyForFace(rightDir), true), 2);
                        } else if (hasFaceLeft && isAcceptableNeighbour(world, leftPos, leftDir)) {
                            world.setBlock(offsetPos, this.defaultBlockState().setValue(getPropertyForFace(leftDir), true), 2);
                        } else {
                            Direction oppositeDir = randDir.getOpposite();
                            if (hasFaceRight && world.isEmptyBlock(rightPos) && isAcceptableNeighbour(world, pos.relative(rightDir), oppositeDir)) {
                                world.setBlock(rightPos, this.defaultBlockState().setValue(getPropertyForFace(oppositeDir), true), 2);
                            } else if (hasFaceLeft && world.isEmptyBlock(leftPos) && isAcceptableNeighbour(world, pos.relative(leftDir), oppositeDir)) {
                                world.setBlock(leftPos, this.defaultBlockState().setValue(getPropertyForFace(oppositeDir), true), 2);
                            } else if (world.random.nextFloat() < vineSpreadUpChance && isAcceptableNeighbour(world, offsetPos.above(), Direction.UP)) {
                                world.setBlock(offsetPos, this.defaultBlockState().setValue(UP, true), 2);
                            }
                        }
                    } else if (isAcceptableNeighbour(world, offsetPos, randDir)) {
                        world.setBlock(pos, state.setValue(getPropertyForFace(randDir), true), 2);
                    }

                }
            } else {
                if (randDir == Direction.UP && pos.getY() < 255) {
                    if (this.canSupportAtFace(world, pos, randDir)) {
                        world.setBlock(pos, state.setValue(UP, true), 2);
                        return;
                    }

                    if (world.isEmptyBlock(upPos)) {
                        if (!this.canSpread(world, pos)) {
                            return;
                        }

                        BlockState blockstate3 = state;

                        for(Direction direction2 : Direction.Plane.HORIZONTAL) {
                            if (random.nextBoolean() || !isAcceptableNeighbour(world, upPos.relative(direction2), Direction.UP)) {
                                blockstate3 = blockstate3.setValue(getPropertyForFace(direction2), false);
                            }
                        }

                        if (this.hasHorizontalConnection(blockstate3)) {
                            world.setBlock(upPos, blockstate3, 2);
                        }

                        return;
                    }
                }

                if (pos.getY() > 0) {
                    BlockPos blockpos1 = pos.below();
                    BlockState blockstate = world.getBlockState(blockpos1);
                    boolean isAir = blockstate.isAir(world, blockpos1);
                    if (isAir || blockstate.is(this)) {
                        BlockState blockstate1 = isAir ? this.defaultBlockState() : blockstate;
                        BlockState blockstate2 = this.copyRandomFaces(state, blockstate1, random);
                        if (blockstate1 != blockstate2 && this.hasHorizontalConnection(blockstate2)) {
                            world.setBlock(blockpos1, blockstate2, 2);
                        }
                    }
                }

            }
        }
    }

}
