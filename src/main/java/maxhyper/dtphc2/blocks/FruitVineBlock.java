package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.util.WorldContext;
import maxhyper.dtphc2.DynamicTreesPHC2;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import static com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper.isSeasonBetween;

public class FruitVineBlock extends VineBlock {


    private static final float baseFruitingChance = 0.002f;
    private static final float fruitGrowChance = 0.2f;
    private static final float fruitOverripenChance = 0.02f;
    private static final int maxLength = 3;
    private static final int maxAge = 4;

    private static final IntegerProperty ageProperty = IntegerProperty.create("age", 0, maxAge);

    private ItemStack fruitStack;
    private ItemStack overripeFruitStack;
    //private Integer fruitingOffset;
    private int matureAge = maxAge;

    @Nullable
    private final Float seasonOffset = 0f;

    private float flowerHoldPeriodLength = 0.5F;

    private float minProductionFactor = 0.3F;

    public FruitVineBlock() {
        super(AbstractBlock.Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().strength(0.2F).sound(SoundType.VINE));
        registerDefaultState(defaultBlockState().setValue(ageProperty, 0));
    }

    public void setAge(World world, BlockPos pos, BlockState state, int age, boolean destroy) {
        state = state.setValue(ageProperty, age);
        //LogManager.getLogger(DynamicTreesPHC2.MOD_ID).info("Set Block at " + pos.toString() + " age to " + age);
        if (destroy) world.destroyBlock(pos, false);
        world.setBlock(pos, state, 2);
    }

    public FruitVineBlock setMatureAge(int age) {
        if (age <= maxAge && age > 0)
            matureAge = age;
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

//    public FruitVineBlock setFruitingOffset(Integer offset) {
//        fruitingOffset = offset;
//        return this;
//    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ageProperty);
    }


//    private float getFruitOffset() {
//        return fruitingOffset;
//    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        doTick(state, world, pos, random);
    }

    public void doTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!this.canSurvive(state, world, pos)) {
            drop(world, pos, state);
            return;
        }

        final Integer age = getAgeFromState(state);
        if (age == null) return;
        final Float season = SeasonHelper.getSeasonValue(WorldContext.create(world), pos);

        if (season != null) { // Non-Null means we are season capable.
            if (isOutOfSeason(world, pos)) {
                this.outOfSeason(world, pos); // Destroy the block or similar action.
                return;
            }
            if (age == 0 && isInFlowerHoldPeriod(world, pos, season)) {
                return;
            }
        }

        else if (age < maxAge) {
            if (matureAge != maxAge && age >= matureAge) {

            }
            tryGrow(state, world, pos, random, age, season);
        }
    }

    protected void drop(World world, BlockPos pos, BlockState state) {

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
                Integer sideAge = getAgeFromState(world.getBlockState(pos.offset(dir.getNormal())));
                if (sideAge != null && sideAge > 0) {
                    fruitFoundAround++;
                }
            }
            if (fruitFoundAround >= 2) {
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

    private void outOfSeason(World world, BlockPos pos) {

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
        float fruitFactor = SeasonHelper.globalSeasonalFruitProductionFactor(WorldContext.create(world), pos, seasonOffset, true);
        return baseFruitingChance * Math.max((fruitFactor + 0.25f), 1);
    }

    private void changeVineWithProperties(World world, BlockPos pos, BlockState baseState, BlockState stateWithWantedProperties) {
        BlockState state = baseState.setValue(UP, stateWithWantedProperties.getValue(UP))
                .setValue(NORTH, stateWithWantedProperties.getValue(NORTH))
                .setValue(WEST, stateWithWantedProperties.getValue(WEST))
                .setValue(SOUTH, stateWithWantedProperties.getValue(SOUTH))
                .setValue(EAST, stateWithWantedProperties.getValue(EAST));
        world.setBlock(pos, state, 2);
    }

    private Integer getAgeFromState(BlockState state) {
        if (!state.hasProperty(ageProperty)) return null;
        return state.getValue(ageProperty);
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
        Integer age = getAgeFromState(state);
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
        Integer age = getAgeFromState(state);
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


}
