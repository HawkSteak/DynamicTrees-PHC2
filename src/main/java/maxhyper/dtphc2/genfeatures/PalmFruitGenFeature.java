package maxhyper.dtphc2.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.WorldContext;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class PalmFruitGenFeature extends GenFeature {

    public static final ConfigurationProperty<Pod> POD =
            ConfigurationProperty.property("pod", Pod.class);

    public static final ConfigurationProperty<Integer> EXPAND_UP_FRUIT_HEIGHT = ConfigurationProperty.integer("expand_up_fruit_height");
    public static final ConfigurationProperty<Integer> EXPAND_DOWN_FRUIT_HEIGHT = ConfigurationProperty.integer("expand_down_fruit_height");

        /*

     -v-      -v-      -v-
    / v \    /ovo\    / v \
     oIo      oIo      oIo
      I        I       oIo
      I        I        I
      I        I        I
      I        I        I

     */

    public PalmFruitGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(POD, QUANTITY, FRUITING_RADIUS, PLACE_CHANCE, EXPAND_UP_FRUIT_HEIGHT, EXPAND_DOWN_FRUIT_HEIGHT);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(POD, Pod.NULL)
                .with(QUANTITY, 8)
                .with(FRUITING_RADIUS, 6)
                .with(PLACE_CHANCE, 0.25f)
                .with(EXPAND_UP_FRUIT_HEIGHT, 0)
                .with(EXPAND_DOWN_FRUIT_HEIGHT, 0);
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        final IWorld world = context.world();
        final BlockPos rootPos = context.pos();
        if ((TreeHelper.getRadius(world, rootPos.above()) >= configuration.get(FRUITING_RADIUS)) && context.natural() &&
                world.getRandom().nextInt() % 16 == 0) {
            if (context.species().seasonalFruitProductionFactor(WorldContext.create(world), rootPos) > world.getRandom().nextFloat()) {
                addFruit(configuration, world, rootPos, getLeavesHeight(rootPos, world).below(), false);
                return true;
            }
        }
        return false;
    }

    private BlockPos getLeavesHeight(BlockPos rootPos, IWorld world) {
        for (int y = 1; y < 20; y++) {
            BlockPos testPos = rootPos.above(y);
            if ((world.getBlockState(testPos).getBlock() instanceof LeavesBlock)) {
                return testPos;
            }
        }
        return rootPos;
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        final IWorld world = context.world();
        final BlockPos rootPos = context.pos();
        boolean placed = false;
        int qty = configuration.get(QUANTITY);
        qty *= context.fruitProductionFactor();
        for (int i = 0; i < qty; i++) {
            if (!context.endPoints().isEmpty() && world.getRandom().nextFloat() <= configuration.get(PLACE_CHANCE)) {
                addFruit(configuration, world, rootPos, context.endPoints().get(0), true);
                placed = true;
            }
        }
        return placed;
    }

    protected void addFruit(GenFeatureConfiguration configuration, IWorld world, BlockPos rootPos, BlockPos leavesPos, boolean worldGen) {
        if (rootPos.getY() == leavesPos.getY()) return;
        Direction placeDir = CoordUtils.HORIZONTALS[world.getRandom().nextInt(4)];
        BlockPos pos = expandRandom(configuration, world, leavesPos.offset(placeDir.getNormal()));
        if (world.getBlockState(pos).getMaterial().isReplaceable()) {
            Float seasonValue = SeasonHelper.getSeasonValue(world, rootPos);
            Pod pod = configuration.get(POD);
            if (worldGen) {
                pod.placeDuringWorldGen(world, pos, seasonValue, placeDir.getOpposite());
            } else {
                pod.place(world, pos, seasonValue, placeDir.getOpposite());
            }
        }
    }

    protected BlockPos expandRandom(GenFeatureConfiguration configuration, IWorld world, BlockPos startingPos){
        int fullHeight = 1+configuration.get(EXPAND_UP_FRUIT_HEIGHT)+configuration.get(EXPAND_DOWN_FRUIT_HEIGHT);
        return startingPos.below(configuration.get(EXPAND_DOWN_FRUIT_HEIGHT))
                .above(world.getRandom().nextInt(fullHeight));
    }

}